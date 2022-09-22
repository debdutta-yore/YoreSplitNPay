package co.yore.splitnpay

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.snapshots.SnapshotStateList

class Resolver{
    private val map: MutableMap<Any,Any?> = mutableMapOf()
    fun <T>get(key: Any): T{
        return map[key] as T
    }

    operator fun set(key: Any, value: Any?){
        map[key] = value
    }
}

data class NotificationService(
    val notify: (Any,Any?)->Unit
)
val LocalResolver = compositionLocalOf { Resolver() }

val LocalNotificationService = compositionLocalOf { NotificationService{ _, _->} }

@Composable
fun stringState(key: Any): State<String> {
    return LocalResolver.current.get(key)
}
@Composable
fun boolState(key: Any): State<Boolean> {
    return LocalResolver.current.get(key)
}
@Composable
fun <T>listState(key: Any): SnapshotStateList<T> {
    return LocalResolver.current.get(key)
}
@Composable
fun <T>tState(key: Any): State<T> {
    return LocalResolver.current.get(key)
}

@Composable
fun notifier():NotificationService{
    return LocalNotificationService.current
}