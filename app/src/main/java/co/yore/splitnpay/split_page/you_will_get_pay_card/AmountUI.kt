package co.yore.splitnpay.split_page.you_will_get_pay_card

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import co.yore.splitnpay.*
import co.yore.splitnpay.libs.dep
import co.yore.splitnpay.libs.sep
import co.yore.splitnpay.locals.localCurrency
import co.yore.splitnpay.ui.theme.robotoFonts

@Composable
fun ColumnScope.AmountUI(
    amountTopPadding: Float,
    currencyTextSize: Float,
    decimalTextSize: Float,
    whole: String,
    decimal: String,
    amountTextSize: Float
) {
    Box(
        modifier = Modifier
            .padding(top = amountTopPadding.dep() - 2.dp)
            .align(Alignment.End)
    ){

        val currencyScript = SpanStyle(
            baselineShift = BaselineShift.None,
            fontSize = currencyTextSize.sep()
        )

        val decimalScript = SpanStyle(
            baselineShift = BaselineShift.None,
            fontSize = decimalTextSize.sep(),
            fontWeight = FontWeight.Normal
        )
        Text(
            text = buildAnnotatedString {
                withStyle(currencyScript) {
                    append(localCurrency.current)
                    append(" ")
                }
                append(whole)
                withStyle(decimalScript) {
                    append(".")
                    append(decimal)
                }
            },
            fontSize = amountTextSize.sep(),
            color = colorResource(id = R.color.splitGreyCard),
            fontFamily = robotoFonts,
            fontWeight = FontWeight.Bold
        )
    }
}