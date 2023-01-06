package co.yore.splitnpay.viewModels

import android.Manifest
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.yore.splitnpay.DataBank
import co.yore.splitnpay.app.Routes
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.libs.jerokit.*
import co.yore.splitnpay.libs.jerokit.bottom_sheet.Sheeting
import co.yore.splitnpay.libs.jerokit.bottom_sheet.Sheets
import co.yore.splitnpay.models.ContactData
import co.yore.splitnpay.models.DataIds
import co.yore.splitnpay.models.StatusBarColor
import co.yore.splitnpay.pages.subpages.PhotoSelectionBottomSheetModel
import co.yore.splitnpay.repo.MasterRepo
import co.yore.splitnpay.ui.theme.BlackSqueeze
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupCreationPageViewModel @Inject constructor(
    private val repo: MasterRepo,
    private val media: Media
) : ViewModel(), WirelessViewModelInterface {
    override val softInputMode = mutableStateOf(SoftInputMode.adjustNothing)
    private val contacts = mutableStateListOf<ContactData>()
    private val profileImage = mutableStateOf<Any?>(null)

    @OptIn(ExperimentalMaterialApi::class)
    override val resultingActivityHandler = ResultingActivityHandler()
    override val resolver = Resolver()
    override val navigation = Navigation()
    override val permissionHandler = PermissionHandler()

    @OptIn(ExperimentalMaterialApi::class)
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
        },
        confirmStateChange = {
            true
        }
    )

    // ////////////////////////////////////////
    private val _statusBarColor = mutableStateOf<StatusBarColor?>(null)
    private val _groupName = mutableStateOf("")
    private val canProceed = mutableStateOf(false)

    private fun createGroup(){
        val members = contacts.map {
            it.copy(
                image = getImage(it.image)
            )
        }
        val groupName = _groupName.value
        val image = getImage(profileImage.value)
        viewModelScope.launch(Dispatchers.IO) {
            val id = repo.createGroup(members, groupName, image)
            if(id != null){
                onGroupCreated(id)
            }
            else{
                onGroupCreateFailed()
            }
        }
    }

    private fun onGroupCreateFailed() {

    }

    private fun onGroupCreated(id: String) {
        navigation.scope { navHostController, lifecycleOwner, toaster ->
            navHostController.popBackStack(Routes.splitPage.name,false)
            if (this.getBoolean("split")) {
                navHostController.navigate("${Routes.splitReviewPage.name}?asGroup=true")
            } else {
                navHostController.navigate("${Routes.groupChatPage.name}/$id")
            }
        }
    }

    private fun getImage(image: Any?): Bitmap? {
        if(image is Uri){
            return media.decodeBitmapFromUri(image)
        }
        if(image is Bitmap){
            return image
        }
        if(image is String && image.startsWith("content://")){
            return media.decodeBitmapFromUri(Uri.parse(image))
        }
        return null
    }

    // ////////////////////////////////////////
    @OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterialApi::class)
    override val notifier =
        NotificationService { id, arg ->
            when (id) {
                DataIds.proceed -> {
                    navigation.scope { navHostController, lifecycleOwner, toaster ->
                        //navHostController.popBackStack(Routes.splitPage.full, false)
                        if (this.getBoolean("split")) {
                            DataBank.once[DataBank.Key.SplitMembers] = contacts.toList()
                            DataBank.once[DataBank.Key.GroupName] = _groupName.value
                            DataBank.once[DataBank.Key.GroupImage] = profileImage.value
                            navHostController.navigate("${Routes.splitReviewPage.name}?asGroup=true")
                        } else {
                            createGroup()
                            //navHostController.navigate(Routes.groupChatPage.name)
                        }
                    }
                }
                DataIds.deleteAdded -> {
                    deleteItem(arg as? ContactData ?: return@NotificationService)
                }
                WirelessViewModelInterface.startupNotification -> {
                    fetchContacts()
                }
                DataIds.cameraOrGallery -> {
                    handleCameraOrGallery(arg as? String ?: return@NotificationService)
                }
                DataIds.back -> {
                    navigation.scope { navHostController, lifecycleOwner, toaster ->
                        navHostController.popBackStack()
                    }
                }
                DataIds.pickImage -> {
                    mySheeting.show()
                }
                DataIds.groupName -> {
                    _groupName.value = (arg as? String) ?: ""
                    updateCanProceed()
                }
            }
        }

    private fun updateCanProceed() {
        canProceed.value = _groupName.value.isNotEmpty()
    }

    private fun deleteItem(contactData: ContactData) {
        if (contacts.size < 3){
            return
        }
        val index = contacts.indexOfFirst {
            it.mobile == contactData.mobile
        }
        if (index > -1){
            contacts.removeAt(index)
        }
        adjustDeletable()
    }

    private fun adjustDeletable() {
        if (contacts.size == 2){
            try {
                contacts[0] = contacts[0].copy(deletable = false)
                contacts[1] = contacts[1].copy(deletable = false)
            } catch (e: Exception) {
            }
        }
    }

    private fun fetchContacts() {
        viewModelScope.launch(Dispatchers.IO) {
            //val c = repo.contacts()
            //contacts.clear()
            val list = DataBank.once[DataBank.Key.Members] as? List<ContactData>?: emptyList()
            contacts.addAll(/*repo.deviceContacts(c)*/list.map { it.copy(deletable = true) })
            contacts.add(repo.mySelfContact())
            adjustDeletable()
        }
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
                profileImage.value = resultingActivityHandler.takePicturePreview()
            } else {
                val result = permissionHandler.request(p)
                if (result?.get(p) == true){
                    profileImage.value = resultingActivityHandler.takePicturePreview()
                }
            }
            updateCanProceed()
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    private fun takePicture() {
        mySheeting.hide()
        viewModelScope.launch {
            val uri = resultingActivityHandler.getContent("image/*")
            profileImage.value = uri
            updateCanProceed()
        }
    }

    // ///////////////////////////////////////
    init {
        resolver.addAll(
            DataIds.statusBarColor to _statusBarColor,
            DataIds.groupName to _groupName,
            DataIds.profileImage to profileImage,
            DataIds.contacts to contacts,
            DataIds.canProceed to canProceed,
        )
        _statusBarColor.value = StatusBarColor(
            color = BlackSqueeze,
            darkIcons = true
        )
    }
}
