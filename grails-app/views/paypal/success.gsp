<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main"/>
	<title><g:message code="thanksOrderPlaced.label" default="Thanks your order is now placed"/></title>
</head>
<body>
<div class="row">
<div class="col panel-primary panel-shadow">
	<div class="panel-heading ">
		<div class="transSummaryItem">
			<span class="transSummaryItemName">Order ID:</span>
			<span class="transSummaryItemValue"><b>${payment?.id}</b></span>
			<span class="transSummaryItemName">Transaction ID:</span>
			<span class="transSummaryItemValue"><b>${payment?.transactionId}</b></span>
			<span class="navbar-right">
				<span class="transSummaryItemName">Order Date:</span>
				<span class="transSummaryItemValue"><b><g:formatDate date="${payment.dateCreated}" format="${g.message(code:'dateTime.format')}"/></b></span>
				<span class="transSummaryItemName"></span>
				<span class="transSummaryItemValue"><b>&nbsp;</b></span>
			</span>
		</div>
	</div>
	<div class="panel-heading ">
		Thank you, your purchase is now complete, Item will be dispatched and be with you shortly.
		You will find further updates and all of the order information under your account.
		Only if you have saved login information for next time.
	</div>
</div>
</div>
<br/>

<div id="transactionSummary" class="transactionSummary row">
	<g:if test="${payment}">
		<g:render template="txsummary" model="[payment:payment,buyerInformation:payment.buyerInformation,
											   user:payment.user,
											   postalAddress:payment?.postalAddress]"  />
	</g:if>
</div>
</body>
</html>