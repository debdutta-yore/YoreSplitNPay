package co.yore.splitnpay.repo

import co.yore.splitnpay.addmembers.GroupOrContact

interface Repo {
    suspend fun groupAndContacts(): List<GroupOrContact>
}