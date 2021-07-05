package org.grails.plugin.payment.paypal

import com.paypal.api.payments.PayerInfo
import org.grails.plugin.payment.enums.CountryCode

class BuyerInformation implements Serializable {

	String uniqueCustomerId

	String firstName
	String lastName
	String companyName
	String receiverName // included when the customer provides a Gift Address
	String email
	String line1
	String line2
	//String line3
	String zip
	String city
	String state
	String country
	String countryCode
	String phoneNumber

	//After execution its paypal resonse captured once in Payment to start then again after executed here
	//String status
	//String paymentMethod

	boolean addressConfirmed

	static belongsTo = PaypalPayment

	static constraints = { // everything nullable - need to store this object even when values are missing!
		uniqueCustomerId nullable: true, blank: true
		firstName nullable: true, blank: true
		lastName nullable: true, blank: true
		companyName nullable: true, blank: true
		receiverName nullable: true, blank: true
		email nullable: true, blank: true
		line1 nullable: true, blank: true
		line2 nullable: true, blank: true
		//line3 nullable: true, blank: true
		zip nullable: true, blank: true
		city nullable: true, blank: true
		state nullable: true, blank: true
		country nullable: true, blank: true
		countryCode nullable: true, blank: true
		phoneNumber nullable: true, blank: true
		//status nullable: true, blank: true
		//paymentMethod nullable: true, blank: true
	}

	void populateFromPaypal(Map paypalArgs) {
		uniqueCustomerId = paypalArgs.payer_id
		firstName = paypalArgs.first_name
		lastName = paypalArgs.last_name
		companyName = paypalArgs.payer_business_name
		receiverName = paypalArgs.address_name
		email = paypalArgs.payer_email
		line2 = paypalArgs.address_line2
		zip = paypalArgs.address_zip
		city = paypalArgs.address_city
		state = paypalArgs.address_state
		country = paypalArgs.address_country
		countryCode = paypalArgs.address_country_code
		phoneNumber = paypalArgs.contact_phone
		addressConfirmed = (paypalArgs.address_status == 'confirmed')
	}

	void populateFromPaypal(PayerInfo payerInfo) {
		uniqueCustomerId = payerInfo.payerId
		firstName = payerInfo.firstName
		lastName = payerInfo.lastName
		//companyName = payerInfo.c
		receiverName = payerInfo.shippingAddress?.recipientName
		email = payerInfo.email
		line2 = payerInfo.shippingAddress.line1
		zip = payerInfo.shippingAddress.line2
		city = payerInfo.shippingAddress.city
		state = payerInfo.shippingAddress.state
		//country = payerInfo.shippingAddress.countryCode
		countryCode = payerInfo.shippingAddress.countryCode
		phoneNumber = payerInfo.shippingAddress.phone
		addressConfirmed = (payerInfo.shippingAddress.status == 'confirmed')
	}

	List getAddressArray() {
		List a=[]

		if (receiverName) {
			a <<receiverName
		} else {
			if (firstName) {
				if (lastName) {
					a << firstName+" "+lastName
				} else {
					a << firstName
				}
			}
		}
		if (line2) {
			a <<line2
		}
		if (city) {
			a <<city
		}
		if (state) {
			a <<state
		}
		if (countryCode) {
			a << CountryCode.valueOf(countryCode)
		}

		return a
	}
}
