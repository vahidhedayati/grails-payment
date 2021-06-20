
package org.grails.plugin.payment

import org.grails.plugin.payment.beans.AddressBean

class PaymentAddress {

    String title
    String firstName
    String lastName

    //Required
    String line1
    String line2
    String city
    String country
    String countryCode
    String postcode

    BigDecimal longitude
    BigDecimal latitude
    String state

    Date dateCreated  //auto
    Date lastUpdated    //auto

    String telephone
    String emailAddress

    boolean primaryAddress=false

    static constraints = {
        title (nullable: true, enumType: "ordinal")
        firstName (nullable: true)
        lastName (nullable: true)
        line2 (nullable:true)
        longitude nullable: true
        latitude nullable: true
        state (nullable: true)
        countryCode(nullable:true)
        telephone(shared:'phone',nullable:true)
        emailAddress nullable:true
    }

    AddressBean getAddressBean() {
        AddressBean bean = new AddressBean()
        bean.id = id
        bean.city=city
        bean.country=country
        bean.countryCode=countryCode
        bean.postcode = postcode
        bean.line2=line2
        bean.line1=line1
        bean.state=state
        bean.title=title
        bean.firstName=firstName
        bean.lastName=lastName
        bean.emailAddress=emailAddress
        bean.telephone=telephone

        return bean

    }

    List getAddressArray() {
        List a=[]
        if (firstName) {
            if (lastName) {
                a << firstName+" "+lastName
            } else {
                a << firstName
            }
        }
        if (line1) {
            a << line1
        }
        if (line2) {
            a << line2
        }
        if (city) {
            a << city
        }
        if (country) {
            a << country
        }
        if (postcode) {
            a << postcode
        }
        return a
    }

    String getFlatAddress() {
        return addressArray?.join('\n')?:''
    }

    String getFlatTelephone() {
        if (telephone) {
            return "${telephone}"
        }
    }



    String getFriendlyName() {
        return ((firstName ? (firstName+' '+lastName?:'') :
                (lastName?:'')))
    }

    static mapping = {
        cache true
    }
}
