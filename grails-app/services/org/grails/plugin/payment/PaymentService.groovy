package org.grails.plugin.payment

import com.paypal.api.payments.Transaction
import com.paypal.orders.AddressPortable
import com.paypal.orders.LinkDescription
import com.paypal.orders.MerchantReceivableBreakdown
import com.paypal.orders.PaymentCollection
import com.squareup.square.models.CreatePaymentResponse
import grails.core.GrailsApplication
import grails.gorm.transactions.Transactional
import grails.validation.ValidationException
import org.grails.plugin.payment.beans.CartBean
import org.grails.plugin.payment.enums.CountryCode
import org.grails.plugin.payment.enums.PaymentStatus
import org.grails.plugin.payment.listeners.PaymentConfigListener
import org.grails.plugin.payment.paypal.BuyerInformation
import org.grails.plugin.payment.paypal.PaypalPayment
import org.grails.plugin.payment.paypal.PaypalSale
import org.grails.plugin.payment.square.SquarePayment
import org.grails.plugin.payment.stripe.StripePayment
import org.springframework.transaction.annotation.Propagation
import org.springframework.web.context.request.RequestContextHolder

import javax.servlet.http.HttpSession

class PaymentService {

    GrailsApplication grailsApplication

    @Transactional
    def addPaymentConfig() {
        PaymentConfig paymentConfig = PaymentConfig?.first()
      
        if (!paymentConfig) {
   
            Map params = [:]
        
            params.paypalEnabled = grailsApplication?.config?.payment?.paypal?.enabled
            params.paypalEmail = grailsApplication?.config?.payment?.paypal?.email
            params.paypalClientId = grailsApplication?.config?.payment?.paypal?.clientId
            params.paypalClientSecret = grailsApplication?.config?.payment?.paypal?.clientSecret
            params.paypalEndpoint = grailsApplication?.config?.payment?.paypal?.endpoint
            params.paypalSandboxEmail = grailsApplication?.config?.payment?.paypal?.sandbox?.email
            params.paypalSandboxClientId = grailsApplication?.config?.payment?.paypal?.sandbox?.clientId
            params.paypalSandboxClientSecret = grailsApplication?.config?.payment?.paypal?.sandbox?.clientSecret
            params.paypalSandboxEndpoint = grailsApplication?.config?.payment?.paypal?.sandbox?.endpoint
            params.paypalMode= grailsApplication?.config?.payment?.paypal?.mode

            params.stripeEnabled = grailsApplication?.config?.payment?.stripe?.enabled
            params.stripeMode = grailsApplication?.config?.payment?.stripe?.mode
            params.stripeSecretKey = grailsApplication?.config?.payment?.stripe?.secretKey
            params.stripeTestSecretKey = grailsApplication?.config?.payment?.stripe?.test.secretKey
            params.stripeTestPublishableKey = grailsApplication?.config?.payment?.stripe?.test.publishableKey
            params.stripeApplicationSecret = grailsApplication?.config?.payment?.stripe?.applicationSecret
            params.stripePublishableKey = grailsApplication?.config?.payment?.stripe?.publishableKey

            params.paymentConfigEnabled = grailsApplication?.config?.payment?.paymentConfigEnabled
            params.paymentCheckoutEnabled = grailsApplication?.config?.payment?.paymentCheckoutEnabled

            params.squareSandboxApplicationId = grailsApplication?.config?.payment?.square?.sandbox?.applicationId
            params.squareSandboxAccessToken = grailsApplication?.config?.payment?.square?.sandbox?.accessToken
            params.squareSandboxApplicationSecret = grailsApplication?.config?.payment?.square?.sandbox?.applicationSecret
            params.squareApplicationId = grailsApplication?.config?.payment?.square?.applicationId
            params.squareAccessToken = grailsApplication?.config?.payment?.square?.accessToken
            params.squareSandboxLocation = grailsApplication?.config?.payment?.square?.sandbox?.location
            params.squareLocation = grailsApplication?.config?.payment?.square?.location

            params.squareApplicationSecret = grailsApplication?.config?.payment?.square?.applicationSecret
            params.squareEnabled = grailsApplication?.config?.payment?.square?.enabled
            params.squareMode = grailsApplication?.config?.payment?.square?.mode
            params.hostName = grailsApplication?.config?.payment?.hostName
         
           
            paymentConfig = new PaymentConfig(params)
            if (!paymentConfig.save(flush:true)) {
                log.error "Errors: ${paymentConfig.errors.allErrors}"
            }
          
        }
  
        updatePaymentConfigListener(paymentConfig)
    }


