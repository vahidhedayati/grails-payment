
#### Address  binding some variables explained:

If no `username` is provided it will set it or use `emailAddress` as `username`
The overrideErrorHalts is a new variable introduced and if you wish to override username taken errors
set this to true so in case of buttons it will seamlessly work with existing users who placed orders etc

The plugin will try to logically work out if a given set of details is a true match to existing data.
If user is an existing user and the address details of line1 and line2 are identical then it will load up the existing user
If it's an existing user but the address details a different then in typical checkout mode it would complain username is taken
with `buttons` mode you can enable `overrideErrorHalts:true` the system will know to generate an alternative unused username to allow
a new sign up to complete sale. So perhaps in the case of buttons you want to enable this feature.

``` groovy
 address:[
    ..
    //By default false
    overrideErrorHalts:true  
    //by default false
    saveInfo:true 
    
    emailAddress='required@domain.com'  
    username=''
    ]
```




The `CartBean` has a lot of unmentioned definitions which are mostly used by system to configure checkout/buttons
Examples shown delve into basic parameters and format required to be posted
for buttons to actually post correct information to selected provider.

Front end currently asks for password and confirmation this is because it came from a template
that signed up user onto system via spring security, this plugin does not provide spring security

The buttons use vanilla JavaScript to control the basic stuff that it does, it is all to give you an idea
The buttons and processes can be tweaked by copying locally and updating.

By all means if you improve plugin get in touch and improve for everyone else too
