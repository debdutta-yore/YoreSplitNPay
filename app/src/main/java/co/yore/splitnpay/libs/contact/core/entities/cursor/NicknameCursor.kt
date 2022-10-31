package co.yore.splitnpay.libs.contact.core.entities.cursor

import android.database.Cursor
import co.yore.splitnpay.libs.contact.core.Fields
import co.yore.splitnpay.libs.contact.core.NicknameField

/**
 * Retrieves [Fields.Nickname] data from the given [cursor].
 *
 * This does not modify the [cursor] position. Moving the cursor may result in different attribute
 * values.
 */
internal class NicknameCursor(cursor: Cursor, includeFields: Set<NicknameField>) :
    AbstractDataCursor<NicknameField>(cursor, includeFields) {

    val name: String? by string(Fields.Nickname.Name)
}
