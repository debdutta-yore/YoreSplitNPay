package co.yore.splitnpay

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
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
import kotlinx.coroutines.selects.select
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
        Oct,        Nov,        Dec
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

@Composable
fun DatePickerUI(
    selectedDay: Int?,
    selectedMonth: Kal.Month?,
    selectedYear: Int,
    minDate: Kal.Date?,
    maxDate: Kal.Date?,
    onYearClick: (Int)->Unit,
    onMonthClick: (Kal.Month)->Unit,
    onDayClick: (Int)->Unit
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
                selectedYear,
                minDate = minDate,
                maxDate = maxDate,
                onMode = {
                    yearPicking = it
                },
                onYearPicked = {
                    onYearClick(it)
                }
            )
        }
        var minMonth by remember {
            mutableStateOf(Kal.Month.Jan)
        }
        var maxMonth by remember {
            mutableStateOf(Kal.Month.Dec)
        }
        /*LaunchedEffect(key1 = Unit){
            if (minDate != null && selectedMonth != null && minDate.year == selectedYear && minDate.month == selectedMonth.ordinal + 1) {
                minDay = minDate.day
            }
            if (maxDate != null && selectedMonth != null && maxDate.year == selectedYear && maxDate.month == selectedMonth.ordinal + 1) {
                maxDay = maxDate.day
            }
        }*/
        var minDay by remember {
            Log.d("fjldfjdklf","1")
            mutableStateOf(1)
        }
        var maxDay by remember {
            mutableStateOf(31)
        }
        LaunchedEffect(key1 = minDate){
            minMonth = if(selectedYear==minDate?.year){
                enumValues<Kal.Month>()[minDate.month-1]
            }
            else{
                Kal.Month.Jan
            }

            Log.d("fjldfjdklf","2")
            if(minDate!=null&&selectedMonth!=null&&minDate.year==selectedYear&&minDate.month==selectedMonth.ordinal+1){
                minDay = minDate.day
            }
            else{
                minDay = 1
            }
            Log.d("fjldfjdklf","$minDay,$maxDay")
        }
        LaunchedEffect(key1 = maxDate){
            maxMonth = if(selectedYear==maxDate?.year){
                enumValues<Kal.Month>()[maxDate.month-1]
            }
            else{
                Kal.Month.Dec
            }

            Log.d("fjldfjdklf","3")
            if(maxDate!=null&&selectedMonth!=null&&maxDate.year==selectedYear&&maxDate.month==selectedMonth.ordinal+1){
                maxDay = maxDate.day
            }
            else{
                maxDay = 31
            }
            Log.d("fjldfjdklf","$minDay,$maxDay")
        }
        LaunchedEffect(key1 = selectedYear){
            Log.d("fjldfjdklf","4")
            if(minDate!=null&&selectedMonth!=null&&minDate.year==selectedYear&&minDate.month==selectedMonth.ordinal+1){
                minDay = minDate.day
            }
            else{
                minDay = 1
            }
            if(maxDate!=null&&selectedMonth!=null&&maxDate.year==selectedYear&&maxDate.month==selectedMonth.ordinal+1){
                maxDay = maxDate.day
            }
            else{
                maxDay = 31
            }
            Log.d("fjldfjdklf","$minDay,$maxDay")
            minMonth = if(selectedYear==minDate?.year){
                enumValues<Kal.Month>()[minDate.month-1]
            }
            else{
                Kal.Month.Jan
            }
            maxMonth = if(selectedYear==maxDate?.year){
                enumValues<Kal.Month>()[maxDate.month-1]
            }
            else{
                Kal.Month.Dec
            }
        }
        LaunchedEffect(key1 = selectedMonth) {
            Log.d("fjldfjdklf","5")
            if (minDate != null && selectedMonth != null && minDate.year == selectedYear && minDate.month == selectedMonth.ordinal + 1) {
                minDay = minDate.day
            }
            else{
                minDay = 1
            }
            if (maxDate != null && selectedMonth != null && maxDate.year == selectedYear && maxDate.month == selectedMonth.ordinal + 1) {
                maxDay = maxDate.day
            }
            else{
                maxDay = 31
            }
            Log.d("fjldfjdklf","$minDay,$maxDay")
        }

        Spacer(modifier = Modifier.height(24.dep()))
        AnimatedVisibility(
            !yearPicking
        ,enter = fadeIn(animationSpec = tween(700))/*+ expandIn(animationSpec = tween(700))*/,
        exit = fadeOut(animationSpec = tween(700))/*+ shrinkOut(animationSpec = tween(700))*/
        ) {
            Box(
                modifier = Modifier.padding(top = 42.dep())
            ){
                MonthPicker(
                    selectedYear,
                    selectedMonth,
                    selectedDay,
                    minMonth,
                    maxMonth,
                    minDay,
                    maxDay,
                    {
                        onMonthClick(it)
                    }
                ){
                    onDayClick(it)
                }
            }
        }
    }
}


