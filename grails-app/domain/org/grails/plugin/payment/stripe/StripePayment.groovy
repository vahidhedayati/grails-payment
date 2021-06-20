package org.grails.plugin.payment.stripe

import org.grails.plugin.payment.BasePayment


class StripePayment extends BasePayment {

    String description
    BigDecimal amount
    String emailAddress
    String token
    String stripeChargeId
    String stripeStatus
    String balanceTransaction


    static constraints = {
        emailAddress nullable:true
        token nullable:true
        stripeStatus nullable:true
        balanceTransaction nullable:true
    }


}
