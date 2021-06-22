<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
    <title><g:message code="transactionCancelled.label" default="Transaction Cancelled"/></title>
    <asset:stylesheet src="payment.css" />
</head>
<body>
<div id="shoppingCart">
    <div class="card col-md-8">
        <h1>Square Payment Failure</h1>
        <g:if test="${flash.message}">
            <div class="message" role="status">${flash.message}</div>
        </g:if>
        <p style="text-align: center">
            <g:link action="checkout">Try Again</g:link>
        </p>
    </div>
</div>
</body>
</html>