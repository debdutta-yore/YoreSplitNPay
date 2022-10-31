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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import co.yore.splitnpay.R
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.libs.jerokit.NotificationService
import co.yore.splitnpay.libs.jerokit.dep
import co.yore.splitnpay.libs.jerokit.notifier
import co.yore.splitnpay.libs.jerokit.sep
import co.yore.splitnpay.models.*
import co.yore.splitnpay.ui.theme.Ghost80
import co.yore.splitnpay.ui.theme.RadicalRed
import co.yore.splitnpay.ui.theme.Turquoise1

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
            _selected || isPressed || isCheckBoxPressed
        }
    }

    val computedBackgroundColor by remember(config) {
        derivedStateOf{
            if (focused) {
                config.cardBackgroundColor
            } else {
                config.cardUnselectedColor
            }
        }
    }
    val animatedBackgroundColor by animateColorAsState(
        targetValue = computedBackgroundColor,
        animationSpec = tween(700)
    )
    val computedBorderStroke by remember(config) {
        derivedStateOf{
            if (focused) {
                config.borderStroke
            } else {
                0f
            }
        }
    }
    val animatedBorderStroke by animateFloatAsState(
        targetValue = computedBorderStroke,
        animationSpec = tween(700)
    )
    val computedStrokeColor by remember(config) {
        derivedStateOf {
            if (focused){
                config.borderColor
            } else {
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
            data.willGet > 0f || data.willPay > 0f
        }
    }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(config.cardHeight.dep())
            .coloredShadow(
                color = Ghost80,
                borderRadius = config.cornerRadius.dep(),
                blurRadius = config.blurRadius.dep(),
                spread = config.blurSpread,
                offsetX = config.blurOffsetX.dep(),
                offsetY = config.blurOffsetY.dep()
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
                        if (showAmount) {
                            Modifier.width(85.dep())
                        } else {
                            Modifier.weight(1f)
                        }
                    )
                    .align(Alignment.CenterVertically)
            ) {
                FontFamilyText(
                    text = data.name,
                    fontSize = 12.sep(),
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.darkblue),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(5.dep()))
                FontFamilyText(
                    text = data.mobile,
                    fontSize = 11.sep(),
                    fontWeight = FontWeight.Normal,
                    color = colorResource(R.color.lightblue5),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
            if (showAmount){
                Spacer(
                    modifier = Modifier
                        .width(10.dep())
                        .weight(1f)
                )
            }
            Column(
                modifier = Modifier
                    .align(Alignment.Top)
                    .padding(top = 7.dep())
                    .wrapContentWidth(),
                horizontalAlignment = Alignment.End
            ) {
                if (data.willGet > 0) {
                    val willGetSplitted by remember(data.willGet) {
                        derivedStateOf {
                            data.willGet.splitted()
                        }
                    }
                    YoreAmount(
                        config = YoreAmountConfiguration(
                            color = Turquoise1,
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
                            color = RadicalRed,
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
            if (config.checkable){
                CheckBoxIcon_b6qwbf(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    selected = if (selected) TriState.CHECKED else TriState.UNCHECKED,
                    contentDescription = checkBoxContentDescription,
                    onClick = {
                        notifier.notify(DataIds.checkItem, data.id)
                    },
                    onPressed = {
                        isCheckBoxPressed = it
                    }
                )
                14.sx()
            } else {
                ArrowButton_ohezqf(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    contentDescription = "",
                    pressed = focused,
                    onClicked = {
                        notifier.notify(DataIds.peopleCardGo, data)
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
