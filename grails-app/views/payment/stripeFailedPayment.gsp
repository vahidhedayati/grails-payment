<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="layout" content="main">
        <title>Stripe Payment Failure</title>
    </head>
    <body>
        <h1>Stripe Payment Failure</h1>
        <g:if test="${flash.message}">
            <div class="message" role="status">${flash.message}</div>
        </g:if>
        <p style="text-align: center">
            <g:link action="cart">Try Again</g:link>
        </p>

    </body>
</html>
