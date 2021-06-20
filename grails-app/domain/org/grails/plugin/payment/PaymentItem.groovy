package org.grails.plugin.payment

import groovy.transform.ToString
import org.grails.plugin.payment.enums.ItemStatus

@ToString(includeNames=true, includePackage=false)
class PaymentItem implements Serializable {

	BigDecimal amount
	BigDecimal shippingAmount
    BigDecimal discountAmount = 0
	BigDecimal weight = 0.0 // added for ship weight
	String itemName
	String description
	String itemNumber
	Integer quantity = 1
	ItemStatus status = ItemStatus.AWAITING_DISPATCH

	static belongsTo = [payment:BasePayment]

	Date dateCreated
	Date lastUpdated

	//Not a grails service - instead company used to ship item with - you can manage this maybe a drop down
	String postalService

	//tracking number bound to postal service
	String trackingNumber

	static constraints = {
		description:nullable:true
		itemName blank:false
		itemNumber blank:false
		shippingAmount nullable:true
		status inList: ItemStatus.values().findAll()
		trackingNumber nullable:true
		postalService nullable: true
	}


	static mapping= {
		//status(sqlType:'char(2)')
		itemName(index: 'payment_item_name_idx')
		status(enumType: 'string', sqlType: 'char(90)')
	}
}
