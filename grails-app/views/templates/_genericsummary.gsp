<g:if test="${payment?.paymentItems}">
    <div class="card">
        <div class="row">
            <div class="col title">
                Items purchased
            </div>
        </div>

        <div class="row">
            <div class="col card-info">
                <span class="transSummaryItemName">Item Name:</span>
            </div>
            <div class="col card-info">
                <span class="transSummaryItemName">Quantity:</span>
            </div>
            <div class="col card-info">
                <span class="transSummaryItemName">Price:</span>
            </div>
        </div>

        <g:each var="paymentItem" in="${payment.paymentItems}">
            <div class="row">
                <div class="col">
                    <span class="transSummaryItemValue">${paymentItem.itemName.encodeAsHTML()}</span>
                </div>
                <div class="col">
                    <span class="transSummaryItemValue">${paymentItem.quantity}</span>
                </div>
                <div class="col">
                    <span class="transSummaryItemValue">${paymentItem.amount * paymentItem.quantity}</span>
                </div>
            </div>

        </g:each>
    </div>
</g:if>
<div class=" row">
    <div class="col">
        <div class="card-header card-warning">
            User Details
        </div>
    </div>
</div>
<div class=" row">
    <div class="col">
        <div class="col card-info">
            <span class="transSummaryItemName">Sale Email: <b>${postalAddress?.emailAddress}</b></span>
        </div>
        <div class="col card-info">
            <span class="transSummaryItemName">User Username:<b>
                ${user?.username}
                <g:if test="${user?.username != user.emailAddress && postalAddress.emailAddress != user?.emailAddress}">
                    (${user?.emailAddress})
                </g:if>
            </b></span>
        </div>
        <div class="col card-info">
            <span class="transSummaryItemName">Sale Completed:<b>${payment?.completed}</b></span>
        </div>
    </div>
</div>

<div class="row">
    <div class="col">
        <div class="card-body">
            <address>
                <div class="center-block">
                    <div class=" title">
                        <h2>Postal details from site</h2>
                        <hr/>
                        <g:each in="${postalAddress?.addressArray}" var="line">
                            ${line}<br/>
                        </g:each>
                        ${postalAddress?.flatTelephone}
                    </div>
                </div>
            </address>
        </div>
    </div>
    <div class="col">
        <div class="card-body">
            <g:render template="${templateFile}"/>
        </div>
    </div>
</div>
<div class="row">
    <div class="col">
        <div class="card-header title">
            Authentication information
        </div>
    </div>
    <div class="col">
        <span class="transSummaryItemName">Username: <b>${user?.username}</b></span>
    </div>
</div>
<div class="row">
    <div class="col">
        <div class="card-header title">
            Shipping and final Totals
        </div>
    </div>
</div>
<div class="row">
    <g:if test="${payment?.shipping}">
        <div class="transSummaryItem  col">
            <span class="transSummaryItemName">Shipping:
                <span>${payment.shipping}</span>
            </span>
        </div>
    </g:if>
    <g:if test="${payment?.tax}">
        <div class="transSummaryItem col">
            <span class="transSummaryItemName">Tax:
                <span>${payment.tax}</span>
            </span>
        </div>
    </g:if>
    <g:if test="${payment?.gross}">
        <div class="transSummaryItem col  text-right">
            <span class="title">Total:
                <span>${payment.gross} ${payment.currency}</span>
            </span>
        </div>
    </g:if>
</div>