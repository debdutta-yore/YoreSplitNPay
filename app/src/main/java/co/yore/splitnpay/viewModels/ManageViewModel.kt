package co.yore.splitnpay.viewModels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.yore.splitnpay.libs.NotificationService
import co.yore.splitnpay.libs.StatusBarColor
import co.yore.splitnpay.libs.WirelessViewModelInterface
import co.yore.splitnpay.models.DataIds
import co.yore.splitnpay.pages.Member
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface ManageRepository {
    suspend fun getMembers(): List<Member>
}

class ManageViewModel(
    private val repo: ManageRepository = ManageRepositoryMockImpl()
) : ViewModel(), WirelessViewModelInterface {
    override val resolver = Resolver()
    override val navigation = Navigation()

    //////////////////////////////////////////
    private val _groupMembers = mutableStateListOf<Member>()
    private val _statusBarColor = mutableStateOf<StatusBarColor?>(null)
    private val _groupName = mutableStateOf("Office buddies")
    private val _numberOfGroupMembers = mutableStateOf(5)
    private val _groupCreatedBy = mutableStateOf("you")
    private val _groupCreationDate = mutableStateOf("9th May, 2022")
    private val _groupNotificationSwitch = mutableStateOf(false)
    private val _groupImage = mutableStateOf<Any?>("https://picsum.photos/200/300")

    //////////////////////////////////////////
    override val notifier = NotificationService { id, arg ->
        when (id) {
            WirelessViewModelInterface.startupNotification -> {
                setUpStatusBarColor()
            }
            DataIds.back -> {
                navigation.scope { navHostController, lifecycleOwner, toaster ->
                    navHostController.popBackStack()
                }
            }
            DataIds.groupNotificationSwitch -> {
                _groupNotificationSwitch.value = !_groupNotificationSwitch.value
            }
            DataIds.addMemberClick -> {
                //TODO: click
            }
            DataIds.inviteViaLinkClick -> {
                //TODO: click
            }
            DataIds.leaveGroupClick -> {
                //TODO: click
            }
            DataIds.deleteGroupClick -> {
                //TODO: open bottom sheet
            }
            DataIds.selectMemberClick -> {
                onSelectMemberClick(arg)
            }
            DataIds.deleteMembersClick -> {

            }
        }
    }

    private fun setUpStatusBarColor() {
        _statusBarColor.value = StatusBarColor(
            color = StatusBarGreen,
            darkIcons = true
        )
    }

    private fun onSelectMemberClick(arg: Any?) {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                val index = _groupMembers.indexOf(arg)
                if (index > -1) {
                    _groupMembers[index] =
                        _groupMembers[index].copy(isSelected = !_groupMembers[index].isSelected)
                }
            }
        }
    }

    /////////////////////////////////////////
    init {
        setUpResolver()
        populateMembers()
    }

    private fun populateMembers() {
        viewModelScope.launch(Dispatchers.IO) {
            val members = repo.getMembers()
            withContext(Dispatchers.Main) {
                _groupMembers.addAll(
                    members
                )
            }
        }
    }

    private fun setUpResolver() {
        resolver.addAll(
            DataIds.groupMembers to _groupMembers,
            DataIds.statusBarColor to _statusBarColor,
            DataIds.groupName to _groupName,
            DataIds.numberOfGroupMembers to _numberOfGroupMembers,
            DataIds.groupCreatedBy to _groupCreatedBy,
            DataIds.groupCreationDate to _groupCreationDate,
            DataIds.groupNotificationSwitch to _groupNotificationSwitch,
            DataIds.groupImage to _groupImage,
        )
    }
}