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
 
  compile "io.github.vahidhedayati:payment:1.0.2"

  //You will only need this if you are looking to use square payment
  compile 'org.jetbrains.kotlin:kotlin-stdlib:1.3.70'

}
```


## Supports 3 payment systems: PayPal, Stripe & Square
 

A web interface is provided to allow configuration changes of the following aspects: 
> Enable / Disable any / all providers as you like 
>
> Change from SANDBOX to LIVE on any or all give providers
> 
> Change sandbox / live secret / public keys for any / all providers.
> 

All underlying providers use a concurrent hashmap which load are referred to by plugin as real configuration settings.

 


Getting plugin to work on a sample site:
---

## Step 1: [Sample Config](https://github.com/vahidhedayati/grails-payment/blob/main/docs/SAMPLE_CONFIG.md)

Copy [SampleApplication.groovy](https://github.com/vahidhedayati/grails-payment/blob/main/grails-app/conf/sampleApplication.groovy) from conf folder of this  
plugin to your app as `/grails-app/conf/application.groovy` & update according to your provider key / configuration.


## Step 2: Update init/{package}/`BootStrap.groovy`
This will trigger all above configuration defined values to load from config onto Database and from Database to a 
concurrent hashmap containing all configuration values, allowing dynamic configuration change by product owner from a 
web interface:    
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



### [Checkout feature](https://github.com/vahidhedayati/grails-payment/blob/main/docs/SAMPLE_CHECKOUT.md)

### [Button only feature](https://github.com/vahidhedayati/grails-payment/blob/main/docs/SAMPLE_BUTTONS.md)

### [Sample Controller / views](https://github.com/vahidhedayati/grails-payment/blob/main/docs/SAMPLE_CONTROLLER_VIEWS.md)



Start up your test site, there will now be an additional 2 controllers available: 

>>http://localhost:8080/payment   you will be working with:
> 
>>> http://localhost:8080/payment/checkout
> 
 >>http://localhost:8080/paymentConfig 

Full walk through video 
--
>> [You-Tube Video showing plugin in use](https://www.youtube.com/watch?v=TJUeMjrMW3U)


How to use plugin locally
---
By installing the plugin you get the working steps to make payments work and all of the code to execute run it.
If what you see in video is sufficient then all you need is the buttons

Where you would pass instance which contains what the sample [checkout](https://github.com/vahidhedayati/grails-payment/blob/main/grails-app/views/payment/checkout.gsp)
as part of the `instance` variable this would be all relevant input, all providers paypal, stripe & square have been fully implemented to create a customer on their systems which takes all of your users input by this I mean their address etc is also passed to the third party 
and is also returned in their final response back to you.

To figure out what variables / parameters are required refer to [CartBean](https://github.com/vahidhedayati/grails-payment/blob/main/src/main/groovy/org/grails/plugin/payment/beans/CartBean.groovy)


### [Further read on Address Binding & CartBean](https://github.com/vahidhedayati/grails-payment/blob/main/docs/ADDRESS_BINDING.md)


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

