package co.yore.splitnpay.viewModels

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
    private val _friends = mutableStateListOf<ContactData>()
    private val _statusBarColor = mutableStateOf<StatusBarColor?>(null)
    private val _groupName = mutableStateOf("")
    //////////////////////////////////////////
    @OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterialApi::class)
    override val notifier = NotificationService{ id, arg->
        when(id){
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
    /////////////////////////////////////////
    init {
        resolver.addAll(
            DataIds.contacts to _friends,
            DataIds.statusBarColor to _statusBarColor,
            DataIds.groupName to _groupName,
        )
        _statusBarColor.value = StatusBarColor(
            color = Color(0xffEDF3F9),
            darkIcons = true
        )
        viewModelScope.launch(Dispatchers.IO) {
            _friends.addAll(repo.peoples(4))
        }
    }
}