package co.yore.splitnpay.libs

import android.graphics.BlurMaskFilter
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalMinimumTouchTargetEnforcement
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.isSimple
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import co.yore.splitnpay.models.VerticalRotation
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

fun Modifier.dashedBorder(width: Dp, color: Color, shape: Shape = RectangleShape, on: Dp, off: Dp) =
    dashedBorder(width, SolidColor(color), shape, on, off)

fun Modifier.dashedBorder(width: Dp, brush: Brush, shape: Shape, on: Dp, off: Dp): Modifier =
    composed(
        factory = {
            this.then(
                Modifier.drawWithCache {
                    val outline: Outline = shape.createOutline(size, layoutDirection, this)
                    val borderSize = if (width == Dp.Hairline) 1f else width.toPx()

                    var insetOutline: Outline? = null // outline used for roundrect/generic shapes
                    var stroke: Stroke? = null // stroke to draw border for all outline types
                    var pathClip: Path? = null // path to clip roundrect/generic shapes
                    var inset = 0f // inset to translate before drawing the inset outline
                    // path to draw generic shapes or roundrects with different corner radii
                    var insetPath: Path? = null
                    if (borderSize > 0 && size.minDimension > 0f) {
                        if (outline is Outline.Rectangle) {
                            stroke = Stroke(
                                borderSize,
                                pathEffect = PathEffect.dashPathEffect(
                                    floatArrayOf(on.toPx(), off.toPx())
                                )
                            )
                        } else {
                            // Multiplier to apply to the border size to get a stroke width that is
                            // large enough to cover the corners while not being too large to overly
                            // square off the internal shape. The resultant shape will be
                            // clipped to the desired shape. Any value lower will show artifacts in
                            // the corners of shapes. A value too large will always square off
                            // the internal shape corners. For example, for a rounded rect border
                            // a large multiplier will always have squared off edges within the
                            // inner section of the stroke, however, having a smaller multiplier
                            // will still keep the rounded effect for the inner section of the
                            // border
                            val strokeWidth = 1.2f * borderSize
                            inset = borderSize - strokeWidth / 2
                            val insetSize = Size(
                                size.width - inset * 2,
                                size.height - inset * 2
                            )
                            insetOutline = shape.createOutline(insetSize, layoutDirection, this)
                            stroke = Stroke(
                                strokeWidth,
                                pathEffect = PathEffect.dashPathEffect(
                                    floatArrayOf(on.toPx(), off.toPx())
                                )
                            )
                            pathClip = if (outline is Outline.Rounded) {
                                Path().apply { addRoundRect(outline.roundRect) }
                            } else if (outline is Outline.Generic) {
                                outline.path
                            } else {
                                // should not get here because we check for Outline.Rectangle
                                // above
                                null
                            }

                            insetPath =
                                if (insetOutline is Outline.Rounded &&
                                    !insetOutline.roundRect.isSimple
                                ) {
                                    // Rounded rect with non equal corner radii needs a path
                                    // to be pre-translated
                                    Path().apply {
                                        addRoundRect(insetOutline.roundRect)
                                        translate(Offset(inset, inset))
                                    }
                                } else if (insetOutline is Outline.Generic) {
                                    // Generic paths must be created and pre-translated
                                    Path().apply {
                                        addPath(insetOutline.path, Offset(inset, inset))
                                    }
                                } else {
                                    // Drawing a round rect with equal corner radii without
                                    // usage of a path
                                    null
                                }
                        }
                    }

                    onDrawWithContent {
                        drawContent()
                        // Only draw the border if a have a valid stroke parameter. If we have
                        // an invalid border size we will just draw the content
                        if (stroke != null) {
                            if (insetOutline != null && pathClip != null) {
                                val isSimpleRoundRect = insetOutline is Outline.Rounded &&
                                    insetOutline.roundRect.isSimple
                                withTransform({
                                    clipPath(pathClip)
                                    // we are drawing the round rect not as a path so we must
                                    // translate ourselves othe
                                    if (isSimpleRoundRect) {
                                        translate(inset, inset)
                                    }
                                }) {
                                    if (isSimpleRoundRect) {
                                        // If we don't have an insetPath then we are drawing
                                        // a simple round rect with the corner radii all identical
                                        val rrect = (insetOutline as Outline.Rounded).roundRect
                                        drawRoundRect(
                                            brush = brush,
                                            topLeft = Offset(rrect.left, rrect.top),
                                            size = Size(rrect.width, rrect.height),
                                            cornerRadius = rrect.topLeftCornerRadius,
                                            style = stroke
                                        )
                                    } else if (insetPath != null) {
                                        drawPath(insetPath, brush, style = stroke)
                                    }
                                }
                                // Clip rect to ensure the stroke does not extend the bounds
                                // of the composable.
                                /*clipRect {
                                    // Draw a hairline stroke to cover up non-anti-aliased pixels
                                    // generated from the clip
                                    if (isSimpleRoundRect) {
                                        val rrect = (outline as Outline.Rounded).roundRect
                                        drawRoundRect(
                                            brush = brush,
                                            topLeft = Offset(rrect.left, rrect.top),
                                            size = Size(rrect.width, rrect.height),
                                            cornerRadius = rrect.topLeftCornerRadius,
                                            style = Stroke(
                                                Stroke.HairlineWidth,
                                                pathEffect = PathEffect.dashPathEffect(
                                                    floatArrayOf(on.toPx(), off.toPx())
                                                )
                                            )
                                        )
                                    } else {
                                        drawPath(
                                            pathClip, brush = brush, style = Stroke(
                                                Stroke.HairlineWidth,
                                                pathEffect = PathEffect.dashPathEffect(
                                                    floatArrayOf(on.toPx(), off.toPx())
                                                )
                                            )
                                        )
                                    }
                                }*/
                            } else {
                                // Rectangular border fast path
                                val strokeWidth = stroke.width
                                val halfStrokeWidth = strokeWidth / 2
                                drawRect(
                                    brush = brush,
                                    topLeft = Offset(halfStrokeWidth, halfStrokeWidth),
                                    size = Size(
                                        size.width - strokeWidth,
                                        size.height - strokeWidth
                                    ),
                                    style = stroke
                                )
                            }
                        }
                    }
                }
            )
        },
        inspectorInfo = debugInspectorInfo {
            name = "border"
            properties["width"] = width
            if (brush is SolidColor) {
                properties["color"] = brush.value
                value = brush.value
            } else {
                properties["brush"] = brush
            }
            properties["shape"] = shape
        }
    )

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
        val colors1 = listOf(starting1Color, ending1Color)
        drawContent()
        if (!horizontal) {
            drawRect(
                brush = Brush.verticalGradient(colors, endY = length)
            )
            drawRect(
                brush = Brush.verticalGradient(
                    colors1,
                    startY = size.height - length1,
                    endY = size.height
                )
            )
        } else {
            drawRect(
                brush = Brush.horizontalGradient(colors, endX = length)
            )
            drawRect(
                brush = Brush.horizontalGradient(
                    colors1,
                    startX = size.width - length1,
                    endX = size.width
                )
            )
        }
    }
)

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
                    radius = rippleRadius
                ),
                enabled = enabled,
                onClickLabel = onClickLabel,
                role = role,
                onClick = onClick
            )
    }
)

