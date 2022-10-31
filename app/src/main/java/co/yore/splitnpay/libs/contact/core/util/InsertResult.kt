package co.yore.splitnpay.libs.contact.core.util

import co.yore.splitnpay.libs.contact.core.Contacts
import co.yore.splitnpay.libs.contact.core.Insert
import co.yore.splitnpay.libs.contact.core.`in`
import co.yore.splitnpay.libs.contact.core.entities.Contact
import co.yore.splitnpay.libs.contact.core.entities.NewRawContact
import co.yore.splitnpay.libs.contact.core.entities.RawContact
import co.yore.splitnpay.libs.contact.core.equalTo

/**
 * Returns the newly created [RawContact] or null if the insert operation failed.
 *
 * Supports RawContacts with native/custom data.
 *
 * ## Permissions
 *
 * The [contacts.core.ContactsPermissions.READ_PERMISSION] is required. Otherwise, null will be
 * returned if the permission is not granted.
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
fun Insert.Result.rawContact(
    contacts: Contacts,
    rawContact: NewRawContact,
    cancel: () -> Boolean = { false }
): RawContact? = rawContactId(rawContact)?.let { rawContactId ->
    contacts.query()
        .where { RawContact.Id equalTo rawContactId }
        .find(cancel)
        .asSequence()
        .flatMap { it.rawContacts.asSequence() }
        .find { it.id == rawContactId }
}

/**
 * Returns all newly created [RawContact]s (for those insert operations that succeeded).
 *
 * Supports RawContacts with native/custom data.
 *
 * ## Permissions
 *
 * The [contacts.core.ContactsPermissions.READ_PERMISSION] is required. Otherwise, null will be
 * returned if the permission is not granted.
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
fun Insert.Result.rawContacts(
    contacts: Contacts,
    cancel: () -> Boolean = { false }
): List<RawContact> =
    contacts.query().where { RawContact.Id `in` rawContactIds }
        .find(cancel)
        .asSequence()
        .flatMap { it.rawContacts.asSequence() }
        .filter { rawContactIds.contains(it.id) }
        .toList()

/**
 * Returns the newly created [Contact] containing the [RawContact] or null if the insert operation
 * failed.
 *
 * Supports Contacts with native/custom data.
 *
 * ## Permissions
 *
 * The [contacts.core.ContactsPermissions.READ_PERMISSION] is required. Otherwise, null will be
 * returned if the permission is not granted.
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
fun Insert.Result.contact(
    contacts: Contacts,
    rawContact: NewRawContact,
    cancel: () -> Boolean = { false }
): Contact? = rawContactId(rawContact)?.let { rawContactId ->
    contacts.query()
        .where { RawContact.Id equalTo rawContactId }
        .find(cancel)
        .firstOrNull()
}

/**
 * Returns all newly created [Contact]s containing the [RawContact]s (for those insert operations
 * that succeeded).
 *
 * Supports Contacts with native/custom data.
 *
 * Returns an empty list all insert operations failed.
 *
 * ## Permissions
 *
 * The [contacts.core.ContactsPermissions.READ_PERMISSION] is required. Otherwise, null will be
 * returned if the permission is not granted.
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
fun Insert.Result.contacts(
    contacts: Contacts,
    cancel: () -> Boolean = { false }
): List<Contact> =
    contacts.query()
        .where { RawContact.Id `in` rawContactIds }
        .find(cancel)