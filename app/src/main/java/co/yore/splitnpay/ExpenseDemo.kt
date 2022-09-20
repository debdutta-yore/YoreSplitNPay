package co.yore.splitnpay

import android.graphics.Paint
import android.text.TextPaint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
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
            .fillMaxSize()
            .background(Color.White)
            .padding(
                top = 19.dep(),
                start = 21.dep(),
                end = 21.dep()
            )
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
        ){
            var mode by remember {
                mutableStateOf(SummaryMode.TIME)
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
                        var labelData by remember {
                            mutableStateOf<LabelData?>(null)
                        }
                        var labelOffset by remember {
                            mutableStateOf(TouchArea(
                                Offset.Zero,
                                Size.Zero
                            ))
                        }
                        LaunchedEffect(key1 = labelData){
                            labelOffset = labelData?.touchArea?:TouchArea(
                                Offset.Zero,
                                Size.Zero
                            )
                        }
                        var xAxisLabels by remember {
                            mutableStateOf(listOf(
                                "Jan",
                                "Feb",
                                "Mar",
                            ))
                        }
                        var values by remember {
                            mutableStateOf(listOf(
                                12000f,
                                25000f,
                                45000f
                            ))
                        }
                        BarGraph(
                            modifier = Modifier.fillMaxSize(),
                            data = BarGraphData(
                                xAxisGap = 5f,
                                xValuesCount = values.size,
                                xValueCallback = {
                                    (it+1)*5000f
                                },
                                xAxisLabelCallback = {
                                    xAxisLabels[it]
                                },
                                xLabelCallback = {
                                    //((it+1)*5000f).toString()
                                                 ""
                                },
                                {
                                    Color(0xff35BAE9)
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
                                highlightedBarColor = Color.Red,
                                highlightedXLabelColor = Color.Blue
                            )
                        ){index,touchArea->
                            labelData = if(index==-1){
                                null
                            } else{
                                LabelData(
                                    index,
                                    touchArea
                                )
                            }
                        }
                        if(labelData!=null) {
                            Column(
                                modifier = Modifier
                                    .offset {
                                        IntOffset(
                                            labelOffset.offset.x.toInt(),
                                            labelOffset.offset.y.toInt() - 250
                                        )
                                    }
                                    .coloredShadow(
                                        color = Color(0xff8498AB),
                                        borderRadius = 5.dep(),
                                        blurRadius = 19.dep(),
                                        offsetY = 3.dep()
                                    )
                                    .wrapContentSize()
                                    .clip(RoundedCornerShape(5.dep()))
                                    .background(Color.White)
                                    .padding(9.dep())

                            ){
                                Text(values.getOrNull(labelData?.index?:0)?.toString()?:"")
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(34.dep()))
                    RobotoText(
                        "Overtime",
                        fontSize = 18.sep(),
                        fontWeight = FontWeight.Bold,
                        color = Color(0xff243257),
                        modifier = Modifier.padding(start = 20.dep())
                    )
                    Spacer(modifier = Modifier.height(27.dep()))
                    Box(
                        modifier = Modifier
                            .coloredShadow(
                                color = Color(0xffC6CFD8).copy(alpha = 0.3f),
                                blurRadius = 33.dep(),
                                offsetX = 7.dep(),
                                offsetY = 7.dep()
                            )
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(11.dep()))
                            .background(Color.White)
                            .padding(20.dep())
                    ){
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(4.dep()))
                                .background(Color(0xffF3FCFF))
                                .border(
                                    BorderStroke(
                                        width = 1.dep(),
                                        color = Color(0xff35BAE9)
                                    ),
                                    shape = RoundedCornerShape(4.dep())
                                )
                                .padding(
                                    horizontal = 24.dep(),
                                    vertical = 13.dep()
                                )
                        ){
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ){
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ){
                                    RobotoText(
                                        "Total Paid",
                                        fontSize = 12.sep(),
                                        color = Color(0xff0A688A),

                                        )
                                    Spacer(
                                        modifier = Modifier.height(9.dep())
                                    )
                                    Row(){
                                        RobotoText(
                                            "₹ ",
                                            fontSize = 9.sep(),
                                            color = Color(0xff243257),
                                            modifier = Modifier.alignByBaseline()
                                        )
                                        RobotoText(
                                            "500",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sep(),
                                            color = Color(0xff243257),
                                            modifier = Modifier.alignByBaseline()
                                        )
                                        RobotoText(
                                            ".00",
                                            fontSize = 6.sep(),
                                            color = Color(0xff243257),
                                            modifier = Modifier.alignByBaseline()
                                        )
                                    }
                                }
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ){
                                    myText(
                                        "Month",
                                        12,
                                        0xff0a688a
                                    )
                                    10.sy()
                                    myText(
                                        "June",
                                        12,
                                        0xff243257
                                    )
                                }
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ){
                                    myText(
                                        "No. of Transaction",
                                        12,
                                        0xff0a688a,
                                    )
                                    10.sy()
                                    myText(
                                        "1",
                                        12,
                                        0xff243257
                                    )
                                }
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
                            "Categories",
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
        }
    }
}

