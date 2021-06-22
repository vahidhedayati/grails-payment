<address>
    <div class="center-block">
        <div class="title">
            <h2>Stripe Payment Details</h2>
            <hr/>
            <label>Charge Id</label>${payment?.stripeChargeId} <br/>
            <label>Status </label>${payment.stripeStatus}<br/>
            <label>BalanceTransaction </label>${payment.balanceTransaction}<br/>
            <label>Token </label>${payment.token}<br/>
        </div>
    </div>
</address>