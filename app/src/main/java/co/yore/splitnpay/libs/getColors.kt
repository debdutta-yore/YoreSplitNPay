package co.yore.splitnpay.libs

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

fun colorsFromHumanName(name: String): Pair<Color,Color> {
        val ints = name.uppercase().map{ it.code }
        val sum = ints.sumOf{it}
        val positionalSum = (ints.mapIndexed{index, i -> index*i}.sumOf{it})/name.length
        val back = Color(0xFFFFFF and (sum*10000+positionalSum)).copy(alpha = 1f)
        val white = Color.White
        val black = Color.Black
        val contrast = androidx.core.graphics.ColorUtils.calculateContrast(white.toArgb(), back.toArgb())
        return if(contrast > 1.7){
            Pair(back,white)
        } else{
            Pair(back,black)
        }
    }