package co.yore.splitnpay.pages.subpages

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import co.yore.splitnpay.R
import co.yore.splitnpay.components.components.CustomButton_3egxtx
import co.yore.splitnpay.components.components.FontFamilyText
import co.yore.splitnpay.components.components.YoreDatePicker
import co.yore.splitnpay.components.components.YoreDatePickerData
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.libs.jerokit.*
import co.yore.splitnpay.libs.jerokit.bottom_sheet.BottomSheetModel
import co.yore.splitnpay.models.*
import co.yore.splitnpay.pages.CustomRadioButton_2ofz97
import co.yore.splitnpay.ui.theme.*
import kotlinx.coroutines.CoroutineScope
import kotlin.math.ceil

class DatePickerAdvancedBottomSheetModel(val callback: Callback) : BottomSheetModel {
    sealed class Result{
        class MonthYear(
            val year: Int,
            val month: Int,
            private val display: String
        ) : Result() {
            override fun display(): String {
                return display
            }
        }

        class MonthYearRange(
            val from: MonthYear,
            val to: MonthYear,
            private val display: String
        ) : Result() {
            override fun display(): String {
                return display
            }
        }

        class Date(
            val year: Int,
            val month: Int,
            day: Int,
            private val display: String
        ) : Result() {
            override fun display(): String {
                return display
            }
        }

        class DateRange(
            val from: Date,
            val to: Date,
            private val display: String
        ) : Result() {
            override fun display(): String {
                return display
            }
        }

        abstract fun display(): String
    }
    interface Callback{
        fun scope(): CoroutineScope
        fun close()
        fun onContinue(result: Result)
    }

