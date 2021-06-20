package org.grails.plugin.payment.paypal

class ExampleTagLib {

    static namespace = "example"
    static defaultEncodeAs = [taglib:'text']

    def each = { attrs, body ->
        for (n in 1..3) {
            def itemInfo = [:]
            itemInfo.itemId=n
            itemInfo.itemName='item name '+n
            itemInfo.qty=n
            itemInfo.currency='GBP'
            itemInfo.itemTotal=n*n
            out << body(itemInfo)
        }
    }

    def currentTotal = { attrs, body ->

            out << body([finalTotal:2.00, currency:'GBP'])

    }
}
