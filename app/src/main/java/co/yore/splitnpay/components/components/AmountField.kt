package co.yore.splitnpay.components.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import co.yore.splitnpay.R
import co.yore.splitnpay.libs.AmountStyle
import co.yore.splitnpay.libs.dep
import co.yore.splitnpay.libs.sep
import co.yore.splitnpay.libs.sx
import co.yore.splitnpay.models.Amount
import co.yore.splitnpay.ui.theme.Alabaster1
import co.yore.splitnpay.ui.theme.CloudBurst
import co.yore.splitnpay.ui.theme.Manatee
import co.yore.splitnpay.ui.theme.robotoFonts
import java.math.MathContext

fun String.amount(): Amount {
    val text = this
    val parts = text.split(".")
    var wholeText = parts[0]
    var decText = parts.getOrNull(1) ?: "0"
    val whole = wholeText.toBigDecimal()
    val dec = decText.toFloat()
    wholeText = whole.toString() // YoreAmountFormatter.formatter.format(whole)
    if (decText.length < 2) {
        decText = "${decText}0"
    }
    if (decText.length > 2) {
        decText = decText.substring(0..1)
    }
    return Amount(
        wholeText,
        decText,
        whole,
        dec
    )
}

fun Float.amount(): Amount {
    return this.toBigDecimal().toPlainString().amount()
}

fun Double.amount(): Amount {
    return this.toBigDecimal(MathContext.UNLIMITED).toPlainString().amount()
}

@Composable
fun AmountField(
    amount: Double,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onChange: (Double) -> Unit
) {
    val sep = 1.sep()
    val text by remember(amount) {
        derivedStateOf {
            amount.amount().toString()
        }
    }
    val color by remember(amount) {
        derivedStateOf {
            if (amount > 0) {
                CloudBurst
            } else {
                Manatee
            }
        }
    }
    val animatedColor by animateColorAsState(targetValue = color)
    BasicTextField(
        enabled = enabled,
        value = text,
        modifier = Modifier.width(IntrinsicSize.Min),
        onValueChange = { it ->
            if (it.isEmpty()){
                return@BasicTextField
            }
            if (it.matches("^([+-]?\\d*\\.?\\d*)$".toRegex())){
                onChange(it.replace(",", "").toDouble())
            }
        },
        textStyle = TextStyle(
            color = animatedColor,
            fontFamily = robotoFonts
        ),
        visualTransformation = AmountStyle(sep),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal
        ),
        decorationBox = {
            Row(
                modifier = modifier
                    .height(33.dep())
                    .clip(RoundedCornerShape(8.dep()))
                    .background(Alabaster1)
                    .padding(horizontal = 8.dep()),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.rupee),
                    contentDescription = "rupee",
                    tint = animatedColor,
                    modifier = Modifier.width(7.dep())
                )
                4.sx()
                it()
            }
        },
        singleLine = true,
        maxLines = 1
    )
}
