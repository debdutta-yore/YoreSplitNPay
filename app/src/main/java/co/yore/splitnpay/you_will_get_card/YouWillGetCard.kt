package co.yore.splitnpay.you_will_get_card

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import co.yore.splitnpay.*
import co.yore.splitnpay.R
import java.math.RoundingMode
import java.text.DecimalFormat

data class FloatSplitted(
    val whole: String,
    val dec: String
)

fun Float.splitted(): FloatSplitted{
    val text = this.toString()
    val parts = text.split(".")
    var wholeText = parts[0]
    var decText = parts[1]
    val whole = wholeText.toInt()
    if(whole<10){
        wholeText = "0$whole"
    }
    if(decText.length<2){
        decText = "${decText}0"
    }
    if(decText.length>2){
        decText = decText.substring(0..1)
    }
    return FloatSplitted(
        wholeText,
        decText
    )
}

data class YouWillPayCardConfig(
    val height: Float = 153f,
    val width: Float = 149f,
    val shadowColor: Color = Color(0x80C6CFD8),
    val borderRadius: Float = 15f,
    val shadowBlurRadius: Float = 33f,
    val shadowOffset: Float = 7f,
    val youWillPayTopPadding: Float = 22f,
    val youWillPayStringId:  Int = R.string.you_will_pay,
    val youWillPayFontSize: Int = 16,
    val decimalTextSize: Int = 12,
    val amountTextSize: Int = 20,
    val youWillPayIconLeftPadding: Float = 27f,
    val amountTopPadding: Float = 6f,
    val currencyTextSize: Float = 12f,
    val arrowButtonRightPadding: Float = 10f,
    val arrowButtonBottomPadding: Float = 8f,
)

data class YouWillGetCardConfig(
    val height: Float = 153f,
    val width: Float = 149f,
    val shadowColor: Color = Color(0x80C6CFD8),
    val borderRadius: Float = 15f,
    val shadowBlurRadius: Float = 33f,
    val shadowOffset: Float = 7f,
    val youWillGetTopPadding: Float = 22f,
    val youWillGetStringId:  Int = R.string.you_will_get,
    val youWillGetFontSize: Int = 16,
    val decimalTextSize: Int = 12,
    val amountTextSize: Int = 20,
    val youWillGetIconLeftPadding: Float = 33f,
    val amountTopPadding: Float = 6f,
    val currencyTextSize: Float = 12f,
    val arrowButtonRightPadding: Float = 10f,
    val arrowButtonBottomPadding: Float = 8f,
)

data class ArrowButtonConfiguration(
    val arrowButtonSize: Float = 26f,
)

data class YouWillPayIconConfig(
    val youWillPayIconWidth: Float = 46f,
    val youWillPayIconHeight: Float = 62f,
)

@Composable
fun ArrowButton(
    config: ArrowButtonConfiguration = ArrowButtonConfiguration()
){
    Icon(
        modifier = Modifier
            .size(config.arrowButtonSize.dep())
            .clip(CircleShape)
            .clickable { },
        painter = painterResource(id = R.drawable.ic_debited_arrow),
        contentDescription = "pay split icon",
        tint = Color.Unspecified
    )
}

@Composable
fun GetArrowButton(
    config: ArrowButtonConfiguration = ArrowButtonConfiguration()
){
    Icon(
        modifier = Modifier
            .size(config.arrowButtonSize.dep())
            .clip(CircleShape)
            .clickable { },
        painter = painterResource(id = R.drawable.ic_you_will_get_arrow),
        contentDescription = "pay split icon",
        tint = Color.Unspecified
    )
}

