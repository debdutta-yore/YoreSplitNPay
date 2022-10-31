package co.yore.splitnpay.libs.contact.core.profile

import android.content.ContentProviderOperation.newDelete
import android.content.ContentResolver
import co.yore.splitnpay.libs.contact.core.*
import co.yore.splitnpay.libs.contact.core.entities.ExistingContactEntity
import co.yore.splitnpay.libs.contact.core.entities.ExistingRawContactEntityWithContactId
import co.yore.splitnpay.libs.contact.core.entities.operation.RawContactsOperation
import co.yore.splitnpay.libs.contact.core.entities.table.ProfileUris
import co.yore.splitnpay.libs.contact.core.util.applyBatch
import co.yore.splitnpay.libs.contact.core.util.deleteSuccess
import co.yore.splitnpay.libs.contact.core.util.isProfileId
import co.yore.splitnpay.libs.contact.core.util.unsafeLazy

/**
 * Deletes one or more (Profile) raw contacts or the (Profile) contact from the raw contacts and
 * contacts tables respectively. All associated (Profile) data rows are also deleted.
 *
 * ## Permissions
 *
 * The [ContactsPermissions.WRITE_PERMISSION] is assumed to have been granted already in these
 * examples for brevity. All deletes will do nothing if the permission is not granted.
 *
 * For API 22 and below, the permission "android.permission.WRITE_PROFILE" is also required but
 * only at the manifest level. Prior to API 23 (Marshmallow), permissions needed to be granted
 * prior to installation instead of at runtime.
 *
 * ## Usage
 *
 * To delete the profile [ExistingContactEntity] and all associated
 * [ExistingRawContactEntityWithContactId]s;
 *
 * ```kotlin
 * val result = profileDelete.contact().commit()
 * ```
 *
 * In Java,
 *
 * ```java
 * ProfileDelete.Result result = profileDelete.contact().commit();
 * ```
 */
interface ProfileDelete : CrudApi {

    /**
     * Adds the given profile [rawContacts] ([ExistingRawContactEntityWithContactId.isProfile]) to
     * the delete queue, which will be deleted on [commit].
     *
     * If [contact] is called, then any [ExistingRawContactEntityWithContactId]s provided here will
     * be ignored.
     *
     * ## Note
     *
     * Deleting all profile [ExistingRawContactEntityWithContactId]s of a profile
     * [ExistingContactEntity] will result in the deletion of the associated profile
     * [ExistingContactEntity]! However, the profile [ExistingContactEntity] will remain as long as
     * it has at least has one associated profile [ExistingRawContactEntityWithContactId].
     */
    fun rawContacts(vararg rawContacts: ExistingRawContactEntityWithContactId): ProfileDelete

    /**
     * See [ProfileDelete.rawContacts].
     */
    fun rawContacts(rawContacts: Collection<ExistingRawContactEntityWithContactId>): ProfileDelete

    /**
     * See [ProfileDelete.rawContacts].
     */
    fun rawContacts(rawContacts: Sequence<ExistingRawContactEntityWithContactId>): ProfileDelete

    /**
     * Adds the existing profile contact (if any) to the delete queue, which will be deleted on
     * [commit].
     *
     * If this is called, then any [ExistingRawContactEntityWithContactId]s provided via
     * [rawContacts] will be ignored.
     *
     * ## Note
     *
     * Deleting the profile [ExistingContactEntity] will result in the deletion of all associated
     * profile [ExistingRawContactEntityWithContactId]s!
     */
    fun contact(): ProfileDelete

    /**
     * Deletes the profile [ExistingContactEntity] or profile
     * [ExistingRawContactEntityWithContactId]s in the queue (added via [contact] and
     * [rawContacts]) and returns the [Result].
     *
     * ## Permissions
     *
     * Requires the [ContactsPermissions.WRITE_PERMISSION].
     *
     * For API 22 and below, the permission "android.permission.WRITE_PROFILE" is also required but
     * only at the manifest level. Prior to API 23 (Marshmallow), permissions needed to be granted
     * prior to installation instead of at runtime.
     *
     * ## Thread Safety
     *
     * This should be called in a background thread to avoid blocking the UI thread.
     */
    // [ANDROID X] @WorkerThread (not using annotation to avoid dependency on androidx.annotation)
    fun commit(): Result

