package org.grails.plugin.payment.paypal

import com.paypal.api.payments.*
import com.paypal.http.HttpResponse
import com.paypal.http.exceptions.HttpException
import com.paypal.orders.AddressPortable
import com.paypal.orders.LinkDescription
import com.paypal.orders.MerchantReceivableBreakdown
import com.paypal.orders.OrdersCaptureRequest
import com.paypal.orders.OrdersGetRequest
import com.paypal.orders.PaymentCollection
import com.paypal.orders.PurchaseUnit
import grails.core.GrailsApplication
import grails.gorm.transactions.Transactional
import org.grails.plugin.payment.PaymentItem
import org.grails.plugin.payment.beans.CheckoutBean
import org.grails.plugin.payment.listeners.PaymentConfigListener
import org.grails.web.json.JSONObject

@Transactional(readOnly = true)
class PaypalController {

    GrailsApplication grailsApplication
    def paypalService
    def paymentService

    def uploadCart(CheckoutBean bean) {
        PaypalPayment storedPayment = bean.payment

        def cfg = paypalService.configuration
        def accessToken = paypalService.getAccessToken(cfg.clientId,cfg.clientSecret,cfg.sdkConfig)
        def apiContext = paypalService.getAPIContext(accessToken,cfg.sdkConfig)

        List<Item> items =[]
        storedPayment.paymentItems.each { PaymentItem paymentItem ->
            def itemTransaction = paypalService.createItem([
                    'name':paymentItem.itemName,
                    'price':  paymentItem.amount?.toString(),
                    'quantity': paymentItem.quantity?.toString(),
                    'currency': paymentItem.payment.currency?.toString(),
                    'description': paymentItem.description])
            items.add(itemTransaction)
        }

        Map addressInfo=[:]
        if (storedPayment && storedPayment?.postalAddress ) {
            if (storedPayment?.postalAddress?.line2) {
                addressInfo.line1 = storedPayment?.postalAddress?.line1+' '+storedPayment?.postalAddress?.line2
            } else {
                addressInfo.line1 = storedPayment?.postalAddress?.line1
            }
            if (!storedPayment?.postalAddress?.countryCode && storedPayment?.postalAddress?.country) {
                storedPayment?.postalAddress?.countryCode = PaymentConfigListener.countryCode?.toString()?:
                        org.grails.plugin.payment.enums.CountryCode.values()?.find{it.name == storedPayment?.postalAddress?.country}?.toString()
            }
            addressInfo.countryCode = storedPayment?.postalAddress?.countryCode
            addressInfo.city = storedPayment.postalAddress?.city
            addressInfo.postalCode = storedPayment.postalAddress?.postcode
            //addressInfo.email = storedPayment.postalAddress?.emailAddress
            addressInfo.state = ''
        }

        Map userInfo =[:]
        if (storedPayment.postalAddress?.title) {
            userInfo.salutation =  storedPayment.postalAddress?.title.toString()
        }
        if (storedPayment.postalAddress?.firstName) {
            userInfo.firstName =  storedPayment.postalAddress?.firstName
        }
        if (storedPayment.postalAddress?.lastName) {
            userInfo.lastName =  storedPayment.postalAddress?.lastName
        }
        if (storedPayment.postalAddress?.emailAddress) {
            userInfo.email =  storedPayment.postalAddress?.emailAddress
        }

        if (storedPayment.postalAddress?.countryCode) {
            userInfo.countryCode =  storedPayment.postalAddress?.countryCode
        }
        if (addressInfo.line1) {
            userInfo.billingAddress =  paypalService.createAddress(addressInfo)
        }

        def shippingAddress = paypalService.createShippingAddress(addressInfo)
        ItemList itemList = paypalService.createItemList(['items':items, 'shippingAddress':shippingAddress ])

        PayerInfo payerInfo = paypalService.createPayerInfo(userInfo)
        Payer payer = paypalService.createPayer(['paymentMethod': 'paypal', 'payerInfo': payerInfo])

        def transactionDetails = paypalService.createDetails(['subtotal': storedPayment.gross])
        def transactionAmount = paypalService.createAmount(['currency': storedPayment.currency.toString(),
                                                            'total': storedPayment.gross, 'details': transactionDetails])

        Transaction transaction = paypalService.createTransaction(['amount': transactionAmount,
                                                                   'description': "Final total", 'itemList':itemList])
        //Map cfg = paypalService.configuration
        def cancelUrl = cfg.hostName + "/paypal/cancel";
        def returnUrl = cfg.hostName + "/paypal/execute";

        def redirectUrls = paypalService.createRedirectUrls(['cancelUrl': cancelUrl, 'returnUrl': returnUrl])

        def payment
        try {
            // create the paypal payment
            payment = paypalService.createPayment(['payer'            : payer, 'intent': 'sale'
                                                   , 'transactionList': [transaction], 'redirectUrls': redirectUrls
                                                   , 'apiContext'     : apiContext])

            paymentService.updatePaypalDetails(payment, storedPayment?.id as Long)

        }
        catch (Exception ex) {
            log.error ex.message, ex.stackTrace
            String msg = ex.getMessage()
            flash.message = "${g.message(code: 'paypal.failed')} ${msg ? msg : ''}"
            chain controller: 'payment', action: "checkout"
            return
        }
        def approvalUrl = ""
        def retUrl = ""
        // retrieve links from returned paypal object
        payment?.links.each {
            if (it?.rel == 'approval_url') {
                approvalUrl = it.href
            }
            if (it?.rel == 'return_url') {
                retUrl = it.href
            }
        }
        redirect url: approvalUrl ? approvalUrl : '/', method: 'POST'
    }

