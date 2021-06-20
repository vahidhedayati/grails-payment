<table>
<tr>
<td width="40%">
<table>
    <thead>
    <tr>
        <th colspan="2"> Payment status</th>
    </tr>
    <tr>
        <th>Status</th>
        <th>Paid</th>
    </tr>
    </thead>
    <tbody>
<g:each var="sale" in="${payment.sales}">
    <tr>
        <td>
            <span class="transSummaryItemValue">${sale.state}</span>
        </td>
        <td>
            <span class="transSummaryItemValue">${sale.total}</span>
        </td>
    </tr>
</g:each>
</tbody>
</table>
</td>
<td width="60%">
    <table>
        <thead>
        <tr>
            <th colspan="3">   Items purchased</th>
        </tr>
        <tr>
            <th>Item Name</th>
            <th>Quantity</th>
            <th>Price</th>
        </tr>
        </thead>
        <tbody>
<g:each var="paymentItem" in="${payment.paymentItems}">
    <tr>
        <td>
            <span class="transSummaryItemValue">${paymentItem.itemName.encodeAsHTML()}</span>
        </td>
        <td>
            <span class="transSummaryItemValue">${paymentItem.quantity}</span>
        </td>
        <td>
            <span class="transSummaryItemValue">${paymentItem.amount * paymentItem.quantity}</span>
        </td>
    </tr>
</g:each>
        </tbody>
    </table>
</td>
</tr>
</table>

<g:if test="${!payment.hideUserDetails}">
    <div class="col-sm-12 panel-danger">
        <div class="panel-heading panel-danger">
            Authentication information
        </div>
        <div class="panel-info">
            <div class="transSummaryItem col-sm-3 panel-heading panel-info">
                <span class="transSummaryItemName">Username:</span>
            </div>
            <div class="transSummaryItem col-sm-3 panel-heading panel-info">
                <span class="transSummaryItemName"><b>${user.username}</b></span>
            </div>
        </div>
    </div>
</g:if>


<table>
    <thead>
    <tr>
        <th colspan="3"> Shipping and final Totals</th>
    </tr>
    <tr>
        <th>Shipping</th>
        <th>Tax</th>
        <th>Total</th>
    </tr>
    </thead>
    <tbody>

        <tr>
            <td>
                <g:if test="${payment?.shipping}">
                    <span>${payment.shipping}</span>
                </g:if>
            </td>
            <td>
                <g:if test="${payment?.tax}">
                    <span>${payment.tax}</span>
                </g:if>
            </td>
            <td>
                <g:if test="${payment?.gross}">
                    <span>${payment.gross} ${payment.currency}</span>
                </g:if>
            </td>
        </tr>

    </tbody>
</table>

