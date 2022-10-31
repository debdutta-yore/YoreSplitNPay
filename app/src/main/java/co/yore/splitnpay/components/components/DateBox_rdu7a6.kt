package co.yore.splitnpay.components.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import co.yore.splitnpay.libs.jerokit.dep
import co.yore.splitnpay.libs.jerokit.sep
import co.yore.splitnpay.models.*

@Composable
fun DateBox_rdu7a6(date: String, config: DateBoxConfiguration = DateBoxConfiguration()) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(config.backgroundColor)
    ) {
        Text(
            text = date,
            color = config.textColor,
            fontSize = config.fontSize.sep(),
            lineHeight = config.lineHeight.sep(),
            modifier = Modifier
                .padding(
                    vertical = config.textInnerVerticalPadding.dep(),
                    horizontal = config.textInnerHorizontalPadding.dep()
                )
        )
    }

}