@Composable
fun Number.sx(color: Color? = null){
    if(color!=null){
        Spacer(modifier = Modifier.width(this.dep()).height(5.dep()).background(color))
    }
    else{
        Spacer(modifier = Modifier.width(this.dep()))
    }
}

@Composable
fun Number.sy(){
    Spacer(modifier = Modifier.height(this.dep()))
}

@Composable
fun myText(
    text: String,
    size: Number,
    color: Any = Color.Unspecified,
    bold: Boolean = false
){
    RobotoText(
        text = text,
        color = color.ensureColor,
        fontSize = size.sep(),
        fontWeight = if(bold) FontWeight.Bold else FontWeight.Normal
    )
}
val Any.ensureColor: Color
    get() {
        return when(this){
            is Color->this
            is Int->Color(this)
            is Long->Color(this)
            else->Color.Unspecified
        }
    }

data class LabelData(
    val index: Int,
    val touchArea: TouchArea?
)

data class BarGraphData(
    val xAxisGap: Float,
    val xValuesCount: Int,
    val xValueCallback: (Int)->Float,
    val xAxisLabelCallback: (Int)->String,
    val xLabelCallback: (Int)->String,
    val barGraphColorCallback: (Int)->Color,
    val maxYAxisValue: Float,
    val yAxisLabelCount: Int,
    val yAxisLabelCallback: (Float)->String,
    val xAxisLabelColor: (Int)->Color,
    val yAxisLabelColor: ()->Color,
    val axisColor: Color = Color.Gray,
    val strokeWidth: Float,
    val axisLabelFontSize: Float,
    val highlightedBarColor: Color,
    val highlightedXLabelColor: Color
)

data class TouchArea(
    val offset: Offset,
    val size: Size
){
    fun contains(pos: Offset): Boolean{
        return pos.x>=offset.x
                && pos.x<=offset.x+size.width
                && pos.y>=offset.y
                && pos.y<=pos.y+size.height
    }
}

