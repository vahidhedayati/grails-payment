package org.grails.plugin.payment.paypal

import com.paypal.api.payments.Sale

class PaypalSale {

    String paymentMode
    String payPalId
    String state
    String parentPayment


    String currency
    /**
     * Total amount charged from the payer to the payee. In case of a refund, this is the refunded amount to the original payer from the payee. 10 characters max with support for 2 decimal places.
     */
    String total

    String subtotal;
    /**
     * Amount charged for shipping. 10 characters max with support for 2 decimal places.
     */
    String shipping;
    /**
     * Amount charged for tax. 10 characters max with support for 2 decimal places.
     */
    String tax;
    /**
     * Amount being charged for the handling fee. Only supported when the `payment_method` is set to `paypal`.
     */
    String handlingFee;
    /**
     * Amount being discounted for the shipping fee. Only supported when the `payment_method` is set to `paypal`.
     */
    String shippingDiscount;
    /**
     * Amount being charged for the insurance fee. Only supported when the `payment_method` is set to `paypal`.
     */
    String insurance;
    /**
     * Amount being charged as gift wrap fee.
     */
    String giftWrap;


    public PaypalSale() {

    }


    static constraints = {
        payPalId nullable:true
        paymentMode nullable:true
        state nullable:true
        parentPayment nullable:true
        total nullable:true
        currency nullable:true
        total nullable:true
        giftWrap nullable:true
        insurance nullable:true
        shippingDiscount nullable:true
        handlingFee nullable:true
        tax nullable:true
        shipping nullable:true
        subtotal nullable:true
    }

    public PaypalSale(Sale sale) {
        this.payPalId = sale.id
        this.currency = sale.amount.currency
        this.total = sale.amount.total
        this.giftWrap = sale.amount.details.giftWrap
        this.insurance = sale.amount.details.insurance
        this.shippingDiscount = sale.amount.details.shippingDiscount
        this.handlingFee = sale.amount.details.handlingFee
        this.tax = sale.amount.details.tax
        this.shipping = sale.amount.details.shipping
        this.subtotal = sale.amount.details.subtotal

        this.state=sale.state
        this.parentPayment=sale.parentPayment
        this.paymentMode = sale.paymentMode

    }
    static mapping= {
        cache true
        parentPayment(index:'sale_parent_payment_idx')
    }
}
