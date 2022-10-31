package co.yore.splitnpay.libs.contact.core.entities.mapper

import co.yore.splitnpay.libs.contact.core.entities.GroupMembership
import co.yore.splitnpay.libs.contact.core.entities.cursor.GroupMembershipCursor

internal class GroupMembershipMapper(private val groupMembershipCursor: GroupMembershipCursor) :
    DataEntityMapper<GroupMembership> {

    override val value: GroupMembership
        get() = GroupMembership(
            id = groupMembershipCursor.dataId,
            rawContactId = groupMembershipCursor.rawContactId,
            contactId = groupMembershipCursor.contactId,

            isPrimary = groupMembershipCursor.isPrimary,
            isSuperPrimary = groupMembershipCursor.isSuperPrimary,

            groupId = groupMembershipCursor.groupId,

            isRedacted = false
        )
}
