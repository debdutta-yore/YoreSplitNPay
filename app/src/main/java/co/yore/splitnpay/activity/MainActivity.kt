package co.yore.splitnpay.activity

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.constraintlayout.compose.*
import co.yore.splitnpay.R
import co.yore.splitnpay.demos.sx
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.localKeyboardHeight
import co.yore.splitnpay.locals.localDesignWidth
import co.yore.splitnpay.ui.theme.YoreSplitNPayTheme
import co.yore.splitnpay.ui.theme.robotoFonts
import java.lang.Integer.min


class MainActivity : ComponentActivity() {
    private val bottomHeight = mutableStateOf(0)
    private var minBottomHeight = Int.MAX_VALUE
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val contentView = findViewById<ViewGroup>(android.R.id.content)
        contentView.viewTreeObserver.addOnGlobalLayoutListener {
            val r = Rect()
            contentView.getWindowVisibleDisplayFrame(r)
            val screenHeight = contentView.rootView.height
            val keypadHeight = screenHeight - r.bottom
            minBottomHeight = min(minBottomHeight,keypadHeight)
            bottomHeight.value = keypadHeight - minBottomHeight
        }
        setContent {
            YoreSplitNPayTheme {
                CompositionLocalProvider(
                    localDesignWidth provides 360f,
                    localKeyboardHeight provides bottomHeight.value
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize(),
                        color = MaterialTheme.colors.background,
                    ) {
                        //YoreApp()
                        Box(
                            contentAlignment = Alignment.Center
                        ){
                            var amount by remember {
                                mutableStateOf(0.0f)
                            }
                            AmountField(amount){
                                amount = it
                            }
                        }
                    }
                }
            }
        }
    }
}

data class Amount(
    val wholeText: String,
    val decText: String,
    val whole: Int,
    val dec: Float
)

fun String.amount(): Amount{
    val text = this
    val parts = text.split(".")
    var wholeText = parts[0]
    var decText = parts[1]
    val whole = wholeText.toInt()
    val dec = decText.toFloat()
    wholeText = if(whole<10){
        "0$whole"
    }
    else{
        YoreAmountFormatter.formatter.format(whole)
    }
    if(decText.length<2){
        decText = "${decText}0"
    }
    if(decText.length>2){
        decText = decText.substring(0..1)
    }
    return Amount(
        wholeText,
        decText,
        whole,
        dec
    )
}

fun Float.amount(): Amount{
    val text = this.toString()
    val parts = text.split(".")
    var wholeText = parts[0]
    var decText = parts[1]
    val whole = wholeText.toInt()
    val dec = decText.toFloat()
    wholeText = if(whole<10){
        "0$whole"
    }
    else{
        YoreAmountFormatter.formatter.format(whole)
    }
    if(decText.length<2){
        decText = "${decText}0"
    }
    if(decText.length>2){
        decText = decText.substring(0..1)
    }
    return Amount(
        wholeText,
        decText,
        whole,
        dec
    )
}

@Composable
fun AmountField(
    amount: Float,
    onChange: (Float)->Unit
) {
    val wholeDec by remember {
        derivedStateOf {
            amount.amount()
        }
    }
    Row(
        modifier = Modifier
            .widthIn(69.dep())
            .height(33.dep())
            .clip(RoundedCornerShape(8.dep()))
            .background(Color(0xffF9F9F9))
            .padding(horizontal = 8.dep()),
        verticalAlignment = Alignment.CenterVertically
    ){
        val sep = 1.sep()
        val tf by remember(wholeDec.whole,wholeDec.dec) {
            derivedStateOf {
                TextFieldValue(
                    buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                fontSize = 14*sep,
                                fontWeight = FontWeight.Bold
                            )
                        ){
                            append(wholeDec.wholeText)
                        }
                        withStyle(
                            SpanStyle(
                                fontSize = 10*sep
                            )
                        ){
                            append(wholeDec.decText)
                        }
                    }
                )
            }
        }
        val color by remember(wholeDec.wholeText,wholeDec.decText) {
            derivedStateOf {
                if(wholeDec.wholeText!="0" || wholeDec.decText!="00"){
                    Color(0xff243257)
                }
                else{
                    Color(0xff8C93A2)
                }
            }
        }
        val animatedColor by animateColorAsState(targetValue = color)
        Icon(
            painter = painterResource(id = R.drawable.rupee),
            contentDescription = "rupee",
            tint = animatedColor,
            modifier = Modifier.width(7.dep())
        )
        4.sx()
        BasicTextField(
            value = tf,
            onValueChange = {
                onChange(it.text.toFloat())
            },
            textStyle = TextStyle(
                color = animatedColor,
                fontFamily = robotoFonts
            ),
        )
    }
}






