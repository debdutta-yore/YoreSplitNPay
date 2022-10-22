package co.yore.splitnpay.components.components


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import co.yore.splitnpay.ui.theme.CyanLight
import co.yore.splitnpay.ui.theme.DarkBlue
import co.yore.splitnpay.ui.theme.LightGreen3

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
)