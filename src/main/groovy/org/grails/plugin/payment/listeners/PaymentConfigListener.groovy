package org.grails.plugin.payment.listeners

import org.grails.plugin.payment.enums.CountryCode
import org.grails.plugin.payment.enums.CurrencyTypes
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class PaymentConfigListener {
    private static Logger log = LoggerFactory.getLogger(getClass())
    private static final ConcurrentMap<String, ?> GENERIC_MAP = new ConcurrentHashMap<String, ?>()

    PaymentConfigListener() {
        this.reload()
    }

    def reload() {
        log.info "starting up  ${getClass().simpleName}"
    }

    public static void addConfig(String configName, String value) {
        def r = GENERIC_MAP.get(configName)
        if (!r) {
            GENERIC_MAP.put(configName, value)
        }
    }

    public static void removeConfig(String configName) {
        if (configName) {
            GENERIC_MAP.remove(configName)
        } else {
            GENERIC_MAP.clear()
        }
    }
    public static void updateGeneric( CountryCode value) {
        String configName='countryCode'
        genericUpdate(configName,value)
    }
    public static void updateGeneric( CurrencyTypes value) {
        String configName='currencyCode'
        genericUpdate(configName,value)
    }
    public static void updateGeneric(String configName, String value) {
        genericUpdate(configName,value)
    }

    public static void genericUpdate(String configName,value) {
        def r = GENERIC_MAP.get(configName)
        if (value) {
            if (!r) {
                GENERIC_MAP.put(configName, value)
            } else {
                GENERIC_MAP.remove(configName)
                GENERIC_MAP.put(configName, value)
            }
        } else {
            log.error "No Value set "+configName+" "+value
        }
    }


    public static String getValue(String configName) {
        return GENERIC_MAP.get(configName)
    }

    public static boolean getPaypalEnabled(String configName='paypalEnabled') {
        getBooleanField(configName)
    }
    public static boolean getStripeEnabled(String configName='stripeEnabled') {
        getBooleanField(configName)
    }
    public static boolean getSquareEnabled(String configName='squareEnabled') {
        getBooleanField(configName)
    }

    public static boolean getPaymentConfigEnabled(String configName='paymentConfigEnabled') {
        getBooleanField(configName)
    }

    public static boolean getBooleanField(String configName='enableContactForm') {
        return Boolean.valueOf(GENERIC_MAP.get(configName))
    }

    public static String getKey(String configValue) {
        def a =   GENERIC_MAP.find{ k,  v ->  v == configValue}
        if (a) {
            return a.key
        }
    }

    public static CountryCode getCountryCode(String configName='countryCode') {
        return ((CountryCode)GENERIC_MAP.get(configName))
    }
    public static CurrencyTypes getCurrencyCode(String configName='currencyCode') {
        return ((CurrencyTypes)GENERIC_MAP.get(configName))
    }


}
