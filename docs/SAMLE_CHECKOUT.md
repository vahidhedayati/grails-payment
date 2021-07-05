
### 2. Full checkout feature

>controller/TestController

```groovy
/**
     * Test checkout
     * Action index: has no variables set
     * index.gsp hard codes parameters set to
     * payment:checkout
     */
    def index() {}
```

>/test/index.gsp <payment:checkout

Instance is populated in gsp in this case but could be like first
example where instance comes from controller.
the instance model is quite flexible you can provide everything or as little as possible


```gsp 
<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Welcome to Grails</title>
</head>
<body>
<h4><g:render template="menu"/></h4>
<payment:checkout instance="[
        editCartUrl:g.createLink(controller:'test', action:'checkout'),
        cart:[
                [id:1, name:'item 1',  currency:'GBP', listPrice:1.10],
                [id:2, name:'item 2',  currency:'GBP', listPrice:1.00],
                [id:1, name:'item 1',  currency:'GBP', listPrice:1.10],
                [id:1, name:'item 1',  currency:'GBP', listPrice:1.10],
                [id:3, name:'item 3',  currency:'GBP', listPrice:0.50],
                [id:4, name:'item 4',  currency:'GBP', listPrice:0.50],
        ],
       
]"
/>
</body>
</html>

```