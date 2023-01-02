package co.yore.splitnpay.libs.jerokit

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.libs.jerokit.bottom_sheet.LocalSheeting

@Composable
fun YorePage(
    navController: NavHostController,
    suffix: String,
    wvm: WirelessViewModelInterface,
    content: @Composable () -> Unit
) {
    wvm.permissionHandler.handlePermission()
    wvm.resultingActivityHandler.handle()
    LaunchedEffect(key1 = Unit){
        wvm.notifier.notify(WirelessViewModelInterface.startupNotification, null)
    }
    val owner = LocalLifecycleOwner.current
    val context = LocalContext.current
    LaunchedEffect(key1 = wvm.navigation.value){
        wvm.navigation.forward(navController, owner, ActivityService(context))
    }
    // /////////
    val activity = LocalContext.current as Activity
    LaunchedEffect(key1 = wvm.softInputMode.value) {
        activity.window.setSoftInputMode(wvm.softInputMode.value)
    }
    // /////////
    CompositionLocalProvider(
        LocalResolver provides wvm.resolver,
        LocalNotificationService provides wvm.notifier,
        LocalSheetHandler provides wvm.sheeting.sheetHandler,
        LocalSheeting provides wvm.sheeting
    ) {
        OnLifecycleEvent{owner, event ->
            wvm.notifier.notify(WirelessViewModelInterface.lifecycleEvent, event)
        }
        StatusBarColorControl()
        content()
        BackHandle(suffix)
    }
}

