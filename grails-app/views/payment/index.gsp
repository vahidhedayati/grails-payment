<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Something has gone wrong</title>
</head>
<body>
<h1>Something has gone wrong</h1>
<g:if test="${flash.message}">
    <div class="message" role="status">${flash.message}</div>
</g:if>
<p style="text-align: center">
    <a href="/">Take me home</a>
</p>
</body>
</html>