package co.yore.splitnpay.split_page.you_will_get_pay_card

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import co.yore.splitnpay.R
import co.yore.splitnpay.dep

data class YouWillGetPayArrowButtonConfiguration(
    val arrowButtonSize: Float = 26f,
    val icon: Int
){
    companion object{
        val Get: YouWillGetPayArrowButtonConfiguration
            get() = YouWillGetPayArrowButtonConfiguration(icon = R.drawable.ic_you_will_get_arrow)
        val Pay: YouWillGetPayArrowButtonConfiguration
            get() = YouWillGetPayArrowButtonConfiguration(icon = R.drawable.ic_you_will_pay_arrow)
    }
}

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