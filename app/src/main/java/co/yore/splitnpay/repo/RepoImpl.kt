package co.yore.splitnpay.repo

import co.yore.splitnpay.libs.Rand
import co.yore.splitnpay.libs.randomDate
import co.yore.splitnpay.models.ContactData
import co.yore.splitnpay.models.GroupData
import co.yore.splitnpay.models.GroupOrContact
import io.github.serpro69.kfaker.Faker
import java.util.*
import kotlin.random.Random

class RepoImpl:Repo {
    override suspend fun groupAndContacts(): List<GroupOrContact> {
        val contacts = peoples()
        val groups = groups()
        val list = mutableListOf<GroupOrContact>()
        list.addAll(contacts)
        list.addAll(groups)
        list.shuffle()
        return list
    }

    override suspend fun groups(): List<GroupData> {
        val f = Faker()
        val r = Random(0)
        var i = 0
        return MutableList(10){
            val images = MutableList(r.nextInt(1,7)){
                "https://randomuser.me/api/portraits/men/${i+it}.jpg"
            }
            GroupData(
                id = newId,
                name = f.animal.name(),
                image = "https://randomuser.me/api/portraits/lego/${(++i)%10}.jpg",
                memberImages = images,
                lastActivity = randomDate(1643049000000L,1664099455386L),
                willGet = Rand.nextFloat(0f,10000f, reseed = true, biased = 0f),
                willPay = Rand.nextFloat(0f,1000f, reseed = true, biased = 0f),
            )
        }
    }

    override suspend fun peoples(): List<ContactData> {
        val f = Faker()
        val r = Random(System.nanoTime())
        var i = 0
        return MutableList(20){
            ContactData(
                id = newId,
                name = "fdfdfdfdfd fdfdf12345678901234",//f.name.name(),
                mobile = f.phoneNumber.cellPhone(),
                image = "https://randomuser.me/api/portraits/men/${(++i)-1}.jpg",
                lastActivity = randomDate(1643049000000L,1664099455386L),
                willGet = Rand.nextFloat(0f,10000f, reseed = true, biased = 0f),
                willPay = Rand.nextFloat(0f,1000f, reseed = true, biased = 0f),
            )
        }
    }

    private val newId get() = UUID.randomUUID().toString()
}