@Composable
fun YouWillPayCard(
    config: YouWillPayCardConfig = YouWillPayCardConfig(),
    whole: String,
    decimal: String
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

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Column(
                modifier = Modifier
                    .padding(top = config.youWillPayTopPadding.dep())
            ){
                RobotoText(
                    modifier = Modifier,
                    text = stringResource(config.youWillPayStringId),
                    fontSize = config.youWillPayFontSize.sep(),
                    fontWeight = FontWeight.Bold,
                )
                Box(
                    modifier = Modifier
                        .padding(top = config.amountTopPadding.dep() - 2.dp)
                        .align(Alignment.End)
                ){
                    val wholeText = whole
                    val decimalText = decimal

                    val currencyScript = SpanStyle(
                        baselineShift = BaselineShift.None,
                        fontSize = config.currencyTextSize.sep()
                    )

                    val decimalScript = SpanStyle(
                        baselineShift = BaselineShift.None,
                        fontSize = config.decimalTextSize.sep(),
                        fontWeight = FontWeight.Normal
                    )
                    Text(
                        text = buildAnnotatedString {
                            withStyle(currencyScript) {
                                append(localCurrency.current)
                                append(" ")
                            }
                            append(wholeText)
                            withStyle(decimalScript) {
                                append(".")
                                append(decimalText)
                            }
                        },
                        fontSize = config.amountTextSize.sep(),
                        color = colorResource(id = R.color.splitGreyCard),
                        fontFamily = robotoFonts,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Box(
            Modifier
            .padding(start = config.youWillPayIconLeftPadding.dep())
            .align(Alignment.BottomStart)
        ){
            YouWillPayIcon()
        }



        Box(
            modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(
                end = config.arrowButtonRightPadding.dep(),
                bottom = config.arrowButtonBottomPadding.dep()
            )
        ){
            ArrowButton()
        }
    }
}

@Composable
fun YouWillGetCard(
    config: YouWillGetCardConfig = YouWillGetCardConfig(),
    whole: String,
    decimal: String
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

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Column(
                modifier = Modifier
                    .padding(top = config.youWillGetTopPadding.dep())
            ){
                RobotoText(
                    modifier = Modifier,
                    text = stringResource(config.youWillGetStringId),
                    fontSize = config.youWillGetFontSize.sep(),
                    fontWeight = FontWeight.Bold,
                )
                Box(
                    modifier = Modifier
                        .padding(top = config.amountTopPadding.dep() - 2.dp)
                        .align(Alignment.End)
                ){
                    val wholeText = whole
                    val decimalText = decimal

                    val currencyScript = SpanStyle(
                        baselineShift = BaselineShift.None,
                        fontSize = config.currencyTextSize.sep()
                    )

                    val decimalScript = SpanStyle(
                        baselineShift = BaselineShift.None,
                        fontSize = config.decimalTextSize.sep(),
                        fontWeight = FontWeight.Normal
                    )
                    Text(
                        text = buildAnnotatedString {
                            withStyle(currencyScript) {
                                append(localCurrency.current)
                                append(" ")
                            }
                            append(wholeText)
                            withStyle(decimalScript) {
                                append(".")
                                append(decimalText)
                            }
                        },
                        fontSize = config.amountTextSize.sep(),
                        color = colorResource(id = R.color.splitGreyCard),
                        fontFamily = robotoFonts,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Box(
            Modifier
                .padding(start = config.youWillGetIconLeftPadding.dep())
                .align(Alignment.BottomStart)
        ){
            YouWillGetIcon()
        }



        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    end = config.arrowButtonRightPadding.dep(),
                    bottom = config.arrowButtonBottomPadding.dep()
                )
        ){
            GetArrowButton()
        }
    }
}



@Composable
fun YouWillPayIcon(
    config: YouWillPayIconConfig = YouWillPayIconConfig()
) {
    Icon(
        modifier = Modifier
            .width(config.youWillPayIconWidth.dep())
            .height(config.youWillPayIconHeight.dep())
            .clickable { },
        painter = painterResource(id = R.drawable.you_will_pay_hand_icon),
        contentDescription = "pay split icon",
        tint = Color.Unspecified
    )
}
@Composable
fun YouWillGetIcon(
    config: YouWillPayIconConfig = YouWillPayIconConfig()
) {
    Icon(
        modifier = Modifier
            .width(config.youWillPayIconWidth.dep())
            .height(config.youWillPayIconHeight.dep())
            .clickable { },
        painter = painterResource(id = R.drawable.you_will_get_icon),
        contentDescription = "pay split icon",
        tint = Color.Unspecified
    )
}
