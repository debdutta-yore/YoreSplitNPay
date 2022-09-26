package co.yore.splitnpay.addmembers

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.yore.splitnpay.*
import co.yore.splitnpay.repo.Repo
import co.yore.splitnpay.repo.RepoImpl
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class ObservableMutableStateList<T>(
    val list: SnapshotStateList<T>
)

val <T>SnapshotStateList<T>.observable
get() = ObservableMutableStateList(this)

class SplitWithPageViewModel(
    private val repo: Repo = RepoImpl()
): ViewModel() {
    val resolver = Resolver()
    private val selectedContactIds = mutableStateListOf<Any>()
    private val groupsAndContacts = mutableStateListOf<GroupOrContact>()
    private val visibleGroupsAndContacts = mutableStateListOf<GroupOrContact>()
    private val selectedIndex = mutableStateOf(0)
    private val splitWithInput = mutableStateOf("")
    private val proceedWithContacts = mutableStateOf(false)
    private val addedContacts = mutableStateListOf<ContactData>().animated
    private val _notificationService = NotificationService{id,arg->
        when(id){
            DataIds.textInput->{
                val query = (arg as? String)?:return@NotificationService
                splitWithInput.value = query
                initiateSearch()
            }
            DataIds.deleteAdded->{
                if(arg==null){
                    return@NotificationService
                }
                if(selectedContactIds.contains(arg)){
                    selectedContactIds.remove(arg)
                    addedContacts.remove {
                        it.id==arg
                    }
                }
                proceedWithContacts.value = selectedContactIds.isNotEmpty()
            }
            DataIds.checkItem->{
                if(arg==null){
                    return@NotificationService
                }
                if(selectedContactIds.contains(arg)){
                    selectedContactIds.remove(arg)
                    addedContacts.remove {
                        it.id==arg
                    }
                }
                else{
                    selectedContactIds.add(arg)
                    addedContacts.add(groupsAndContacts.first {
                        (it is ContactData && it.id()==arg)
                    } as ContactData)
                }
                proceedWithContacts.value = selectedContactIds.isNotEmpty()
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
        resolver[DataIds.textInput] = splitWithInput
        resolver[DataIds.groupOrContact] = visibleGroupsAndContacts
        resolver[DataIds.addedContacts] = addedContacts
        resolver[DataIds.selectedTabIndex] = selectedIndex
        resolver[DataIds.selecteds] = selectedContactIds
        resolver[DataIds.proceedWithContacts] = proceedWithContacts
        fetchGroupAndContacts()
        filter()
    }

    private fun fetchGroupAndContacts() {
        viewModelScope.launch {
            groupsAndContacts.addAll(repo.groupAndContacts())
        }
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