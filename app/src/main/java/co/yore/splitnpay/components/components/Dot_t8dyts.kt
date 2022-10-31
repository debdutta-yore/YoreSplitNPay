package co.yore.splitnpay.components.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import co.yore.splitnpay.libs.jerokit.dep
import co.yore.splitnpay.models.*

@Composable
fun Dot_t8dyts(config: DotConfiguration = DotConfiguration()) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(config.color)
            .size(config.size.dep())
    )
}
