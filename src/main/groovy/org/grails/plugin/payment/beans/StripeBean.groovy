package org.grails.plugin.payment.beans

import com.stripe.model.Customer
import grails.validation.Validateable

class StripeBean implements Validateable  {

    String publicKey
    String token
    String status
    String balanceTransaction
    String description
    String id
    Customer customer

    static constraints = {
        publicKey(nullable:true)
        token(nullable:true)
        status(nullable:true)
        balanceTransaction(nullable:true)
        description nullable:true
        id nullable:true
        customer nullable:true

    }
}
