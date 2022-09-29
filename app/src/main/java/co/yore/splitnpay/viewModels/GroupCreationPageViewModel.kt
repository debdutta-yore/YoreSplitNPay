package co.yore.splitnpay.viewModels

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GroupCreationPageViewModel(
    private val repo: Repo = RepoImpl()
): ViewModel(), WirelessViewModelInterface {
    override val resolver = Resolver()
    override val navigation = Navigation()
    //////////////////////////////////////////
    private val _friends = mutableStateListOf<ContactData>()
    private val _statusBarColor = mutableStateOf<StatusBarColor?>(null)
    private val _groupName = mutableStateOf("")
    //////////////////////////////////////////
    override val notifier = NotificationService{id,arg->
        when(id){
            DataIds.back->{
                navigation.scope { navHostController, lifecycleOwner, toaster ->
                    navHostController.popBackStack()
                }
            }
            DataIds.pickImage->{

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
            DataIds.groupName to _groupName
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