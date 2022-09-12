package co.yore.splitnpay

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import dev.chrisbanes.snapper.rememberSnapperFlingBehavior
import java.util.*
import kotlin.math.abs

object Kal{
    enum class Month{
        JAN,        FEB,        MAR,
        APR,        MAY,        JUN,
        JUL,        AUG,        SEP,
        OCT,        NOV,        DEC
    }
    enum class WeekDay{
        SUN,
        MON,
        TUE,
        WED,
        THU,
        FRI,
        SAT,
        NONE,
    }
    data class Day(
        val weekDay: WeekDay,
        val dayOfMonth: Int
    )

    fun numberOfDaysInYearMonth(year: Int, month: Month): Int{
        return when(month){
            Month.JAN -> 31
            Month.FEB -> if(year%400==0 || (year%4==0&&year%100!=0)) 29 else 28
            Month.MAR -> 31
            Month.APR -> 30
            Month.MAY -> 31
            Month.JUN -> 30
            Month.JUL -> 31
            Month.AUG -> 31
            Month.SEP -> 30
            Month.OCT -> 31
            Month.NOV -> 30
            Month.DEC -> 31
        }
    }

    fun weekDayOfDate(day: Int, month: Int, year: Int): WeekDay{
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
            else -> WeekDay.NONE
        }
    }

    fun daysInYearMonth(year: Int, month: Month): List<Day>{
        val c = Calendar.getInstance()

    }
}

@Composable
fun DatePickerUI() {
    BoxWithConstraints {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            YearSwitcher()
            Spacer(modifier = Modifier.height(24.dep()))
            MonthPicker()
        }
    }
}


@Composable
fun MonthPicker() {
    var selected by remember { mutableStateOf("") }
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
    Row(
        modifier = Modifier
            .padding(horizontal = 58.dep())
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        MonthUI("Jan",selected = selected=="Jan"){selected="Jan"}
        MonthUI("Feb",selected = selected=="Feb"){selected="Feb"}
        MonthUI("Mar",selected = selected=="Mar"){selected="Mar"}
    }
    AnimatedVisibility(
        listOf("Jan","Feb","Mar").contains(selected),
        enter = enter,
        exit = exit,
    ){
        MonthDayPicker()
    }
    AnimatedVisibility(
        !listOf("Jan","Feb","Mar").contains(selected),
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
        MonthUI("Apr",selected = selected=="Apr"){selected="Apr"}
        MonthUI("May",selected = selected=="May"){selected="May"}
        MonthUI("Jun",selected = selected=="Jun"){selected="Jun"}
    }
    AnimatedVisibility(
        listOf("Apr","May","Jun").contains(selected),
        enter = enter,
        exit = exit,
    ){
        MonthDayPicker()
    }
    AnimatedVisibility(
        !listOf("Apr","May","Jun").contains(selected),
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
        MonthUI("Jul",selected = selected=="Jul"){selected="Jul"}
        MonthUI("Aug",selected = selected=="Aug"){selected="Aug"}
        MonthUI("Sep",selected = selected=="Sep"){selected="Sep"}
    }
    AnimatedVisibility(
        listOf("Jul","Aug","Sep").contains(selected),
        enter = enter,
        exit = exit,
    ){
        MonthDayPicker()
    }
    AnimatedVisibility(
        !listOf("Jul","Aug","Sep").contains(selected),
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
        MonthUI("Oct",selected = selected=="Oct"){selected="Oct"}
        MonthUI("Nov",selected = selected=="Nov"){selected="Nov"}
        MonthUI("Dec",selected = selected=="Dec"){selected="Dec"}
    }
    AnimatedVisibility(
        listOf("Oct","Nov","Dec").contains(selected),
        enter = enter,
        exit = exit,
    ){
        MonthDayPicker()
    }
}

@Composable
fun MonthUI(
    s: String,
    selected: Boolean = false,
    onClick: ()->Unit
) {
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
                onClick()
            }
            .background(if (selected || isPressed) Color(0xffEDF5FF) else Color.Transparent),
        contentAlignment = Alignment.Center
    ){
        RobotoText(
            s,
            fontSize = 14.sep(),
            color = if(selected) Color(0xff1B79E6) else Color(0xff243257),
            fontWeight = FontWeight.Bold
        )
    }

}

@Composable
fun YearSwitcher() {
    Row(){
        Icon(
            painter = painterResource(id = R.drawable.ic_left_chevron),
            contentDescription = null,
            modifier = Modifier
                .size(18.dep())
                .clip(CircleShape)
                .clickable { }
                .background(Color(0xff1A79E5))
                .padding(4.dep()),
            tint = Color.White
        )
        Spacer(modifier = Modifier.width(40.dep()))
        Text(
            "2022",
            fontSize = 16.sep(),
            fontWeight = FontWeight.Bold,
            color = Color(0xff243257)
        )
        Spacer(modifier = Modifier.width(40.dep()))
        Icon(
            painter = painterResource(id = R.drawable.ic_right_chevron),
            contentDescription = null,
            modifier = Modifier
                .size(18.dep())
                .clip(CircleShape)
                .clickable { }
                .background(Color(0xff1A79E5))
                .padding(4.dep()),
            tint = Color.White
        )
    }
}

@OptIn(ExperimentalSnapperApi::class)
@Composable
fun MonthDayPicker() {
    val items = remember {
        (1..31).map { it.toString() }
    }
    val listState = rememberLazyListState()
    BoxWithConstraints(
        modifier = Modifier
            .padding(vertical = 9.dep())
            .height(89.dep())
            .background(Color(0xffEDF5FF))
    ) {
        val full = localFullWidth.current
        val box = 64
        val pad = (full - box)/2f
        val halfRowWidth = constraints.maxWidth / 2
        Box(
            modifier = Modifier
                .width(box.dep())
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
            itemsIndexed(items) { i, item ->
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
                        .width((opacity * box).dep())
                        .fillMaxHeight()
                    //.scale(opacity)
                    //.background(Color.Blue)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        RobotoText(
                            "Week",
                            fontSize = (opacity*11).sep(),
                            fontWeight = FontWeight.Bold,
                            color = if(opacity in 0.9f..1f) Color.White else   Color(0xff243257)
                        )
                        Spacer(modifier = Modifier.height(12.dep()))
                        RobotoText(
                            i.toString(),
                            fontSize = (opacity*11).sep(),
                            fontWeight = FontWeight.Bold,
                            color = if(opacity in 0.9f..1f) Color.White else   Color(0xff1A79E5)
                        )
                    }
                }
            }
        }

    }
}