package co.yore.splitnpay

import android.graphics.Paint
import android.text.TextPaint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import co.yore.splitnpay.you_will_get_card.splitted
import coil.compose.AsyncImage
import java.text.DecimalFormat

enum class SummaryMode{
    CATEGORY,
    TIME;
    val next: SummaryMode
    get(){
        val index = this.ordinal
        var nextIndex = index+1
        val vals = enumValues<SummaryMode>()
        val count = vals.size
        nextIndex %= count
        return vals[nextIndex]
    }
}

@Composable
fun ExpenseDemo() {
    val numberFormatter = remember {
        mutableStateOf(DecimalFormat("#,###,###"))
    }
    val pieChartData = remember {
        mutableStateOf(listOf(
            PieData(
                Color(0xffFF4077),
                0.5f
            ),
            PieData(
                Color(0xff1A79E5),
                0.5f
            )
        ))
    }
    val categoryExpenseData = remember {
        mutableStateOf(listOf(
            CategoryExpense(
                icon = R.drawable.ic_trip_icon,
                category = "Trip",
                description = "Business Trip",
                count = 1,
                amount = 500.00f,
                tint = Color(0xffFF4077)
            ),
            CategoryExpense(
                icon = R.drawable.ic_food,
                category = "Food",
                description = "BFFs",
                count = 5,
                amount = 2500.00f,
                tint = Color(0xff1A79E5)
            )
        ))
    }
    Box(
        modifier = Modifier
            .padding(
                top = 19.dep(),
                start = 21.dep(),
                end = 21.dep()
            )
            .fillMaxSize()
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
        ){
            var mode by remember {
                mutableStateOf(SummaryMode.CATEGORY)
            }
            val modeString by remember {
                derivedStateOf {
                    if(mode==SummaryMode.CATEGORY){
                        "Expense Category"
                    }
                    else{
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
            ){
                Row(
                    modifier = Modifier
                        .height(29.dep())
                        .clip(RoundedCornerShape(14.dep()))
                        .clickable {
                            mode = mode.next
                        }
                        .background(Color(0xffEDF5FF))
                        .padding(horizontal = 14.dep()),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    RobotoText(
                        modeString,
                        fontSize = 14.sep(),
                        fontWeight = FontWeight.Bold,
                        color = Color(0xff1A79E5),
                        modifier = Modifier.animateContentSize()
                    )
                    Spacer(modifier = Modifier.width(7.dep()))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_down_chevron),
                        contentDescription = "",
                        modifier = Modifier
                            .size(6.86.dep()),
                        tint = Color(0xff1B79E6)
                    )
                }
                Row {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_calendar_icon),
                        contentDescription = "",
                        modifier = Modifier
                            .size(14.dep())
                    )
                    Spacer(modifier = Modifier.width(4.62.dep()))
                    RobotoText(
                        "Content",
                        fontSize = 12.sep(),
                        color = Color(0xff243257)
                    )
                }
            }
            AnimatedVisibility(
                mode==SummaryMode.CATEGORY,
                enter = fadeIn(),
                exit = fadeOut()
            ){
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ){
                    Spacer(modifier = Modifier.height(42.dep()))
                    Box(
                        modifier = Modifier
                            .size(227.dep())
                            .align(Alignment.CenterHorizontally),
                        contentAlignment = Alignment.Center
                    ){
                        val stroke = with(LocalDensity.current){
                            50.dep().toPx()
                        }

                        Canvas(
                            modifier = Modifier
                                .padding(25.dep())
                                .fillMaxSize()
                        ){
                            var start = -90f
                            pieChartData.value.forEach {
                                val used = 360f*it.portion
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
                        ){
                            RobotoText(
                                "Total share",
                                color = Color(0xff677C91),
                                fontSize = 13.sep()
                            )
                            Spacer(modifier = Modifier.height(3.dep()))
                            Row(){
                                RobotoText(
                                    "₹ ",
                                    fontSize = 10.sep(),
                                    color = Color(0xff243257),
                                    modifier = Modifier
                                        .alignByBaseline()
                                )
                                RobotoText(
                                    "500.",
                                    fontSize = 15.sep(),
                                    color = Color(0xff243257),
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .alignByBaseline()
                                )
                                RobotoText(
                                    "00",
                                    fontSize = 15.sep(),
                                    color = Color(0xff243257),
                                    modifier = Modifier
                                        .alignByBaseline()
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(28.dep()))
                    Row(
                        modifier = Modifier
                            .padding(
                                start = 16.dep(),
                                end = 7.dep()
                            )
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        RobotoText(
                            "Expense Categories",
                            color = Color(0xff243257),
                            fontSize = 18.sep(),
                            fontWeight = FontWeight.Bold
                        )
                        Row {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_search),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(19.dep()),
                                tint = Color(0xff1B79E6)
                            )
                            Spacer(modifier = Modifier.width(20.dep()))
                            Icon(
                                painter = painterResource(id = R.drawable.ic_filter),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(21.dep()),
                                tint = Color(0xff1B79E6)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dep()))
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                    ){
                        items(categoryExpenseData.value){
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
                            ){
                                Column(){
                                    Row(
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        AsyncImage(
                                            model = it.icon,
                                            contentDescription = "",
                                            modifier = Modifier
                                                .size(15.28.dep())
                                        )
                                        Spacer(modifier = Modifier.width(2.dep()))
                                        RobotoText(
                                            it.category,
                                            fontSize = 14.sep(),
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xff243257),
                                            modifier = Modifier
                                                //.alignBy(FirstBaseline)
                                                .offset(y = -4.dep())
                                        )
                                        Spacer(modifier = Modifier.width(6.dep()))
                                        RobotoText(
                                            "(${it.description})",
                                            fontSize = 11.sep(),
                                            color = Color(0xff5A87BB),
                                            modifier = Modifier
                                                //.alignBy(FirstBaseline)
                                                .offset(y = -2.dep())
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(19.dep()))
                                    Row(){
                                        RobotoText(
                                            "Number of expenses: ",
                                            color = Color(0xff677C91),
                                            fontSize = 11.sep()
                                        )
                                        RobotoText(
                                            String.format("%02d", it.count),
                                            color = Color(0xff243257),
                                            fontSize = 11.sep()
                                        )
                                    }
                                }
                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .width(50.dep())
                                        .background(Color.Red)
                                ){

                                }
                                Column(
                                    horizontalAlignment = Alignment.End
                                ) {
                                    Row(){
                                        RobotoText(
                                            "₹ ",
                                            color = Color(0xff243257),
                                            fontSize = 9.sep(),
                                            modifier = Modifier.alignByBaseline()
                                        )
                                        RobotoText(
                                            numberFormatter.value.format(it.amount.splitted().whole),
                                            fontSize = 12.sep(),
                                            color = Color(0xff243257),
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.alignByBaseline()
                                        )
                                        RobotoText(
                                            ".${it.amount.splitted().decString}",
                                            color = Color(0xff243257),
                                            fontSize = 6.sep(),
                                            modifier = Modifier.alignByBaseline()
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(16.dep()))
                                    Icon(
                                        painter = painterResource(R.drawable.ic_right_chevron),
                                        contentDescription = "",
                                        modifier = Modifier
                                            .size(20.dep())
                                            .clip(CircleShape)
                                            .background(Color(0xff1A79E5))
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
            AnimatedVisibility(
                mode == SummaryMode.TIME,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ){
                    Spacer(modifier = Modifier.height(23.dep()))
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 17.dep())
                            .fillMaxWidth()
                            .height(239.dep())
                    ){
                        BarGraph(
                            modifier = Modifier.fillMaxSize(),
                            data = BarGraphData(
                                xValues = listOf(
                                    BarGraphData.XValue(
                                        axisLabel = "Jan",
                                        value = 500.0f,
                                        label = "500.00",
                                        color = Color(0xff35BAE9)
                                    ),
                                    BarGraphData.XValue(
                                        axisLabel = "Feb",
                                        value = 1500.0f,
                                        label = "1500.00",
                                        color = Color(0xff35BAE9)
                                    )
                                ),
                                yAxisLabels = listOf(
                                    BarGraphData.YAxisValue(
                                        value = 10000f,
                                        label = "10k"
                                    ),
                                    BarGraphData.YAxisValue(
                                        value = 20000f,
                                        label = "20k"
                                    ),
                                    BarGraphData.YAxisValue(
                                        value = 30000f,
                                        label = "30k"
                                    )
                                )
                            ),
                        )
                    }
                }
            }
        }
    }
}

data class BarGraphData(
    val xValues: List<XValue>,
    val yAxisLabels: List<YAxisValue>
){
    data class YAxisValue(
        val value: Float,
        val label: String
    )
    data class XValue(
        val axisLabel: String,
        val value: Float,
        val label: String,
        val color: Color
    )
}

@Composable
fun BarGraph(
    modifier: Modifier = Modifier,
    data: BarGraphData,
    yAxisColor: Color = Color.Gray,
    yAxisStrokeWidth: Float = 4f,
    xAxisColor: Color = Color.Gray,
    xAxisStrokeWidth: Float = 4f,
    yAxisLabelColor: Color = Color.Gray
) {
    val yAxisLabelFontSize = with(LocalDensity.current){
        11.sep().toPx()
    }
    Canvas(modifier = modifier){
        val w = size.width
        val h = size.height

        val paddingFactor = 0.0917f
        val yAxisLabelGapUnitFactor = 0.0428f
        val yAxisLabelGapUnit = w*yAxisLabelGapUnitFactor

        val padding = w*paddingFactor
        drawLine(//yAxis
            yAxisColor,
            start = Offset(padding,0f),
            end = Offset(padding,h - padding),
            strokeWidth = yAxisStrokeWidth
        )
        drawLine(
            xAxisColor,
            start = Offset(padding,h-padding),
            end = Offset(w,h-padding),
            strokeWidth = xAxisStrokeWidth
        )
        val yAxisLabelValuesSum = data.yAxisLabels.sumOf {
            it.value.toDouble()
        }.toFloat()
        val yAxisLabelMinValue = data.yAxisLabels.minOf {
            it.value
        }
        val yAxisLabelGap = (h - padding - yAxisLabelGapUnit*2f)/(data.yAxisLabels.size - 1)
        var yAxisLabelStart = h - padding - yAxisLabelGapUnit
        val yAxisLabelPaint = TextPaint().apply {
            color = yAxisLabelColor.native
            textSize = yAxisLabelFontSize
        }
        data.yAxisLabels.forEach {
            myDrawText(
                it.label,
                0f,
                yAxisLabelStart,
                yAxisLabelPaint
            )
            yAxisLabelStart -= ((it.value-yAxisLabelMinValue)/yAxisLabelValuesSum)*(h - padding - yAxisLabelGapUnit*2f)
        }
    }
}

val Color.native: Int
get(){
    return -0x1000000 or
            ((red * 255.0f + 0.5f).toInt() shl 16) or
            ((green * 255.0f + 0.5f).toInt() shl 8) or (blue * 255.0f + 0.5f).toInt()
}

fun DrawScope.myDrawText(
    text: String, x: Float, y: Float, paint: Paint
){
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