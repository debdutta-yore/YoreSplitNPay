package co.yore.splitnpay.libs.contact.core.util

import co.yore.splitnpay.libs.contact.core.Contacts
import co.yore.splitnpay.libs.contact.core.`in`
import co.yore.splitnpay.libs.contact.core.entities.Contact
import co.yore.splitnpay.libs.contact.core.equalTo

// Note that there is no need to handle isProfile here as ContactLinks operations do not support it.

/**
 * Returns the [Contact] that contains all of the successfully linked RawContacts. Returns null if
 * the link operation failed or permissions are not granted.
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
fun ContactLinkResult.contact(contacts: Contacts, cancel: () -> Boolean = { false }): Contact? =
    contactId?.let {
        contacts.query()
            .where { Contact.Id equalTo it }
            .find(cancel)
            .firstOrNull()
    }

/**
 * Returns all of the [Contact]s that are associated with each of the unlinked RawContacts.
 *
 * Returns an empty list if the link operation failed or permissions are not granted.
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
fun ContactUnlinkResult.contacts(
    contacts: Contacts,
    cancel: () -> Boolean = { false }
): List<Contact> = if (rawContactIds.isEmpty()) {
    emptyList()
} else {
    contacts.query().where { RawContact.Id `in` rawContactIds }.find(cancel)
}

