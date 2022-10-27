package co.yore.splitnpay.pages

import androidx.compose.animation.*
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
import co.yore.splitnpay.addmembers.FontFamilyText
import co.yore.splitnpay.components.components.YoreDatePicker
import co.yore.splitnpay.components.components.YoreDatePickerData
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.models.DataIds
import co.yore.splitnpay.ui.theme.*
import co.yore.splitnpay.viewModels.currentYear
import co.yore.splitnpay.viewModels.getOrMyDefault
import kotlinx.coroutines.CoroutineScope

enum class DatePickerOption {
    Monthly,
    Weekly;

    val switch: DatePickerOption
        get() {
            val index = this.ordinal
            var nextIndex = index + 1
            val vals = enumValues<DatePickerOption>()
            val count = vals.size
            nextIndex %= count
            return vals[nextIndex]
        }
}

enum class DatePickerRange {
    Date,
    DateRange;

    val switch: DatePickerRange
        get() {
            val index = this.ordinal
            var nextIndex = index + 1
            val vals = enumValues<DatePickerRange>()
            val count = vals.size
            nextIndex %= count
            return vals[nextIndex]
        }
}

enum class DatePickerFromTo {
    From,
    To;

    val switch: DatePickerFromTo
        get() {
            val index = this.ordinal
            var nextIndex = index + 1
            val vals = enumValues<DatePickerFromTo>()
            val count = vals.size
            nextIndex %= count
            return vals[nextIndex]
        }
}

class DatePickerAdvancedBottomSheetModel(val callback: Callback): BottomSheetModel{
    interface Callback{
        fun scope(): CoroutineScope
        fun close()
    }
    /////////////////
    private val _resolver = Resolver()
    private val _notifier = NotificationService{id,arg->
        when(id){
            DataIds.monthlyOrWeekly -> {
                handleDatePickerOption(arg = arg)
            }
            DataIds.year->{
                years[combineKey] = arg as? Int?:return@NotificationService
                updateDatePicker()
            }
            DataIds.month->{
                months[combineKey] = arg as? Int?:return@NotificationService
                updateDatePicker()
            }
            DataIds.day->{
                val day = arg as? Int?:return@NotificationService
                days[combineKey] = day
                updateDatePicker()
            }
            DataIds.monthlyTab->{
                selectedMonthlyTab.value = arg as? String?:return@NotificationService
                if(selectedMonthlyTab.value == "Select Month Range"){
                    datePickerFromTo.value = DatePickerFromTo.From
                }
                updateDatePicker()
            }
            DataIds.weeklyTab->{
                selectedWeeklyTab.value = arg as? String?:return@NotificationService
                updateDatePicker()
            }
            DataIds.datePickerFromTo -> {
                handleDatePickerFromTo(arg = arg)
            }
        }
    }
    /////////////////
    override val resolver = _resolver
    override val notifier = _notifier
    override val scope get() = callback.scope()

    @Composable
    override fun Content() {
        DatePickerDemo()
    }

    override fun initialize() {

    }

    override fun clear() {

    }