    // ///////////////
    private val _resolver = Resolver()
    private val _notifier = NotificationService{id, arg ->
        when (id){
            DataIds.monthlyOrWeekly -> {
                handleDatePickerOption(arg = arg)
            }
            DataIds.year -> {
                years[combineKey] = arg as? Int ?: return@NotificationService
                updateDatePicker()
            }
            DataIds.month -> {
                months[combineKey] = arg as? Int ?: return@NotificationService
                updateDatePicker()
            }
            DataIds.day -> {
                val day = arg as? Int ?: return@NotificationService
                days[combineKey] = day
                updateDatePicker()
            }
            DataIds.monthlyTab -> {
                selectedMonthlyTab.value = arg as? TimeTabs ?: return@NotificationService
                if (selectedMonthlyTab.value == TimeTabs.RANGE){
                    datePickerFromTo.value = DatePickerFromTo.From
                }
                updateDatePicker()
            }
            DataIds.weeklyTab -> {
                selectedWeeklyTab.value = arg as? TimeTabs ?: return@NotificationService
                if (selectedWeeklyTab.value == TimeTabs.RANGE){
                    datePickerFromTo.value = DatePickerFromTo.From
                }
                updateDatePicker()
            }
            DataIds.datePickerFromTo -> {
                handleDatePickerFromTo(arg = arg)
            }
            DataIds.datePickerContinueClick -> {
                val result: Result
                if (
                    datePickerOption.value == DatePickerOption.Monthly
                ){
                    if (selectedMonthlyTab.value == TimeTabs.RANGE){
                        val keyFrom = "${DatePickerOption.Monthly.name}-${TimeTabs.RANGE.name}-${DatePickerFromTo.From.name}"
                        val keyTo = "${DatePickerOption.Monthly.name}-${TimeTabs.RANGE.name}-${DatePickerFromTo.To}"
                        val fromYear = years[keyFrom] ?: currentYear
                        val toYear = years[keyTo] ?: currentYear
                        val fromMonth = months[keyFrom] ?: 0
                        val toMonth = months[keyTo] ?: 0
                        // ////////
                        var fromMonthString = monthNameShort(fromMonth)
                        var toMonthString = monthNameShort(toMonth)
                        var yearSuffix = ""
                        if (fromYear != toYear){
                            fromMonthString += " $fromYear"
                            toMonthString += " $toYear"
                        } else {
                            yearSuffix = ", $fromYear"
                        }
                        // ////////
                        result = Result.MonthYearRange(
                            from = Result.MonthYear(
                                year = fromYear,
                                month = fromMonth,
                                display = fromMonthString
                            ),
                            to = Result.MonthYear(
                                year = toYear,
                                month = toMonth,
                                display = toMonthString
                            ),
                            display = "$fromMonthString - $toMonthString$yearSuffix"
                        )
                    } else {
                        val ck = combineKey
                        val year = years[ck] ?: currentYear
                        val month = months[ck] ?: 0
                        result = Result.MonthYear(
                            year = year,
                            month = month,
                            display = "$month $year"
                        )
                    }
                } else {
                    if (selectedWeeklyTab.value == TimeTabs.RANGE){
                        val keyFrom = "${DatePickerOption.Weekly.name}-${TimeTabs.RANGE.name}-${DatePickerFromTo.From.name}"
                        val keyTo = "${DatePickerOption.Weekly.name}-${TimeTabs.RANGE.name}-${DatePickerFromTo.To}"
                        val fromYear = years[keyFrom] ?: currentYear
                        val toYear = years[keyTo] ?: currentYear
                        val fromMonth = months[keyFrom] ?: 0
                        val toMonth = months[keyTo] ?: 0
                        val fromDay = days[keyFrom] ?: 0
                        val toDay = days[keyTo] ?: 0
                        // ////
                        var display = ""
                        val fromDisplay = displayableDate(fromDay, fromMonth, fromYear)
                        val toDisplay = displayableDate(toDay, toMonth, toYear)
                        if (fromYear == toYear){
                            if (fromMonth == toMonth){
                                if (fromDay == toDay){
                                    display = displayableDate(fromDay, fromMonth, fromYear)
                                } else {
                                    val fromOrdinal = ordinal(fromDay)
                                    val toDayOrdinal = ordinal(toDay)
                                    val month = shortMonth(fromMonth)

                                    display = "$fromDay$fromOrdinal - $toDay$toDayOrdinal $month, $fromYear"
                                }
                            } else {
                                val fromOrdinal = ordinal(fromDay)
                                val toDayOrdinal = ordinal(toDay)
                                val fromMonthString = shortMonth(fromMonth)
                                val toMonthString = shortMonth(toMonth)

                                display = "$fromDay$fromOrdinal $fromMonthString - $toDay$toDayOrdinal $toMonthString , $fromYear"
                            }
                        } else {

                            display = "$fromDisplay - $toDisplay"
                        }
                        // ////
                        result = Result.DateRange(
                            from = Result.Date(
                                year = fromYear,
                                month = fromMonth,
                                day = fromDay,
                                display = fromDisplay
                            ),
                            to = Result.Date(
                                year = toYear,
                                month = toMonth,
                                day = toDay,
                                display = toDisplay
                            ),
                            display = display
                        )
                    } else {
                        val ck = combineKey
                        val year = years[ck] ?: currentYear
                        val month = months[ck] ?: 0
                        val day = days[ck] ?: 0
                        result = Result.Date(
                            year = year,
                            month = month,
                            day = day,
                            display = displayableDate(day, month, year)
                        )
                    }
                }
                callback.onContinue(result)
            }
        }
    }

    // ///////////////
    override val resolver = _resolver
    override val notifier = _notifier
    override val scope get() = callback.scope()

    private fun monthNameShort(value: Int?): String
    {
        return when (value) {
            1 -> "Jan"
            2 -> "Feb"
            3 -> "Mar"
            4 -> "Apr"
            5 -> "May"
            6 -> "Jun"
            7 -> "Jul"
            8 -> "Aug"
            9 -> "Sep"
            10 -> "Oct"
            11 -> "Nov"
            12 -> "Dec"
            else -> ""
        }
    }

    @Composable
    override fun Content() {
        AdvancedDatePickerBottomSheet()
    }

    override fun initialize() {

    }

    override fun clear() {

    }

    override fun onBack() {

    }

