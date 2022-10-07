package co.yore.splitnpay.viewModels

import android.Manifest
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.models.ContactData
import co.yore.splitnpay.models.DataIds
import co.yore.splitnpay.repo.Repo
import co.yore.splitnpay.repo.RepoImpl
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GroupCreationPageViewModel(
    private val repo: Repo = RepoImpl()
): ViewModel(), WirelessViewModelInterface {
    private val contacts = mutableStateListOf<ContactData>()
    private val profileImage = mutableStateOf<Any?>(null)
    @OptIn(ExperimentalMaterialApi::class)
    override val sheetHandler = SheetHandler(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
        confirmStateChange = {
            true
        }
    )
    override val resultingActivityHandler = ResultingActivityHandler()
    override val resolver = Resolver()
    override val navigation = Navigation()
    override val permissionHandler = PermissionHandler()

    //////////////////////////////////////////
    private val _statusBarColor = mutableStateOf<StatusBarColor?>(null)
    private val _groupName = mutableStateOf("")
    //////////////////////////////////////////
    @OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterialApi::class)
    override val notifier = NotificationService{ id, arg->
        when(id){
            DataIds.deleteAdded->{
                deleteItem(arg as? ContactData?:return@NotificationService)
            }
            WirelessViewModelInterface.startupNotification->{
                fetchContacts()
            }
            DataIds.cameraOrGallery->{
                handleCameraOrGallery(arg as? String?:return@NotificationService)
            }
            DataIds.back->{
                navigation.state { navHostController, lifecycleOwner, toaster ->
                    navHostController.popBackStack()
                }
            }
            DataIds.pickImage->{
                viewModelScope.launch {
                    /*val r = permissionHandler.check(Manifest.permission.CAMERA) ?: return@launch
                    Log.d("fldjfdlf","${r.permissions}")
                    if(!r.allPermissionsGranted){
                        var m = permissionHandler.request(Manifest.permission.CAMERA)
                        Log.d("fldjfdlf","$m")
                    }
                    else{
                        val a = resultingActivityHandler.takePicturePreview()
                        if(a!=null){
                            val b = a.width
                        }
                    }*/

                    //val a = resultingActivityHandler.getContent("image/*")
                    /*if(a!=null){
                        val b = a
                    }*/
                    sheetHandler.state {
                        show()
                    }
                }
            }
            DataIds.groupName->{
                _groupName.value = (arg as? String)?:""
            }
        }
    }

    private fun deleteItem(contactData: ContactData) {
        if(contacts.size<3){
            return
        }
        val index = contacts.indexOfFirst {
            it.mobile==contactData.mobile
        }
        if(index > -1){
            contacts.removeAt(index)
        }
        if(contacts.size==2){
            try {
                contacts[0] = contacts[0].copy(deletable = false)
                contacts[1] = contacts[1].copy(deletable = false)
            } catch (e: Exception) {
            }
        }
    }

    private fun fetchContacts() {
        viewModelScope.launch(Dispatchers.IO) {
            val c = repo.contacts()
            contacts.clear()
            contacts.addAll(repo.deviceContacts(c))
        }
    }

    private fun handleCameraOrGallery(arg: String) {
        when(arg){
            "Camera"->{
                capturePicture()
            }
            "Gallery"->{
                takePicture()
            }
        }
    }

    @OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterialApi::class)
    private fun capturePicture() {
        sheetHandler.state {
            hide()
        }
        viewModelScope.launch {
            val p = Manifest.permission.CAMERA
            val state = permissionHandler.check(p)
            if(state?.allPermissionsGranted==true){
                profileImage.value = resultingActivityHandler.takePicturePreview()
            }
            else{
                val result = permissionHandler.request(p)
                if(result?.get(p)==true){
                    profileImage.value = resultingActivityHandler.takePicturePreview()
                }
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    private fun takePicture() {
        sheetHandler.state {
            hide()
        }
        viewModelScope.launch {
            val uri = resultingActivityHandler.getContent("image/*")
            profileImage.value = uri
        }
    }

    /////////////////////////////////////////
    init {
        resolver.addAll(
            DataIds.statusBarColor to _statusBarColor,
            DataIds.groupName to _groupName,
            DataIds.profileImage to profileImage,
            DataIds.contacts to contacts
        )
        _statusBarColor.value = StatusBarColor(
            color = Color(0xffEDF3F9),
            darkIcons = true
        )
    }
}