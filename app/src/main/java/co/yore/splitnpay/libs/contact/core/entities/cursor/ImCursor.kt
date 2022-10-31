package co.yore.splitnpay.libs.contact.core.entities.cursor

import android.database.Cursor
import co.yore.splitnpay.libs.contact.core.Fields
import co.yore.splitnpay.libs.contact.core.ImField
import co.yore.splitnpay.libs.contact.core.entities.ImEntity

/**
 * Retrieves [Fields.Im] data from the given [cursor].
 *
 * This does not modify the [cursor] position. Moving the cursor may result in different attribute
 * values.
 */
internal class ImCursor(cursor: Cursor, includeFields: Set<ImField>) :
    AbstractDataCursor<ImField>(cursor, includeFields) {

    val protocol: ImEntity.Protocol? by type(
        Fields.Im.Protocol,
        typeFromValue = ImEntity.Protocol::fromValue
    )

    val customProtocol: String? by string(Fields.Im.CustomProtocol)

    val data: String? by string(Fields.Im.Data)
}
