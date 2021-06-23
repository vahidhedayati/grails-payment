Grails Payment plugin
---

### Author: Vahid Hedayati 
Date: 18th June 2021 

Grails Payment Plugin written in Grails 4

Installation for a grails 4 application
-----
#### Add dependency to `build.gradle`:

```

dependencies {

 
  compile "io.github.vahidhedayati:payment:1.0.1"

  //You will only need this if you are looking to use square payment
  compile 'org.jetbrains.kotlin:kotlin-stdlib:1.3.70'

}
```


Supports 3 payment systems that you can simply add to your site:

### Paypal
### Stripe
### Square 

It provides a web interface so you can update database and underlying configuration listener with following details: 
> Enable / Disable any / all providers as you like 
>
> Change from SANDBOX to LIVE on any or all give providers
> 
> Change sandbox / live secret / public keys for any / all providers.
> 

All underlying providers use the listener configuration values and as any change is made the provides will instantly pick up those effects.

 


Getting plugin to work on a sample site:
---

Copy [SampleApplication.groovy](https://github.com/vahidhedayati/grails-payment/blob/main/grails-app/conf/sampleApplication.groovy) from conf folder in plugin to your app as `/grails-app/conf/application.groovy`
Update according to your provider keys / configuration:
```groovy


payment {
    currencyCode = org.grails.plugin.payment.enums.CurrencyTypes.GBP
    countryCode = org.grails.plugin.payment.enums.CountryCode.GB
    hostName = "http://localhost:8080"
    square {
        applicationId = 'LIVE_APP_ID'
        accessToken = 'LIVE_ACCESS_TOKEN'
        location='LOC1' //get this from location tab in square api dev console
        applicationSecret = 'LIVE APP SECRET'  //oauth
        enabled=true
        mode='sandbox'  // choose sandbox or live in web interface
        sandbox {
            applicationId = 'sandbox-sq0111-S1sssd3232ssA'
            accessToken = 'EAAAExxdeee22wwwwwww2aaaaakkty'
            applicationSecret = 'sandbox-sqsww-ss222222sssssssssssT-tQsaq2o'  //oauth
            location='Lxxsa34aa3'
        }
    }


    paypal{
        enabled=true
        mode='sandbox'  // choose sandbox or live in web interface
        email="your_paypal@emailaddress.com"
        clientId = 'AZsdfdsfsrewrwerwesddsfdsfsdfdsfsdfsgswresdfdsfgfdsfsdfd-1'
        clientSecret = 'EEsdfdsfsdfsfsdfdsfsdfsdfdsfdsfsdfdsfdsfdsfddsdsdsdsffds_oi'
        endpoint = "https://api.paypal.com"
        sandbox{
            email='sb-sdfsdfsdf2@business.example.com'
            clientId = 'AZsdfdsfdsfdsfdsfdsdf-sdfsdfdsfdsfdsfsdfsdfdsfsdfsdfdsfddsf-1'
            clientSecret ='EEsdfdsfdsfdsfsdfdsfdsfdsfdsdfsdsdfsdsdfddffddsfdf_oi'
            endpoint = "https://api.sandbox.paypal.com"
        }
    }
    stripe {
        ecretKey = 'YOUR_LIVE_SECRET_KEY'
        publishableKey = 'YOUR_LIVE_PUBLISHABLE_KEY'
        enabled=true
        mode='sandbox'  // choose sandbox or live in web interface
        test {
            secretKey='sk_test_51dsfdsfdsfdsfdsfdsfsfsdfsdfdfdfdsdfDQ'
            publishableKey= 'pk_test_51sdfdsfdsfdsfdsfdsdsfdfsdsdsdssdsfdsddsdfddfsdsd9'
        }
    }

}
```






Then update init/{package}/`BootStrap.groovy`
This simply loads in application.groovy's configuration into PaymentConfig domain class and copies all 
configuration values to a listener which updates all providers immediately  
```groovy
class BootStrap {

    def paymentService
    def init = { servletContext ->

        paymentService.addPaymentConfig()
    }
    def destroy = {
    }
}
```

Start up your test site, there will now be an additional 2 controllers available: 

>>http://localhost:8080/payment   you will be working with:
> 
>>> http://localhost:8080/payment/checkout
> 
 >>http://localhost:8080/paymentConfig 

Full walkthrough video 
--
>> [You-Tube Video showing plugin in use](https://www.youtube.com/watch?v=U4iCEBeRiYs)
>
>> [You-Tube Video Part 2 easier usage of plugin explained](https://www.youtube.com/watch?v=llHpo8H90M4)


How to use plugin locally
---
By installing the plugin you get the working steps to make payments work and all of the code to execute run it.
If what you see in video is sufficient then all you need is the buttons

Where you would pass instance which contains what the sample [checkout](https://github.com/vahidhedayati/grails-payment/blob/main/grails-app/views/payment/checkout.gsp)
as part of the `instance` variable this would be all relevant input, all providers paypal, stripe & square have been fully implemented to create a customer on their systems which takes all of your users input by this I mean their address etc is also passed to the third party 
and is also returned in their final response back to you.

To figure out what variables / parameters are required refer to [CartBean](https://github.com/vahidhedayati/grails-payment/blob/main/src/main/groovy/org/grails/plugin/payment/beans/CartBean.groovy)

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
                        remoteCall:true,
                ]
        ]
}" 
            />
```


### 2. Full checkout feature

>controller/TestController

```groovy
/**
     * Test checkout
     * Action index: has no variables set
     * index.gsp hard codes parameters set to
     * payment:checkout
     */
    def index() {}
```

>/test/index.gsp <payment:checkout

Instance is populated in gsp in this case but could be like first 
example where instance comes from controller. 
the instance model is quite flexible you can provide everything or as little as possible

 
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
       
]"
/>
</body>
</html>

```

For buttons you could probably get away as per example with just the user details and a finalTotal 
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
                remoteCall:true,
                telephone:'12345',
                emailAddress:'fredisinit@headhight.com',

        ]
]}"
/>
```


#### Address  binding some variables explained:

If no `username` is provided it will set it or use `emailAddress` as `username`
The remoteCall is a new variable introduced and if you wish to override username taken errors
set this to true so in case of buttons it will seamlessly work with existing users who placed orders etc

The plugin will try to logically work out if a given set of details is a true match to existing data.
If user is an existing user and the address details of line1 and line2 are identical then it will load up the existing user
If it's an existing user but the address details a different then in typical checkout mode it would complain username is taken
with `buttons` mode you can enable `remoteCall:true` the system will know to generate an alternative unused username to allow 
a new sign up to complete sale. So perhaps in the case of buttons you want to enable this feature.  

``` groovy
 address:[
    ..
    //By default false
    remoteCall:true  
    //by default false
    saveInfo:true 
    
    emailAddress='required@domain.com'  
    username=''
    ]
