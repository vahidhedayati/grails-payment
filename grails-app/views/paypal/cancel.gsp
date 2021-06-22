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
		<g:render template="/common/flash-message"/>
		<g:render template="/common/errors" model="[bean:params]" />
		Your purchase transaction has been cancelled. Information about the items you planned to purchase can be seen below:
		<div id="transactionSummary" class="transactionSummary">
			<g:if test="${payment}">
				<g:render template="txsummary" model="[payment:payment]"  />
			</g:if>
		</div>
	</div>
</div>
</body>
</html>
