package org.grails.plugin.payment.beans

import grails.validation.Validateable
import org.grails.plugin.payment.paypal.PaypalPayment

class CheckoutBean implements Validateable  {


    String transactionId
    Long paymentId


    static constraints = {
        transactionId(nullable:true)
        paymentId(nullable:true)

    }

    public CheckoutBean() {

    }

    public CheckoutBean(String transactionId, Long paymentId ) {

        this.paymentId=paymentId
        this.transactionId=transactionId
    }

    PaypalPayment getPayment() {
        return PaypalPayment.load(paymentId)
    }
}
