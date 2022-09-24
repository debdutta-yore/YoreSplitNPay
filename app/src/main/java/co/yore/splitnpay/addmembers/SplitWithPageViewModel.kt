package co.yore.splitnpay.addmembers

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import co.yore.splitnpay.DataIds
import co.yore.splitnpay.NotificationService
import co.yore.splitnpay.Resolver
import co.yore.splitnpay.animated
import io.github.serpro69.kfaker.Faker
import kotlin.random.Random


class SplitWithPageViewModel: ViewModel() {
    private val f = Faker()
    private val r = Random(0)
    val resolver = Resolver()
    private val groups = mutableStateListOf<GroupData>()
    private val contacts = mutableStateListOf<ContactData>()
    private val recents = mutableStateListOf<GroupOrContact>()
    private val selectedIndex = mutableStateOf(0)
    private val splitWithInput = mutableStateOf("")
    private val addedContacts = mutableStateListOf<ContactData>().animated
    private val _notificationService = NotificationService{id,arg->
        when(id){
            DataIds.textInput->{
                splitWithInput.value = (arg as? String)?:return@NotificationService
            }
            "${DataIds.checkItem}recent"->{
                val item = (arg as? GroupOrContact)?:return@NotificationService
                if(item is ContactData){
                    val index = recents.indexOf(item)
                    val finalChecked = !item.selected
                    if(finalChecked){
                        addedContacts.add(item)
                    }
                    else{
                        addedContacts.remove{
                            it.id==item.id
                        }
                    }
                    recents[index] = item.copy(selected = !item.selected)
                    /////////////////
                    val contactIndex = contacts.indexOfFirst {
                        it.id()==item.id
                    }
                    contacts[contactIndex] = item.copy(selected = !item.selected)
                }
            }
            DataIds.deleteAdded->{
                val item = (arg as? ContactData)?:return@NotificationService
                val index = addedContacts.indexOf{
                    it.id==item.id
                }
                if(index<0){
                    return@NotificationService
                }
                addedContacts.removeAt(index)
                ////////////
                val recentIndex = recents.indexOfFirst {
                    it.id()==item.id
                }
                recents[recentIndex] = item.copy(selected = false)
                ////////////
                val contactIndex = contacts.indexOfFirst {
                    it.id()==item.id
                }
                contacts[contactIndex] = item.copy(selected = false)
            }
            DataIds.selectedTabIndex->{
                if(arg is Int){
                    selectedIndex.value = arg
                }
            }
        }
    }
    val notifier = _notificationService
    init {
        resolver[DataIds.textInput] = splitWithInput
        resolver[DataIds.groupOrContact] = recents
        resolver[DataIds.addedContacts] = addedContacts
        resolver[DataIds.selectedTabIndex] = selectedIndex
        resolver[DataIds.groups] = groups
        resolver[DataIds.contacts] = contacts
        var i = 0
        contacts.addAll(MutableList(20){
            ContactData(
                id = ++i,
                name = f.name.name(),
                mobile = f.phoneNumber.cellPhone(),
                image = "https://randomuser.me/api/portraits/men/${i-1}.jpg",
            )
        })
        groups.addAll(MutableList<GroupData>(10){
            val images = MutableList(r.nextInt(1,7)){
                "https://randomuser.me/api/portraits/men/${i+it}.jpg"
            }
            GroupData(
                id = ++i,
                name = f.animal.name(),
                image = "https://randomuser.me/api/portraits/lego/${i%10}.jpg",
                memberImages = images
            )
        })
        val _recents = mutableListOf<GroupOrContact>()
        _recents.addAll(groups.take(5))
        _recents.addAll(contacts.take(5))
        _recents.shuffle()
        recents.addAll(_recents)
    }
}