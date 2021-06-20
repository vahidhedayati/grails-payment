<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main"/>
	<title><g:message code="transactionCancelled.label" default="Transaction Cancelled"/></title>
</head>

<body>
	<body id="body">
	<g:render template="/common/flash-message"/>
	<g:render template="/common/errors" model="[bean:params]" />
		Your purchase transaction has been cancelled. Information about the items you planned to purchase can be seen below:
		<div id="transactionSummary" class="transactionSummary">
		<g:if test="${payment}">
			<g:render template="txsummary" model="[payment:payment]"  />
		</g:if>
		</div>
	</body>
</html>
