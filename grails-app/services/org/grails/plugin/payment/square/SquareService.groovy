package org.grails.plugin.payment.square

import com.squareup.square.Environment
import com.squareup.square.SquareClient
import com.squareup.square.api.CheckoutApi
import com.squareup.square.api.CustomersApi
import com.squareup.square.api.PaymentsApi
import com.squareup.square.exceptions.ApiException
import com.squareup.square.models.*
import grails.core.GrailsApplication
import org.grails.plugin.payment.PaymentConfig
import org.grails.plugin.payment.listeners.PaymentConfigListener

class SquareService {

    GrailsApplication grailsApplication
    SquareClient squareClient


    void setInternalSquareClient() {
        squareClient = new SquareClient.Builder()
                .environment(configuration.environment)
                .accessToken(configuration.squareAccessToken).build()
    }
    PaymentsApi getPaymentsApi() {
        setInternalSquareClient()
        return squareClient.paymentsApi
    }

    CheckoutApi getCheckoutApi() {
        setInternalSquareClient()
        return squareClient.checkoutApi
    }

    CustomersApi getCustomersApi() {
        setInternalSquareClient()
        return squareClient.customersApi
    }

    def getConfiguration(){
        String hostName = PaymentConfigListener.getValue('hostName') ?: grailsApplication.config.payment?.hostName
        String addConfig='square'
        Environment environment = Environment.PRODUCTION
        if (PaymentConfigListener.getValue('squareMode')  == PaymentConfig.SANDBOX) {
            addConfig+='Sandbox'
            environment = Environment.SANDBOX

        }
        String applicationSecret = PaymentConfigListener.getValue(addConfig+'ApplicationSecret') ?:  grailsApplication.config.square.accessToken
        String squareApplicationId = PaymentConfigListener.getValue(addConfig+'ApplicationId') ?: grailsApplication.config.square.applicationId
        String squareAccessToken = PaymentConfigListener.getValue(addConfig+'AccessToken') ?:  grailsApplication.config.square.accessToken
        String squareLocation = PaymentConfigListener.getValue(addConfig+'Location') ?:  grailsApplication.config.square.location
        return [squareApplicationId:squareApplicationId,squareAccessToken:squareAccessToken,environment:environment,
                applicationSecret:applicationSecret,hostName:hostName, location:squareLocation]
    }


    OrderSource createOrder(String name) {
        return new OrderSource.Builder().name(name).build()
    }

    MeasurementUnit getDefaultMeasurementUnit(Map<String, ?> props = null) {
        return new MeasurementUnit.Builder()
                .areaUnit((String)props?.areaUnit ?: "IMPERIAL_SQUARE_YARD")
                .lengthUnit((String)props?.lengthUnit ?: "METRIC_CENTIMETER")
                .volumeUnit((String)props?.volumeUnit ?: "GENERIC_SHOT")
                .weightUnit((String)props?.weightUnit ?: "METRIC_MILLIGRAM")
                .build()
    }

    OrderQuantityUnit getOrderQuantityUnit(MeasurementUnit unit = defaultMeasurementUnit) {
        return new OrderQuantityUnit.Builder().measurementUnit(unit).precision(191).build()
    }

    Money createMoney(Map<String, ?> props) {
        return new Money.Builder().amount((Long)props.amount).currency((String)props.currency).build()
    }

    CreatePaymentRequest createPaymentRequest(Map<String, ?> props) {
        return new CreatePaymentRequest.Builder((String)props.sourceId,
                (String)props.idempotencyKey, (Money) props.money)
                .customerId((String)props.customerId)
               //.locationId(configuration.location)
                .build()
    }

    OrderLineItemAppliedTax applyItemTax(Map<String, ?> props) {
        return new OrderLineItemAppliedTax.Builder((String)props.taxUid)
                .uid((String)props.uid)
                .appliedMoney((Money)props.money)
                .build()
    }

    OrderLineItemAppliedDiscount applyItemDiscount(Map<String, ?> props) {
        return new OrderLineItemAppliedDiscount.Builder((String)props.discountUid)
                .uid((String)props.uid)
                .appliedMoney((Money)props.money)
                .build();
    }

    OrderLineItem buildLineItem(Map<String, ?> props) {
        new OrderLineItem.Builder((String)props.quantity)
                .uid((String)props.uid)
                .name((String)props.name)
                .quantityUnit((OrderQuantityUnit)props.quantityUnit)
                .note((String)props.note)
                .catalogObjectId((String)props.catalogId)
                .appliedTaxes((List<OrderLineItemAppliedTax> )props.appliedTaxes)
                .appliedDiscounts((List<OrderLineItemAppliedDiscount> )props.appliedDiscounts)
                .basePriceMoney((Money)props.price)
                .build()
    }

    Order createOrder(Map<String, ?> props) {
        return new Order.Builder((String)props.locationId)
                .id((String)props.id)
                .referenceId((String)props.referenceId)
                .source((OrderSource)props.orderSource)
                .customerId((String)props.customerId)
                .lineItems((List<OrderLineItem>)props.lineItems)
                .taxes((List<OrderLineItemTax>)props.taxes)
                .discounts((List<OrderLineItemDiscount>)props.discounts)
                .build()
    }

    CreateOrderRequest createOrderRequest(Map<String, ?> props) {
        return new CreateOrderRequest.Builder()
                .order((Order)props.order)
                .idempotencyKey((String)props.idempotencyKey)
                .build()
    }

    Address createAddress(Map<String, ?> props) {
        return new Address.Builder()
                .addressLine1((String)props.line1)
                .addressLine2((String)props.line2)
                .addressLine3((String)props.line3)
                .locality((String)props.city)
                .sublocality((String)props.state)
                .administrativeDistrictLevel1((String)props.administrativeDistrictLevel1)
                .postalCode((String)props.postCode)
                .country((String)props.countryCode)
                .firstName((String)props.firstName)
                .lastName((String)props.lastName)
                .build()
    }



    CreateCustomerRequest createCustomerRequest(Map<String, ?> props) {
        return new CreateCustomerRequest.Builder()
                .givenName((String)props.firstName)
                .familyName((String)props.lastName)
                .emailAddress((String)props.emailAddress)
                .address((Address)props.address)
                .phoneNumber((String)props.telephone)
                .referenceId((String)props.referenceId)
                .note((String)props.note)
                .build()
    }

    CreateCustomerResponse createCustomer(CreateCustomerRequest body) throws ApiException, IOException {
           return customersApi.createCustomer(body)
    }

    CreateCheckoutRequest createBody(Map<String, ?> props) {
        return new CreateCheckoutRequest.Builder(
                    (String)props.idempotencyKey,
                    (CreateOrderRequest) props.order
                )
                .askForShippingAddress((boolean)props.askForShippingAddress)
                .merchantSupportEmail((String)props.merchantSupportEmail)
                .prePopulateBuyerEmail((String)props.prePopulateBuyerEmail)
                .prePopulateShippingAddress((Address)props.prePopulateShippingAddress)
                .redirectUrl((String)props.redirectUrl)
                .additionalRecipients((List<ChargeRequestAdditionalRecipient>) props.additionalRecipients)
                .build()
    }

    ChargeRequestAdditionalRecipient ChargeRequestAdditionalRecipient(Map<String, ?> props) {
        return new ChargeRequestAdditionalRecipient.Builder(
                (String)props.locationId,
                (String)props.description,
                (Money)props.money
        ).build()
    }

}
