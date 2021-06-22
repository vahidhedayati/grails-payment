<div class=" card ">
    <div class="row">
             <div class="title">
        <div class="col">
                    Payment status
                </div>
        </div>
    </div>
<g:each var="sale" in="${payment?.sales}">
    <div class="row">
        <div class="col">
            <span class="transSummaryItemName">Status: <b>${sale.state}</b></span>
        </div>
        <div class="col">
            <span class="transSummaryItemName">Paid: <b>${sale.total}</b></span>
        </div>
    </div>
    </div>
</g:each>
<div class="card">
    <div class="row">
        <div class="col">
            <div class="card-body">
                <div class="title">
                    Items purchased
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="transSummaryItem col card-header card-info">
            <span class="transSummaryItemName">Item Name:</span>
        </div>
        <div class="transSummaryItem col card-header card-info">
            <span class="transSummaryItemName">Quantity:</span>
        </div>
        <div class="transSummaryItem col card-header card-info">
            <span class="transSummaryItemName">Price:</span>
        </div>
    </div>
    <div class="row">
        <g:each var="paymentItem" in="${payment.paymentItems}">
            <div class="row">
                <div class="transSummaryItem col">
                    <span class="transSummaryItemValue">${paymentItem.itemName.encodeAsHTML()}</span>
                </div>
                <div class="transSummaryItem col">
                    <span class="transSummaryItemValue">${paymentItem.quantity}</span>
                </div>
                <div class="transSummaryItem col">
                    <span class="transSummaryItemValue">${paymentItem.amount * paymentItem.quantity}</span>
                </div>
            </div>
        </g:each>
    </div>
</div>

<p>&nbsp;</p>

<g:if test="${user?.isAdmin}">
    <div class="card">
        <div class=col">
            <div class="row">
                <div class="col">
                    <div class="title">

                        Paypal Response
                    </div>
                </div>
            </div>
            <div class=row">

                <div class="transSummaryItem col">
                    <span class="transSummaryItemName">Paypal Email: <b>${buyerInformation?.email}</b></span>
                </div>
                <div class="transSummaryItem col">
                    <span class="transSummaryItemName">User Status: <b>${payment?.paypalUserStatus}</b></span>
                </div>
                <div class="transSummaryItem col">
                    <span class="transSummaryItemName">Sale Completed: <b>${payment?.completed}</b></span>
                </div>
            </div>
        </div>


        <div class=col">
            <div class="row">
                <div class="col">
                    <div class="title">
                        Site Information
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="transSummaryItem col">
                    <span class="transSummaryItemName">User Email: <b>${user?.emailAddress}</b></span>
                </div>
                <div class="transSummaryItem col">
                    <span class="transSummaryItemName">User Username: <b>${user?.username}</b></span>
                </div>
                <div class="transSummaryItem col">
                    <span class="transSummaryItemName">Sale Email: <b>${postalAddress?.emailAddress}</b></span>
                </div>
            </div>
        </div>
    </div>
    <div class="card">
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
                <address>
                    <div class="center-block">
                        <div class=" title">
                            <h2>Paypal Address Response</h2>
                            <hr/>
                            <g:each in="${buyerInformation?.addressArray}" var="line">
                                ${line}<br/>
                            </g:each>
                        </div>
                    </div>
                </address>
            </div>
        </div>
    </div>

</g:if>

<g:if test="${!payment.hideUserDetails && user && !user?.isAdmin}">
    <div class="card">
        <div class="col">
            <div class="title">
                Authentication information
            </div>
        </div>
        <div class="col">
            <span class="h4">Username: <b>${user.username}</b></span>
        </div>
    </div>

</g:if>
<div class="card">
    <div class="row">
        <div class=" col">
            <div class="title">
                Shipping and final Totals
            </div>
        </div>
        <g:if test="${payment?.shipping}">
            <div class="col">
                <span class="transSummaryItemName">Shipping: <b>${payment.shipping}</b></span>
            </div>
        </g:if>
        <g:if test="${payment?.tax}">
            <div class="col">
                <span class="transSummaryItemName">Tax:       <b>${payment.tax}</b></span>
            </div>
        </g:if>
        <g:if test="${payment?.gross}">
            <div class="col">
                <span class="transSummaryItemName">Total:  <span>${payment.gross} ${payment.currency}</span></span>
            </div>
        </g:if>
    </div>
</div>

