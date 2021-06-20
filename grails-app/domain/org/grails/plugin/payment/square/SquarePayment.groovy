package org.grails.plugin.payment.square

import org.grails.plugin.payment.BasePayment


class SquarePayment extends BasePayment {

    String description
    BigDecimal amount
    String emailAddress
    String token
    String squareChargeId
    String squareStatus

    boolean completed=false

    static constraints = {
        emailAddress nullable:true
        token nullable:true
        squareStatus nullable:true
    }

}
