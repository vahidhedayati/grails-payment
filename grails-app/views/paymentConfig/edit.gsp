<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'paymentConfig.label', default: 'paymentConfig.label')}"/>
    <title><g:message code="default.edit.label" args="[entityName]"/></title>
</head>
<body>
<div class="container">
    <g:render template="has-errors"/>
    <div class="card-body">

        <g:form resource="${this.paymentConfig}" method="PUT">
            <g:hiddenField name="version" value="${this.paymentConfig?.version}"/>
            <g:render template="form"/>
            <div>
                <input class="btn btn-success" type="submit"
                       value="${message(code: 'default.button.update.label', default: 'Update')}"/>
            </div>
        </g:form>
    </div>


</div>

</body>
</html>
