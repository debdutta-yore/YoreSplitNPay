package co.yore.splitnpay.libs.contact.core.entities.operation

import co.yore.splitnpay.libs.contact.core.EmailField
import co.yore.splitnpay.libs.contact.core.Fields
import co.yore.splitnpay.libs.contact.core.entities.EmailEntity
import co.yore.splitnpay.libs.contact.core.entities.MimeType

internal class EmailOperation(isProfile: Boolean, includeFields: Set<EmailField>) :
    AbstractDataOperation<EmailField, EmailEntity>(isProfile, includeFields) {

    override val mimeType = MimeType.Email

    override fun setValuesFromData(
        data: EmailEntity, setValue: (field: EmailField, dataValue: Any?) -> Unit
    ) {
        setValue(Fields.Email.Type, data.type?.value)
        setValue(Fields.Email.Label, data.label)
        setValue(Fields.Email.Address, data.address)
    }
}