    // //////////
    // data
    // //////////
    private val datePickerOption = mutableStateOf(DatePickerOption.Monthly)
    private val datePickerFromTo = mutableStateOf(DatePickerFromTo.From)
    private val datePickerData = mutableStateOf(YoreDatePickerData())
    private val selectedMonthlyTab = mutableStateOf(TimeTabs.SINGLE)
    private val selectedWeeklyTab = mutableStateOf(TimeTabs.SINGLE)
    private val displayResult = mutableStateOf<DateDisplayData>(DateDisplayDataNone)
    private val canSubmit = mutableStateOf(false)

    // /////////
    private val years = mutableMapOf<String, Int>()
    private val months = mutableMapOf<String, Int?>()
    private val days = mutableMapOf<String, Int?>()
    init {
        _resolver.addAll(
            DataIds.monthlyOrWeekly to datePickerOption,
            DataIds.datePickerFromTo to datePickerFromTo,
            DataIds.datePickerFromData to datePickerData,
            DataIds.monthlyTab to selectedMonthlyTab,
            DataIds.weeklyTab to selectedWeeklyTab,
            DataIds.dateDisplayData to displayResult,
            DataIds.canSubmit to canSubmit
        )
    }

    // ///////////
    private fun handleDatePickerOption(arg: Any?) {
        val option = arg as? DatePickerOption ?: return
        datePickerOption.value = option
        updateDatePicker()
    }
    private fun updateDatePicker() {
        val ck = combineKey
        val year = years.getOrMyDefault(ck, currentYear)
        val month = months.getOrMyDefault(ck, null)
        val day = days.getOrMyDefault(ck, null)
        val showDates = datePickerOption.value == DatePickerOption.Weekly
        val ydpd = YoreDatePickerData(
            selectedYear = year,
            selectedMonth = month,
            selectedDay = day,
            dateSelectable = showDates
        )
        if (YoreDatePickerData.same(datePickerData.value, ydpd)){
            updateDisplayResultAndCanSubmit()
            return
        } else {
            datePickerData.value = ydpd
            updateDisplayResultAndCanSubmit()
        }
    }

    private fun monthDurationString(
        fromMonth: Int,
        toMonth: Int,
        fromYear: Int,
        toYear: Int
    ): String {
        if (fromMonth == 0 || toMonth == 0){return ""}
        return when (val duration = 12 * (toYear - fromYear) + toMonth - fromMonth + 1) {
            1 -> "1 Month"
            in 2..Int.MAX_VALUE -> "$duration Months"
            else -> ""
        }
    }

