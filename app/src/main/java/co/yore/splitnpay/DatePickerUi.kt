package co.yore.splitnpay

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import dev.chrisbanes.snapper.rememberLazyListSnapperLayoutInfo
import dev.chrisbanes.snapper.rememberSnapperFlingBehavior
import java.util.*
import kotlin.IllegalStateException
import kotlin.math.abs

object Kal{
    data class DateDifference(
        private val small: Date,
        private val big: Date,
        val positive: Boolean = true
    ){
        val years: Int
            get() {
                val y1 = small.year
                val y2 = big.year
                return y2 - y1
            }
        val months: Int
        get(){
            val dy = years
            if(dy==0){
                val m1 = small.month
                val m2 = big.month
                val d = m2 - m1
                return d
            }
            else if(dy>0){
                return if(dy==1){
                    val d1 = small.month
                    val d2 = big.month + 12
                    val d = d2 - d1 - 1
                    d
                } else{
                    val d1 = small.month
                    val d2 = big.month + 12
                    val d = d2 - d1 - 1
                    d + 12*(dy-2)
                }
            }
            else{
                return 0
            }
        }
        val days: Int
        get() {
            return getDifference(small,big)
        }

        private fun countLeapYears(d: Date): Int {
            var years: Int = d.year
            if (d.month <= 2) {
                years--
            }
            return years / 4 - years / 100 + years / 400
        }

        private fun getDifference(dt1: Date, dt2: Date): Int {
            val monthDays = intArrayOf(
                31, 28, 31, 30, 31, 30,
                31, 31, 30, 31, 30, 31
            )
            var n1: Int = dt1.year * 365 + dt1.day
            for (i in 0 until dt1.month - 1) {
                n1 += monthDays.get(i)
            }
            n1 += countLeapYears(dt1)
            var n2: Int = dt2.year * 365 + dt2.day
            for (i in 0 until dt2.month - 1) {
                n2 += monthDays.get(i)
            }
            n2 += countLeapYears(dt2)
            return n2 - n1
        }
    }
    data class Date(
        val day: Int,
        val month: Int,
        val year: Int
    ){
        companion object{
            fun isValidDate(day: Int, month: Int, year: Int): Boolean{
                return year>0
                        && month in 1..12
                        && day in 1..numberOfDaysInYearMonth(year,month)
            }
            fun create(day: Int, month: Int, year: Int): Date?{
                if(isValidDate(day, month, year)){
                    return Date(day, month, year)
                }
                return null
            }
        }
        init {
            if(!isValid()){
                throw IllegalStateException()
            }
        }
        private fun isValid(): Boolean{
            return year>0
                    && month in 1..12
                    && day in 1..numberOfDaysInYearMonth(year,month)
        }
        override fun equals(other: Any?): Boolean{
            if(other !is Date){
                return false
            }
            return this.year==other.year
                    &&this.month==other.month
                    &&this.day==other.day
        }
        operator fun compareTo(other: Date): Int{
            return when{
                year>other.year->1
                year<other.year->-1
                month>other.month->1
                month<other.month->-1
                day>other.day->1
                day<other.day->-1
                else->0
            }
        }
        operator fun minus(other: Date): DateDifference{
            val small: Date
            val big: Date
            var sign = 1
            if(this>other){
                small = other
                big = this
            }
            else{
                small = this
                big = other
                sign = -1
            }
            return DateDifference(small,big,sign==1)
        }
    }
    enum class Month{
        Jan,        Feb,        Mar,
        Apr,        May,        Jun,
        Jul,        Aug,        Sep,
        Oct,        Nov,        Dec;
        companion object{
            fun name(month: Int): String{
                return when(month){
                    1->"Jan"
                    2->"Feb"
                    3->"Mar"
                    4->"Apr"
                    5->"May"
                    6->"Jun"
                    7->"Jul"
                    8->"Aug"
                    9->"Sep"
                    10->"Oct"
                    11->"Nov"
                    12->"Dec"
                    else->""
                }
            }
        }
    }
    enum class WeekDay{
        SUN,
        MON,
        TUE,
        WED,
        THU,
        FRI,
        SAT,
    }
    data class Day(
        val weekDay: WeekDay,
        val dayOfMonth: Int
    )

