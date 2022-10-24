package co.yore.splitnpay.components.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import co.yore.splitnpay.R
import co.yore.splitnpay.addmembers.FontFamilyText
import co.yore.splitnpay.libs.dep
import co.yore.splitnpay.libs.sep
import co.yore.splitnpay.ui.theme.DarkBlue
import co.yore.splitnpay.ui.theme.Pink
import coil.compose.AsyncImage

@Composable
fun Category_rhu0vy(
    text: String,
    icon: Any?,
    config: CategorySelectorCardConfiguration = CategorySelectorCardConfiguration(),
    contentDescription: String
) {
    Row(
        modifier = Modifier
            .semantics { this.contentDescription = contentDescription }
            .height(config.height.dep()),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = icon,
            contentDescription = "",
            modifier = Modifier
                .padding(
                    top = config.iconTopPadding.dep(),
                    bottom = config.iconBottomPadding.dep(),
                    start = config.iconStartPadding.dep()
                )
                .size(16.67.dep()),
        )
        //////////////
        Spacer(modifier = Modifier.width(config.iconTextSpacer.dep()))

        FontFamilyText(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = text,
            fontSize = config.fontSize.sep(),
            color = config.textColor,
            fontWeight = config.fontWeight
        )

        Spacer(modifier = Modifier.width(config.iconEndSpacer.dep()))
    }
}

data class CategorySelectorCardConfiguration(
    val height: Float = 26f,
    val icon: Int = R.drawable.ic_trip,
    val fontSize: Float = 11f,
    val textColor: Color = DarkBlue,
    val fontWeight: FontWeight = FontWeight(400),
    val iconTopPadding: Float = 4.53f,
    val iconBottomPadding: Float = 6.19f,
    val iconStartPadding: Float = 9.53f,
    val iconTextSpacer: Float = 3.19f,
    val iconEndSpacer: Float = 21f
) {
    companion object {
        val variation1 = CategorySelectorCardConfiguration(
            textColor = Pink,
            fontWeight = FontWeight.Bold,
            fontSize = 12f,
            iconTopPadding = 0f,
            iconBottomPadding = 0f,
            iconStartPadding = 0f,
            iconTextSpacer = 3.67f,
            iconEndSpacer = 0f
        )
    }
}
