<%@ page import="org.grails.plugin.payment.listeners.PaymentConfigListener" %>
<script src="https://www.paypal.com/sdk/js?currency=${instance?.currencyCode}&client-id=${PaymentConfigListener.getValue('paypalClientId')}">
</script>

    <div id="paypalButton" style="display:none;">
        <div id="paypal-button-container"></div>


        <div class="row ">
            <div class="col text-right">
                <div id="cancelPaypal" class="btn btn-default btn-sm ">Change method</div>
            </div>
        </div>

    </div>

<script>
    let firstName = (document.getElementById("firstName").value ?  document.getElementById("firstName").value : '${instance?.address?.firstName}')
    let lastName = (document.getElementById("lastName").value ? document.getElementById("lastName").value : '${instance?.address?.lastName}')
    let addressLine1 = (document.getElementById("line1").value ? document.getElementById("line1").value : '${instance?.address?.line1}')
    let addressLine2 = (document.getElementById("line2").value ? document.getElementById("line2").value : '${instance?.address?.line2}')
    let city = (document.getElementById("city").value ? document.getElementById("city").value : '${instance?.address?.city}')
    let state = (document.getElementById("state").value ? document.getElementById("state").value : '${instance?.address?.state}')
    let countryCode = (document.getElementById("countryCode").value ? document.getElementById("countryCode").value : '${instance?.address?.countryCode}')
    let zip = (document.getElementById("postcode").value?document.getElementById("postcode").value:'${instance?.address?.postcode}')
    let emailAddress = (document.getElementById("emailAddress").value?document.getElementById("emailAddress").value:'${instance?.address?.emailAddress}')



    paypal.Buttons({
        style: {
            layout:  'vertical',
            color:   'blue',
            shape:   'rect',
            label:   'pay'
        },
        createOrder: function(data, actions) {
            return actions.order.create( {
                "intent": "CAPTURE",
                "application_context": {
                    "return_url": "${PaymentConfigListener.getValue('hostName')}/paypal/thanks",
                    "cancel_url": "${PaymentConfigListener.getValue('hostName')}/paypal/cancel",
                    "brand_name": "${PaymentConfigListener.getValue('hostName')}",
                    "locale": "${instance?.locale?.language}",
                    "landing_page": "BILLING",
                    <g:if test="${instance?.includeAddress}">
                    "shipping_preference": "SET_PROVIDED_ADDRESS",
                    </g:if>
                    "user_action": "CONTINUE"
                },
                purchase_units: [{
                    <g:if test="${instance?.referenceId}">
                    "reference_id": "${instance.referenceId}",
                    </g:if>
                    <g:if test="${instance?.description}">
                    "description": "${instance.description}",
                    </g:if>
                    <g:if test="${instance?.customId}">
                    "custom_id": "${instance.customId}",
                    </g:if>
                    <g:if test="${instance?.softDescriptor}">
                    "soft_descriptor": "${instance.softDescriptor}",
                    </g:if>
                    amount: {
                        currency_code: "${instance?.currencyCode}",
                        value: '${instance?.finalTotal}',
                        <g:if test="${instance?.includeBreakDown}">
                        "breakdown": {
                            "item_total": {
                                currency_code: "${instance?.currencyCode}",
                                value: "${instance?.subTotal}"
                            },
                            <g:if test="${instance?.shipping}">
                            "shipping": {
                                currency_code: "${instance?.currencyCode}",
                                value: "${instance?.shipping}"
                            },
                            </g:if>
                            <g:if test="${instance?.handling}">
                            "handling": {
                                currency_code: "${instance?.currencyCode}",
                                value: '${instance?.handling}'
                            },
                            </g:if>
                            <g:if test="${instance?.taxTotal}">
                            "tax_total": {
                                currency_code: "${instance?.currencyCode}",
                                value: "${instance?.taxTotal}"
                            },
                            </g:if>
                            <g:if test="${instance?.shippingDiscount}">
                            "shipping_discount": {
                                currency_code: "${instance?.currencyCode}",
                                value: "${instance?.shippingDiscount}"
                            }
                            </g:if>
                        }
                        </g:if>
                    },
                    <g:if test="${instance?.includeItems}">"items": [
                        <g:each in="${instance.cartItems}" var="item">{
                            "name": "${item?.name}",
                            "description": "${item?.description?:item?.name}",

                            <g:if test="${item?.sku}">
                            "sku": "${item?.sku}",
                            </g:if>
                            "unit_amount": {
                                currency_code: "${instance?.currencyCode}",
                                value: "${item?.listPrice}"
                            },
                            <g:if test="${item?.taxPrice}">
                            "tax": {
                                currency_code: "${instance?.currencyCode}",
                                value: "${item?.taxPrice}"
                            },
                            </g:if>
                            "quantity": "${item.quantity}",
                            "category": "${item?.category?:'PHYSICAL_GOODS'}"
                        },
                        </g:each>
                    ], </g:if>
                    <g:if test="${instance?.includeAddress}">
                    "shipping": {
                        <g:if test="${instance?.shippingMethod}">
                        "method": "${instance?.shippingMethod}",
                        </g:if>
                        "address": {
                            "name": {
                                "full_name":firstName,
                                "surname":lastName
                            },
                            "address_line_1": addressLine1,
                            "address_line_2": addressLine2,
                            "admin_area_2": city,
                            "admin_area_1": state,
                            "postal_code": zip,
                            "country_code": countryCode
                        }
                    }
                    </g:if>
                }]
            });
        },
        onApprove: function(data, actions) {
            return actions.order.capture().then(function(details) {
                details.emailAddress = emailAddress
                details.username = emailAddress
                updateDatabase(JSON.stringify(details))
            });
        },
        onError: err => {
            console.log(err);
            window.location.href = "/paypal/cancel" ;
        }

    }).render('#paypal-button-container'); // Display payment options on your web page

    async function updateDatabase(jsonData) {

        let result = await makeRequest("POST", '${g.createLink(controller:'paypal', action:'executeJSON')}', jsonData);
        if (result === 'complete') {
            window.location.href = "/paypal/thanks" ;
        } else {
            window.location.href = "/paypal/cancel" ;
        }
    }

    function makeRequest(method, url, jsonData) {
        return new Promise(function (resolve, reject) {
            let xhr = new XMLHttpRequest();
            xhr.open(method, url);
            xhr.onload = function () {
                if (this.status >= 200 && this.status < 300) {
                    resolve(xhr.response);
                } else {
                    reject({
                        status: this.status,
                        statusText: xhr.statusText
                    });
                }
            };
            xhr.onerror = function () {
                reject({
                    status: this.status,
                    statusText: xhr.statusText
                });
            };
            xhr.send(jsonData);
        });
    }

    function enablePaypal() {
        document.getElementById("nonStripeSquare").style.display = "none";
        document.getElementById("paypalButton").style.display = "block";
    }
</script>