    fun numberOfDaysInYearMonth(year: Int, month: Int): Int{
        return when(month){
            1 -> 31
            2 -> if(year%400==0 || (year%4==0&&year%100!=0)) 29 else 28
            3 -> 31
            4 -> 30
            5 -> 31
            6 -> 30
            7 -> 31
            8 -> 31
            9 -> 30
            10 -> 31
            11 -> 30
            12 -> 31
            else->-1
        }
    }

    fun weekDayOfDate(day: Int, month: Int, year: Int): WeekDay?{
        var m = month
        if(month<3){
            m += 12
        }
        val k = year % 100
        val j = year / 100
        var h = day + 13 * (m + 1) / 5 + k + k / 4 + j / 4 + 5 * j
        h %= 7
        return when(h){
            0->WeekDay.SAT
            1->WeekDay.SUN
            2->WeekDay.MON
            3->WeekDay.TUE
            4->WeekDay.WED
            5->WeekDay.THU
            6->WeekDay.FRI
            else -> null
        }
    }

    fun daysInYearMonth(year: Int, month: Int): List<Day>{
        val firstWeekDayOrdinal = weekDayOfDate(1,month,year)?.ordinal?:-1
        val daysCount = numberOfDaysInYearMonth(year,month)
        val weekDays = enumValues<WeekDay>()
        return (0 until daysCount).map {
            Day(
                weekDay = weekDays[(firstWeekDayOrdinal+it)%7],
                dayOfMonth = it+1
            )
        }
    }
}

data class YoreDatePickerData(
    val selectedDay: Int? = Calendar.getInstance().get(Calendar.DATE),
    val selectedMonth: Int? = Calendar.getInstance().get(Calendar.MONTH)+1,
    val selectedYear: Int = Calendar.getInstance().get(Calendar.YEAR),
    val minDate: Kal.Date? = null,
    val maxDate: Kal.Date? = null
)

@Composable
fun YoreDatePicker(
    yoreDatePickerData: YoreDatePickerData = YoreDatePickerData(),
    onYearClick: (Int)->Unit,
    onMonthClick: (Int)->Unit,
    onDaySelect: (Int)->Unit
) {
    Box {
        var yearPicking by remember {
            mutableStateOf(false)
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth()
        ){
            YearSwitcher(
                YearSwitcherData(
                    yoreDatePickerData.selectedYear,
                    minDate = yoreDatePickerData.minDate,
                    maxDate = yoreDatePickerData.maxDate,
                ),
                onMode = {
                    yearPicking = it
                },
                onYearPicked = {
                    onYearClick(it)
                }
            )
        }
        var minMonth by remember {
            mutableStateOf(1)
        }
        var maxMonth by remember {
            mutableStateOf(12)
        }
        var minDay by remember {
            mutableStateOf(1)
        }
        var maxDay by remember {
            mutableStateOf(31)
        }
        LaunchedEffect(key1 = yoreDatePickerData){
            minMonth = if(yoreDatePickerData.selectedYear==yoreDatePickerData.minDate?.year){
                yoreDatePickerData.minDate.month
            }
            else{
                1
            }
            minDay = if(
                yoreDatePickerData.minDate!=null
                &&yoreDatePickerData.selectedMonth!=null
                &&yoreDatePickerData.minDate.year==yoreDatePickerData.selectedYear
                &&yoreDatePickerData.minDate.month==yoreDatePickerData.selectedMonth
            ){
                yoreDatePickerData.minDate.day
            } else{
                1
            }

            maxMonth = if(yoreDatePickerData.selectedYear==yoreDatePickerData.maxDate?.year){
                yoreDatePickerData.maxDate.month
            }
            else{
                12
            }
            maxDay = if(
                yoreDatePickerData.maxDate!=null
                &&yoreDatePickerData.selectedMonth!=null
                &&yoreDatePickerData.maxDate.year==yoreDatePickerData.selectedYear
                &&yoreDatePickerData.maxDate.month==yoreDatePickerData.selectedMonth){
                yoreDatePickerData.maxDate.day
            } else{
                31
            }
        }
        Spacer(modifier = Modifier.height(24.dep()))
        AnimatedVisibility(
            !yearPicking
        ,enter = fadeIn(animationSpec = tween(700)),
        exit = fadeOut(animationSpec = tween(700))
        ) {
            Box(
                modifier = Modifier.padding(top = 42.dep())
            ){
                MonthPicker(
                    MonthPickerData(
                        yoreDatePickerData.selectedYear,
                        yoreDatePickerData.selectedMonth,
                        yoreDatePickerData.selectedDay,
                        minMonth,
                        maxMonth,
                        minDay,
                        maxDay,
                    ),
                    {
                        onMonthClick(it)
                    }
                ){
                    onDaySelect(it)
                }
            }
        }
    }
}