    /**
     * Deletes the profile [ExistingContactEntity] or [ExistingRawContactEntityWithContactId]s in
     * the queue in one transaction. Either ALL deletes succeed or ALL fail.
     *
     * If [rawContacts] is used instead of [contact], then this is successful only if all
     * [ExistingRawContactEntityWithContactId]s provided in [rawContacts] have been successfully
     * deleted.
     *
     * ## Permissions
     *
     * Requires the [ContactsPermissions.WRITE_PERMISSION].
     *
     * For API 22 and below, the permission "android.permission.WRITE_PROFILE" is also required but
     * only at the manifest level. Prior to API 23 (Marshmallow), permissions needed to be granted
     * prior to installation instead of at runtime.
     *
     * ## Thread Safety
     *
     * This should be called in a background thread to avoid blocking the UI thread.
     */
    // [ANDROID X] @WorkerThread (not using annotation to avoid dependency on androidx.annotation)
    fun commitInOneTransaction(): Result

    /**
     * Returns a redacted instance where all private user data are redacted.
     *
     * ## Redacted instances may produce invalid results!
     *
     * Redacted instance may have critical information redacted, which is required to make
     * the operation work properly.
     *
     * **Redacted operations should typically only be used for logging in production!**
     */
    // We have to cast the return type because we are not using recursive generic types.
    override fun redactedCopy(): ProfileDelete

    interface Result : CrudApi.Result {

        /**
         * True if the profile [ExistingContactEntity] has been successfully deleted
         * (via [contact]).
         *
         * If [rawContacts] is used instead of [contact], then this is true only if all
         * [ExistingRawContactEntityWithContactId]s provided in [rawContacts] have been
         * successfully deleted.
         */
        val isSuccessful: Boolean

        /**
         * True if the [rawContact] has been successfully deleted provided via [rawContacts].
         *
         * This will always be true if the profile [ExistingContactEntity] has been successfully
         * deleted (via [contact]).
         */
        fun isSuccessful(rawContact: ExistingRawContactEntityWithContactId): Boolean

        // We have to cast the return type because we are not using recursive generic types.
        override fun redactedCopy(): Result
    }
}

@Suppress("FunctionName")
internal fun ProfileDelete(contacts: Contacts): ProfileDelete = ProfileDeleteImpl(contacts)

private class ProfileDeleteImpl(
    override val contactsApi: Contacts,

    private val rawContactIds: MutableSet<Long> = mutableSetOf(),
    private var deleteProfileContact: Boolean = false,

    override val isRedacted: Boolean = false
) : ProfileDelete {

    override fun toString(): String =
        """
            ProfileDelete {
                rawContactIds: $rawContactIds
                deleteProfileContact: $deleteProfileContact
                hasPermission: ${permissions.canUpdateDelete()}
                isRedacted: $isRedacted
            }
        """.trimIndent()

    // There isn't really anything to redact =)
    override fun redactedCopy(): ProfileDelete = ProfileDeleteImpl(
        contactsApi,

        rawContactIds,
        deleteProfileContact,

        isRedacted = true
    )

    override fun rawContacts(
        vararg rawContacts: ExistingRawContactEntityWithContactId
    ): ProfileDelete = rawContacts(rawContacts.asSequence())

    override fun rawContacts(
        rawContacts: Collection<ExistingRawContactEntityWithContactId>
    ): ProfileDelete = rawContacts(rawContacts.asSequence())

    override fun rawContacts(
        rawContacts: Sequence<ExistingRawContactEntityWithContactId>
    ): ProfileDelete = apply {
        rawContactIds.addAll(rawContacts.map { it.id })
    }

    override fun contact(): ProfileDelete = apply {
        deleteProfileContact = true
    }

    override fun commit(): ProfileDelete.Result {
        onPreExecute()

        return if ((rawContactIds.isEmpty() && !deleteProfileContact)
            || !permissions.canUpdateDelete()
        ) {
            ProfileDeleteAllResult(isSuccessful = false)
        } else if (deleteProfileContact) {
            ProfileDeleteResult(
                emptyMap(),
                profileContactDeleteSuccess = contentResolver.deleteProfileContact(),
            )
        } else {
            val rawContactsResult = mutableMapOf<Long, Boolean>()
            for (rawContactId in rawContactIds) {
                rawContactsResult[rawContactId] =
                    if (!rawContactId.isProfileId) {
                        // Intentionally fail the operation to ensure that this is only used for profile
                        // deletes. Otherwise, operation can succeed. This is only done to enforce API
                        // design.
                        false
                    } else {
                        contentResolver.deleteRawContactsWhere(
                            RawContactsFields.Id equalTo rawContactId
                        )
                    }
            }
            ProfileDeleteResult(
                rawContactsResult,
                profileContactDeleteSuccess = false,
            )
        }
            .redactedCopyOrThis(isRedacted)
            .also { onPostExecute(contactsApi, it) }
    }

    override fun commitInOneTransaction(): ProfileDelete.Result {
        onPreExecute()

        return if ((rawContactIds.isEmpty() && !deleteProfileContact)
            || !permissions.canUpdateDelete()
        ) {
            ProfileDeleteAllResult(isSuccessful = false)
        } else if (deleteProfileContact) {
            ProfileDeleteAllResult(isSuccessful = contentResolver.deleteProfileContact())
        } else {
            val profileRawContactIds = rawContactIds.filter { it.isProfileId }

            if (rawContactIds.size != profileRawContactIds.size) {
                // There are some non-profile RawContact Ids, fail without performing operation.
                ProfileDeleteAllResult(isSuccessful = false)
            } else {
                ProfileDeleteAllResult(
                    isSuccessful = contentResolver.applyBatch(
                        RawContactsOperation(true)
                            .deleteRawContactsWhere(RawContactsFields.Id `in` profileRawContactIds)
                    ).deleteSuccess
                )
            }
        }
            .redactedCopyOrThis(isRedacted)
            .also { onPostExecute(contactsApi, it) }
    }
}

