package co.yore.splitnpay.components.components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Velocity
import co.yore.splitnpay.libs.DragRecord
import co.yore.splitnpay.libs.SwipingStates

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CollapsibleBox(
    modifier: Modifier = Modifier,
    threshold: Float = 0.5f,
    keyboardAware: Boolean = false,
    insetAware: Boolean = keyboardAware,
    content: @Composable BoxScope.(Float) -> Unit
){
    val swipingState = rememberSwipeableState(initialValue = SwipingStates.EXPANDED)
    if (keyboardAware){
        val insetLength = WindowInsets.ime.getBottom(LocalDensity.current)
        val record = remember {
            DragRecord()
        }
        LaunchedEffect(key1 = insetLength){
            val d = record.current(insetLength)
            if (d < 0){
                swipingState.animateTo(
                    SwipingStates.COLLAPSED,
                    anim = tween(2000)
                )
                return@LaunchedEffect
            }
            if (d > 0){
                swipingState.animateTo(
                    SwipingStates.EXPANDED,
                    anim = tween(2000)
                )
                return@LaunchedEffect
            }
        }
    }
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .then(
                if (insetAware) {
                    Modifier.safeDrawingPadding()
                } else {
                    Modifier
                }
            )
    ) {
        val heightInPx = with(LocalDensity.current) { maxHeight.toPx() }
        val connection = remember {
            object : NestedScrollConnection {
                override fun onPreScroll(
                    available: Offset,
                    source: NestedScrollSource
                ): Offset {
                    val delta = available.y
                    return if (delta < 0) {
                        swipingState.performDrag(delta).toOffset()
                    } else {
                        Offset.Zero
                    }
                }

                override fun onPostScroll(
                    consumed: Offset,
                    available: Offset,
                    source: NestedScrollSource
                ): Offset {
                    val delta = available.y
                    return swipingState.performDrag(delta).toOffset()
                }

                override suspend fun onPostFling(
                    consumed: Velocity,
                    available: Velocity
                ): Velocity {
                    swipingState.performFling(velocity = available.y)
                    return super.onPostFling(consumed, available)
                }

                private fun Float.toOffset() = Offset(0f, this)
            }
        }
        Box(
            modifier = modifier
                .swipeable(
                    state = swipingState,
                    thresholds = { _, _ -> FractionalThreshold(threshold) },
                    orientation = Orientation.Vertical,
                    anchors = mapOf(
                        0f to SwipingStates.COLLAPSED,
                        heightInPx to SwipingStates.EXPANDED
                    )
                )
                .nestedScroll(connection)
        ) {
            val computedProgress by remember {
                derivedStateOf {
                    if (swipingState.progress.to == SwipingStates.COLLAPSED) {
                        swipingState.progress.fraction
                    } else {
                        1f - swipingState.progress.fraction
                    }
                }
            }
            content(computedProgress)
        }
    }
}
