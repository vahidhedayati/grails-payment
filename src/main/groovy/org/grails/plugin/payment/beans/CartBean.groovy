package org.grails.plugin.payment.beans

import com.stripe.param.CustomerCreateParams
import grails.util.Holders
import grails.validation.Validateable
import org.grails.plugin.payment.PaymentAddress
import org.grails.plugin.payment.PaymentUser
import org.grails.plugin.payment.enums.CountryCode
import org.grails.plugin.payment.listeners.PaymentConfigListener
import org.grails.plugins.web.taglib.ApplicationTagLib
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError

class CartBean implements Validateable  {

    private ApplicationTagLib g = Holders.grailsApplication.mainContext.getBean(ApplicationTagLib)

    AddressBean address
    AddressBean newAddress
    Set<AddressBean> otherAddresses=[]

    PaymentUser user

    PaymentAddress selectedAddress

    boolean hasAddress

    String editCartUrl = g.createLink(controller:'payment', action:'checkout')

    List cart=[]
    Map cartCounter=[:]
    String currencyCode

    BigDecimal shipping
    BigDecimal subTotal
    BigDecimal finalTotal

    String stripeToken
    String stripePublicKey
    StripeBean stripe

    CountryCode countryCode = CountryCode.GB

    String squareApplicationId
    String squareLocationId
    String squareToken
    String squareLocation

    static constraints = {
        squareApplicationId(nullable:true)
        squareLocationId(nullable:true)
        squareToken(nullable:true)
        squareLocation(nullable:true)
        stripe(nullable:true)
        stripePublicKey nullable:true
        stripeToken(nullable:true)
        shipping(nullable:true)
        subTotal(nullable:true)
        user(nullable:true)
        finalTotal(nullable:true)
        selectedAddress(nullable:true)
        currencyCode(nullable:true)
        address(nullable:true, validator: checkAddress)
        newAddress nullable:true



    }
    static def checkAddress={AddressBean val, obj,errors->
            if (obj.hasAddress && obj.address && !obj.address.id || !obj.hasAddress) {
            if (val) {
                val?.errors?.allErrors?.each {  error ->
                    errors.rejectValue(error.code, error.arguments, error.defaultMessage)
                }
            }

        }
    }

    def bindKey(String key) {
        stripePublicKey=key
    }

    def bindBean(List cart, Map cartCounter, String key,  PaymentUser authenticatedUser=null) {
        this.bindKey(key)
        this.cart = cart
        this.cartCounter=cartCounter
        this.countryCode = this.countryCode? this.countryCode : PaymentConfigListener.countryCode

        if (authenticatedUser) {
            this.user = authenticatedUser
            this.hasAddress=authenticatedUser?.hasAddress
            if (this.hasAddress) {
                if (authenticatedUser?.primaryAddress) {
                    this.address=authenticatedUser?.primaryAddress?.addressBean
                } else {
                    this.address=authenticatedUser?.addresses[0]?.addressBean
                }
                if (authenticatedUser?.otherAddresses) {
                   this.otherAddresses=authenticatedUser?.otherAddressesBean
                }
            } else {
                AddressBean addr  = new AddressBean()
                addr.isNew=true
                addr.existingUser=true
                addr.title=authenticatedUser?.title
                addr.firstName=authenticatedUser?.firstName
                addr.lastName=authenticatedUser?.lastName
                addr.emailAddress=authenticatedUser?.emailAddress
                addr.countryCode=this.countryCode?.toString()
                addr.country=this.countryCode?.name
                this.address =addr
            }
        } else {
            AddressBean addr  = new AddressBean()
            addr.isNew=true
            addr.countryCode=this.countryCode?.toString()
            addr.country=this.countryCode?.name
            this.address =addr
        }
        return this
    }

    String getPurchaseDescription() {
        if (address && cart && finalTotal) {
            return (address.firstName+' '+address.lastName+' '+cart?.name+' '+finalTotal+' '+new Date())
        }
        return 'To be set'
    }

    AddressBean findAddressBean() {
        AddressBean chosenAddress
        if (selectedAddress && selectedAddress?.line1) {
            chosenAddress = selectedAddress.addressBean
        } else {
            if (address && address?.line1)  {
                chosenAddress = address
            }
            if (!chosenAddress && newAddress && newAddress?.line1) {
                chosenAddress = newAddress
            }
        }
        return chosenAddress
    }

    def loadCustomerValues(CustomerCreateParams.Address address) {
        def result = [:]
        AddressBean chosen = findAddressBean()

        if (chosen) {
            result.email=chosen.emailAddress
            result.name=chosen.friendlyName
            result.firstName=chosen.firstName
            result.lastName=chosen.lastName
            result.description=chosen.friendlyName
            result.note=chosen.friendlyName
            result.source=stripeToken
            result.phone=chosen.telephone
            result.address=address
            return result
        }
    }

    Map<String,String> loadCustomerAddress() {
        Map<String,String> result = [:]
        AddressBean chosen = findAddressBean()
        if (chosen) {
            result.country = chosen.country
            if (!chosen?.countryCode && chosen?.country) {
                chosen?.countryCode =  CountryCode.values()?.
                        find{it.name.toLowerCase() == chosen?.country?.toLowerCase()}?.toString() ?:
                        PaymentConfigListener.countryCode?.toString()
            }
            result.countryCode = chosen.countryCode
            result.city = chosen.city
            result.line1 = chosen.line1
            result.line2 = chosen.line2
            result.postalCode = chosen.postcode
            result.postCode = chosen.postcode
            result.state = chosen.state
            //result.extraParams=[:]
        }
        return result
    }

    Integer getCartQuantity() {
        Integer result = 0
        Integer cartCount =cartCounter?.size()
        if (cartCount) {
            result = cartCount
        }
        return result
    }
}
