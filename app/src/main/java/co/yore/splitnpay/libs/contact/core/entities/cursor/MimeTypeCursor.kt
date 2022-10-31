package co.yore.splitnpay.libs.contact.core.entities.cursor

import android.database.Cursor
import co.yore.splitnpay.libs.contact.core.Fields
import co.yore.splitnpay.libs.contact.core.GenericDataField
import co.yore.splitnpay.libs.contact.core.entities.MimeType
import co.yore.splitnpay.libs.contact.core.entities.custom.CustomDataRegistry

/**
 * Retrieves [Fields.MimeType] data from the given [cursor].
 *
 * This does not modify the [cursor] position. Moving the cursor may result in different attribute
 * values.
 */
internal class MimeTypeCursor(cursor: Cursor, private val customDataRegistry: CustomDataRegistry) :
    AbstractEntityCursor<GenericDataField>(cursor, setOf(Fields.MimeType)) {

    val mimeType: MimeType
        get() = MimeType.fromValue(getString(Fields.MimeType), customDataRegistry)
}
