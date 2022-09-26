package co.yore.splitnpay

import android.annotation.SuppressLint
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import co.yore.splitnpay.components.components.coloredShadow
import co.yore.splitnpay.split_page.SplitPage

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SplitPageFull() {
    Scaffold(
        floatingActionButton = {
            SplitButton()
        }
    ) {
        SplitPage()
    }
}

data class SplitButtonConfiguration(
    val backgroundColor: Color = Color(0xffFF4077),
    val tint: Color = Color.White,
    val shadowColor: Color = Color(0x4fff4077),
    val shadowBorderRadius: Float = 100f,
    val shadowBlurRadius: Float = 12f,
    val shadowSpread: Float = 0f,
    val offsetX: Float = 0f,
    val offsetY: Float = 3f,
    val iconId: Int = R.drawable.ic_split_white
)

@Composable
fun SplitButton(
    config: SplitButtonConfiguration = SplitButtonConfiguration(),
    notifier: NotificationService = notifier()
) {
    FloatingActionButton(
        onClick = {notifier.notify("split_page_button",null)},
        backgroundColor = config.backgroundColor,
        contentColor = config.tint,
        modifier = Modifier
            .coloredShadow(
                color = config.shadowColor,
                borderRadius = config.shadowBorderRadius.dep(),
                blurRadius = config.shadowBlurRadius.dep(),
                spread = config.shadowSpread,
                offsetX = config.offsetX.dep(),
                offsetY = config.offsetY.dep(),
            ),
        shape = CircleShape,
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            hoveredElevation = 0.dp,
            focusedElevation = 0.dp,
        )
    ) {
        Icon(
            painter = painterResource(id = config.iconId),
            "", tint = Color.Unspecified
        )
    }
}
