package org.grails.plugin.payment

import com.squareup.square.api.PaymentsApi
import com.squareup.square.exceptions.ApiException
import com.squareup.square.models.*
import com.stripe.Stripe
import com.stripe.model.Card
import com.stripe.model.Charge
import com.stripe.model.Customer
import org.grails.plugin.payment.beans.CartBean
import org.grails.plugin.payment.beans.StripeBean
import org.grails.plugin.payment.listeners.PaymentConfigListener
import org.grails.plugin.payment.paypal.PaypalService
import org.grails.plugin.payment.square.SquarePayment
import org.grails.plugin.payment.square.SquareService
import org.grails.plugin.payment.stripe.StripePayment
import org.grails.plugin.payment.stripe.StripeService
import org.grails.plugin.payment.util.PaymentHelper

class PaymentController {

    static defaultAction = "index"
    PaymentService paymentService
    StripeService stripeService
    SquareService squareService
    def userService
    PaypalService paypalService

    private String getSECRET_KEY() {
        return stripeService.configuration.stripeSecretKey
    }

    private String getPUBLIC_KEY() {
        return stripeService.configuration.stripePublishableKey
    }

    private def getIfCheckoutEnabled() {
        if (!PaymentConfigListener.paymentCheckoutEnabled) {
            redirect(controller:'payment', action:'index')
            return
        }
    }
    private def getIfPaypalEnabled() {
        if (!PaymentConfigListener.paypalEnabled) {
            redirect(controller:'payment', action:'index')
            return
        }
    }

    private def getIfStripeEnabled() {
        if (!PaymentConfigListener.stripeEnabled) {
            redirect(controller:'payment', action:'index')
            return
        }
    }

    private def getIfSquareEnabled() {
        if (!PaymentConfigListener.squareEnabled) {
            redirect(controller:'payment', action:'index')
            return
        }
    }

    def index() {

    }

    /**
     * Thanks is called by stripe and square final actions
     * @param provider
     * @return
     */
    def thanks(String provider) {
        def payment
        String view = provider+'FailedPayment'
        if (session.paymentId) {
            if (provider=='stripe') {
                payment = StripePayment.get(session.paymentId)
            } else if (provider == 'square') {
                payment = SquarePayment.get(session.paymentId)
            }
            if (payment && payment?.user) {
                //TODO - PLUGIN does not have a userService this is for your app !!!!!
                if (userService && userService?.currentUser) {
                    // this is when current user is actually logged in
                    // we check to see if payment.user matches currentUser -
                    //
                    if (payment?.userId != userService.currentUser?.id) {
                        render view: view
                        return
                    }
                } else if (session.currentUser) {
                    //non auth user but actual user who paid for something
                    if (payment?.user?.id != session.currentUser?.id) {
                        render view: view
                        return
                    }
                } else {
                    // any other trier who is trying to hack the page
                    render view: view
                    return
                }
            } else {
                // any other trier who is trying to hack the page
                render view: view
                return
            }
        }
        String template = "/templates/${provider}summary"
        //Above loop should capture bad users and send them away otherwise all good users will see this
        render view: 'thanks', model: [payment:payment, template:template]
        return
    }



    /**
     * Paypal cart completion action
     * @param bean
     * @return
     */
    def paypalcheckout(CartBean bean) {
        ifPaypalEnabled
        updateBean(bean)
        try {
            if (!bean.hasErrors()) {
                def result = paymentService.checkout(bean)
                if (result && result.transactionId) {
                    session.paymentId = result?.paymentId
                    redirect(controller: 'paypal', action: 'uploadCart', params: result)
                    return
                }
            }
        } catch (Throwable t) {
           // throw new Throwable(t)
            flash.message = "Generic exception: ${t.getMessage()}."
            log.error t.message, t.stackTrace
            bean.errors.reject('exception',[t.toString(),System.currentTimeMillis()].toArray(),t.toString())
        }
        render view:'checkout', model: [instance:bean]
        return
    }

    def stripeFailedPayment() {
        render view: 'stripeFailedPayment'
    }

    CartBean updatePayConfig(CartBean bean) {
        bean.bindKey(PUBLIC_KEY)
        Map sqCfg = squareService.configuration
        bean.squareApplicationId = sqCfg.squareApplicationId
        bean.squareLocationId = (String) sqCfg?.location

        //Map ppCfg = paypalService.configuration

        return bean
    }

    CartBean updateBean(CartBean bean) {
        validateCart
        updatePayConfig(bean)

        if (session?.user && !bean.user) {
            bean.user = session?.user
        }
        if (session?.cart && !bean?.cart) {
            bean.cart = session?.cart
        }
        if (!bean.finalTotal) {
            bean.finalTotal = bean?.cart*.listPrice?.sum()?: session?.finalTotal
        }
        if (!bean.subTotal && bean.finalTotal) {
            bean.subTotal = bean.finalTotal
        }


        if (session?.cartCounter && !bean?.cartCounter) {
            bean.cartCounter = session?.cartCounter
        }
        bean.validate()
        return bean
    }

