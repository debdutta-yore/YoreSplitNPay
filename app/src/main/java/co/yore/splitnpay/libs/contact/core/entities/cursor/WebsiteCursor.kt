package co.yore.splitnpay.libs.contact.core.entities.cursor

import android.database.Cursor
import co.yore.splitnpay.libs.contact.core.Fields
import co.yore.splitnpay.libs.contact.core.WebsiteField

/**
 * Retrieves [Fields.Website] data from the given [cursor].
 *
 * This does not modify the [cursor] position. Moving the cursor may result in different attribute
 * values.
 */
internal class WebsiteCursor(cursor: Cursor, includeFields: Set<WebsiteField>) :
    AbstractDataCursor<WebsiteField>(cursor, includeFields) {

    val url: String? by string(Fields.Website.Url)
}
