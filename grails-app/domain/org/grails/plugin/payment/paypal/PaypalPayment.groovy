package org.grails.plugin.payment.paypal

import org.grails.plugin.payment.BasePayment

class PaypalPayment extends BasePayment {

    String paypalTransactionId

    BuyerInformation buyerInformation // details, provided by Paypal

    String paypalUserStatus
    String paymentMethod
    String paymentMode

    String paypalFee
    String currencyCode

    void populateFromPaypal(com.paypal.api.payments.Payment payment) {
        paypalUserStatus = payment.payer.status
        paymentMethod = payment.payer.paymentMethod
    }

    static constraints = {
        transactionId nullable: true
        paypalFee nullable:true
        currencyCode nullable:true
        paypalTransactionId nullable: true
        buyerInformation nullable: true
        paypalUserStatus nullable: true //, inList: PaymentStatus.values().collect{it.toString()}
        paymentMethod nullable:true
        paymentMode nullable:true
    }


    static mapping= {
        //discriminator value: 'paypalPayment'
        autoImport false
        //cache true
        //id generator:'org.hibernate.id.enhanced.TableGenerator', params:[segment_value:this.simpleName,optimizer:'hilo',increment_size:20]
        paypalTransactionId(index:'payment_paypal_transaction_idx')
    }

    Set<PaypalSale> getSales() {
        List<PaypalSale> sales = []
        if (this?.paypalTransactionId) {
            sales = PaypalSale?.findAllByParentPayment(this.paypalTransactionId) ?: []
        }
        return sales as Set<PaypalSale>
    }

}
