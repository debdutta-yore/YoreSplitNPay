package co.yore.splitnpay.libs.contact.core.entities.cursor

import android.database.Cursor
import co.yore.splitnpay.libs.contact.core.SimContactsField
import co.yore.splitnpay.libs.contact.core.SimContactsFields
import co.yore.splitnpay.libs.contact.core.entities.Entity

/**
 * Retrieves [SimContactsFields] data from the given [cursor].
 *
 * This does not modify the [cursor] position. Moving the cursor may result in different attribute
 * values.
 */
internal class SimContactCursor(cursor: Cursor, includeFields: Set<SimContactsField>) :
    AbstractEntityCursor<SimContactsField>(cursor, includeFields) {

    val id: Long by nonNullLong(SimContactsFields.Id, Entity.INVALID_ID)

    val name: String? by string(SimContactsFields.Name)

    val number: String? by string(SimContactsFields.Number)

    // val emails: String? by string(SimContactsFields.Emails)
}
