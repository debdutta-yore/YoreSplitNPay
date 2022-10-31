package co.yore.splitnpay.libs.contact.core.util

import co.yore.splitnpay.libs.contact.core.Contacts
import co.yore.splitnpay.libs.contact.core.`in`
import co.yore.splitnpay.libs.contact.core.entities.Group
import co.yore.splitnpay.libs.contact.core.entities.GroupMembershipEntity
import co.yore.splitnpay.libs.contact.core.equalTo


/**
 * Returns the [Group] referenced by this membership.
 *
 * This may return null if the group no longer exists or permissions are not granted.
 *
 * ## Permissions
 *
 * The [contacts.core.ContactsPermissions.READ_PERMISSION] is required.
 *
 * ## Cancellation
 *
 * To cancel this operation at any time, the [cancel] function should return true.
 *
 * This is useful when running this function in a background thread or coroutine.
 *
 * ## Thread Safety
 *
 * This should be called in a background thread to avoid blocking the UI thread.
 */
// [ANDROID X] @WorkerThread (not using annotation to avoid dependency on androidx.annotation)
@JvmOverloads
fun GroupMembershipEntity.group(contacts: Contacts, cancel: () -> Boolean = { false }): Group? =
    groupId?.let {
        contacts.groups().query().where { Id equalTo it }.find(cancel).first()
    }

/**
 * Returns the groups referenced by these memberships.
 *
 * ## Permissions
 *
 * The [contacts.core.ContactsPermissions.READ_PERMISSION] is required.
 *
 * ## Cancellation
 *
 * To cancel this operation at any time, the [cancel] function should return true.
 *
 * This is useful when running this function in a background thread or coroutine.
 *
 * ## Thread Safety
 *
 * This should be called in a background thread to avoid blocking the UI thread.
 */
// [ANDROID X] @WorkerThread (not using annotation to avoid dependency on androidx.annotation)
@JvmOverloads
fun Collection<GroupMembershipEntity>.groups(
    contacts: Contacts,
    cancel: () -> Boolean = { false }
): List<Group> {
    val membershipIds = mapNotNull { it.groupId }

    return if (membershipIds.isEmpty()) {
        emptyList()
    } else {
        contacts.groups().query()
            .where { Id `in` membershipIds }
            .find(cancel)
    }
}