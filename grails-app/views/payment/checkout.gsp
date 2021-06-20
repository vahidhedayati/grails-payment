
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
    <title>
    <g:message code="payment.label"/>
    </title>
    <style>
    .clearfix:after {
        display: block;
        content: "";
        clear: both;
    }
    </style>
</head>
<body>
<g:render template="/common/errors"/>

<div class="container">
    <g:render template="/common/flash-message"/>
    <div id="failSegment"></div>

<div class="row cart-body shoppingCart">
    <g:form controller="payment" action="paypalcheckout" class="form form-horizontal" name="form" >
        <g:hiddenField name="currencyCode" value="${params?.currencyCode}"/>
      <table><tr><td>

            <!--SHIPPING METHOD-->

            <g:render template="address-form"  model="[instance:instance,
                                                       salutations: [[id:'Mr', name:'Mr'], [id:'Miss', name:'Miss']],
                                                       address:instance?.address]"/>



            <!--SHIPPING METHOD END-->
            <hr class="mb-4">


            <div class="form-group" id="usernameField" >
                <g:render template="username-password" model="[address:instance?.address]"/>
            </div>



            <hr class="mb-4">
            <!--CREDIT CART PAYMENT-->
            <payment:buttons instance="${instance}" finalTotal="${finalTotal}" />
        </td><td>

            <!--REVIEW ORDER-->
            <div class="panel panel-info">
                <div class="panel-heading">
                    Review Order <div class="pull-right"><small>

                    <g:link controller="payment" action="cart" class="check-bc">Edit Cart</g:link></small></div>
                </div>

                <example:each>


                    <div class="row">
                        <div class="col">

                            ${it.itemName}
                        </div>

                        <div class="col"><small>Quantity:<span>${it.qty}</span></small></div>

                        <div class="col text-right">
                            <h6>
                                <g:formatNumber number="${it.itemTotal}" type="currency"
                                                currencyCode="${it.currency}"/>
                            </h6>
                        </div>

                    </div>
                    <div class="row"><hr /></div>
                </example:each>

                <div class="row"><hr /></div>
                <div class="row">
                    <div class="col">
                        <strong>Subtotal</strong>
                        <div class="pull-right">
                            <strong>
                                <example:currentTotal>
                                    <g:formatNumber number="${finalTotal}"
                                                    type="currency"
                                                    currencyCode="${currency}"/>
                                </example:currentTotal>
                            </strong>
                        </div>
                    </div>
                    <!--
                                    <div class="col">
                                        <small>Shipping</small>
                                        <div class="pull-right"><span>-</span></div>
                                    </div>
                                    -->
                </div>
                <div class="row"><hr /></div>
                <div class="row">
                    <div class="col">
                        <strong>Order Total</strong>
                        <div class="pull-right">
                            <strong>
                                <example:currentTotal>
                                    <g:hiddenField name="finalTotal" value="${finalTotal}"/>
                                    <g:formatNumber number="${finalTotal}" type="currency"
                                                    currencyCode="${currency}"/>
                                </example:currentTotal>
                            </strong>
                        </div>
                    </div>
                </div>
            </div>
    </td>
        </tr>
      </table>
        <!--REVIEW ORDER END-->
    </div>


    </g:form>
</div>



<script type="text/javascript">
    function triggerBrowserValidation() {
        var submit = document.createElement('input');
        submit.type = 'submit';
        submit.style.display = 'none';
        form.appendChild(submit);
        submit.click();
        submit.remove();
    }

    document.addEventListener("DOMContentLoaded", function() {
        document.getElementById("line1").attributes["required"] = false;
        document.getElementById("country").attributes["required"] = false;
        document.getElementById("emailAddress").attributes["required"] = false;
    })


    //document.getElementById("saveInfo").addEventListener("click", saveInfoClick);


    // function saveInfoClick(e) {
    //   doUserBlock(e);
    //}

    function doUserBlock(value) {
        if (value.prop('checked')) {

            document.getElementById("usernameField").style.display = "block";
            var username = document.getElementById("username").value;
            var emailAddress = document.getElementById("emailAddress").value;
            if (!username && emailAddress) {
                $('#username').val(emailAddress);
            }
            document.getElementById("username").attributes["required"] = false;
            document.getElementById("pass1").attributes["required"] = false;
            document.getElementById("pass2").attributes["required"] = false;

        } else {
            document.getElementById("username").value='';
            document.getElementById("pass1").value='';
            document.getElementById("pass2").value='';
            document.getElementById("usernameField").style.display = "none";
            document.getElementById("username").attributes["required"] = false;
            document.getElementById("pass1").attributes["required"] = false;
            document.getElementById("pass2").attributes["required"] = false;

        }
    }
</script>
</body>
</html>