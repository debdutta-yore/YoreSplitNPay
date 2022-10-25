package co.yore.splitnpay.viewModels

import android.Manifest
import android.util.Log
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.yore.splitnpay.components.PhotoSelectionBottomSheetModel
import co.yore.splitnpay.components.components.DeleteAlertSheetModel
import co.yore.splitnpay.components.components.SettledUnsettledMembersBottomSheetModel
import co.yore.splitnpay.components.components.SuccessUndoSheetModel
import co.yore.splitnpay.components.components.UnsettledMembersAlertSheetModel
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.models.ContactData
import co.yore.splitnpay.models.DataIds
import co.yore.splitnpay.models.GroupData
import co.yore.splitnpay.models.Sheets
import co.yore.splitnpay.pages.SettleOptions
import co.yore.splitnpay.pages.SingleSettledOrUnsettledMember
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class IndividualManagePageViewModel: ViewModel(), WirelessViewModelInterface {
    override val resolver = Resolver()
    override val sheeting = Sheeting(
        sheetMap = mapOf(
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
    override val notifier = NotificationService{id,arg->
        Log.d("lflfjdfdf","$id")
        when(id){
            "${DataIds.back}individual_manage_page"->{
                when(mySheeting.sheets.value){
                    Sheets.None->pageHandleBack()
                    else->mySheeting.onBack()
                }
            }
            DataIds.groupNotificationSwitch->{
                notificationOn.value = !notificationOn.value
            }
            DataIds.groupImageClick->{
                mySheeting.change(Sheets.ImagePicker)
                mySheeting.show()
            }
        }
    }

    private fun pageHandleBack() {

    }

    override val navigation = Navigation()
    override val permissionHandler = PermissionHandler()
    override val resultingActivityHandler = ResultingActivityHandler()
    private val name = mutableStateOf("Deb Pan")
    private val mobile = mutableStateOf("7851203695")
    private val image = mutableStateOf<Any?>("https://i.pravatar.cc/300")
    private val joiningDate = mutableStateOf("9th May, 2022")
    private val notificationOn = mutableStateOf(false)
    private val groups = mutableStateListOf<GroupData>()
    private val statusBarColor = mutableStateOf(StatusBarColor(
        color = Color(0xff00CEC3),
        darkIcons = false
    ))
    init {
        resolver.addAll(
            DataIds.memberName to name,
            DataIds.memberMobile to mobile,
            DataIds.memberImage to image,
            DataIds.memberJoiningDate to joiningDate,
            DataIds.groupNotificationSwitch to notificationOn,
            DataIds.groups to groups,
            DataIds.statusBarColor to statusBarColor
        )
        groups.addAll(
            listOf(
                GroupData(
                    id = 0,
                    image = "https://i.pravatar.cc/300",
                    name = "Tiger",
                    members = listOf(
                        ContactData(
                            id = 0,
                            image = "https://i.pravatar.cc/300",
                            name = "Sanjana Roy",
                            mobile = "7964210356",
                        ),
                        ContactData(
                            id = 0,
                            image = "https://i.pravatar.cc/300",
                            name = "Sanjana Roy",
                            mobile = "7964210356",
                        ),
                        ContactData(
                            id = 0,
                            image = "https://i.pravatar.cc/300",
                            name = "Sanjana Roy",
                            mobile = "7964210356",
                        ),
                        ContactData(
                            id = 0,
                            image = "https://i.pravatar.cc/300",
                            name = "Sanjana Roy",
                            mobile = "7964210356",
                        ),
                    ),
                    willGet = 1000f,
                    willPay = 200f,
                )
            )
        )
    }

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
                image.value = resultingActivityHandler.takePicturePreview()
            } else {
                val result = permissionHandler.request(p)
                if (result?.get(p) == true){
                    image.value = resultingActivityHandler.takePicturePreview()
                }
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    private fun takePicture() {
        mySheeting.hide()
        viewModelScope.launch {
            val uri = resultingActivityHandler.getContent("image/*")
            image.value = uri
        }
    }
}