package co.yore.splitnpay.viewModels

import android.Manifest
import android.util.Log
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.libs.jerokit.*
import co.yore.splitnpay.libs.jerokit.bottom_sheet.Sheeting
import co.yore.splitnpay.libs.jerokit.bottom_sheet.Sheets
import co.yore.splitnpay.models.*
import co.yore.splitnpay.pages.subpages.*
import co.yore.splitnpay.repo.MasterRepo
import co.yore.splitnpay.repo.MasterRepoImpl
import co.yore.splitnpay.ui.theme.RobinsEggBlue
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ManageViewModel @Inject constructor(
    private val repo: MasterRepo// = MasterRepoImpl()
) : ViewModel(), WirelessViewModelInterface {
    override val softInputMode = mutableStateOf(SoftInputMode.adjustNothing)
    override val resolver = Resolver()
    override val navigation = Navigation()
    override val permissionHandler = PermissionHandler()
    override val resultingActivityHandler = ResultingActivityHandler()

    // ////////////////////////////////////////
    private val _profileImage = mutableStateOf<Any?>(null)
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
                object : UnsettledMembersAlertSheetModel.Callback{
                    override fun scope(): CoroutineScope {
                        return viewModelScope
                    }

                    override suspend fun members(): List<SingleSettledOrUnsettledMember> {
                        return repo.singleSettledOrUnsettledMembers()
                    }

                    override fun onContinue() {
                        mySheeting.sheets.value = Sheets.ConfirmDelete
                    }
                }
            ),
            Sheets.ConfirmDelete to DeleteAlertSheetModel(
                object : DeleteAlertSheetModel.Callback{
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
                object : SuccessUndoSheetModel.Callback{
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
                        return 60 * 1000
                    }
                }
            ),
            Sheets.SettledUnsettledMembers to SettledUnsettledMembersBottomSheetModel(
                object : SettledUnsettledMembersBottomSheetModel.Callback{
                    override fun scope(): CoroutineScope {
                        return viewModelScope
                    }

                    override fun close() {
                        mySheeting.hide()
                    }

                    override suspend fun settledMembers(): List<SingleSettledOrUnsettledMember> {
                        return repo.settledMembers()
                    }

                    override suspend fun unsettledMembers(): List<SingleSettledOrUnsettledMember> {
                        return repo.unsettledMembers()
                    }

                    override fun onContinue(
                        settledMembers: List<SingleSettledOrUnsettledMember>,
                        unsettledMembers: List<SingleSettledOrUnsettledMember>
                    ) {
                        mySheeting.change(Sheets.ConfirmDelete)
                    }

                }
            ),
            Sheets.ImagePicker to PhotoSelectionBottomSheetModel(
                object : PhotoSelectionBottomSheetModel.Callback{
                    override fun scope(): CoroutineScope {
                        return viewModelScope
                    }

                    override fun onContinue(arg: Any?) {
                        mySheeting.hide()
                        handleCameraOrGallery(arg as? String ?: return)
                    }

                    override fun close() {
                        mySheeting.hide()
                    }
                }
            )
        ),
        onVisibilityChanged = {
            if (!it){
                mySheeting.sheets.value = Sheets.None
            }
        }
    )

    private fun handleCameraOrGallery(arg: String) {
        when (arg){
            "Camera" -> {
                capturePicture()
            }
            "Gallery" -> {
                takePicture()
            }
        }
    }

    @OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterialApi::class)
    private fun capturePicture() {
        mySheeting.hide()
        viewModelScope.launch {
            val p = Manifest.permission.CAMERA
            val state = permissionHandler.check(p)
            if (state?.allPermissionsGranted == true){
                _groupImage.value = resultingActivityHandler.takePicturePreview()
            } else {
                val result = permissionHandler.request(p)
                if (result?.get(p) == true){
                    _groupImage.value = resultingActivityHandler.takePicturePreview()
                }
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    private fun takePicture() {
        mySheeting.hide()
        viewModelScope.launch {
            val uri = resultingActivityHandler.getContent("image/*")
            _groupImage.value = uri
        }
    }

    // ////////////////////////////////////////
    override val notifier =
        NotificationService { id, arg ->
            when (id) {
                DataIds.pickImage -> {
                    mySheeting.change(Sheets.ImagePicker)
                    mySheeting.show()
                }
                WirelessViewModelInterface.startupNotification -> {
                    setUpStatusBarColor()
                }
                "${DataIds.back}group_manage" -> {
                    when (mySheeting.sheets.value) {
                        Sheets.None -> {
                            navigation.scope { navHostController, lifecycleOwner, toaster ->
                                navHostController.popBackStack()
                            }
                        }
                        else -> {
                            mySheeting.map[mySheeting.sheets.value]?.onBack()
                        }
                    }
                }
                DataIds.groupNotificationSwitch -> {
                    _groupNotificationSwitch.value = !_groupNotificationSwitch.value
                }
                DataIds.addMemberClick -> {
                    // TODO: click
                }
                DataIds.inviteViaLinkClick -> {
                    // TODO: click
                }
                DataIds.leaveGroupClick -> {
                    // TODO: click
                }
                DataIds.deleteGroupClick -> {
                    mySheeting.sheets.value = Sheets.UnsettledMembersAlert
                    mySheeting.show()
                }
                DataIds.selectMemberClick -> {
                    onSelectMemberClick(arg)
                }
                DataIds.deleteMembersClick -> {
                    mySheeting.change(Sheets.SettledUnsettledMembers)
                    mySheeting.show()
                    val count = _groupMembers.size
                    for (i in 0 until count) {
                        if (_groupMembers[i].isSelected) {
                            _groupMembers[i] = _groupMembers[i].copy(isSelected = false)
                        }
                    }
                }
            }
        }

    private fun setUpStatusBarColor() {
        _statusBarColor.value = StatusBarColor(
            color = RobinsEggBlue,
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

    // ///////////////////////////////////////
    init {
        setUpResolver()
        populateMembers()
    }

    private fun populateMembers() {
        viewModelScope.launch(Dispatchers.IO) {
            val members = repo.getMembers1()
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
            DataIds.groupImage to _groupImage
        )
    }
}
