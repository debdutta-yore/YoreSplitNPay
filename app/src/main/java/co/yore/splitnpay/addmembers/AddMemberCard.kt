package co.yore.splitnpay.addmembers

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import co.yore.splitnpay.*
import co.yore.splitnpay.R
import co.yore.splitnpay.components.components.coloredShadow
import co.yore.splitnpay.ui.theme.Bluish
import co.yore.splitnpay.ui.theme.GreyShadow
import coil.compose.AsyncImage
import coil.request.ImageRequest


data class ProfileImageConfiguration(
    val imageSize: Float = 49f,
    val borderStroke: Float = 3f,
    val borderColor: Color = Color(0xffEDF5FF),
    val shape: Shape = CircleShape,
    val placeholder: Int = R.drawable.user_dummy4,
    val contentScale: ContentScale = ContentScale.Crop
)

@Composable
fun ProfileImage_2hf7q0(
    image: Any?,
    config: ProfileImageConfiguration = ProfileImageConfiguration(),
    contentDescription: String
) {
    AsyncImage(
        modifier = Modifier
            .size(config.imageSize.dep())
            .border(
                width = config.borderStroke.dep(),
                color = config.borderColor,
                shape = config.shape
            )
            .padding(config.borderStroke.dep())
            .clip(CircleShape),
        model = ImageRequest.Builder(LocalContext.current)
            .data(image)
            .crossfade(true)
            .build(),
        placeholder = painterResource(config.placeholder),
        contentScale = config.contentScale,
        contentDescription = contentDescription
    )
}


data class CheckboxConfiguration(
    val iconColor: Color = Bluish,
    val iconSize: Float = 28f,
    val icon: Int = R.drawable.ic_check,
    val cornerRadius: Float = 10f
)
@Composable
fun CheckBoxIcon_b6qwbf(
    selected: Boolean,
    contentDescription: String,
    config: CheckboxConfiguration = CheckboxConfiguration(),
    onPressed: (Boolean)->Unit,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    LaunchedEffect(key1 = isPressed){
        onPressed(isPressed)
    }
    val computedBackgroundColor by remember(selected,config) {
        derivedStateOf{
            if (selected)
                config.iconColor
            else
                Color.White
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
            if (selected) 0f else 1f
        }
    }
    val animatedBorderStroke by animateFloatAsState(
        targetValue = computedBorderStroke,
        animationSpec = tween(700)
    )
    Box(
        modifier = Modifier
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
        Icon(
            modifier = Modifier
                .align(Center),
            painter = painterResource(config.icon),
            contentDescription = "check icon",
            tint = Color.White
        )
    }
}
data class Friend(
    val uid: Int = 0,
    val name: String,
    val mobileNumber: String,
    val imageUrl: String = "https://i.pravatar.cc/300",
    val isSelected: Boolean = false,
    val hasRead: Boolean = false
)
data class AddMemberCardConfiguration(
    val cardHeight: Float = 95f,
    val paddingStart: Float = 18f,
    val paddingEnd: Float = 18f,
    val borderStroke: Float = 1f,
    val borderColor: Color = Bluish,
    val cardBackgroundColor: Color = Color(0xffEDF5FF),
    val cardUnselectedColor: Color = Color.White,
    val cornerRadius: Float = 22f,
    val blurRadius: Float = 33f,
    val blurOffsetX: Float = 7f,
    val blurOffsetY: Float = 7f,
    val blurSpread: Float = 0f,
)

@Composable
fun FontFamilyText(
    modifier: Modifier = Modifier,
    text: String = "",
    annotatedString: AnnotatedString = AnnotatedString(""),
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = robotoFonts,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current
) {
    if(annotatedString.isEmpty()) {
        Text(
            text = text,
            modifier = modifier,
            color = color,
            fontSize = fontSize,
            fontStyle = fontStyle,
            fontWeight = fontWeight,
            fontFamily = fontFamily,
            letterSpacing = letterSpacing,
            textDecoration = textDecoration,
            textAlign = textAlign,
            lineHeight = lineHeight,
            overflow = overflow,
            softWrap = softWrap,
            maxLines = maxLines,
            onTextLayout = onTextLayout,
            style = style
        )
    }
    else {
        Text(
            text = annotatedString,
            modifier = modifier,
            color = color,
            fontSize = fontSize,
            fontStyle = fontStyle,
            fontWeight = fontWeight,
            fontFamily = fontFamily,
            letterSpacing = letterSpacing,
            textDecoration = textDecoration,
            textAlign = textAlign,
            lineHeight = lineHeight,
            overflow = overflow,
            softWrap = softWrap,
            maxLines = maxLines,
            onTextLayout = onTextLayout,
            style = style
        )
    }
}

