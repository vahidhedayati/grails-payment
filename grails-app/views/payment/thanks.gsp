<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
    <title>
    <g:message code="shoppingCart.label"/>
    </title>
</head>

<body>
<div class="container">
    <div class="col-md-9 col-md-offset-2">
        <div class="container wrapper">
            <div class="row cart-head">
                <div class="container">

                    <div class="row">
                        <p>&nbsp</p>
                        <div class="col-sm-12 panel-primary panel-shadow">
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

                            </div>
                        </div>
                        <br/>

                        <div id="transactionSummary" class="transactionSummary">
                            <g:if test="${payment}">
                                <g:render template="${template}" model="[payment:payment,
                                                                       user:payment.user,
                                                                       postalAddress:payment?.postalAddress]"  />
                            </g:if>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>