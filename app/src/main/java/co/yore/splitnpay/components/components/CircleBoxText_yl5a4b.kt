package co.yore.splitnpay.components.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import co.yore.splitnpay.libs.jerokit.dep
import co.yore.splitnpay.libs.jerokit.sep
import co.yore.splitnpay.models.*

@Composable
fun CircleBoxText_yl5a4b(
    text: String,
    config: CircleBoxTextConfiguration = CircleBoxTextConfiguration()
) {
    Box(
        modifier = Modifier
            .padding(start = config.boxStartPadding.dep())
            .height(config.boxHeight.dep())
            .clip(RoundedCornerShape(100.dep()))
            .background(color = config.backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        FontFamilyText(
            text = text,
            fontSize = config.fontSize.sep(),
            color = config.textColor,
            lineHeight = config.lineHeight.sep(),
            modifier = Modifier.padding(
                horizontal = config.textInnerHorizontalPadding.dep()
            ),
            letterSpacing = config.letterSpacing.sep()
        )
    }
}
