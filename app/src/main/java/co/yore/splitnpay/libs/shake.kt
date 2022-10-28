package co.yore.splitnpay.libs

import androidx.compose.animation.core.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.debugInspectorInfo

fun Modifier.shake(enabled: Boolean) = composed(

    factory = {
        val depx = with(LocalDensity.current){12.dep().toPx()}
        val scale by animateFloatAsState(
            targetValue = if (enabled) .9f else 1f,
            animationSpec = repeatable(
                iterations = Int.MAX_VALUE,
                animation = tween(durationMillis = 2000, easing = LinearEasing, delayMillis = 1000),
                repeatMode = RepeatMode.Reverse
            )
        )

        Modifier.graphicsLayer {
            translationX = scale*depx
            //scaleX = if (enabled) scale else 1f
            //scaleY = if (enabled) scale else 1f
        }
    },
    inspectorInfo = debugInspectorInfo {
        name = "shake"
        properties["enabled"] = enabled
    }
)