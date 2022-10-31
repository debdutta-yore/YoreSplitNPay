package co.yore.splitnpay.libs.contact.core.entities.mapper

import co.yore.splitnpay.libs.contact.core.entities.Event
import co.yore.splitnpay.libs.contact.core.entities.EventDate
import co.yore.splitnpay.libs.contact.core.entities.cursor.EventCursor

internal class EventMapper(private val eventCursor: EventCursor) : DataEntityMapper<Event> {

    override val value: Event
        get() = Event(
            id = eventCursor.dataId,
            rawContactId = eventCursor.rawContactId,
            contactId = eventCursor.contactId,

            isPrimary = eventCursor.isPrimary,
            isSuperPrimary = eventCursor.isSuperPrimary,

            type = eventCursor.type,
            label = eventCursor.label,

            date = EventDate.fromDateStrFromDb(eventCursor.date),

            isRedacted = false
        )
}