    def updatePaymentConfigListener(PaymentConfig paymentConfig) {
        if (paymentConfig && paymentConfig?.id) {

            PaymentConfigListener.updateGeneric('paymentConfigEnabled',(paymentConfig?.paymentConfigEnabled).toString())
            PaymentConfigListener.updateGeneric('paymentCheckoutEnabled',(paymentConfig?.paymentCheckoutEnabled).toString())

            PaymentConfigListener.updateGeneric('paypalEnabled',(paymentConfig?.paypalEnabled).toString())

            if (paymentConfig?.paypalEmail) {
                PaymentConfigListener.updateGeneric('paypalEmail', paymentConfig?.paypalEmail)
            }
            if (paymentConfig?.paypalClientId) {
                PaymentConfigListener.updateGeneric('paypalClientId', paymentConfig?.paypalClientId)
            }
            if (paymentConfig?.paypalClientSecret) {
                PaymentConfigListener.updateGeneric('paypalClientSecret', paymentConfig?.paypalClientSecret)
            }
            if (paymentConfig?.paypalEndpoint) {
                PaymentConfigListener.updateGeneric('paypalEndpoint', paymentConfig?.paypalEndpoint)
            }
            if (paymentConfig?.paypalSandboxEmail) {
                PaymentConfigListener.updateGeneric('paypalSandboxEmail', paymentConfig?.paypalSandboxEmail)
            }
            if (paymentConfig?.paypalSandboxClientId) {
                PaymentConfigListener.updateGeneric('paypalSandboxClientId', paymentConfig?.paypalSandboxClientId)
            }
            if (paymentConfig?.paypalSandboxClientSecret) {
                PaymentConfigListener.updateGeneric('paypalSandboxClientSecret', paymentConfig?.paypalSandboxClientSecret)
            }
            if (paymentConfig?.paypalSandboxEndpoint) {
                PaymentConfigListener.updateGeneric('paypalSandboxEndpoint', paymentConfig?.paypalSandboxEndpoint)
            }
            if (paymentConfig?.paypalMode) {
                PaymentConfigListener.updateGeneric('paypalMode', paymentConfig?.paypalMode)
            }


            PaymentConfigListener.updateGeneric('stripeEnabled',(paymentConfig?.stripeEnabled).toString())
            if (paymentConfig?.stripeMode) {
                PaymentConfigListener.updateGeneric('stripeMode', paymentConfig?.stripeMode)
            }

            if (paymentConfig?.stripePublishableKey) {
                PaymentConfigListener.updateGeneric('stripePublishableKey', paymentConfig?.stripePublishableKey)
            }
            if (paymentConfig?.stripeTestPublishableKey) {
                PaymentConfigListener.updateGeneric('stripeTestPublishableKey', paymentConfig?.stripeTestPublishableKey)
            }

            if (paymentConfig?.stripeSecretKey) {
                PaymentConfigListener.updateGeneric('stripeSecretKey', paymentConfig?.stripeSecretKey)
            }
            if (paymentConfig?.stripeTestSecretKey) {
                PaymentConfigListener.updateGeneric('stripeTestSecretKey', paymentConfig?.stripeTestSecretKey)
            }

            PaymentConfigListener.updateGeneric('squareEnabled',(paymentConfig?.squareEnabled).toString())
            if (paymentConfig?.squareMode) {
                PaymentConfigListener.updateGeneric('squareMode', paymentConfig?.squareMode)
            }
            if (paymentConfig?.squareSandboxApplicationId) {
                PaymentConfigListener.updateGeneric('squareSandboxApplicationId', paymentConfig?.squareSandboxApplicationId)
            }
            if (paymentConfig?.squareApplicationId) {
                PaymentConfigListener.updateGeneric('squareApplicationId', paymentConfig?.squareApplicationId)
            }

            if (paymentConfig?.squareAccessToken) {
                PaymentConfigListener.updateGeneric('squareAccessToken', paymentConfig?.squareAccessToken)
            }
            if (paymentConfig?.squareSandboxAccessToken) {
                PaymentConfigListener.updateGeneric('squareSandboxAccessToken', paymentConfig?.squareSandboxAccessToken)
            }

            if (paymentConfig?.squareSandboxApplicationSecret) {
                PaymentConfigListener.updateGeneric('squareSandboxApplicationSecret', paymentConfig?.squareSandboxApplicationSecret)
            }

            if (paymentConfig?.squareApplicationSecret) {
                PaymentConfigListener.updateGeneric('squareApplicationSecret', paymentConfig?.squareApplicationSecret)
            }


            if (paymentConfig?.squareLocation) {
                PaymentConfigListener.updateGeneric('squareLocation', paymentConfig?.squareLocation)
            }
            if (paymentConfig?.squareSandboxLocation) {
                PaymentConfigListener.updateGeneric('squareSandboxLocation', paymentConfig?.squareSandboxLocation)
            }
            
            if (paymentConfig?.hostName) {
                PaymentConfigListener.updateGeneric('hostName', paymentConfig?.hostName)
            }

            if (paymentConfig?.currencyCode) {
                PaymentConfigListener.updateGeneric( paymentConfig?.currencyCode)
            }

            if (paymentConfig?.countryCode) {
                PaymentConfigListener.updateGeneric(paymentConfig?.countryCode)
            }
        }
    }

