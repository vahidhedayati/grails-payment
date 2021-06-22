>Test Controller
-------------
```groovy
package test.payment

import org.grails.plugin.payment.PaymentUser

class TestController {

    /**
     * All session variables used by plugin - needs to be reset according to your usage of plugin
     * @return
     */
    def clearAll() {
        session.user =null
        session.cart=[]
        session.cartCounter=[:]
        session.finalTotal=null
        render template:'menu'
        return
    }

    /**
     * Test checkout
     * Action index: has no variables set
     * index.gsp hard codes parameters set to
     * payment:checkout
     */
    def index() {}

    /**
     * Test checkout with user who has existing address
     * Action testUser is same as index instead variables come from controller
     * or a service or where ever feeding controller
     * it also attempts to load the first User entry so a user must be setup
     * payment:checkout
     */
    def testUser() {
        Map instance=[:]
        instance.editCartUrl=g.createLink(controller:'test', action:'checkout')
        //description used by doPayment in PaymentService when finalising sale
        instance.cart=[
                [id:1, name:'item 1', description: 'something',  currency:'GBP', listPrice:1.10],
                [id:2, name:'item 2', description: 'something', currency:'GBP', listPrice:1.00],
                [id:1, name:'item 1',  description: 'something', currency:'GBP', listPrice:1.10],
                [id:1, name:'item 1',  description: 'something', currency:'GBP', listPrice:1.10],
                [id:3, name:'item 3',  description: 'something',currency:'GBP', listPrice:0.50],
                [id:4, name:'item 4',  description: 'something', currency:'GBP', listPrice:0.50],
        ]
        instance.finalTotal=2.50
        instance.subTotal=12.50
        instance.user= PaymentUser?.first()

        render view:'testUser', model:[instance:instance]
    }

    /**
     * Test Form buttons alone
     */
    def testButtons() {

    }


    /**
     * Test form buttons just like above but params or map sent by controller instead
     * @return
     *   //override calculation from cart items below with your own final figure
     *                   finalTotal: 1.10,
     */
    def testControllerButtons() {
        Map instance=[

                currencyCode: 'GBP',  //defaults to PaymentConfig value if not set
                editCartUrl:g.createLink(controller:'test', action:'checkout'),
                cart:[
                        [id:1, name:'item 1',  currency:'GBP', listPrice:1.10],
                        [id:2, name:'item 2',  currency:'GBP', listPrice:1.00],
                        [id:1, name:'item 1',  currency:'GBP', listPrice:1.10],
                        [id:1, name:'item 1',  currency:'GBP', listPrice:1.10],
                        [id:3, name:'item 3',  currency:'GBP', listPrice:0.50],
                        [id:4, name:'item 4',  currency:'GBP', listPrice:0.50],
                ],
                address:[
                        title:'Mr',
                        firstName:'fred',
                        lastName: 'Smith',
                        line1: '1aaaqqa Long lane',
                        line2: 'Bigaaa street',
                        city: 'London',
                        state:'x',
                        country: 'United Kingdom',
                        countryCode: 'GB',  //should default to set value in PaymentConfig if not set
                        postcode:'se11at',
                        telephone:'12345',
                        emailAddress:'fred@example.fred.smith.a11.a10.com',
                        username: 'fred@example.fred.smith.a11.a10.com',
                        remoteCall:true,
                ]
        ]

        render view:'testControllerButtons', model:[instance:instance]
    }


    /**
     * Test sessions set before calling plugin checkout method of plugin
     * This loads cart as per your session values
     * @return plugin payment checkout method
     */
    def sessionTest() {
        session.cart = [
                [id:1, name:'session item 1',  currency:'GBP', listPrice:1.10],
                [id:1, name:'session item 1',  currency:'GBP', listPrice:1.10],
                [id:1, name:'session item 1',  currency:'GBP', listPrice:1.10],
                [id:2, name:'session item 2',  currency:'GBP', listPrice:1.00],
                [id:3, name:'session item 3',  currency:'GBP', listPrice:1.10],
                [id:4, name:'session item 4',  currency:'GBP', listPrice:1.10],
                [id:3, name:'session item 3',  currency:'GBP', listPrice:0.50],
                [id:4, name:'session item 4',  currency:'GBP', listPrice:0.50],
        ]
        session.cartCounter=[:]
        Map cartCounter=[:]
        List<Long> doneIds = []
        List items =  session.cart
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
                cartCounter[item.id]=qty
            }
        }
        session.cartCounter = cartCounter
        redirect(controller:'payment', action:'checkout')
    }

}
```
>/test/_menu.gsp

```gsp
<g:link action="testButtons">Test payment:buttons</g:link> |
<g:link action="testControllerButtons">Test Controller payment:buttons</g:link> |
<g:link action="index">Test payment:checkout</g:link> |
<g:link action="testUser">Test payment:checkout with a user</g:link> |
<g:link action="sessionTest">session test</g:link> |
<g:link action="clearAll">clear session data</g:link> |
```

>/test/index.gsp

```gsp
<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Welcome to Grails</title>
</head>
<body>
<h4><g:render template="menu"/></h4>
<payment:checkout instance="[
        editCartUrl:g.createLink(controller:'test', action:'checkout'),
        cart:[
                [id:1, name:'item 1',  currency:'GBP', listPrice:1.10],
                [id:2, name:'item 2',  currency:'GBP', listPrice:1.00],
                [id:1, name:'item 1',  currency:'GBP', listPrice:1.10],
                [id:1, name:'item 1',  currency:'GBP', listPrice:1.10],
                [id:3, name:'item 3',  currency:'GBP', listPrice:0.50],
                [id:4, name:'item 4',  currency:'GBP', listPrice:0.50],
        ],
        finalTotal:2.50,
        subTotal:12.50
]"
/>
</body>
</html>
```

>/test/testButtons.gsp
```gsp
<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Welcome to Grails</title>
</head>
<body>
<h4><g:render template="menu"/></h4>
<payment:buttons instance="${[
        finalTotal: 1.00,  //override calculation from cart items below with your own final figure
        currencyCode: 'GBP',  //defaults to PaymentConfig value if not set
        editCartUrl:g.createLink(controller:'test', action:'checkout'),
        address:[
                title:'Mr',
                firstName:'fred from gsp',
                lastName: 'Smith in GSP',
                line1: '1 GSP Page',
                line2: 'set by gsp',
                city: 'GrailsServerPage',
                state:'GSP State',
                country: 'United Kingdom',
                countryCode: 'GB',  //should default to set value in PaymentConfig if not set
                postcode:'se11at',
                username: 'fredisinit@headhight.com',
                remoteCall:true,
                telephone:'12345',
                emailAddress:'fredisinit@headhight.com',

        ]
]}"
/>
</body>
</html>
```

>/test/testControllerButtons.gsp
```gsp
<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Welcome to Grails</title>
</head>
<body>
<h4><g:render template="menu"/></h4>
<payment:buttons instance="${instance}"/>
</body>
</html>
```

>/test/testUser.gsp
```gsp
<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Welcome to Grails</title>
</head>
<body>
<h4><g:render template="menu"/></h4>
<payment:checkout instance="${instance}"/>
</body>
</html>
```