@Composable
fun BarGraph(
    modifier: Modifier = Modifier,
    data: BarGraphData,
    onBarClick: (Int, TouchArea?)->Unit
) {
    val sepx = with(LocalDensity.current){
        1.sep().toPx()
    }
    val depx = with(LocalDensity.current){
        1.dep().toPx()
    }
    val touchAreas = remember {
        mutableStateListOf<TouchArea>()
    }
    var selectedIndex by remember {
        mutableStateOf(-1)
    }
    Canvas(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures { offset->
                    selectedIndex = touchAreas.indexOfFirst {
                        it.contains(offset)
                    }
                    onBarClick(selectedIndex,touchAreas.getOrNull(selectedIndex))
                }
            }
    ){
        if(touchAreas.isEmpty()){
            touchAreas.addAll(MutableList(data.xValuesCount){
                TouchArea(
                    Offset.Zero,
                    Size.Zero
                )
            })
        }
        val canvasWidth = size.width
        val canvasHeight = size.height

        val axisPaddingFactor = 0.0917f
        val axisInitialGapFactor = 0.0428f
        val axisInitialGap = canvasWidth*axisInitialGapFactor
        val axisPadding = canvasWidth*axisPaddingFactor
        val axisBottomY = canvasHeight - axisPadding
        drawLine(
            data.axisColor,
            start = Offset(axisPadding,0f),
            end = Offset(axisPadding,axisBottomY),
            strokeWidth = data.strokeWidth
        )
        drawLine(
            data.axisColor,
            start = Offset(axisPadding,axisBottomY),
            end = Offset(canvasWidth,axisBottomY),
            strokeWidth = data.strokeWidth
        )
        val axisLabelPaint = TextPaint().apply {
            color = data.yAxisLabelColor().native
            textSize = data.axisLabelFontSize*sepx
        }
        val divs = data.yAxisLabelCount
        val f = (1f/divs.toFloat())
        val pack = data.maxYAxisValue/divs
        val avh = canvasHeight - axisPadding - axisInitialGap - data.strokeWidth/2
        val yAxisHeight = canvasHeight-axisPadding
        for(i in 1..divs){
            myDrawText(
                data.yAxisLabelCallback(i*pack),
                0f,
                yAxisHeight-f*avh*i,
                axisLabelPaint
            )
        }
        val gap = data.xAxisGap*depx
        val xValuesCount = data.xValuesCount
        val labelSize = (canvasWidth - axisPadding - axisInitialGap - gap*(xValuesCount-1))/xValuesCount
        val maxYAxisValue = data.maxYAxisValue
        (0 until xValuesCount).forEach {i->
            val xAxisLabelX = axisPadding+axisInitialGap+i*labelSize+i*gap
            val selected = selectedIndex==i
            myDrawText(
                data.xAxisLabelCallback(i),
                xAxisLabelX,
                canvasHeight,
                axisLabelPaint.apply {
                    color = if(selected){
                        data.highlightedXLabelColor.native
                    } else{
                        data.xAxisLabelColor(i).native
                    }
                }
            )
            val height = ((data.xValueCallback(i)/maxYAxisValue)*avh).coerceAtMost(avh)
            val barLeft = axisPadding+axisInitialGap+i*(labelSize+gap)
            val barTop = canvasHeight-axisPadding-height-data.strokeWidth/2f
            drawRect(
                color = if(selected) data.highlightedBarColor else data.barGraphColorCallback(i),
                topLeft = Offset(barLeft,barTop),
                size = Size(labelSize,height)
            )
            if(selected){
                drawLine(
                    color = Color.Gray,
                    start = Offset(barLeft+labelSize,0f),
                    end = Offset(barLeft+labelSize,canvasHeight-axisPadding),
                    strokeWidth = 5f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f,10f))
                )
            }
            touchAreas[i] = TouchArea(
                Offset(barLeft,barTop),
                Size(labelSize,height)
            )
            val label = data.xLabelCallback(i)
            if(label.isNotEmpty()){
                myDrawText(
                    label,
                    axisPadding+axisInitialGap+i*labelSize+i*gap,
                    canvasHeight-axisPadding-height-10f,
                    axisLabelPaint
                )
            }
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

@Preview
@Composable
fun ComposablePreview() {
    ExpenseDemo()
}

////////////////////////
sealed interface ClickableItem

sealed class Device(val name: String) {
    object Camera : Device("Camera"), ClickableItem
    object Lamp : Device("Lamp")
    // ... etc, more devices
}

fun onDeviceItemClicked(item: ClickableItem) {
    when (item) {
        Device.Camera -> { /* do something */ }
    }
}