@Composable
fun MonthPicker(
    year: Int,
    selectedMonth: Kal.Month?,
    selectedDay: Int?,
    minMonth: Kal.Month,
    maxMonth: Kal.Month,
    minDay: Int,
    maxDay: Int,
    onMonthClick: (Kal.Month) -> Unit,
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
    LaunchedEffect(key1 = year){

    }
    Column(){
        Row(
            modifier = Modifier
                .padding(horizontal = 58.dep())
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            MonthUI(
                Kal.Month.Jan,
                selectedMonth,
                minMonth,
                maxMonth
            ){
                onMonthClick(it)
            }
            MonthUI(
                Kal.Month.Feb,
                selectedMonth,
                minMonth,
                maxMonth
            ){
                onMonthClick(it)
            }
            MonthUI(
                Kal.Month.Mar,
                selectedMonth,
                minMonth,
                maxMonth
            ){
                onMonthClick(it)
            }
        }
        AnimatedVisibility(
            selectedMonth is Kal.Month &&
            selectedMonth in Kal.Month.Jan..Kal.Month.Mar,
            enter = enter,
            exit = exit,
        ){
            selectedMonth?.let {
                MonthDayPicker(
                    Kal.daysInYearMonth(year,selectedMonth.ordinal+1),
                    selectedDay,
                    minDay,
                    maxDay
                ){
                    onDayClick(it)
                }
            }
        }
        AnimatedVisibility(
            selectedMonth is Kal.Month &&
                    selectedMonth !in Kal.Month.Jan..Kal.Month.Mar,
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
                Kal.Month.Apr,
                selectedMonth,
                minMonth,
                maxMonth
            ){
                onMonthClick(it)
            }
            MonthUI(
                Kal.Month.May,
                selectedMonth,
                minMonth,
                maxMonth
            ){
                onMonthClick(it)
            }
            MonthUI(
                Kal.Month.Jun,
                selectedMonth,
                minMonth,
                maxMonth
            ){
                onMonthClick(it)
            }
        }
        AnimatedVisibility(
            selectedMonth is Kal.Month &&
                    selectedMonth in Kal.Month.Apr..Kal.Month.Jun,
            enter = enter,
            exit = exit,
        ){
            selectedMonth?.let {
                MonthDayPicker(
                    Kal.daysInYearMonth(year,selectedMonth.ordinal+1),
                    selectedDay,
                    minDay,
                    maxDay
                ){
                    onDayClick(it)
                }
            }
        }
        AnimatedVisibility(
            selectedMonth is Kal.Month &&
                    selectedMonth !in Kal.Month.Apr..Kal.Month.Jun,
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
                Kal.Month.Jul,
                selectedMonth,
                minMonth,
                maxMonth
            ){
                onMonthClick(it)
            }
            MonthUI(
                Kal.Month.Aug,
                selectedMonth,
                minMonth,
                maxMonth
            ){
                onMonthClick(it)
            }
            MonthUI(
                Kal.Month.Sep,
                selectedMonth,
                minMonth,
                maxMonth
            ){
                onMonthClick(it)
            }
        }
        AnimatedVisibility(
            selectedMonth is Kal.Month &&
                    selectedMonth in Kal.Month.Jul..Kal.Month.Sep,
            enter = enter,
            exit = exit,
        ){
            selectedMonth?.let {
                MonthDayPicker(
                    Kal.daysInYearMonth(year,selectedMonth.ordinal+1),
                    selectedDay,
                    minDay,
                    maxDay
                ){
                    onDayClick(it)
                }
            }
        }
        AnimatedVisibility(
            selectedMonth is Kal.Month &&
                    selectedMonth !in Kal.Month.Jul..Kal.Month.Sep,
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
                Kal.Month.Oct,
                selectedMonth,
                minMonth,
                maxMonth
            ){
                onMonthClick(it)
            }
            MonthUI(
                Kal.Month.Nov,
                selectedMonth,
                minMonth,
                maxMonth
            ){
                onMonthClick(it)
            }
            MonthUI(
                Kal.Month.Dec,
                selectedMonth,
                minMonth,
                maxMonth
            ){
                onMonthClick(it)
            }
        }
        AnimatedVisibility(
            selectedMonth is Kal.Month &&
                    selectedMonth in Kal.Month.Oct..Kal.Month.Dec,
            enter = enter,
            exit = exit,
        ){
            selectedMonth?.let {
                MonthDayPicker(
                    Kal.daysInYearMonth(year,selectedMonth.ordinal+1),
                    selectedDay,
                    minDay,
                    maxDay
                ){
                    onDayClick(it)
                }
            }
        }
    }
}

