package co.yore.splitnpay.libs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect

interface BottomSheetModel{
    val resolver : Resolver
    val notifier : NotificationService

    fun initialize()
    fun clear()

    @Composable
    fun provide(
        content: @Composable () -> Unit
    ){
        LaunchedEffect(key1 = Unit){
            initialize()
        }
        CompositionLocalProvider(
            LocalResolver provides resolver,
            LocalNotificationService provides notifier
        ) {
            content()
        }
    }

    fun onBack()
}