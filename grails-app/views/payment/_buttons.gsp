<%@ page import="org.grails.plugin.payment.listeners.PaymentConfigListener" %>

<g:set var="paypalEnabled"
       value="${PaymentConfigListener.paypalEnabled}"/>
<g:set var="stripeEnabled"
       value="${PaymentConfigListener.stripeEnabled}"/>
<g:set var="squareEnabled"
       value="${PaymentConfigListener.squareEnabled}"/>
<g:set var="existingAddress" value="${false}"/>
<script type="text/javascript">
    document.addEventListener("DOMContentLoaded", function() {
        document.getElementById("cancelStripe").addEventListener('click', (event) => {
            changeState()
        })
        document.getElementById("cancelSquare").addEventListener('click', (event) => {
            changeState()
        })
        document.getElementById("cancelPaypal").addEventListener('click', (event) => {
            changeState()
        })
        function changeState() {
            document.getElementById("nonStripeSquare").style.removeProperty("display");
            document.getElementById("stripeCardFields").style.display = "none";
            document.getElementById("stripeButton").style.display = "none";
            document.getElementById("paypalButton").style.display = "none";
            document.getElementById("squareCardFields").style.display = "none";
        }
    })
</script>


<div class="row  border-top margin-top-2">
    <div class="col title">
        <h4><b>Secure Payment</b></h4>
    </div>
</div>
    <g:set var="total" value="${finalTotal?:instance?.finalTotal?:session?.finalTotal}"/>
    <g:if test="${!total}">
        <div class="row">
            <div class="col">
                <label  class="col-form-label" for="finalTotal">
                    <g:message code="chargeAmount.label"/>
                </label>
                <g:field type="text" name="finalTotal" class="form-control" required="required" value="" placeholder="charge amount" />
            </div>
        </div>
    </g:if>
<g:if test="${stripeEnabled}">
    <asset:stylesheet src="stripeForm.css" />
    <div class="stripeCardFields" id="stripeCardFields" style="display:none;">
        <div class="fieldset" id="stripeFields" >

            <div id="card-number" class="field empty"></div>
            <div id="card-expiry" class="field empty third-width"></div>
            <div id="card-cvc" class="field empty third-width"></div>
        </div>
        <div class="error " role="alert">
            <div class="message"></div>
        </div>
        <div class="row ">
            <div class="col text-right">
                <div id="cancelStripe" class="btn btn-default btn-sm ">Change method</div>
            </div>
        </div>
    </div>
</g:if>
<g:if test="${squareEnabled}">
    <asset:stylesheet src="sq-payment.css" />
    <div class="squareCardFields" id="squareCardFields" style="display:none;">


        <div id="card-container"></div>
        <button id="card-button" type="button">Pay ${total}</button>
        <div id="payment-status-container"></div>
        <div class="row ">
            <div class="col text-right">
            <div id="cancelSquare" class="btn btn-default btn-sm ">Change method</div>
            </div>
        </div>
    </div>
</g:if>

<div class="row" id="nonStripeSquare">
    <g:if test="${paypalEnabled}">
        <div class="col">
            <g:if test="${instance.paypalJSMethod}">
                <div class="col"  onclick="enablePaypal()">
                    <asset:image src="btn_xpressCheckout.png"  class="img-fluid"/>
                </div>
            </g:if>
            <g:else>
                <g:actionSubmitImage value="Paypal" controller="payment" action="paypalcheckout"  class="img-fluid"
                                     src="${resource(dir: 'images', file: 'btn_xpressCheckout.png')}" />
            </g:else>


        </div>
    </g:if>

    <g:if test="${squareEnabled}">
        <div class="col"  onclick="enableSquare()">
            <asset:image src="btn_squareCheckout.png"  class="img-fluid"/>
        </div>
    </g:if>

    <g:if test="${stripeEnabled}">
        <div class="col "  onclick="enableStripe()">
            <asset:image src="btn_stripeCheckout.png" class="img-fluid"/>
        </div>

    </g:if>
</div>
<div id="stripeButton" style="display:none;">
    <div class="stripeSubmit" id="stripeSubmit">
        <asset:image src="btn_stripeCheckout.png" />
    </div>
</div>


<g:if test="${stripeEnabled}">
    <g:render template="/payment/stripe" model="[instance:instance]"/>
</g:if>
<g:if test="${squareEnabled}">
    <g:render template="/payment/square" model="[instance:instance]"/>
</g:if>
<g:if test="${paypalEnabled && instance.paypalJSMethod}">
    <g:render template="/payment/paypal" model="[instance:instance]"/>
</g:if>
