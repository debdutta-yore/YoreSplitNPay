package co.yore.splitnpay.addmembers

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.yore.splitnpay.*
import io.github.serpro69.kfaker.Faker
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

data class ObservableMutableStateList<T>(
    val list: SnapshotStateList<T>
)

val <T>SnapshotStateList<T>.observable
get() = ObservableMutableStateList(this)

class SplitWithPageViewModel: ViewModel() {
    private val f = Faker()
    private val r = Random(0)
    val resolver = Resolver()
    private val selectedContacts = mutableStateListOf<Any>()
    private val groupsAndContacts = mutableStateListOf<GroupOrContact>()
    private val visibleGroupsAndContacts = mutableStateListOf<GroupOrContact>()
    private val selectedIndex = mutableStateOf(0)
    private val splitWithInput = mutableStateOf("")
    private val addedContacts = mutableStateListOf<ContactData>().animated
    private val _notificationService = NotificationService{id,arg->
        when(id){
            DataIds.textInput->{
                val query = (arg as? String)?:return@NotificationService
                splitWithInput.value = query
                initiateSearch()
            }
            DataIds.checkItem->{
                if(arg==null){
                    return@NotificationService
                }
                if(selectedContacts.contains(arg)){
                    selectedContacts.remove(arg)
                }
                else{
                    selectedContacts.add(arg)
                }
            }
            DataIds.deleteAdded->{

            }
            DataIds.selectedTabIndex->{
                if(arg is Int){
                    selectedIndex.value = arg
                    filter()
                }
            }
        }
    }

    private var searchJob: kotlinx.coroutines.Job? = null
    private fun initiateSearch() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500)
            filter()
        }
    }

    val notifier = _notificationService
    init {
        selectedContacts.onEach {
            Log.d("ffsfsf","$it")
        }
        resolver[DataIds.textInput] = splitWithInput
        resolver[DataIds.groupOrContact] = visibleGroupsAndContacts
        resolver[DataIds.addedContacts] = addedContacts
        resolver[DataIds.selectedTabIndex] = selectedIndex
        resolver[DataIds.selecteds] = selectedContacts
        var i = 0
        groupsAndContacts.addAll(MutableList(20){
            ContactData(
                id = ++i,
                name = f.name.name(),
                mobile = f.phoneNumber.cellPhone(),
                image = "https://randomuser.me/api/portraits/men/${i-1}.jpg",
                lastActivity = randomDate(1643049000000L,1664099455386L)
            )
        })
        groupsAndContacts.addAll(MutableList<GroupData>(10){
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
        })
        groupsAndContacts.shuffle()
        filter()
    }

    fun filter(){
        visibleGroupsAndContacts.clear()
        visibleGroupsAndContacts.addAll(groupsAndContacts.filter {
            itemFilter(it)
        })
    }

    private fun itemFilter(it: GroupOrContact): Boolean {
        val now = System.currentTimeMillis()
        return when (selectedIndex.value) {
            1 -> {
                (it is GroupData) && search(splitWithInput.value,it.searchables())
            }
            2 -> {
                (it is ContactData) && search(splitWithInput.value,it.searchables())
            }
            else -> {
                val dif = (now - it.lastActivity())/(24*3600000)
                dif <= 30 && search(splitWithInput.value,it.searchables())
            }
        }
    }
}