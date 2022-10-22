package co.yore.splitnpay.viewModels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.yore.splitnpay.components.components.DeleteAlertSheetModel
import co.yore.splitnpay.components.components.SuccessUndoSheetModel
import co.yore.splitnpay.components.components.UnsettledMembersAlertSheetModel
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.models.DataIds
import co.yore.splitnpay.models.Sheets
import co.yore.splitnpay.pages.Member
import co.yore.splitnpay.pages.SettleOptions
import co.yore.splitnpay.pages.SingleSettledOrUnsettledMember
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface ManageRepository {
    suspend fun getMembers(): List<Member>
}
class ManageRepositoryMockImpl: ManageRepository {
    val group = listOf(
        Member(
            id = 1,
            profilePic = "https://i.pravatar.cc/300",
            userName = "You",
            mobileNo = "9563376942",
            isSelected = false,
            isGroupAdmin = true
        ),
        Member(
            id = 2,
            profilePic = "https://i.pravatar.cc/300",
            userName = "Manisha Roy",
            mobileNo = "9563376942",
            isSelected = false
        ),
        Member(
            id = 3,
            profilePic = "https://i.pravatar.cc/300",
            userName = "Sushil Roy",
            mobileNo = "9563376942",
            isSelected = false
        ),
        Member(
            id = 4,
            profilePic = "https://i.pravatar.cc/300",
            userName = "Sanjana Ray",
            mobileNo = "9563376942",
            isSelected = false
        ),
        Member(
            id = 5,
            profilePic = "https://i.pravatar.cc/300",
            userName = "Ankita Ray",
            mobileNo = "9563376942",
            isSelected = false
        ),
    )

