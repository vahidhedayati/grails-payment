<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main"/>
        <g:set var="entityName" value="${message(code: 'paymentConfig.label', default: 'Notice')}"/>
        <title><g:message code="default.list.label" args="[entityName]"/></title>
    </head>
    <body>
        <div class="container">
            <a href="#list-paymentConfig" class="sr-only sr-only-focusable" tabindex="-1"><g:message code="default.link.skip.label"
                    default="Skip to content&hellip;"/></a>
            <div id="list-paymentConfig" class="page-header">
                <h1><g:message code="default.list.label" args="[entityName]"/></h1>
            </div>
            <div class="table-responsive">
                <table class="table table-condensed">
                    <thead>
                        <tr>
                            <g:sortableColumn property="hostName"
                            title="${message(code: 'default.hostName.label', default: 'default.siteName.label')}"/>
                        </tr>
                    </thead>
                    <tbody>
                        <g:each in="${paymentConfigList}" status="i" var="item">
                            <tr>
                                <td><g:link action="show" id="${item.id}">${item.hostName}</g:link></td>
                            </tr>
                        </g:each>
                    </tbody>
                </table>
            </div>
    </div>
</body>
</html>