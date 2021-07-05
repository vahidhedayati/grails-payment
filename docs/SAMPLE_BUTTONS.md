
### 1. Buttons only feature

>controller/TestController
```groovy
 /**
 * Test checkout with user who has existing address
 * Action testUser is same as index instead variables come from controller
 * or a service or where ever feeding controller
 * it also attempts to load the first User entry so a user must be setup
 * payment:checkout
 * 
 * The cart items is a flat list of items so if item id:1 is clicked twice it is listed twice
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
 // if you set wrong finalTotal paypal will complain your items above cost is more than your given total
 // you do not need to provide 2 below variables. If not set plugin will figure out based on items in your list above
 //instance.finalTotal=2.50    
 //instance.subTotal=12.50
 
 // optional if you have an existing user set it here at the moment the form will auto fill address details  
 instance.user= PaymentUser?.first()

 render view:'testUser', model:[instance:instance]
}

```

>/views/test/testuser  <payment:buttons
```gsp
     <!--CREDIT CART PAYMENT-->
            <payment:buttons instance="${instance}"  />
```


Everything the user provides on the checkout page is captured in
[AddressBean](https://github.com/vahidhedayati/grails-payment/blob/main/src/main/groovy/org/grails/plugin/payment/beans/AddressBean.groovy)
So you need to ensure your instance has the user details / address details set provided
your instance should look like:


#### You can also provide address details and populate form like this
>/views/test/index.gsp
```gsp
     <!--CREDIT CART PAYMENT-->
            <payment:buttons instance="${[
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
                        overrideErrorHalts:true,
                ]
        ]
}" 
            />
```




With button feature you could probably get away as per example with just the user details and a finalTotal
so if you are just capturing a charge maybe a donation without cart items you could follow this example:


```gsp
<payment:buttons instance="${[
        // this buttons instance has no cart items just a total
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
                overrideErrorHalts:true,
                telephone:'12345',
                emailAddress:'fredisinit@headhight.com',

        ]
]}"
/>
```


