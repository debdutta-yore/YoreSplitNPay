package co.yore.splitnpay.libs.contact.core.entities.cursor

import android.database.Cursor
import co.yore.splitnpay.libs.contact.core.PhoneLookupField
import co.yore.splitnpay.libs.contact.core.PhoneLookupFields
import co.yore.splitnpay.libs.contact.core.entities.Entity

/**
 * Retrieves [PhoneLookupFields] data from the given [cursor].
 *
 * This does not modify the [cursor] position. Moving the cursor may result in different attribute
 * values.
 */
internal class PhoneLookupCursor(
    cursor: Cursor, includeFields: Set<PhoneLookupField>
) : AbstractEntityCursor<PhoneLookupField>(cursor, includeFields) {

    val id: Long by nonNullLong(PhoneLookupFields.Id, Entity.INVALID_ID)

    val contactId: Long by nonNullLong(PhoneLookupFields.ContactId, Entity.INVALID_ID)
}