    private fun updateDisplayResultAndCanSubmit() {
        val error = updateCanSubmit()
        if (
            datePickerOption.value == DatePickerOption.Monthly &&
            selectedMonthlyTab.value == TimeTabs.RANGE
        ) {
            val keyFrom = "${DatePickerOption.Monthly.name}-${TimeTabs.RANGE.name}-${DatePickerFromTo.From.name}"
            val keyTo = "${DatePickerOption.Monthly.name}-${TimeTabs.RANGE.name}-${DatePickerFromTo.To}"
            val fromYear = years[keyFrom] ?: currentYear
            val toYear = years[keyTo] ?: currentYear
            val fromMonth = months[keyFrom] ?: 0
            val toMonth = months[keyTo] ?: 0
            var fromMonthString = monthNameShort(fromMonth)
            var toMonthString = monthNameShort(toMonth)
            var durationString = monthDurationString(fromMonth, toMonth, fromYear, toYear)
            if (fromYear != toYear){
                fromMonthString += " $fromYear"
                toMonthString += " $toYear"
            }
            displayResult.value = DateDisplayDataMonths(
                from = fromMonthString,
                to = toMonthString,
                duration = durationString,
                error = error
            )
        } else if (datePickerOption.value == DatePickerOption.Weekly){
            if (selectedWeeklyTab.value == TimeTabs.SINGLE){
                val ck = combineKey
                val year = years.getOrMyDefault(ck, currentYear)
                val month = months.getOrMyDefault(ck, null)
                val day = days.getOrMyDefault(ck, null)
                val display = displayableDate(day, month, year)
                displayResult.value = DateDisplayDataDate(
                    value = display,
                    "",
                    false,
                    error = error
                )
            } else if (selectedWeeklyTab.value == TimeTabs.RANGE){
                val keyFrom = "${DatePickerOption.Weekly.name}-${TimeTabs.RANGE.name}-${DatePickerFromTo.From.name}"
                val keyTo = "${DatePickerOption.Weekly.name}-${TimeTabs.RANGE.name}-${DatePickerFromTo.To}"
                val fromYear = years[keyFrom] ?: currentYear
                val toYear = years[keyTo] ?: currentYear
                val fromMonth = months[keyFrom] ?: 0
                val toMonth = months[keyTo] ?: 0
                val fromDay = days[keyFrom] ?: 0
                val toDay = days[keyTo] ?: 0
                val dif = dayDif(
                    dt1 = Date(
                        year = fromYear,
                        month = fromMonth,
                        day = fromDay
                    ),
                    dt2 = Date(
                        year = toYear,
                        month = toMonth,
                        day = toDay
                    )
                )
                val weeksCount = ceil(dif / 7f).toInt()
                val weeks = if (weeksCount > 1) "Weeks" else "Week"
                val duration = if (weeksCount == 0) "" else "$weeksCount $weeks"
                displayResult.value = DateDisplayDataDate(
                    value = "",
                    "",
                    true,
                    error = error
                )
                if (fromYear == toYear){
                    if (fromMonth == toMonth){
                        if (fromDay == toDay){
                            displayResult.value = DateDisplayDataDate(
                                value = displayableDate(fromDay, fromMonth, fromYear),
                                duration,
                                true,
                                error = error
                            )
                        } else {
                            val fromOrdinal = ordinal(fromDay)
                            val toDayOrdinal = ordinal(toDay)
                            val month = shortMonth(fromMonth)
                            if (toDay == 0){
                                displayResult.value = DateDisplayDataDate(
                                    value = "$fromDay$fromOrdinal $month, $fromYear -",
                                    duration,
                                    true,
                                    error = error
                                )
                            } else if (fromDay == 0){
                                displayResult.value = DateDisplayDataDate(
                                    value = "- $toDay$toDayOrdinal $month, $fromYear",
                                    duration,
                                    true,
                                    error = error
                                )
                            } else {
                                displayResult.value = DateDisplayDataDate(
                                    value = "$fromDay$fromOrdinal - $toDay$toDayOrdinal $month, $fromYear",
                                    duration,
                                    true,
                                    error = error
                                )
                            }
                        }
                    } else {
                        val fromOrdinal = ordinal(fromDay)
                        val toDayOrdinal = ordinal(toDay)
                        val fromMonthString = shortMonth(fromMonth)
                        val toMonthString = shortMonth(toMonth)
                        if (toMonth == 0){
                            displayResult.value = DateDisplayDataDate(
                                value = "$fromDay$fromOrdinal $fromMonthString, $fromYear -",
                                duration,
                                true,
                                error = error
                            )
                        } else if (fromMonth == 0){
                            displayResult.value = DateDisplayDataDate(
                                value = "- $toDay$toDayOrdinal $toMonthString, $fromYear",
                                duration,
                                true,
                                error = error
                            )
                        } else {
                            displayResult.value = DateDisplayDataDate(
                                value = "$fromDay$fromOrdinal $fromMonthString - $toDay$toDayOrdinal $toMonthString , $fromYear",
                                duration,
                                true,
                                error = error
                            )
                        }
                    }
                } else {
                    val fromDisplay = displayableDate(fromDay, fromMonth, fromYear)
                    val toDisplay = displayableDate(toDay, toMonth, toYear)
                    displayResult.value = DateDisplayDataDate(
                        value = "$fromDisplay - $toDisplay",
                        duration,
                        true,
                        error = error
                    )
                }
            }
        } else {
            displayResult.value = DateDisplayDataNone
        }
    }

