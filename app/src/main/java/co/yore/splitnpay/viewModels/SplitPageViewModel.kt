package co.yore.splitnpay.viewModels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.yore.splitnpay.components.components.SplitPageTabs
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.models.*
import co.yore.splitnpay.repo.Repo
import co.yore.splitnpay.repo.RepoImpl
import kotlinx.coroutines.launch

enum class SplitPageState{
    GET,
    PAY,
    NONE
}

class SplitPageViewModel(
    private val repo: Repo = RepoImpl()
): ViewModel(), WirelessViewModelInterface {
    override val navigation = mutableStateOf<UIScope?>(null)
    private val _whole = mutableStateOf("4,000")
    private val _decimal = mutableStateOf("05")
    private val _wholeGet = mutableStateOf("2,000")
    private val _decimalGet = mutableStateOf("50")
    private val _wholePay = mutableStateOf("500")
    private val _decimalPay = mutableStateOf("30")
    private val _statusBarColor = mutableStateOf<StatusBarColor?>(null)
    private val _ultimateState = mutableStateOf(SplitPageState.PAY)
    private val _groups = mutableStateListOf<GroupCardData>()
    private val _peoples = mutableStateListOf<ContactData>()
    private val _willGetActive = mutableStateOf(false)
    private val _willPayActive = mutableStateOf(false)
    private val _input = mutableStateOf("")
    private val _subTabGroup = mutableStateOf(SplitPageTabs.All)
    private val _subTabPeople = mutableStateOf(SplitPageTabs.All)
    private val _groupsAndPeoplesVault = mutableListOf<GroupOrContact>()
    private val _groupsAndPeoples = mutableStateListOf<GroupOrContact>()
    private val _selectedTabIndex = mutableStateOf(0)
    private val _noGroup = mutableStateOf(false)
    private val _haveSplit = mutableStateOf(true)
    ///////////////////////////////////////////////////////
    private val _notificationService = NotificationService{id,arg->
        when(id){
            DataIds.textInput ->{
                _input.value = (arg as? String)?:return@NotificationService
                filter()
            }
            DataIds.groupCard ->{
                //Todo
            }
            DataIds.peopleCard ->{
                //Todo
            }
            DataIds.split ->{
                gotoSplitWithPage()
            }
            DataIds.groupCardGo ->{
                //Todo
            }
            DataIds.addGroup ->{
                //Todo
            }
            DataIds.peopleCardGo ->{
                //Todo
            }
            "${DataIds.back}split_page" ->{
                //Todo
            }
            DataIds.getCard ->{
                //Todo
            }
            DataIds.payCard ->{
                //Todo
            }
            DataIds.selectedTabIndex ->{
                val index = (arg as? Int)?:return@NotificationService
                if(_selectedTabIndex.value==index){
                    return@NotificationService
                }
                _selectedTabIndex.value = index
                filter()
            }
            "${DataIds.subTab}group"->{
                _subTabGroup.value = (arg as? SplitPageTabs)?:return@NotificationService
                filter()
            }
            "${DataIds.subTab}people"->{
                _subTabPeople.value = (arg as? SplitPageTabs)?:return@NotificationService
                filter()
            }
        }
    }

    private fun gotoSplitWithPage() {
        navigation.scope{ navHostController, lifecycleOwner, toaster ->
            navHostController.navigate("split_with_page")
        }
    }

    override val notifier = _notificationService
    private val _resolver = Resolver()
    override val resolver = _resolver
    init {
        resolver.addAll(
            DataIds.whole to _whole,
            DataIds.decimal to _decimal,
            DataIds.wholeGet to _wholeGet,
            DataIds.decimalGet to _decimalGet,
            DataIds.wholePay to _wholePay,
            DataIds.decimalPay to _decimalPay,
            DataIds.statusBarColor to _statusBarColor,
            DataIds.groups to _groups,
            DataIds.peoples to _peoples,
            DataIds.willGetActive to _willGetActive,
            DataIds.willPayActive to _willPayActive,
            DataIds.textInput to _input,
            DataIds.ultimateState to _ultimateState,
            DataIds.groupsAndPeoples to _groupsAndPeoples,
            DataIds.selectedTabIndex to _selectedTabIndex,
            DataIds.noGroup to _noGroup,
            DataIds.haveSplit to _haveSplit,
            "${DataIds.subTab}group" to _subTabGroup,
            "${DataIds.subTab}people" to _subTabPeople,
        )
        _statusBarColor.value = StatusBarColor(Color(0xffDB3665),false)
        viewModelScope.launch {
            _groupsAndPeoplesVault.addAll(repo.groupAndContacts())
            filter()
        }
    }

    fun filter(){
        _groupsAndPeoples.clear()
        _groupsAndPeoples.addAll(
            _groupsAndPeoplesVault.filter {
                itemFilter(it)
            }
        )
    }

    private fun itemFilter(it: GroupOrContact): Boolean {
        val tab = _selectedTabIndex.value
        if((tab==0&&it !is GroupData)||(tab==1&&it !is ContactData)){
            return false
        }
        val owe = if(tab==0) _subTabGroup.value else _subTabPeople.value
        if(
            (owe==SplitPageTabs.YouOwe&&(it.willGet()>0f||it.willPay()==0f))
            ||(owe==SplitPageTabs.YouAreOwed&&(it.willPay()>0f||it.willGet()==0f))
        ){
            return false
        }
        return search(_input.value,it.searchables())
    }
}