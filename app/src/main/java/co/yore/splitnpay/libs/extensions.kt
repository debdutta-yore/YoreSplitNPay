package co.yore.splitnpay.libs

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import co.yore.splitnpay.models.DataIds
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import java.util.concurrent.ThreadLocalRandom

object YoreAmountFormatter{
    val formatter = DecimalFormat("#,###")
    val decFormatter = DecimalFormat("#,##,###.##")
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
fun Number.formatComma():String = formatter.format(this)

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

//34*1.dep() and 1.dep()*34 can be achieved by the following extensions
operator fun TextUnit.times(number: Number): TextUnit {
    return (this.value*number.toFloat()).sp
}
operator fun Number.times(sp: TextUnit): TextUnit {
    return (this.toFloat()*sp.value).sp
}

fun Color.Companion.blend(
    color1: Color,
    color2: Color,
    progress: Float
): Color{
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

fun lcs(a: String, b: String): String {
    if (a.length > b.length) return lcs(b, a)
    var res = ""
    for (ai in 0 until a.length) {
        for (len in a.length - ai downTo 1) {
            for (bi in 0 until b.length - len) {
                if (a.regionMatches(ai, b, bi,len) && len > res.length) {
                    res = a.substring(ai, ai + len)
                }
            }
        }
    }
    return res
}

fun countFreq(pat: String, txt: String): Int {
    val M = pat.length
    val N = txt.length
    var res = 0

    for (i in 0..N - M) {
        var j: Int
        j = 0
        while (j < M) {
            if (txt[i + j] != pat[j]) {
                break
            }
            j++
        }
        if (j == M) {
            res++
            j = 0
        }
    }
    return res
}

fun String.containsAny(vararg sub: String): Boolean{
    val s = sub.joinToString("|")
    if(s.isEmpty()){
        return false
    }
    return this.contains(s.toRegex())
}

fun search(query: String, targets: List<String>): Boolean{
    if(query.isEmpty()){
        return true
    }
    val q = query
        .lowercase()
        .split("[ ,\n\t-]+".toRegex())
        .distinct()

    var matched = 0
    targets.forEach {
        matched += if(it.containsAny(*(q.toTypedArray()))) 1 else 0
    }
    return matched>0
}

fun randomDate(start: Long, end: Long): Long {
    return ThreadLocalRandom
        .current()
        .nextLong(start, end)
}

data class StatusBarColor(
    val color: Color,
    val darkIcons: Boolean = false
)

@Composable
fun StatusBarColorControl(
    state: StatusBarColor? = safeTState<StatusBarColor>(DataIds.statusBarColor)?.value
) {
    val systemUiController = rememberSystemUiController()

    DisposableEffect(systemUiController, state) {
        state?.let {
            val color = it.color
            val darkIcons = it.darkIcons
            systemUiController.setStatusBarColor(
                color = color,
                darkIcons = darkIcons
            )
        }
        onDispose {}
    }
}

fun Modifier.clickable(
    rippleColor: Color,
    rippleRadius: Dp = Dp.Unspecified,
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit
) = composed(
    factory = {
        val interactionSource = remember { MutableInteractionSource() }
        Modifier
            .clickable(
                interactionSource = interactionSource,
                indication = rememberRipple(
                    color = rippleColor,
                    radius = rippleRadius,
                ),
                enabled = enabled,
                onClickLabel = onClickLabel,
                role = role,
                onClick = onClick
            )
    }
)

data class NavAnimation @OptIn(ExperimentalAnimationApi::class) constructor(
    val duration: Int = 700,
    val enterTransition: (AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition?)? = {
        slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(duration))

    },
    val exitTransition: (AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition?)? = {
        slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(duration))
    },
    val popEnterTransition: (
    AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition?
    )? = {
        slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(duration))
    },
    val popExitTransition: (
    AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition?
    )? = {
        slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(duration))
    },
)

@ExperimentalAnimationApi
fun NavGraphBuilder.yoreComposable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    navAnimation: NavAnimation = NavAnimation(),
    content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit
){
    composable(
        route,
        arguments,
        deepLinks,
        navAnimation.enterTransition,
        navAnimation.exitTransition,
        navAnimation.popEnterTransition,
        navAnimation.popExitTransition,
        content
    )
}

fun <E>MutableCollection<E>.removeIfMy(filter: (E)->Boolean): Boolean{
    Objects.requireNonNull(filter)
    var removed = false
    val each: MutableIterator<E> = iterator()
    while (each.hasNext()) {
        val item = each.next()
        if (filter(item)) {
            each.remove()
            removed = true
        }
    }
    return removed
}

val String.isValidFormattableAmount get(): Boolean = isNotBlank() && isDigitsOnly() && length <= 6
fun formatAmountOrMessage(
    input: String
): String = if (input.isValidFormattableAmount) {
    DecimalFormat("#,##,###").format(input.toDouble())
} else {
    input
}
class AmountOrMessageVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {

        val originalText = text.text
        val formattedText = formatAmountOrMessage(text.text)

