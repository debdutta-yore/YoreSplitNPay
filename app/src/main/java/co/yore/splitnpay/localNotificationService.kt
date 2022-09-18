package co.yore.splitnpay

import androidx.compose.runtime.compositionLocalOf

data class NotificationService(
    val notify: (Int,Any?)->Unit
)
val yoreNotificationService = compositionLocalOf { NotificationService{ _, _->} }