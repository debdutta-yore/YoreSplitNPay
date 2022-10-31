package co.yore.splitnpay.libs.contact.core.util

import co.yore.splitnpay.libs.contact.core.entities.NewName
import co.yore.splitnpay.libs.contact.core.entities.NewPhone
import co.yore.splitnpay.libs.contact.core.entities.NewRawContact
import co.yore.splitnpay.libs.contact.core.entities.SimContactEntity

/**
 * Returns a new [NewRawContact] instance that may be used for insertion.
 */
fun SimContactEntity.toNewRawContact() = NewRawContact().also {
    it.setName(NewName(displayName = name))
    it.addPhone(NewPhone(type = null, number = number))
}

/**
 * Returns [this] collection of [SimContactEntity]s as list of [NewRawContact] that may be used for
 * insertion.
 */
fun Collection<SimContactEntity>.toNewRawContacts(): List<NewRawContact> =
    map { it.toNewRawContact() }


/**
 * Returns [this] sequence of [SimContactEntity]s as list of [NewRawContact] that may be used for
 * insertion.
 */
fun Sequence<SimContactEntity>.toNewRawContacts(): Sequence<NewRawContact> =
    map { it.toNewRawContact() }