data class MonthPickerData(
    val year: Int,
    val selectedMonth: Int?,
    val selectedDay: Int?,
    val minMonth: Int,
    val maxMonth: Int,
    val minDay: Int,
    val maxDay: Int,
)

@Composable
fun MonthPicker(
    monthPickerData: MonthPickerData,
    onMonthClick: (Int) -> Unit,
    onDayClick: (Int)->Unit
) {
    val time by remember { mutableStateOf(500) }
    val enter by remember {
        mutableStateOf(
            expandVertically(animationSpec = tween(durationMillis = time)) +
                    fadeIn(animationSpec = tween(durationMillis = time*2))
        )
    }
    val exit by remember {
        mutableStateOf(
            shrinkVertically(animationSpec = tween(durationMillis = time)) +
                    fadeOut(animationSpec = tween(durationMillis = time/2))
        )
    }
    Column(){
        Row(
            modifier = Modifier
                .padding(horizontal = 58.dep())
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            MonthUI(
                MonthUIData(
                    1,
                    monthPickerData.selectedMonth,
                    monthPickerData.minMonth,
                    monthPickerData.maxMonth
                )
            ){
                onMonthClick(it)
            }
            MonthUI(
                MonthUIData(
                    2,
                    monthPickerData.selectedMonth,
                    monthPickerData.minMonth,
                    monthPickerData.maxMonth
                )
            ){
                onMonthClick(it)
            }
            MonthUI(
                MonthUIData(
                    3,
                    monthPickerData.selectedMonth,
                    monthPickerData.minMonth,
                    monthPickerData.maxMonth
                )
            ){
                onMonthClick(it)
            }
        }
        AnimatedVisibility(
            monthPickerData.selectedMonth is Int &&
                    monthPickerData.selectedMonth in 1..3,
            enter = enter,
            exit = exit,
        ){
            monthPickerData.selectedMonth?.let {
                MonthDayPicker(
                    MonthDayPickerData(
                        Kal.daysInYearMonth(monthPickerData.year,monthPickerData.selectedMonth),
                        monthPickerData.selectedDay,
                        monthPickerData.minDay,
                        monthPickerData.maxDay
                    )
                ){
                    onDayClick(it)
                }
            }
        }
        AnimatedVisibility(
            monthPickerData.selectedMonth is Int &&
                    monthPickerData.selectedMonth !in 1..3,
            enter = enter,
            exit = exit,
        ){
            Spacer(modifier = Modifier.height(21.dep()))
        }

        Row(
            modifier = Modifier
                .padding(horizontal = 58.dep())
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            MonthUI(
                MonthUIData(
                    4,
                    monthPickerData.selectedMonth,
                    monthPickerData.minMonth,
                    monthPickerData.maxMonth
                )
            ){
                onMonthClick(it)
            }
            MonthUI(
                MonthUIData(
                    5,
                    monthPickerData.selectedMonth,
                    monthPickerData.minMonth,
                    monthPickerData.maxMonth
                )
            ){
                onMonthClick(it)
            }
            MonthUI(
                MonthUIData(
                    6,
                    monthPickerData.selectedMonth,
                    monthPickerData.minMonth,
                    monthPickerData.maxMonth
                )
            ){
                onMonthClick(it)
            }
        }
        AnimatedVisibility(
            monthPickerData.selectedMonth is Int &&
                    monthPickerData.selectedMonth in 4..6,
            enter = enter,
            exit = exit,
        ){
            monthPickerData.selectedMonth?.let {
                MonthDayPicker(
                    MonthDayPickerData(
                        Kal.daysInYearMonth(monthPickerData.year,monthPickerData.selectedMonth),
                        monthPickerData.selectedDay,
                        monthPickerData.minDay,
                        monthPickerData.maxDay
                    )
                ){
                    onDayClick(it)
                }
            }
        }
        AnimatedVisibility(
            monthPickerData.selectedMonth is Int &&
                    monthPickerData.selectedMonth !in 4..6,
            enter = enter,
            exit = exit,
        ){
            Spacer(modifier = Modifier.height(21.dep()))
        }

        Row(
            modifier = Modifier
                .padding(horizontal = 58.dep())
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            MonthUI(
                MonthUIData(
                    7,
                    monthPickerData.selectedMonth,
                    monthPickerData.minMonth,
                    monthPickerData.maxMonth
                )
            ){
                onMonthClick(it)
            }
            MonthUI(
                MonthUIData(
                    8,
                    monthPickerData.selectedMonth,
                    monthPickerData.minMonth,
                    monthPickerData.maxMonth
                )
            ){
                onMonthClick(it)
            }
            MonthUI(
                MonthUIData(
                    9,
                    monthPickerData.selectedMonth,
                    monthPickerData.minMonth,
                    monthPickerData.maxMonth
                )
            ){
                onMonthClick(it)
            }
        }
        AnimatedVisibility(
            monthPickerData.selectedMonth is Int &&
                    monthPickerData.selectedMonth in 7..9,
            enter = enter,
            exit = exit,
        ){
            monthPickerData.selectedMonth?.let {
                MonthDayPicker(
                    MonthDayPickerData(
                        Kal.daysInYearMonth(monthPickerData.year,monthPickerData.selectedMonth),
                        monthPickerData.selectedDay,
                        monthPickerData.minDay,
                        monthPickerData.maxDay
                    )
                ){
                    onDayClick(it)
                }
            }
        }
        AnimatedVisibility(
            monthPickerData.selectedMonth is Int &&
                    monthPickerData.selectedMonth !in 7..9,
            enter = enter,
            exit = exit,
        ){
            Spacer(modifier = Modifier.height(21.dep()))
        }

        Row(
            modifier = Modifier
                .padding(horizontal = 58.dep())
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            MonthUI(
                MonthUIData(
                    10,
                    monthPickerData.selectedMonth,
                    monthPickerData.minMonth,
                    monthPickerData.maxMonth
                )
            ){
                onMonthClick(it)
            }
            MonthUI(
                MonthUIData(
                    11,
                    monthPickerData.selectedMonth,
                    monthPickerData.minMonth,
                    monthPickerData.maxMonth
                )
            ){
                onMonthClick(it)
            }
            MonthUI(
                MonthUIData(
                    12,
                    monthPickerData.selectedMonth,
                    monthPickerData.minMonth,
                    monthPickerData.maxMonth
                )
            ){
                onMonthClick(it)
            }
        }
        AnimatedVisibility(
            monthPickerData.selectedMonth is Int &&
                    monthPickerData.selectedMonth in 10..12,
            enter = enter,
            exit = exit,
        ){
            monthPickerData.selectedMonth?.let {
                MonthDayPicker(
                    MonthDayPickerData(
                        Kal.daysInYearMonth(monthPickerData.year,monthPickerData.selectedMonth),
                        monthPickerData.selectedDay,
                        monthPickerData.minDay,
                        monthPickerData.maxDay
                    )
                ){
                    onDayClick(it)
                }
            }
        }
    }
}

