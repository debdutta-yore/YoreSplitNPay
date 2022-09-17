package co.yore.splitnpay

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight

enum class SplitPageTab {
    All,
    YouOwe,
    YouAreOwed
}

data class SplitTabItemConfiguration(
    val minWidth: Float = 48f,
    val roundedCorner: Float = 13.5f,
    val selectedBackground: Color = Color(0xff1A79E5),
    val unSelectedBackground: Color = Color(0xffEDF5FF),
    val paddingStart: Float = 9f,
    val paddingEnd: Float = 8f,
    val paddingTop: Float = 7f,
    val paddingBottom: Float = 7f,
    val textSize: Float = 11f,
    val selectedTextColor: Color = White,
    val unSelectedTextColor: Color = Color(0xff1A79E5),
    val fontWeight: FontWeight = FontWeight(400)
)

@Composable
fun SplitTabItem_89keto(
    text: String,
    current: SplitPageTab,
    selected: SplitPageTab,
    contentDescription: String,
    config: SplitTabItemConfiguration = SplitTabItemConfiguration(),
    onClick: () -> Unit,
) {
    val computedBackgroundColor by remember {
        derivedStateOf{
            if(selected == current)
                config.selectedBackground
            else
                config.unSelectedBackground
        }
    }
    val computedColor by remember {
        derivedStateOf{
            if (selected == current)
                config.selectedTextColor
            else
                config.unSelectedTextColor
        }
    }
    Box(
        modifier = Modifier
            .semantics { this.contentDescription = contentDescription }
            .widthIn(config.minWidth.dep())
            .clip(RoundedCornerShape(config.roundedCorner.dep()))
            .background(computedBackgroundColor)
            .clickable {
                onClick()
            }
            .padding(
                top = config.paddingTop.dep(),
                bottom = config.paddingBottom.dep(),
                start = config.paddingStart.dep(),
                end = config.paddingEnd.dep()
            ),
        contentAlignment = Alignment.Center
    ) {
        RobotoText(
            text = text,
            fontSize = config.textSize.sep(),
            color = computedColor,
            fontWeight = config.fontWeight
        )
    }
}