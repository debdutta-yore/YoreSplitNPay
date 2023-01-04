package co.yore.splitnpay.libs.kontakts.models

import androidx.room.*
import co.yore.splitnpay.libs.kontakts.database.ContactEntity
import co.yore.splitnpay.libs.kontakts.phone

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