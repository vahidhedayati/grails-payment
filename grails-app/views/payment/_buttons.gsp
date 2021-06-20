<%@ page import="org.grails.plugin.payment.listeners.PaymentConfigListener" %>

<g:set var="paypalEnabled"
       value="${PaymentConfigListener.paypalEnabled}"/>
<g:set var="stripeEnabled"
       value="${PaymentConfigListener.stripeEnabled}"/>
<g:set var="squareEnabled"
       value="${PaymentConfigListener.squareEnabled}"/>
<g:set var="existingAddress" value="${false}"/>

<div class="panel panel-info">
    <div class="panel-heading h4"><span><i class="glyphicon glyphicon-lock"></i>
    </span> Secure Payment</div>
    <div class="panel-body">

        <div >
            <input type="hidden" name="amount" value="${finalTotal}">
            <g:if test="${stripeEnabled}">
                <asset:stylesheet src="stripeForm.css" />
                <div class="stripeCardFields" id="stripeCardFields" style="display:none;">
                    <div class="fieldset" id="stripeFields" >

                        <input type="hidden" name="stripeToken" value="" id="stripeToken"/>
                        <div id="card-number" class="field empty"></div>
                        <div id="card-expiry" class="field empty third-width"></div>
                        <div id="card-cvc" class="field empty third-width"></div>
                    </div>
                    <div class="error " role="alert">
                        <div class="message"></div></div>
                </div>
            </g:if>
            <g:if test="${squareEnabled}">
                <asset:stylesheet src="sq-payment.css" />
                <div class="squareCardFields" id="squareCardFields" style="display:none;">

                    <input type="hidden" name="squareToken" value="" id="squareToken"/>
                    <div id="card-container"></div>
                    <button id="card-button" type="button">Pay ${finalTotal}</button>
                    <div id="payment-status-container"></div>
                </div>

            </g:if>
        </div>
            <div class="row" id="nonStripeSquare">
                <g:if test="${paypalEnabled}">
                    <div class="col">
                        <g:actionSubmitImage value="Paypal" controller="payment" action="paypalcheckout"  class="transparent-button"
                                             src="${resource(dir: 'images', file: 'btn_xpressCheckout.png')}" />
                    </div>
                </g:if>

                <g:if test="${squareEnabled}">
                    <div class="col btn btn-trans"  onclick="enableSquare()">
                        <asset:image src="btn_squareCheckout.png" width="145" height="42px"/>
                    </div>
                </g:if>

                <g:if test="${stripeEnabled}">
                    <div class="col btn btn-trans"  onclick="enableStripe()">
                        <asset:image src="btn_stripeCheckout.png" width="145px" height="42x" />
                    </div>

                </g:if>
            </div>
            <div id="stripeButton" style="display:none;">
                <div class="stripeSubmit btn " id="stripeSubmit">
                    <asset:image src="btn_stripeCheckout.png" />
                </div>
            </div>
        </div>
    </div>


<g:if test="${stripeEnabled}">
    <g:render template="stripe" model="[instance:instance]"/>
</g:if>
<g:if test="${squareEnabled}">
    <g:render template="square" model="[instance:instance]"/>
</g:if>