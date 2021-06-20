package org.grails.plugin.payment
import grails.compiler.GrailsCompileStatic
import grails.gorm.DetachedCriteria
import groovy.transform.ToString
import org.codehaus.groovy.util.HashCodeHelper

@GrailsCompileStatic
@ToString(cache=true, includeNames=true, includePackage=false)
class PaymentUserPaymentAddress implements Serializable {

	private static final long serialVersionUID = 1

	PaymentUser user
	PaymentAddress address

	@Override
	boolean equals(other) {
		if (other instanceof PaymentUserPaymentAddress) {
			other.userId == user?.id && other.addressId == address?.id
		}
	}

	@Override
	int hashCode() {
		int hashCode = HashCodeHelper.initHash()
		if (user) {
			hashCode = HashCodeHelper.updateHash(hashCode, user.id)
		}
		if (address) {
			hashCode = HashCodeHelper.updateHash(hashCode, address.id)
		}
		hashCode
	}

	static PaymentUserPaymentAddress get(long userId, long addressId) {
		criteriaFor(userId, addressId).get()
	}

	static boolean exists(long userId, long addressId) {
		if (userId != null && addressId != null) {
			criteriaFor(userId, addressId).count()
		} else {
			return 0
		}
	}

	private static DetachedCriteria criteriaFor(long userId, long addressId) {
		PaymentUserPaymentAddress.where {
			user == PaymentUser.load(userId) &&
					address == PaymentAddress.load(addressId)
		}
	}

	static PaymentUserPaymentAddress create(PaymentUser user, PaymentAddress address, boolean flush = false) {
		def instance = new PaymentUserPaymentAddress(user: user, address: address)
		instance.save(flush: flush)
		instance
	}

	static boolean remove(PaymentUser u, PaymentAddress r) {
		if (u != null && r != null) {
			PaymentUserPaymentAddress.where { user == u && address == r }.deleteAll()
		}
	}

	static int removeAll(PaymentUser u) {
		u == null ? 0 : PaymentUserPaymentAddress.where { user == u }.deleteAll() as int
	}

	static int removeAll(PaymentAddress r) {
		r == null ? 0 : PaymentUserPaymentAddress.where { address == r }.deleteAll() as int
	}

	static constraints = {
		user nullable: false
		address nullable: false, validator: { PaymentAddress a, PaymentUserPaymentAddress ua ->
			if (ua?.user?.id && a!=null ) {
				if (PaymentUserPaymentAddress?.exists(ua?.user?.id, a?.id)) {
					return ['address.exists']
				}
			}
		}
	}

	static mapping = {
		id composite: ['user', 'address']
		version false
	}
}
