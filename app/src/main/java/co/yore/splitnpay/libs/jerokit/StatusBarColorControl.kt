package co.yore.splitnpay.libs.jerokit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import co.yore.splitnpay.models.DataIds
import co.yore.splitnpay.models.StatusBarColor
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun StatusBarColorControl(
    state: StatusBarColor? = safeTState<StatusBarColor>(DataIds.statusBarColor)?.value
) {
    val systemUiController = rememberSystemUiController()

    DisposableEffect(systemUiController, state) {
        state?.let {
            val color = it.color
            val darkIcons = it.darkIcons
            systemUiController.setStatusBarColor(
                color = color,
                darkIcons = darkIcons
            )
        }
        onDispose {}
    }
}
