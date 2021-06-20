<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'paymentConfig.label', default: 'Notice')}"/>
    <title><g:message code="default.show.label" args="[entityName]"/></title>
</head>
<body>
<div class="container">
    <a href="#show-paymentConfig" class="sr-only sr-only-focusable" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <g:render template="/common/subnav-list"/>
    <div id="show-paymentConfig" class="page-header">
        <h1><g:message code="default.show.label" args="[entityName]"/></h1>
    </div>
    <g:render template="/common/flash-message"/>
    <div>
        <dl class="dl-horizontal">
            <dt><g:message code="default.hostName.label" default="default.hostName.label"/></dt>
            <dd><f:display bean="paymentConfig" property="hostName"/></dd>
        </dl>
    </div>

    <g:form resource="${this.paymentConfig}" method="DELETE">
        <div>
            <g:link class="btn btn-primary" action="edit" resource="${this.paymentConfig}"><g:message code="default.button.edit.label"
                                                                                                      default="Edit"/></g:link>
        </div>
    </g:form>

</div> <%-- /.container --%>
</body>
</html>
