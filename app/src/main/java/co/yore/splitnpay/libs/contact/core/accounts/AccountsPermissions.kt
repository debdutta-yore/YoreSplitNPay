package co.yore.splitnpay.libs.contact.core.accounts

import android.Manifest
import android.content.Context
import co.yore.splitnpay.libs.contact.core.ContactsPermissions.Companion.READ_PERMISSION
import co.yore.splitnpay.libs.contact.core.ContactsPermissions.Companion.WRITE_PERMISSION
import co.yore.splitnpay.libs.contact.core.accounts.AccountsPermissions.Companion.GET_ACCOUNTS_PERMISSION
import co.yore.splitnpay.libs.contact.core.isPermissionGrantedFor

/**
 * Provides functions for checking account permissions.
 */
interface AccountsPermissions {

    /**
     * Returns true if [GET_ACCOUNTS_PERMISSION] and [READ_PERMISSION] are granted.
     */
    fun canQueryAccounts(): Boolean

    /**
     * Returns true if [READ_PERMISSION] is granted.
     */
    fun canQueryRawContacts(): Boolean

    /**
     * Returns true if [GET_ACCOUNTS_PERMISSION] and [WRITE_PERMISSION] are granted.
     */
    fun canUpdateLocalRawContactsAccount(): Boolean

    companion object {
        const val GET_ACCOUNTS_PERMISSION: String = Manifest.permission.GET_ACCOUNTS
    }
}

@Suppress("FunctionName")
internal fun AccountsPermissions(context: Context): AccountsPermissions =
    AccountsPermissionsImpl(context.applicationContext)

private class AccountsPermissionsImpl(private val applicationContext: Context) :
    AccountsPermissions {

    override fun canQueryAccounts(): Boolean =
        applicationContext.isPermissionGrantedFor(GET_ACCOUNTS_PERMISSION)
                && applicationContext.isPermissionGrantedFor(READ_PERMISSION)

    override fun canQueryRawContacts(): Boolean =
        applicationContext.isPermissionGrantedFor(READ_PERMISSION)

    override fun canUpdateLocalRawContactsAccount(): Boolean =
        applicationContext.isPermissionGrantedFor(GET_ACCOUNTS_PERMISSION)
                && applicationContext.isPermissionGrantedFor(WRITE_PERMISSION)
}