    def checkout(CartBean bean) {
        setCartUserAddress(bean)
        Map result=[:]
        if (bean.user) {
            result = addPayment(bean)
        }
        return result
    }

    @Transactional
    SquarePayment finaliseSquareCheckout(CartBean bean, CreatePaymentResponse response ) {
        SquarePayment payment
        if (bean.user) {
            payment = new SquarePayment()
            payment = doPayment((BasePayment) payment, bean)
            payment.completed = true
            payment.token = bean.squareToken
            payment.squareChargeId = response.payment.id
            payment.squareStatus = response.payment.status
            payment.description = bean.purchaseDescription
            payment.transactionId = "${payment.transactionIdPrefix}-${bean.user?.id}-${System.currentTimeMillis()}"
            payment.amount = bean.finalTotal
            //re-write this value since stripe works by pennies over doPayment method which sets for paypal and this
            payment.gross = payment.amount
            payment.status = PaymentStatus.PAID
            if (!payment.save(flush: true)) {
                log.error "${payment.errors.allErrors}"
            }
            updateProductStock((BasePayment)payment)
        }
        return payment
    }


    StripePayment finaliseStripeCheckout(CartBean bean) {
        StripePayment payment
        if (bean.user) {
            payment = finaliseStripePayment(bean)
        }
        return payment
    }