@OptIn(ExperimentalLayoutApi::class)
fun Modifier.ModalBottomSheetAdjustWithIme() =
    composed(
        factory = {
            val density = LocalDensity.current.density
            val kh = WindowInsets.ime.getBottom(LocalDensity.current) -
                (
                    WindowInsets.systemBars.asPaddingValues()
                        .calculateBottomPadding().value * density
                    )
            val keyboardOpen = WindowInsets.isImeVisible
            val scrollState = rememberScrollState()
            val scope = rememberCoroutineScope()
            val screenHeight = LocalConfiguration.current.screenHeightDp
            LaunchedEffect(key1 = keyboardOpen) {
                if (keyboardOpen) {
                    scope.launch {
                        scrollState.animateScrollTo(Int.MAX_VALUE)
                    }
                }
            }
            if (keyboardOpen) {
                Modifier
                    .imePadding()
                    .heightIn(min = 0.dp, max = ((screenHeight - kh / density)).dp)
                    .verticalScroll(scrollState)
            } else {
                Modifier
            }
        }
    )

internal fun Modifier.coloredShadow(
    color: Color = Color.Black,
    borderRadius: Dp = 0.dp,
    blurRadius: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    offsetX: Dp = 0.dp,
    spread: Float = 0f,
    modifier: Modifier = Modifier
) = this.then(
    modifier.drawBehind {
        this.drawIntoCanvas {
            val paint = Paint()
            val frameworkPaint = paint.asFrameworkPaint()
            val spreadPixel = spread.dp.toPx()
            val leftPixel = (0f - spreadPixel) + offsetX.toPx()
            val topPixel = (0f - spreadPixel) + offsetY.toPx()
            val rightPixel = (this.size.width + spreadPixel)
            val bottomPixel = (this.size.height + spreadPixel)

            if (blurRadius != 0.dp) {
                /*
                    The feature maskFilter used below to apply the blur effect only works
                    with hardware acceleration disabled.
                 */
                frameworkPaint.maskFilter =
                    (BlurMaskFilter(blurRadius.toPx(), BlurMaskFilter.Blur.NORMAL))
            }

            frameworkPaint.color = color.toArgb()
            it.drawRoundRect(
                left = leftPixel,
                top = topPixel,
                right = rightPixel,
                bottom = bottomPixel,
                radiusX = borderRadius.toPx(),
                radiusY = borderRadius.toPx(),
                paint
            )
        }
    }
)