    override suspend fun getMembers(): List<Member> {
        return group
    }

}
class ManageViewModel(
    private val repo: ManageRepository = ManageRepositoryMockImpl()
) : ViewModel(), WirelessViewModelInterface {
    override val resolver = Resolver()
    override val navigation = Navigation()
    override val permissionHandler = PermissionHandler()
    override val resultingActivityHandler = ResultingActivityHandler()

    //////////////////////////////////////////
    private val _groupMembers = mutableStateListOf<Member>()
    private val _statusBarColor = mutableStateOf<StatusBarColor?>(null)
    private val _groupName = mutableStateOf("Office buddies")
    private val _numberOfGroupMembers = mutableStateOf(5)
    private val _groupCreatedBy = mutableStateOf("you")
    private val _groupCreationDate = mutableStateOf("9th May, 2022")
    private val _groupNotificationSwitch = mutableStateOf(false)
    private val _groupImage = mutableStateOf<Any?>("https://picsum.photos/200/300")
    override val sheeting = Sheeting(
        sheetMap = mapOf(
            Sheets.UnsettledMembersAlert to UnsettledMembersAlertSheetModel(
                object: UnsettledMembersAlertSheetModel.Callback{
                    override fun scope(): CoroutineScope {
                        return viewModelScope
                    }

                    override suspend fun members(): List<SingleSettledOrUnsettledMember> {
                        return listOf(
                            SingleSettledOrUnsettledMember(
                                selectedSettleOption = SettleOptions.SplitIndividual,
                                isChecked = true,
                                isSettledMember = false,
                                imageUrl = "https://i.pravatar.cc/300",
                                userName = "Sushil Roy",
                                userPhNo = "8967114927",
                                getAmount = 600f,
                                paidAmount = 0f
                            ),
                            SingleSettledOrUnsettledMember(
                                selectedSettleOption = SettleOptions.SplitIndividual,
                                isChecked = true,
                                isSettledMember = false,
                                imageUrl = "https://i.pravatar.cc/300",
                                userName = "Sushil Roy",
                                userPhNo = "8967114927",
                                getAmount = 600f,
                                paidAmount = 0f
                            ),
                            SingleSettledOrUnsettledMember(
                                selectedSettleOption = SettleOptions.SplitIndividual,
                                isChecked = true,
                                isSettledMember = false,
                                imageUrl = "https://i.pravatar.cc/300",
                                userName = "Sushil Roy",
                                userPhNo = "8967114927",
                                getAmount = 600f,
                                paidAmount = 0f
                            ),
                            SingleSettledOrUnsettledMember(
                                selectedSettleOption = SettleOptions.SplitIndividual,
                                isChecked = true,
                                isSettledMember = false,
                                imageUrl = "https://i.pravatar.cc/300",
                                userName = "Sushil Roy",
                                userPhNo = "8967114927",
                                getAmount = 600f,
                                paidAmount = 0f
                            ),
                            SingleSettledOrUnsettledMember(
                                selectedSettleOption = SettleOptions.SplitIndividual,
                                isChecked = true,
                                isSettledMember = false,
                                imageUrl = "https://i.pravatar.cc/300",
                                userName = "Sushil Roy",
                                userPhNo = "8967114927",
                                getAmount = 600f,
                                paidAmount = 0f
                            ),
                            SingleSettledOrUnsettledMember(
                                selectedSettleOption = SettleOptions.SplitIndividual,
                                isChecked = true,
                                isSettledMember = false,
                                imageUrl = "https://i.pravatar.cc/300",
                                userName = "Sushil Roy",
                                userPhNo = "8967114927",
                                getAmount = 600f,
                                paidAmount = 0f
                            ),
                            SingleSettledOrUnsettledMember(
                                selectedSettleOption = SettleOptions.SplitIndividual,
                                isChecked = true,
                                isSettledMember = false,
                                imageUrl = "https://i.pravatar.cc/300",
                                userName = "Sushil Roy",
                                userPhNo = "8967114927",
                                getAmount = 600f,
                                paidAmount = 0f
                            ),
                        )
                    }

                    override fun onContinue() {
                        mySheeting.sheets.value = Sheets.ConfirmDelete
                    }
                }
            ),
            Sheets.ConfirmDelete to DeleteAlertSheetModel(
                object: DeleteAlertSheetModel.Callback{
                    override fun scope(): CoroutineScope {
                        return viewModelScope
                    }

                    override fun message(): String {
                        return "Are you sure, you want to delete this group?"
                    }

                    override fun onDelete() {
                        mySheeting.change(Sheets.SuccessUndo)
                    }

                    override fun onCancel() {
                        mySheeting.hide()
                    }

                    override fun close() {
                        mySheeting.hide()
                    }
                }
            ),
            Sheets.SuccessUndo to SuccessUndoSheetModel(
                object: SuccessUndoSheetModel.Callback{
                    override fun scope(): CoroutineScope {
                        return viewModelScope
                    }

                    override fun undo() {
                        mySheeting.hide()
                    }

                    override fun timerEnded() {
                        mySheeting.hide()
                    }

                    override fun close() {
                        mySheeting.hide()
                    }

                    override fun message(): String {
                        return "Group deleted successfully"
                    }

                    override fun timeMillis(): Int {
                        return 60*1000
                    }
                }
            )
        ),
        onVisibilityChanged = {
            if(!it){
                mySheeting.sheets.value = Sheets.None
            }
        }
    )
    //////////////////////////////////////////
    override val notifier = NotificationService { id, arg ->
        when (id) {
            WirelessViewModelInterface.startupNotification -> {
                setUpStatusBarColor()
            }
            "${DataIds.back}group_manage" -> {
                when(mySheeting.sheets.value){
                    Sheets.None->{
                        navigation.scope { navHostController, lifecycleOwner, toaster ->
                            navHostController.popBackStack()
                        }
                    }
                    else->{
                        mySheeting.map[mySheeting.sheets.value]?.onBack()
                    }
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
                mySheeting.sheets.value = Sheets.UnsettledMembersAlert
                mySheeting.show()
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