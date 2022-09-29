package co.yore.splitnpay.repo

import co.yore.splitnpay.models.ContactData
import co.yore.splitnpay.models.GroupData
import co.yore.splitnpay.models.GroupOrContact

interface Repo {
    suspend fun groupAndContacts(): List<GroupOrContact>
    suspend fun groups(contacts: List<ContactData>): List<GroupData>
    suspend fun peoples(count: Int = 20): List<ContactData>
}