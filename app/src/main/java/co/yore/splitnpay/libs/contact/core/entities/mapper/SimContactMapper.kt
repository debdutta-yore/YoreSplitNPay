package co.yore.splitnpay.libs.contact.core.entities.mapper

import co.yore.splitnpay.libs.contact.core.entities.SimContact
import co.yore.splitnpay.libs.contact.core.entities.cursor.SimContactCursor

internal class SimContactMapper(private val simContactCursor: SimContactCursor) :
    EntityMapper<SimContact> {

    override val value: SimContact
        get() = SimContact(
            id = simContactCursor.id,

            name = simContactCursor.name,
            number = simContactCursor.number,
            // emails = simContactCursor.emails,

            isRedacted = false
        )
}
