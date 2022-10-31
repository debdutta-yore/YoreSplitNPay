package co.yore.splitnpay.libs.contact.core.entities.operation

import co.yore.splitnpay.libs.contact.core.Fields
import co.yore.splitnpay.libs.contact.core.SipAddressField
import co.yore.splitnpay.libs.contact.core.entities.MimeType
import co.yore.splitnpay.libs.contact.core.entities.SipAddressEntity

internal class SipAddressOperation(isProfile: Boolean, includeFields: Set<SipAddressField>) :
    AbstractDataOperation<SipAddressField, SipAddressEntity>(isProfile, includeFields) {

    override val mimeType = MimeType.SipAddress

    override fun setValuesFromData(
        data: SipAddressEntity, setValue: (field: SipAddressField, dataValue: Any?) -> Unit
    ) {
        setValue(Fields.SipAddress.SipAddress, data.sipAddress)
    }
}