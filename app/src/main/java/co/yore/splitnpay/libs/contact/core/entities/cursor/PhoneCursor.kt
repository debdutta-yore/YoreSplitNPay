package co.yore.splitnpay.libs.contact.core.entities.cursor

import android.database.Cursor
import co.yore.splitnpay.libs.contact.core.Fields
import co.yore.splitnpay.libs.contact.core.PhoneField
import co.yore.splitnpay.libs.contact.core.entities.PhoneEntity

/**
 * Retrieves [Fields.Phone] data from the given [cursor].
 *
 * This does not modify the [cursor] position. Moving the cursor may result in different attribute
 * values.
 */
internal class PhoneCursor(cursor: Cursor, includeFields: Set<PhoneField>) :
    AbstractDataCursor<PhoneField>(cursor, includeFields) {

    val type: PhoneEntity.Type? by type(
        Fields.Phone.Type,
        typeFromValue = PhoneEntity.Type::fromValue
    )

    val label: String? by string(Fields.Phone.Label)

    val number: String? by string(Fields.Phone.Number)

    val normalizedNumber: String? by string(Fields.Phone.NormalizedNumber)
}