@Composable
fun AddMemberCard_eq3k8h(
    modifier: Modifier = Modifier,
    member: ContactData,
    selected: Boolean,
    contentDescription: String,
    checkBoxContentDescription: String,
    profileImageContentDescription: String,
    config: AddMemberCardConfiguration = AddMemberCardConfiguration(),
    notifier: NotificationService = notifier()
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    var isCheckBoxPressed by remember(selected) {
        mutableStateOf(selected)
    }

    val computedBackgroundColor by remember(selected,config,isCheckBoxPressed) {
        derivedStateOf{
            if (selected||isPressed||isCheckBoxPressed)
                config.cardBackgroundColor 
            else 
                config.cardUnselectedColor
        }
    }
    val animatedBackgroundColor by animateColorAsState(
        targetValue = computedBackgroundColor,
        animationSpec = tween(700)
    )
    val computedBorderStroke by remember(selected,config,isCheckBoxPressed) {
        derivedStateOf{
            if (selected||isPressed||isCheckBoxPressed)
                config.borderStroke
            else
                0f
        }
    }
    val animatedBorderStroke by animateFloatAsState(
        targetValue = computedBorderStroke,
        animationSpec = tween(700)
    )
    val computedStrokeColor by remember(selected,config,isCheckBoxPressed) {
        derivedStateOf {
            if(selected||isPressed||isCheckBoxPressed){
                config.borderColor
            }
            else{
                Color.Transparent
            }
        }
    }
    val animatedStrokeColor by animateColorAsState(
        targetValue = computedStrokeColor,
        tween(700)
    )
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(config.cardHeight.dep())
            .coloredShadow(
                color = GreyShadow,
                borderRadius = config.cornerRadius.dep(),
                blurRadius = config.blurRadius.dep(),
                spread = config.blurSpread,
                offsetX = config.blurOffsetX.dep(),
                offsetY = config.blurOffsetY.dep(),
            )
            .clip(RoundedCornerShape(config.cornerRadius.dep()))
            .clickable(
                interactionSource = interactionSource,
                indication = LocalIndication.current
            ) {
                notifier.notify(DataIds.checkItem, member.id)
            }
            .border(
                width = animatedBorderStroke.dep(),
                color = animatedStrokeColor,
                shape = RoundedCornerShape(config.cornerRadius.dep())
            )
            .background(animatedBackgroundColor)
            .semantics { this.contentDescription = contentDescription }
    ) {
        Row(
            modifier = Modifier
                .padding(
                    start = 16.dep(),
                    top = 23.dep(),
                    bottom = 23.dep()
                )
        ) {
            ProfileImage_2hf7q0(
                contentDescription = profileImageContentDescription,
                image = member.image
            )

            Column(
                modifier = Modifier
                    .padding(start = 22.dep())
            ) {
                FontFamilyText(
                    text = member.name,
                    fontSize = 12.sep(),
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.darkblue),
                )
                Spacer(modifier = Modifier.height(5.dep()))
                FontFamilyText(
                    text = member.mobile,
                    fontSize = 11.sep(),
                    fontWeight = FontWeight.Normal,
                    color = colorResource(R.color.lightblue5)
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 23.dep()),
            contentAlignment = CenterEnd
        ) {
            CheckBoxIcon_b6qwbf(
                selected = selected,
                contentDescription = checkBoxContentDescription,
                onClick = {
                    notifier.notify(DataIds.checkItem, member.id)
                },
                onPressed = {
                    isCheckBoxPressed = it
                }
            )
        }
    }
}