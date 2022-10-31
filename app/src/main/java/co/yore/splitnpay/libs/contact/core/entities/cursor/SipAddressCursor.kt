package co.yore.splitnpay.libs.contact.core.entities.cursor

import android.database.Cursor
import co.yore.splitnpay.libs.contact.core.Fields
import co.yore.splitnpay.libs.contact.core.SipAddressField

/**
 * Retrieves [Fields.SipAddress] data from the given [cursor].
 *
 * This does not modify the [cursor] position. Moving the cursor may result in different attribute
 * values.
 */
internal class SipAddressCursor(cursor: Cursor, includeFields: Set<SipAddressField>) :
    AbstractDataCursor<SipAddressField>(cursor, includeFields) {

    val sipAddress: String? by string(Fields.SipAddress.SipAddress)
}
