package org.grails.plugin.payment.util

import com.fasterxml.uuid.Generators

class PaymentHelper {

    public static UUID getUuid () {
        return Generators.timeBasedGenerator().generate();
    }

    public static String getIdempotencyKey() {
        return uuid.toString()
    }
}