@Composable
fun MonthUI(
    month: Kal.Month,
    selected: Kal.Month?,
    minMonth: Kal.Month,
    maxMonth: Kal.Month,
    onClick: (Kal.Month)->Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    Box(
        modifier = Modifier
            .width(74.dep())
            .height(42.dep())
            .clip(RoundedCornerShape(21.dep()))
            .then(
                if (month in minMonth..maxMonth) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = LocalIndication.current
                    ) {
                        onClick(month)
                    }
                } else {
                    Modifier
                }
            )
            .background(
                if (
                    month in minMonth..maxMonth
                    && selected == month || isPressed
                )
                    Color(0xffEDF5FF)
                else Color.Transparent
            ),
        contentAlignment = Alignment.Center
    ){
        RobotoText(
            month.toString(),
            fontSize = 14.sep(),
            color =
            if(month !in minMonth..maxMonth)
                Color.Gray
            else if(selected==month)
                Color(0xff1B79E6)
            else Color(0xff243257),
            fontWeight = FontWeight.Bold
        )
    }

}

@Composable
fun YearSwitcher(
    selectedYear: Int,
    minDate: Kal.Date?,
    maxDate: Kal.Date?,
    onMode: (Boolean)->Unit,
    onYearPicked: (Int)->Unit
) {
    val currentYear by remember {
        mutableStateOf(Calendar.getInstance().get(Calendar.YEAR))
    }
    LaunchedEffect(key1 = Unit){
        if(minDate!=null&& maxDate!=null){
            if(minDate > maxDate || selectedYear < minDate.year || selectedYear > maxDate.year){
                throw IllegalStateException()
            }
        }
    }
    val minYear by remember {
        mutableStateOf(minDate?.year?:1)
    }
    val maxYear by remember {
        mutableStateOf(maxDate?.year?:9999)
    }
    var hasPrev by remember {
        mutableStateOf(minYear<selectedYear)
    }
    var hasNext by remember {
        mutableStateOf(maxYear>selectedYear)
    }
    LaunchedEffect(key1 = selectedYear){
        hasPrev = minYear<selectedYear
        hasNext = maxYear>selectedYear
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
            Icon(
                painter = painterResource(id = R.drawable.ic_left_chevron),
                contentDescription = null,
                modifier = Modifier
                    .size(18.dep())
                    .clip(CircleShape)
                    .then(
                        if (hasPrev) {
                            Modifier.clickable {
                                onYearPicked(selectedYear - 1)
                            }
                        } else {
                            Modifier
                        }
                    )
                    .background(if (!hasPrev) Color.Gray else Color(0xff1A79E5))
                    .padding(4.dep()),
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(40.dep()))
            Text(
                selectedYear.toString(),
                fontSize = 16.sep(),
                fontWeight = FontWeight.Bold,
                color = Color(0xff243257),
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                        selectionMode = !selectionMode
                    }
                    .padding(horizontal = 4.dep())
            )
            Spacer(modifier = Modifier.width(40.dep()))
            Icon(
                painter = painterResource(id = R.drawable.ic_right_chevron),
                contentDescription = null,
                modifier = Modifier
                    .size(18.dep())
                    .clip(CircleShape)
                    .then(
                        if (hasNext) {
                            Modifier.clickable {
                                onYearPicked(selectedYear + 1)
                            }
                        } else {
                            Modifier
                        }
                    )
                    .background(if (!hasNext) Color.Gray else Color(0xff1A79E5))
                    .padding(4.dep()),
                tint = Color.White
            )
        }
        AnimatedVisibility(
            selectionMode,
        enter = fadeIn(animationSpec = tween(700))/*+ expandIn(animationSpec = tween(700))*/,
        exit = fadeOut(animationSpec = tween(700))/*+ shrinkOut(animationSpec = tween(700))*/
        ) {
            val state = rememberLazyGridState()
            LaunchedEffect(key1 = selectedYear){
                state.scrollToItem(selectedYear-minYear)
            }
            LazyVerticalGrid(
                state = state,
                columns = GridCells.Fixed(3),
                verticalArrangement = Arrangement.spacedBy(21.dep()),
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(horizontal = 58.dep())
                    .fillMaxWidth()
                    .height(251.dep())
                    .drawWithContent {
                        val colors = listOf(Color.White, Color.Transparent)
                        val colors1 = listOf(Color.Transparent, Color.White)
                        drawContent()
                        drawRect(
                            brush = Brush.verticalGradient(colors, endY = 60f),
                        )
                        drawRect(
                            brush = Brush.verticalGradient(
                                colors1,
                                startY = size.height - 60f,
                                endY = size.height
                            ),
                        )
                    },
                contentPadding = PaddingValues(vertical = 20.dep())
            ) {
                items((minYear..maxYear).toList()){
                    val interactionSource = remember { MutableInteractionSource() }
                    val isPressed by interactionSource.collectIsPressedAsState()

                    Box(
                        modifier = Modifier
                            .width(74.dep())
                            .height(42.dep())
                            .clip(RoundedCornerShape(21.dep()))
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null//LocalIndication.current
                            ) {
                                onYearPicked(it)
                                selectionMode = false
                            }
                            .background(if (selectedYear == it || isPressed) Color(0xffEDF5FF) else Color.Transparent),
                        contentAlignment = Alignment.Center
                    ){
                        RobotoText(
                            it.toString(),
                            fontSize = 14.sep(),
                            color = if(selectedYear==it) Color(0xff1B79E6) else Color(0xff243257),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }

}

@OptIn(ExperimentalSnapperApi::class)
@Composable
fun MonthDayPicker(
    days: List<Kal.Day>,
    selectedDay: Int?,
    minDay: Int,
    maxDay: Int,
    onDaySelected: (Int) -> Unit
) {
    val listState = rememberLazyListState()
    val layoutInfo = rememberLazyListSnapperLayoutInfo(listState)
    LaunchedEffect(key1 = selectedDay){
        Log.d("fldkf","se")
        if(selectedDay!=null){
            listState.scrollToItem(selectedDay-1)
        }
        else{
            listState.scrollToItem(0)
            onDaySelected(1)
        }
    }
    LaunchedEffect(key1 = listState.isScrollInProgress){
        if(!listState.isScrollInProgress){
            val d = layoutInfo.currentItem
            d?.let {
                Log.d("fldkf","def")
                onDaySelected(d.index+1)
            }
        }
    }
    var initiateClick by remember {
        mutableStateOf(-1)
    }
    LaunchedEffect(key1 = initiateClick){
        if(initiateClick<0){
            return@LaunchedEffect
        }
        listState.animateScrollToItem(initiateClick)
    }
    BoxWithConstraints(
        modifier = Modifier
            .padding(vertical = 9.dep())
            .height(89.dep())
            .background(Color(0xffEDF5FF))
    ) {
        val full = localFullWidth.current
        val box = 54
        val pad = (full - box)/2f
        val halfRowWidth = constraints.maxWidth / 2
        Box(
            modifier = Modifier
                .width(64.dep())
                .fillMaxHeight()
                .clip(RoundedCornerShape(13.dep()))
                .background(Color(0xff1A79E5))
                .align(Alignment.Center)
        ){

        }
        LazyRow(
            state = listState,
            modifier = Modifier
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = pad.dep()),
            flingBehavior = rememberSnapperFlingBehavior(listState),
        ) {
            itemsIndexed(days) { i, item ->
                val opacity by remember {
                    derivedStateOf {
                        val currentItemInfo = listState.layoutInfo.visibleItemsInfo
                            .firstOrNull { it.index == i }
                            ?: return@derivedStateOf 0.5f
                        val itemHalfSize = currentItemInfo.size / 2
                        (1f - minOf(1f, abs(currentItemInfo.offset).toFloat() / halfRowWidth) * 0.5f)
                    }
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        //.scale(opacity)
                        .alpha(opacity)
                        .width((/*opacity **/ box).dep())
                        .fillMaxHeight()
                        .clickable {
                            initiateClick = item.dayOfMonth - 1
                        }
                    //.scale(opacity)
                    //.background(Color.Blue)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        RobotoText(
                            item.weekDay.toString(),
                            fontSize = (opacity*11).sep(),
                            fontWeight = FontWeight.Bold,
                            color =
                            if(item.dayOfMonth !in minDay..maxDay)
                                Color.LightGray
                            else if(opacity in 0.9f..1f) Color.White
                            else   Color(0xff243257)
                        )
                        Spacer(modifier = Modifier.height(12.dep()))
                        RobotoText(
                            item.dayOfMonth.toString(),
                            fontSize = (/*opacity**/11).sep(),
                            fontWeight = FontWeight.Bold,
                            color =
                            if(item.dayOfMonth !in minDay..maxDay)
                                Color.LightGray
                            else if(opacity in 0.9f..1f) Color.White
                            else   Color(0xff1A79E5)
                        )
                    }
                }
            }
        }

    }
}