    override fun onBack() {

    }
    ////////////
    //data
    ////////////
    private val datePickerOption = mutableStateOf(DatePickerOption.Monthly)
    private val datePickerFromTo = mutableStateOf(DatePickerFromTo.From)
    private val datePickerData = mutableStateOf(YoreDatePickerData())
    private val selectedMonthlyTab = mutableStateOf("Select Month")
    private val selectedWeeklyTab = mutableStateOf("Select Date")
    private val displayResult = mutableStateOf<DateDisplayData>(DateDisplayDataNone)
    ///////////
    private val years = mutableMapOf<String,Int>()
    private val months = mutableMapOf<String,Int?>()
    private val days = mutableMapOf<String,Int?>()
    init {
        _resolver.addAll(
            DataIds.monthlyOrWeekly to datePickerOption,
            DataIds.datePickerFromTo to datePickerFromTo,
            DataIds.datePickerFromData to datePickerData,
            DataIds.monthlyTab to selectedMonthlyTab,
            DataIds.weeklyTab to selectedWeeklyTab,
            DataIds.dateDisplayData to displayResult,
        )
    }
    /////////////
    private fun handleDatePickerOption(arg: Any?) {
        val option  = arg as? DatePickerOption ?: return
        datePickerOption.value = option
        updateDatePicker()
    }
    private fun updateDatePicker() {
        val ck = combineKey
        val year = years.getOrMyDefault(ck, currentYear)
        val month = months.getOrMyDefault(ck,null)
        val day = days.getOrMyDefault(ck,null)
        val showDates = datePickerOption.value== DatePickerOption.Weekly
        datePickerData.value = YoreDatePickerData(
            selectedYear = year,
            selectedMonth = month,
            selectedDay = day,
            dateSelectable = showDates
        )
    }
    private val combineKey: String
        get(){
            val _1st_level_key = datePickerOption.value.name
            val _2nd_level_key = if(datePickerOption.value== DatePickerOption.Monthly)
                selectedMonthlyTab.value
            else
                selectedWeeklyTab.value
            val _3rd_level_key = if(_2nd_level_key.contains("Range")) datePickerFromTo.value else "normal"
            val key = "$_1st_level_key-$_2nd_level_key-$_3rd_level_key"
            return key
        }
    private fun handleDatePickerFromTo(arg: Any?) {
        val option  = if(arg==true) DatePickerFromTo.From else DatePickerFromTo.To
        datePickerFromTo.value = option
        updateDatePicker()
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DatePickerDemo(
    datePickerOption: DatePickerOption = tState<DatePickerOption>(key = DataIds.monthlyOrWeekly).value,
    datePickerFromTo: DatePickerFromTo = tState<DatePickerFromTo>(key = DataIds.datePickerFromTo).value,
    datePickerData: YoreDatePickerData = tState<YoreDatePickerData>(key = DataIds.datePickerFromData).value,
    notifier: NotificationService = notifier(),
    selectedMonthlyTab: String = stringState(key = DataIds.monthlyTab).value,
    selectedWeeklyTab: String = stringState(key = DataIds.weeklyTab).value,
    displayResult: DateDisplayData = tState<DateDisplayData>(key = DataIds.dateDisplayData).value
) {
    val isMonthlySelected by remember(key1 = datePickerOption) {
        derivedStateOf {
            datePickerOption == DatePickerOption.Monthly
        }
    }
    val isRangeSelected by remember(selectedWeeklyTab,selectedMonthlyTab) {
        derivedStateOf {
            selectedMonthlyTab.contains("Range") || selectedWeeklyTab.contains("Range")
        }
    }
    val isFromSelected by remember(key1 = datePickerFromTo) {
        derivedStateOf {
            datePickerFromTo == DatePickerFromTo.From
        }
    }

    val tab1Text by remember(key1 = datePickerOption) {
        derivedStateOf {
            if (datePickerOption == DatePickerOption.Monthly) {
                R.string.select_monthly
            } else {
                R.string.select_date
            }
        }
    }
    val tab2Text by remember(key1 = datePickerOption) {
        derivedStateOf {
            if (datePickerOption == DatePickerOption.Monthly) {
                R.string.select_month_range
            } else {
                R.string.select_date_range
            }
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
                    .background(LightBlue5)
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
                    fadeIn(animationSpec = tween(700)) with //+scaleIn(initialScale = 0.92f, animationSpec = tween(700, delayMillis = 90)) with
                    fadeOut(animationSpec = tween(700))
                }
            ) {
                when(it){
                    true -> TabGroup(
                        text1 = "Select Month",
                        text2 = "Select Month Range",
                        selectedMonthlyTab,
                    ){
                        notifier.notify(DataIds.monthlyTab,it)
                    }
                    false -> TabGroup(
                        text1 = "Select Date",
                        text2 = "Select Date Range",
                        selectedWeeklyTab,
                    ){
                        notifier.notify(DataIds.weeklyTab,it)
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
                        notifier.notify(DataIds.datePickerFromTo,it)
                    }
                }
                AnimatedVisibility(visible = !isRangeSelected) {
                    37.sy()
                }
            }

            14.sy()

            DateResultDisplayUI(displayResult)

            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dep())
                    .height(47.dep())
            ) {
                CustomButton_3egxtx(
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
                horizontal = 21.dep(),
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
        SingleFromToUI("From",isFromSelected){onClick(true)}
        FromToDash()
        SingleFromToUI("To",!isFromSelected){onClick(false)}
    }
}

@Composable
fun RowScope.FromToDash() {
    Box(
        modifier = Modifier
            .height(1.dep())
            .fillMaxWidth()
            .weight(1f)
            .dashedBorder(
                width = 1.dep(),
                color = GreyShadow,
                shape = RoundedCornerShape(2.dep()),
                on = 5.dep(),
                off = 10.dep()
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
            if(selected)
                Bluish
            else
                GreyBorder
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
                rippleColor = Bluish,
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
    selected: String,
    onClick: (String) -> Unit
) {
    Row(modifier = Modifier.padding(start = 28.dep())) {
        SingleTab(
            text1,
            selected == text1
        ){
            onClick(text1)
        }
        53.sx()
        SingleTab(
            text2,
            selected == text2
        ){
            onClick(text2)
        }
    }
}

@Composable
fun SingleTab(
    text: String,
    selected: Boolean,
    onClick: ()->Unit
) {
    val color by remember(selected) {
        derivedStateOf {
            if(selected)
                DarkBlue
            else
                GreyBorder
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
//////////////
interface DateDisplayData
object DateDisplayDataNone: DateDisplayData

data class DateDisplayDataDates(
    val value: String,
    val duration: String,
): DateDisplayData
data class DateDisplayDataMonths(
    val from: String,
    val to: String,
    val duration: String
): DateDisplayData
/////////////
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DateResultDisplayUI(
    data: DateDisplayData
){
    AnimatedContent(data) {
        when(it){
            is DateDisplayDataNone->{
                15.sy()
            }
            is DateDisplayDataDates->{
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
                            text = stringResource(R.string.dates) + ":",
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
                    FontFamilyText(
                        text = it.duration,
                        fontSize = 14.sep(),
                        color = DarkBlue,
                        fontWeight = FontWeight(500)
                    )
                    15.sx()
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
    }
}