data class MonthUIData(
    val month: Int,
    val selected: Int?,
    val minMonth: Int,
    val maxMonth: Int,
)

data class MonthUIConfiguration(
    val width: Float = 74f,
    val height: Float = 42f,
    val borderRadius: Float = 21f,
    val selectedBackgroundColor: Color = Color(0xffEDF5FF),
    val fontSize: Float = 14f,
    val disabledColor: Color = Color.Gray,
    val activeColor: Color = Color(0xff1B79E6),
    val color: Color = Color(0xff243257)
)

@Composable
fun MonthUI(
    monthUIData: MonthUIData,
    config: MonthUIConfiguration = MonthUIConfiguration(),
    onClick: (Int)->Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val computedBackgroundColor by remember {
        derivedStateOf {
            if (
                monthUIData.month in monthUIData.minMonth..monthUIData.maxMonth
                && monthUIData.selected == monthUIData.month || isPressed
            )
                config.selectedBackgroundColor
            else Color.Transparent
        }
    }
    val computedColor by remember {
        derivedStateOf {
            if(monthUIData.month !in monthUIData.minMonth..monthUIData.maxMonth)
                config.disabledColor
            else if(monthUIData.selected==monthUIData.month)
                config.activeColor
            else
                config.color
        }
    }
    Box(
        modifier = Modifier
            .width(config.width.dep())
            .height(config.height.dep())
            .clip(RoundedCornerShape(config.borderRadius.dep()))
            .then(
                if (monthUIData.month in monthUIData.minMonth..monthUIData.maxMonth) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = LocalIndication.current
                    ) {
                        onClick(monthUIData.month)
                    }
                } else {
                    Modifier
                }
            )
            .background(computedBackgroundColor),
        contentAlignment = Alignment.Center
    ){
        RobotoText(
            Kal.Month.name(monthUIData.month),
            fontSize = config.fontSize.sep(),
            color =
            computedColor,
            fontWeight = FontWeight.Bold
        )
    }
}

