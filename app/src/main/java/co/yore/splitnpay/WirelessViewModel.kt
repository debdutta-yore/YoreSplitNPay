package co.yore.splitnpay

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.State
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.snapshots.SnapshotStateList

class Resolver{
    private val _map: MutableMap<Any,Any?> = mutableMapOf()
    fun <T>get(key: Any): T{
        return _map[key] as T
    }

    operator fun set(key: Any, value: Any?){
        _map[key] = value
    }

    fun addAll(map: Map<Any,Any?>){
        _map.putAll(map)
    }

    fun addAll(vararg pairs: Pair<Any,Any?>){
        _map.putAll(pairs)
    }
}

data class NotificationService(
    val notify: (Any,Any?)->Unit
)
val LocalResolver = compositionLocalOf { Resolver() }
val LocalNotificationService = compositionLocalOf { NotificationService{ _, _->} }

val LocalSuffix = compositionLocalOf { "" }
@Composable
fun suffix(): String{
    return LocalSuffix.current
}

@Composable
fun suffix(
    suffix: String,
    content: @Composable () -> Unit
){
    CompositionLocalProvider(
        values = arrayOf(LocalSuffix provides "${LocalSuffix.current}$suffix"),
        content
    )
}

@Composable
fun stringState(key: Any): State<String> {
    return LocalResolver.current.get(key)
}
@Composable
fun boolState(key: Any): State<Boolean> {
    return LocalResolver.current.get(key)
}
@Composable
fun intState(key: Any): State<Int> {
    return LocalResolver.current.get(key)
}
@Composable
fun <T>listState(key: Any): SnapshotStateList<T> {
    return LocalResolver.current.get(key)
}
@Composable
fun <T>t(key: Any): T {
    return LocalResolver.current.get(key)
}
@Composable
fun <T>tState(key: Any): State<T> {
    return LocalResolver.current.get(key)
}
@Composable
fun <T>safeTState(key: Any): State<T>? {
    return LocalResolver.current.get(key)
}

@Composable
fun notifier():NotificationService{
    return LocalNotificationService.current
}