    CartBean setCartUserAddress(CartBean bean ) {
        if (!bean.user && !bean.address) {
            bean.errors.reject('noUserAddress.label', 'Required details are missing from form')
            throw new ValidationException("Required information is missing", bean.errors)
        }
        if (bean?.address && bean.address?.isNewSignup) {
            // This is a brand new sign up not bound to any existing user
            if (bean?.address.username) {
                PaymentUser user = PaymentUser.findByUsername(bean?.address.username)
                if (user) {
                    bean.errors.reject('usernameTaken.label', 'Username already in use')
                    throw new ValidationException("Duplicate username Exception", bean.errors)
                }
            }

            bean.address.primaryAddress=true
            def result = addUser(bean.address.loadValues().clone())
            bean.user = result?.user
            bean.selectedAddress = result?.address
        } else {
            if (!bean.user) {
                // Since we have no user we will secretly add this as a user bound to their required emailAddress
                if (!bean.address.username && bean.address.emailAddress && bean.address.saveInfo)  {
                    bean.address.username=bean.address.emailAddress
                } else if (!bean.address.username && bean.address.emailAddress && !bean.address.saveInfo)  {
                    def result =detectUserInfo(bean.address.loadValues().clone())
                    bean.selectedAddress  = result?.address ? result.address : null
                    bean.user = result?.user ? result.user : null
                    if (result.username && !bean?.user) {
                        bean.address.username=result.username
                        bean.address.primaryAddress=true
                        result = addUser(bean.address.loadValues().clone())
                        bean.user = result?.user
                        bean.selectedAddress = result?.address
                    }
                } else {
                    PaymentUser user
                    if (bean?.address.username) {
                        user = PaymentUser.findByUsername(bean?.address.username)
                        if (user) {
                            bean.errors.reject('usernameTaken.label', "Username ${bean?.address?.username} already in use ")
                            throw new ValidationException("Duplicate username Exception", bean.errors)
                        }
                    }
                    // Even though not requested we want to add them to the system
                    def result = addUser(bean.address.loadValues().clone(), user)
                    if (result?.user) {
                        bean.user = result?.user
                    }
                    //This way we have a structured address stored and accessible like any other user
                    bean.selectedAddress = result?.address
                }

            } else {
                if (bean?.address?.id && !bean?.newAddress?.firstName) {
                    bean.address.saveInfo=true
                    bean.selectedAddress = bean.user.addresses?.find{it.id == bean.address?.id}
                    if (bean.selectedAddress) {
                        bean.address =bean.selectedAddress.addressBean
                    }
                } else {
                    if (bean.newAddress && bean.newAddress.firstName && bean?.newAddress?.line1) {
                        bean.selectedAddress = addAddress(bean.user, bean.newAddress.loadValues().clone())
                    } else {
                        // This block is hit if it is an existing user
                        if (bean?.address && bean?.address?.line1 ) {
                            if (!bean?.address?.firstName && bean?.user?.firstName) {
                                bean?.address?.firstName=bean?.user?.firstName
                                bean?.address?.lastName=bean?.user?.lastName
                            }
                            // like to hit here for 2 reasons - either odd account like system
                            // or alternative address
                            bean.selectedAddress = addAddress(bean.user, bean.address.loadValues().clone())
                        }
                    }
                }
            }
        }

        if (!bean.selectedAddress) {
            bean.errors.reject('issueWithAddress.label', 'There was an issue selecting address')
            throw new ValidationException("Address bind failed", bean.errors)
        }
        return bean
    }



    private HttpSession getSession() {
        return RequestContextHolder.currentRequestAttributes().getSession()
    }

    @Transactional
    def addPayment(CartBean bean) {
        PaypalPayment payment = new PaypalPayment()
        payment = doPayment((BasePayment) payment, bean)
        if (!payment.save(flush:true)) {
            log.error "${payment.errors.allErrors}"
        }
        return  [transactionId: payment.transactionId, paymentId: payment?.id]
    }

