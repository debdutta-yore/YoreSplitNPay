package co.yore.splitnpay.components.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import co.yore.splitnpay.components.configuration.SplitTabItemConfiguration
import co.yore.splitnpay.libs.dep
import co.yore.splitnpay.libs.sep

enum class SplitPageTabs {
    All,
    YouOwe,
    YouAreOwed
}



@Composable
fun SplitTabItem_89keto(
    text: String,
    selected: SplitPageTabs,
    currentTab: SplitPageTabs,
    contentDescription: String,
    config: SplitTabItemConfiguration = SplitTabItemConfiguration(),
    onClick: () -> Unit,
) {
    val computedBackgroundColor by remember(selected) {
        derivedStateOf {
            if (selected == currentTab)
                config.selectedBackground
            else
                config.unSelectedBackground
        }
    }
    val computedColor by remember(selected) {
        derivedStateOf {
            if (selected == currentTab)
                config.selectedTextColor
            else
                config.unSelectedTextColor
        }
    }
    val animatedBackgroundColor by animateColorAsState(
        targetValue = computedBackgroundColor,
        animationSpec = tween(1000)
    )
    val animatedColor by animateColorAsState(
        targetValue = computedColor,
        animationSpec = tween(1000)
    )
    Box(
        modifier = Modifier
            .semantics { this.contentDescription = contentDescription }
            .widthIn(config.minWidth.dep())
            .clip(RoundedCornerShape(config.roundedCorner.dep()))
            .background(animatedBackgroundColor)
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
        Text(
            text = text,
            fontSize = config.textSize.sep(),
            color = animatedColor,
            fontWeight = config.fontWeight
        )
    }
}