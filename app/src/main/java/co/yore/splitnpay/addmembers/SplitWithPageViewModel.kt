package co.yore.splitnpay.addmembers

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.yore.splitnpay.*
import io.github.serpro69.kfaker.Faker
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
                val query = (arg as? String)?:return@NotificationService
                splitWithInput.value = query
                initiateSearch(query)
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
            "${DataIds.checkItem}contact"->{
                val item = (arg as? ContactData)?:return@NotificationService
                val index = contacts.indexOf(item)
                val finalChecked = !item.selected
                if(finalChecked){
                    addedContacts.add(item)
                }
                else{
                    addedContacts.remove{
                        it.id==item.id
                    }
                }
                contacts[index] = item.copy(selected = !item.selected)
                /////////////////
                val recentIndex = recents.indexOfFirst {
                    it.id()==item.id
                }
                if(recentIndex<0){
                    return@NotificationService
                }
                recents[recentIndex] = item.copy(selected = !item.selected)
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

    private var searchJob: kotlinx.coroutines.Job? = null
    private fun initiateSearch(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500)
            reallySearch(query)
        }
    }

    private var _recentVault = mutableListOf<GroupOrContact>()
    private var _contactVault = mutableListOf<ContactData>()
    private var _groupVault = mutableListOf<GroupData>()
    private fun reallySearch(query: String) {
        if(_recentVault.isEmpty()){
            _recentVault.addAll(recents)
        }
        else if(query.isEmpty()){
            recents.clear()
            recents.addAll(_recentVault)
            _recentVault.clear()
        }
        if(_contactVault.isEmpty()){
            _contactVault.addAll(contacts)
        }
        if(_groupVault.isEmpty()){
            _groupVault.addAll(groups)
        }
        recents.clear()
        recents.addAll(
            _recentVault.filter {
                when (it) {
                    is GroupData -> {
                        search(query,it.name)
                    }
                    is ContactData -> {
                        search(query,it.name,it.mobile)
                    }
                    else -> {
                        false
                    }
                }
            }
        )
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