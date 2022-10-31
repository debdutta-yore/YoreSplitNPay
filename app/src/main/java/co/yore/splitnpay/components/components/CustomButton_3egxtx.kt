package co.yore.splitnpay.components.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import co.yore.splitnpay.libs.jerokit.dep
import co.yore.splitnpay.libs.jerokit.sep
import co.yore.splitnpay.models.CustomButtonConfiguration
import co.yore.splitnpay.ui.theme.WildBlueYonder

@Composable
fun CustomButton_3egxtx(
    config: CustomButtonConfiguration = CustomButtonConfiguration(),
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit,
    contentDescription: String
) {
    Button(
        enabled = enabled,
        modifier = Modifier
            .fillMaxSize()
            .semantics { this.contentDescription = contentDescription },
        onClick = { onClick() },
        shape = RoundedCornerShape(config.cornerRadius.dep()),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = config.buttonColor,
            disabledBackgroundColor = WildBlueYonder,
            disabledContentColor = config.fontColor,
            contentColor = config.fontColor
        )
    ) {
        FontFamilyText(
            text = text,
            fontSize = config.fontSize.sep(),
            fontWeight = config.fontWeight
            // color = config.fontColor
        )
    }
}
