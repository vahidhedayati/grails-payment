package org.grails.plugin.payment.beans

import grails.converters.JSON
import grails.validation.Validateable

class AddressBean implements Validateable  {

    Long id
    Boolean isNew
    Boolean primaryAddress

    String countryCode
    String country

    String city
    String postcode


    boolean existingUser

    Boolean saveInfo
    String username
    String password
    String confirmPassword

    String title
    String firstName
    String lastName

    String roleName='ROLE_USER'
    Date birthDate


    //Required
    String line1
    String line2

    // end Required

    BigDecimal longitude
    BigDecimal latitude
    String state

    Date dateCreated  //auto
    Date lastUpdated    //auto


    String telephone
    String emailAddress


    boolean remoteCall

    static constraints = {
        id(nullable:true)
        username(nullable:true,maxSize:80)//,validator:this.firstPassword)
        password(nullable:true,maxSize:20,validator:this.firstPassword)
        confirmPassword(nullable:true,maxSize:20,validator:this.confirmPass)

        line2(nullable:true)
        title (nullable: false, enumType: "ordinal")
        firstName (nullable: false,  shared:'alphabet') //, matches: /^((.)(?!\2))+$/)
        lastName (nullable: false,  shared:'alphabet')

        birthDate(nullable:true)

        postcode(nullable:true)
        city(nullable:true)
        country(nullable:true)

        longitude nullable: true
        latitude nullable: true
        state (nullable: true, shared:'alphabet')


        saveInfo:nullable:true


        telephone(shared:'phone',nullable:true)
        emailAddress (nullable:true, email:true, maxSize:120)
        isNew(nullable:true)
        primaryAddress(nullable:true)

        dateCreated(nullable:true)
        lastUpdated(nullable:true)

        saveInfo(nullable:true)

    }


    boolean getIsNewSignup() {
        return (saveInfo && username && password && confirmPassword|| remoteCall && username)
    }

    static def firstPassword={val,obj,errors->
        if (val && val.length()<5) errors.rejectValue('password','tooShort.password')
    }

    static def confirmPass={val,obj,errors->
        if (val && val!=obj.password) errors.rejectValue('confirmPassword','password.mismatch')
    }

    List getAddressArray() {
        List a=[]
        if (firstName) {
            if (lastName) {
                a << firstName+" "+lastName
            } else {
                a << firstName
            }
        }
        if (line1) {
            a <<line1
        }
        if (line2) {
            a <<line2
        }
        if (city) {
            a <<city
        }
        if (country) {
            a <<country
        }
        if (postcode) {
            a <<postcode
        }
        if (emailAddress) {
            a <<emailAddress
        }
        return a
    }


    String loadJSON() {
        return (loadBasicValues() as JSON).toString()
    }
    Map loadBasicValues() {
        Map result = [:]
        result.line1= line1
        result.line2= line2?:''
        result.city= city?:''
        result.country= country?:''
        result.postcode= postcode?:''
        result.state= state?:''
        result.title= title?:''
        result.firstName= firstName
        result.lastName= lastName

        result.telephone= telephone?:''
        return result
    }
    Map loadValues() {
        Map result = [:]

        result.countryCode= countryCode?:''
        result.country= country?:''

        result.city= city?:''
        result.postcode= postcode?:''


        result.username= username
        result.password= password
        result.confirmPassword= confirmPassword

        result.title= title
        result.firstName= firstName
        result.lastName= lastName

        result.roleName=roleName
        result.birthDate= birthDate

        result.line1= line1
        result.line2= line2?:''
        result.city= city?:''
        result.country= country?:''
        result.postcode= postcode?:''

        result.longitude= longitude?:''
        result.latitude= latitude?:''
        result.state= state?:''



        result.telephone= telephone?:''
        result.emailAddress= emailAddress?:''

        result.birthDate = birthDate?:''

        return result
    }

    String getFriendlyName() {
        return ((firstName ? (firstName+' '+lastName?:'') :
                (lastName?:'')))
    }
}
