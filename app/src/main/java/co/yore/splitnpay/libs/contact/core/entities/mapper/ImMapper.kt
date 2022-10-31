package co.yore.splitnpay.libs.contact.core.entities.mapper

import co.yore.splitnpay.libs.contact.core.entities.Im
import co.yore.splitnpay.libs.contact.core.entities.cursor.ImCursor

internal class ImMapper(private val imCursor: ImCursor) : DataEntityMapper<Im> {

    override val value: Im
        get() = Im(
            id = imCursor.dataId,
            rawContactId = imCursor.rawContactId,
            contactId = imCursor.contactId,

            isPrimary = imCursor.isPrimary,
            isSuperPrimary = imCursor.isSuperPrimary,

            protocol = imCursor.protocol,
            customProtocol = imCursor.customProtocol,

            data = imCursor.data,

            isRedacted = false
        )
}
