package co.yore.splitnpay.libs.contact.core.entities.mapper

import co.yore.splitnpay.libs.contact.core.entities.Note
import co.yore.splitnpay.libs.contact.core.entities.cursor.NoteCursor

internal class NoteMapper(private val noteCursor: NoteCursor) : DataEntityMapper<Note> {

    override val value: Note
        get() = Note(
            id = noteCursor.dataId,
            rawContactId = noteCursor.rawContactId,
            contactId = noteCursor.contactId,

            isPrimary = noteCursor.isPrimary,
            isSuperPrimary = noteCursor.isSuperPrimary,

            note = noteCursor.note,

            isRedacted = false
        )
}
