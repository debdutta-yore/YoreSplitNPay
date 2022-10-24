package co.yore.splitnpay.components.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import co.yore.splitnpay.addmembers.FontFamilyText
import co.yore.splitnpay.libs.dep
import co.yore.splitnpay.libs.sep
import co.yore.splitnpay.pages.LightBlue5
import co.yore.splitnpay.pages.Whitish6
import co.yore.splitnpay.ui.theme.LightGreen3
import co.yore.splitnpay.ui.theme.LightRedButton
import co.yore.splitnpay.ui.theme.Pink

@Composable
fun YouWillGetDetailsSingleTabItem_7ozj5w(
    selected: Boolean,
    onClick: () -> Unit,
    item: String,
    config: YouWillGetDetailsSingleTabItemConfiguration = YouWillGetDetailsSingleTabItemConfiguration(),
    contentDescription: String
) {

    val optionColor = animateColorAsState(
        targetValue =
        if (selected) config.selectedOptionBackground
        else config.unSelectedOptionBackground
    )
    val optionTextColor = animateColorAsState(
        targetValue =
        if (selected) config.selectedOptionTextColor
        else config.unSelectedOptionTextColor
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .semantics { this.contentDescription = contentDescription }
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null
            ) {
                onClick()
            }
            .clip(RoundedCornerShape(config.cornerRadius.dep()))
            .background(
                color = optionColor.value
            )
    ) {
        FontFamilyText(
            modifier = Modifier
                .padding(
                    top = config.textTopPadding.dep(),
                    bottom = config.textBottomPadding.dep(),
                    start = config.textStartPadding.dep(),
                    end = config.textEndPadding.dep()
                ),
            text = item,
            color = optionTextColor.value,
            fontSize = config.fontSize.sep()
        )
    }
}

data class YouWillGetDetailsSingleTabItemConfiguration(
    val cornerRadius: Float = 50f,
    val selectedOptionBackground: Color = Whitish7,
    val unSelectedOptionBackground: Color = Whitish6,
    val selectedOptionTextColor: Color = LightGreen3,
    val unSelectedOptionTextColor: Color = LightBlue5,
    val fontSize: Float = 9f,
    val textTopPadding: Float = 3f,
    val textBottomPadding: Float = 3f,
    val textStartPadding: Float = 9f,
    val textEndPadding: Float = 9f
) {
    companion object {
        val variationOne = YouWillGetDetailsSingleTabItemConfiguration(
            selectedOptionBackground = LightRedButton,
            unSelectedOptionBackground = Whitish6,
            selectedOptionTextColor = Pink,
            unSelectedOptionTextColor = LightBlue5

        )
    }

}
