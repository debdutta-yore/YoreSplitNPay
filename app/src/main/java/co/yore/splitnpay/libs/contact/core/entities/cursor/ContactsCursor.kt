package co.yore.splitnpay.libs.contact.core.entities.cursor

import android.database.Cursor
import android.net.Uri
import co.yore.splitnpay.libs.contact.core.ContactsField
import co.yore.splitnpay.libs.contact.core.ContactsFields
import co.yore.splitnpay.libs.contact.core.entities.Entity
import java.util.*

/**
 * Retrieves [ContactsFields] data from the given [cursor].
 *
 * This does not modify the [cursor] position. Moving the cursor may result in different attribute
 * values.
 */
internal class ContactsCursor(
    cursor: Cursor, includeFields: Set<ContactsField>
) : AbstractEntityCursor<ContactsField>(cursor, includeFields), JoinedContactsCursor {

    override val contactId: Long by nonNullLong(ContactsFields.Id, Entity.INVALID_ID)

    override val lookupKey: String? by string(ContactsFields.LookupKey)

    override val displayNamePrimary: String? by string(ContactsFields.DisplayNamePrimary)

    override val displayNameAlt: String? by string(ContactsFields.DisplayNameAlt)

    override val lastUpdatedTimestamp: Date? by date(ContactsFields.LastUpdatedTimestamp)

    val displayNameSource: Int? by int(ContactsFields.DisplayNameSource)

    // @TargetApi(Build.VERSION_CODES.LOLLIPOP) Not applicable to delegated properties
    val nameRawContactId: Long? by long(ContactsFields.NameRawContactId)

    override val photoFileId: Long?
        get() {
            val value = getLong(ContactsFields.PhotoFileId)
            // Sometimes the value will be zero instead of null but 0 is not a valid photo file id.
            return if (value != null && value > 0) value else null
        }

    override val photoUri: Uri? by uri(ContactsFields.PhotoUri)

    override val photoThumbnailUri: Uri? by uri(ContactsFields.PhotoThumbnailUri)

    override val hasPhoneNumber: Boolean? by boolean(ContactsFields.HasPhoneNumber)
}
