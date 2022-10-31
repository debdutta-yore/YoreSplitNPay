package co.yore.splitnpay.libs.contact.core.profile

import android.accounts.Account
import android.content.ContentResolver
import co.yore.splitnpay.libs.contact.core.*
import co.yore.splitnpay.libs.contact.core.entities.Contact
import co.yore.splitnpay.libs.contact.core.entities.cursor.rawContactsCursor
import co.yore.splitnpay.libs.contact.core.entities.custom.CustomDataRegistry
import co.yore.splitnpay.libs.contact.core.entities.mapper.ContactsMapper
import co.yore.splitnpay.libs.contact.core.entities.table.ProfileUris
import co.yore.splitnpay.libs.contact.core.util.isEmpty
import co.yore.splitnpay.libs.contact.core.util.query
import co.yore.splitnpay.libs.contact.core.util.toRawContactsWhere
import co.yore.splitnpay.libs.contact.core.util.unsafeLazy

/**
 * Queries the Contacts, RawContacts, and Data tables and returns the one and only profile
 * [Contact], if available.
 *
 * ## Permissions
 *
 * The [ContactsPermissions.READ_PERMISSION] is assumed to have been granted already in these
 * examples for brevity. If not granted, the query will do nothing and return null.
 *
 * For API 22 and below, the permission "android.permission.READ_PROFILE" is also required but only
 * at the manifest level. Prior to API 23 (Marshmallow), permissions needed to be granted prior
 * to installation instead of at runtime.
 *
 * ## Usage
 *
 * Here is an example query that returns the profile [Contact]. Include only RawContacts belonging
 * to the given account. Include only the name and email properties of the profile [Contact].
 *
 * In Kotlin,
 *
 * ```kotlin
 * val profileContact : Contact? = profileQuery.
 *      .accounts(account)
 *      .include { Name.all + Address.all }
 *      .find()
 * ```
 *
 * In Java,
 *
 * ```java
 * import static contacts.core.Fields.*;
 *
 * List<Contact> profileContact = profileQuery
 *      .accounts(account)
 *      .include(new ArrayList<>() {{
 *           addAll(Name.getAll());
 *           addAll(Address.getAll());
 *       }})
 *      .find();
 * ```
 */
interface ProfileQuery : CrudApi {

    /**
     * Limits the RawContacts and associated data to those associated with one of the given
     * accounts. The Contact returned will not contain data that belongs to other accounts not
     * specified in [accounts].
     *
     * If no accounts are specified (this function is not called or called with no Accounts), then
     * all RawContacts and associated data are included in the search.
     *
     * A null [Account] may be provided here, which results in RawContacts with no associated
     * Account to be included in the search. RawContacts without an associated account are
     * considered local or device-only contacts, which are not synced.
     */
    fun accounts(vararg accounts: Account?): ProfileQuery

    /**
     * See [ProfileQuery.accounts]
     */
    fun accounts(accounts: Collection<Account?>): ProfileQuery

    /**
     * See [ProfileQuery.accounts]
     */
    fun accounts(accounts: Sequence<Account?>): ProfileQuery

    /**
     * Includes only the given set of [fields] (data) in each of the matching contacts.
     *
     * If no fields are specified, then all fields are included. Otherwise, only the specified
     * fields will be included in addition to required API fields [Fields.Required] (e.g. IDs),
     * which are always included.
     *
     * When all fields are included in a query operation, all properties of Contacts, RawContacts,
     * and Data are populated with values from the database. Properties of fields that are included
     * are not guaranteed to be non-null because the database may actually have no data for the
     * corresponding field.
     *
     * When only some fields are included, only those included properties of Contacts, RawContacts,
     * and Data are populated with values from the database. Properties of fields that are not
     * included are guaranteed to be null.
     *
     * Note that this may affect performance. It is recommended to only include fields that will be
     * used to save CPU and memory.
     */
    fun include(vararg fields: AbstractDataField): ProfileQuery

    /**
     * See [ProfileQuery.include].
     */
    fun include(fields: Collection<AbstractDataField>): ProfileQuery

    /**
     * See [ProfileQuery.include].
     */
    fun include(fields: Sequence<AbstractDataField>): ProfileQuery

