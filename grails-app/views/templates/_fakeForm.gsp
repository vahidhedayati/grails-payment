<div class="shoppingCart">
<g:form controller="payment" action="paypalcheckout" class="form form-horizontal" name="form" >
        <input type="hidden" name="address.firstName" id="firstName" value="${instance?.address?.firstName}"/>
        <input type="hidden" name="address.lastName" id="lastName" value="${instance?.address?.lastName}"/>
        <input type="hidden" name="address.line1" id="line1" value="${instance?.address?.line1}"/>
        <input type="hidden" name="address.line2" id="line2" value="${instance?.address?.line2}"/>
        <input type="hidden" name="address.city" id="city" value="${instance?.address?.city}"/>
        <input type="hidden" name="address.state" id="state" value="${instance?.address?.state}"/>
        <input type="hidden" name="address.country" id="country" value="${instance?.address?.country}"/>
        <input type="hidden" name="address.postcode" id="postcode" value="${instance?.address?.postcode}"/>
        <input type="hidden" name="address.emailAddress" id="emailAddress" value="${instance?.address?.emailAddress}"/>
        <input type="hidden" name="finalTotal" id="finalTotal" value="${instance?.finalTotal}"/>
        <input type="hidden" name="address.saveInfo" id="saveInfo" value="${instance?.address?.saveInfo}"/>
        <input type="hidden" name="stripeToken" id="stripeToken" value=""/>
        <input type="hidden" name="squareToken" value="" id="squareToken"/>
    <g:render template="${templateFile}" model="${instance}"/>
</g:form>
</div>
