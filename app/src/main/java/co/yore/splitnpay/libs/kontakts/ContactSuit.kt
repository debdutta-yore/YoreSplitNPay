package co.yore.splitnpay.libs.kontakts

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.provider.ContactsContract
import androidx.room.*

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

data class Contact(
        val id: String,
        val name: String,
        val phones: List<Phone>,
        val emails: List<Email>,
        val image: String
    ){
    val toContactEntity get() = ContactEntity(
        id = id,
        phone = phones.joinToString(","),
        name = name,
        email = emails.joinToString(","),
        image = image
    )

    data class Phone(
        val phone: String,
        val label: String
    ){
        override fun toString(): String {
            return "$label:$phone"
        }
    }
    data class Email(
        val email: String,
        val label: String
    ){
        override fun toString(): String {
            return "$label:$email"
        }
    }

    val toSingles get(): List<MergedContact>{
        val email = emails.sortedBy { it.email }.joinToString(",")
        val phss = mutableListOf<Phone>()
        phones.forEach {
            val r = phone(it.phone)
            if(r!=null){
                phss.add(it.copy(phone = r))
            }
        }
        val labelNeeded = phones.count() > 1
        return phss.map {
            MergedContact(
                name = if(labelNeeded) "$name (${it.label})" else name,
                phone = it.phone,
                email = email,
                image = image
            )
        }
    }

    companion object{
        fun fromEntity(entity: ContactEntity): Contact{
            return Contact(
                id = entity.id,
                name = entity.name,
                phones = entity
                    .phone
                    .split(",")
                    .map {
                        val parts = it.split(":")
                        if(parts.size!=2){
                            return@map Contact.Phone("","")
                        }
                        Phone(phone = parts[1], label = parts[0])
                    },
                emails = entity
                    .email
                    .split(",")
                    .map {
                        if(it.isEmpty()){
                            return@map Email("","")
                        }
                        val parts = it.split(":")
                        if(parts.size!=2){
                            return@map Contact.Email("","")
                        }
                        Email(email = parts[1], label = parts[0])
                    },
                image = entity.image
            )
        }
    }
}

suspend fun suitNamePhoneEmailImage(context: Context): List<MergedContact>{
    var contactsFromDevice = mutableListOf<Contact>()
    val currentTimestamp = System.currentTimeMillis()
    val prevTimestamp = getContactFetchedTimestamp(context)

    val db = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java, "contact-database"
    )
        .build()

    if(prevTimestamp == 0L){
        contactsFromDevice.addAll(getAllContacts(context))
        db.contactEntityDao().insertAll(contactsFromDevice.map { it.toContactEntity })
        setContactFetchedTimestamp(context, currentTimestamp)
    }
    else{
        val updatedContacts = getUpdates(context, prevTimestamp)
        db.contactEntityDao().updateAll(updatedContacts.map { it.toContactEntity })

        val deleteds = deletedContacts(context, prevTimestamp)

        db.contactEntityDao().deleteByIds(deleteds)

        contactsFromDevice.addAll(db.contactEntityDao().getAll().map { Contact.fromEntity(it) })
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
        .where(ContactsContract.DeletedContacts.CONTACT_DELETED_TIMESTAMP gte timestamp)
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

@SuppressLint("Range")
private fun getAllContacts(context: Context): List<Contact> {
    val list = mutableListOf<Contact>()
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
                .where(ContactsContract.CommonDataKinds.Email.CONTACT_ID equals id)
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
                .where(ContactsContract.CommonDataKinds.Phone.CONTACT_ID equals id)
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

@SuppressLint("Range")
private fun getUpdates(
    context: Context,
    timestamp: Long
): List<Contact>{
    val list = mutableListOf<Contact>()
    Kontakts()
        .from(Kontakts.Contacts)
        .where(ContactsContract.Contacts.CONTACT_LAST_UPDATED_TIMESTAMP gte timestamp)
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
                .where(ContactsContract.CommonDataKinds.Email.CONTACT_ID equals id)
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
                .where(ContactsContract.CommonDataKinds.Phone.CONTACT_ID equals id)
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

data class MergedContact(
    val name: String,
    val phone: String,
    val email: String,
    val image: String
){

    companion object{
        fun fromEntity(entity: ContactEntity): MergedContact{
            return MergedContact(
                entity.name,entity.phone,entity.email,entity.image
            )
        }
    }
}