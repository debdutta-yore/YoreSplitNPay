package co.yore.splitnpay.libs.contact.core.entities.mapper

import co.yore.splitnpay.libs.contact.core.entities.SipAddress
import co.yore.splitnpay.libs.contact.core.entities.cursor.SipAddressCursor

internal class SipAddressMapper(private val sipAddressCursor: SipAddressCursor) :
    DataEntityMapper<SipAddress> {

    override val value: SipAddress
        get() = SipAddress(
            id = sipAddressCursor.dataId,
            rawContactId = sipAddressCursor.rawContactId,
            contactId = sipAddressCursor.contactId,

            isPrimary = sipAddressCursor.isPrimary,
            isSuperPrimary = sipAddressCursor.isSuperPrimary,

            sipAddress = sipAddressCursor.sipAddress,

            isRedacted = false
        )
}