data class YearSwitcherData(
    val selectedYear: Int,
    val minDate: Kal.Date?,
    val maxDate: Kal.Date?,
)

data class YearSwitcherConfiguration(
    val navYearSpace: Float = 40f,
    val fontSize: Float = 16f,
    val color: Color = Color(0xff243257),
    val horizontalPadding: Float = 4f
)

@Composable
fun YearSwitcher(
    yearSwitcherData: YearSwitcherData,
    config: YearSwitcherConfiguration = YearSwitcherConfiguration(),
    onMode: (Boolean)->Unit,
    onYearPicked: (Int)->Unit
) {
    LaunchedEffect(key1 = Unit){
        if(yearSwitcherData.minDate!=null&& yearSwitcherData.maxDate!=null){
            if(
                yearSwitcherData.minDate > yearSwitcherData.maxDate
                || yearSwitcherData.selectedYear < yearSwitcherData.minDate.year
                || yearSwitcherData.selectedYear > yearSwitcherData.maxDate.year
            ){
                throw IllegalStateException()
            }
        }
    }
    val minYear by remember {
        mutableStateOf(yearSwitcherData.minDate?.year?:1)
    }
    val maxYear by remember {
        mutableStateOf(yearSwitcherData.maxDate?.year?:9999)
    }
    var hasPrev by remember {
        mutableStateOf(minYear<yearSwitcherData.selectedYear)
    }
    var hasNext by remember {
        mutableStateOf(maxYear>yearSwitcherData.selectedYear)
    }
    LaunchedEffect(key1 = yearSwitcherData.selectedYear){
        hasPrev = minYear<yearSwitcherData.selectedYear
        hasNext = maxYear>yearSwitcherData.selectedYear
    }
    var selectionMode by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = selectionMode){
        onMode(selectionMode)
    }
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Row(
        ){
            NavIconButton(
                hasPrev,
                NavIconConfiguration.Prev
            ){
                onYearPicked(yearSwitcherData.selectedYear - 1)
            }
            Spacer(modifier = Modifier.width(config.navYearSpace.dep()))
            Text(
                yearSwitcherData.selectedYear.toString(),
                fontSize = config.fontSize.sep(),
                fontWeight = FontWeight.Bold,
                color = config.color,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                        selectionMode = !selectionMode
                    }
                    .padding(horizontal = config.horizontalPadding.dep())
            )
            Spacer(modifier = Modifier.width(config.navYearSpace.dep()))
            NavIconButton(
                hasNext,
                NavIconConfiguration.Next
            ){
                onYearPicked(yearSwitcherData.selectedYear + 1)
            }
        }

        AnimatedVisibility(
            selectionMode,
        enter = fadeIn(animationSpec = tween(700)),
        exit = fadeOut(animationSpec = tween(700))
        ) {
            YearsList(
                YearsListData(
                    yearSwitcherData.selectedYear,
                    minYear,
                    maxYear
                )
            ){
                onYearPicked(it)
                selectionMode = false
            }
        }
    }

}

