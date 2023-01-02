package co.yore.splitnpay.viewModels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.yore.splitnpay.libs.EventBus
import co.yore.splitnpay.libs.kontakts.Kontakts
import co.yore.splitnpay.libs.jerokit.*
import co.yore.splitnpay.libs.kontakts.MergedContact
import co.yore.splitnpay.libs.kontakts.suitNamePhoneEmailImage
import co.yore.splitnpay.libs.kontakts.toSingles
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

class MainViewModel : WirelessViewModelInterface, ViewModel() {

    init {
        viewModelScope.launch {
            EventBus.events.filter { event -> event.first == "content" }.collectLatest { fetchContacts() }
        }
    }
    val lifecycleCallback = EventCallback()
    override val softInputMode = mutableStateOf(SoftInputMode.adjustNothing)
    override val resolver = Resolver()
    override val notifier = NotificationService{id, arg ->
        when(id){
            WirelessViewModelInterface.lifecycleEvent->{
                lifecycleCallback.onEvent(arg as? Lifecycle.Event?: return@NotificationService)
            }
            "contacts"->{
                fetchContacts()
            }
        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    private fun fetchContacts() {
        val p = android.Manifest.permission.READ_CONTACTS
        viewModelScope.launch(Dispatchers.Main) {
            val ok = permissionHandler.check(p)?.allPermissionsGranted?:false
            if(!ok){
                val okk = permissionHandler.request(p)?.all { it.value }?:false
                if(!okk){
                    lifecycleCallback.oneShot(Lifecycle.Event.ON_RESUME){
                        fetchContacts()
                    }
                    navigation.scope { navHostController, lifecycleOwner, activityService ->
                        activityService?.myAppSettingsPage()
                    }
                    return@launch
                }
            }
            navigation.scope { navHostController, lifecycleOwner, activityService ->
                viewModelScope.launch(Dispatchers.IO) {
                    val cts = suitNamePhoneEmailImage(activityService?.context()?:return@launch)
                    contacts.clear()
                    contacts.addAll(cts)
                }
            }
        }
    }

    override val navigation = Navigation()
    override val permissionHandler = PermissionHandler()
    override val resultingActivityHandler = ResultingActivityHandler()

    private val contacts = mutableStateListOf<MergedContact>()
    init {
        resolver.addAll(
            "contacts" to contacts
        )
    }
}
