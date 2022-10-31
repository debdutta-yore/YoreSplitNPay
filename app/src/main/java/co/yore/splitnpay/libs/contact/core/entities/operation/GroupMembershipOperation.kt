package co.yore.splitnpay.libs.contact.core.entities.operation

import android.accounts.Account
import android.content.ContentProviderOperation
import android.content.ContentResolver
import android.content.Context
import co.yore.splitnpay.libs.contact.core.Fields
import co.yore.splitnpay.libs.contact.core.GroupMembershipField
import co.yore.splitnpay.libs.contact.core.Include
import co.yore.splitnpay.libs.contact.core.accounts.accountForRawContactWithId
import co.yore.splitnpay.libs.contact.core.entities.GroupMembership
import co.yore.splitnpay.libs.contact.core.entities.GroupMembershipEntity
import co.yore.splitnpay.libs.contact.core.entities.MimeType
import co.yore.splitnpay.libs.contact.core.entities.mapper.groupMembershipMapper
import co.yore.splitnpay.libs.contact.core.groups.Groups
import co.yore.splitnpay.libs.contact.core.util.query

internal class GroupMembershipOperation(
    isProfile: Boolean,
    includeFields: Set<GroupMembershipField>,
    private val groups: Groups
) : AbstractDataOperation<GroupMembershipField, GroupMembershipEntity>(isProfile, includeFields) {

    override val mimeType = MimeType.GroupMembership

    override fun setValuesFromData(
        data: GroupMembershipEntity,
        setValue: (field: GroupMembershipField, dataValue: Any?) -> Unit
    ) {
        setValue(Fields.GroupMembership.GroupId, data.groupId)
    }

    /**
     * Inserts all of the [groupMemberships] that belong to the given [account].
     */
    fun insertForNewRawContact(
        groupMemberships: Collection<GroupMembershipEntity>,
        account: Account
    ): List<ContentProviderOperation> = mutableListOf<ContentProviderOperation>().apply {
        if (includeFields.isEmpty()) {
            // No-op when entity is blank or no fields are included.
            return@apply
        }


        val accountGroups = groups.query().accounts(account).find()
            .associateBy { it.id }
            .toMutableMap()

        // Ensure no duplicate group memberships by only comparing the groupId and that they belong
        // to the same account.
        groupMemberships
            .asSequence()
            .distinctBy { it.groupId }
            .filter { accountGroups[it.groupId] != null }
            .forEach { insertForNewRawContact(it)?.let(::add) }
    }

    /**
     * Provides the [ContentProviderOperation] for inserting or deleting the [groupMemberships] data
     * row(s) of the raw contact with the given [rawContactId]. A group membership cannot be updated
     * because it only contains an immutable reference to the group id.
     *
     * The given [groupMemberships] are compared to the memberships in the DB using
     * [GroupMembershipEntity.groupId] instead of the data row id. This allows consumers to pass
     * in new group membership entities without unnecessarily deleting rows in the DB with a
     * different row ID but the same group id.
     *
     * [GroupMembershipEntity]s that do not belong to the Account associated with the [rawContactId]
     * will be ignored. Also, memberships to default groups are never deleted.
     */
    fun updateInsertOrDelete(
        groupMemberships: Collection<GroupMembershipEntity>,
        rawContactId: Long,
        context: Context
    ): List<ContentProviderOperation> = mutableListOf<ContentProviderOperation>().apply {
        if (includeFields.isEmpty()) {
            // No-op when no fields are included.
            return@apply
        }

        // Groups must always be associated with an account. No account, no group operation.
        val account = context.contentResolver
            .accountForRawContactWithId(rawContactId) ?: return emptyList()

        // A map of Group.id -> Group
        val accountGroups = groups.query().accounts(account).find()
            .associateBy { it.id }

        // A map of Group.id -> GroupMembership
        val groupMembershipsInDB = context.contentResolver.getGroupMembershipsInDB(rawContactId)
            .asSequence()
            // There should not exist any memberships in the DB that does not belong to the same
            // account. Just in case though...
            .filter { accountGroups[it.groupId] != null }
            .associateBy { it.groupId }
            .toMutableMap()

        // Ensure no duplicate group memberships by only comparing the groupId and that they belong
        // to the same account.
        groupMemberships
            .asSequence()
            .distinctBy { it.groupId }
            .filter { accountGroups[it.groupId] != null }
            .forEach { groupMembership ->
                // Remove this groupMembership from the groupMembershipsInDB so that it will not
                // be deleted later down this function.
                if (groupMembershipsInDB.remove(groupMembership.groupId) == null) {
                    // If the groupMembership is not in the DB, insert it.
                    insertDataRowForRawContact(groupMembership, rawContactId)?.let(::add)
                }
                // Else if the groupMembership is in the DB, do nothing.
            }

        // Delete the remaining non-default groupMembershipsInDB.
        groupMembershipsInDB.values
            .asSequence()
            .filter {
                val groupId = it.groupId
                // Do no delete memberships to the default group!
                groupId != null && !accountGroups.getValue(groupId).isDefaultGroup
            }
            .forEach {
                add(deleteDataRowWithId(it.id))
            }
    }

    private fun ContentResolver.getGroupMembershipsInDB(rawContactId: Long):
            List<GroupMembership> = query(contentUri, INCLUDE, selectionWithMimeTypeForRawContact(rawContactId)) {

        mutableListOf<GroupMembership>().apply {
            val groupMembershipMapper = it.groupMembershipMapper()
            while (it.moveToNext()) {
                add(groupMembershipMapper.value)
            }
        }
    } ?: emptyList()
}

private val INCLUDE = Include(Fields.DataId, Fields.GroupMembership.GroupId)