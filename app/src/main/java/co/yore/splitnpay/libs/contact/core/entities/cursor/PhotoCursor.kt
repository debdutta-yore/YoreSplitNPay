package co.yore.splitnpay.libs.contact.core.entities.cursor

import android.database.Cursor
import co.yore.splitnpay.libs.contact.core.Fields
import co.yore.splitnpay.libs.contact.core.PhotoField

/**
 * Retrieves [Fields.Photo] data from the given [cursor].
 *
 * This does not modify the [cursor] position. Moving the cursor may result in different attribute
 * values.
 */
internal class PhotoCursor(cursor: Cursor, includeFields: Set<PhotoField>) :
    AbstractDataCursor<PhotoField>(cursor, includeFields) {

    val photoFileId: Long?
        get() {
            val value = getLong(Fields.Photo.PhotoFileId)
            // Sometimes the value will be zero instead of null but 0 is not a valid photo file id.
            return if (value != null && value > 0) value else null
        }

    val photoThumbnail: ByteArray? by blob(Fields.Photo.PhotoThumbnail)
}
