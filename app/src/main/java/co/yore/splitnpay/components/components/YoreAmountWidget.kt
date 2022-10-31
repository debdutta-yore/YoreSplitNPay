package co.yore.splitnpay.components.components

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import co.yore.splitnpay.libs.locals.localCurrency
import co.yore.splitnpay.libs.jerokit.sep
import co.yore.splitnpay.models.YoreAmountConfiguration

@Composable
fun YoreAmount(
    modifier: Modifier = Modifier,
    config: YoreAmountConfiguration,
    whole: String,
    decimal: String
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
                if (config.negative){
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
                if (!config.decimalPointLocationInDecimalPart){
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
                if (config.decimalPointLocationInDecimalPart){
                    append(".")
                }
                append(decimal)
            }
        }
    )
}
