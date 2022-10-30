package co.yore.splitnpay.components.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import co.yore.splitnpay.libs.dep
import co.yore.splitnpay.models.YouWillGetPayArrowButtonConfiguration


@Composable
fun YouWillGetPayArrowButton(
    config: YouWillGetPayArrowButtonConfiguration,
    contentDescription: String,
    onClick: ()->Unit
){
    Icon(
        modifier = Modifier
            .size(config.arrowButtonSize.dep())
            .clip(CircleShape)
            .clickable {
                onClick()
            },
        painter = painterResource(config.icon),
        contentDescription = contentDescription,
        tint = Color.Unspecified
    )
}