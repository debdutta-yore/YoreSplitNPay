package co.yore.splitnpay.viewModels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.models.ContactData
import co.yore.splitnpay.models.DataIds
import co.yore.splitnpay.models.GroupData
import co.yore.splitnpay.models.GroupOrContact
import co.yore.splitnpay.repo.Repo
import co.yore.splitnpay.repo.RepoImpl
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class TriState{
    CHECKED,
    UNCHECKED,
    INTERMEDIATE
}

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
    private val addedContacts = mutableStateListOf<ContactData>()/*.animated{
        it.id
    }*/
    private val _statusBarColor = mutableStateOf<StatusBarColor?>(null)
    private val _groupsChecked = mutableStateMapOf<Any,TriState>()
    private val _notificationService = NotificationService{id,arg->
        when(id){
            DataIds.proceedWithContacts->{
                gotoGroupCreationPage()
            }
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
                    removeMembersFromSelecteds(setOf(arg))
                }
                proceedWithContacts.value = selectedContactIds.isNotEmpty()
            }
            DataIds.checkItem->{
                if(arg==null){
                    return@NotificationService
                }
                if(selectedContactIds.contains(arg)){
                    removeMembersFromSelecteds(setOf(arg))
                }
                else{
                    addMembersToSelecteds(setOf(arg))
                }
                proceedWithContacts.value = selectedContactIds.isNotEmpty()
            }
            DataIds.checkGroupItem->{
                val groupId = arg?:return@NotificationService
                val checked = _groupsChecked[groupId]?:TriState.UNCHECKED
                val group = (groupsAndContacts.first { it.id()==groupId } as? GroupData)?:return@NotificationService
                val memberIds = group.members.map { it.id() }.toSet()
                if(checked!=TriState.UNCHECKED){
                    removeMembersFromSelecteds(memberIds)
                }
                else{
                    addMembersToSelecteds(memberIds)
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

    private fun gotoGroupCreationPage() {
        navigation.scope { navHostController, lifecycleOwner, toaster ->
            navHostController.navigate("group_creation")
        }
    }

    private fun addMembersToSelecteds(memberIds: Set<Any>) {
        val finalIds = selectedContactIds.union(memberIds)
        val states = mutableMapOf<Any,TriState>()
        val membersToAdd = mutableListOf<ContactData>()
        val alreadyAdded = addedContacts.map{
            it.id()
        }
        groupsAndContacts.forEach {
            if(it is GroupData){
                val ids = it.members.map { it.id }.toSet()
                val commonCount = finalIds.intersect(ids).size
                if(commonCount!=0){
                    if(commonCount==it.members.size){
                        states[it.id] = TriState.CHECKED
                    }
                    else if(commonCount<it.members.size){
                        states[it.id] = TriState.INTERMEDIATE
                    }
                }
                else{
                    states[it.id] = TriState.UNCHECKED
                }
            }
            else{
                if(memberIds.contains(it.id())&&!alreadyAdded.contains(it.id())){
                    membersToAdd.add(it as ContactData)
                }
            }
        }
        _groupsChecked.putAll(states)
        selectedContactIds.addAll(membersToAdd.map { it.id })
        addedContacts.addAll(membersToAdd)
    }

    private fun removeMembersFromSelecteds(memberIds: Set<Any?>) {
        val finalIds = selectedContactIds.minus(memberIds)
        if(finalIds.isEmpty()){
            _groupsChecked.clear()
        }
        else{
            val states = mutableMapOf<Any,TriState>()
            groupsAndContacts.forEach {
                if(it is GroupData){
                    val ids = it.members.map { it.id }.toSet()
                    val commonCount = finalIds.intersect(ids).size
                    if(commonCount!=0){
                        if(commonCount==it.members.size){
                            states[it.id] = TriState.CHECKED
                        }
                        else if(commonCount<it.members.size){
                            states[it.id] = TriState.INTERMEDIATE
                        }
                    }
                    else{
                        states[it.id] = TriState.UNCHECKED
                    }
                }
            }
            _groupsChecked.putAll(states)
        }
        selectedContactIds.removeAll(memberIds)
        memberIds.forEach { m->
            val index = addedContacts.indexOfFirst {
                it.id == m
            }
            if(index>-1){
                addedContacts.removeAt(index)
            }
        }
    }

    data class GroupIdState(
        val id: Any,
        val state: TriState
    )

    private var searchJob: kotlinx.coroutines.Job? = null
    private fun initiateSearch() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500)
            filter()
        }
    }

    //1. Any possibility of changing details of split like amount, member(add, delete) etc? If yes then system will recalculate the settlement status?
    //2. If unsettled member can be deleted then system will recalculate the settlement status?


    override val notifier = _notificationService
    init {
        resolver[DataIds.textInput] = splitWithInput
        resolver[DataIds.groupsAndPeoples] = visibleGroupsAndContacts
        resolver[DataIds.addedContacts] = addedContacts
        resolver[DataIds.selectedTabIndex] = selectedIndex
        resolver[DataIds.selecteds] = selectedContactIds
        resolver[DataIds.proceedWithContacts] = proceedWithContacts
        resolver[DataIds.statusBarColor] = _statusBarColor
        resolver[DataIds.groupsChecked] = _groupsChecked
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