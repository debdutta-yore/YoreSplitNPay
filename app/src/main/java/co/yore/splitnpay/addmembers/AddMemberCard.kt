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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import co.yore.splitnpay.R
import co.yore.splitnpay.components.components.YoreAmount
import co.yore.splitnpay.components.components.coloredShadow
import co.yore.splitnpay.friend_item.ArrowButton_ohezqf
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.models.*
import co.yore.splitnpay.ui.theme.GreyShadow
import co.yore.splitnpay.ui.theme.robotoFonts
import coil.compose.AsyncImage
import coil.request.ImageRequest


@Composable
fun ProfileImage_2hf7q0(
    image: Any?,
    config: ProfileImageConfiguration2 = ProfileImageConfiguration2(),
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



@Composable
fun CheckBoxIcon_b6qwbf(
    modifier: Modifier = Modifier,
    selected: TriState,
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
            if (selected==TriState.CHECKED)
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
            if (selected==TriState.CHECKED) 0f else 1f
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
        if(selected==TriState.INTERMEDIATE){
            Icon(
                modifier = Modifier
                    .align(Center),
                painter = painterResource(R.drawable.square),
                contentDescription = "check icon",
                tint = config.iconColor
            )
        }
        else{
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
fun PeopleCard_eq3k8h(
    modifier: Modifier = Modifier,
    data: ContactData,
    selected: Boolean = false,
    contentDescription: String,
    checkBoxContentDescription: String,
    profileImageContentDescription: String,
    config: PeopleCardConfiguration = PeopleCardConfiguration(),
    notifier: NotificationService = notifier()
) {
    var _selected by remember {
        mutableStateOf(selected)
    }
    LaunchedEffect(key1 = selected){
        _selected = selected
    }
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    var isCheckBoxPressed by remember(selected) {
        mutableStateOf(selected)
    }

    val focused by remember {
        derivedStateOf {
            _selected||isPressed||isCheckBoxPressed
        }
    }

    val computedBackgroundColor by remember(config) {
        derivedStateOf{
            if (focused)
                config.cardBackgroundColor 
            else 
                config.cardUnselectedColor
        }
    }
    val animatedBackgroundColor by animateColorAsState(
        targetValue = computedBackgroundColor,
        animationSpec = tween(700)
    )
    val computedBorderStroke by remember(config) {
        derivedStateOf{
            if (focused)
                config.borderStroke
            else
                0f
        }
    }
    val animatedBorderStroke by animateFloatAsState(
        targetValue = computedBorderStroke,
        animationSpec = tween(700)
    )
    val computedStrokeColor by remember(config) {
        derivedStateOf {
            if(focused){
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
    val showAmount by remember(data) {
        derivedStateOf {
            data.willGet>0f||data.willPay>0f
        }
    }
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
                notifier.notify(DataIds.checkItem, data.id)
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
                .fillMaxWidth()
        ) {
            ProfileImage_2hf7q0(
                contentDescription = profileImageContentDescription,
                image = data.image
            )

            Column(
                modifier = Modifier
                    .padding(start = 22.dep())
                    .then(
                        if (showAmount)
                            Modifier.width(85.dep())
                        else
                            Modifier.weight(1f)
                    )
                    .align(Alignment.CenterVertically),
            ) {
                FontFamilyText(
                    text = data.name,
                    fontSize = 12.sep(),
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.darkblue),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
                Spacer(modifier = Modifier.height(5.dep()))
                FontFamilyText(
                    text = data.mobile,
                    fontSize = 11.sep(),
                    fontWeight = FontWeight.Normal,
                    color = colorResource(R.color.lightblue5),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
            }
            if(showAmount){
                Spacer(modifier = Modifier
                    .width(10.dep())
                    .weight(1f))
            }
            Column(
                modifier = Modifier
                    .align(Alignment.Top)
                    .padding(top = 7.dep())
                    .wrapContentWidth(),
                horizontalAlignment = Alignment.End,
            ) {
                if (data.willGet > 0) {
                    val willGetSplitted by remember(data.willGet) {
                        derivedStateOf {
                            data.willGet.splitted()
                        }
                    }
                    YoreAmount(
                        config = YoreAmountConfiguration(
                            color = Color(0xff37D8CF),
                            fontSize = 14,
                            currencyFontSize = 12,
                            decimalFontSize = 10,
                            wholeFontWeight = FontWeight.Bold
                        ),
                        whole = willGetSplitted.wholeString,
                        decimal = willGetSplitted.decString
                    )
                }
                if (data.willPay > 0) {
                    val willPaySplitted by remember(data.willPay) {
                        derivedStateOf {
                            data.willPay.splitted()
                        }
                    }
                    YoreAmount(
                        config = YoreAmountConfiguration(
                            color = Color(0xffFF4077),
                            fontSize = 14,
                            currencyFontSize = 12,
                            decimalFontSize = 10,
                            wholeFontWeight = FontWeight.Bold,
                            negative = true
                        ),
                        whole = willPaySplitted.wholeString,
                        decimal = willPaySplitted.decString
                    )
                }
            }
            25.sx()
            if(config.checkable){
                CheckBoxIcon_b6qwbf(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    selected = if(selected) TriState.CHECKED else TriState.UNCHECKED,
                    contentDescription = checkBoxContentDescription,
                    onClick = {
                        notifier.notify(DataIds.checkItem, data.id)
                    },
                    onPressed = {
                        isCheckBoxPressed = it
                    }
                )
                14.sx()
            }
            else{
                ArrowButton_ohezqf(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    contentDescription = "",
                    pressed = focused,
                    onClicked = {
                        notifier.notify(DataIds.peopleCardGo,data)
                    },
                    onPressed = {
                        isCheckBoxPressed = it
                    }
                )
                14.sx()
            }
        }
    }
}