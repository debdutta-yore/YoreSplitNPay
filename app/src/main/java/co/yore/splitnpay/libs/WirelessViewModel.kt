package co.yore.splitnpay.libs

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.navigation.NavHostController
import co.yore.splitnpay.models.Sheets
import co.yore.splitnpay.pages.BackHandle
import co.yore.splitnpay.pages.elseLet

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
@OptIn(ExperimentalMaterialApi::class)
val LocalSheetHandler = compositionLocalOf { SheetHandler(
    initialValue = ModalBottomSheetValue.Hidden,
    skipHalfExpanded = true,
    confirmStateChange = { false },
    onVisibilityChange = {}
) }
@Composable
fun localSheetHandler(): SheetHandler{
    return LocalSheetHandler.current
}
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
fun floatState(key: Any): State<Float> {
    return LocalResolver.current.get(key)
}
@Composable
fun doubleState(key: Any): State<Double> {
    return LocalResolver.current.get(key)
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

val LocalSheetMap = compositionLocalOf { mapOf<Sheets,BottomSheetModel>() }
val LocalSheeting = compositionLocalOf { Sheeting() }

@Composable
fun sheetMap(): Map<Sheets, BottomSheetModel>{
    return LocalSheetMap.current
}
@Composable
fun sheeting(): Sheeting{
    return LocalSheeting.current
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Sheeting.sheetContent(){
    val animDuration = remember{ 1000 }
    Box(
        modifier = Modifier
            .animateContentSize()
            .fillMaxWidth()
            .wrapContentHeight()
    ){
        AnimatedContent(
            targetState = this@sheetContent.sheets.value,
            transitionSpec = {
                fadeIn(
                    animationSpec = tween(animDuration)
                ) +
                        scaleIn(
                            initialScale = 1f,
                            animationSpec = tween(animDuration)
                        ) with
                        fadeOut(
                            animationSpec = tween(animDuration)
                        )
            }
        ) {
            this@sheetContent[it]
        }
    }
}

class Sheeting @OptIn(ExperimentalMaterialApi::class) constructor(
    private val skipHalfExpanded: Boolean = true,
    private val sheetMap: Map<Sheets,BottomSheetModel> = emptyMap(),
    private val onVisibilityChanged: (Boolean)->Unit = {},
    confirmStateChange: (ModalBottomSheetValue)->Boolean = {true}
){
    private val _sheets = mutableStateOf(Sheets.None)
    val sheets get() = _sheets
    val map get() = sheetMap
    fun model(sheet: Sheets) = sheetMap[sheet]
    @Composable
    operator fun get(sheet: Sheets){
        return sheetMap[sheet].elseLet(elseBlock = { Text("") }) { it() }
    }

    @OptIn(ExperimentalMaterialApi::class)
    private val _sheetHandler = SheetHandler(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = skipHalfExpanded,
        confirmStateChange = confirmStateChange,
        onVisibilityChange = onVisibilityChanged
    )
    val sheetHandler get() = _sheetHandler
    @OptIn(ExperimentalMaterialApi::class)
    fun hide(){
        sheetHandler.state {
            hide()
        }
    }
    @OptIn(ExperimentalMaterialApi::class)
    fun show(){
        sheetHandler.state {
            show()
        }
    }

    fun change(sheet: Sheets){
        _sheets.value = sheet
    }
}

interface WirelessViewModelInterface{
    val resolver: Resolver
    val notifier: NotificationService
    val navigation: MutableState<UIScope?>
    val permissionHandler: PermissionHandler
    val resultingActivityHandler: ResultingActivityHandler
    val sheeting: Sheeting get() = Sheeting()
    val mySheeting get() = sheeting
    companion object{
        const val startupNotification = -10000
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
    wvm.resultingActivityHandler.handle()
    LaunchedEffect(key1 = Unit){
        wvm.notifier.notify(WirelessViewModelInterface.startupNotification,null)
    }
    val owner = LocalLifecycleOwner.current
    LaunchedEffect(key1 = wvm.navigation.value){
        wvm.navigation.forward(navController,owner)
    }
    CompositionLocalProvider(
        LocalResolver provides wvm.resolver,
        LocalNotificationService provides wvm.notifier,
        LocalSheetHandler provides wvm.sheeting.sheetHandler,
        LocalSheeting provides wvm.sheeting
    ) {
        StatusBarColorControl()
        content()
        BackHandle(suffix)
    }
}


