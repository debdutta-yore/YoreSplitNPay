package co.yore.splitnpay.libs.contact.core.entities.cursor

import android.database.Cursor
import co.yore.splitnpay.libs.contact.core.BlockedNumbersField
import co.yore.splitnpay.libs.contact.core.BlockedNumbersFields
import co.yore.splitnpay.libs.contact.core.entities.Entity

/**
 * Retrieves [BlockedNumbersFields] data from the given [cursor].
 *
 * This does not modify the [cursor] position. Moving the cursor may result in different attribute
 * values.
 */
internal class BlockedNumbersCursor(cursor: Cursor, includeFields: Set<BlockedNumbersField>) :
    AbstractEntityCursor<BlockedNumbersField>(cursor, includeFields) {

    val id: Long by nonNullLong(BlockedNumbersFields.Id, Entity.INVALID_ID)

    val number: String? by string(BlockedNumbersFields.Number)

    val normalizedNumber: String? by string(BlockedNumbersFields.NormalizedNumber)
}
