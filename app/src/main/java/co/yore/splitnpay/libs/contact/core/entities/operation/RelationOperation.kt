package co.yore.splitnpay.libs.contact.core.entities.operation

import co.yore.splitnpay.libs.contact.core.Fields
import co.yore.splitnpay.libs.contact.core.RelationField
import co.yore.splitnpay.libs.contact.core.entities.MimeType
import co.yore.splitnpay.libs.contact.core.entities.RelationEntity

internal class RelationOperation(isProfile: Boolean, includeFields: Set<RelationField>) :
    AbstractDataOperation<RelationField, RelationEntity>(isProfile, includeFields) {

    override val mimeType = MimeType.Relation

    override fun setValuesFromData(
        data: RelationEntity, setValue: (field: RelationField, dataValue: Any?) -> Unit
    ) {
        setValue(Fields.Relation.Type, data.type?.value)
        setValue(Fields.Relation.Label, data.label)
        setValue(Fields.Relation.Name, data.name)
    }
}