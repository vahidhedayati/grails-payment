package org.grails.plugin.payment

import org.grails.plugin.payment.enums.CountryCode
import org.grails.plugin.payment.enums.CurrencyTypes

class PaymentConfig {

    CurrencyTypes currencyCode= CurrencyTypes.GBP
    CountryCode countryCode= CountryCode.GB

    boolean paymentConfigEnabled=true


    final static String SANDBOX = 'sandbox'
    final static String LIVE = 'live'
    final static List paymentModes = [LIVE,SANDBOX]

    String hostName="http://localhost:8080"

    boolean paypalEnabled=true
    String paypalMode=SANDBOX
    String paypalEmail
    String paypalClientId
    String paypalClientSecret
    String paypalEndpoint
    String paypalSandboxEmail
    String paypalSandboxClientId
    String paypalSandboxClientSecret
    String paypalSandboxEndpoint

    boolean stripeEnabled=true
    String stripeMode=SANDBOX
    String stripeTestSecretKey
    String stripeTestPublishableKey
    String stripeSecretKey
    String stripePublishableKey

    boolean squareEnabled=true
    String squareMode=SANDBOX
    String squareSandboxApplicationId
    String squareSandboxAccessToken
    String squareSandboxLocation
    String squareSandboxApplicationSecret
    String squareApplicationId
    String squareAccessToken
    String squareApplicationSecret
    String squareLocation

    static constraints = {
        squareLocation nullable:true
        squareSandboxLocation nullable:true
        paypalMode inList: paymentModes
        stripeMode inList: paymentModes
        squareMode inList: paymentModes
    }

    static mapping= {
        cache true
    }

}
