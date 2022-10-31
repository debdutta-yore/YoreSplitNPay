package co.yore.splitnpay.libs.contact.core.entities.operation

import android.content.ContentProviderOperation
import android.content.ContentProviderOperation.newUpdate
import co.yore.splitnpay.libs.contact.core.ContactsFields
import co.yore.splitnpay.libs.contact.core.Fields
import co.yore.splitnpay.libs.contact.core.RawContactsFields
import co.yore.splitnpay.libs.contact.core.entities.OptionsEntity
import co.yore.splitnpay.libs.contact.core.entities.table.ProfileUris
import co.yore.splitnpay.libs.contact.core.entities.table.Table
import co.yore.splitnpay.libs.contact.core.equalTo
import co.yore.splitnpay.libs.contact.core.util.isProfileId

/*
 * Note that changes to the options of a RawContact may affect the options of the parent Contact.
 * On the other hand, changes to the options of the parent Contact will be propagated to all child
 * RawContact options.
 */
internal class OptionsOperation {

    fun updateContactOptions(contactId: Long, options: OptionsEntity): ContentProviderOperation =
        newUpdate(if (contactId.isProfileId) ProfileUris.CONTACTS.uri else Table.Contacts.uri)
            .withSelection(ContactsFields.Id equalTo contactId)
            .withOptions(options)
            .build()

    fun updateRawContactOptions(
        rawContactId: Long, options: OptionsEntity
    ): ContentProviderOperation = newUpdate(
        if (rawContactId.isProfileId) ProfileUris.RAW_CONTACTS.uri else Table.RawContacts.uri
    )
        .withSelection(RawContactsFields.Id equalTo rawContactId)
        .withOptions(options)
        .build()
}

private fun ContentProviderOperation.Builder.withOptions(options: OptionsEntity)
        : ContentProviderOperation.Builder =
    withValue(Fields.Contact.Options.Starred, options.starred.toSqlValue())
        .withValue(Fields.Contact.Options.CustomRingtone, options.customRingtone.toString())
        .withValue(Fields.Contact.Options.SendToVoicemail, options.sendToVoicemail.toSqlValue())
        /* Deprecated in API 29 - contains useless value for all Android versions in Play store.
        .withValue(Fields.Contact.Options.TimesContacted, options.timesContacted ?: 0)
        .withValue(Fields.Contact.Options.LastTimeContacted, options.lastTimeContacted?.time ?: 0)
         */

private fun Boolean?.toSqlValue(): Int {
    if (this != null) {
        return if (this) 1 else 0
    }
    return 0
}