    private fun updateCanSubmit(): Boolean {
        var error = false
        if (
            datePickerOption.value == DatePickerOption.Monthly &&
            selectedMonthlyTab.value == TimeTabs.SINGLE
        ){
            val month = months[combineKey] ?: 0
            canSubmit.value = month > 0
        } else if (
            datePickerOption.value == DatePickerOption.Monthly &&
            selectedMonthlyTab.value == TimeTabs.RANGE
        ) {
            val keyFrom = "${DatePickerOption.Monthly.name}-${TimeTabs.RANGE.name}-${DatePickerFromTo.From.name}"
            val keyTo = "${DatePickerOption.Monthly.name}-${TimeTabs.RANGE.name}-${DatePickerFromTo.To}"
            val fromMonth = months[keyFrom] ?: 0
            val toMonth = months[keyTo] ?: 0
            val fromYear = years[keyFrom] ?: currentYear
            val toYear = years[keyTo] ?: currentYear
            error = dayDif(
                Date(
                    day = 1,
                    month = fromMonth,
                    year = fromYear
                ),
                Date(
                    day = 1,
                    month = toMonth,
                    year = toYear
                )
            ) < 0
            canSubmit.value = fromMonth > 0 && toMonth > 0 && !error
        } else if (
            datePickerOption.value == DatePickerOption.Weekly &&
            selectedWeeklyTab.value == TimeTabs.SINGLE
        ){
            val ck = combineKey
            val year = years[ck] ?: currentYear
            val month = months[ck] ?: 0
            val day = days[ck] ?: 0
            canSubmit.value = year > 0 && month > 0 && day > 0
        } else if (
            datePickerOption.value == DatePickerOption.Weekly &&
            selectedWeeklyTab.value == TimeTabs.RANGE
        ){
            val keyFrom = "${DatePickerOption.Weekly.name}-${TimeTabs.RANGE.name}-${DatePickerFromTo.From.name}"
            val keyTo = "${DatePickerOption.Weekly.name}-${TimeTabs.RANGE.name}-${DatePickerFromTo.To}"
            val fromYear = years[keyFrom] ?: currentYear
            val fromMonth = months[keyFrom] ?: 0
            val fromDay = days[keyFrom] ?: 0

            val toYear = years[keyTo] ?: currentYear
            val toMonth = months[keyTo] ?: 0
            val toDay = days[keyTo] ?: 0
            error = dayDif(
                Date(
                    day = fromDay,
                    month = fromMonth,
                    year = fromYear
                ),
                Date(
                    day = toDay,
                    month = toMonth,
                    year = toYear
                )
            ) < 0
            canSubmit.value =
                fromYear > 0 &&
                fromMonth > 0 &&
                fromDay > 0 &&
                toYear > 0 &&
                toMonth > 0 &&
                toDay > 0 &&
                !error
        }
        return error
    }

