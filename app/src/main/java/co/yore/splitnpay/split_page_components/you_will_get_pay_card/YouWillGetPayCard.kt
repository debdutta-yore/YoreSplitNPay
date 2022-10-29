package co.yore.splitnpay.split_page_components.you_will_get_pay_card

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.yore.splitnpay.libs.LocalNotificationService
import co.yore.splitnpay.libs.NotificationService
import co.yore.splitnpay.components.components.YoreAmount
import co.yore.splitnpay.components.components.coloredShadow
import co.yore.splitnpay.libs.dep
import co.yore.splitnpay.locals.RobotoText
import co.yore.splitnpay.libs.sep
import co.yore.splitnpay.models.YoreAmountConfiguration
import co.yore.splitnpay.models.YouWillGetPayArrowButtonConfiguration
import co.yore.splitnpay.models.YouWillGetPayCardConfig
import co.yore.splitnpay.ui.theme.robotoFonts



@Composable
fun YouWillGetPayCard(
    config: YouWillGetPayCardConfig,
    whole: String,
    decimal: String,
    active: Boolean,
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
        Content(config,whole,decimal,active)
        CardIcon(config)
        ArrowButton(config)
    }
}

@Composable
fun BoxScope.Content(
    config: YouWillGetPayCardConfig,
    whole: String,
    decimal: String,
    active: Boolean
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
                val computedAmountColor by remember(active) {
                    derivedStateOf {
                        if(!active){
                            config.amountColor
                        }
                        else{
                            if(config.type==YouWillGetPayCardConfig.Type.GET){
                                config.activeGetColor
                            }
                            else{
                                config.activePayColor
                            }
                        }
                    }
                }
                val animatedAmountColor by animateColorAsState(
                    computedAmountColor,
                    animationSpec = tween(700)
                )
                YoreAmount(
                    config = YoreAmountConfiguration(
                        fontSize = config.amountTextSize,
                        fontFamily = robotoFonts,
                        wholeFontWeight = FontWeight.Bold,
                        currencyFontSize = config.currencyTextSize,
                        decimalFontSize = config.decimalTextSize,
                        color = animatedAmountColor
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


