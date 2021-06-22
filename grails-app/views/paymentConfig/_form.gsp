<%@ page import="org.grails.plugin.payment.PaymentConfig" %>
<div class="title">
    <div class="row">
        <div class="col">
            <h4><b>Tick to enable features</b></h4>
        </div>
    </div>
</div>

<div class="row">
    <div class="col">
        <label>
            <g:message code="paymentConfigControllerEnabled.label" default="paymentConfigControllerEnabled.label"/>
        </label>
        <g:checkBox name="paymentConfigEnabled" checked="${paymentConfig?.paymentConfigEnabled?:false}"/>
    </div>
    <div class="col">
        <label>
            <g:message code="paymentCheckoutEnabled.label" />
        </label>
        <g:checkBox name="paymentCheckoutEnabled" checked="${paymentConfig?.paymentCheckoutEnabled?:false}"/>
    </div>
</div>

<div class="btn btn-trans">
    <label for="paypalEnabled">
        <g:message code="paypalEnabled.label" default="paypalEnabled.label"/>
    </label>

    <g:checkBox name="paypalEnabled" checked="${paymentConfig?.paypalEnabled?:false}"/>
</div>

<div class="btn btn-trans">
    <label for="paypalMode">
        <g:message code="paypalMode.label" default="paypalMode.label"/>
    </label>
    <g:select name="paypalMode" from="${PaymentConfig.paymentModes}" value="${paymentConfig?.paypalMode}"/>
</div>


<div class="btn btn-trans">
    <label for="stripeEnabled">
        <g:message code="stripeEnabled.label" default="default.stripeEnabled.label"/>
    </label>

    <g:checkBox name="stripeEnabled" checked="${paymentConfig?.stripeEnabled?:false}"/>
</div>

<div class="btn btn-trans">
    <label for="stripeMode">
        <g:message code="stripeMode.label" default="stripeMode.label"/>
    </label>
    <g:select name="stripeMode" from="${PaymentConfig.paymentModes}" value="${paymentConfig?.stripeMode}"/>
</div>

<div class="btn btn-trans">
    <label for="squareEnabled">
        <g:message code="squareEnabled.label" default="default.squareEnabled.label"/>
    </label>

    <g:checkBox name="squareEnabled" checked="${paymentConfig?.squareEnabled?:false}"/>
</div>

<div class="btn btn-trans">
    <label for="squareMode">
        <g:message code="squareMode.label" default="squareMode.label"/>
    </label>
    <g:select name="squareMode" from="${PaymentConfig.paymentModes}" value="${paymentConfig?.squareMode}"/>
</div>

</div>


<div class="form-group col-md-6 ${hasErrors(bean: paymentConfig, field: 'paypalEmail', 'error')} ">
    <label for="paypalEmail">
        <g:message code="paypalEmail.label" default="paypalEmail.label"/>
    </label>
    <g:field type="text" class="form-control" name="paypalEmail" value="${paymentConfig?.paypalEmail}"
             maxLength="250" placeholder="150 chars max"/>
</div>
<div class="form-group col-md-6 ${hasErrors(bean: paymentConfig, field: 'paypalClientId', 'error')} ">
    <label for="paypalClientId">
        <g:message code="paypalClientId.label" default="paypalClientId.label"/>
    </label>
    <g:field type="text" class="form-control" name="paypalClientId" value="${paymentConfig?.paypalClientId}"
             maxLength="255" placeholder="255 chars max"/>
</div>
<div class="form-group col-md-6 ${hasErrors(bean: paymentConfig, field: 'paypalClientSecret', 'error')} ">
    <label for="paypalClientSecret">
        <g:message code="paypalClientSecret.label" default="paypalClientSecret.label"/>
    </label>
    <g:field type="text" class="form-control" name="paypalClientSecret" value="${paymentConfig?.paypalClientSecret}"
             maxLength="255" placeholder="255 chars max"/>
</div>
<div class="form-group col-md-6 ${hasErrors(bean: paymentConfig, field: 'paypalEndpoint', 'error')} ">
    <label for="paypalEndpoint">
        <g:message code="paypalEndpoint.label" default="paypalEndpoint.label"/>
    </label>
    <g:field type="text" class="form-control" name="paypalEndpoint" value="${paymentConfig?.paypalEndpoint}"
             maxLength="255" placeholder="255 chars max"/>
</div>
<div class="form-group col-md-6 ${hasErrors(bean: paymentConfig, field: 'paypalSandboxEmail', 'error')} ">
    <label for="paypalSandboxEmail">
        <g:message code="paypalSandboxEmail.label" default="paypalSandboxEmail.label"/>
    </label>
    <g:field type="text" class="form-control" name="paypalSandboxEmail" value="${paymentConfig?.paypalSandboxEmail}"
             maxLength="255" placeholder="255 chars max"/>
</div>
<div class="form-group col-md-6 ${hasErrors(bean: paymentConfig, field: 'paypalSandboxClientId', 'error')} ">
    <label for="paypalSandboxClientId">
        <g:message code="paypalSandboxClientId.label" default="paypalSandboxClientId.label"/>
    </label>
    <g:field type="text" class="form-control" name="paypalSandboxClientId" value="${paymentConfig?.paypalSandboxClientId}"
             maxLength="255" placeholder="255 chars max"/>
</div>
<div class="form-group col-md-6 ${hasErrors(bean: paymentConfig, field: 'paypalSandboxClientSecret', 'error')} ">
    <label for="paypalSandboxClientSecret">
        <g:message code="paypalSandboxClientSecret.label" default="paypalSandboxClientSecret.label"/>
    </label>
    <g:field type="text" class="form-control" name="paypalSandboxClientSecret" value="${paymentConfig?.paypalSandboxClientSecret}"
             maxLength="255" placeholder="255 chars max"/>