    private val combineKey: String
        get(){
            val _1st_level_key = datePickerOption.value.name
            val _2nd_level_key = if (datePickerOption.value == DatePickerOption.Monthly) {
                selectedMonthlyTab.value
            } else {
                selectedWeeklyTab.value
            }
            val _3rd_level_key = if (_2nd_level_key == TimeTabs.RANGE) datePickerFromTo.value.name else "normal"
            val key = "$_1st_level_key-$_2nd_level_key-$_3rd_level_key"
            return key
        }
    private fun handleDatePickerFromTo(arg: Any?) {
        val option = if (arg == true) DatePickerFromTo.From else DatePickerFromTo.To
        datePickerFromTo.value = option
        updateDatePicker()
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AdvancedDatePickerBottomSheet(
    datePickerOption: DatePickerOption = tState<DatePickerOption>(key = DataIds.monthlyOrWeekly).value,
    datePickerFromTo: DatePickerFromTo = tState<DatePickerFromTo>(key = DataIds.datePickerFromTo).value,
    datePickerData: YoreDatePickerData = tState<YoreDatePickerData>(key = DataIds.datePickerFromData).value,
    notifier: NotificationService = notifier(),
    selectedMonthlyTab: TimeTabs = tState<TimeTabs>(key = DataIds.monthlyTab).value,
    selectedWeeklyTab: TimeTabs = tState<TimeTabs>(key = DataIds.weeklyTab).value,
    displayResult: DateDisplayData = tState<DateDisplayData>(key = DataIds.dateDisplayData).value,
    canSubmit: Boolean = boolState(key = DataIds.canSubmit).value
) {
    val isMonthlySelected by remember(key1 = datePickerOption) {
        derivedStateOf {
            datePickerOption == DatePickerOption.Monthly
        }
    }
    val isRangeSelected by remember(
        datePickerOption,
        selectedWeeklyTab,
        selectedMonthlyTab
    ) {
        derivedStateOf {
            (
                datePickerOption == DatePickerOption.Monthly &&
                    selectedMonthlyTab == TimeTabs.RANGE
                ) ||
                (
                    datePickerOption == DatePickerOption.Weekly &&
                        selectedWeeklyTab == TimeTabs.RANGE
                    )
        }
    }
    val isFromSelected by remember(key1 = datePickerFromTo) {
        derivedStateOf {
            datePickerFromTo == DatePickerFromTo.From
        }
    }

    Box(
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            20.sy()
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dep()))
                    .height(2.dep())
                    .width(19.dep())
                    .background(SteelBlue)
                    .align(Alignment.CenterHorizontally)
            )
            16.sy()
            Column(
                modifier = Modifier
                    .padding(horizontal = 23.dep())
                    .fillMaxWidth()
            ) {
                CustomRadioButton_2ofz97(
                    contentDescription = "monthly",
                    radioButtonName = stringResource(R.string.show_monthly),
                    isSelected = isMonthlySelected
                ) {
                    notifier.notify(DataIds.monthlyOrWeekly, DatePickerOption.Monthly)
                }
                5.sy()
                CustomRadioButton_2ofz97(
                    contentDescription = "weekly",
                    radioButtonName = stringResource(R.string.show_weekly),
                    isSelected = !isMonthlySelected
                ) {
                    notifier.notify(DataIds.monthlyOrWeekly, DatePickerOption.Weekly)
                }
            }
            25.sy()
            AnimatedContent(
                targetState = isMonthlySelected,
                transitionSpec = {
                    fadeIn(animationSpec = tween(700)) with // +scaleIn(initialScale = 0.92f, animationSpec = tween(700, delayMillis = 90)) with
                        fadeOut(animationSpec = tween(700))
                }
            ) {
                when (it){
                    true -> TabGroup(
                        text1 = "Select Month",
                        text2 = "Select Month Range",
                        selectedMonthlyTab
                    ){
                        notifier.notify(DataIds.monthlyTab, it)
                    }
                    false -> TabGroup(
                        text1 = "Select Date",
                        text2 = "Select Date Range",
                        selectedWeeklyTab
                    ){
                        notifier.notify(DataIds.weeklyTab, it)
                    }
                }
            }

            25.sy()
            YoreDatePicker(
                datePickerData,
                {
                    notifier.notify(DataIds.year, it)
                },
                {
                    notifier.notify(DataIds.month, it)
                },
                {
                    notifier.notify(DataIds.day, it)
                }
            ) {
                AnimatedVisibility(
                    visible = isRangeSelected
                ) {
                    FromToSection(
                        isFromSelected
                    ){
                        notifier.notify(DataIds.datePickerFromTo, it)
                    }
                }
                AnimatedVisibility(visible = !isRangeSelected) {
                    37.sy()
                }
            }

            DateResultDisplayUI(displayResult)

            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dep())
                    .height(47.dep())
            ) {
                CustomButton_3egxtx(
                    enabled = canSubmit,
                    text = stringResource(R.string.submit),
                    onClick = {
                        notifier.notify(DataIds.datePickerContinueClick)
                    },
                    contentDescription = ""
                )
            }
            26.sy()
        }
    }
}

@Composable
fun FromToSection(
    isFromSelected: Boolean,
    onClick: (from: Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(
                horizontal = 21.dep()
            )
            .padding(
                top = 25.dep(),
                bottom = 29.dep()
            )
            .height(40.dep())
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SingleFromToUI("From", isFromSelected){onClick(true)}
        FromToDash()
        SingleFromToUI("To", !isFromSelected){onClick(false)}
    }
}

