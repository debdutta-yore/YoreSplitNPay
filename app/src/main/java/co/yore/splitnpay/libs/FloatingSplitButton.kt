package co.yore.splitnpay.libs

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import co.yore.splitnpay.components.components.coloredShadow
import co.yore.splitnpay.components.configuration.SplitButtonConfiguration
import co.yore.splitnpay.models.DataIds

@Composable
fun FloatingSplitButton(
    config: SplitButtonConfiguration = SplitButtonConfiguration(),
    notifier: NotificationService = notifier()
) {
    FloatingActionButton(
        onClick = {
            notifier.notify(DataIds.split,null)
        },
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