internal fun Modifier.coloredShadowDp(
    color: Color = Color.Black,
    borderRadius: Dp = 0.dp,
    blurRadius: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    offsetX: Dp = 0.dp,
    spread: Dp = 0f.dp,
    modifier: Modifier = Modifier
) = this.then(
    modifier.drawBehind {
        this.drawIntoCanvas {
            val paint = Paint()
            val frameworkPaint = paint.asFrameworkPaint()
            val spreadPixel = spread.toPx()
            val leftPixel = (0f - spreadPixel) + offsetX.toPx()
            val topPixel = (0f - spreadPixel) + offsetY.toPx()
            val rightPixel = (this.size.width + spreadPixel)
            val bottomPixel = (this.size.height + spreadPixel)

            if (blurRadius != 0.dp) {
                /*
                    The feature maskFilter used below to apply the blur effect only works
                    with hardware acceleration disabled.
                 */
                frameworkPaint.maskFilter =
                    (BlurMaskFilter(blurRadius.toPx(), BlurMaskFilter.Blur.NORMAL))
            }

            frameworkPaint.color = color.toArgb()
            it.drawRoundRect(
                left = leftPixel,
                top = topPixel,
                right = rightPixel,
                bottom = bottomPixel,
                radiusX = borderRadius.toPx(),
                radiusY = borderRadius.toPx(),
                paint
            )
        }
    }
)

fun Modifier.maxHeightFactor(
    factor: Float = 1f
) = composed(
    factory = {
        val screenHeight = LocalConfiguration.current.screenHeightDp
        Modifier
            .heightIn(max = (screenHeight * factor).dp)
    }
)

fun Modifier.radialBottomLeft(
    parentSize: Dp
) = layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)
    val pw = parentSize.roundToPx()
    val ph = pw
    val mw = placeable.width
    val mh = placeable.height
    val cx = (pw - mw) / 2
    val cy = (ph - mh) / 2
    val offset = (pw / 2.8284f).toInt()
    layout(pw / 2 + mw / 2 + offset, ph / 2 + mh / 2 + offset) {
        placeable.placeRelative(cx + offset, cy + offset)
    }
}

fun Modifier.rotateVertically(rotation: VerticalRotation) = then(
    object : LayoutModifier {
        override fun MeasureScope.measure(
            measurable: Measurable,
            constraints: Constraints
        ): MeasureResult {
            val placeable = measurable.measure(constraints)
            return layout(placeable.height, placeable.width) {
                placeable.place(
                    x = -(placeable.width / 2 - placeable.height / 2),
                    y = -(placeable.height / 2 - placeable.width / 2)
                )
            }
        }

        override fun IntrinsicMeasureScope.minIntrinsicHeight(
            measurable: IntrinsicMeasurable,
            width: Int
        ): Int {
            return measurable.maxIntrinsicWidth(width)
        }

        override fun IntrinsicMeasureScope.maxIntrinsicHeight(
            measurable: IntrinsicMeasurable,
            width: Int
        ): Int {
            return measurable.maxIntrinsicWidth(width)
        }

        override fun IntrinsicMeasureScope.minIntrinsicWidth(
            measurable: IntrinsicMeasurable,
            height: Int
        ): Int {
            return measurable.minIntrinsicHeight(height)
        }

        override fun IntrinsicMeasureScope.maxIntrinsicWidth(
            measurable: IntrinsicMeasurable,
            height: Int
        ): Int {
            return measurable.maxIntrinsicHeight(height)
        }
    }
)
    .then(rotate(rotation.value))

@OptIn(ExperimentalMaterialApi::class)
@Suppress("ModifierInspectorInfo")
fun Modifier.minimumTouchTargetSize(): Modifier = composed(
    inspectorInfo = debugInspectorInfo {
        name = "minimumTouchTargetSize"
        // TODO: b/214589635 - surface this information through the layout inspector in a better way
        //  - for now just add some information to help developers debug what this size represents.
        properties["README"] = "Adds outer padding to measure at least 48.dp (default) in " +
            "size to disambiguate touch interactions if the element would measure smaller"
    }
) {
    if (LocalMinimumTouchTargetEnforcement.current) {
        // TODO: consider using a hardcoded value of 48.dp instead to avoid inconsistent UI if the
        // LocalViewConfiguration changes across devices / during runtime.
        val size = LocalViewConfiguration.current.minimumTouchTargetSize
        MinimumTouchTargetModifier(size)
    } else {
        Modifier
    }
}

private class MinimumTouchTargetModifier(val size: DpSize) : LayoutModifier {
    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints
    ): MeasureResult {

        val placeable = measurable.measure(constraints)

        // Be at least as big as the minimum dimension in both dimensions
        val width = maxOf(placeable.width, size.width.roundToPx())
        val height = maxOf(placeable.height, size.height.roundToPx())

        return layout(width, height) {
            val centerX = ((width - placeable.width) / 2f).roundToInt()
            val centerY = ((height - placeable.height) / 2f).roundToInt()
            placeable.place(centerX, centerY)
        }
    }

    override fun equals(other: Any?): Boolean {
        val otherModifier = other as? MinimumTouchTargetModifier ?: return false
        return size == otherModifier.size
    }

    override fun hashCode(): Int {
        return size.hashCode()
    }
}
