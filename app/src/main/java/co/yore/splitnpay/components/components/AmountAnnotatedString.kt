package co.yore.splitnpay.components.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import co.yore.splitnpay.libs.formatComma
import co.yore.splitnpay.libs.sep
import co.yore.splitnpay.libs.splitted
import co.yore.splitnpay.locals.localCurrency
import co.yore.splitnpay.ui.theme.CloudBurst
import co.yore.splitnpay.ui.theme.Turquoise1
import co.yore.splitnpay.ui.theme.robotoFonts

@Composable
fun Float.groupCard():AnnotatedString {
    return this.amountAnnotatedString(
        currencyFontSize = 12f,
        currencyTextColor = Turquoise1,
        wholeNumberFontSize = 16f,
        wholeNumberTextColor = Turquoise1,
        decNumberFontSize = 10f,
        decNumberTextColor = Turquoise1,
    )
}

@Composable
fun Float.amountAnnotatedString(
    isLeadingTextEnabled: Boolean = false,
    leadingText: String = "",
    leadingTextFontSize: Float = 9f,
    leadingTextTextColor: Color = CloudBurst,
    leadingTextFontWeight: FontWeight = FontWeight(400),
    isTrailingTextEnabled: Boolean = false,
    trailingText: String = "",
    trailingTextFontSize: Float = 9f,
    trailingTextTextColor: Color = CloudBurst,
    trailingTextFontWeight: FontWeight = FontWeight(400),
    isSpaceBetween: Boolean = false,
    currencyFontSize: Float = 12f,
    currencyTextColor: Color,
    currencyFontWeight: FontWeight = FontWeight(400),
    wholeNumberFontSize: Float = 12f,
    wholeNumberTextColor: Color,
    wholeNumberFontWeight: FontWeight = FontWeight(400),
    decNumberFontSize: Float = 12f,
    decNumberTextColor: Color,
    decNumberFontWeight: FontWeight = FontWeight(400),
    isDecimalEnabled: Boolean = true
): AnnotatedString = buildAnnotatedString {

    if (isLeadingTextEnabled) {
        withStyle(
            style = SpanStyle(
                fontSize = leadingTextFontSize.sep(),
                fontWeight = leadingTextFontWeight,
                color = leadingTextTextColor,
                fontFamily = robotoFonts
            ),
        )
        {
            append(leadingText)
        }
    }
    withStyle(
        style = SpanStyle(
            fontSize = currencyFontSize.sep(),
            fontWeight = currencyFontWeight,
            color = currencyTextColor,
            fontFamily = robotoFonts
        ),
    )
    {
        append(localCurrency.current)
        if(isSpaceBetween){
            append(" ")
        }
    }
    withStyle(
        style = SpanStyle(
            fontSize = wholeNumberFontSize.sep(),
            fontWeight = wholeNumberFontWeight,
            color = wholeNumberTextColor,
            fontFamily = robotoFonts
        ),
    )
    {
        append(this@amountAnnotatedString.splitted().whole.toLong().formatComma())
    }
    if (isDecimalEnabled) {
        withStyle(
            style = SpanStyle(
                fontSize = decNumberFontSize.sep(),
                fontWeight = decNumberFontWeight,
                color = decNumberTextColor,
                fontFamily = robotoFonts
            ),
        ) {
            append('.')
            append(this@amountAnnotatedString.splitted().decString)
        }
    }

    if (isTrailingTextEnabled) {
        withStyle(
            style = SpanStyle(
                fontSize = trailingTextFontSize.sep(),
                fontWeight = trailingTextFontWeight,
                color = trailingTextTextColor,
                fontFamily = robotoFonts
            ),
        )
        {
            append(trailingText)
        }
    }
}