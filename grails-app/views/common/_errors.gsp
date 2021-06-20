
<g:hasErrors bean="${instance}">
    <ul class="errors">
        <g:eachError bean="${instance}">
            <li class="error">${it}</li>
        </g:eachError>
    </ul>
</g:hasErrors>