</div>
<div class="form-group col-md-6 ${hasErrors(bean: paymentConfig, field: 'paypalSandboxEndpoint', 'error')} ">
    <label for="paypalSandboxEndpoint">
        <g:message code="paypalSandboxEndpoint.label" default="paypalSandboxEndpoint.label"/>
    </label>
    <g:field type="text" class="form-control" name="paypalSandboxEndpoint" value="${paymentConfig?.paypalSandboxEndpoint}"
             maxLength="255" placeholder="255 chars max"/>
</div>





<div class="form-group col-md-6 ${hasErrors(bean: paymentConfig, field: 'stripeTestSecretKey', 'error')} ">
    <label for="stripeTestSecretKey">
        <g:message code="stripeTestSecretKey.label" default="stripeTestSecretKey.label"/>
    </label>
    <g:field type="text" class="form-control" name="stripeTestSecretKey" value="${paymentConfig?.stripeTestSecretKey}"
             maxLength="255" placeholder="255 chars max"/>
</div>
<div class="form-group col-md-6 ${hasErrors(bean: paymentConfig, field: 'stripeTestPublishableKey', 'error')} ">
    <label for="stripeTestPublishableKey">
        <g:message code="stripeTestPublishableKey.label" default="stripeTestPublishableKey.label"/>
    </label>
    <g:field type="text" class="form-control" name="stripeTestPublishableKey" value="${paymentConfig?.stripeTestPublishableKey}"
             maxLength="255" placeholder="255 chars max"/>
</div>
<div class="form-group col-md-6 ${hasErrors(bean: paymentConfig, field: 'stripeSecretKey', 'error')} ">
    <label for="stripeSecretKey">
        <g:message code="default.stripeSecretKey.label" default="default.stripeSecretKey.label"/>
        <span class="required-indicator">*</span>
    </label>
    <g:field type="text" class="form-control" name="stripeSecretKey" value="${paymentConfig?.stripeSecretKey}"
             maxLength="250" placeholder="250 chars max"/>
</div>
<div class="form-group col-md-6 ${hasErrors(bean: paymentConfig, field: 'stripePublishableKey', 'error')} ">
    <label for="stripePublishableKey">
        <g:message code="default.stripePublishableKey.label" default="default.stripePublishableKey.label"/>
        <span class="required-indicator">*</span>
    </label>
    <g:field type="text" class="form-control" name="stripePublishableKey" value="${paymentConfig?.stripePublishableKey}"
             maxLength="250" placeholder="250 chars max"/>
</div>

<div class="form-group col-md-6 ${hasErrors(bean: paymentConfig, field: 'squareSandboxApplicationId', 'error')} ">
    <label for="squareSandboxApplicationId">
        <g:message code="squareSandboxApplicationId.label" default="squareSandboxApplicationId.label"/>
    </label>
    <g:field type="text" class="form-control" name="squareSandboxApplicationId" value="${paymentConfig?.squareSandboxApplicationId}"
             maxLength="255" placeholder="255 chars max"/>
</div>

<div class="form-group col-md-6 ${hasErrors(bean: paymentConfig, field: 'squareSandboxAccessToken', 'error')} ">
    <label for="squareSandboxAccessToken">
        <g:message code="squareSandboxAccessToken.label" default="squareSandboxAccessToken.label"/>
    </label>
    <g:field type="text" class="form-control" name="squareSandboxAccessToken" value="${paymentConfig?.squareSandboxAccessToken}"
             maxLength="255" placeholder="255 chars max"/>
</div>

<div class="form-group col-md-6 ${hasErrors(bean: paymentConfig, field: 'squareSandboxApplicationSecret', 'error')} ">
    <label for="squareSandboxApplicationSecret">
        <g:message code="squareSandboxApplicationSecret.label" default="squareSandboxApplicationSecret.label"/>
    </label>
    <g:field type="text" class="form-control" name="squareSandboxApplicationSecret" value="${paymentConfig?.squareSandboxApplicationSecret}"
             maxLength="255" placeholder="255 chars max"/>
</div>

<div class="form-group col-md-6 ${hasErrors(bean: paymentConfig, field: 'squareApplicationId', 'error')} ">
    <label for="squareApplicationId">
        <g:message code="squareApplicationId.label" default="squareApplicationId.label"/>
    </label>
    <g:field type="text" class="form-control" name="squareApplicationId" value="${paymentConfig?.squareApplicationId}"
             maxLength="255" placeholder="255 chars max"/>
</div>
<div class="form-group col-md-6 ${hasErrors(bean: paymentConfig, field: 'squareAccessToken', 'error')} ">
    <label for="squareAccessToken">
        <g:message code="squareAccessToken.label" default="squareAccessToken.label"/>
    </label>
    <g:field type="text" class="form-control" name="squareAccessToken" value="${paymentConfig?.squareAccessToken}"
             maxLength="255" placeholder="255 chars max"/>
</div>
<div class="form-group col-md-6 ${hasErrors(bean: paymentConfig, field: 'squareApplicationSecret', 'error')} ">
<label for="squareApplicationSecret">
    <g:message code="squareApplicationSecret.label" default="squareApplicationSecret.label"/>
</label>
<g:field type="text" class="form-control" name="squareApplicationSecret" value="${paymentConfig?.squareApplicationSecret}"
         maxLength="255" placeholder="255 chars max"/>
