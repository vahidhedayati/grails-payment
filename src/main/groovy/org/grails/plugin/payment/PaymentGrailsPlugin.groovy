package org.grails.plugin.payment


import grails.plugins.Plugin

class PaymentGrailsPlugin extends Plugin {
    def version = "1.0"
    def grailsVersion = "4 > *"
    def title = "payment plugin"
    def description = """Payment plugin is a centralised payment system configurable for currently paypal/stripe/square  
centrally configurable for your web application"""
    def documentation = "https://github.com/vahidhedayati/grails-payment-plugin"
    def license = "APACHE"
    def developers = [name: 'Vahid Hedayati', email: 'badvad@gmail.com']
    def issueManagement = [system: 'GITHUB', url: 'https://github.com/vahidhedayati/grails-payment-plugin/issues']
    def scm = [url: 'https://github.com/vahidhedayati/grails-payment-plugin']
}
