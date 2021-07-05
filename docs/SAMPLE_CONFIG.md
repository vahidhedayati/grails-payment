
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
        secretKey = 'YOUR_LIVE_SECRET_KEY'
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