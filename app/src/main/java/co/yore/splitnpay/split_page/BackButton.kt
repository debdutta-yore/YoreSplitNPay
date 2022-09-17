package co.yore.splitnpay.split_page

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import co.yore.splitnpay.R
import co.yore.splitnpay.dep

data class SplitBackButtonConfiguration(
    val boxSize: Number = 24,
    val iconSize: Number = 12,
    val iconId: Int = R.drawable.ic_left_chevron,
    val tint: Color = Color.White,
)

@Composable
fun BackButton(
    config: SplitBackButtonConfiguration = SplitBackButtonConfiguration(),
    contentDescription: String,
    onClick: ()->Unit
) {
    Box(
        modifier = Modifier
            .semantics {
                this.contentDescription = contentDescription
            }
            .size(config.boxSize.dep())
            .clip(CircleShape)
            .clickable {
                       onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.size(config.iconSize.dep()),
            painter = painterResource(id = config.iconId),
            contentDescription = "",
            tint = config.tint
        )
    }
}