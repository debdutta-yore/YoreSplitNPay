package co.yore.splitnpay.viewModels

import android.Manifest
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.yore.splitnpay.DataBank
import co.yore.splitnpay.app.Routes
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.libs.jerokit.*
import co.yore.splitnpay.libs.jerokit.bottom_sheet.Sheeting
import co.yore.splitnpay.libs.jerokit.bottom_sheet.Sheets
import co.yore.splitnpay.models.*
import co.yore.splitnpay.pages.subpages.SplitAsChoiceBottomSheetModel
import co.yore.splitnpay.repo.MasterRepo
import co.yore.splitnpay.ui.theme.BlackSqueeze
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class MemberSelectionPageViewModel @Inject constructor(
    private val repo: MasterRepo
) : ViewModel(), WirelessViewModelInterface {
    override val softInputMode = mutableStateOf(SoftInputMode.adjustNothing)
    override val resultingActivityHandler = ResultingActivityHandler()
    override val permissionHandler = PermissionHandler()
    override val navigation = mutableStateOf<UIScope?>(null)
    override val resolver = Resolver()
    private val selectedContactIds = mutableStateListOf<Any>()
    private val groupsAndContacts = mutableStateListOf<GroupOrContact>()
    private val visibleGroupsAndContacts = mutableStateListOf<GroupOrContact>()
    private val selectedIndex = mutableStateOf(0)
    private val splitWithInput = mutableStateOf("")
    private val proceedWithContacts = mutableStateOf(false)
    private val addedContacts = mutableStateListOf<ContactData>()
    private val _statusBarColor = mutableStateOf<StatusBarColor?>(null)
    private val _groupsChecked = mutableStateMapOf<Any, TriState>()
    private var forSplit = false
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
    }
    override val sheeting = Sheeting(
        sheetMap = mapOf(
            Sheets.SplitAsChoice to SplitAsChoiceBottomSheetModel(
                object : SplitAsChoiceBottomSheetModel.Callback{
                    override fun scope(): CoroutineScope {
                        return viewModelScope
                    }

                    override fun onContinue(arg: Any?) {
                        mySheeting.hide()
                        val index = arg as? Int ?: return
                        if (index == 1) {
                            gotoSplitReviewPage(false)
                        } else {
                            gotoGroupCreationPage(true)
                        }
                    }

                    override fun close() {
                        mySheeting.hide()
                    }
                }
            )
        )
    )
    private fun purgeContacts() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.purgeContacts()
        }
    }

    private val _notificationService =
        NotificationService { id, arg ->
            when (id) {
                WirelessViewModelInterface.startupNotification -> {
                    getArguments()
                    purgeContacts()
                    _statusBarColor.value = StatusBarColor(BlackSqueeze, true)
                }
                DataIds.proceedWithContacts -> {
                    proceedWithContacts()
                }
                DataIds.back -> {
                    navigation.scope { navHostController, lifecycleOwner, toaster ->
                        navHostController.popBackStack()
                    }
                }
                DataIds.textInput -> {
                    val query = (arg as? String) ?: return@NotificationService
                    splitWithInput.value = query
                    initiateSearch()
                }
                DataIds.deleteAdded -> {
                    if (arg == null) {
                        return@NotificationService
                    }
                    if (selectedContactIds.contains(arg)) {
                        removeMembersFromSelecteds(setOf(arg))
                    }
                    updateCanProceed()
                }
                DataIds.checkItem -> {
                    if (arg == null) {
                        return@NotificationService
                    }
                    if (selectedContactIds.contains(arg)) {
                        removeMembersFromSelecteds(setOf(arg))
                    } else {
                        addMembersToSelecteds(setOf(arg))
                    }
                    updateCanProceed()
                }
                DataIds.checkGroupItem -> {
                    val groupId = arg ?: return@NotificationService
                    val checked = _groupsChecked[groupId] ?: TriState.UNCHECKED
                    val group = (groupsAndContacts.first { it.id() == groupId } as? GroupData)
                        ?: return@NotificationService
                    val memberIds = group.members.map { it.id() }.toSet()
                    if (checked != TriState.UNCHECKED) {
                        removeMembersFromSelecteds(memberIds)
                    } else {
                        addMembersToSelecteds(memberIds)
                    }
                    updateCanProceed()
                }
                DataIds.selectedTabIndex -> {
                    if (arg is Int) {
                        selectedIndex.value = arg
                        filter()
                    }
                }
            }
        }

    private fun getArguments() {
        navigation.scope { navHostController, lifecycleOwner, toaster ->
            forSplit = getBoolean("split", false)
        }
    }

    private fun updateCanProceed() {
        proceedWithContacts.value = addedContacts.size > 0
    }

    override val notifier = _notificationService

    private fun proceedWithContacts() {
        saveAddedContacts()
        //saveAddedContacts{
            //withContext(Dispatchers.Main){
                if (!forSplit){
                    gotoGroupCreationPage()
                } else {
                    mySheeting.change(Sheets.SplitAsChoice)
                    mySheeting.show()
                }
            //}
        //}
    }

    private fun saveAddedContacts(/*block: suspend () -> Unit*/) {
        val contacts = addedContacts.map { it.mobile }
        DataBank.once[DataBank.Key.Members] = addedContacts.toList()
        /*viewModelScope.launch(Dispatchers.IO) {
            repo.saveContacts(contacts)
            block()
        }*/
    }

    private fun gotoGroupCreationPage(split: Boolean = false) {
        navigation.scope { navHostController, lifecycleOwner, toaster ->
            if (split){
                navHostController.navigate("${Routes.groupCreation.name}?split=true")
            } else {
                navHostController.navigate(Routes.groupCreation.name)
            }
        }
    }

    private fun addMembersToSelecteds(memberIds: Set<Any>) {
        val finalIds = selectedContactIds.union(memberIds)
        val states = mutableMapOf<Any, TriState>()
        val membersToAdd = mutableListOf<ContactData>()
        val alreadyAdded = addedContacts.map{
            it.id()
        }
        groupsAndContacts.forEach {
            if (it is GroupData){
                val ids = it.members.map { it.id }.toSet()
                val commonCount = finalIds.intersect(ids).size
                if (commonCount != 0){
                    if (commonCount == it.members.size){
                        states[it.id] = TriState.CHECKED
                    } else if (commonCount < it.members.size){
                        states[it.id] = TriState.INTERMEDIATE
                    }
                } else {
                    states[it.id] = TriState.UNCHECKED
                }
            } else {
                if (memberIds.contains(it.id()) && !alreadyAdded.contains(it.id())){
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
        if (finalIds.isEmpty()){
            _groupsChecked.clear()
        } else {
            val states = mutableMapOf<Any, TriState>()
            groupsAndContacts.forEach {
                if (it is GroupData){
                    val ids = it.members.map { it.id }.toSet()
                    val commonCount = finalIds.intersect(ids).size
                    if (commonCount != 0){
                        if (commonCount == it.members.size){
                            states[it.id] = TriState.CHECKED
                        } else if (commonCount < it.members.size){
                            states[it.id] = TriState.INTERMEDIATE
                        }
                    } else {
                        states[it.id] = TriState.UNCHECKED
                    }
                }
            }
            _groupsChecked.putAll(states)
        }
        selectedContactIds.removeAll(memberIds)
        memberIds.forEach { m ->
            val index = addedContacts.indexOfFirst {
                it.id == m
            }
            if (index > -1){
                addedContacts.removeAt(index)
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

    private fun gotoSplitReviewPage(asGroup: Boolean) {
        navigation.scope { navHostController, lifecycleOwner, toaster ->
            navHostController.navigate("${Routes.splitReviewPage.name}?asGroup=$asGroup")
        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    private fun fetchGroupAndContacts() {
        viewModelScope.launch(Dispatchers.Main) {
            val p = Manifest.permission.READ_CONTACTS
            val checked = permissionHandler.check(p)
            if (checked?.allPermissionsGranted == true) {
                reallyFetchGroupAndContacts()
            } else {
                val requested = permissionHandler.request(p)
                if (requested?.get(p) == true) {
                    reallyFetchGroupAndContacts()
                }
            }
            filter()
        }
    }

    private fun reallyFetchGroupAndContacts() {
        viewModelScope.launch(Dispatchers.IO) {
            val items = repo.groupAndContacts()
            withContext(Dispatchers.Main){
                groupsAndContacts.addAll(items)
                filter()
            }
        }
    }

    fun filter(){
        visibleGroupsAndContacts.clear()
        visibleGroupsAndContacts.addAll(
            groupsAndContacts.filter {
                itemFilter(it)
            }
        )
    }

    private fun itemFilter(it: GroupOrContact): Boolean {
        val now = System.currentTimeMillis()
        return when (selectedIndex.value) {
            1 -> {
                (it is GroupData) && search(splitWithInput.value, it.searchables())
            }
            2 -> {
                (it is ContactData) && search(splitWithInput.value, it.searchables())
            }
            else -> {
                val dif = (now - it.lastActivity()) / (24 * 3600000)
                dif <= 30 && search(splitWithInput.value, it.searchables())
            }
        }
    }
}
