package org.grails.plugin.payment.enums

import groovy.transform.CompileStatic


@CompileStatic
public enum ItemStatus {

    AWAITING_DISPATCH('AWD'),
    DISPATCHED('DIS'),
    DELIVERED('DEL'),
    BEING_RETURNED('BRE'),
    RETURNED('RET')


    String value

    ItemStatus(String val) {
        this.value = val
    }
    public String getValue(){
        return value
    }
    static ItemStatus byValue(String val) {
        values().find { it.value == val }
    }


}

