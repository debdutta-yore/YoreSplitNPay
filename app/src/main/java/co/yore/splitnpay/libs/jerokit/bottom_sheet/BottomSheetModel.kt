package co.yore.splitnpay.libs.jerokit.bottom_sheet

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import co.yore.splitnpay.libs.jerokit.LocalNotificationService
import co.yore.splitnpay.libs.jerokit.LocalResolver
import co.yore.splitnpay.libs.jerokit.NotificationService
import co.yore.splitnpay.libs.jerokit.Resolver
import kotlinx.coroutines.CoroutineScope

interface BottomSheetModel{
    val resolver: Resolver
    val notifier: NotificationService
    val scope: CoroutineScope

    @Composable fun Content()
    fun initialize()
    fun clear()
    fun onBack()

    @Composable
    operator fun invoke(){
        LaunchedEffect(key1 = Unit){
            initialize()
        }
        CompositionLocalProvider(
            LocalResolver provides resolver,
            LocalNotificationService provides notifier
        ) {
            Content()
        }
    }
}
