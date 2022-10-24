package co.yore.splitnpay.components.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import co.yore.splitnpay.addmembers.FontFamilyText
import co.yore.splitnpay.libs.dep
import co.yore.splitnpay.libs.sep
import co.yore.splitnpay.ui.theme.*

/*
@Composable
fun TotalCard_6re10h(
    contentDescription: String,
    config: TotalCardConfiguration = TotalCardConfiguration(),
    text: String,
    amount: Float,
    backgroundColor: Color,
    borderColor: Color
) {
    Row(
        modifier = Modifier
            .semantics { this.contentDescription = contentDescription }
            .height(46.dep())
            .fillMaxWidth()
            .clip(RoundedCornerShape(config.cornerRadius))
            .background(color = backgroundColor)
            .border(
                width = config.borderRadius.dep(),
                color = borderColor,
                shape = RoundedCornerShape(config.cornerRadius.dep())
            )
            .padding(horizontal = config.paddingValue.dep()),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        FontFamilyText(
            text = text,
            color = DarkBlue,
            fontSize = 12.sep()
        )
        FontFamilyText(
            annotatedString = amount.amountAnnotatedString(
                isSpaceBetween = true,
                currencyTextColor = DarkBlue,
                currencyFontSize = 11.29f,
                wholeNumberTextColor = DarkBlue,
                wholeNumberFontSize = 14f,
                wholeNumberFontWeight = FontWeight.W600,
                decNumberTextColor = DarkBlue,
                decNumberFontSize = 10f,
                trailingText = " Dr",
                trailingTextFontSize = 11.29f,
                trailingTextTextColor = borderColor,
                isTrailingTextEnabled = true
            )
        )
    }
}

data class TotalCardConfiguration(
    val cornerRadius: Float = 8f,
    val borderRadius: Float = 1f,
    val borderColor: Color = LightGreen3,
    val backgroundColor: Color = CyanLight,
    val paddingValue: Float = 16f,
    val DrTextColor: Color = LightGreen3
)*/

@Composable
fun TotalCard_6re10h(
    contentDescription: String,
    config: TotalCardConfiguration = TotalCardConfiguration(),
    text: String,
    amount: Float,
    amountTrailingText: String
) {
    Row(
        modifier = Modifier
            .semantics { this.contentDescription = contentDescription }
            .fillMaxWidth()
            .clip(RoundedCornerShape(config.cornerRadius))
            .background(color = config.backgroundColor)
            .border(
                width = config.borderRadius.dep(),
                color = config.borderColor,
                shape = RoundedCornerShape(config.cornerRadius.dep())
            )
            .padding(config.paddingValue.dep()),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        FontFamilyText(
            text = text,
            color = DarkBlue,
            fontSize = config.textFontSize.sep(),
            style = MaterialTheme.typography.body1
        )
        FontFamilyText(
            annotatedString =
            amount.amountAnnotatedString(
                isTrailingTextEnabled = true,
                currencyTextColor = config.currencyTextColor,
                wholeNumberTextColor = config.wholeNumberTextColor,
                decNumberTextColor = config.decNumberTextColor,
                trailingTextTextColor = config.trailingTextTextColor,
                isSpaceBetween = true,
                trailingText = " $amountTrailingText",
                currencyFontSize = config.currencyFontSize,
                wholeNumberFontSize = config.wholeNumberFontSize,
                wholeNumberFontWeight = FontWeight.W600,
                decNumberFontSize = config.decNumberFontSize,
                trailingTextFontSize = config.trailingTextFontSize
            )
        )
    }
}

data class TotalCardConfiguration(
    val textFontSize: Float = 12f,
    val cornerRadius: Float = 8f,
    val borderRadius: Float = 1f,
    val borderColor: Color = LightGreen3,
    val backgroundColor: Color = CyanLight,
    val paddingValue: Float = 16f,
    val currencyTextColor: Color = LightGreen3,
    val wholeNumberTextColor: Color = LightGreen3,
    val decNumberTextColor: Color = LightGreen3,
    val trailingTextTextColor: Color = LightGreen3,
    val currencyFontSize: Float = 11f,
    val wholeNumberFontSize: Float = 14f,
    val decNumberFontSize: Float = 10f,
    val trailingTextFontSize: Float = 11f
) {
    companion object {

        val variationOne = TotalCardConfiguration(
            borderColor = Pink,
            backgroundColor = LightRedButton,
            currencyTextColor = Pink,
            wholeNumberTextColor = Pink,
            decNumberTextColor = Pink,
            trailingTextTextColor = Pink
        )
    }
}
