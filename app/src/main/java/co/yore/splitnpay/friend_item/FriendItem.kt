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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import co.yore.splitnpay.*
import co.yore.splitnpay.R
import co.yore.splitnpay.components.components.coloredShadow
import co.yore.splitnpay.friend_item.models.FriendItemConfiguration
import co.yore.splitnpay.friend_item.models.PeopleData

@Composable
fun FriendItem(
    selected: Boolean,
    peopleData: PeopleData,
    contentDescription: String,
    config: FriendItemConfiguration = FriendItemConfiguration(),
    onPressed: (Boolean) -> Unit = {},
    onClicked: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    var innerPressed by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = isPressed) {
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Text(
                    text = peopleData.name,
                    fontSize = 12.sep(),
                    fontWeight = FontWeight.Bold,
                    color = colorResource(
                        R.color.lightblue2,
                    ),
                    fontFamily = robotoFonts,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = peopleData.mobile,
                    fontSize = 11.sep(),
                    color = colorResource(
                        R.color.lightblue5,
                    ),
                    fontFamily = robotoFonts
                )
            }
            Column(
                modifier = Modifier
                    .align(Alignment.Top)
                    .padding(top = 8.dep())
                    .wrapContentWidth(),
                horizontalAlignment = Alignment.End
            ) {
                if (peopleData.willGet > 0) {
                    val willGetSplitted by remember(peopleData.willGet) {
                        derivedStateOf {
                            peopleData.willGet.splitted()
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
                if (peopleData.willPay > 0) {
                    val willPaySplitted by remember(peopleData.willPay) {
                        derivedStateOf {
                            peopleData.willPay.splitted()
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
            ArrowButton_ohezqf(
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