    def error() {
        render view: 'cancel'
    }

    def executeJSON() {
        def jsonParams = request.JSON
        def cfg = paypalService.configuration
        String orderId = jsonParams?.id
        // String finalPaidTotal = jsonParams?.purchase_units?.amount?.value
        // String currencyCode = jsonParams?.purchase_units?.amount?.currency_code
        // List jsonParamItems =  jsonParams?.purchase_units?.items
        // String paymentId = jsonParams?.purchase_units?.payments[0]?.captures[0]?.id
        // String payerId = jsonParams?.payer?.payer_id
        // String status = jsonParams?.status

        int status=200
        String text='complete'
        com.paypal.orders.Order order
        try {
            OrdersGetRequest ordersGetRequest = new OrdersGetRequest(orderId)
            HttpResponse<Order> orderResponse = cfg.client.execute(ordersGetRequest)
            order = orderResponse.result()

            PaypalPayment payment = paymentService.updatePaypalFromCheckout((String)session?.currencyCode, (BigDecimal)session?.finalTotal,
                    (String) jsonParams?.emailAddress,
                    order.payer,
                    order.purchaseUnits().get(0))
            if (payment) {
                session.paymentId = payment?.id
                session.user = payment.user
                if (!payment.completed) {
                    status = 404
                    text='failed'
                    //flash.message = "Payment amount of ${payment.gross} does not match expected  ${session.finalTotal} "
                }
            } else {
                status = 404
                text='failed'
            }
        }
        catch (IOException ioe) {
            status = 404
            text='failed'
            if (ioe instanceof HttpException) {
                HttpException he = (HttpException) ioe;
                log.error he.getMessage()
                // he.headers().forEach { x -> System.out.println(x + " :" + he.headers().header(x)) }
            } else {
                log.error ioe.getMessage()
            }
            flash.message = "execute exception: ${ioe.getMessage()}."
            //chain controller: 'payment', action: "checkout"
            //return
        }

        render text: text, status:status
        return
    }

    def execute(){
       // def apiContext = paypalService.APIContext
        def cfg = paypalService.configuration
        def accessToken = paypalService.getAccessToken(cfg.clientId,cfg.clientSecret,cfg.sdkConfig)
        def apiContext = paypalService.getAPIContext(accessToken,cfg.sdkConfig)
        PaypalPayment dbPayment = PaypalPayment?.findByPaypalTransactionId(params.paymentId as String)
        //params.realPaymentId = dbPayment?.id
        session.paymentId = dbPayment?.id
        try {
            def paypalPayment = paypalService.createPaymentExecution(['paymentId': params.paymentId, 'payerId': params?.PayerID], apiContext)
            if (paypalPayment) {
                dbPayment = paymentService.updatePaypalStatusAndDetails(params.paymentId as String,  paypalPayment)
                session.cart = null
                session.user = dbPayment.user
                session.cartCounter = null
                chain(action: 'thanks', params: params)
                return
            }
        } catch (Throwable t) {
            log.error t.getMessage()
            flash.message = "execute exception: ${t.getMessage()}."
            chain controller: 'payment', action: "checkout"
            return
        }
        render view: 'cancel', model:[payment:dbPayment]
        return
    }


    def cancel() {
        PaypalPayment payment
        //if (params.realPaymentId) {
          //  payment = PaypalPayment.get(params.realPaymentId as Long)
        //}
        if (session.paymentId) {
            payment = PaypalPayment.get(session?.paymentId as Long)
        }
        if (payment && payment.user == session.user) {
            render view: 'cancel', model:[payment:payment]
            return
        }
        render view: 'cancel', model: [payment:[:]]
        return
    }

    def thanks() {
        PaypalPayment payment
        //if (params.realPaymentId) {
         //   payment = PaypalPayment.get(params.realPaymentId as Long)
       // }
        if (session.paymentId) {
            payment = PaypalPayment.get(session?.paymentId as Long)
        }
        if (payment ) {
            //&&
        //} payment.user == session.user) {
            render view: 'success', model: [payment:payment]
            return
        }
        render view: 'cancel', model: [payment:[:]]
        return
    }

}