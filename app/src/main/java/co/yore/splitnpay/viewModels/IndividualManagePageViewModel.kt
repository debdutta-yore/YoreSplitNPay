package co.yore.splitnpay.viewModels

import android.Manifest
import android.util.Log
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.models.*
import co.yore.splitnpay.pages.subpages.PhotoSelectionBottomSheetModel
import co.yore.splitnpay.repo.MasterRepo
import co.yore.splitnpay.repo.MasterRepoImpl
import co.yore.splitnpay.ui.theme.RobinsEggBlue
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class IndividualManagePageViewModel(
    val repo: MasterRepo = MasterRepoImpl()
) : ViewModel(), WirelessViewModelInterface {
    override val softInputMode = mutableStateOf(SoftInputMode.adjustNothing)
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
    override val notifier = NotificationService{id, arg ->
        Log.d("lflfjdfdf", "$id")
        when (id){
            "${DataIds.back}individual_manage_page" -> {
                when (mySheeting.sheets.value){
                    Sheets.None -> pageHandleBack()
                    else -> mySheeting.onBack()
                }
            }
            DataIds.groupNotificationSwitch -> {
                notificationOn.value = !notificationOn.value
            }
            DataIds.groupImageClick -> {
                mySheeting.change(Sheets.ImagePicker)
                mySheeting.show()
            }
        }
    }

    private fun pageHandleBack() {
        navigation.scope { navHostController, lifecycleOwner, toaster ->
            navHostController.popBackStack()
        }
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
    private val statusBarColor = mutableStateOf(
        StatusBarColor(
            color = RobinsEggBlue,
            darkIcons = false
        )
    )
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
            repo.groupDatas()
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
