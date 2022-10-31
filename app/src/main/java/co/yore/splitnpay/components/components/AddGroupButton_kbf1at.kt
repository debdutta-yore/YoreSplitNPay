package co.yore.splitnpay.components.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import co.yore.splitnpay.libs.jerokit.NotificationService
import co.yore.splitnpay.libs.clickable
import co.yore.splitnpay.libs.jerokit.dep
import co.yore.splitnpay.libs.jerokit.notifier
import co.yore.splitnpay.models.AddGroupButtonConfiguration
import co.yore.splitnpay.models.DataIds
import co.yore.splitnpay.ui.theme.RadicalRed

@Composable
fun AddGroupButton_kbf1at(
    config: AddGroupButtonConfiguration = AddGroupButtonConfiguration(),
    notifier: NotificationService = notifier()
) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(color = config.backgroundColor)
            .size(config.size.dep())
            .clickable(
                rippleColor = RadicalRed
            ) {
                notifier.notify(DataIds.addGroup, null)
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = config.icon),
            contentDescription = "",
            tint = Color.Unspecified
        )
    }
}
