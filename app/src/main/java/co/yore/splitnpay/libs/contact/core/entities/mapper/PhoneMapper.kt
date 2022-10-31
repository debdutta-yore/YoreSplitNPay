package co.yore.splitnpay.libs.contact.core.entities.mapper

import co.yore.splitnpay.libs.contact.core.entities.Phone
import co.yore.splitnpay.libs.contact.core.entities.cursor.PhoneCursor

internal class PhoneMapper(private val phoneCursor: PhoneCursor) : DataEntityMapper<Phone> {

    override val value: Phone
        get() = Phone(
            id = phoneCursor.dataId,
            rawContactId = phoneCursor.rawContactId,
            contactId = phoneCursor.contactId,

            isPrimary = phoneCursor.isPrimary,
            isSuperPrimary = phoneCursor.isSuperPrimary,

            type = phoneCursor.type,
            label = phoneCursor.label,

            number = phoneCursor.number,
            normalizedNumber = phoneCursor.normalizedNumber,

            isRedacted = false
        )
}
