package org.grails.plugin.payment

import grails.gorm.transactions.Transactional
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.OK

@Transactional(readOnly = true)
class PaymentConfigController {

    PaymentService paymentService

    static allowedMethods = [save: "POST", upload:"POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond PaymentConfig.list(params), model: [paymentConfigCount: PaymentConfig.count()]
    }

    def show(PaymentConfig paymentConfig) {
        respond paymentConfig
    }

    def edit(PaymentConfig paymentConfig) {
        respond paymentConfig
    }


    @Transactional
    def update(PaymentConfig paymentConfig ) {
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
