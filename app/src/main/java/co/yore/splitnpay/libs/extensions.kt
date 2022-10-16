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
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import co.yore.splitnpay.models.DataIds
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import java.util.concurrent.ThreadLocalRandom

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

fun Long?.formatWithComma(): String {
    if(this==null){
        return ""
    }
    else {
        return NumberFormat.getNumberInstance(Locale.US).format(this)
    }
}
