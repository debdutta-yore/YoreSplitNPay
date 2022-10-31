package co.yore.splitnpay.libs.contact.core.entities.mapper

import co.yore.splitnpay.libs.contact.core.entities.Website
import co.yore.splitnpay.libs.contact.core.entities.cursor.WebsiteCursor

internal class WebsiteMapper(private val websiteCursor: WebsiteCursor) : DataEntityMapper<Website> {

    override val value: Website
        get() = Website(
            id = websiteCursor.dataId,
            rawContactId = websiteCursor.rawContactId,
            contactId = websiteCursor.contactId,

            isPrimary = websiteCursor.isPrimary,
            isSuperPrimary = websiteCursor.isSuperPrimary,

            url = websiteCursor.url,

            isRedacted = false
        )
}
