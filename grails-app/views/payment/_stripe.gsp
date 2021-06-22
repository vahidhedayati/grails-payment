<script src="https://js.stripe.com/v3/"></script>
<script type="text/javascript">
    var formContainer = document.querySelector('.shoppingCart');
    var form = formContainer.querySelector('.form');
    var error = form.querySelector('.error');
    var errorMessage = error.querySelector('.message');
    var stripe = Stripe('${instance.stripePublicKey}');
    var elementStyles = {
        base: {
            color: '#fff',
            fontWeight: 600,
            fontFamily: 'Quicksand, Open Sans, Segoe UI, sans-serif',
            fontSize: '16px',
            fontSmoothing: 'antialiased',

            ':focus': {
                color: '#424770',
            },

            '::placeholder': {
                color: '#9BACC8',
            },

            ':focus::placeholder': {
                color: '#CFD7DF',
            },
        },
        invalid: {
            color: '#fff',
            ':focus': {
                color: '#FA755A',
            },
            '::placeholder': {
                color: '#FFCCA5',
            },
        },
    };

    var elementClasses = {
        focus: 'focus',
        empty: 'empty',
        invalid: 'invalid',
    };

    function updateElements(elements) {

        // Listen for errors from each Element, and show error messages in the UI.
        var savedErrors = {};
        elements.forEach(function(element, idx) {
            element.on('change', function(event) {
                if (event.error) {
                    error.classList.add('visible');
                    savedErrors[idx] = event.error.message;
                    errorMessage.innerText = event.error.message;
                } else {
                    savedErrors[idx] = null;

                    // Loop over the saved errors and find the first one, if any.
                    var nextError = Object.keys(savedErrors)
                        .sort()
                        .reduce(function(maybeFoundError, key) {
                            return maybeFoundError || savedErrors[key];
                        }, null);

                    if (nextError) {
                        // Now that they've fixed the current error, show another one.
                        errorMessage.innerText = nextError;
                    } else {
                        // The user fixed the last error; no more errors.
                        error.classList.remove('visible');
                        errorMessage.innerText = '';
                    }
                }
            });
        });
    }

    var cardElement;
    document.addEventListener("DOMContentLoaded", function() {
        document.getElementById("stripeSubmit").addEventListener("click", function (e) {
            e.preventDefault();
            e.stopImmediatePropagation();
            e.stopPropagation();
            //triggerBrowserValidation();

            var plainInputsValid = true;
            Array.prototype.forEach.call(form.querySelectorAll('input'), function (
                input
            ) {
                if (input.checkValidity && !input.checkValidity()) {
                    plainInputsValid = false;
                    return;
                }
            });
            if (!plainInputsValid) {
                triggerBrowserValidation();
                return;
            }

            var name = document.getElementById("firstName").value + ' ' + document.getElementById("lastName").value
            var address1 = document.getElementById("line1").value + ' ' + document.getElementById("line2").value
            var city = document.getElementById("city").value
            var state = document.getElementById("state").value
            var country = document.getElementById("country").value
            var zip = document.getElementById("postcode").value

            var additionalData = {
                name: name ? name : undefined,
                address_line1: address1 ? address1 : undefined,
                address_city: city ? city : undefined,
                address_state: state ? state : undefined,
                address_country: country ? country : undefined,
                address_zip: zip ? zip : undefined,
            }


             try {
            stripe.createToken(cardElement, additionalData).then(function (result) {
                if (result.token) {
                    window.document.getElementById("stripeToken").value = result.token.id;
                    document.getElementById("form").submit();
                    //return true;
                    //form.action = '/payment/stripecheckout';
                   // form.submit();
                   // setTimeout(function() {return true}, 113000)
                    // document.getElementById("form").submit();

                }
            });
            } catch (e) {
               console.log('stripe.createToken'+JSON.stringify(e))
             }
        });
    })

    function enableStripe() {
        window.document.getElementById("form").action = '/payment/stripecheckout'
        var elements = stripe.elements();
        var cardNumber = elements.create('cardNumber', {
            style: elementStyles,
            classes: elementClasses,
            placeholder: 'Card number',
        });
        cardNumber.mount('#card-number');

        var cardExpiry = elements.create('cardExpiry', {
            style: elementStyles,
            classes: elementClasses,
            placeholder: 'Expiry',
        });
        cardExpiry.mount('#card-expiry');

        var cardCvc = elements.create('cardCvc', {
            style: elementStyles,
            classes: elementClasses,
            placeholder: 'Cvc',
        });
        cardCvc.mount('#card-cvc');
        cardElement = cardNumber
        registerElements(stripe,[cardNumber, cardExpiry, cardCvc]);
        document.getElementById("nonStripeSquare").style.display = "none";
        document.getElementById("stripeCardFields").style.display = "block";
        document.getElementById("stripeButton").style.display = "block";

    }
    function registerElements(stripe,elements) {
        updateElements(elements);

    }
    function enableInputs() {
        Array.prototype.forEach.call(
            form.querySelectorAll(
                "input[type='text'], input[type='email'], input[type='tel']"
            ),
            function(input) {
                input.removeAttribute('disabled');
            }
        );
    }

    function disableInputs() {
        Array.prototype.forEach.call(
            form.querySelectorAll(
                "input[type='text'], input[type='email'], input[type='tel']"
            ),
            function(input) {
                input.setAttribute('disabled', 'true');
            }
        );
    }
    function triggerBrowserValidation() {
        var submit = document.createElement('input');
        submit.type = 'submit';
        submit.style.display = 'none';
        form.appendChild(submit);
        submit.click();
        submit.remove();
    }
</script>