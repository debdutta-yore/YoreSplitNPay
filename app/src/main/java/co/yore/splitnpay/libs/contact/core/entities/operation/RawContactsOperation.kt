package co.yore.splitnpay.libs.contact.core.entities.operation

import android.accounts.Account
import android.content.ContentProviderOperation
import android.content.ContentProviderOperation.newDelete
import android.content.ContentProviderOperation.newInsert
import android.net.Uri
import co.yore.splitnpay.libs.contact.core.RawContactsField
import co.yore.splitnpay.libs.contact.core.RawContactsFields
import co.yore.splitnpay.libs.contact.core.Where
import co.yore.splitnpay.libs.contact.core.entities.table.ProfileUris
import co.yore.splitnpay.libs.contact.core.entities.table.Table

/**
 * Builds [ContentProviderOperation]s for [Table.RawContacts] and [ProfileUris.RAW_CONTACTS].
 */
internal class RawContactsOperation(private val isProfile: Boolean) {

    private val contentUri: Uri
        get() = if (isProfile) ProfileUris.RAW_CONTACTS.uri else Table.RawContacts.uri

    fun insert(rawContactAccount: Account?): ContentProviderOperation = newInsert(contentUri)
        /*
         * Passing in null account name and type is valid. It is the same behavior as the native
         * Android Contacts app when creating contacts when there are no available accounts. When an
         * account becomes available (or is already available), Android will automatically update
         * the RawContact name and type to an existing Account.
         */
        .withValue(RawContactsFields.AccountName, rawContactAccount?.name)
        .withValue(RawContactsFields.AccountType, rawContactAccount?.type)
        .build()

    fun deleteRawContactsWhere(where: Where<RawContactsField>): ContentProviderOperation =
        newDelete(contentUri).withSelection(where).build()
}