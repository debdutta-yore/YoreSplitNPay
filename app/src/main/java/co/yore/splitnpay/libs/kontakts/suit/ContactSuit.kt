package co.yore.splitnpay.libs.kontakts.suit

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.provider.ContactsContract
import android.util.Log
import androidx.room.*
import co.yore.splitnpay.libs.kontakts.core.Equals
import co.yore.splitnpay.libs.kontakts.core.GreaterThanOrEqual
import co.yore.splitnpay.libs.kontakts.core.Kontakts
import co.yore.splitnpay.libs.kontakts.core.forEach
import co.yore.splitnpay.libs.kontakts.database.ContactDatabase
import co.yore.splitnpay.libs.kontakts.datastore.getContactFetchedTimestamp
import co.yore.splitnpay.libs.kontakts.datastore.getContactSkippedCount
import co.yore.splitnpay.libs.kontakts.datastore.setContactFetchedTimestamp
import co.yore.splitnpay.libs.kontakts.datastore.setContactSkippedCount
import co.yore.splitnpay.libs.kontakts.models.Contact
import co.yore.splitnpay.libs.kontakts.models.MergedContact

val Pair<String,List<MergedContact>>.toMergedContact get(): MergedContact {
    return MergedContact(
        name = second
            .map {
                it.name
            }
            .distinct()
            .joinToString("/"),
        phone = first,
        email = second
            .map {
                it.email
            }
            .joinToString(",")
            .split(",")
            .distinct()
            .joinToString(","),
        image = second
            .map {
                it.image
            }
            .filter {
                it != null
            }
            .firstOrNull()?:""
    )
}

val List<Contact>.toSingles get(): List<MergedContact>{
    val list = mutableListOf<MergedContact>()
    forEach {
        list.addAll(it.toSingles)
    }
    val groups = list.groupBy { it.phone }
    val r = groups.map {
        it.toPair().toMergedContact
    }
    return r
}

suspend fun suitNamePhoneEmailImage(context: Context): List<MergedContact>{
    var contactsFromDevice = mutableListOf<Contact>()
    val currentTimestamp = System.currentTimeMillis()
    val prevTimestamp = getContactFetchedTimestamp(context)

    val db = Room.databaseBuilder(
        context.applicationContext,
        ContactDatabase::class.java, "contact-database"
    )
        .build()

    if(prevTimestamp == 0L){
        val allContacts = getAllContacts(context)
        setContactSkippedCount(context, allContacts.first)
        contactsFromDevice.addAll(allContacts.second)
        db.contactEntityDao().insertAll(contactsFromDevice.map { it.toContactEntity })
        setContactFetchedTimestamp(context, currentTimestamp)
    }
    else{
        val updatedContacts = getUpdates(context, prevTimestamp)
        db.contactEntityDao().updateAll(updatedContacts.map { it.toContactEntity })

        val deleteds = deletedContacts(context, prevTimestamp)

        db.contactEntityDao().deleteByIds(deleteds)

        contactsFromDevice.addAll(db.contactEntityDao().getAll().map { Contact.fromEntity(it) })
        val got = contactsFromDevice.size
        val skipped = getContactSkippedCount(context)
        val real = getAllContactsCount(context)
        if(got + skipped != real){
            contactsFromDevice.clear()
            val allContacts = getAllContacts(context)
            setContactSkippedCount(context, allContacts.first)
            contactsFromDevice.addAll(allContacts.second)
            db.clearAllTables()
            db.contactEntityDao().insertAll(contactsFromDevice.map { it.toContactEntity })
        }
        setContactFetchedTimestamp(context, currentTimestamp)
    }

    return contactsFromDevice.toSingles
}

@SuppressLint("Range")
private fun deletedContacts(
    context: Context,
    timestamp: Long
): List<String>{
    val list = mutableListOf<String>()
    Kontakts()
        .from(Kontakts.Deleted)
        .where(ContactsContract.DeletedContacts.CONTACT_DELETED_TIMESTAMP GreaterThanOrEqual timestamp)
        .build(context.contentResolver)
        .forEach {
            val id = getString(
                getColumnIndex(ContactsContract.DeletedContacts.CONTACT_ID)
            )?:""
            if(id.isNotEmpty()){
                list.add(id)
            }
        }
    return list
}

private fun getAllContactsCount(context: Context): Int {
    val start = System.currentTimeMillis()
    val r = Kontakts()
        .from(Kontakts.Contacts)
        .build(context.contentResolver)
        ?.count?:0
    val end = System.currentTimeMillis()
    Log.d("fldkfldf","${end-start}")
    return r
}