@Composable
fun RowScope.FromToDash() {
    Box(
        modifier = Modifier
            .height(1.dep())
            .fillMaxWidth()
            .padding(horizontal = 8.dep())
            .weight(1f)
            .dashedBorder(
                width = 1.dep(),
                color = RockBlue,
                shape = RoundedCornerShape(2.dep()),
                on = 4.dep(),
                off = 4.dep()
            )
    )
}

@Composable
fun SingleFromToUI(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val color by remember(selected) {
        derivedStateOf {
            if (selected) {
                CuriousBlue
            } else {
                RockBlue
            }
        }
    }
    val animatedColor by animateColorAsState(
        targetValue = color,
        tween(700)
    )
    Row(
        modifier = Modifier
            .border(
                width = 1.dep(),
                color = animatedColor,
                shape = RoundedCornerShape(11.dep())
            )
            .clip(shape = RoundedCornerShape(11.dep()))
            .clickable(
                rippleRadius = 76.dep(),
                rippleColor = CuriousBlue,
                onClick = {
                    onClick()
                }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .padding(
                    start = 8.dep(),
                    top = 9.dep(),
                    bottom = 10.dep()
                )
                .size(20.dep()),
            painter = painterResource(id = R.drawable.calendar_2),
            contentDescription = "",
            tint = animatedColor
        )
        11.sx()
        FontFamilyText(
            text = text,
            color = animatedColor,
            fontSize = 14.sep(),
            fontWeight = FontWeight.Bold
        )
        11.sx()
    }
}

@Composable
fun TabGroup(
    text1: String,
    text2: String,
    selected: TimeTabs,
    onClick: (TimeTabs) -> Unit
) {
    Row(modifier = Modifier.padding(start = 28.dep())) {
        SingleTab(
            text1,
            selected == TimeTabs.SINGLE
        ){
            onClick(TimeTabs.SINGLE)
        }
        53.sx()
        SingleTab(
            text2,
            selected == TimeTabs.RANGE
        ){
            onClick(TimeTabs.RANGE)
        }
    }
}

@Composable
fun SingleTab(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val color by remember(selected) {
        derivedStateOf {
            if (selected) {
                CloudBurst
            } else {
                RockBlue
            }
        }
    }
    val animatedColor by animateColorAsState(targetValue = color, tween(700))
    FontFamilyText(
        modifier = Modifier.clickable(
            interactionSource = MutableInteractionSource(),
            indication = null
        ) {
            onClick()
        },
        text = text,
        fontWeight = FontWeight.Bold,
        fontSize = 15.sep(),
        color = animatedColor
    )
}

// ////////////

