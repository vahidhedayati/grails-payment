<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<meta name="layout" content="main"/>
	<title><g:message code="thanksOrderPlaced.label" default="Thanks your order is now placed"/></title>
	<asset:stylesheet src="payment.css" />
</head>
<body>
<div id="shoppingCart">
<div class="card col-md-8">
	<div class=" row">
		<p>&nbsp</p>
		<div class="card">
			<div class="card-header title">
				<div class="transSummaryItem row">
					<span class="transSummaryItemName col">Order ID: <b>${payment?.id}</b></span>
					<span class="transSummaryItemName col">Transaction ID: <b>${payment?.transactionId}</b></span>
					<span class="navbar-right text-right col">
						<span class="transSummaryItemName">Order Date:</span>
						<span class="transSummaryItemValue"><b><g:formatDate date="${payment.dateCreated}" format="${g.message(code:'dateTime.format')}"/></b></span>
						<span class="transSummaryItemName"></span>
						<span class="transSummaryItemValue"><b>&nbsp;</b></span>
					</span>
				</div>
			</div>
			<div class="card-header title">
				Thank you, your purchase is now complete, Item will be dispatched and be with you shortly.
				You will find further updates and all of the order information under your account.
				Only if you have saved login information for next time.
			</div>
		</div>
	</div>
	<div id="transactionSummary" class="transactionSummary row">
		<g:if test="${payment}">
			<g:render template="txsummary" model="[payment:payment,buyerInformation:payment.buyerInformation,
												   user:payment.user,
												   postalAddress:payment?.postalAddress]"  />
		</g:if>
	</div>
</div>
</div>
</body>
</html>