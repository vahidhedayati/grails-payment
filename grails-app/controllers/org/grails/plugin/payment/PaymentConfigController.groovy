package org.grails.plugin.payment

import grails.gorm.transactions.Transactional
import org.grails.plugin.payment.listeners.PaymentConfigListener

import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.OK

@Transactional(readOnly = true)
class PaymentConfigController {

    PaymentService paymentService

    static allowedMethods = [save: "POST", upload:"POST", update: "PUT", delete: "DELETE"]

    private def getIfEnabled() {
        if (!PaymentConfigListener.paymentConfigEnabled) {
            redirect(controller:'payment', action:'index')
            return
        }
    }
    def index(Integer max) {
        ifEnabled
        params.max = Math.min(max ?: 10, 100)
        respond PaymentConfig.list(params), model: [paymentConfigCount: PaymentConfig.count()]
    }

    def show(PaymentConfig paymentConfig) {
        ifEnabled
        respond paymentConfig
    }

    def edit(PaymentConfig paymentConfig) {
        ifEnabled
        respond paymentConfig
    }


    @Transactional
    def update(PaymentConfig paymentConfig ) {
        ifEnabled
        if (paymentConfig == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (paymentConfig.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond paymentConfig.errors, view: 'edit'
            return
        }
        paymentConfig.save flush: true

        paymentService.updatePaymentConfigListener(paymentConfig)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'paymentConfig.label', default: 'PaymentConfig'), paymentConfig.id])
                redirect paymentConfig
            }
            '*' { respond paymentConfig, [status: OK] }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'paymentConfig.label', default: 'PaymentConfig'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
