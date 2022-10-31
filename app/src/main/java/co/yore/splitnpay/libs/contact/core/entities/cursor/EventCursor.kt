package co.yore.splitnpay.libs.contact.core.entities.cursor

import android.database.Cursor
import co.yore.splitnpay.libs.contact.core.EventField
import co.yore.splitnpay.libs.contact.core.Fields
import co.yore.splitnpay.libs.contact.core.entities.EventEntity

/**
 * Retrieves [Fields.Event] data from the given [cursor].
 *
 * This does not modify the [cursor] position. Moving the cursor may result in different attribute
 * values.
 */
internal class EventCursor(cursor: Cursor, includeFields: Set<EventField>) :
    AbstractDataCursor<EventField>(cursor, includeFields) {

    val type: EventEntity.Type? by type(
        Fields.Event.Type,
        typeFromValue = EventEntity.Type::fromValue
    )

    val label: String? by string(Fields.Event.Label)

    val date: String? by string(Fields.Event.Date)
}
