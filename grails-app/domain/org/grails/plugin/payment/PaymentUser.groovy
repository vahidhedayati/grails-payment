package org.grails.plugin.payment

import org.grails.plugin.payment.beans.AddressBean

/**
 * This is a simple user class for plugin, you should ensure your user correctly signs up and gets added to which
 * ever security system i.e. spring security username can be mapped here
 */
class PaymentUser {

    String username

    boolean isAdmin=true
    String title
    String firstName
    String lastName
    String emailAddress


    static constraints = {
        title nullable:true
        firstName nullable:true
        lastName nullable:true
        emailAddress nullable:true
    }

    boolean getHasAddress() {
        return (addresses?.size()>0)
    }
    Set<PaymentAddress> getAddresses() {
        (PaymentUserPaymentAddress.findAllByUser(this) as List<PaymentUserPaymentAddress>)*.address as Set<PaymentAddress>
    }

    PaymentAddress getPrimaryAddress() {
        addresses?.find{it.primaryAddress == true }
    }

    Set<PaymentAddress> getOtherAddresses() {
        if (primaryAddress) {
            addresses?.findAll{it.id != primaryAddress?.id}
        } else {
            addresses?.findAll()
        }
    }

    Set<AddressBean> getOtherAddressesBean() {
        if (primaryAddress) {
            (addresses?.findAll{it.id != primaryAddress?.id} as List<PaymentAddress>)*.addressBean as Set<AddressBean>
        } else {
            (addresses?.findAll()as List<PaymentAddress>)*.addressBean as Set<AddressBean>
        }
    }

    static mapping = {
        cache true
    }
}
