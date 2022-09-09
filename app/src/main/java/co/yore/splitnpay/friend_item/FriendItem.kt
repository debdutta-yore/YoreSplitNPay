package co.yore.splitnpay.friend_item

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import co.yore.splitnpay.*
import co.yore.splitnpay.R
import co.yore.splitnpay.friend_item.models.Friend
import co.yore.splitnpay.friend_item.models.FriendItemConfiguration

@Composable
fun FriendItem(
    contentDescription: String,
    selected: Boolean,
    friend: Friend,
    config: FriendItemConfiguration = FriendItemConfiguration(),
    onClicked: () -> Unit,
    onPressed: (Boolean) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    var innerPressed by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = isPressed){
        onPressed(isPressed)
    }
    Box(
        modifier = Modifier
            .semantics {
                this.contentDescription = contentDescription
            }
            .coloredShadow(
                color = config.shadowColor,
                borderRadius = config.borderRadius.dep(),
                blurRadius = config.blurRadius.dep(),
                offsetX = config.shadowOffset.dep(),
                offsetY = config.shadowOffset.dep()
            )
            .padding(
                /*start = config.cardPaddingX.dep(),
                end = config.cardPaddingX.dep(),*/
                top = config.cardPaddingY.dep()
            )
            .clip(
                RoundedCornerShape(config.borderRadius.dep())
            )
            .background(if (!(isPressed || innerPressed || selected)) config.backgroundColor else config.backgroundPressedColor)
            .border(
                BorderStroke(
                    config.imageBorderStroke.dep(),
                    if ((isPressed || innerPressed || selected)) config.strokeColor else config.noStrokeColor
                ),
                shape = RoundedCornerShape(config.borderRadius.dep())
            )
            .clickable(
                interactionSource = interactionSource,
                indication = LocalIndication.current
            ) {
                onClicked()
            },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = config.rowPadding.dep()),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.width(config.imageLeftPadding.dep()))
            ProfileImage(
                contentDescription = "profile_image"
            )
            Spacer(Modifier.width(config.imageRightPadding.dep()))
            Column {
                Text(
                    text = friend.name,
                    fontSize = 12.sep(),
                    fontWeight = FontWeight.Bold,
                    color = colorResource(
                        R.color.lightblue2,
                    ),
                    fontFamily = robotoFonts
                )
                Text(
                    text = friend.mobile,
                    fontSize = 11.sep(),
                    color = colorResource(
                        R.color.lightblue5,
                    ),
                    fontFamily = robotoFonts
                )
            }
            Spacer(Modifier.weight(1f))
            ArrowButton(
                contentDescription = "arrow_button",
                pressed = (isPressed || innerPressed || selected),
                onClicked = {},
                onPressed = {
                    innerPressed = it
                    onPressed(it)
                }
            )
            Spacer(Modifier.width(config.iconButtonRightPadding.dep()))
        }
    }
}