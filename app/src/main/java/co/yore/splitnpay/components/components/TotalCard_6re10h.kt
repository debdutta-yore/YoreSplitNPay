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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import co.yore.splitnpay.libs.dep
import co.yore.splitnpay.libs.sep
import co.yore.splitnpay.models.TotalCardConfiguration
import co.yore.splitnpay.ui.theme.*


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
            color = CloudBurst,
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