@SuppressLint("Range")
private fun getAllContacts(context: Context): Pair<Int,List<Contact>> {
    val list = mutableListOf<Contact>()
    var skipped = 0
    Kontakts()
        .from(Kontakts.Contacts)
        .sortBy(ContactsContract.Contacts.DISPLAY_NAME)
        .build(context.contentResolver)
        .forEach {
            val id = getString(
                getColumnIndex(ContactsContract.Contacts._ID)
            )
            val name = getString(
                getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
            )
            val image = getString(
                getColumnIndex(ContactsContract.Contacts.PHOTO_URI)
            )?:""
            val emails = mutableListOf<Contact.Email>()
            val phones = mutableListOf<Contact.Phone>()
            Kontakts()
                .from(Kontakts.Email)
                .where(ContactsContract.CommonDataKinds.Email.CONTACT_ID Equals id)
                .build(context.contentResolver)
                .forEach {
                    val email = getString(getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA))
                    val type = getInt(getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE))
                    val label = getString(getColumnIndex(ContactsContract.CommonDataKinds.Email.LABEL))
                    val typeLabel = ContactsContract.CommonDataKinds.Email.getTypeLabel(Resources.getSystem(),type, label)
                    emails.add(
                        Contact.Email(email, typeLabel.toString())
                    )
                }?.close()
            Kontakts()
                .from(Kontakts.Phone)
                .where(ContactsContract.CommonDataKinds.Phone.CONTACT_ID Equals id)
                .build(context.contentResolver)
                .forEach {
                    val phone = getString(getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA))


                    if(phone.isNotEmpty()){
                        val type = getInt(getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE))
                        val label = getString(getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL))
                        val typeLabel = ContactsContract.CommonDataKinds.Phone.getTypeLabel(Resources.getSystem(),type, label)
                        phones.add(
                            Contact.Phone(phone, typeLabel.toString())
                        )
                    }
                }?.close()
            if(phones.isNotEmpty()){
                list.add(
                    Contact(
                        id = id,
                        name = name,
                        phones = phones,
                        emails = emails,
                        image = image
                    )
                )
            }
            else{
                skipped++
            }
        }?.close()

    return Pair(skipped,list)
}

@SuppressLint("Range")
private fun getUpdates(
    context: Context,
    timestamp: Long
): List<Contact>{
    val list = mutableListOf<Contact>()
    Kontakts()
        .from(Kontakts.Contacts)
        .where(ContactsContract.Contacts.CONTACT_LAST_UPDATED_TIMESTAMP GreaterThanOrEqual  timestamp)
        .build(context.contentResolver)
        .forEach {
            val id = getString(
                getColumnIndex(ContactsContract.Contacts._ID)
            )
            val name = getString(
                getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
            )
            val image = getString(
                getColumnIndex(ContactsContract.Contacts.PHOTO_URI)
            )?:""
            val emails = mutableListOf<Contact.Email>()
            val phones = mutableListOf<Contact.Phone>()
            Kontakts()
                .from(Kontakts.Email)
                .where(ContactsContract.CommonDataKinds.Email.CONTACT_ID Equals id)
                .build(context.contentResolver)
                .forEach {
                    val email = getString(getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA))
                    val type = getInt(getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE))
                    val label = getString(getColumnIndex(ContactsContract.CommonDataKinds.Email.LABEL))
                    val typeLabel = ContactsContract.CommonDataKinds.Email.getTypeLabel(Resources.getSystem(),type, label)
                    emails.add(
                        Contact.Email(email, typeLabel.toString())
                    )
                }?.close()
            Kontakts()
                .from(Kontakts.Phone)
                .where(ContactsContract.CommonDataKinds.Phone.CONTACT_ID Equals id)
                .build(context.contentResolver)
                .forEach {
                    val phone = getString(getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA))
                    if(phone.isNotEmpty()){
                        val type = getInt(getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE))
                        val label = getString(getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL))
                        val typeLabel = ContactsContract.CommonDataKinds.Phone.getTypeLabel(Resources.getSystem(),type, label)
                        phones.add(
                            Contact.Phone(phone, typeLabel.toString())
                        )
                    }
                }?.close()
            if(phones.isNotEmpty()){
                list.add(
                    Contact(
                        id = id,
                        name = name,
                        phones = phones,
                        emails = emails,
                        image = image
                    )
                )
            }
        }?.close()

    return list
}

