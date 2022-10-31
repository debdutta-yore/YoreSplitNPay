package co.yore.splitnpay.libs.contact.core.entities.mapper

import co.yore.splitnpay.libs.contact.core.entities.Address
import co.yore.splitnpay.libs.contact.core.entities.cursor.AddressCursor

internal class AddressMapper(private val addressCursor: AddressCursor) : DataEntityMapper<Address> {

    override val value: Address
        get() = Address(
            id = addressCursor.dataId,
            rawContactId = addressCursor.rawContactId,
            contactId = addressCursor.contactId,

            isPrimary = addressCursor.isPrimary,
            isSuperPrimary = addressCursor.isSuperPrimary,

            type = addressCursor.type,
            label = addressCursor.label,

            formattedAddress = addressCursor.formattedAddress,
            street = addressCursor.street,
            poBox = addressCursor.poBox,
            neighborhood = addressCursor.neighborhood,
            city = addressCursor.city,
            region = addressCursor.region,
            postcode = addressCursor.postcode,
            country = addressCursor.country,

            isRedacted = false
        )
}