        val offsetMapping = object : OffsetMapping {

            override fun originalToTransformed(offset: Int): Int {
                if (originalText.isValidFormattableAmount) {
                    val commas = formattedText.count { it == ',' }
                    return when {
                        offset <= 1 -> offset
                        offset <= 3 -> if (commas >= 1) offset + 1 else offset
                        offset <= 5 -> if (commas == 2) offset + 2 else offset + 1
                        else -> 8
                    }
                }
                return offset
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (originalText.isValidFormattableAmount) {
                    val commas = formattedText.count { it == ',' }
                    return when (offset) {
                        8, 7 -> offset - 2
                        6 -> if (commas == 1) 5 else 4
                        5 -> if (commas == 1) 4 else if (commas == 2) 3 else offset
                        4, 3 -> if (commas >= 1) offset - 1 else offset
                        2 -> if (commas == 2) 1 else offset
                        else -> offset
                    }
                }
                return offset
            }
        }

        return TransformedText(
            text = AnnotatedString(formattedText),
            offsetMapping = offsetMapping
        )
    }
}
class AmountStyle(private var sp: TextUnit) : VisualTransformation {
    var out = ""
    var whole = ""
    override fun filter(text: AnnotatedString): TransformedText {
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return when (offset) {
                    whole.length + 1 -> out.length + 1
                    whole.length + 2 -> out.length + 2
                    whole.length + 3 -> out.length + 3
                    else -> {
                        val rightOffset = whole.lastIndex - offset
                        val commasToTheRight = rightOffset / 3
                        out.lastIndex - rightOffset - commasToTheRight
                    }
                }
            }

            override fun transformedToOriginal(offset: Int): Int {
                return when (offset) {
                    out.length+1->whole.length+1
                    out.length+2->whole.length+2
                    out.length+3->whole.length+3
                    else -> {
                        val totalCommas = ((whole.length - 1) / 3).coerceAtLeast(0)
                        val rightOffset = out.length - offset
                        val commasToTheRight = rightOffset / 4
                        offset - (totalCommas - commasToTheRight)
                    }
                }
            }
        }
        return TransformedText(
            buildAnnotatedStringWithColors(text.toString()),
            offsetMapping
        )
    }

    private fun buildAnnotatedStringWithColors(text:String): AnnotatedString{
        val builder = AnnotatedString.Builder()
        val parts = text.split(".")
        val wholeInt = parts[0].toBigDecimal()
        whole = parts[0]
        out = YoreAmountFormatter.formatter.format(wholeInt)
        val dec = parts[1].toInt()
        val color = if(wholeInt+dec.toBigDecimal()> BigDecimal(0)){
            Color(0xff243257)
        }
        else{
            Color(0xff8C93A2)
        }
        builder.withStyle(
            SpanStyle(
                color = color,
                fontSize = 14*sp,
                fontWeight = FontWeight.Bold
            )
        ){
            append(out)
        }
        builder.withStyle(
            SpanStyle(
                color = color,
                fontSize = 12*sp
            )
        ){
            append(".")
            append(parts[1])
        }
        return builder.toAnnotatedString()
    }
}

// format long to 123,456,789,9
class NumberCommaTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return TransformedText(
            text = AnnotatedString(text.text.toLongOrNull().formatWithComma()),
            offsetMapping = object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    return text.text.toLongOrNull().formatWithComma().length
                }

                override fun transformedToOriginal(offset: Int): Int {
                    return text.length
                }
            }
        )
    }
}
class ThousandsTransformer : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val out = text.text.toLongOrNull().formatWithComma()
        return TransformedText(
            text = AnnotatedString(text.text.toLongOrNull().formatWithComma()),
            offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                val rightOffset = text.lastIndex - offset
                val commasToTheRight = rightOffset / 3
                return out.lastIndex - rightOffset - commasToTheRight
            }

            override fun transformedToOriginal(offset: Int): Int {
                val totalCommas = ((text.length - 1) / 3).coerceAtLeast(0)
                val rightOffset = out.length - offset
                val commasToTheRight = rightOffset / 4
                return (offset - (totalCommas - commasToTheRight))
            }
        }
        )
    }
}

fun Long?.formatWithComma(): String {
    if(this==null){
        return ""
    }
    else {
        return NumberFormat.getNumberInstance(Locale.US).format(this)
    }
}

@Composable
fun Int.numberToWords(): String {
    val units = arrayOf(
        "", "One", "Two", "Three", "Four",
        "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve",
        "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen",
        "Eighteen", "Nineteen"
    )

    val tens = arrayOf(
        "",  // 0
        "",  // 1
        "Twenty",  // 2
        "Thirty",  // 3
        "Forty",  // 4
        "Fifty",  // 5
        "Sixty",  // 6
        "Seventy",  // 7
        "Eighty",  // 8
        "Ninety" // 9
    )

    fun convert(n: Int): String {
        if (n < 0) {
            return "Minus " + convert(-n)
        }
        if (n < 20) {
            return units.get(n)
        }
        if (n < 100) {
            return tens.get(n / 10) + (if (n % 10 != 0) " " else "") + units.get(n % 10)
        }
        if (n < 1000) {
            return units.get(n / 100) + " Hundred" + (if (n % 100 != 0) " " else "") + convert(n % 100)
        }
        if (n < 100000) {
            return convert(n / 1000) + " Thousand" + (if (n % 10000 != 0) " " else "") + convert(
                n % 1000
            )
        }
        return if (n < 10000000) {
            convert(n / 100000) + " Lakh" + (if (n % 100000 != 0) " " else "") + convert(n % 100000)
        } else convert(n / 10000000) + " Crore" + (if (n % 10000000 != 0) " " else "") + convert(
            n % 10000000
        )
    }

    // TODO: search better approach for .replace("Indian ","")
    return convert(this@numberToWords) + " " + "Rupees"
}


