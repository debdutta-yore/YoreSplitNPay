package co.yore.splitnpay.components.configuration

import androidx.compose.foundation.background
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
import co.yore.splitnpay.R
import co.yore.splitnpay.dep
import co.yore.splitnpay.ui.theme.LightRedButton

data class AddGroupButtonConfiguration(
    val backgroundColor: Color = LightRedButton,
    val size: Float = 40f,
    val icon: Int = R.drawable.ic_group
)

@Composable
fun AddGroupButton_kbf1at(
    config: AddGroupButtonConfiguration = AddGroupButtonConfiguration()
) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(color = config.backgroundColor)
            .size(config.size.dep())
            .clickable {  },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = config.icon),
            contentDescription = "",
            tint = Color.Unspecified
        )
    }
}