    /**
     * See [ProfileQuery.include].
     */
    fun include(fields: Fields.() -> Collection<AbstractDataField>): ProfileQuery

    /**
     * Returns the profile [Contact] (inside the [Result]), if available.
     *
     * ## Permissions
     *
     * Requires [ContactsPermissions.READ_PERMISSION].
     *
     * For API 22 and below, the permission "android.permission.READ_PROFILE" is also required but
     * only at the manifest level. Prior to API 23 (Marshmallow), permissions needed to be granted
     * prior to installation instead of at runtime.
     *
     * ## Thread Safety
     *
     * This should be called in a background thread to avoid blocking the UI thread.
     */
    // [ANDROID X] @WorkerThread (not using annotation to avoid dependency on androidx.annotation)
    fun find(): Result

    /**
     * Returns the profile [Contact] (inside the [Result]), if available.
     *
     * ## Permissions
     *
     * Requires [ContactsPermissions.READ_PERMISSION].
     *
     * For API 22 and below, the permission "android.permission.READ_PROFILE" is also required but
     * only at the manifest level. Prior to API 23 (Marshmallow), permissions needed to be granted
     * prior to installation instead of at runtime.
     *
     * ## Cancellation
     *
     * The number of contacts and contact data found and processed may be large, which results
     * in this operation to take a while. Therefore, cancellation is supported while the contacts
     * list is being built. To cancel at any time, the [cancel] function should return true.
     *
     * This is useful when running this function in a background thread or coroutine.
     *
     * ## Thread Safety
     *
     * This should be called in a background thread to avoid blocking the UI thread.
     */
    // [ANDROID X] @WorkerThread (not using annotation to avoid dependency on androidx.annotation)
    // @JvmOverloads cannot be used in interface methods...
    // fun find(cancel: () -> Boolean = { false }): Result
    fun find(cancel: () -> Boolean): Result

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
    override fun redactedCopy(): ProfileQuery

    /**
     * Contains the Profile [contact].
     */
    interface Result : CrudApi.Result {

        /**
         * The Profile [Contact], if exist.
         */
        val contact: Contact?

        // We have to cast the return type because we are not using recursive generic types.
        override fun redactedCopy(): Result
    }
}

@Suppress("FunctionName")
internal fun ProfileQuery(contacts: Contacts): ProfileQuery = ProfileQueryImpl(contacts)

private class ProfileQueryImpl(
    override val contactsApi: Contacts,

    private var rawContactsWhere: Where<RawContactsField>? = DEFAULT_RAW_CONTACTS_WHERE,
    private var include: Include<AbstractDataField> = contactsApi.includeAllFields(),

    override val isRedacted: Boolean = false
) : ProfileQuery {

    override fun toString(): String =
        """
            ProfileQuery {
                rawContactsWhere: $rawContactsWhere
                include: $include
                hasPermission: ${permissions.canQuery()}
                isRedacted: $isRedacted
            }
        """.trimIndent()

    override fun redactedCopy(): ProfileQuery = ProfileQueryImpl(
        contactsApi,

        // Redact Account information.
        rawContactsWhere?.redactedCopy(),
        include,

        isRedacted = true
    )

    override fun accounts(vararg accounts: Account?) = accounts(accounts.asSequence())

    override fun accounts(accounts: Collection<Account?>) = accounts(accounts.asSequence())

    override fun accounts(accounts: Sequence<Account?>): ProfileQuery = apply {
        rawContactsWhere = accounts.toRawContactsWhere()?.redactedCopyOrThis(isRedacted)
    }

    override fun include(vararg fields: AbstractDataField) = include(fields.asSequence())

    override fun include(fields: Collection<AbstractDataField>) = include(fields.asSequence())

    override fun include(fields: Sequence<AbstractDataField>): ProfileQuery = apply {
        include = if (fields.isEmpty()) {
            contactsApi.includeAllFields()
        } else {
            Include(fields + REQUIRED_INCLUDE_FIELDS)
        }
    }

    override fun include(fields: Fields.() -> Collection<AbstractDataField>) =
        include(fields(Fields))

    override fun find(): ProfileQuery.Result = find { false }

    override fun find(cancel: () -> Boolean): ProfileQuery.Result {
        onPreExecute()

        val profileContact = if (!permissions.canQuery()) {
            null
        } else {
            contentResolver.resolve(customDataRegistry, rawContactsWhere, include, cancel)
        }
        return ProfileQueryResult(profileContact)
            .redactedCopyOrThis(isRedacted)
            .also { onPostExecute(contactsApi, it) }
    }

    private companion object {
        val DEFAULT_RAW_CONTACTS_WHERE: Where<RawContactsField>? = null
        val REQUIRED_INCLUDE_FIELDS by unsafeLazy { Fields.Required.all.asSequence() }
    }
}

