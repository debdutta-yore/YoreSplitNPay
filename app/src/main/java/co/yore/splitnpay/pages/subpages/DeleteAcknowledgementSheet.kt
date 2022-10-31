package co.yore.splitnpay.pages.subpages

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import co.yore.splitnpay.R
import co.yore.splitnpay.components.components.FontFamilyText
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.libs.jerokit.*
import co.yore.splitnpay.libs.jerokit.bottom_sheet.BottomSheetModel
import co.yore.splitnpay.models.DataIds
import co.yore.splitnpay.ui.theme.Alabaster
import co.yore.splitnpay.ui.theme.CloudBurst
import co.yore.splitnpay.ui.theme.DoveGray1
import co.yore.splitnpay.ui.theme.DustyGray
import kotlinx.coroutines.CoroutineScope

class SuccessUndoSheetModel(val callback: Callback) : BottomSheetModel {
    interface Callback{
        fun scope(): CoroutineScope
        fun undo()
        fun timerEnded()
        fun close()
        fun message(): String
        fun timeMillis(): Int
    }

    // ///////////////
    private val _resolver = Resolver()
    private val _notifier = NotificationService{id, arg ->
        when (id){
            DataIds.undo -> {
                callback.undo()
            }
            DataIds.timerEnded -> {
                callback.timerEnded()
            }
        }
    }

    // ///////////////
    override val resolver = _resolver
    override val notifier = _notifier
    override val scope get() = callback.scope()

    @Composable
    override fun Content() {
        SuccessUndoBottomSheet()
    }

    override fun initialize() {
        _message.value = callback.message()
        _timeMillis.value = callback.timeMillis()
        _timerStarted.value = true
    }

    override fun clear() {

    }

    override fun onBack() {
        callback.close()
    }

    // /////////
    private val _message = mutableStateOf("")
    private val _timerStarted = mutableStateOf(false)
    private val _timeMillis = mutableStateOf(0)

    // /////////
    init {
        _resolver.addAll(
            DataIds.message to _message,
            DataIds.timerStarted to _timerStarted,
            DataIds.timeMillis to _timeMillis
        )
    }
}

@Composable
fun SuccessUndoBottomSheet(
    message: String = stringState(DataIds.message).value,
    timerStarted: Boolean = boolState(key = DataIds.timerStarted).value,
    timeMillis: Int = intState(key = DataIds.timeMillis).value,
    notifier: NotificationService = notifier()
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Icon(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 9.dep()),
            tint = Color.Unspecified,
            painter = painterResource(id = R.drawable.ic_sheet_holder),
            contentDescription = "sheet holder"
        )
        28.sy()
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clip(CircleShape)
                .size(80.dep())
                .background(
                    color = Alabaster,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.success),
                contentDescription = "delete_group_success",
                tint = Color.Unspecified,
                modifier = Modifier.size(42.55.dep())
            )
        }
        21.sy()
        FontFamilyText(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            text = message,
            color = CloudBurst,
            fontSize = 16.sep()
        )
        40.sy()
        var dismissed by remember {
            mutableStateOf(false)
        }
        val enabled by remember(timerStarted) {
            derivedStateOf {
                timerStarted && !dismissed
            }
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(47.dep())
                .padding(
                    start = 30.dep(),
                    end = 29.dep()
                )
                .clip(RoundedCornerShape(23.5.dep()))
                .background(
                    Alabaster
                )
                .then(
                    if (enabled) {
                        Modifier
                            .clickable {
                                notifier.notify(DataIds.undo)
                            }
                    } else {
                        Modifier
                    }
                )
        ) {
            FontFamilyText(
                text = stringResource(R.string.undo),
                textAlign = TextAlign.Center,
                fontSize = 16.sep(),
                color = DoveGray1,
                lineHeight = 18.75.sep()
            )
            CanvasCountDownTimer(
                modifier = Modifier
                    .padding(
                        start = 30.dep(),
                        end = 29.dep()
                    )
                    .align(Alignment.CenterEnd),
                timeMillis = timeMillis,
                timerStarted = timerStarted
            ) {
                dismissed = true
                notifier.notify(DataIds.timerEnded)
            }
        }
        44.sy()
    }
}

@Composable
fun CanvasCountDownTimer(
    modifier: Modifier = Modifier,
    timeMillis: Int,
    timerStarted: Boolean,
    onDismiss: () -> Unit
) {
    val animateFloat = remember { Animatable(0f) }
    if (timerStarted) {
        LaunchedEffect(animateFloat) {
            animateFloat.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = timeMillis, easing = LinearEasing)
            )
        }

        if (animateFloat.value == 1f) {
            onDismiss()
        }
    }
    val context = LocalContext.current
    val depx = with(LocalDensity.current){1.dep().toPx()}
    Canvas(modifier = modifier) {
        val arcRadius = 15 * depx
        val borderRadius = 20 * depx
        val canvasWidth = size.width
        val canvasHeight = size.height

        drawArc(
            color = DustyGray,
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            size = Size(borderRadius, borderRadius),
            topLeft = Offset(
                (canvasWidth / 2) - (borderRadius / 2),
                canvasHeight / 2 - (borderRadius / 2)
            ),
            style = Stroke(2 * depx)
        )

        drawArc(
            color = DustyGray,
            startAngle = -90f,
            sweepAngle = 360f * (1f - animateFloat.value),
            useCenter = true,
            size = Size(arcRadius, arcRadius),
            topLeft = Offset(
                (canvasWidth / 2) - (arcRadius / 2),
                canvasHeight / 2 - (arcRadius / 2)
            ),
            style = Fill
        )
    }
}
