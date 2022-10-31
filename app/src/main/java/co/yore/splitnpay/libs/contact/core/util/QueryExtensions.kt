package co.yore.splitnpay.libs.contact.core.util

import android.content.ContentResolver
import co.yore.splitnpay.libs.contact.core.*
import co.yore.splitnpay.libs.contact.core.entities.cursor.contactsCursor
import co.yore.splitnpay.libs.contact.core.entities.cursor.dataContactsCursor
import co.yore.splitnpay.libs.contact.core.entities.cursor.dataCursor
import co.yore.splitnpay.libs.contact.core.entities.cursor.rawContactsCursor
import co.yore.splitnpay.libs.contact.core.entities.table.Table

// region Contacts table

internal fun ContentResolver.findContactIdsInContactsTable(
    contactsWhere: Where<ContactsField>?,
    suppressDbExceptions: Boolean = false,
    cancel: () -> Boolean = { false }
): Set<Long> = if (cancel()) emptySet() else {
    query(
        Table.Contacts, Include(ContactsFields.Id), contactsWhere,
        suppressDbExceptions = suppressDbExceptions
    ) {
        mutableSetOf<Long>().apply {
            val contactsCursor = it.contactsCursor()
            while (!cancel() && it.moveToNext()) {
                add(contactsCursor.contactId)
            }
        }
    } ?: emptySet()
}

// endregion

// region RawContacts table

internal fun ContentResolver.findContactIdsInRawContactsTable(
    rawContactsWhere: Where<RawContactsField>?,
    suppressDbExceptions: Boolean = false,
    cancel: () -> Boolean = { false }
): Set<Long> = if (cancel()) emptySet() else {
    query(
        Table.RawContacts,
        Include(RawContactsFields.ContactId),
        // There may be RawContacts that are marked for deletion that have not yet been deleted.
        (RawContactsFields.Deleted notEqualTo true) and rawContactsWhere,
        suppressDbExceptions = suppressDbExceptions
    ) {
        mutableSetOf<Long>().apply {
            val rawContactsCursor = it.rawContactsCursor()
            while (!cancel() && it.moveToNext()) {
                add(rawContactsCursor.contactId)
            }
        }
    } ?: emptySet()
}

// endregion

// region Data table

internal fun ContentResolver.findContactIdsInDataTable(
    where: Where<AbstractDataField>?, cancel: () -> Boolean = { false }
): Set<Long> = if (cancel()) emptySet() else {
    query(Table.Data, Include(Fields.Contact.Id), where) {
        mutableSetOf<Long>().apply {
            val contactsCursor = it.dataContactsCursor()
            while (!cancel() && it.moveToNext()) {
                add(contactsCursor.contactId)
            }
        }
    } ?: emptySet()
}

internal fun ContentResolver.findRawContactIdsInDataTable(
    where: Where<AbstractDataField>, cancel: () -> Boolean = { false }
): Set<Long> = query(Table.Data, Include(Fields.RawContact.Id), where) { cursor ->
    mutableSetOf<Long>().apply {
        val dataCursor = cursor.dataCursor()
        while (!cancel() && cursor.moveToNext()) {
            add(dataCursor.rawContactId)
        }
    }
} ?: emptySet()

// endregion