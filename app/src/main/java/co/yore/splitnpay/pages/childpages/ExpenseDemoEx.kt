package co.yore.splitnpay.pages.childpages

import android.graphics.Paint
import android.text.TextPaint
import android.view.MotionEvent
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.yore.splitnpay.R
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.libs.coloredShadow
import co.yore.splitnpay.libs.jerokit.*
import co.yore.splitnpay.libs.locals.RobotoText
import co.yore.splitnpay.libs.locals.localCurrency
import co.yore.splitnpay.models.*
import co.yore.splitnpay.ui.theme.*
import coil.compose.AsyncImage
import java.lang.Math.sqrt
import java.text.DecimalFormat

@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@Composable
fun ExpenseDemo(
    expensesCategories: List<CategoryExpense> = listState(key = DataIds.expensesCategories),
    expenseBarChartList: List<ExpenseChartData> = listState(key = DataIds.expenseBarChartList),
    expensePieChartList: List<PieData> = listState(key = DataIds.expensePieChartList),
    expensesOvertimeTotal: Float = floatState(key = DataIds.expensesOvertimeTotal).value,
    expensesTimeFrameText: String = stringState(key = DataIds.expensesTimeFrameText).value,
    expensesOvertimeTotalTransactions: String = stringState(key = DataIds.expensesOvertimeTotalTransactions).value,
    expenseTimeFrame: String = stringState(key = DataIds.expenseTimeFrame).value,
    timeOption: TimeOptionData = tState<TimeOptionData>(key = DataIds.filterTimeFrame).value,
    notifier: NotificationService = notifier(),
    mode: SummaryMode = tState<SummaryMode>(key = DataIds.summaryMode).value
) {
    val numberFormatter = remember {
        mutableStateOf(DecimalFormat("#,###,###"))
    }

    Column(
        modifier = Modifier
            .padding(
                start = 21.dep(),
                end = 21.dep()
            )
            .fillMaxSize()
    ) {
        val modeString by remember(mode) {
            derivedStateOf {
                if (mode == SummaryMode.CATEGORY) {
                    "Expense Category"
                } else {
                    "Overtime"
                }
            }
        }

        Row(
            modifier = Modifier
                .padding(8.dep())
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .height(29.dep())
                    .clip(RoundedCornerShape(14.dep()))
                    .clickable {
                        notifier.notify(DataIds.summaryMode)
                    }
                    .background(Zumthor)
                    .padding(horizontal = 14.dep()),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RobotoText(
                    modeString,
                    fontSize = 14.sep(),
                    fontWeight = FontWeight.Bold,
                    color = CuriousBlue,
                    modifier = Modifier.animateContentSize()
                )
                7.sx()
                Icon(
                    painter = painterResource(id = R.drawable.ic_down_chevron),
                    contentDescription = "",
                    modifier = Modifier
                        .size(6.86.dep()),
                    tint = CuriousBlue1
                )
            }
            TimeOption(timeOption){
                notifier.notify(DataIds.expenseTimeMode)
            }
        }
        AnimatedContent(
            targetState = mode,
            /*transitionSpec = {
                (slideInHorizontally { -it }  with slideOutHorizontally { it } )
                    .using(
                        SizeTransform(clip = false)
                    )
            }*/
            transitionSpec = {
                fadeIn(animationSpec = tween(500)) with
                    fadeOut(animationSpec = tween(500))
            }
        ) {
            when (it) {
                SummaryMode.CATEGORY -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        43.sy()
                        Box(
                            modifier = Modifier
                                .size(227.dep())
                                .align(Alignment.CenterHorizontally),
                            contentAlignment = Alignment.Center
                        ) {
                            val stroke = with(LocalDensity.current) {
                                50.dep().toPx()
                            }

                            Canvas(
                                modifier = Modifier
                                    .padding(25.dep())
                                    .fillMaxSize()
                            ) {
                                var start = -90f
                                expensePieChartList.forEach {
                                    val used = 360f * it.portion
                                    drawArc(
                                        color = it.color,
                                        useCenter = false,
                                        startAngle = start,
                                        sweepAngle = used,
                                        style = Stroke(
                                            width = stroke
                                        ),
                                        size = this.size
                                    )
                                    start += used
                                }
                            }
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                RobotoText(
                                    stringResource(R.string.total_share),
                                    color = Lynch,
                                    fontSize = 13.sep()
                                )
                                3.sy()
                                Row() {
                                    RobotoText(
                                        "${localCurrency.current} ",
                                        fontSize = 10.sep(),
                                        color = CloudBurst,
                                        modifier = Modifier
                                            .alignByBaseline()
                                    )
                                    RobotoText(
                                        expensesOvertimeTotal.splitted().wholeString,
                                        fontSize = 15.sep(),
                                        color = CloudBurst,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier
                                            .alignByBaseline()
                                    )
                                    RobotoText(
                                        text = ". ${expensesOvertimeTotal.splitted().decString}",
                                        fontSize = 15.sep(),
                                        color = CloudBurst,
                                        modifier = Modifier
                                            .alignByBaseline()
                                    )
                                }
                            }
                        }
                        28.sy()
                        Row(
                            modifier = Modifier
                                .padding(
                                    start = 16.dep(),
                                    end = 7.dep()
                                )
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            RobotoText(
                                text = stringResource(R.string.expense_categories),
                                color = CloudBurst,
                                fontSize = 18.sep(),
                                fontWeight = FontWeight.Bold
                            )
                            Row {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_search),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(19.dep()),
                                    tint = CuriousBlue1
                                )
                                20.sx()
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_filter),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(21.dep())
                                        .clickable {
                                            notifier.notify(DataIds.expenseCategoryFilterClick)
                                        },
                                    tint = CuriousBlue1
                                )
                            }
                        }
                        16.sy()
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .fadingEdge(),
                            contentPadding = PaddingValues(vertical = 8.dep())
                        ) {
                            items(expensesCategories) {
                                Row(
                                    modifier = Modifier
                                        .padding(bottom = 16.dep())
                                        .fillMaxWidth()
                                        .wrapContentHeight()
                                        .clip(
                                            RoundedCornerShape(8.dep())
                                        )
                                        .background(it.tint.copy(alpha = 0.03f))
                                        .clickable { }
                                        .border(
                                            BorderStroke(
                                                width = 1.dep(),
                                                color = it.tint
                                            ),
                                            shape = RoundedCornerShape(8.dep())
                                        )
                                        .padding(
                                            horizontal = 19.dep(),
                                            vertical = 13.dep()
                                        ),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column() {
                                        Row(
                                            verticalAlignment = Alignment.Top
                                        ) {
                                            AsyncImage(
                                                model = it.icon,
                                                contentDescription = "",
                                                modifier = Modifier
                                                    .size(15.28.dep())
                                            )
                                            2.sx()
                                            RobotoText(
                                                it.category,
                                                fontSize = 14.sep(),
                                                fontWeight = FontWeight.Bold,
                                                color = CloudBurst,
                                                modifier = Modifier
                                                    // .alignBy(FirstBaseline)
                                                    .offset(y = -4.dep())
                                            )
                                            6.sx()
                                            RobotoText(
                                                "(${it.description})",
                                                fontSize = 11.sep(),
                                                color = SteelBlue,
                                                modifier = Modifier
                                                    // .alignBy(FirstBaseline)
                                                    .offset(y = -2.dep())
                                            )
                                        }
                                        19.sy()
                                        Row() {
                                            RobotoText(
                                                stringResource(R.string.number_of_expenses) + ": ",
                                                color = Lynch,
                                                fontSize = 11.sep()
                                            )
                                            RobotoText(
                                                String.format("%02d", it.count),
                                                color = CloudBurst,
                                                fontSize = 11.sep()
                                            )
                                        }
                                    }
                                    Box(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .width(50.dep())
                                            .background(Color.Red)
                                    ) {

                                    }
                                    Column(
                                        horizontalAlignment = Alignment.End
                                    ) {
                                        Row() {
                                            RobotoText(
                                                "${localCurrency.current} ",
                                                color = CloudBurst,
                                                fontSize = 9.sep(),
                                                modifier = Modifier.alignByBaseline()
                                            )
                                            RobotoText(
                                                it.amount.splitted().wholeString,
                                                fontSize = 12.sep(),
                                                color = CloudBurst,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.alignByBaseline()
                                            )
                                            RobotoText(
                                                ".${it.amount.splitted().decString}",
                                                color = CloudBurst,
                                                fontSize = 6.sep(),
                                                modifier = Modifier.alignByBaseline()
                                            )
                                        }
                                        16.sy()
                                        Icon(
                                            painter = painterResource(R.drawable.ic_right_chevron),
                                            contentDescription = "",
                                            modifier = Modifier
                                                .size(20.dep())
                                                .clip(CircleShape)
                                                .background(it.tint)
                                                .clickable {

                                                }
                                                .padding(6.dep()),
                                            tint = Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                SummaryMode.TIME -> {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        23.sy()
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 17.dep())
                                .fillMaxWidth()
                                .height(239.dep())
                        ) {
                            var labelData by remember {
                                mutableStateOf<LabelData?>(null)
                            }
                            var labelOffset by remember {
                                mutableStateOf(
                                    TouchArea(
                                        Offset.Zero,
                                        Size.Zero
                                    )
                                )
                            }
                            var dragX by remember {
                                mutableStateOf(0f)
                            }
                            LaunchedEffect(key1 = labelData) {
                                dragX = 0f
                                labelOffset = labelData?.touchArea ?: TouchArea(
                                    Offset.Zero,
                                    Size.Zero
                                )
                            }
                            val popupX by animateFloatAsState(targetValue = labelOffset.offset.x)
                            val popupY by animateFloatAsState(targetValue = labelOffset.offset.y)
                            var xAxisLabels by remember {
                                mutableStateOf(
                                    listOf(
                                        "Jan",
                                        "Feb",
                                        "Mar"
                                    )
                                )
                            }
                            var values by remember {
                                mutableStateOf(
                                    listOf(
                                        12000f,
                                        25000f,
                                        45000f
                                    )
                                )
                            }
                            BarGraph(
                                modifier = Modifier.fillMaxSize(),
                                onDrag = {
                                    dragX += it
                                },
                                data = BarGraphData(
                                    xAxisGap = 5f,
                                    xValuesCount = expenseBarChartList.size,
                                    xValueCallback = {
//                                        (it + 1) * 5000f
                                        expenseBarChartList[it].yAxis
                                    },
                                    xAxisLabelCallback = {
//                                        xAxisLabels[it]
                                        expenseBarChartList[it].xAxis
                                    },
                                    xLabelCallback = {
                                        // ((it+1)*5000f).toString()
                                        ""
                                    },
                                    {
                                        PictonBlue
                                    },
                                    yAxisLabelCount = 6,
                                    maxYAxisValue = 90000f,
                                    yAxisLabelCallback = {
                                        "${(it / 1000).toInt()}k"
                                    },
                                    xAxisLabelColor = {
                                        Color.Black
                                    },
                                    yAxisLabelColor = {
                                        Color.Gray
                                    },
                                    strokeWidth = 5f,
                                    axisColor = Color.Gray,
                                    axisLabelFontSize = 11f,
                                    highlightedBarColor = CuriousBlue2,
                                    highlightedXLabelColor = PictonBlue,
                                    disabledTextColor = Color.LightGray,
                                    enabled = {
                                        expenseBarChartList[it].enabled
                                    },
                                )
                            ) { index, touchArea ->
                                labelData = if (index == -1) {
                                    null
                                } else {
                                    LabelData(
                                        index,
                                        touchArea
                                    )
                                }
                            }

                            val density = LocalDensity.current.density
                            if (labelData != null) {
                                Column(
                                    modifier = Modifier
                                        /*.offset {
                                            IntOffset(
                                                popupX.toInt(),

                                            )
                                        }*/
                                        .offset(
                                            x = ((popupX.toInt() + dragX) / density).dp,
                                            y = ((popupY.toInt() - 250) / density).dp
                                        )
                                        /*.draggable(
                                            rememberDraggableState(onDelta = { dragX += it }),
                                            Orientation.Horizontal
                                        )*/
                                        .coloredShadow(
                                            color = BaliHai,
                                            borderRadius = 5.dep(),
                                            blurRadius = 19.dep(),
                                            offsetY = 3.dep()
                                        )
                                        .wrapContentSize()
                                        .clip(RoundedCornerShape(5.dep()))
                                        .background(Color.White)
                                        .padding(9.dep())
                                ) {
//                                    Text(chartList.value.getOrNull(labelData?.index ?: 0)?.toString() ?: "")
                                    RobotoText(
                                        text = expenseBarChartList[labelData!!.index].description,
                                        fontSize = 12.sep(),
                                        color = Lynch,
                                        letterSpacing = 0.67.sep()
                                    )
                                    4.sy()
                                    Row() {
                                        RobotoText(
                                            text = "${localCurrency.current} ",
                                            color = CloudBurst,
                                            fontSize = 9.sep(),
                                            modifier = Modifier.alignByBaseline()
                                        )
                                        RobotoText(
                                            text = numberFormatter.value.format(expenseBarChartList[labelData!!.index].yAxis.splitted().whole.toFloat()),
                                            fontSize = 12.sep(),
                                            color = CloudBurst,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.alignByBaseline()
                                        )
                                        RobotoText(
                                            text = ".${expenseBarChartList[labelData!!.index].yAxis.splitted().decString}",
                                            color = CloudBurst,
                                            fontSize = 6.sep(),
                                            modifier = Modifier.alignByBaseline()
                                        )
                                    }
                                }
                            }
                        }

                        30.sy()

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fadingEdge(),
                            contentPadding = PaddingValues(top = 4.dep())
                        ) {
                            stickyHeader {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
//                                .background(color = Color.White)
                                ) {
                                    RobotoText(
                                        stringResource(R.string.overtime),
                                        fontSize = 18.sep(),
                                        fontWeight = FontWeight.Bold,
                                        color = CloudBurst,
                                        modifier = Modifier.padding(start = 20.dep())
                                    )
                                    27.sy()
                                    Box(
                                        modifier = Modifier
                                            .coloredShadow(
                                                color = Ghost.copy(alpha = 0.3f),
                                                blurRadius = 33.dep(),
                                                offsetX = 7.dep(),
                                                offsetY = 7.dep()
                                            )
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(11.dep()))
                                            .background(Color.White)
                                            .padding(20.dep())
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(4.dep()))
                                                .background(TwilightBlue)
                                                .border(
                                                    BorderStroke(
                                                        width = 1.dep(),
                                                        color = PictonBlue
                                                    ),
                                                    shape = RoundedCornerShape(4.dep())
                                                )
                                                .padding(
                                                    horizontal = 24.dep(),
                                                    vertical = 13.dep()
                                                )
                                        ) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Column(
                                                    horizontalAlignment = Alignment.CenterHorizontally
                                                ) {
                                                    RobotoText(
                                                        stringResource(R.string.total_paid),
                                                        fontSize = 12.sep(),
                                                        color = VeniceBlue1
                                                    )
                                                    9.sy()
                                                    Row() {
                                                        RobotoText(
                                                            "${localCurrency.current} ",
                                                            fontSize = 9.sep(),
                                                            color = CloudBurst,
                                                            modifier = Modifier.alignByBaseline()
                                                        )
                                                        RobotoText(
                                                            expensesOvertimeTotal.splitted().wholeString,
                                                            fontWeight = FontWeight.Bold,
                                                            fontSize = 14.sep(),
                                                            color = CloudBurst,
                                                            modifier = Modifier.alignByBaseline()
                                                        )
                                                        RobotoText(
                                                            ". ${expensesOvertimeTotal.splitted().decString}",
                                                            fontSize = 6.sep(),
                                                            color = CloudBurst,
                                                            modifier = Modifier.alignByBaseline()
                                                        )
                                                    }
                                                }
                                                Column(
                                                    horizontalAlignment = Alignment.CenterHorizontally
                                                ) {
                                                    myText(
                                                        expenseTimeFrame,
                                                        12,
                                                        0xff0a688a
                                                    )
                                                    10.sy()
                                                    myText(
                                                        expensesTimeFrameText,
                                                        12,
                                                        0xff243257
                                                    )
                                                }
                                                Column(
                                                    horizontalAlignment = Alignment.CenterHorizontally
                                                ) {
                                                    myText(
                                                        stringResource(R.string.number_of_transaction),
                                                        12,
                                                        0xff0a688a
                                                    )
                                                    10.sy()
                                                    myText(
                                                        expensesOvertimeTotalTransactions,
                                                        12,
                                                        0xff243257
                                                    )
                                                }
                                            }
                                        }
                                    }
                                    28.sy()
                                }
                            }

                            stickyHeader {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .padding(
                                                start = 16.dep(),
                                                end = 7.dep()
                                            )
                                            .fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        RobotoText(
                                            text = stringResource(R.string.categories),
                                            color = CloudBurst,
                                            fontSize = 18.sep(),
                                            fontWeight = FontWeight.Bold
                                        )
                                        Row {
                                            Icon(
                                                painter = painterResource(id = R.drawable.ic_search),
                                                contentDescription = "",
                                                modifier = Modifier
                                                    .size(19.dep()),
                                                tint = CuriousBlue1
                                            )
                                            20.sx()
                                            Icon(
                                                painter = painterResource(id = R.drawable.ic_filter),
                                                contentDescription = "",
                                                modifier = Modifier
                                                    .size(21.dep())
                                                    .clickable {
                                                        notifier.notify(DataIds.expenseCategoryFilterClick)
                                                    },
                                                tint = CuriousBlue1
                                            )
                                        }
                                    }
                                    16.sy()
                                }
                            }

                            items(expensesCategories) {
                                Row(
                                    modifier = Modifier
                                        .padding(bottom = 16.dep())
                                        .fillMaxWidth()
                                        .wrapContentHeight()
                                        .clip(
                                            RoundedCornerShape(8.dep())
                                        )
                                        .background(it.tint.copy(alpha = 0.03f))
                                        .clickable { }
                                        .border(
                                            BorderStroke(
                                                width = 1.dep(),
                                                color = it.tint
                                            ),
                                            shape = RoundedCornerShape(8.dep())
                                        )
                                        .padding(
                                            horizontal = 19.dep(),
                                            vertical = 13.dep()
                                        ),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column() {
                                        Row(
                                            verticalAlignment = Alignment.Top
                                        ) {
                                            AsyncImage(
                                                model = it.icon,
                                                contentDescription = "",
                                                modifier = Modifier
                                                    .size(15.28.dep())
                                            )
                                            2.sx()
                                            RobotoText(
                                                it.category,
                                                fontSize = 14.sep(),
                                                fontWeight = FontWeight.Bold,
                                                color = CloudBurst,
                                                modifier = Modifier
                                                    // .alignBy(FirstBaseline)
                                                    .offset(y = -4.dep())
                                            )
                                            6.sx()
                                            RobotoText(
                                                "(${it.description})",
                                                fontSize = 11.sep(),
                                                color = SteelBlue,
                                                modifier = Modifier
                                                    // .alignBy(FirstBaseline)
                                                    .offset(y = -2.dep())
                                            )
                                        }
                                        19.sy()
                                        Row() {
                                            RobotoText(
                                                text = stringResource(R.string.number_of_expanses) + ": ",
                                                color = Lynch,
                                                fontSize = 11.sep()
                                            )
                                            RobotoText(
                                                String.format("%02d", it.count),
                                                color = CloudBurst,
                                                fontSize = 11.sep()
                                            )
                                        }
                                    }
                                    Box(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .width(50.dep())
                                            .background(Color.Red)
                                    ) {

                                    }
                                    Column(
                                        horizontalAlignment = Alignment.End
                                    ) {
                                        Row() {
                                            RobotoText(
                                                text = "${localCurrency.current} ",
                                                color = CloudBurst,
                                                fontSize = 9.sep(),
                                                modifier = Modifier.alignByBaseline()
                                            )
                                            RobotoText(
                                                text = numberFormatter.value.format(it.amount.splitted().whole.toFloat()),
                                                fontSize = 12.sep(),
                                                color = CloudBurst,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.alignByBaseline()
                                            )
                                            RobotoText(
                                                text = ".${it.amount.splitted().decString}",
                                                color = CloudBurst,
                                                fontSize = 6.sep(),
                                                modifier = Modifier.alignByBaseline()
                                            )
                                        }
                                        16.sy()
                                        Icon(
                                            painter = painterResource(R.drawable.ic_right_chevron),
                                            contentDescription = "",
                                            modifier = Modifier
                                                .size(20.dep())
                                                .clip(CircleShape)
                                                .background(CuriousBlue)
                                                .clickable {

                                                }
                                                .padding(6.dep()),
                                            tint = Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

sealed class TimeOptionData(val text: String){
    class Outlined(val title: String) : TimeOptionData(title)
    class Normal(val title: String) : TimeOptionData(title)
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TimeOption(
    data: TimeOptionData,
    onClick: () -> Unit
) {
    val animDuration = remember {
        500
    }
    val outlined by remember(data) {
        derivedStateOf {
            data is TimeOptionData.Outlined
        }
    }
    val borderStroke by animateFloatAsState(
        targetValue = if (outlined) 1f else -1f,
        tween(animDuration)
    )
    val borderColor by animateColorAsState(
        targetValue = if (outlined) SilverSand else Color.Transparent,
        tween(animDuration)
    )
    val paddingOpacity by animateFloatAsState(
        targetValue = if (outlined) 1f else 0f,
        tween(animDuration)
    )
    Row(
        modifier = Modifier
            .padding(start = 8.dep())
            .animateContentSize(
                // tween(500)
            )
            .height(30.dep())
            .clip(CircleShape)
            .border(
                width = borderStroke.dep(),
                color = borderColor,
                CircleShape
            )
            .clickable(
                rippleColor = CuriousBlue
            ) {
                onClick()
            }
            .padding(
                start = (paddingOpacity * 21).dep(),
                end = (paddingOpacity * 12).dep()
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        /*AnimatedVisibility(
            visible = !outlined,
            enter = fadeIn(tween(animDuration))+ scaleIn(tween(animDuration)),
            exit = fadeOut(tween(animDuration))+ scaleOut(tween(animDuration))
        ) {*/
        Row(){
            Icon(
                painter = painterResource(id = R.drawable.calendar_1),
                contentDescription = "",
                modifier = Modifier
                    .size(((1f - paddingOpacity) * 14).dep())
            )
            ((1f - paddingOpacity) * 4.62).sx()
        }
        // }
        /*RobotoText(
            text = data.text,
            fontSize = if(data is TimeOptionData.Outlined) 14.sep() else 12.sep(),
            color = Darkblue,
            fontWeight = if(data is TimeOptionData.Outlined) FontWeight.Bold else FontWeight.Normal
        )*/
        val depx = with(LocalDensity.current){2.dep().toPx()}
        LazyRow(
            modifier = Modifier
                .fadingEdge(
                    horizontal = true,
                    length = depx
                ),
            contentPadding = PaddingValues(horizontal = 2.dep())
        ){
            item{
                TextAnimation(
                    text = data.text,
                    animate = true,
                    fontFamily = robotoFonts,
                    fontSize = if (data is TimeOptionData.Outlined) 14.sep() else 12.sep(),
                    color = CloudBurst,
                    fontWeight = if (data is TimeOptionData.Outlined) FontWeight.Bold else FontWeight.Normal,
                    animationSpec = tween(animDuration),
                    maxLines = 1
                )
            }
        }

        /*AnimatedVisibility(
            data is TimeOptionData.Outlined,
            enter = fadeIn(tween(animDuration))+ scaleIn(tween(animDuration)),
            exit = fadeOut(tween(animDuration))+ scaleOut(tween(animDuration))
        ){*/
        Row(){
            (paddingOpacity * 4.62).sx()
            Icon(
                painter = painterResource(id = R.drawable.down_arrow),
                contentDescription = "",
                modifier = Modifier
                    .size((paddingOpacity * 7).dep()),
                tint = CloudBurst
            )
        }
        // }
    }
}

@Composable
fun myText(
    text: String,
    size: Number,
    color: Any = Color.Unspecified,
    bold: Boolean = false
) {
    RobotoText(
        text = text,
        color = color.ensureColor,
        fontSize = size.sep(),
        fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal
    )
}

val Any.ensureColor: Color
    get() {
        return when (this) {
            is Color -> this
            is Int -> Color(this)
            is Long -> Color(this)
            else -> Color.Unspecified
        }
    }

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BarGraph(
    modifier: Modifier = Modifier,
    data: BarGraphData,
    onDrag: (Float) -> Unit,
    onBarClick: (Int, TouchArea?) -> Unit
) {
    val sepx = with(LocalDensity.current) {
        1.sep().toPx()
    }
    val depx = with(LocalDensity.current) {
        1.dep().toPx()
    }
    val touchAreas = remember {
        mutableStateListOf<TouchArea>()
    }
    var selectedIndex by remember {
        mutableStateOf(-1)
    }

    var touchState = remember { TouchState() }
    val dragOffset = remember { mutableStateOf(Offset.Zero) }
    Canvas(
        modifier = modifier
            .pointerInteropFilter { it ->
                val x = it.x
                val y = it.y
                when (it.action){
                    MotionEvent.ACTION_MOVE -> {
                        if (touchState.lastAction != null){
                            val dx = x - (touchState.lastPoint?.x ?: 0f)
                            val dy = y - (touchState.lastPoint?.y ?: 0f)
                            dragOffset.value = Offset(dragOffset.value.x + dx, dragOffset.value.y + dy)
                            onDrag(dx)
                        }
                        touchState.lastPoint = TouchState.Point(x, y)
                        touchState.lastAction = MotionEvent.ACTION_MOVE
                        true
                    }
                    MotionEvent.ACTION_CANCEL -> {
                        touchState.lastAction = null
                        touchState.lastPoint = null
                        touchState.firstPoint = null
                        true
                    }
                    MotionEvent.ACTION_DOWN -> {
                        touchState.firstPoint = TouchState.Point(x, y)
                        touchState.lastPoint = TouchState.Point(x, y)
                        touchState.lastAction = MotionEvent.ACTION_DOWN
                        true
                    }
                    MotionEvent.ACTION_UP -> {
                        if (
                            (touchState.firstPoint != null) && (touchState.firstPoint?.distance(x, y) ?: 0f < 5f)
                        ) {
                            val offset = Offset(x, y)
                            selectedIndex = touchAreas.indexOfFirst {
                                it.contains(offset)
                            }
                            onBarClick(selectedIndex, touchAreas.getOrNull(selectedIndex))
                        }
                        touchState.lastAction = null
                        touchState.lastPoint = null
                        touchState.firstPoint = null
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
            /*.pointerInput(Unit) {
                detectTapGestures { offset ->
                    selectedIndex = touchAreas.indexOfFirst {
                        it.contains(offset)
                    }
                    onBarClick(selectedIndex, touchAreas.getOrNull(selectedIndex))
                }
                detectDragGestures { change, dragAmount ->
                    Log.d("fjldjfld", "$dragAmount")
                    change.consume()
                }
            }*/
    ) {
        if (touchAreas.isEmpty()) {
            touchAreas.addAll(
                MutableList(data.xValuesCount) {
                    TouchArea(
                        Offset.Zero,
                        Size.Zero
                    )
                }
            )
        }
        val canvasWidth = size.width
        val canvasHeight = size.height

        val axisPaddingFactor = 0.0917f
        val axisInitialGapFactor = 0.0428f
        val axisInitialGap = canvasWidth * axisInitialGapFactor
        val axisPadding = canvasWidth * axisPaddingFactor
        val axisBottomY = canvasHeight - axisPadding
        val axisLabelPaint = TextPaint().apply {
            color = data.yAxisLabelColor().native
            textSize = data.axisLabelFontSize * sepx
        }
        val divs = data.yAxisLabelCount
        val f = (1f / divs.toFloat())
        val pack = data.maxYAxisValue / divs
        val avh = canvasHeight - axisPadding - axisInitialGap - data.strokeWidth / 2
        val yAxisHeight = canvasHeight - axisPadding
        /*drawLine(
            data.axisColor,
            start = Offset(axisPadding, 0f),
            end = Offset(axisPadding, axisBottomY),
            strokeWidth = data.strokeWidth
        )
        drawLine(
            data.axisColor,
            start = Offset(axisPadding, axisBottomY),
            end = Offset(canvasWidth, axisBottomY),
            strokeWidth = data.strokeWidth
        )
        for (i in 1..divs) {
            myDrawText(
                data.yAxisLabelCallback(i * pack),
                0f,
                yAxisHeight - f * avh * i,
                axisLabelPaint
            )
        }*/
        val gap = data.xAxisGap * depx
        val xValuesCount = data.xValuesCount
        // val labelSize = (canvasWidth - axisPadding - axisInitialGap - gap * (xValuesCount - 1)) / xValuesCount
        val labelSize = 32 * depx
        val maxYAxisValue = data.maxYAxisValue
        (0 until xValuesCount).forEach { i ->
            val xAxisLabelX = axisPadding + axisInitialGap + i * labelSize + i * gap
            val selected = selectedIndex == i
            myDrawText(
                data.xAxisLabelCallback(i),
                xAxisLabelX + dragOffset.value.x,
                canvasHeight,
                axisLabelPaint.apply {
                    color = if (selected) {
                        data.highlightedXLabelColor.native
                    } else if(!data.enabled(i)){
                        data.disabledTextColor.native
                    }
                    else {
                        data.xAxisLabelColor(i).native
                    }
                }
            )
            val height = ((data.xValueCallback(i) / maxYAxisValue) * avh).coerceAtMost(avh)
            val barLeft = axisPadding + axisInitialGap + i * (labelSize + gap)
            val barTop = canvasHeight - axisPadding - height - data.strokeWidth / 2f
            drawRect(
                color = if (selected) data.highlightedBarColor else data.barGraphColorCallback(i),
                topLeft = Offset(barLeft + dragOffset.value.x, barTop),
                size = Size(labelSize, height)
            )
            if (selected) {
                drawLine(
                    color = Color.Gray,
                    start = Offset(barLeft + dragOffset.value.x + labelSize, 0f),
                    end = Offset(barLeft + dragOffset.value.x + labelSize, canvasHeight - axisPadding),
                    strokeWidth = 5f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
                )
            }
            touchAreas[i] = TouchArea(
                Offset(barLeft + dragOffset.value.x, barTop),
                Size(labelSize, height)
            )
            val label = data.xLabelCallback(i)
            if (label.isNotEmpty()) {
                myDrawText(
                    label,
                    axisPadding + dragOffset.value.x + axisInitialGap + i * labelSize + i * gap,
                    canvasHeight - axisPadding - height - 10f,
                    axisLabelPaint
                )
            }
        }

        drawLine(
            data.axisColor,
            start = Offset(axisPadding, 0f),
            end = Offset(axisPadding, axisBottomY),
            strokeWidth = data.strokeWidth
        )
        drawLine(
            data.axisColor,
            start = Offset(axisPadding, axisBottomY),
            end = Offset(canvasWidth, axisBottomY),
            strokeWidth = data.strokeWidth
        )
        for (i in 1..divs) {
            myDrawText(
                data.yAxisLabelCallback(i * pack),
                0f,
                yAxisHeight - f * avh * i,
                axisLabelPaint
            )
        }
    }
}
data class TouchState(
    var firstPoint: TouchState.Point? = null,
    var lastPoint: TouchState.Point? = null,
    var lastAction: Int? = null
){
    data class Point(val x: Float, val y: Float){
        fun distance(x: Float, y: Float): Float {
            val dx = x - this.x
            val dy = y - this.y
            return kotlin.math.sqrt(dx * dx + dy * dy)
        }
    }
}
val Color.native: Int
    get() {
        return -0x1000000 or
            ((red * 255.0f + 0.5f).toInt() shl 16) or
            ((green * 255.0f + 0.5f).toInt() shl 8) or (blue * 255.0f + 0.5f).toInt()
    }

fun DrawScope.myDrawText(
    text: String,
    x: Float,
    y: Float,
    paint: Paint
) {
    drawContext.canvas.nativeCanvas.apply {
        drawText(
            text,
            x,
            y,
            paint
        )
    }
}

data class PieData(
    val color: Color,
    val portion: Float
)

data class CategoryExpense(
    val icon: Any?,
    val category: String,
    val description: String,
    val count: Int,
    val amount: Float,
    val tint: Color
)

@Preview
@Composable
fun ComposablePreview() {
    ExpenseDemo()
}

// //////////////////////
sealed interface ClickableItem

sealed class Device(val name: String) {
    object Camera : Device("Camera"), ClickableItem
    object Lamp : Device("Lamp")
    // ... etc, more devices
}

fun onDeviceItemClicked(item: ClickableItem) {
    when (item) {
        Device.Camera -> { /* do something */
        }
    }
}
