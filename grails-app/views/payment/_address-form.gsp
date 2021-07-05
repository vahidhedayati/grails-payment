<%@ page import="org.grails.plugin.payment.PaymentAddress" %>
<div class="row">
        <div class="col title">
            <h4><g:message code="shippingAddress.label"/></h4>
        </div>
    </div>

    <div class="row">
        <div class="col">
            <span class=" ${hasErrors(bean: address, field: 'title', 'has-error')}">
                <label class="col-form-label" for="address.title" ><g:message code="title.label"/></label>
                <g:select name="address.title" from="${salutations}" optionKey="id" optionValue="name"
                          value="${address?.title}" it="title"
                          class="form-control" noSelection="['':'']"
                          required="true" />
            </span>
        </div>

        <div class=" col">
            <span class=" ${hasErrors(bean: address, field: 'firstName', 'has-error')}">
                <label  class="col-form-label" for="address.firstName">
                    <g:message code="firstName.label"/>
                </label>
                <g:field type="text" name="address.firstName" id="firstName" class="form-control" required="required"

                         value="${address?.firstName?:''}" />
            </span>
        </div>

        <div class=" col">
            <span class=" ${hasErrors(bean: address, field: 'lastName', 'has-error')}">
                <label  class="col-form-label" for="address.lastName">
                    <g:message code="lastName.label"/>
                </label>
                <g:field  type="text" name="address.lastName"

                          class="form-control" id="lastName" required="required"
                          value="${address?.lastName?:''}" />
            </span>
        </div>
        <div class="clearfix visible-xs"></div>
    </div>

    <div class="row">

        <div class="col">
            <span class=" ${hasErrors(bean: address, field: 'line1', 'has-error')}">
                <label  class="col-form-label" for="address.line1" ><g:message code="addline1.label"/></label>
                <g:field name="address.line1" required="required"  class="form-control" id="line1"
                         maxlength="100"
                         value="${address?.line1?:''}"  type="text"
                />
            </span>
        </div>

        <div class="col">
            <span class=" ${hasErrors(bean: address, field: 'line1', 'has-error')}">
                <label class="col-form-label" for="address.line2" ><g:message code="addline2.label"/></label>
                <g:field name="address.line2" class="form-control" id="line2"
                         maxlength="100"
                         value="${address?.line1?:''}"  type="text"
                />
            </span>
        </div>
        <div class="clearfix visible-xs"></div>
    </div>

    <div class="row">
        <div class="col">
            <span class=" ${hasErrors(bean: address, field: 'city', 'has-error')}">
                <label class="col-form-label" for="city">
                    <g:message code="city.label"/>
                </label>
                <g:field type="text"
                         value="${address?.city?:''}"
                         id="city"
                         maxlength="100"
                         class="form-control" name="address.city" />
            </span>
        </div>
        <div class="col"  id="stateObject" style="display:none">
            <span class=" ${hasErrors(bean: address, field: 'state', 'has-error')}">
                <label class="col-form-label" for="state">
                    <g:message code="state.label"/>
                </label>
                <g:field type="text"
                         value="${address?.state ?: ''}"
                         id="state"
                         maxlength="100"
                         class="form-control" name="address.state" />
            </span>
        </div>
        <div class="col">
            <span class=" ${hasErrors(bean: address, field: 'country', 'has-error')}">
                <label for="address.country"  class="col-form-label">
                    <g:message code="country.label"/>
                </label>
                <g:select name="address.country" id="country" from="${org.grails.plugin.payment.enums.CountryCode.values().findAll().sort{it.name}}"  optionValue="name"
                          value="${address?.country}" onchange="updateCountryCode(this.value)"
                          class="form-control" noSelection="['':'']"
                          required="true" />

                <g:hiddenField name="countryCode" value="${address.countryCode}"/>
            </span>
        </div>
        <div class="clearfix visible-xs"></div>
    </div>

    <div class="row">

        <div class="col">
            <span class=" ${hasErrors(bean: address, field: 'postcode', 'has-error')}">
                <label for="address.postcode"  class="col-form-label">
                    <g:message code="postcode.label"/>
                </label>
                <g:field type="text"  class="form-control "
                         id="postcode"
                         value="${address?.postcode}"
                         pattern="^[a-zA-Z0-9\\s+]+\$"
                         name="address.postcode" />
            </span>
        </div>



    <div class="col">
        <span class=" ${hasErrors(bean: address, field: 'telephone', 'has-error')}">
            <label for="address.telephone" class="col-form-label">
                <g:message code="telephoneNumber.label"/></label>

            <g:field name="address.telephone"
                     id="telephone"
                     class="form-control"
                     maxlength="50" autocomplete="off"
                     value="${address?.telephone?:''}"  type="text"
                     placeholder="123456789"/>
        </span>
    </div>
    </div>

<div class="clearfix visible-xs"></div>
<div class="row" >

    <div class="col">
        <span class=" ${hasErrors(bean: address, field: 'emailAddress', 'has-error')}">
            <label for="address.emailAddress" class="col-form-label">
                <g:message code="emailAddress.label"/>
            </label>
            <g:field type="email"  id="emailAddress" name="address.emailAddress"
                     value="${address?.emailAddress?:''}"
                     maxlength="180" required="required"
                     class="form-control ${hasErrors(bean: address, field: 'emailAddress', 'has-error')}"/>
        </span>
    </div>

    <div class="col" id="primaryAddressCheckBox" style="display:none" >
        <span class="custom-control custom-checkbox ${hasErrors(bean: address, field: 'primaryAddress', 'has-error')}">
            <g:field type="checkbox" class="custom-control-input" checked="${true}"
                     name="address.primaryAddress" />
            <label class="custom-control-label" for="address.primaryAddress" >
                <g:message code="primaryAddress.label"/>
            </label>
        </span>
    </div>
</div>
<script>
    function updateCountryCode(val) {
        document.getElementById("countryCode").value=val
    }
</script>