    def squarecheckout(CartBean bean) {
        ifSquareEnabled
        updateBean(bean)
        try {
            paymentService.setCartUserAddress((CartBean) bean)
            Map moneyMap = [:]
            moneyMap.amount=((bean?.finalTotal?:0) * 100).longValue()
            moneyMap.currency=bean?.currencyCode?: PaymentConfigListener.currencyCode?.toString()?:Currency.getInstance(Locale.UK).currencyCode
            Money amountMoney = squareService.createMoney(moneyMap)
            //SquareClient squareClient = squareService.squareClient

            Map userMap = bean.loadCustomerValues()
            Map addressMap = bean.loadCustomerAddress()
            addressMap.firstName=userMap.firstName
            addressMap.lastName=userMap.lastName
            com.squareup.square.models.Address customerAddress = squareService.createAddress(addressMap)

            userMap.address=customerAddress

            CreateCustomerRequest customerRequest  = squareService.createCustomerRequest(userMap)
            CreateCustomerResponse customer = squareService.createCustomer(customerRequest)

            Map bodyMap = [:]
            bodyMap.sourceId = bean.squareToken //'cnon:card-nonce-ok'
            bodyMap.idempotencyKey = PaymentHelper.idempotencyKey
            bodyMap.money = amountMoney
            bodyMap.customerId = customer?.customer?.id

            PaymentsApi paymentsApi = squareService.paymentsApi
            CreatePaymentRequest body = squareService.createPaymentRequest(bodyMap)
            CreatePaymentResponse response = paymentsApi.createPayment(body)
            if (response) {
                SquarePayment squarePayment = paymentService.finaliseSquareCheckout(bean,response)
                if (squarePayment && squarePayment?.id) {
                    flash.message = "Your order succeeded"
                    session.paymentId = squarePayment?.id
                    Map returnParams = [provider:'square']//, realPaymentId: squarePayment?.id]
                    session.currentUser = bean.user
                    session.cart = null
                    session.cartCounter = null
                    chain(action: 'thanks', params: returnParams)
                    return
                }
            }
        } catch (ApiException e) {
            log.error e.message
            //throw new Exception (e)
            flash.message = "ApiException exception: ${e.getMessage()}."
            e.errors?.each{ error->
                bean.errors.rejectValue(error?.field?:'finalTotal',error.code,[''] as Object[],error.detail)
            }
            render view:'checkout', model: [instance:bean]
            return
        } catch (Throwable e) {
            log.error e.message
            //throw new Exception (e)
            flash.message = "Generic exception: ${e.getMessage()}."
            bean.errors.reject('exception',[e.toString(),System.currentTimeMillis()].toArray(),e.toString())
            render view:'checkout', model: [instance:bean]
            return
        }
        session.cart = null
        session.cartCounter = null
        chain(controller:'payment', action: 'checkout')
        return

    }
    /**
     * Stripe Commit action for payment
     * @param bean
     * @return
     */
    def stripecheckout(CartBean bean) {
        ifStripeEnabled
        updateBean(bean)
        Stripe.apiKey =  SECRET_KEY
        //boolean failed
        try {
            Map<String, ?> chargeParams = new HashMap<String, Object>()
            chargeParams.put("amount", (bean?.finalTotal * 100).intValue()); // Amount in cents
           // chargeParams.put("currency", bean.currencyCode as String)
            chargeParams.currency=bean?.currencyCode?: PaymentConfigListener.currencyCode?.toString()?:Currency.getInstance(Locale.UK).currencyCode
            paymentService.setCartUserAddress(bean)

            session.currentUser = bean.user
            Customer customer = stripeService.createCustomer(bean.loadCustomerValues(stripeService.createAddress(bean.loadCustomerAddress())))
            Card card = stripeService.createCard(customer.id)
            if (customer) {
                chargeParams.put("customer", customer.id)
                chargeParams.put("source", card.id)
            } else {
                chargeParams.put("source", bean.stripeToken)
            }

            chargeParams.put("description", bean.purchaseDescription)

            Charge charge = stripeService.charge(chargeParams)

            StripeBean stripeBean = new StripeBean()
            stripeBean.status=charge.status
            stripeBean.description = charge.description
            stripeBean.id=charge.id
            stripeBean.customer = charge.customerObject
            stripeBean.balanceTransaction=charge.balanceTransaction
            bean.finalTotal = charge.amount?.toBigDecimal()
            bean.stripe=stripeBean
        } catch (Throwable e) {
            log.error e.message, e.stackTrace
           // throw new Throwable(e)
            flash.message = "Generic exception: ${e.getMessage()}."
            bean.errors.reject('exception',[e.toString(),System.currentTimeMillis()].toArray(),e.toString())
            render view:'checkout', model: [instance:bean]
            return
        }

        StripePayment stripePayment = paymentService.finaliseStripeCheckout(bean)
        if (stripePayment && stripePayment?.id) {
            flash.message = "Your order succeeded"
            session.paymentId = stripePayment?.id
            Map returnParams = [provider:'stripe']//, realPaymentId: stripePayment?.id]
            session.cart = null
            session.cartCounter = null
            chain(action: 'thanks', params: returnParams)
            return
        }

    }


    def checkout(CartBean bean) {
        ifCheckoutEnabled
        validateCart
        // Todo you must store your current logged in user as session.user
        bean.bindBean(session?.cart as List,session?.cartCounter as Map,PUBLIC_KEY, (PaymentUser)session.user)
        updatePayConfig(bean)

        if (!bean?.cart && !session?.cart) {
            redirect(controller:'payment', view:'index')
            return
        }

        render view:'checkout', model: [instance:bean]
    }

    def login() {
        chain(action: 'checkout')
    }

    private getValidateCart() {
        if (session?.cart && session?.cart?.size()==0||!session.cart) {
        //    redirect(controller:'payment', action:'checkout')
        }
    }

}