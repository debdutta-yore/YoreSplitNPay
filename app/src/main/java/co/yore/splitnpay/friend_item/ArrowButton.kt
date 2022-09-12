package co.yore.splitnpay.friend_item
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import co.yore.splitnpay.dep
import co.yore.splitnpay.friend_item.models.ArrowButtonConfiguration

@Composable
fun ArrowButton_ohezqf(
    contentDescription: String,
    config: ArrowButtonConfiguration = ArrowButtonConfiguration(),
    pressed: Boolean,
    onClicked: ()->Unit,
    onPressed: (Boolean)->Unit,
){
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    LaunchedEffect(key1 = isPressed){
        onPressed(isPressed)
    }

    IconButton(
        interactionSource = interactionSource,
        modifier = Modifier
            .semantics {
                this.contentDescription = contentDescription
            }
            .clip(RoundedCornerShape(config.iconButtonRadius.dep()))
            .background(
                if (!(isPressed || pressed)) config.iconBackgroundColor else config.iconPressedColor
            )
            .size(config.iconButtonSize.dep()),
        onClick = onClicked,
    ) {
        Icon(
            modifier = Modifier.size(config.iconSize.dep()),
            painter = painterResource(id = config.iconResource),
            contentDescription = "",
            tint = config.iconTint
        )
    }
}