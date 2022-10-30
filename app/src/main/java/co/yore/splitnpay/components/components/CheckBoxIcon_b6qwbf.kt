package co.yore.splitnpay.components.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import co.yore.splitnpay.R
import co.yore.splitnpay.libs.dep
import co.yore.splitnpay.models.CheckboxConfiguration
import co.yore.splitnpay.models.TriState

@Composable
fun CheckBoxIcon_b6qwbf(
    modifier: Modifier = Modifier,
    selected: TriState,
    contentDescription: String,
    config: CheckboxConfiguration = CheckboxConfiguration(),
    onPressed: (Boolean) -> Unit,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    LaunchedEffect(key1 = isPressed){
        onPressed(isPressed)
    }
    val computedBackgroundColor by remember(selected, config) {
        derivedStateOf{
            if (selected == TriState.CHECKED) {
                config.iconColor
            } else {
                Color.White
            }
        }
    }
    val animatedBackgroundColor by animateColorAsState(
        targetValue = computedBackgroundColor,
        animationSpec = tween(
            durationMillis = 700
        )
    )
    val computedBorderStroke by remember(selected) {
        derivedStateOf {
            if (selected == TriState.CHECKED) 0f else 1f
        }
    }
    val animatedBorderStroke by animateFloatAsState(
        targetValue = computedBorderStroke,
        animationSpec = tween(700)
    )
    Box(
        modifier = modifier
            .size(config.iconSize.dep())
            .clip(RoundedCornerShape(config.cornerRadius.dep()))
            .background(animatedBackgroundColor)
            .border(
                animatedBorderStroke.dep(),
                color = config.iconColor,
                shape = RoundedCornerShape(10.dep())
            )
            .clickable(
                interactionSource = interactionSource,
                indication = LocalIndication.current
            ) {
                onClick()
            }
            .semantics { this.contentDescription = contentDescription }
    ) {
        if (selected == TriState.INTERMEDIATE){
            Icon(
                modifier = Modifier
                    .align(Center),
                painter = painterResource(R.drawable.square),
                contentDescription = "check icon",
                tint = config.iconColor
            )
        } else {
            Icon(
                modifier = Modifier
                    .align(Center),
                painter = painterResource(config.icon),
                contentDescription = "check icon",
                tint = Color.White
            )
        }
    }
}
