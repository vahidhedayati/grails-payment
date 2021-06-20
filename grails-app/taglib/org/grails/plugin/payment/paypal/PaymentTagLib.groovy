package org.grails.plugin.payment.paypal

class PaymentTagLib {
    static namespace = "payment"

    def buttons = { attrs->
        out << g.render(contextPath: pluginContextPath, template : '/payment/buttons', model: attrs)
    }
}
