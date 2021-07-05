package org.grails.plugin.payment.paypal

import org.grails.plugin.payment.PaymentUser
import org.grails.plugin.payment.beans.CartBean

/**
 * @Author: Vahid Hedayati
 * @Date: 21st June 2021
 * @Email: weaselpause@gmail.com
 *
 */
class PaymentTagLib {
    def stripeService
    def squareService

    private String getSECRET_KEY() {
        return stripeService.configuration.stripeSecretKey
    }

    private String getPUBLIC_KEY() {
        return stripeService.configuration.stripePublishableKey
    }
    static namespace = "payment"

    /**
     * payment:buttons to be called in a gsp as a html tag e.g:
     *
     *      <payment:buttons
     *          instance="${instance}"
     *          finalTotal="${finalTotal}"
     *      />
     *
     *   Will load up your payment buttons, output is :
     *   1) Paypal pay button - if activated as a provider
     *   2) Stripe pay button - if activated as a provider
     *   3) Square pay button - if activated as provider
     *
     * ---------------------------------------------------------
     *  Instance will be shaped similar to this map example:
     *    <payment:buttons instance="${[
                finalTotal: 200,
                currencyCode: 'GBP'  //defaults to PaymentConfig value if not set
                address:[
                    title:'Mr',
                    firstName:'fred',
                    lastName: 'Smith',
                    line1: '1 Long lane',
                    line2: 'Big street',
                    city: 'London',
                    state:'x',
                    country: 'United Kingdom',
                    countryCode: 'GB',  //should default to set value in PaymentConfig if not set
                    username: 'fred',
                    telephone:'12345'
                ]
            ]}"
            finalTotal="${200}" />
     *
     */
    def internalButtons = { attrs->
        CartBean bean = (CartBean) attrs.instance
        //updateBean(bean)
        session.finalTotal = bean.finalTotal
        session.currencyCode = bean.currencyCode
        out << g.render(contextPath: pluginContextPath, template : '/payment/buttons', model:attrs)
    }

    def buttons = { attrs->
        CartBean bean = new CartBean(attrs.instance)
        updateBean(bean)
        session.finalTotal = bean.finalTotal
        session.currencyCode = bean.currencyCode
        out << g.render(contextPath: pluginContextPath, template:"/templates/fakeForm" , model:[instance:bean, templateFile : '/payment/buttons'])
    }

    /**
     *      <payment:checkout
     *          instance="${instance}"
     *       />
     *
     *
     */
    def checkout = {Map attrs ->
        CartBean bean = new CartBean(attrs?.instance)
        //PaymentUser user
        //if (attrs?.instance?.user) {
        //    user = PaymentUser.get(attrs?.instance?.user?.id as Long)
        //}
        bean.bindBean(attrs?.instance?.cart as List,attrs?.instance?.cartCounter as Map,PUBLIC_KEY,(PaymentUser) attrs?.instance?.user)

        updateBean(bean)
        if (!bean?.cart) {
            out << g.render(contextPath: pluginContextPath, template : '/payment/failed', model: [instance:bean])
        } else {
            session.finalTotal = bean.finalTotal
            session.currencyCode = bean.currencyCode
            session.cart = bean.cart
            session.cartCounter = bean.cartCounter
            out << g.render(contextPath: pluginContextPath, template : '/payment/checkout', model: [instance:bean])
        }

    }



    private CartBean updateBean(CartBean bean) {

        //if (session?.user && !bean?.user) {
        //    bean.user = session?.user
       // }
        if (session?.cart && !bean?.cart) {
            bean.cart = session?.cart
        }
        if (session?.cartCounter && !bean?.cartCounter) {
            bean.cartCounter = session?.cartCounter
        }
        if (!bean?.finalTotal) {
            bean.finalTotal = bean?.cart*.listPrice?.sum()?: session?.finalTotal
        }
        if (!bean?.subTotal && bean?.finalTotal) {
            bean.subTotal = bean.finalTotal
        }
        bean.bindKey(PUBLIC_KEY)
        Map sqCfg = squareService.configuration
        bean.squareApplicationId = sqCfg.squareApplicationId
        bean.squareLocationId = (String) sqCfg?.location
        bean.validate()
        return bean
    }

}
