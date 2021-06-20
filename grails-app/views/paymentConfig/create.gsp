<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main"/>
        <g:set var="entityName" value="${message(code: 'notice.label', default: 'notice.label')}"/>
        <title><g:message code="default.create.label" args="[entityName]"/></title>
</head>

<body>
    <div class="container">
        <a href="#create-notice" class="sr-only sr-only-focusable" tabindex="-1"><g:message code="default.link.skip.label"
                default="Skip to content&hellip;"/></a>

        <div id="create-notice" class="page-header">
            <h1><g:message code="default.create.label" args="[entityName]"/></h1>
        </div>


        <g:render template="has-errors"/>

        <g:form action="save">
            <fieldset class="form">
                <g:render template="form"/>
            </fieldset>
            <div>
                <g:submitButton name="create" class="btn btn-success"
                value="${message(code: 'default.button.create.label', default: 'Create')}"/>
            </div>
        </g:form>


    </div>
</body>
</html>
