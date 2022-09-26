package co.yore.splitnpay.split_page.you_will_get_pay_card

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import co.yore.splitnpay.R
import co.yore.splitnpay.components.configuration.YouWillPayGetIconConfig
import co.yore.splitnpay.libs.dep

@Composable
fun YouWillGetIcon(
    config: YouWillPayGetIconConfig = YouWillPayGetIconConfig()
) {
    Icon(
        modifier = Modifier
            .width(config.youWillPayIconWidth.dep())
            .height(config.youWillPayIconHeight.dep()),
        painter = painterResource(id = R.drawable.you_will_get_icon),
        contentDescription = "pay split icon",
        tint = Color.Unspecified
    )
}