package co.yore.splitnpay

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.snapshots.SnapshotStateList

class Resolver{
    private val map: MutableMap<Int,Any?> = mutableMapOf()
    fun <T>get(key: Int): T{
        return map[key] as T
    }

    fun set(key: Int, value: Any?){
        map[key] = value
    }
}

data class NotificationService(
    val notify: (Any,Any?)->Unit
)
val LocalResolver = compositionLocalOf { Resolver() }

val LocalNotificationService = compositionLocalOf { NotificationService{ _, _->} }

@Composable
fun stringState(key: Int): State<String> {
    return LocalResolver.current.get(key)
}
@Composable
fun boolState(key: Int): State<Boolean> {
    return LocalResolver.current.get(key)
}
@Composable
fun <T>listState(key: Int): SnapshotStateList<T> {
    return LocalResolver.current.get(key)
}
@Composable
fun <T>tState(key: Int): State<T> {
    return LocalResolver.current.get(key)
}

@Composable
fun notifier():NotificationService{
    return LocalNotificationService.current
}