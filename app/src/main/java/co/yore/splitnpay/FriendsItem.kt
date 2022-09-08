package co.yore.splitnpay

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImage

data class ProfileImageConfiguration(
    val imageBorderColor: Color = Color(0xffEDF5FF),
    val imageUrl: String = "https://i.pravatar.cc/300",
    val imageContainerSize: Float = 49f,
    val imageSize: Float = 47f,
    val strokeWidth: Float = 3f
)

@Composable
fun ProfileImage(
    contentDescription: String,
    config: ProfileImageConfiguration = ProfileImageConfiguration()
){

    Box(
        Modifier
            .semantics {
                this.contentDescription = contentDescription
            }
            .size(config.imageContainerSize.dep())
            .clip(
                CircleShape
            )
            .border(
                BorderStroke(config.strokeWidth.dep(), config.imageBorderColor),
                CircleShape
            ),
        contentAlignment = Alignment.Center
    ){
        AsyncImage(
            transition = CrossfadeTransition.Factory,
            placeholder = painterResource(id = R.drawable.person),
            model = config.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .size(config.imageSize.dep())
                .clip(
                    CircleShape
                )
        )
    }
}
data class ArrowButtonConfiguration(
    val iconBackgroundColor: Color = Color(0xff839BB9),
    val iconPressedColor: Color = Color(0xff1A79E5),
    val iconTint: Color = Color.White,
    val iconButtonRadius: Float = 9f,
    val iconButtonSize: Float = 28f,
    val iconSize: Float = 11f,
    val iconResource: Int = R.drawable.ic_nextarrow,
)
@Composable
fun ArrowButton(
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

data class FriendItemConfiguration(
    val shadowColor: Color = Color(0x80c6cfd8),
    val backgroundPressedColor: Color = Color(0xffEDF5FF),
    val backgroundColor: Color = Color.White,
    val strokeColor: Color = Color(0xff1A79E5),
    val noStrokeColor: Color = Color(0x00000000),
    val borderRadius: Float = 22f,
    val blurRadius: Float = 33f,
    val shadowOffset: Float = 7f,
    val cardPaddingX : Float= 17f,
    val cardPaddingY : Float= 17f,
    val rowPadding: Float = 23f,
    val imageLeftPadding: Float = 18f,
    val imageRightPadding: Float = 22f,
    val iconButtonRightPadding: Float = 17f,
    val imageBorderStroke: Float = 1f,
    val fullWidth: Float = 360f
)

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
                borderRadius = config.borderRadius.dep(config.fullWidth),
                blurRadius = config.blurRadius.dep(),
                offsetX = config.shadowOffset.dep(),
                offsetY = config.shadowOffset.dep()
            )
            .padding(
                start = config.cardPaddingX.dep(),
                end = config.cardPaddingX.dep(),
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
                    fontFamily = fonts
                )
                Text(
                    text = friend.mobile,
                    fontSize = 11.sep(),
                    color = colorResource(
                        R.color.lightblue5,
                    ),
                    fontFamily = fonts
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