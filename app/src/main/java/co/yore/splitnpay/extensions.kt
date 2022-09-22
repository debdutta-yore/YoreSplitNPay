package co.yore.splitnpay

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.text.DecimalFormat

object YoreAmountFormatter{
    val formatter = DecimalFormat("#,##,###")
}

data class FloatSplitted(
    val wholeString: String,
    val decString: String,
    val whole: Int,
)

fun Float.splitted(): FloatSplitted {
    val text = this.toString()
    val parts = text.split(".")
    var wholeText = parts[0]
    var decText = parts[1]
    val whole = wholeText.toInt()
    if(whole<10){
        wholeText = "0$whole"
    }
    else{
        wholeText = YoreAmountFormatter.formatter.format(whole)
    }
    if(decText.length<2){
        decText = "${decText}0"
    }
    if(decText.length>2){
        decText = decText.substring(0..1)
    }
    return FloatSplitted(
        wholeText,
        decText,
        whole,
    )
}

var formatter = DecimalFormat("#,###")
fun Long.formatComma():String = formatter.format(this)

fun Modifier.fadingEdge(
    startingColor: Color = Color.White,
    endingColor: Color = Color.Transparent,
    length: Float = 60f,
    horizontal: Boolean = false,
    starting1Color: Color = endingColor,
    ending1Color: Color = startingColor,
    length1: Float = length
) = this.then(
    drawWithContent {
        val colors = listOf(startingColor, endingColor)
        val colors1 = listOf(starting1Color,ending1Color)
        drawContent()
        if(!horizontal){
            drawRect(
                brush = Brush.verticalGradient(colors, endY = length),
            )
            drawRect(
                brush = Brush.verticalGradient(
                    colors1,
                    startY = size.height - length1,
                    endY = size.height
                ),
            )
        }
        else{
            drawRect(
                brush = Brush.horizontalGradient(colors, endX = length),
            )
            drawRect(
                brush = Brush.horizontalGradient(
                    colors1,
                    startX = size.width - length1,
                    endX = size.width
                ),
            )
        }
    }
)

//34*1.dep() and 1.dep()*34 can be achieved by the following extensions
operator fun Dp.times(number: Number): Dp {
    return (this.value*number.toFloat()).dp
}
operator fun Number.times(dp: Dp): Dp {
    return (this.toFloat()*dp.value).dp
}

fun Color.Companion.blend(color1: Color, color2: Color, progress: Float): Color{
    val r1 = color1.red
    val g1 = color1.green
    val b1 = color1.blue

    val r2 = color2.red
    val g2 = color2.green
    val b2 = color2.blue

    val p = progress
    val q = 1f - progress
    return Color(
        red = q*r1 + p*r2,
        green = q*g1 + p*g2,
        blue = q*b1 + p*b2,
    )
}

const val root2 = 1.4142

val Number.droot2
    get() = this.toFloat() / root2