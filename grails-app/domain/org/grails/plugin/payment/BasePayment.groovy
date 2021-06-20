package org.grails.plugin.payment

import org.grails.plugin.payment.enums.PaymentStatus

abstract class BasePayment implements Serializable {

    PaymentStatus status = PaymentStatus.PENDING
    Double tax = 0 // tax applies to entire payment, not to each item!
    BigDecimal discountCartAmount = 0 // optional currency value; if specified will override individual item discounts
    Currency currency //= Currency.getInstance(currencyCode) // default to GBP

    BigDecimal shipping = 0.0

    //When user provides the address for what ever payment - it ends in here
    PaymentAddress postalAddress

    BigDecimal paid = 0.0   // how much got paid of order should match gross
    BigDecimal gross = 0.0  // final total

    boolean completed=false
    def transactionIdPrefix = "TRANS"

    // This is underlying security user mapped to payment user during sign up I presume
    PaymentUser user

    Date dateCreated
    Date lastUpdated

    boolean hideUserDetails=false

    String transactionId
    static transients = ['transactionIdPrefix']
    transient beforeInsert = {
        transactionId = "${transactionIdPrefix}-${user?.id}-${System.currentTimeMillis()}"
    }

    @Override
    String toString() { "Payment: ${transactionId ?: 'not saved'}" }

    //This contains each item sold in this overall purchase paid
    static hasMany = [paymentItems: PaymentItem]

    static constraints = {
        status inList: PaymentStatus.values().findAll()
        shipping nullable:true
        paid nullable:true
        userId nullable:true
        postalAddress nullable:true
    }

    static mapping = {
        cache true
        //tablePerHierarchy true
    }
}
