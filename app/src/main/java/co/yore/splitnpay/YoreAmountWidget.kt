package co.yore.splitnpay

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle

data class YoreAmountConfiguration(
    val fontSize: Number,
    val fontFamily: FontFamily = robotoFonts,
    val color: Color,
    val fontWeight: FontWeight = FontWeight.Normal,
    val decimalPointLocationInDecimalPart: Boolean = true,
    val negative: Boolean = false,

    val currencyFontSize: Number? = null,
    val currencyFontFamily: FontFamily? = null,
    val currencyColor: Color? = null,
    val currencyBaselineShift: Number? = null,
    val currencyFontWeight: FontWeight? = null,

    val wholeFontSize: Number? = null,
    val wholeFontFamily: FontFamily? = null,
    val wholeColor: Color? = null,
    val wholeFontWeight: FontWeight? = null,

    val decimalFontSize: Number? = null,
    val decimalFontFamily: FontFamily? = null,
    val decimalColor: Color? = null,
    val decimalFontWeight: FontWeight? = null,
){
    companion object{
        val splitPageHeadContent = YoreAmountConfiguration(
            fontSize = 30,
            color = Color.White,
            wholeFontWeight = FontWeight.Bold,
            currencyFontSize = 21,
            decimalFontSize = 14,
            currencyBaselineShift = -0.2f
        )

        val splitGroupCardGet = YoreAmountConfiguration(
            color = Color(0xff37D8CF),
            fontSize = 14,
            currencyFontSize = 12,
            decimalFontSize = 10,
            wholeFontWeight = FontWeight.Bold
        )

        val splitGroupCardPay = YoreAmountConfiguration(
            color = Color(0xffFF4077),
            fontSize = 14,
            currencyFontSize = 12,
            decimalFontSize = 10,
            wholeFontWeight = FontWeight.Bold,
            negative = true
        )
    }
    val _currencyFontWeight: FontWeight
    get(){
        return currencyFontWeight?:fontWeight
    }
    val _wholeFontWeight: FontWeight
    get(){
        return wholeFontWeight?:fontWeight
    }
    val _decimalFontWeight: FontWeight
    get(){
        return decimalFontWeight?:fontWeight
    }
    val _currencyFontSize: Number
    get(){
        return currencyFontSize?:fontSize
    }
    val _currencyFontFamily: FontFamily
    get(){
        return currencyFontFamily?:fontFamily
    }
    val _currencyColor: Color
    get(){
        return currencyColor?:color
    }
    val _wholeFontSize: Number
    get(){
        return wholeFontSize?:fontSize
    }
    val _wholeFontFamily: FontFamily
    get(){
        return wholeFontFamily?:fontFamily
    }
    val _wholeColor: Color
    get(){
        return wholeColor?:color
    }
    val _decimalFontSize: Number
    get(){
        return decimalFontSize?:fontSize
    }
    val _decimalFontFamily: FontFamily
    get(){
        return decimalFontFamily?:fontFamily
    }
    val _decimalColor: Color
    get(){
        return decimalColor?:color
    }
    val _currencyBaselineShift: Float
    get(){
        return (currencyBaselineShift?:0).toFloat()
    }
}

@Composable
fun YoreAmount(
    modifier: Modifier = Modifier,
    config: YoreAmountConfiguration,
    whole: String,
    decimal: String,
){
    Text(
        modifier = modifier,
        text = buildAnnotatedString {
            withStyle(
                SpanStyle(
                    fontSize = config._currencyFontSize.sep(),
                    fontFamily = config._currencyFontFamily,
                    color = config._currencyColor,
                    baselineShift = BaselineShift(config._currencyBaselineShift),
                    fontWeight = config._currencyFontWeight
                )
            ){
                if(config.negative){
                    append("-")
                }
                append(localCurrency.current)
                append(" ")
            }
            withStyle(
                SpanStyle(
                    fontSize = config._wholeFontSize.sep(),
                    fontFamily = config._wholeFontFamily,
                    color = config._wholeColor,
                    fontWeight = config._wholeFontWeight
                )
            ){
                append(whole)
                if(!config.decimalPointLocationInDecimalPart){
                    append(".")
                }
            }
            withStyle(
                SpanStyle(
                    fontSize = config._decimalFontSize.sep(),
                    fontFamily = config._decimalFontFamily,
                    color = config._decimalColor,
                    fontWeight = config._decimalFontWeight
                )
            ){
                if(config.decimalPointLocationInDecimalPart){
                    append(".")
                }
                append(decimal)
            }
        }
    )
}