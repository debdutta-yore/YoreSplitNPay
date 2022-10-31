package co.yore.splitnpay.libs.contact.core.entities.cursor

import android.database.Cursor
import co.yore.splitnpay.libs.contact.core.AddressField
import co.yore.splitnpay.libs.contact.core.Fields
import co.yore.splitnpay.libs.contact.core.entities.AddressEntity

/**
 * Retrieves [Fields.Address] data from the given [cursor].
 *
 * This does not modify the [cursor] position. Moving the cursor may result in different attribute
 * values.
 */
internal class AddressCursor(cursor: Cursor, includeFields: Set<AddressField>) :
    AbstractDataCursor<AddressField>(cursor, includeFields) {

    val type: AddressEntity.Type? by type(
        Fields.Address.Type,
        typeFromValue = AddressEntity.Type::fromValue
    )

    val label: String? by string(Fields.Address.Label)

    val formattedAddress: String? by string(Fields.Address.FormattedAddress)

    val street: String? by string(Fields.Address.Street)

    val poBox: String? by string(Fields.Address.PoBox)

    val neighborhood: String? by string(Fields.Address.Neighborhood)

    val city: String? by string(Fields.Address.City)

    val region: String? by string(Fields.Address.Region)

    val postcode: String? by string(Fields.Address.PostCode)

    val country: String? by string(Fields.Address.Country)
}
