package co.yore.splitnpay.pages

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import co.yore.splitnpay.R
import co.yore.splitnpay.addmembers.FontFamilyText
import co.yore.splitnpay.demos.sx
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.models.DataIds
import co.yore.splitnpay.ui.theme.Bluish
import co.yore.splitnpay.ui.theme.LightBlue4
import co.yore.splitnpay.ui.theme.LightGreen3

@Composable
fun IndividualSettingsCard(
    modifier: Modifier = Modifier,
    notifier: NotificationService = notifier()
) {
    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 18.dep(),
                    bottom = 18.dep()
                )
        ) {
            SingleSetting(
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = painterResource(id = R.drawable.ic_person_add),
                text = "Add to a group",
                trailingIcon = painterResource(id = R.drawable.menu_right_arrow),
                onClick = {
                    notifier.notify(DataIds.individualAddToGroup)
                }
            )
            SingleSetting(
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = painterResource(id = R.drawable.ic_notification),
                text = "Mute notifications",
                trailingIcon = painterResource(id = R.drawable.menu_right_arrow),
                onClick = {
                    notifier.notify(DataIds.individualNotificationSwitch)
                },
                switchable = true,
            )
            SingleSetting(
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = painterResource(id = R.drawable.ic_block),
                text = "Block this user",
                trailingIcon = painterResource(id = R.drawable.menu_right_arrow),
                switchable = false,
                onClick = {
                    notifier.notify(DataIds.individualBlockUser)
                }
            )
        }
    }
}

@Composable
fun SingleSettingV1(
    modifier: Modifier = Modifier,
    leadingIcon: Painter, text: String,
    trailingIcon: Painter,
    isThereSwitch: Boolean = false,
    onClick: () -> Unit,
    notifier: NotificationService = notifier(),
    groupNotificationSwitch: Boolean = boolState(key = DataIds.groupNotificationSwitch).value,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    onClick()
                }
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(24.dep()),
                painter = leadingIcon,
                tint = Color.Unspecified,
                contentDescription = "leadingIcon"
            )
            36.sx()
            FontFamilyText(
                text = text,
                color = LightBlue5,
                fontSize = 14.sep()
            )
        }
        if (isThereSwitch) {
            CustomSwitchV1(
                scale = 1f,
                width = 36.dep(),
                height = 20.dep(),
                strokeWidth = 1.dep(),
                checkedTrackColor = LightGreen3,
                uncheckedTrackColor = LightBlue4,
                checkedBackgroundColor = LightGreenSwitchBackground,
                uncheckedBackgroundColor = LightBlue1,
                gapBetweenThumbAndTrackEdge = 3.dep(),
                onSwitch = {
                    notifier.notify(DataIds.groupNotificationSwitch)
                },
                switchON = groupNotificationSwitch
            )
        } else {
            Icon(
                modifier = Modifier.size(14.dep()),
                painter = trailingIcon,
                tint = Bluish,
                contentDescription = "trailingIcon"
            )
        }
    }
}

@Composable
fun CustomSwitchV1(
    switchON: Boolean,
    scale: Float = 2f,
    width: Dp = 36.dep(),
    height: Dp = 20.dep(),
    strokeWidth: Dp = 2.dep(),
    checkedTrackColor: Color = LightGreen3,
    uncheckedTrackColor: Color = LightBlue4,
    checkedBackgroundColor: Color = LightGreenSwitchBackground,
    uncheckedBackgroundColor: Color = LightBlue1,
    gapBetweenThumbAndTrackEdge: Dp = 4.dep(),
    onSwitch: () -> Unit
) {
    val thumbRadius = (height / 2) - gapBetweenThumbAndTrackEdge
    // To move thumb, we need to calculate the position (along x axis)
    val animatePosition = animateFloatAsState(
        targetValue = if (switchON)
            with(LocalDensity.current) { (width - thumbRadius - gapBetweenThumbAndTrackEdge).toPx() }
        else
            with(LocalDensity.current) { (thumbRadius + gapBetweenThumbAndTrackEdge).toPx() }
    )

    val backgroundColor =
        animateColorAsState(targetValue = if (switchON) checkedBackgroundColor else uncheckedBackgroundColor)
    val trackColor =
        animateColorAsState(targetValue = if (switchON) checkedTrackColor else uncheckedTrackColor)

    Canvas(
        modifier = Modifier
            .size(width = width, height = height)
            .scale(scale = scale)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        onSwitch()
                        // This is called when the user taps on the canvas
//                        switchON = !switchON
                    }
                )
            }
    ) {
        drawRoundRect(
            color = backgroundColor.value,
            cornerRadius = CornerRadius(x = 10.dp.toPx(), y = 10.dp.toPx()),
            style = Fill
        )
        // Track
        drawRoundRect(
            color = trackColor.value,
            cornerRadius = CornerRadius(x = 10.dp.toPx(), y = 10.dp.toPx()),
            style = Stroke(width = strokeWidth.toPx())
        )
        // Thumb
        drawCircle(
            color = trackColor.value,
            radius = thumbRadius.toPx(),
            center = Offset(
                x = animatePosition.value,
                y = size.height / 2
            )
        )
    }
}