package co.yore.splitnpay.libs.contact.core.entities.operation

import co.yore.splitnpay.libs.contact.core.Fields
import co.yore.splitnpay.libs.contact.core.ImField
import co.yore.splitnpay.libs.contact.core.entities.ImEntity
import co.yore.splitnpay.libs.contact.core.entities.MimeType

internal class ImOperation(isProfile: Boolean, includeFields: Set<ImField>) :
    AbstractDataOperation<ImField, ImEntity>(isProfile, includeFields) {

    override val mimeType = MimeType.Im

    override fun setValuesFromData(
        data: ImEntity, setValue: (field: ImField, dataValue: Any?) -> Unit
    ) {
        setValue(Fields.Im.Protocol, data.protocol?.value)
        setValue(Fields.Im.CustomProtocol, data.customProtocol)
        setValue(Fields.Im.Data, data.data)
    }
}