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
        val groups = groups(contacts)
        val list = mutableListOf<GroupOrContact>()
        list.addAll(contacts)
        list.addAll(groups)
        list.shuffle()
        return list
    }

    override suspend fun groups(contacts: List<ContactData>): List<GroupData> {
        val f = Faker()
        val r = Random(System.nanoTime())
        var i = 0
        return MutableList(50){
            val members = contacts.takeSome(2,5).toList()
            val willGet = Rand.nextFloat(0f,10000f, reseed = true, biased = 0f)
            val willPay = Rand.nextFloat(0f,10000f, reseed = true, biased = 0f)
            val name = f.animal.name()
            GroupData(
                id = newId,
                name = name,
                image = "https://randomuser.me/api/portraits/lego/${(++i)%10}.jpg",
                members = members,
                lastActivity = randomDate(1643049000000L,1664099455386L),
                willGet = willGet,
                willPay = willPay,
            )
        }
    }

    override suspend fun peoples(count: Int): List<ContactData> {
        val f = Faker()
        val r = Random(System.nanoTime())
        var i = 0
        return MutableList(count){
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

fun <T>Collection<T>.takeSome(min: Int, max: Int): Collection<T>{
    val count = (Random(System.nanoTime()).nextFloat()*(max-min)).toInt()+min
    val indices = (0 until this.size).toList().shuffled()
    return indices.take(count).map {
        this.elementAt(it)
    }
}