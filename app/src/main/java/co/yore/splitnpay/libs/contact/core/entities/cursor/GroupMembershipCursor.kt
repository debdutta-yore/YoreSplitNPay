package co.yore.splitnpay.libs.contact.core.entities.cursor

import android.database.Cursor
import co.yore.splitnpay.libs.contact.core.Fields
import co.yore.splitnpay.libs.contact.core.GroupMembershipField

/**
 * Retrieves [Fields.GroupMembership] data from the given [cursor].
 *
 * This does not modify the [cursor] position. Moving the cursor may result in different attribute
 * values.
 */
internal class GroupMembershipCursor(cursor: Cursor, includeFields: Set<GroupMembershipField>) :
    AbstractDataCursor<GroupMembershipField>(cursor, includeFields) {

    val groupId: Long? by long(Fields.GroupMembership.GroupId)
}
