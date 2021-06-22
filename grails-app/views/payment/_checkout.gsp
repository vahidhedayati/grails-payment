<asset:stylesheet src="payment.css" />
<g:render template="/common/errors"/>
<g:render template="/common/flash-message"/>
<div id="failSegment"></div>
<div class="card col-md-8">
    <div class=" row">
        <div id="shoppingCart" class="col-md-8  cart shoppingCart">
            <div class="row">
                <div class="col title">
                    <h4><b>Shopping Cart</b></h4>
                </div>
                <div class="col align-self-center text-right text-muted">
                    ${(instance?.cartQuantity > 0) ? instance?.cartQuantity : ''}
                </div>
            </div>
            <g:form controller="payment" action="paypalcheckout" class="form form-horizontal" name="form" >
                <div class="row border-top border-bottom">
                    <div >
                        <g:hiddenField name="currencyCode" value="${instance?.currencyCode}"/>

                        <g:render template="/payment/address-form"  model="[instance:instance,
                                                                   salutations: [[id:'Mr', name:'Mr'], [id:'Miss', name:'Miss']],
                                                                   address:instance?.address]"/>
                        <div class="row">
                            <div class="col">
                                <label for="address.saveInfo">Save details for next time?</label>
                                <g:checkBox id="saveInfo"  name="address.saveInfo"
                                            checked="${instance?.address?.saveInfo}" value="${instance?.address?.saveInfo}"  />
                            </div>
                        </div>
                        <div class="row border-bottom">
                            <div class="form-group" id="usernameField" >
                                <g:render template="/payment/username-password" model="[address:instance?.address]"/>
                            </div>
                        </div>
                        <input type="hidden" name="squareToken" value="" id="squareToken"/>
                        <input type="hidden" name="stripeToken" id="stripeToken" value=""/>
                            <!--CREDIT CART PAYMENT-->
                            <payment:internalButtons instance="${instance}" finalTotal="${instance?.finalTotal}" />

                    </div>
                </div>
            </g:form>
        </div>
        <div class="col-md-4 summary">

            <div class="row">
                <g:if test="${instance.editCartUrl}">
                    <div class="row border-bottom ">
                        <div class="col btn btn-block "></div>
                        <div class="col align-self-center text-right text-muted title">
                            <h4><a class="btn btn-default" href="${instance.editCartUrl}">Edit Cart</a></h4>
                        </div>
                        <p>&nbsp;</p>
                    </div>
                </g:if>
            </div>
            <p>&nbsp;</p>
            <div class="row">
                <div class="col title">
                    <h4><b>Review order</b></h4>
                </div>
            </div>
            <div >
                <p>&nbsp;</p>
                <item:each cart="${instance?.cart?:session?.cart}">
                    <div class="row border-bottom">
                        <div class="col-sm-8">
                            ${it?.item?.name} X (Qty: ${it?.qty})
                        </div>
                        <div class="col-sm-4 text-right">
                            <b>
                                <g:formatNumber number="${it?.itemTotal}" type="currency"
                                                currencyCode="${it?.item?.currency}"/>
                            </b>
                        </div>
                    </div>
                </item:each>
                <div class="row border-bottom">

                    <div class="col-sm-7">
                        <strong><h4>Subtotal</h4></strong>
                    </div>
                    <div class="col-sm-5 text-right">
                        <strong>  <h4>
                            <item:currentTotal finalTotal="${instance?.subTotal  ?
                                    instance?.subTotal :
                                    instance?.finalTotal }"
                                               currency="${instance?.currencyCode}"/>
                        </h4>
                        </strong>
                    </div>
                </h4>
                </div>
                <!--
                                    <div class="col">
                                        <small>Shipping</small>
                                        <div class="pull-right"><span>-</span></div>
                                    </div>
                                    -->

                <div class="row border-top border-bottom">

                    <div class="col-sm-7">
                        <strong><h3><b>Order Total</b></h3></strong>
                    </div>
                    <div class="col-sm-5 text-right">
                        <strong><h3><b>
                            <item:currentTotal finalTotal="${instance?.finalTotal}"
                                               currency="${instance?.currencyCode}"
                                               showHiddenField="${true}"/>
                        </b>
                        </h3>
                        </strong>
                    </div>
                </h3>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="row">&nbsp;</div>
<script type="text/javascript">


    document.addEventListener("DOMContentLoaded", function() {
        doUserBlock(document.getElementById("saveInfo"));
        document.getElementById("line1").required=false;
        document.getElementById("country").required=false;
        document.getElementById("emailAddress").required=false;
    })

    const checkbox = document.getElementById('saveInfo')
    checkbox.addEventListener('change', (event) => {
        doUserBlock(event.currentTarget)
    })

    function doUserBlock(value) {
        if (value.checked) {
            document.getElementById("usernameField").style.display = "block";
            let username = document.getElementById("username");
            let emailAddress = document.getElementById("emailAddress").value;
            if (!username.value && emailAddress) {
                username.value=emailAddress;
            }
            document.getElementById("username").required=true;
            document.getElementById("pass1").required=true;
            document.getElementById("pass2").required=true;
        } else {
            document.getElementById("username").value='';
            document.getElementById("pass1").value='';
            document.getElementById("pass2").value='';
            document.getElementById("usernameField").style.display = "none";
            document.getElementById("username").required=false;
            document.getElementById("pass1").required=true;
            document.getElementById("pass2").required=true;
        }
    }
</script>
