
<div class="row border-bottom border-top margin-top-2">
    <div class="col title">
        <h4><b><g:message code="credentialInfo.label"/></b></h4>
    </div>
</div>
<div class="row">
    <div class="col">
        <span class=" ${hasErrors(bean: address, field: 'username', 'error')}">
            <label  for="address.username">
                <g:message code="usernameEmail.label"/>
            </label>


            <g:field  type="text" id="username" name="address.username"
                      value="${address?.username?:''}"
                      maxlength="80"
                      class="form-control ${hasErrors(bean: address, field: 'username', 'has-error')}"/>
        </span>
    </div>
</div>
<div class="row">
    <div class="col">
        <label for="address.password"><g:message code="password.label"/></label>
        <g:field type="password" id="pass1" name="address.password" maxlength="20" class="form-control"
                 placeholder="${g.message(code:'password.label')}" />
    </div>
    <div class="col" >
        <label for="address.confirmPassword"><g:message code="confirm.password.label"/></label>

        <g:field type="password" id="pass2" name="address.confirmPassword" maxlength="20" class="form-control"
                 placeholder="${g.message(code:'password.label')}" />

    </div>
</div>



