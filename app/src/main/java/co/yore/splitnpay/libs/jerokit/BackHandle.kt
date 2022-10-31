package co.yore.splitnpay.libs.jerokit

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import co.yore.splitnpay.libs.jerokit.NotificationService
import co.yore.splitnpay.libs.jerokit.notifier
import co.yore.splitnpay.models.DataIds

@Composable
fun BackHandle(
    suffix: String,
    notifier: NotificationService = notifier()
) {
    BackHandler(enabled = true, onBack = {
        notifier.notify("${DataIds.back}$suffix", null)
    })
}
