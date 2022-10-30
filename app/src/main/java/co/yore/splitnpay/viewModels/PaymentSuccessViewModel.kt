package co.yore.splitnpay.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.models.DataIds
import co.yore.splitnpay.models.StatusBarColor
import co.yore.splitnpay.ui.theme.Shark

class PaymentSuccessViewModel() : ViewModel(), WirelessViewModelInterface {
    override val softInputMode = mutableStateOf(SoftInputMode.adjustNothing)
    override val resolver = Resolver()
    override val navigation = Navigation()
    override val permissionHandler = PermissionHandler()
    override val resultingActivityHandler = ResultingActivityHandler()

    // ////////////////////////////////////////
    private val _paymentSuccessMessage = mutableStateOf("Request Successful")
    private val _paymentSuccessButtonText = mutableStateOf("View Summary")
    private val _paymentSuccessBackText = mutableStateOf("Split Group")
    private val _statusBarColor = mutableStateOf<StatusBarColor?>(null)

    // ////////////////////////////////////////
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
            DataIds.paymentSuccessButtonClick -> {
                navigation.scope { navHostController, lifecycleOwner, toaster ->
                    // TODO: Navigate
//                    navHostController.navigate("")
                }
            }
        }
    }

    // ///////////////////////////////////////
    init {
        setUpResolver()
        setUpStatusBarColor()
    }

    private fun setUpStatusBarColor() {
        _statusBarColor.value = StatusBarColor(
            color = Shark,
            darkIcons = false
        )
    }

    private fun setUpResolver() {
        resolver.addAll(
            DataIds.statusBarColor to _statusBarColor,
            DataIds.paymentSuccessMessage to _paymentSuccessMessage,
            DataIds.paymentSuccessButtonText to _paymentSuccessButtonText,
            DataIds.paymentSuccessBackText to _paymentSuccessBackText
        )
    }
}
