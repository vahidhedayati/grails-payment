<div class="col-sm-4  card-warning">
    <div class="card-header card-warning">
        Payment status
    </div>
    <div class=" card-info">
        <div class="transSummaryItem col-sm-6 card-header card-info">
            <span class="transSummaryItemName">Status:</span>
        </div>
        <div class="transSummaryItem col-sm-6 card-header card-info">
            <span class="transSummaryItemName">Paid:</span>
        </div>
    </div>
    <g:each var="sale" in="${payment.sales}">
        <div>
            <div class="transSummaryItem  col-sm-6">
                <span class="transSummaryItemValue">${sale.state}</span>
            </div>
            <div class="transSummaryItem  col-sm-6">
                <span class="transSummaryItemValue">${sale.total}</span>
            </div>
        </div>
    </g:each>
</div>

<div class="col-sm-8 card-warning">
    <div class="card-header card-warning">
        Items purchased
    </div>
    <div class=" card-info">
        <div class="transSummaryItem col-sm-4 card-header card-info">
            <span class="transSummaryItemName">Item Name:</span>
        </div>
        <div class="transSummaryItem col-sm-4 card-header card-info">
            <span class="transSummaryItemName">Quantity:</span>
        </div>
        <div class="transSummaryItem col-sm-4 card-header card-info">
            <span class="transSummaryItemName">Price:</span>
        </div>
    </div>
    <g:each var="paymentItem" in="${payment.paymentItems}">
        <div >
            <div class="transSummaryItem col-sm-4">
                <span class="transSummaryItemValue">${paymentItem.itemName.encodeAsHTML()}</span>
            </div>
            <div class="transSummaryItem col-sm-4">
                <span class="transSummaryItemValue">${paymentItem.quantity}</span>
            </div>
            <div class="transSummaryItem col-sm-4">
                <span class="transSummaryItemValue">${paymentItem.amount * paymentItem.quantity}</span>
            </div>
        </div>
    </g:each>
</div>

    <div class="col-sm-4  card-warning">
        <div class="card-header card-warning">
            Paypal Response
        </div>
        <div class=" card-info">
            <div class="transSummaryItem col-sm-6 card-header card-info">
                <span class="transSummaryItemName">Paypal Email:</span>
            </div>
            <div class="transSummaryItem col-sm-3 card-header card-info">
                <span class="transSummaryItemName">User Status:</span>
            </div>
            <div class="transSummaryItem col-sm-3 card-header card-info">
                <span class="transSummaryItemName">Sale Completed:</span>
            </div>
        </div>

            <div>
                <div class="transSummaryItem  col-sm-6">
                    <span class="transSummaryItemValue">${payment?.buyerInformation?.email}</span>
                </div>
                <div class="transSummaryItem  col-sm-3">
                    <span class="transSummaryItemValue">${payment?.paypalUserStatus}</span>
                </div>
                <div class="transSummaryItem  col-sm-3">
                    <span class="transSummaryItemValue">${payment?.completed}</span>
                </div>
            </div>

        <div class="card-header card-warning">
            Site Information
        </div>
        <div class=" card-info">
            <div class="transSummaryItem col-sm-6 card-header card-info">
                <span class="transSummaryItemName">User Email:</span>
            </div>
            <div class="transSummaryItem col-sm-3 card-header card-info">
                <span class="transSummaryItemName">User Username:</span>
            </div>
            <div class="transSummaryItem col-sm-3 card-header card-info">
                <span class="transSummaryItemName">Sale Email:</span>
            </div>
        </div>

            <div>
                <div class="transSummaryItem  col-sm-6">
                    <span class="transSummaryItemValue">${user?.emailAddress}</span>
                </div>
                <div class="transSummaryItem  col-sm-3">
                    <span class="transSummaryItemValue">${user?.username}</span>
                </div>
                <div class="transSummaryItem  col-sm-3">
                    <span class="transSummaryItemValue">${postalAddress?.emailAddress}</span>
                </div>
            </div>


    </div>

    <div class="col-sm-8 card-warning">

        <div class="row">
            <div class="col-md-6">
                <div class="card-body">
                    <address>
                        <div class="center-block">
                            <div class="card-body">
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
            <div class="col-md-6">
                <div class="card-body">
                    <address>
                        <div class="center-block">
                            <div class="card-body">
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
    </div>

    <div class="col-sm-12 card-danger">
        <div class="card-header card-danger">
            Authentication information
        </div>
        <div class="card-info">
            <div class="transSummaryItem col-sm-3 card-header card-info">
                <span class="transSummaryItemName">Username:</span>
            </div>
            <div class="transSummaryItem col-sm-3 card-header card-info">
                <span class="transSummaryItemName"><b>${user.username}</b></span>
            </div>
        </div>
    </div>

<div class="col-sm-12 card-primary">
    <div class="card-header card-primary">
        Shipping and final Totals
    </div>
    <div class=" card-success">
        <div class="transSummaryItem col-sm-4 card-header card-success">
            <span class="transSummaryItemName">Shipping:</span>
        </div>
        <div class="transSummaryItem col-sm-4 card-header card-success">
            <span class="transSummaryItemName">Tax:</span>
        </div>
        <div class="transSummaryItem col-sm-4 card-header card-success">
            <span class="transSummaryItemName">Total:</span>
        </div>
    </div>
    <div>
        <div class="transSummaryItem col-sm-4">
            <g:if test="${payment?.shipping}">
                <span>${payment.shipping}</span>
            </g:if>
        </div>
        <div class="transSummaryItem col-sm-4">
            <g:if test="${payment?.tax}">
                <span>${payment.tax}</span>
            </g:if>
        </div>
        <div class="transSummaryItem col-sm-4">
            <g:if test="${payment?.gross}">
                <span>${payment.gross} ${payment.currency}</span>
            </g:if>
        </div>
    </div>
</div>
<pre>
${paypalPayment}
</pre>