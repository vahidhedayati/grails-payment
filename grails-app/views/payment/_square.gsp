<%@ page import="org.grails.plugin.payment.PaymentConfig; org.grails.plugin.payment.listeners.PaymentConfigListener" %>

<script type="text/javascript" src="https://js.squareupsandbox.com/v2/paymentform"></script>
<g:if test="${PaymentConfigListener.getValue('squareMode')==PaymentConfig.LIVE}">
    <script src="https://web.squarecdn.com/v1/square.js"></script>
</g:if>
<g:else>
    <script src="https://sandbox.web.squarecdn.com/v1/square.js"></script>
</g:else>

<script type="text/javascript">
    function displayPaymentResults(status) {
        const statusContainer = document.getElementById(
            'payment-status-container'
        );
        if (status === 'SUCCESS') {
            statusContainer.classList.remove('is-failure');
            statusContainer.classList.add('is-success');
        } else {
            statusContainer.classList.remove('is-success');
            statusContainer.classList.add('is-failure');
        }

        statusContainer.style.visibility = 'visible';
    }

    function enableSquare() {

        document.getElementById("nonStripeSquare").style.display = "none";
        //document.getElementById("squareButton").style.display = "block";
        document.getElementById("squareCardFields").style.display = "block";
        async function main() {
            async function tokenize(paymentMethod) {
                const tokenResult = await paymentMethod.tokenize();
                if (tokenResult.status === 'OK') {
                    return tokenResult.token;
                } else {
                    let errorMessage = 'tokenization failed with status: '+tokenResult.status;
                    if (tokenResult.errors) {
                        errorMessage += ' and errors: '+JSON.stringify(tokenResult.errors);
                    }
                    throw new Error(errorMessage);
                }
            }
            const payments = Square.payments('${instance.squareApplicationId}', "${instance.squareLocationId}");
            const card = await payments.card();
            await card.attach('#card-container');
            const cardButton = document.getElementById('card-button');
            cardButton.addEventListener('click', submitSquareForm);
            async function submitSquareForm(event) {
                event.preventDefault();
                try {
                    const result = await card.tokenize();
                    if (result.status === 'OK') {
                        result.squareToken = await tokenize(card);
                        displayPaymentResults('SUCCESS')
                        //console.log(result.token+'+ --> '+JSON.stringify(result.squareToken))
                        //document.getElementById("squareToken").value=result.token.id
                        document.getElementById("squareToken").value=result.squareToken
                        document.getElementById("form").action = '/payment/squarecheckout'
                        document.getElementById("form").submit();
                    }
                } catch (e) {
                    displayPaymentResults('FAILURE')
                }
            };


        }
        main();
    }
</script>