data class YearsListConfiguration(
    val columnCount: Int = 3,
    val horizontalPadding: Float = 58f,
    val height: Float = 251f,
    val fadeColor: Color = Color.White,
    val fadeY: Float = 60f,
    val verticalContentPadding: Float = 20f,
    val verticalItemSpacing: Float = 21f
)

data class YearsListData(
    val selectedYear: Int,
    val minYear: Int,
    val maxYear: Int,
)

@Composable
fun YearsList(
    yearsListData: YearsListData,
    config: YearsListConfiguration = YearsListConfiguration(),
    onYearPicked: (Int)->Unit
) {
    val yearsList by remember {
        derivedStateOf {
            return@derivedStateOf (yearsListData.minYear..yearsListData.maxYear).toList()
        }
    }
    val state = rememberLazyGridState()
    LaunchedEffect(key1 = yearsListData.selectedYear){
        state.scrollToItem(yearsListData.selectedYear-yearsListData.minYear)
    }
    LazyVerticalGrid(
        state = state,
        columns = GridCells.Fixed(config.columnCount),
        verticalArrangement = Arrangement.spacedBy(config.verticalItemSpacing.dep()),
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(horizontal = config.horizontalPadding.dep())
            .fillMaxWidth()
            .height(config.height.dep())
            .drawWithContent {
                val colors = listOf(config.fadeColor, Color.Transparent)
                val colors1 = listOf(Color.Transparent, config.fadeColor)
                drawContent()
                drawRect(
                    brush = Brush.verticalGradient(colors, endY = config.fadeY),
                )
                drawRect(
                    brush = Brush.verticalGradient(
                        colors1,
                        startY = size.height - config.fadeY,
                        endY = size.height
                    ),
                )
            },
        contentPadding = PaddingValues(vertical = config.verticalContentPadding.dep())
    ) {
        items(yearsList){ it ->
            YearItem(
                it,
                yearsListData.selectedYear==it
            ){
                onYearPicked(it)
            }
        }
    }
}

data class YearItemConfiguration(
    val width: Float = 72f,
    val height: Float = 42f,
    val borderRadius: Float = 21f,
    val selectedBackgroundColor: Color = Color(0xffEDF5FF),
    val unselectedBackgroundColor: Color = Color.Transparent,
    val fontSize: Float = 14f,
    val selectedColor: Color = Color(0xff1B79E6),
    val unselectedColor: Color = Color(0xff243257)
)

