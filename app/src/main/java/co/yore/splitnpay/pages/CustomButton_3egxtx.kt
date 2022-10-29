package co.yore.splitnpay.pages

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import co.yore.splitnpay.addmembers.FontFamilyText
import co.yore.splitnpay.libs.dep
import co.yore.splitnpay.libs.sep
import co.yore.splitnpay.models.CustomButtonConfiguration

@Composable
fun CustomButton_3egxtx(
    config: CustomButtonConfiguration = CustomButtonConfiguration(),
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit,
    contentDescription: String
) {
    /*val backgroundColor by remember(enabled) {
        derivedStateOf {
            if(enabled){
                config.buttonColor
            }
            else{
                Color(0xff839BB9)
            }
        }
    }
    val animatedBackgroundColor by animateColorAsState(targetValue = backgroundColor)*/
    Button(
        enabled = enabled,
        modifier = Modifier
            .fillMaxSize()
            .semantics { this.contentDescription = contentDescription },
        onClick = { onClick() },
        shape = RoundedCornerShape(config.cornerRadius.dep()),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = config.buttonColor,
            disabledBackgroundColor = Color(0xff839BB9),
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
