package co.yore.splitnpay.viewModels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.yore.splitnpay.components.components.animated
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.models.ContactData
import co.yore.splitnpay.models.DataIds
import co.yore.splitnpay.models.GroupData
import co.yore.splitnpay.models.GroupOrContact
import co.yore.splitnpay.repo.Repo
import co.yore.splitnpay.repo.RepoImpl
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class ObservableMutableStateList<T>(
    val list: SnapshotStateList<T>
)

val <T>SnapshotStateList<T>.observable
get() = ObservableMutableStateList(this)

class MemberSelectionPageViewModel(
    private val repo: Repo = RepoImpl()
): ViewModel(), WirelessViewModelInterface {
    override val navigation = mutableStateOf<UIScope?>(null)
    override val resolver = Resolver()
    private val selectedContactIds = mutableStateListOf<Any>()
    private val groupsAndContacts = mutableStateListOf<GroupOrContact>()
    private val visibleGroupsAndContacts = mutableStateListOf<GroupOrContact>()
    private val selectedIndex = mutableStateOf(0)
    private val splitWithInput = mutableStateOf("")
    private val proceedWithContacts = mutableStateOf(false)
    private val addedContacts = mutableStateListOf<ContactData>().animated
    private val _statusBarColor = mutableStateOf<StatusBarColor?>(null)
    private val _notificationService = NotificationService{id,arg->
        when(id){
            WirelessViewModelInterface.startupNotification->{
                _statusBarColor.value = StatusBarColor(Color(0xffEDF3F9),true)
            }
            DataIds.back->{
                navigation.scope { navHostController, lifecycleOwner, toaster ->
                    navHostController.popBackStack()
                }
            }
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
            DataIds.checkGroupItem->{
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

    override val notifier = _notificationService
    init {
        resolver[DataIds.textInput] = splitWithInput
        resolver[DataIds.groupsAndPeoples] = visibleGroupsAndContacts
        resolver[DataIds.addedContacts] = addedContacts
        resolver[DataIds.selectedTabIndex] = selectedIndex
        resolver[DataIds.selecteds] = selectedContactIds
        resolver[DataIds.proceedWithContacts] = proceedWithContacts
        resolver[DataIds.statusBarColor] = _statusBarColor
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