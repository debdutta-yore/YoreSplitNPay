package co.yore.splitnpay.split_page.you_will_get_pay_card

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.yore.splitnpay.*
import co.yore.splitnpay.R

data class YouWillGetPayCardConfig(
    val height: Float = 153f,
    val width: Float = 149f,
    val shadowColor: Color = Color(0x80C6CFD8),
    val borderRadius: Float = 15f,
    val shadowBlurRadius: Float = 33f,
    val shadowOffset: Float = 7f,
    val headingTopPadding: Float = 22f,
    val youWillGetStringId:  Int = R.string.you_will_get,
    val youWillPayStringId:  Int = R.string.you_will_pay,
    val headingFontSize: Int = 16,
    val decimalTextSize: Float = 12f,
    val amountTextSize: Float = 20f,
    val youWillGetIconLeftPadding: Float = 33f,
    val amountTopPadding: Float = 6f,
    val currencyTextSize: Float = 12f,
    val arrowButtonRightPadding: Float = 10f,
    val arrowButtonBottomPadding: Float = 8f,
    val type: Type,
    val amountColor: Color = Color(0xff839BB9),
    val shadowSpread: Float = 0f,
    val backgroundColor: Color = Color.White,
){
    enum class Type{
        GET,
        PAY
    }
}

@Composable
fun YouWillGetPayCard(
    config: YouWillGetPayCardConfig,
    whole: String,
    decimal: String,
    notifier: NotificationService = LocalNotificationService.current
) {
    Box(
        modifier = Modifier
            .height(config.width.dep())
            .width(config.width.dep())
            .coloredShadow(
                color = config.shadowColor,
                borderRadius = config.borderRadius.dep(),
                blurRadius = config.shadowBlurRadius.dep(),
                spread = config.shadowSpread,
                offsetX = config.shadowOffset.dep(),
                offsetY = config.shadowOffset.dep(),
            )
            .clip(RoundedCornerShape(config.borderRadius.dep()))
            .background(config.backgroundColor)
            .clickable {
                notifier.notify("you_will${config.type.name}_big_card", null)
            }

    ) {
        Content(config,whole,decimal)
        CardIcon(config)
        ArrowButton(config)
    }
}

@Composable
fun BoxScope.Content(
    config: YouWillGetPayCardConfig,
    whole: String,
    decimal: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .padding(top = config.headingTopPadding.dep())
        ) {
            val headingStringId by remember {
                derivedStateOf {
                    if (config.type == YouWillGetPayCardConfig.Type.GET) {
                        config.youWillGetStringId
                    } else {
                        config.youWillPayStringId
                    }
                }
            }
            RobotoText(
                modifier = Modifier,
                text = stringResource(headingStringId),
                fontSize = config.headingFontSize.sep(),
                fontWeight = FontWeight.Bold,
            )

            Box(
                modifier = Modifier
                    .padding(top = config.amountTopPadding.dep() - 2.dp)
                    .align(Alignment.End)
            ) {
                YoreAmount(
                    config = YoreAmountConfiguration(
                        fontSize = config.amountTextSize,
                        fontFamily = robotoFonts,
                        wholeFontWeight = FontWeight.Bold,
                        currencyFontSize = config.currencyTextSize,
                        decimalFontSize = config.decimalTextSize,
                        color = config.amountColor
                    ),
                    whole = whole,
                    decimal = decimal
                )
            }

        }
    }
}

@Composable
private fun BoxScope.CardIcon(
    config: YouWillGetPayCardConfig
) {
    Box(
        Modifier
            .padding(start = config.youWillGetIconLeftPadding.dep())
            .align(Alignment.BottomStart)
    ) {
        if (config.type == YouWillGetPayCardConfig.Type.GET) {
            YouWillGetIcon()
        } else {
            YouWillPayIcon()
        }
    }
}

@Composable
private fun BoxScope.ArrowButton(
    config: YouWillGetPayCardConfig,
    notifier: NotificationService = LocalNotificationService.current
) {
    Box(
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(
                end = config.arrowButtonRightPadding.dep(),
                bottom = config.arrowButtonBottomPadding.dep()
            )
    ) {
        val arrowButtonConfig by remember {
            derivedStateOf {
                if (config.type == YouWillGetPayCardConfig.Type.GET) {
                    YouWillGetPayArrowButtonConfiguration.Get
                } else {
                    YouWillGetPayArrowButtonConfiguration.Pay
                }
            }
        }
        YouWillGetPayArrowButton(
            arrowButtonConfig,
            contentDescription = "you_will_${config.type}_arrow_button"
        ) {
            notifier.notify("you_will_${config.type}_arrow_button", null)
        }
    }
}


