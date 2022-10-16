package co.yore.splitnpay.pages

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import co.yore.splitnpay.addmembers.FontFamilyText
import co.yore.splitnpay.libs.dep
import co.yore.splitnpay.libs.sep
import co.yore.splitnpay.ui.theme.Bluish

data class CustomButtonConfiguration(
    val buttonColor: androidx.compose.ui.graphics.Color = Bluish,
    val fontSize: Float = 16f,
    val fontColor: androidx.compose.ui.graphics.Color = White,
    val fontWeight: FontWeight = FontWeight.Bold,
    val cornerRadius: Float = 23.5f
)

@Composable
fun CustomButton_3egxtx(
    config: CustomButtonConfiguration = CustomButtonConfiguration(),
    text: String,
    enabled: Boolean,
    onClick: () -> Unit,
    contentDescription: String
) {
    val backgroundColor by remember(enabled) {
        derivedStateOf {
            if(enabled){
                config.buttonColor
            }
            else{
                Color(0xff839BB9)
            }
        }
    }
    val animatedBackgroundColor by animateColorAsState(targetValue = backgroundColor)
    Button(
        modifier = Modifier
            .fillMaxSize()
            .semantics { this.contentDescription = contentDescription },
        onClick = { onClick() },
        shape = RoundedCornerShape(config.cornerRadius.dep()),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = animatedBackgroundColor
        )
    ) {
        FontFamilyText(
            text = text,
            fontSize = config.fontSize.sep(),
            fontWeight = config.fontWeight,
            color = config.fontColor
        )
    }
}