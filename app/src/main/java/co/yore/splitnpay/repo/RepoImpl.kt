package co.yore.splitnpay.repo

import co.yore.splitnpay.addmembers.ContactData
import co.yore.splitnpay.addmembers.GroupData
import co.yore.splitnpay.addmembers.GroupOrContact
import co.yore.splitnpay.randomDate
import io.github.serpro69.kfaker.Faker
import kotlin.random.Random

class RepoImpl:Repo {
    override suspend fun groupAndContacts(): List<GroupOrContact> {
        val f = Faker()
        val r = Random(0)
        var i = 0
        val contacts = MutableList(20){
            ContactData(
                id = ++i,
                name = f.name.name(),
                mobile = f.phoneNumber.cellPhone(),
                image = "https://randomuser.me/api/portraits/men/${i-1}.jpg",
                lastActivity = randomDate(1643049000000L,1664099455386L)
            )
        }
        val groups = MutableList<GroupData>(10){
            val images = MutableList(r.nextInt(1,7)){
                "https://randomuser.me/api/portraits/men/${i+it}.jpg"
            }
            GroupData(
                id = ++i,
                name = f.animal.name(),
                image = "https://randomuser.me/api/portraits/lego/${i%10}.jpg",
                memberImages = images,
                lastActivity = randomDate(1643049000000L,1664099455386L)
            )
        }
        val list = mutableListOf<GroupOrContact>()
        list.addAll(contacts)
        list.addAll(groups)
        list.shuffle()
        return list
    }
}