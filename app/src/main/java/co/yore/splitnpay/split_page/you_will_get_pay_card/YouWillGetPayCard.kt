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
    val type: Type
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
    onClick: ()->Unit,
    onArrowClick: ()->Unit
) {
    Box(
        modifier = Modifier
            .height(config.width.dep())
            .width(config.width.dep())
            .coloredShadow(
                color = config.shadowColor,
                borderRadius = config.borderRadius.dep(),
                blurRadius = config.shadowBlurRadius.dep(),
                spread = 0f,
                offsetX = config.shadowOffset.dep(),
                offsetY = config.shadowOffset.dep(),
            )
            .clip(RoundedCornerShape(config.borderRadius.dep()))
            .background(Color.White)
            .clickable {
                onClick()
            }

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Column(
                modifier = Modifier
                    .padding(top = config.headingTopPadding.dep())
            ){
                val headingStringId by remember {
                    derivedStateOf {
                        if(config.type==YouWillGetPayCardConfig.Type.GET){
                            config.youWillGetStringId
                        }
                        else{
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
                AmountUI(
                    amountTopPadding = config.amountTopPadding,
                    currencyTextSize = config.currencyTextSize,
                    decimalTextSize = config.decimalTextSize,
                    whole = whole,
                    decimal = decimal,
                    amountTextSize = config.amountTextSize
                )
            }
        }

        Box(
            Modifier
                .padding(start = config.youWillGetIconLeftPadding.dep())
                .align(Alignment.BottomStart)
        ){
            if(config.type==YouWillGetPayCardConfig.Type.GET){
                YouWillGetIcon()
            }
            else {
                YouWillPayIcon()
            }
        }



        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    end = config.arrowButtonRightPadding.dep(),
                    bottom = config.arrowButtonBottomPadding.dep()
                )
        ){
            val arrowButtonConfig by remember {
                derivedStateOf {
                    if(config.type==YouWillGetPayCardConfig.Type.GET){
                        YouWillGetPayArrowButtonConfiguration.Get
                    }
                    else{
                        YouWillGetPayArrowButtonConfiguration.Pay
                    }
                }
            }
            YouWillGetPayArrowButton(
                arrowButtonConfig,
                contentDescription = "you_will_${config.type}_arrow_button"
            ){
                onArrowClick()
            }
        }
    }
}