    @Transactional
    def finaliseStripePayment(CartBean bean) {
        StripePayment payment = new StripePayment()
        payment  = doPayment((BasePayment) payment, bean)
        payment.completed = true
        payment.token = bean.stripeToken
        payment.stripeChargeId=bean.stripe.id
        payment.stripeStatus = bean.stripe.status
        payment.balanceTransaction = bean.stripe.balanceTransaction
        payment.description= bean.stripe.description
        payment.transactionId = "${payment.transactionIdPrefix}-${bean.user?.id}-${System.currentTimeMillis()}"
        payment.amount=((bean.finalTotal) / 100)
        //re-write this value since stripe works by pennies over doPayment method which sets for paypal and this
        payment.gross=payment.amount
        payment.status= PaymentStatus.PAID
        if (!payment.save(flush:true)) {
            log.error "${payment.errors.allErrors}"
        }
        updateProductStock((BasePayment)payment)
        return payment
    }

    BasePayment doPayment(BasePayment payment, CartBean bean) {
        payment.user  = bean.user
        payment.postalAddress = bean.selectedAddress
        payment.gross = (bean?.finalTotal ?: session?.cart*.listPrice?.sum()?: session?.finalTotal  )
        payment.shipping = (bean?.shipping?:0.00d)
        payment.hideUserDetails = (bean.address.saveInfo == true ? false : true )
        payment.currency = Currency.getInstance(bean?.currencyCode?bean?.currencyCode?.toString():PaymentConfigListener.currencyCode?.toString())
        List doneIds=[]
        bean?.cart?.each { item ->
            if (!doneIds.contains(item.id)) {
                int qty = bean.cartCounter[item.id]

                /**
                 * TODO - this is relying on flat map passed into bean as cart it probably started as a session
                 * or set value by yourself - but it isn't tied to any actual products and is up for manipulation
                 * - you should revisit this segment override service method in local app to call product like
                 * method below to actually load up paymentItem based on what is on your DB -
                 *
                 */


                PaymentItem paymentItem = new PaymentItem()
                paymentItem.itemName =item?.name
                paymentItem.description = item?.description?:item?.name
                paymentItem.itemNumber = item?.id
                paymentItem.amount = item?.listPrice
                paymentItem.quantity = qty

                payment.addToPaymentItems(paymentItem)
                doneIds << item?.id
                log.info "paymentItem  ${paymentItem}"

                // TODO load product information over paymentItem at moment happening from bean.cart as above
                // plugin does not contain products !

               // YourProduct product = YourProduct.get(item.id as Long)
               // PaymentItem paymentItem = new PaymentItem()
               // paymentItem.itemName =product.name
               // paymentItem.description = product?.shortDescription?:''
               // paymentItem.itemNumber = product?.id
               // paymentItem.amount = product?.price
               // paymentItem.quantity = qty
                  // only works with our modified paypal paymentItem
               // paymentItem.weight = product.shipWeight

                //payment.addToPaymentItems(paymentItem)
                doneIds << item?.id
                //log.info "paymentItem  ${paymentItem}"

            }
        }
        return payment
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    def updatePaypalStatusAndDetails(String paymentId,   com.paypal.api.payments.Payment payment) {
        PaypalPayment dbPayment = PaypalPayment?.findByPaypalTransactionId(paymentId)
        if (dbPayment) {
            dbPayment.populateFromPaypal(payment)
            populateDbFromPaypal(payment)

            if (payment.payer.payerInfo?.firstName && payment.payer.payerInfo?.shippingAddress?.line1){
                BuyerInformation buyerInfo = dbPayment?.buyerInformation

                if (!buyerInfo) {
                    buyerInfo = new BuyerInformation()
                }
                buyerInfo.populateFromPaypal(payment.payer.payerInfo)
                //buyerInfo.save()
                if (!buyerInfo.save(flush:true)) {
                    //log.error
                }
                dbPayment.buyerInformation = buyerInfo
            }

            String total = dbPayment?.sales?.collect{it.total}?.first()
            String state = dbPayment?.sales?.collect{it.state}?.first()
            if (total && state) {
                BigDecimal totalValue = BigDecimal.valueOf( new Double(total))
                if (dbPayment?.gross == totalValue && state.toUpperCase() == PaymentStatus.COMPLETED.toString() ) {
                    dbPayment.completed = true
                    dbPayment.status= PaymentStatus.PAID
                }
            }
            if (!dbPayment.save(flush:true)) {
                log.error "PaypalPayment Error: ${dbPayment.errors.allErrors}"
            }
            updateProductStock(dbPayment)

        }

        //if (transactionAllGood && userAllGood) {//}
        return dbPayment
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    def addAddress(PaymentUser user, Map input) {
        return addressAdd(user, input)
    }

    def addressAdd(PaymentUser user, Map input) {
        PaymentAddress address = bindAddress(input)
        if (address && user) {
            PaymentUserPaymentAddress.create(user, address, true)
        }
        return address
    }
    @Transactional
    def addUser(Map input, PaymentUser user = null) {
        PaymentAddress address
        if (!user) {

            if (input?.emailAddress) {
                def result =detectUserInfo(input)
                address = result?.address ? result.address : null
                user = result?.user ? result.user : null
                if (result.username) {
                    input.username=result.username
                }
            }

            if (input?.username && !user) {
                user = PaymentUser.findByUsername(input.username)
            }
        }

        PaymentUser user1
        if (!user) {
            //User.withNewTransaction {
            user1 = new PaymentUser()
            user1.username = input?.username
            user1.firstName = input?.firstName
            user1.lastName = input?.lastName
            user1.emailAddress = input?.emailAddress
            user1.title = input?.title
            user1.save(flush:true)
            address = addressAdd(user1, input)
        }
        return [user:user1?user1:user, address:address]
    }
    /**
     * This is a complex procedure to figure out based on provided info if user has already signed up
     * or is someone else reusing an existing email bound or used for another different address
     * will return a map of user address and username based on what needs to be done by caller
     * username will be dynamically figured out this is for users who say they do not wish to sign up
     * but we need to sign them up to keep order inline with process
     * @param input
     */
    def detectUserInfo(Map input) {
        Map results = [:]
        PaymentUser user = PaymentUser.findByUsername(input?.emailAddress)
        if (user) {
            if (input?.line2) {
                results.address  = user.addresses.find{it.line1 == input?.line1 && it.line2 == input?.line2}
            } else {
                results.address  = user.addresses.find{it.line1 == input?.line1}
            }
            if (!results.address) {
                //This is when the email exists but is bound to probably another address or something
                int found = PaymentUser.findAllByUsernameLike("${input.emailAddress}%")?.size()
                String username= incrementEmailAddress(input.emailAddress,found)
                user = PaymentUser.findByUsername(username)
                if (!user) {
                    results.username=username
                } else {
                    boolean run = true
                    while (user !=null) {
                        username= incrementEmailAddress(input.emailAddress,found)
                        user = PaymentUser.findByUsername(username)
                        results.username = username
                        found++
                    }
                }
            }
            results.user = user
        } else {
            results.username=input.emailAddress
        }
        return results
    }

    String incrementEmailAddress(String emailAddress, int counter) {
        String firstPart = emailAddress.substring(0, emailAddress.indexOf('@'))
        String secondPart = emailAddress.substring(emailAddress.indexOf('@'), emailAddress.length())
        return firstPart+"_sys_"+counter.toString()+secondPart
    }

    @Transactional
    PaymentAddress bindAddress(Map input) {
        if (input?.line1 && input.postcode) {
            PaymentAddress address
            String where=''
            Map whereParams =[:]
            String query = """ from PaymentAddress a """
            if (input?.line1) {
                where = addClause(where, 'a.line1  = :line1')
                whereParams.line1 = input?.line1
            }
            if (input?.line2) {
                where = addClause(where, 'a.line2  = :line2')
                whereParams.line2 = input?.line2
            }
            if (input?.postcode) {
                where = addClause(where, 'a.postcode  = :postcode')
                whereParams.postcode = input?.postcode
            }

            if (input?.countryCode) {
                where = addClause(where, 'a.countryCode  = :countryCode')
                whereParams.countryCode = input?.countryCode
            }


            query += where
            address = PaymentAddress.executeQuery(query, whereParams,[max:1, readOnly:true])[0]

            if (!address||address && !address?.country) {
                address = new PaymentAddress()
                address.countryCode = input.countryCode
                address.country = input?.country
                address.city = input?.city
                address.postcode = input?.postcode
                address.line1 = input?.line1
                address.line2 = input?.line2
                address.emailAddress =input?.emailAddress
                if (input?.telephone) {
                    address.telephone = input?.telephone
                }
                if (input?.firstName) {
                    address.firstName = input.firstName
                }
                if (input?.lastName) {
                    address.lastName = input.lastName
                }
                if (input.title) {
                    address.title = input.title
                }
                address.primaryAddress = (input?.primaryAddress ?: false)
                if (!address.save(flush: true)) {
                    log.error address.errors.allErrors.toString(), address.errors
                }
            }
            return address
        }
    }

    void updateProductStock(BasePayment dbPayment) {
        dbPayment?.paymentItems?.each { PaymentItem paymentItem ->
            Integer soldAmount = (paymentItem?.quantity ?: 1)
            //TODO
           // YourProduct p = YourProduct.get(paymentItem.itemNumber as Long)
           // p.stockCount = p.stockCount - soldAmount
           // p.save(flush:true)
        }
    }


    // @Transactional(propagation = Propagation.REQUIRES_NEW)
    void populateDbFromPaypal(com.paypal.api.payments.Payment payment) {
        List sales = []
        payment?.transactions?.each { Transaction t->
            t.relatedResources?.each { r->
                sales << new PaypalSale(r.sale).save(flush:true)
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    PaypalPayment updatePaypalFromCheckout(String currencyCode, BigDecimal finalTotal,
            String emailAddress, com.paypal.orders.Payer payer, com.paypal.orders.PurchaseUnit purchaseUnit) {

        com.paypal.orders.Payee payee = purchaseUnit.payee()
        List<com.paypal.orders.Item> items = purchaseUnit.items()
        AddressPortable address = purchaseUnit.shippingDetail().addressPortable()
        PaymentCollection payments =  purchaseUnit.payments()
        com.paypal.orders.Capture capture = payments.captures().get(0)
        List<LinkDescription> orderLinks = capture.links()
        com.paypal.orders.Money money = capture.amount()
        MerchantReceivableBreakdown breakdown = capture.sellerReceivableBreakdown()

        PaypalPayment payment =  new PaypalPayment()
        payment.paypalTransactionId = capture.id()

        payment.paypalFee = breakdown.paypalFee().value()
        payment.currencyCode = breakdown.paypalFee().currencyCode()


        Map input = [:]

        input.username = emailAddress ?: payer.email()
        input.emailAddress =  emailAddress ?: payer.email()
        input.firstName = payer?.name()?.givenName()
        input.lastName = payer?.name()?.surname()
        AddressPortable userAddress = payer.addressPortable()
        input.address=[:]
        input.address.countryCode = userAddress.countryCode() ?: address.countryCode()
        input.address.country = CountryCode.valueOf((String)input.address.countryCode).name
        input.address.city = userAddress?.adminArea2() ?: address.adminArea2()
        input.address.state = userAddress?.adminArea1()?:address.adminArea1()
        input.address.postcode = userAddress?.postalCode() ?:address.postalCode()
        input.address.line1 = userAddress?.addressLine1() ?:address.addressLine1()
        input.address.line2 = userAddress?.addressLine2() ?:address.addressLine2()
        input.address.emailAddress =emailAddress ?: payer.email()
        input.address.firstName =payer?.name()?.givenName()
        input.address.lastName = payer?.name()?.surname()

        def addedUser = addUser(input)
        if (addedUser.user) {
            payment.user = addedUser.user
        }

        BuyerInformation buyerInfo = payment?.buyerInformation
        if (!buyerInfo) {
            buyerInfo = new BuyerInformation()
        }
        buyerInfo.email = payer?.email()
        buyerInfo.countryCode = userAddress.countryCode() ?: address.countryCode()
        buyerInfo.country = CountryCode.valueOf(buyerInfo.countryCode).name
        buyerInfo.line1 = userAddress?.addressLine1() ?:address.addressLine1()
        buyerInfo.line2 = userAddress?.addressLine2() ?:address.addressLine2()
        //buyerInfo.line3 = address?.addressLine3()
        buyerInfo.city  = userAddress?.adminArea2() ?: address.adminArea2()
        buyerInfo.state = userAddress?.adminArea1()?:address.adminArea1()

        buyerInfo.zip =  userAddress?.postalCode() ?:address.postalCode()
        buyerInfo.save(flush:true)
        if (!payment?.buyerInformation) {
            payment?.buyerInformation=buyerInfo
        }

        payment.paypalUserStatus = capture.status()
        //payment.paymentMethod = payer.

        items?.each { com.paypal.orders.Item item->
               new PaypalSale(capture.id(),  capture.status(), item).save(flush:true)
        }
        payment.gross = new BigDecimal(money.value())
        payment.currency= Currency.getInstance(money.currencyCode())
        if (currencyCode == money.currencyCode() && finalTotal == new BigDecimal(money.value())) {
            payment.completed = true
        }
        //payment.currency = Currency.getInstance(breakdown.grossAmount().currencyCode())
        payment.subTotal = new BigDecimal(breakdown.grossAmount().value())
       return  payment.save(flush:true)

        //println "1-- ${capture.id()}"
        //println "2-- ${money.value()} vs ${money.currencyCode()}"
        //println "3-- ${capture.status()}"
        //println "4-- ${breakdown.grossAmount().value()} vs ${breakdown.grossAmount().currencyCode()}"
        //println "5-- ${capture.captureStatusDetails()}"
        //println "6-- ${capture.createTime()}"
        //orderLinks?.each { link->
           // println(link.rel() + " => " + link.method() + ":" + link.href())
        //}
    }

    @Transactional
    def generatePaypalDetails(com.paypal.api.payments.Payment incomePayment) {
        PaypalPayment payment =  new PaypalPayment()
        payment.paypalTransactionId = incomePayment.id as String
        payment.paymentMethod = incomePayment?.payer?.paymentMethod

        BuyerInformation buyerInfo = payment?.buyerInformation
        if (!buyerInfo) {
            buyerInfo = new BuyerInformation()
        }
        buyerInfo.email = incomePayment?.payer?.payerInfo?.email
        buyerInfo.countryCode = incomePayment?.payer?.payerInfo?.countryCode
        buyerInfo.save(flush:true)
        payment.populateFromPaypal(incomePayment)
        populateDbFromPaypal(incomePayment)
        payment.save(flush:true)
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    def updatePaypalDetails(com.paypal.api.payments.Payment incomePayment, Long paymentId) {
        PaypalPayment payment = PaypalPayment.get(paymentId)
        payment.paypalTransactionId = incomePayment.id as String
        payment.paymentMethod = incomePayment?.payer?.paymentMethod

        BuyerInformation buyerInfo = payment?.buyerInformation
        if (!buyerInfo) {
            buyerInfo = new BuyerInformation()
        }
        buyerInfo.email = incomePayment?.payer?.payerInfo?.email
        buyerInfo.countryCode = incomePayment?.payer?.payerInfo?.countryCode
        buyerInfo.save(flush:true)
        payment.populateFromPaypal(incomePayment)
        populateDbFromPaypal(incomePayment)
        payment.save(flush:true)
    }


    private String addClause(String where,String clause) {
        return (where ? where + ' and ' : 'where ') + clause
    }
}
