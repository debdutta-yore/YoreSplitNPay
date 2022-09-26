package co.yore.splitnpay.repo

import co.yore.splitnpay.models.ContactData
import co.yore.splitnpay.models.GroupData
import co.yore.splitnpay.models.GroupOrContact

interface Repo {
    suspend fun groupAndContacts(): List<GroupOrContact>
    suspend fun groups(): List<GroupData>
    suspend fun peoples(): List<ContactData>
}