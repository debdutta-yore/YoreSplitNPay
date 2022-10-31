package co.yore.splitnpay.libs.contact.core.util

import co.yore.splitnpay.libs.contact.core.Contacts
import co.yore.splitnpay.libs.contact.core.`in`
import co.yore.splitnpay.libs.contact.core.entities.Group
import co.yore.splitnpay.libs.contact.core.entities.NewGroup
import co.yore.splitnpay.libs.contact.core.equalTo
import co.yore.splitnpay.libs.contact.core.groups.GroupsInsert

/**
 * Returns the newly created [Group] or null if the insert operation failed.
 *
 * Returns null if the insert operation failed or permissions are not granted.
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
fun GroupsInsert.Result.group(
    contacts: Contacts, group: NewGroup, cancel: () -> Boolean = { false }
): Group? = groupId(group)?.let { groupId ->
    contacts.groups().query()
        .where { Id equalTo groupId }
        .find(cancel)
        .firstOrNull()
}

/**
 * Returns the newly created [Group]s (for those insert operations that succeeded).
 *
 * Returns an empty list if the insert operation failed or permissions are not granted.
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
fun GroupsInsert.Result.groups(contacts: Contacts, cancel: () -> Boolean = { false }): List<Group> =
    if (groupIds.isEmpty()) {
        emptyList()
    } else {
        contacts.groups().query()
            .where { Id `in` groupIds }
            .find(cancel)
    }