package co.yore.splitnpay.libs.contact.core.entities.mapper

import co.yore.splitnpay.libs.contact.core.entities.Contact
import co.yore.splitnpay.libs.contact.core.entities.Options
import co.yore.splitnpay.libs.contact.core.entities.cursor.JoinedContactsCursor

/**
 * Creates [Contact] instances. May be used for cursors from the Contacts or Data table.
 */
internal class ContactMapper(
    private val contactsCursor: JoinedContactsCursor,
    private val optionsMapper: EntityMapper<Options>
) : EntityMapper<Contact> {

    override val value: Contact
        get() = Contact(
            id = contactsCursor.contactId,
            lookupKey = contactsCursor.lookupKey,

            rawContacts = emptyList(),

            // These fields are accessible in both the Contacts and Data tables.
            displayNamePrimary = contactsCursor.displayNamePrimary,
            displayNameAlt = contactsCursor.displayNameAlt,

            lastUpdatedTimestamp = contactsCursor.lastUpdatedTimestamp,

            options = optionsMapper.value,

            photoFileId = contactsCursor.photoFileId,
            photoUri = contactsCursor.photoUri,
            photoThumbnailUri = contactsCursor.photoThumbnailUri,

            hasPhoneNumber = contactsCursor.hasPhoneNumber,

            isRedacted = false
        )
}