private fun ContentResolver.deleteProfileContact(): Boolean =
    applyBatch(newDelete(ProfileUris.RAW_CONTACTS.uri).build()).deleteSuccess

private class ProfileDeleteResult private constructor(
    private val rawContactIdsResultMap: Map<Long, Boolean>,
    private val profileContactDeleteSuccess: Boolean,
    override val isRedacted: Boolean
) : ProfileDelete.Result {

    constructor(
        rawContactIdsResultMap: Map<Long, Boolean>,
        profileContactDeleteSuccess: Boolean
    ) : this(
        rawContactIdsResultMap,
        profileContactDeleteSuccess = profileContactDeleteSuccess,
        isRedacted = false
    )

    override fun toString(): String =
        """
            ProfileDelete.Result {
                isSuccessful: $isSuccessful
                profileContactDeleteSuccess: $profileContactDeleteSuccess
                rawContactIdsResultMap: $rawContactIdsResultMap
                isRedacted: $isRedacted
            }
        """.trimIndent()

    override fun redactedCopy(): ProfileDelete.Result = ProfileDeleteResult(
        rawContactIdsResultMap, profileContactDeleteSuccess,
        isRedacted = true
    )

    override val isSuccessful: Boolean by unsafeLazy {
        profileContactDeleteSuccess
                // By default, all returns true when the collection is empty. So, we override that.
                || rawContactIdsResultMap.run { isNotEmpty() && all { it.value } }
    }

    override fun isSuccessful(rawContact: ExistingRawContactEntityWithContactId): Boolean =
        rawContactIdsResultMap.getOrElse(rawContact.id) { false }
}

private class ProfileDeleteAllResult private constructor(
    override val isSuccessful: Boolean,
    override val isRedacted: Boolean
) : ProfileDelete.Result {

    constructor(isSuccessful: Boolean) : this(
        isSuccessful = isSuccessful,
        isRedacted = false
    )

    override fun toString(): String =
        """
            ProfileDelete.Result {
                isSuccessful: $isSuccessful
                isRedacted: $isRedacted
            }
        """.trimIndent()

    override fun redactedCopy(): ProfileDelete.Result = ProfileDeleteAllResult(
        isSuccessful = isSuccessful,
        isRedacted = true
    )

    override fun isSuccessful(
        rawContact: ExistingRawContactEntityWithContactId
    ): Boolean = isSuccessful
}