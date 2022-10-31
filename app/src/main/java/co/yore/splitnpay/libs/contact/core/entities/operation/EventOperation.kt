package co.yore.splitnpay.libs.contact.core.entities.operation

import co.yore.splitnpay.libs.contact.core.EventField
import co.yore.splitnpay.libs.contact.core.Fields
import co.yore.splitnpay.libs.contact.core.entities.EventEntity
import co.yore.splitnpay.libs.contact.core.entities.MimeType
import co.yore.splitnpay.libs.contact.core.entities.toDbString

internal class EventOperation(isProfile: Boolean, includeFields: Set<EventField>) :
    AbstractDataOperation<EventField, EventEntity>(isProfile, includeFields) {

    override val mimeType = MimeType.Event

    override fun setValuesFromData(
        data: EventEntity, setValue: (field: EventField, dataValue: Any?) -> Unit
    ) {
        setValue(Fields.Event.Type, data.type?.value)
        setValue(Fields.Event.Label, data.label)
        setValue(Fields.Event.Date, data.date?.toDbString())
    }
}