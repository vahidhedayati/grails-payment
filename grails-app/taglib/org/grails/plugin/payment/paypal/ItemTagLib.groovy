package org.grails.plugin.payment.paypal

import org.grails.plugin.payment.listeners.PaymentConfigListener

class ItemTagLib {

    static namespace = "item"
    static defaultEncodeAs = [taglib:'text']

    def each = { attrs, body ->
        List items = (List) attrs?.cart
        if (items && items?.size()>0 ) {
            Map cartCounter=[:]
            List<Long> doneIds = []

            if (!session?.cart && items) {
                session.cart=items
                session.cartCounter=[:]
            }


            items?.sort { a, b -> a?.id <=> b?.id }.each { item ->
                Integer qty = items?.findAll{it?.id == item?.id}?.size() ?: 1
                Long id = item?.id as Long
                BigDecimal itemTotal = item?.listPrice * qty
                if (id && !doneIds.contains(id as Long)) {
                    def itemInfo = [:]
                    itemInfo.item=item
                    itemInfo.qty=qty
                    itemInfo.itemTotal=itemTotal
                    cartCounter[item.id]=qty
                    session.cartCounter = cartCounter
                    doneIds.push(item.id as Long)
                    out << body(itemInfo)
                }
            }
        } else {
            // plugin must be in test mode - so we will just output dummy data
            Map cartCounter=[:]
            List cart=[]
            for (n in 1..3) {

                def item = [:]
                item.id=n
                item.name='Example '+n
                item.currency='GBP'
                item.listPrice=n*n

                def itemInfo = [:]
                itemInfo.item=item
                itemInfo.qty=n
                itemInfo.itemTotal=item.listPrice* itemInfo.qty

                cart << item
                cartCounter[item.id]=n
                if (!session?.cart) {
                    session.cart=cart
                    session.cartCounter=cartCounter
                }
                out << body(itemInfo)
            }

        }

    }

    def currentTotal = { attrs->
        if (attrs.finalTotal||(session.cart && session.cart?.size()>0)) {
            if (!attrs.finalTotal) {
                attrs.finalTotal =  session?.cart*.listPrice?.sum()
            }
            if (attrs.finalTotal) {
                if (!session?.finalTotal) {
                    session.finalTotal = attrs.finalTotal
                }
                if (attrs.showHiddenField) {
                    out <<  g.hiddenField(name:'finalTotal', value:BigDecimal.valueOf(attrs.finalTotal as int).toString())
                }
                out << g.formatNumber(number:BigDecimal.valueOf(attrs.finalTotal as int),type:'currency',
                        currency:attrs?.currency?: PaymentConfigListener.currencyCode.toString())
            }

        } else {
            // plugin must be in test mode - so we will just output dummy data
            int itemTotal=0
            for (n in 1..3) {
                itemTotal+=n*n
            }
            if (attrs.showHiddenField) {
                out <<  g.hiddenField(name:'finalTotal', value:BigDecimal.valueOf(itemTotal).toString())
            }
            out << g.formatNumber(number:BigDecimal.valueOf(itemTotal),type:'currency',
                    currency:attrs?.currency?: PaymentConfigListener.currencyCode.toString())
        }
    }
}