```




The `CartBean` has a load more inputs most of it set by system.
The above example is the basic parameters and format required to be posted 
for buttons to actually post correct information to selected provider.
Front end currently asks for password and confirmation this is because it came from a template 
that signed up user onto system via spring security, this plugin does not provide spring security 

The buttons use vanilla JavaScript to control the basic stuff that it does, it is all to give you an idea 
The buttons and processes can be tweaked by copying locally and updating.

By all means if you improve plugin get in touch and improve for everyone else too




## Paypal steps:
Step 1 [paypalcheckout](https://github.com/vahidhedayati/grails-payment/blob/main/grails-app/controllers/org/grails/plugin/payment/PaymentController.groovy#L94) 

Step 2 [uploadCart](https://github.com/vahidhedayati/grails-payment/blob/main/grails-app/controllers/org/grails/plugin/payment/paypal/PaypalController.groovy#L16)

Step 3 [execute](https://github.com/vahidhedayati/grails-payment/blob/main/grails-app/controllers/org/grails/plugin/payment/paypal/PaypalController.groovy#L125)

step 4 [cancel/thanks](https://github.com/vahidhedayati/grails-payment/blob/main/grails-app/controllers/org/grails/plugin/payment/paypal/PaypalController.groovy#L149-L169)


## Square steps:

Step 1 [squarecheckout](https://github.com/vahidhedayati/grails-payment/blob/main/grails-app/controllers/org/grails/plugin/payment/PaymentController.groovy#L143)

step 2 [thanks](https://github.com/vahidhedayati/grails-payment/blob/main/grails-app/controllers/org/grails/plugin/payment/PaymentController.groovy#L40)


## Stripe steps:

Step 1 [stripecheckout](https://github.com/vahidhedayati/grails-payment/blob/main/grails-app/controllers/org/grails/plugin/payment/PaymentController.groovy#L212)

Step 2 [thanks](https://github.com/vahidhedayati/grails-payment/blob/main/grails-app/controllers/org/grails/plugin/payment/PaymentController.groovy#L40)



## [Sample Controller / views](https://github.com/vahidhedayati/grails-payment/blob/main/SAMPLE_CONTROLLER_VIEWS.md)