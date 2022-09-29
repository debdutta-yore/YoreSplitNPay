package co.yore.splitnpay.libs

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.navigation.NavHostController
import co.yore.splitnpay.pages.BackHandle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

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
    val callback: (Any,Any?)->Unit
){
    fun notify(id: Any, arg: Any? = null){
        callback(id,arg)
    }
}
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
fun safeBoolState(key: Any): State<Boolean>? {
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
fun <T>safeListState(key: Any): SnapshotStateList<T>? {
    return LocalResolver.current.get(key)
}
@Composable
fun <T,E>mapState(key: Any): SnapshotStateMap<T,E> {
    return LocalResolver.current.get(key)
}
@Composable
fun <T,E>safeMapState(key: Any): SnapshotStateMap<T,E>? {
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
fun notifier(): NotificationService {
    return LocalNotificationService.current
}

interface WirelessViewModelInterface{
    val resolver: Resolver
    val notifier: NotificationService
    val navigation: MutableState<UIScope?>
    val permissionHandler: PermissionHandler
    companion object{
        const val startupNotification = -10000
        const val permissions = -10001
        const val requestPermission = -10002
        const val permissionResult = -10003
        const val permissionDisposition = -10004
    }
}

@Composable
fun YorePage(
    navController: NavHostController,
    suffix: String,
    wvm: WirelessViewModelInterface,
    content: @Composable () -> Unit
) {
    wvm.permissionHandler.handlePermission()
    LaunchedEffect(key1 = Unit){
        wvm.notifier.notify(WirelessViewModelInterface.startupNotification,null)
    }
    val owner = LocalLifecycleOwner.current
    LaunchedEffect(key1 = wvm.navigation.value){
        wvm.navigation.forward(navController,owner)
    }
    CompositionLocalProvider(
        LocalResolver provides wvm.resolver,
        LocalNotificationService provides wvm.notifier
    ) {
        StatusBarColorControl()
        content()
        BackHandle(suffix)
    }
}