// ///////////
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DateResultDisplayUI(
    data: DateDisplayData
){
    val show by remember(data){
        derivedStateOf {
            if (data !is DateDisplayDataNone) 1f else 0f
        }
    }
    val animatedShow by animateFloatAsState(
        targetValue = show,
        tween(500)
    )
    (15 * (1f - animatedShow)).sy()
    (14 * (animatedShow)).sy()
    val borderColor by animateColorAsState(
        targetValue = if (data.error) RadicalRed else BlackSqueeze1,
        tween(500)
    )
    val backgroundColor by animateColorAsState(
        targetValue = if (data.error) LavenderBlush2 else BlackSqueeze1,
        tween(500)
    )
    Row(
        modifier = Modifier
            .padding(horizontal = 31.dep())
            .padding(bottom = 26.dep())
            .fillMaxWidth()
            .height((animatedShow * 46).dep())
            .clip(
                shape = RoundedCornerShape(5.dep())
            )
            .border(
                width = 1.dep(),
                color = borderColor,
                shape = RoundedCornerShape(5.dep())
            )
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(5.dep())
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        AnimatedContent(
            data,
            transitionSpec = { fadeIn() with fadeOut() }
        ){
            when (it){
                is DateDisplayDataDate -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Row() {
                            20.sx()
                            FontFamilyText(
                                text = (if (it.range) stringResource(R.string.dates) else stringResource(R.string.date)) + ": ",
                                fontSize = 12.sep(),
                                color = Lynch
                            )
                            FontFamilyText(
                                text = it.value,
                                fontSize = 12.sep(),
                                color = Black,
                                letterSpacing = (-0.33).sep()
                            )
                        }
                        Row(){
                            FontFamilyText(
                                text = it.duration,
                                fontSize = 14.sep(),
                                color = CloudBurst,
                                fontWeight = FontWeight(500)
                            )
                            15.sx()
                        }
                    }
                }
                is DateDisplayDataMonths -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Row() {
                            20.sx()
                            FontFamilyText(
                                text = stringResource(R.string.from) + ":",
                                fontSize = 12.sep(),
                                color = Lynch
                            )
                            3.sx()
                            FontFamilyText(
                                text = it.from,
                                fontSize = 12.sep(),
                                color = Black
                            )
                            33.sx()
                            FontFamilyText(
                                text = stringResource(R.string.to) + ":",
                                fontSize = 12.sep(),
                                color = Lynch
                            )
                            3.sx()
                            FontFamilyText(
                                text = it.to,
                                fontSize = 12.sep(),
                                color = Black
                            )
                        }
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        )
                        FontFamilyText(
                            text = it.duration,
                            fontSize = 14.sep(),
                            color = CloudBurst,
                            fontWeight = FontWeight(500)
                        )
                        15.sx()
                    }
                }
            }
        }
    }
    /*AnimatedContent(
        data,
        transitionSpec = { fadeIn() with fadeOut() }
    ) {
        when(it){
            is DateDisplayDataNone->{
                15.sy()
            }
            is DateDisplayDataDate->{
                Row(
                    modifier = Modifier
                        .padding(horizontal = 31.dep())
                        .padding(bottom = 26.dep())
                        .fillMaxWidth()
                        .clip(
                            shape = RoundedCornerShape(5.dep())
                        )
                        .background(
                            color = LightGrey7,
                            shape = RoundedCornerShape(5.dep())
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .padding(
                                top = 16.dep(),
                                bottom = 16.dep(),
                            )
                    ) {
                        20.sx()
                        FontFamilyText(
                            text = (if(it.range) stringResource(R.string.dates) else stringResource(R.string.date))+ ":",
                            fontSize = 12.sep(),
                            color = LightGrey6
                        )
                        FontFamilyText(
                            text = it.value,
                            fontSize = 12.sep(),
                            color = Black,
                            letterSpacing = (-0.33).sep()
                        )
                    }
                    Row(){
                        FontFamilyText(
                            text = it.duration,
                            fontSize = 14.sep(),
                            color = DarkBlue,
                            fontWeight = FontWeight(500)
                        )
                        15.sx()
                    }
                }
            }
            is DateDisplayDataMonths->{
                Row(
                    modifier = Modifier
                        .padding(horizontal = 31.dep())
                        .padding(bottom = 26.dep())
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(5.dep()))
                        .background(
                            color = LightGrey7,
                            shape = RoundedCornerShape(5.dep())
                        ),
                    //horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .padding(
                                top = 16.dep(),
                                bottom = 16.dep(),
                            )
                    ) {
                        20.sx()
                        FontFamilyText(
                            text = stringResource(R.string.from) + ":",
                            fontSize = 12.sep(),
                            color = LightGrey6
                        )
                        3.sx()
                        FontFamilyText(
                            text = it.from,
                            fontSize = 12.sep(),
                            color = Black
                        )
                        33.sx()
                        FontFamilyText(
                            text = stringResource(R.string.to) + ":",
                            fontSize = 12.sep(),
                            color = LightGrey6
                        )
                        3.sx()
                        FontFamilyText(
                            text =  it.to,
                            fontSize = 12.sep(),
                            color = Black
                        )
                    }
                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f))
                    FontFamilyText(
                        text = it.duration,
                        fontSize = 14.sep(),
                        color = DarkBlue,
                        fontWeight = FontWeight(500)
                    )
                    15.sx()
                }
            }
        }
    }*/
}
