package co.yore.splitnpay.libs.contact.core.entities.operation

import co.yore.splitnpay.libs.contact.core.Fields
import co.yore.splitnpay.libs.contact.core.NicknameField
import co.yore.splitnpay.libs.contact.core.entities.MimeType
import co.yore.splitnpay.libs.contact.core.entities.NicknameEntity

internal class NicknameOperation(isProfile: Boolean, includeFields: Set<NicknameField>) :
    AbstractDataOperation<NicknameField, NicknameEntity>(isProfile, includeFields) {

    override val mimeType = MimeType.Nickname

    override fun setValuesFromData(
        data: NicknameEntity, setValue: (field: NicknameField, dataValue: Any?) -> Unit
    ) {
        setValue(Fields.Nickname.Name, data.name)
    }
}