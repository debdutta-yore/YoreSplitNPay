package co.yore.splitnpay.libs.contact.core

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.res.Resources
import android.telephony.TelephonyManager
import co.yore.splitnpay.libs.contact.core.accounts.Accounts
import co.yore.splitnpay.libs.contact.core.accounts.AccountsPermissions
import co.yore.splitnpay.libs.contact.core.blockednumbers.BlockedNumbers
import co.yore.splitnpay.libs.contact.core.data.Data
import co.yore.splitnpay.libs.contact.core.entities.custom.CustomDataRegistry
import co.yore.splitnpay.libs.contact.core.groups.Groups
import co.yore.splitnpay.libs.contact.core.log.EmptyLogger
import co.yore.splitnpay.libs.contact.core.log.Logger
import co.yore.splitnpay.libs.contact.core.log.LoggerRegistry
import co.yore.splitnpay.libs.contact.core.profile.Profile
import co.yore.splitnpay.libs.contact.core.sim.SimContacts

/**
 * Provides new [Query], [BroadQuery], [PhoneLookupQuery], [Insert], [Update], [Delete], [Data],
 * [Groups], [Profile], [Accounts], [BlockedNumbers], and [SimContacts] instances.
 *
 * ## Permissions
 *
 * - Add the "android.permission.READ_CONTACTS" to the AndroidManifest in order to [query] and
 *   [broadQuery].
 * - Add the "android.permission.WRITE_CONTACTS" to the AndroidManifest in order to [insert],
 *   [update], and [delete].
 *
 * Use [permissions] convenience functions to check for required permissions. The same permissions
 * apply to [Data], [Groups], [Profile], and [SimContacts].
 *
 * Use [accountsPermissions] convenience functions to check for required permissions to use the
 * [Accounts] API.
 */
interface Contacts {

    /**
     * Returns a new [Query] instance.
     */
    fun query(): Query

    /**
     * Returns a new [BroadQuery] instance.
     */
    fun broadQuery(): BroadQuery

    /**
     * Returns a new [PhoneLookupQuery] instance.
     */
    fun phoneLookupQuery(): PhoneLookupQuery

    /**
     * Returns a new [Insert] instance.
     */
    fun insert(): Insert

    /**
     * Returns a new [Update] instance.
     */
    fun update(): Update

    /**
     * Returns a new [Delete] instance.
     */
    fun delete(): Delete

    /**
     * Returns a new [Data] instance for non-Profile data operations.
     */
    fun data(): Data

    /**
     * Returns a new [Groups] instance.
     */
    fun groups(): Groups

    /**
     * Returns a new [Profile] instance.
     */
    fun profile(): Profile

    /**
     * Returns a new [Accounts] instance for non-profile operations.
     */
    fun accounts(): Accounts

    /**
     * Returns a new [Accounts] instance that is for profile or non-profile operations based on the
     * given [isProfile].
     */
    // @JvmOverloads cannot be used in interface methods...
    // fun accounts(isProfile: Boolean = false): Accounts
    fun accounts(isProfile: Boolean): Accounts

    /**
     * Returns a new [BlockedNumbers] instance.
     */
    fun blockedNumbers(): BlockedNumbers

    /**
     * Returns a new [SimContacts] instance.
     */
    fun sim(): SimContacts

    /**
     * Returns a [ContactsPermissions] instance, which provides functions for checking required
     * permissions for Contacts Provider operations.
     */
    val permissions: ContactsPermissions

    /**
     * Returns a [AccountsPermissions] instance, which provides functions for checking required
     * permissions for Account operations.
     */
    val accountsPermissions: AccountsPermissions

    /**
     * Reference to the Application's Context for use in extension functions and external library
     * modules. This is safe to hold on to. Not meant for consumer use.
     *
     * ## Developer notes
     *
     * It's safe to save a hard reference to the Application context as it is alive for as long as
     * the app is alive. No need to make this a weak reference and make our lives more difficult
     * for no reason. Other libraries do the same; e.g. coil.
     *
     * Don't believe me? Then read the official Android documentation about this posted back in
     * 2009; https://android-developers.googleblog.com/2009/01/avoiding-memory-leaks.html
     *
     * Obviously, we should not save a reference to any Activity context.
     *
     * Consumers of this should still use [Context.getApplicationContext] for redundancy, which
     * provides further protection.
     */
    val applicationContext: Context

    /**
     * Registry for [Logger].
     */
    val loggerRegistry: LoggerRegistry

    /**
     * Registry of custom data components, enabling queries, inserts, updates, and deletes for
     * custom data.
     */
    val customDataRegistry: CustomDataRegistry

    /**
     * Registry for all [CrudApi.Listener]s.
     */
    val apiListenerRegistry: CrudApiListenerRegistry
}

/**
 * Creates a new [Contacts] instance.
 */
@JvmOverloads
@Suppress("FunctionName")
fun Contacts(
    context: Context,
    customDataRegistry: CustomDataRegistry = CustomDataRegistry(),
    logger: Logger = EmptyLogger()
): Contacts {
    val apiListenerRegistry = CrudApiListenerRegistry()
    val loggerRegistry = LoggerRegistry(logger)
    return ContactsImpl(
        context.applicationContext,
        ContactsPermissions(context.applicationContext),
        AccountsPermissions(context.applicationContext),
        loggerRegistry,
        customDataRegistry,
        apiListenerRegistry.register(loggerRegistry.apiListener)
    )
}

/**
 * Creates a new [Contacts] instance.
 *
 * This is mainly exist for traditional Java conventions. Kotlin users should use the [Contacts]
 * function instead.
 */
object ContactsFactory {

    @JvmStatic
    @JvmOverloads
    fun create(
        context: Context,
        customDataRegistry: CustomDataRegistry = CustomDataRegistry(),
        logger: Logger = EmptyLogger()
    ): Contacts = Contacts(context, customDataRegistry, logger)

    @JvmStatic
    fun create(context: Context, logger: Logger): Contacts =
        create(context, CustomDataRegistry(), logger)
}

private class ContactsImpl(
    override val applicationContext: Context,
    override val permissions: ContactsPermissions,
    override val accountsPermissions: AccountsPermissions,
    override val loggerRegistry: LoggerRegistry,
    override val customDataRegistry: CustomDataRegistry,
    override val apiListenerRegistry: CrudApiListenerRegistry
) : Contacts {

    override fun query() = Query(this)

    override fun broadQuery() = BroadQuery(this)

    override fun phoneLookupQuery() = PhoneLookupQuery(this)

    override fun insert() = Insert(this)

    override fun update() = Update(this)

    override fun delete() = Delete(this)

    override fun data() = Data(this, false)

    override fun groups() = Groups(this)

    override fun profile() = Profile(this)

    override fun accounts() = accounts(false)

    override fun accounts(isProfile: Boolean) = Accounts(this, isProfile)

    override fun blockedNumbers() = BlockedNumbers(this)

    override fun sim() = SimContacts(this)
}

// region Shortcuts

internal val Contacts.contentResolver: ContentResolver
    get() = applicationContext.contentResolver

internal val Contacts.resources: Resources
    get() = applicationContext.resources

internal val Contacts.telephonyManager: TelephonyManager
    get() = applicationContext.getSystemService(Activity.TELEPHONY_SERVICE) as TelephonyManager

internal val Contacts.isSimCardReady: Boolean
    get() = telephonyManager.simState == TelephonyManager.SIM_STATE_READY

// endregion