@Composable
fun YearItem(
    year: Int,
    selected: Boolean,
    config: YearItemConfiguration = YearItemConfiguration(),
    onYearPicked: (Int) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val yearString by remember {
        mutableStateOf(year.toString())
    }
    val virtuallySelected by remember {
        derivedStateOf {
            return@derivedStateOf selected || isPressed
        }
    }
    val backgroundColor by remember {
        derivedStateOf {
            return@derivedStateOf if
                    (virtuallySelected) config.selectedBackgroundColor
            else
                config.unselectedBackgroundColor
        }
    }
    val color by remember {
        derivedStateOf {
            return@derivedStateOf if
                    (selected) config.selectedColor
            else
                config.unselectedColor
        }
    }
    Box(
        modifier = Modifier
            .width(config.width.dep())
            .height(config.height.dep())
            .clip(RoundedCornerShape(config.borderRadius.dep()))
            .clickable(
                interactionSource = interactionSource,
                indication = null//LocalIndication.current
            ) {
                onYearPicked(year)
            }
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ){
        RobotoText(
            yearString,
            fontSize = config.fontSize.sep(),
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
}

data class NavIconConfiguration(
    val size: Float = 18f,
    val disabledBackgroundColor: Color = Color.Gray,
    val enabledBackgroundColor: Color = Color(0xff1A79E5),
    val drawableId: Int,
    val padding: Float = 4f,
    val iconTint: Color = Color.White
){
    companion object{
        val Prev = NavIconConfiguration(
            drawableId = R.drawable.ic_left_chevron
        )
        val Next = NavIconConfiguration(
            drawableId = R.drawable.ic_right_chevron
        )
    }
}

@Composable
fun NavIconButton(
    enabled: Boolean,
    config: NavIconConfiguration,
    onClicked: ()->Unit
) {
    var backgroundColor by remember {
        mutableStateOf(config.disabledBackgroundColor)
    }
    LaunchedEffect(key1 = enabled){
        backgroundColor = if (!enabled) config.disabledBackgroundColor else config.enabledBackgroundColor
    }
    Icon(
        painter = painterResource(id = config.drawableId),
        contentDescription = null,
        modifier = Modifier
            .size(config.size.dep())
            .clip(CircleShape)
            .then(
                if (enabled) {
                    Modifier.clickable {
                        onClicked()
                    }
                } else {
                    Modifier
                }
            )
            .background(backgroundColor)
            .padding(config.padding.dep()),
        tint = config.iconTint
    )
}

data class MonthDayPickerData(
    val days: List<Kal.Day>,
    val selectedDay: Int?,
    val minDay: Int,
    val maxDay: Int,
)

data class MonthDayPickerConfiguration(
    val verticalPadding: Float = 9f,
    val height: Float = 89f,
    val backgroundColor: Color = Color(0xffEDF5FF),
    val boxSize: Float = 54f,
    val highlightWidth: Float = 64f,
    val highlightBorderRadius: Float = 13f,
    val highlightBackgroundColor: Color = Color(0xff1A79E5),
    val apparentCurrentThreshold: Float = 0.1f
)

suspend fun LazyListState.safeAnimateScrollToItem(index: Int){
    try {
        animateScrollToItem(index)
    } catch (e: Exception) {
    }
}
suspend fun LazyListState.safeScrollToItem(index: Int){
    try {
        scrollToItem(index)
    } catch (e: Exception) {
    }
}
@OptIn(ExperimentalSnapperApi::class, ExperimentalFoundationApi::class)
@Composable
fun MonthDayPicker(
    monthDayPickerData: MonthDayPickerData,
    config: MonthDayPickerConfiguration = MonthDayPickerConfiguration(),
    onDaySelected: (Int) -> Unit
) {
    val listState = rememberLazyListState()
    val layoutInfo = rememberLazyListSnapperLayoutInfo(listState)
    LaunchedEffect(key1 = monthDayPickerData.selectedDay){
        if(monthDayPickerData.selectedDay!=null){
            listState.safeScrollToItem(monthDayPickerData.selectedDay-1)
        }
        else{
            listState.safeScrollToItem(0)
            onDaySelected(1)
        }
    }
    var currentItem by remember {
        mutableStateOf(0)
    }

    var initiateClick by remember {
        mutableStateOf(-1)
    }
    var apparentCurrentItem by remember {
        mutableStateOf(-1)
    }
    LaunchedEffect(key1 = apparentCurrentItem){
        if(apparentCurrentItem<monthDayPickerData.minDay-1){
            listState.safeAnimateScrollToItem(monthDayPickerData.minDay-1)
            return@LaunchedEffect
        }
        if(apparentCurrentItem>monthDayPickerData.maxDay-1){
            listState.safeAnimateScrollToItem(monthDayPickerData.maxDay-1)
            return@LaunchedEffect
        }
    }
    LaunchedEffect(key1 = listState.isScrollInProgress){
        if(!listState.isScrollInProgress){
            val d = layoutInfo.currentItem
            d?.let {
                currentItem = (d.index)
                if(currentItem<monthDayPickerData.minDay-1){
                    listState.safeAnimateScrollToItem(monthDayPickerData.minDay-1)
                    return@LaunchedEffect
                }
                if(currentItem>monthDayPickerData.maxDay-1){
                    listState.safeAnimateScrollToItem(monthDayPickerData.maxDay-1)
                    return@LaunchedEffect
                }
                onDaySelected(currentItem+1)
            }
        }
    }
    LaunchedEffect(key1 = initiateClick){
        if(initiateClick<0){
            return@LaunchedEffect
        }
        listState.safeAnimateScrollToItem(initiateClick)
    }
    BoxWithConstraints(
        modifier = Modifier
            .padding(vertical = config.verticalPadding.dep())
            .height(config.height.dep())
            .background(config.backgroundColor)
    ) {
        val full = localFullWidth.current
        val box = config.boxSize
        val pad = (full - box)/2f
        val halfRowWidth = constraints.maxWidth / 2
        Box(
            modifier = Modifier
                .width(config.highlightWidth.dep())
                .fillMaxHeight()
                .clip(RoundedCornerShape(config.highlightBorderRadius.dep()))
                .background(config.highlightBackgroundColor)
                .align(Alignment.Center)
        ){

        }
        CompositionLocalProvider(
            LocalOverscrollConfiguration provides null
        ) {
            LazyRow(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth()
                ,
                contentPadding = PaddingValues(horizontal = pad.dep()),
                flingBehavior = rememberSnapperFlingBehavior(listState),
            ) {
                itemsIndexed(
                    monthDayPickerData.days,
                    key = { _, item->
                        item.dayOfMonth
                    }
                ) { i, item ->
                    val opacity by remember {
                        derivedStateOf {
                            val currentItemInfo = listState.layoutInfo.visibleItemsInfo
                                .firstOrNull { it.index == i }
                                ?: return@derivedStateOf 0.5f
                            (1f - minOf(1f, abs(currentItemInfo.offset).toFloat() / halfRowWidth) * 0.5f)
                        }
                    }
                    LaunchedEffect(key1 = opacity){
                        if(1f-opacity<=config.apparentCurrentThreshold){
                            apparentCurrentItem = i
                        }
                    }
                    WeekDayUI(
                        WeekDayUIData(
                            opacity,
                            box,
                            item,
                            monthDayPickerData.minDay,
                            monthDayPickerData.maxDay
                        )
                    ){
                        initiateClick = item.dayOfMonth - 1
                    }
                }
            }
        }
    }
}

data class WeekDayUIData(
    val opacity: Float,
    val boxSize: Float,
    val day: Kal.Day,
    val minDay: Int,
    val maxDay: Int,
)

data class WeekDayUIConfiguration(
    val weekDayFontSize: Float = 11f,
    val activeThreshold: Float = 0.1f,
    val weekDayDisabledColor: Color = Color.LightGray,
    val weekDayActiveColor: Color = Color.White,
    val weekDayEnabledColor: Color = Color(0xff243257),
    val weekDayDateSpace: Float = 12f,
    val dateFontSize: Float = 11f,
    val dateDisabledColor: Color = Color.LightGray,
    val dateActiveColor: Color = Color.White,
    val dateEnabledColor: Color = Color(0xff1A79E5),
)

@Composable
fun WeekDayUI(
    weekDayUIData: WeekDayUIData,
    config: WeekDayUIConfiguration = WeekDayUIConfiguration(),
    onClick: ()->Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .alpha(weekDayUIData.opacity)
            .width((weekDayUIData.boxSize).dep())
            .fillMaxHeight()
            .clickable {
                onClick()
            }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            RobotoText(
                weekDayUIData.day.weekDay.toString(),
                fontSize = (weekDayUIData.opacity*config.weekDayFontSize).sep(),
                fontWeight = FontWeight.Bold,
                color =
                if(weekDayUIData.day.dayOfMonth !in weekDayUIData.minDay..weekDayUIData.maxDay)
                    config.weekDayDisabledColor
                else if(1f-weekDayUIData.opacity<=config.activeThreshold)
                    config.weekDayActiveColor
                else
                    config.weekDayEnabledColor
            )
            Spacer(modifier = Modifier.height(config.weekDayDateSpace.dep()))
            RobotoText(
                weekDayUIData.day.dayOfMonth.toString(),
                fontSize = config.dateFontSize.sep(),
                fontWeight = FontWeight.Bold,
                color =
                if(weekDayUIData.day.dayOfMonth !in weekDayUIData.minDay..weekDayUIData.maxDay)
                    config.dateDisabledColor
                else if(1f-weekDayUIData.opacity<=config.activeThreshold)
                    config.dateActiveColor
                else
                    config.dateEnabledColor
            )
        }
    }
}
