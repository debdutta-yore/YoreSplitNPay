package co.yore.splitnpay.libs.contact.core.entities.operation

import co.yore.splitnpay.libs.contact.core.Fields
import co.yore.splitnpay.libs.contact.core.NoteField
import co.yore.splitnpay.libs.contact.core.entities.MimeType
import co.yore.splitnpay.libs.contact.core.entities.NoteEntity

internal class NoteOperation(isProfile: Boolean, includeFields: Set<NoteField>) :
    AbstractDataOperation<NoteField, NoteEntity>(isProfile, includeFields) {

    override val mimeType = MimeType.Note

    override fun setValuesFromData(
        data: NoteEntity, setValue: (field: NoteField, dataValue: Any?) -> Unit
    ) {
        setValue(Fields.Note.Note, data.note)
    }
}