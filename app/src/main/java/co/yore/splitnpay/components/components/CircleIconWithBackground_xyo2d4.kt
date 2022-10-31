package co.yore.splitnpay.components.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import co.yore.splitnpay.libs.jerokit.dep
import co.yore.splitnpay.models.*

@Composable
fun CircleIconWithBackground_xyo2d4(
    icon: Int,
    config: CircleIconWithBackgroundConfiguration = CircleIconWithBackgroundConfiguration()
) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(config.backgroundColor)
            .size(config.iconBackgroundSize.dep()),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier
                .size(config.categoryIconSize.dep()),
            painter = painterResource(id = icon),
            contentDescription = "",
            tint = Color.Unspecified
        )
    }
}
