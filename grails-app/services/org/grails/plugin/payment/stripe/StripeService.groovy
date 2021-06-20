package org.grails.plugin.payment.stripe

import com.stripe.Stripe
import com.stripe.model.*
import com.stripe.net.RequestOptions
import com.stripe.param.*
import grails.core.GrailsApplication
import org.grails.plugin.payment.PaymentConfig
import org.grails.plugin.payment.listeners.PaymentConfigListener

import javax.naming.AuthenticationException
import javax.smartcardio.CardException

class StripeService {

    GrailsApplication grailsApplication

    def getConfiguration(){
        String hostName = PaymentConfigListener.getValue('hostName') ?: grailsApplication.config.mybusiness?.hostName
        String addConfig='stripe'
        if (PaymentConfigListener.getValue('stripeMode')  == PaymentConfig.SANDBOX) {
            addConfig+='Test'
        }
        String stripeSecretKey = PaymentConfigListener.getValue(addConfig+'SecretKey') ?: grailsApplication.config.stripe.secretKey
        String stripePublishableKey = PaymentConfigListener.getValue(addConfig+'PublishableKey') ?:  grailsApplication.config.stripe.publishableKey
        return [stripePublishableKey:stripePublishableKey,stripeSecretKey:stripeSecretKey,hostName:hostName]
    }

    private String getSECRET_KEY() {
        return configuration.stripeSecretKey
    }

    private String getPUBLIC_KEY() {
        return configuration.stripePublishableKey
    }

    Card retrieveCard(Map<String, String> props) {
        Customer customer = Customer.retrieve(props.customerId)
        return (Card) customer.getSources().retrieve(props.cardId)
    }

    Card updateCard(Map<String, String> props){
        Customer customer = Customer.retrieve(props.customerId)
        Card card = (Card) customer.getSources().retrieve(props.cardId)
        Map updateParams = new HashMap()
        updateParams.put("name", props.name)
        card.update(updateParams)
    }


    void deleteCard(Map<String, String> props){
        Stripe.apiKey = SECRET_KEY
        Customer customer = Customer.retrieve(props.customerId)
        customer.getSources().retrieve(props.cardId).delete()
    }

    PaymentSourceCollection listCards(String customerId){
        return Customer.retrieve(customerId).getSources().findAll{PaymentSourceCollection c -> c.object == 'card'}
    }

    Charge charge(Map<String, ?> props)
            throws AuthenticationException, CardException {
        ChargeCreateParams chargeParams =
                ChargeCreateParams.builder()
                        .setAmount(props.amount)
                        .setCurrency(props.currency)
                        .setDescription(props.description)
                        .setSource(props.source)  //stripeToken
                        .setCustomer(props.customer)  // use methods below to retrieve or create customer
                        .build()
        return Charge.create(chargeParams,requestOptions);
    }

    RequestOptions getRequestOptions() {
        return RequestOptions.builder()
                .setApiKey(configuration.stripeSecretKey)
                .build()
    }

    Customer getCustomer(String customerName) {
        Customer customer
        try {
            customer = Customer.retrieve(customerName, requestOptions)
        } catch(Exception e) {
            log.error e.message
        }
        return customer
    }

    CustomerCreateParams.Address createAddress(Map<String, String> props) {
        Map<String, Object> extraParams=[:]
        return new  CustomerCreateParams.Address(props?.city,props?.country,extraParams,props?.line1,props?.line2,props?.postalCode, props?.state)
    }

    Customer findOrCreateCustomer(Map<String, ?> props) {
        Customer customer = findByEmail(props.email)[0]
        if (!customer) {
            customer = createCustomer(props)
        }
        return customer
    }

    Customer createCustomer(Map<String, ?> props) {
        Customer customer
        CustomerCreateParams params = CustomerCreateParams.builder()
        //.addPreferredLocale("en")
        //.addPreferredLocale("es")
                .setEmail(props.email)
                .setName(props.name)
                .setDescription(props.description)
                .setSource(props.token)
                .setShipping(props.shipping)
                .setPhone(props.phone)
                .setAddress(props.address)
                .build()
        try {
            customer = Customer.create(params,requestOptions)
        } catch(Exception e) {
            log.error e.message
        }
        return customer
    }

    boolean deleteCustomer(String customerName) {
        boolean success
        try {
            Customer customer = Customer.retrieve(customerName,requestOptions)
            //Customer deletedCustomer = customer.delete()
            customer.delete()
            success=true
        } catch(Exception e) {
            success=false
            log.error e.message
        }
        return success
    }

    Card createCard(Map<String, Object> props, String customerId) {
        Map<String, Object> retrieveParams = new HashMap<>()
        List<String> expandList = new ArrayList<>()
        expandList.add("sources")
        retrieveParams.put("expand", expandList);
        Customer customer = Customer.retrieve(customerId, retrieveParams, null);
        Card card
        Map<String, Object> params = new HashMap<>();
        params.put("source", "tok_visa");

        try {
             card = (Card) customer.sources.create(params);

        } catch(Exception e) {
            log.error e.message
        }
        return card
    }
    CustomerCollection findByEmail(String email, Long limit=1L) {
        CustomerCollection customers
        CustomerListParams params =
                CustomerListParams.builder()
                        .setLimit(limit)
                        .setEmail(email)
                        .build()
        try {
            customers = Customer.list(params,requestOptions)
        } catch(Exception e) {
            log.error e.message
        }
        return customers
    }

    CustomerCollection getAllCustomers() {
        CustomerCollection customers
        CustomerListParams params =
                CustomerListParams.builder()
                        .build()
        try {
            customers = Customer.list(params,requestOptions);
        } catch(Exception e) {
            log.error e.message
        }
        return customers
    }

    Customer updateCustomer(String customerName, Map<String, ?> props) {
        Customer updatedCustomer
        CustomerUpdateParams params = CustomerUpdateParams.builder()
                .setEmail(props.email)
                .setName(props.name)
                .setDescription(props.description)
                .setSource(props.token)
                .setShipping(props.shipping)
                .setPhone(props.phone)
                .setAddress(props.address)
                .build()
        try {
            RequestOptions options = requestOptions
            Customer customer = Customer.retrieve(customerName,options)
            updatedCustomer = customer.update(params,options)
        } catch(Exception e) {
            log.error e.message
        }
        return updatedCustomer
    }

    PaymentIntent createIntent(Map<String, ?> props) {
        PaymentIntent intent
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(props.amount)
                .setCurrency(props.currency)
                .setCustomer(props.customer)
                .setReceiptEmail(props.receiptEmail)
                .build()
         try {
             intent = PaymentIntent.create(params,requestOptions)
         } catch(Exception e) {
             log.error e.message
         }
        return intent
    }

    PaymentIntent findPaymentIntentById(String intentId) {
        PaymentIntent intent
        try {
            intent = PaymentIntent.retrieve(intentId,requestOptions)
        } catch(Exception e) {
            log.error e.message
        }
        return intent
    }

    PaymentIntent confirmationIntent(String intentId, Map<String, ?> props) {
        PaymentIntent confirmedIntent
        PaymentIntentConfirmParams params = PaymentIntentConfirmParams.builder()
             .setPaymentMethod(props.paymentMethod)
             .build()

         try {
             PaymentIntent intent = findPaymentIntentById(intentId)
             confirmedIntent = intent.confirm(params);
         } catch(Exception e) {
             log.error e.message
         }
        return confirmedIntent
    }
}