private fun ContentResolver.resolve(
    customDataRegistry: CustomDataRegistry,
    rawContactsWhere: Where<RawContactsField>?,
    include: Include<AbstractDataField>,
    cancel: () -> Boolean
): Contact? {
    val rawContactIds = rawContactIds(rawContactsWhere, cancel)

    // Data table queries using profile uris only return user profile data.
    // Yes, I am aware of IS_USER_PROFILE and RAW_CONTACT_IS_USER_PROFILE but those seem to cause
    // queries to throw an exception.
    val contactsMapper = ContactsMapper(customDataRegistry, cancel)

    // Get the Data, RawContacts, and Contact from the Data table.
    if (!cancel() && rawContactIds.isNotEmpty()) {
        query(
            ProfileUris.DATA.uri,
            include,
            Fields.RawContact.Id `in` rawContactIds,
            processCursor = contactsMapper::processDataCursor
        )
    }

    // If Contact only has blank RawContacts (no Data rows), then we need to query the Contacts
    // table. This query should be done before a potential query to the RawContacts table so that
    // the mapper saves the Contact with the values in the Contacts table instead of the RawContacts
    // table. Some fields such as options are different depending on the table. The Contacts and
    // Data table contains options of the Contacts. The RawContacts table contains the options of
    // the RawContacts. This libs' Contact model requires the options of the Contacts table.
    if (!cancel() && rawContactIds.isNotEmpty() && contactsMapper.contactIds.isEmpty()) {
        query(
            ProfileUris.CONTACTS.uri,
            include.onlyContactsFields(),
            null,
            processCursor = contactsMapper::processContactsCursor
        )
    }

    // Get the blank RawContacts (no Data rows), which are those RawContacts that have not been
    // retrieved from the data query.
    val blankRawContactIds = rawContactIds.minus(contactsMapper.rawContactIds)
    if (!cancel() && blankRawContactIds.isNotEmpty()) {
        query(
            ProfileUris.RAW_CONTACTS.uri,
            include.onlyRawContactsFields(),
            RawContactsFields.Id `in` blankRawContactIds,
            processCursor = contactsMapper::processRawContactsCursor
        )
    }

    return contactsMapper.map().firstOrNull()
}

private fun ContentResolver.rawContactIds(
    rawContactsWhere: Where<RawContactsField>?, cancel: () -> Boolean
): Set<Long> = query(
    ProfileUris.RAW_CONTACTS.uri,
    Include(RawContactsFields.Id),
    // There may be RawContacts that are marked for deletion that have not yet been deleted.
    (RawContactsFields.Deleted notEqualTo true) and rawContactsWhere
) {
    mutableSetOf<Long>().apply {
        val rawContactsCursor = it.rawContactsCursor()
        while (!cancel() && it.moveToNext()) {
            add(rawContactsCursor.rawContactId)
        }

        // Ensure incomplete data sets are not returned.
        if (cancel()) {
            clear()
        }
    }
} ?: emptySet()

private class ProfileQueryResult private constructor(
    override val contact: Contact?,
    override val isRedacted: Boolean
) : ProfileQuery.Result {

    constructor(contact: Contact?) : this(contact, false)

    override fun toString(): String =
        """
            ProfileQuery.Result {
                Profile contact: $contact
                isRedacted: $isRedacted
            }
        """.trimIndent()

    override fun redactedCopy(): ProfileQuery.Result = ProfileQueryResult(
        contact?.redactedCopy(),
        isRedacted = true
    )
}