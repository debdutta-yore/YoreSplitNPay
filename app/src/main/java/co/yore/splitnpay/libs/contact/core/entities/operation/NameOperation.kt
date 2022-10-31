package co.yore.splitnpay.libs.contact.core.entities.operation

import co.yore.splitnpay.libs.contact.core.Fields
import co.yore.splitnpay.libs.contact.core.NameField
import co.yore.splitnpay.libs.contact.core.entities.MimeType
import co.yore.splitnpay.libs.contact.core.entities.NameEntity

internal class NameOperation(isProfile: Boolean, includeFields: Set<NameField>) :
    AbstractDataOperation<NameField, NameEntity>(isProfile, includeFields) {

    override val mimeType = MimeType.Name

    override fun setValuesFromData(
        data: NameEntity, setValue: (field: NameField, dataValue: Any?) -> Unit
    ) {
        setValue(Fields.Name.DisplayName, data.displayName)

        setValue(Fields.Name.GivenName, data.givenName)
        setValue(Fields.Name.MiddleName, data.middleName)
        setValue(Fields.Name.FamilyName, data.familyName)

        setValue(Fields.Name.Prefix, data.prefix)
        setValue(Fields.Name.Suffix, data.suffix)

        setValue(Fields.Name.PhoneticGivenName, data.phoneticGivenName)
        setValue(Fields.Name.PhoneticMiddleName, data.phoneticMiddleName)
        setValue(Fields.Name.PhoneticFamilyName, data.phoneticFamilyName)
    }
}