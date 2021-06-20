package org.grails.plugin.payment.paypal

import com.paypal.api.payments.*
import grails.core.GrailsApplication
import grails.gorm.transactions.Transactional
import org.grails.plugin.payment.PaymentItem
import org.grails.plugin.payment.beans.CheckoutBean

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
                storedPayment?.postalAddress?.countryCode =  org.grails.plugin.payment.enums.CountryCode.valueOf(storedPayment?.postalAddress?.country)?.toString()
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

    def execute(){
       // def apiContext = paypalService.APIContext
        def cfg = paypalService.configuration
        def accessToken = paypalService.getAccessToken(cfg.clientId,cfg.clientSecret,cfg.sdkConfig)
        def apiContext = paypalService.getAPIContext(accessToken,cfg.sdkConfig)
        PaypalPayment dbPayment = PaypalPayment?.findByPaypalTransactionId(params.paymentId as String)
        params.realPaymentId = dbPayment?.id
        try {
            def paypalPayment = paypalService.createPaymentExecution(['paymentId': params.paymentId, 'payerId': params?.PayerID], apiContext)
            if (paypalPayment) {
                dbPayment = paymentService.updatePaypalStatusAndDetails(params.paymentId as String,  paypalPayment)
                session.cart = null
                session.cartCounter = null
                chain(action: 'thanks', params: params)
                return
            }
        } catch (Throwable t) {
            //println "-- e = ${t.stackTrace}"
            log.error t.stackTrace
        }
        render view: 'cancel', model:[payment:dbPayment]
        return
    }

    def cancel() {
        PaypalPayment payment
        if (params.realPaymentId) {
            payment = PaypalPayment.get(params.realPaymentId as Long)
        }
        if (!payment && session.paymentId) {
            payment = PaypalPayment.get(session?.paymentId as Long)
        }
        render view: 'cancel', model:[payment:payment]
        return
    }

    def thanks() {
        PaypalPayment payment
        if (params.realPaymentId) {
            payment = PaypalPayment.get(params.realPaymentId as Long)
        }
        render view: 'success', model: [payment:payment]
    }

}