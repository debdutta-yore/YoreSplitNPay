package co.yore.splitnpay.libs.contact.core.entities.operation

import android.content.ContentProviderOperation
import android.content.ContentValues
import co.yore.splitnpay.libs.contact.core.SimContactsFields
import co.yore.splitnpay.libs.contact.core.entities.NewSimContact
import co.yore.splitnpay.libs.contact.core.entities.SimContactEntity
import co.yore.splitnpay.libs.contact.core.entities.table.Table

/**
 * Builds [ContentProviderOperation]s for [Table.SimContacts].
 */
internal class SimContactsOperation {

    fun insert(simContact: NewSimContact): ContentValues? = if (simContact.isBlank) {
        null
    } else {
        ContentValues().apply {
            put(SimContactsFields.Tag.columnName, simContact.name)
            put(SimContactsFields.Number.columnName, simContact.number)
        }
    }

    // The ID is not used here at all. Therefore, we can be very flexible by allowing any
    // implementations of SimContactEntity.
    fun update(
        originalSimContact: SimContactEntity, updatedSimContact: SimContactEntity
    ): ContentValues? = if (updatedSimContact.isBlank) {
        null
    } else {
        ContentValues().apply {
            put(SimContactsFields.Tag.columnName, originalSimContact.name)
            put(SimContactsFields.Number.columnName, originalSimContact.number)

            put(SimContactsFields.NewTag.columnName, updatedSimContact.name)
            put(SimContactsFields.NewNumber.columnName, updatedSimContact.number)
        }
    }
}