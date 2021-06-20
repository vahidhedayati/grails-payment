package org.grails.plugin.payment.enums

import groovy.transform.CompileStatic


@CompileStatic
public enum PaymentStatus {
    PENDING,INVALID,FAILED,COMPLETE,COMPLETED,APPROVED,CANCELLED,VERIFIED,PAID


    public static EnumSet<PaymentStatus> getGoodStatuses() {
        final EnumSet< PaymentStatus > ret_val = EnumSet.noneOf( PaymentStatus.class )
        ret_val.add(COMPLETE)
        ret_val.add(COMPLETED)
        ret_val.add(APPROVED)
        ret_val.add(VERIFIED)
        ret_val.add(PAID)
        return ret_val
    }

    public static EnumSet<PaymentStatus> getBadStatuses() {
        final EnumSet< PaymentStatus > ret_val = EnumSet.noneOf( PaymentStatus.class )
        ret_val.add(PENDING)
        ret_val.add(INVALID)
        ret_val.add(FAILED)
        ret_val.add(CANCELLED)
        return ret_val
    }
}