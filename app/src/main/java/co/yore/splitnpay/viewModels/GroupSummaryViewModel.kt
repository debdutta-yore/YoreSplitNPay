package co.yore.splitnpay.viewModels

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.yore.splitnpay.components.components.MemberTransact
import co.yore.splitnpay.components.components.YoreDatePickerData
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.models.*
import co.yore.splitnpay.pages.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

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

class GroupSummaryViewModel(
    private val summaryRepo: GroupSummaryRepository = GroupSummaryRepositoryImpl(),
    private val expenseRepository: ExpenseRepository = ExpenseRepositoryImpl(),
    private val timeFilterRepo: TimeFilterRepository = TimeFilterRepositoryImpl()
) : ViewModel(), WirelessViewModelInterface {
    override val softInputMode = mutableStateOf(SoftInputMode.adjustNothing)
    private val _resolver = Resolver()

    ///////
    override val resolver: Resolver
    get(){
        val r = _resolver
        return r
    }
    override val navigation = Navigation()
    override val permissionHandler = PermissionHandler()
    override val resultingActivityHandler = ResultingActivityHandler()
    //////////////////////////////////////////
    private val _members = mutableStateListOf<SplitSelectableMember>()
    private val _willGetTransactions = mutableStateListOf<MemberTransact>()
    private val _willPayTransactions = mutableStateListOf<MemberTransact>()
    private val _payTotal = mutableStateOf(0f)
    private val _getTotal = mutableStateOf(4500f)
    private val _payeeName = mutableStateOf("Sushil")
    private val _payerName = mutableStateOf("You")
    private val _selectedBalanceExpenseTab = mutableStateOf(0)

    private val _expensesCategories = mutableStateListOf<CategoryExpense>()
    private val _expenseChartList = mutableStateListOf<ExpenseChartData>()
    private val _expensePieChartList = mutableStateListOf<PieData>()
    private val _expensesOvertimeTotal = mutableStateOf(0f)
    private val _expenseTimeFrame = mutableStateOf("Month")
    private val _expensesTimeFrameText = mutableStateOf("June")
    private val _filterTimeFrame = mutableStateOf<TimeOptionData>(TimeOptionData.Normal("All Time"))
    private val _expensesOvertimeTotalTransactions = mutableStateOf("1")

    private val _datePickerOption = mutableStateOf(DatePickerOption.Monthly)
    private val _datePickerDateRange = mutableStateOf(DatePickerRange.Date)
    private val _datePickerFromTo = mutableStateOf(DatePickerFromTo.From)

    private val _selectedFromYear = mutableStateOf(2022)
    private val _selectedFromMonth = mutableStateOf(1)
    private val _selectedFromDay = mutableStateOf(1)
    private val _numberOfSelectedTimeframe = mutableStateOf("1 Month")
    private val _selectedDates = mutableStateOf("")

    private val _selectedToYear = mutableStateOf(2022)
    private val _selectedToMonth = mutableStateOf(1)
    private val _selectedToDay = mutableStateOf(1)
    private val summaryMode = mutableStateOf(SummaryMode.CATEGORY)

    private val _datePickerData1 = mutableStateOf(
        YoreDatePickerData(
            dateSelectable = false
        )
    )
    private val _datePickerToData = mutableStateOf(YoreDatePickerData())
    private val _timeFilterItems = mutableStateListOf<SheetItem>()

    private val _statusBarColor = mutableStateOf<StatusBarColor?>(null)

    private val selectedWeeklyTab = mutableStateOf("Select Date")
    private val selectedMonthlyTab = mutableStateOf("Select Month")
    private val dateDisplayData = mutableStateOf<DateDisplayData>(DateDisplayDataMonths("ffdfdfljkf","fldfdffkd","flfdfffdjf"))
    private val years = mutableMapOf<String,Int>()
    private val months = mutableMapOf<String,Int?>()
    private val days = mutableMapOf<String,Int?>()

    //////////////////////////////////////////
    private val combineKey: String
    get(){
        val _1st_level_key = _datePickerOption.value.name
        val _2nd_level_key = if(_datePickerOption.value==DatePickerOption.Monthly)
            selectedMonthlyTab.value
        else
            selectedWeeklyTab.value
        val _3rd_level_key = if(_2nd_level_key.contains("Range")) _datePickerFromTo.value else "normal"
        val key = "$_1st_level_key-$_2nd_level_key-$_3rd_level_key"
        return key
    }
    @OptIn(ExperimentalMaterialApi::class)
    override val notifier = NotificationService { id, arg ->
        when (id) {
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
                    _datePickerFromTo.value = DatePickerFromTo.From
                }
                updateDatePicker()
            }
            DataIds.weeklyTab->{
                selectedWeeklyTab.value = arg as? String?:return@NotificationService
                updateDatePicker()
            }
            WirelessViewModelInterface.startupNotification -> {
                setUpStatusBarColor()
            }
            DataIds.back -> {
                navigation.scope { navHostController, lifecycleOwner, toaster ->
                    navHostController.popBackStack()
                }
            }
            "${DataIds.back}group_split_summary" -> {
                navigation.scope { navHostController, lifecycleOwner, toaster ->
                    navHostController.navigateUp()
                }
            }
            DataIds.selectBalanceMember -> {
                onSelectMemberClick(arg)
            }
            DataIds.selectedBalanceExpenseTab -> {
                onSelectTabClick(arg)
            }
            DataIds.expenseCategoryItemClick -> {
                //TODO: Add click event
            }
            DataIds.expenseCategorySearchClick -> {
                //TODO: Add click event
            }
            DataIds.expenseCategoryFilterClick -> {
                //TODO: Add click event
            }
            DataIds.expenseTimeMode -> {
                mySheeting.change(Sheets.TimeFilter)
                mySheeting.show()
            }
            DataIds.timeFilterSelectionClick -> {// not inside date picker
                selectItem(arg)
            }
            DataIds.monthlyOrWeekly -> {
                handleDatePickerOption(arg = arg)
            }

            DataIds.datePickerFromTo -> {
                handleDatePickerFromTo(arg = arg)
            }
            DataIds.summaryMode->{
                mySheeting.change(Sheets.ExpenseFilter)
                mySheeting.show()
            }

            /*DataIds.datePickerDateRange -> {
                handleDatePickerRange(arg = arg)
            }*/

            /*DataIds.selectedFromYear -> {
                handleSelectionFromYear(arg = arg)
            }
            DataIds.selectedFromMonth -> {
                handleSelectionFromMonth(arg = arg)
            }
            DataIds.selectedFromDay -> {
                handleSelectionFromDay(arg = arg)
            }

            DataIds.selectedToYear -> {
                handleSelectionToYear(arg = arg)
            }

            DataIds.selectedToMonth -> {
                handleSelectionToMonth(arg = arg)
            }
            DataIds.selectedToDay -> {
                handleSelectionToDay(arg = arg)
            }*/
            DataIds.datePickerContinueClick -> {
                /*//TODO: Navigate
                //_expensesCategories.filter { it.category}
                sheetHandler.state {
                    hide()
                }*/
            }
        }
    }

    private fun updateDatePicker() {
        val ck = combineKey
        val year = years.getOrMyDefault(ck,currentYear)
        val month = months.getOrMyDefault(ck,null)
        val day = days.getOrMyDefault(ck,null)
        val showDates = _datePickerOption.value==DatePickerOption.Weekly
        _datePickerData1.value = YoreDatePickerData(
            selectedYear = year,
            selectedMonth = month,
            selectedDay = day,
            dateSelectable = showDates
        )
    }

    private fun handleDatePickerRange(arg: Any?) {
        val option  = arg as? DatePickerRange ?: return
        _datePickerDateRange.value = option
    }

    private fun handleDatePickerFromTo(arg: Any?) {
        val option  = if(arg==true) DatePickerFromTo.From else DatePickerFromTo.To
        _datePickerFromTo.value = option
        updateDatePicker()
    }

    private fun handleDatePickerOption(arg: Any?) {
        val option  = arg as? DatePickerOption ?: return
        _datePickerOption.value = option
        updateDatePicker()
    }

    private fun handleSelectionToYear(arg: Any?) {
        _datePickerToData.value = _datePickerToData.value.copy(selectedYear = arg as Int)
        _datePickerToData.value =
            _datePickerToData.value.copy(selectedMonth = _datePickerToData.value.minDate?.month)
        _datePickerToData.value =
            _datePickerToData.value.copy(selectedDay = _datePickerToData.value.minDate?.day)
    }
    override val sheeting = Sheeting(
        sheetMap = mapOf(
            Sheets.ExpenseFilter to ExpenseFilterBottomSheetModel(
                object : ExpenseFilterBottomSheetModel.Callback{
                    override fun scope(): CoroutineScope {
                        return viewModelScope
                    }

                    override fun close() {
                        mySheeting.hide()
                    }

                    override fun onOptionSelected(arg: Any?) {
                        mySheeting.hide()
                        summaryMode.value = SummaryMode.values()[arg as? Int?:return]
                    }
                }
            ),
            Sheets.TimeFilter to TimeFilterBottomSheetModel(
                object : TimeFilterBottomSheetModel.Callback{
                    override fun scope(): CoroutineScope {
                        return viewModelScope
                    }

                    override fun close() {
                        mySheeting.hide()
                    }

                    override fun onOptionSelected(arg: Any?) {
                        val index = arg as? Int?:return
                        when(index){
                            0->{
                                mySheeting.hide()
                                _filterTimeFrame.value = TimeOptionData.Normal("All Time")
                            }
                            1->{
                                mySheeting.hide()
                                _filterTimeFrame.value = TimeOptionData.Outlined(lastMonthShort)
                            }
                            2->{
                                mySheeting.hide()
                                _filterTimeFrame.value = TimeOptionData.Outlined(thisMonthShort)
                            }
                            3->{
                                mySheeting.change(Sheets.AdvancedDatePicker)
                            }
                        }

                    }

                }
            ),
            Sheets.AdvancedDatePicker to DatePickerAdvancedBottomSheetModel(
                object : DatePickerAdvancedBottomSheetModel.Callback{
                    override fun scope(): CoroutineScope {
                        return viewModelScope
                    }

                    override fun close() {
                        mySheeting.hide()
                    }

                    override fun onContinue(result: DatePickerAdvancedBottomSheetModel.Result) {
                        mySheeting.hide()
                        _filterTimeFrame.value = TimeOptionData.Normal(result.display())
                    }
                }
            )
        )
    )
    val lastMonthShort: String
    get(){
        val thisMonth = Calendar.getInstance().get(Calendar.MONTH) + 1
        val lastMonth = if(thisMonth==1) 12 else thisMonth - 1
        return when(lastMonth){
            1-> "Jan"
            2-> "Feb"
            3-> "Mar"
            4-> "Apr"
            5-> "May"
            6-> "Jun"
            7-> "Jul"
            8-> "Aug"
            9-> "Sep"
            10-> "Oct"
            11-> "Nov"
            12-> "Dec"
            else-> ""
        }
    }
    val thisMonthShort: String
    get(){
        val thisMonth = Calendar.getInstance().get(Calendar.MONTH) + 1
        return when(thisMonth){
            1-> "Jan"
            2-> "Feb"
            3-> "Mar"
            4-> "Apr"
            5-> "May"
            6-> "Jun"
            7-> "Jul"
            8-> "Aug"
            9-> "Sep"
            10-> "Oct"
            11-> "Nov"
            12-> "Dec"
            else-> ""
        }
    }
    private fun setUpResolver() {
        _resolver.addAll(
            DataIds.statusBarColor to _statusBarColor,
            DataIds.members to _members,
            DataIds.willGetTransactions to _willGetTransactions,
            DataIds.willPayTransactions to _willPayTransactions,
            DataIds.payTotal to _payTotal,
            DataIds.getTotal to _getTotal,
            DataIds.payeeName to _payeeName,
            DataIds.payerName to _payerName,
            DataIds.selectedBalanceExpenseTab to _selectedBalanceExpenseTab,

            DataIds.expensesCategories to _expensesCategories,
            DataIds.expenseBarChartList to _expenseChartList,
            DataIds.expensePieChartList to _expensePieChartList,
            DataIds.expensesOvertimeTotal to _expensesOvertimeTotal,
            DataIds.expensesTimeFrameText to _expensesTimeFrameText,
            DataIds.expensesOvertimeTotalTransactions to _expensesOvertimeTotalTransactions,
            DataIds.expenseTimeFrame to _expenseTimeFrame,
            DataIds.filterTimeFrame to _filterTimeFrame,

            DataIds.timeFilterItems to _timeFilterItems,

            DataIds.monthlyOrWeekly to _datePickerOption,
            DataIds.datePickerDateRange to _datePickerDateRange,
            DataIds.datePickerFromTo to _datePickerFromTo,

            DataIds.datePickerFromData to _datePickerData1,
            DataIds.datePickerToData to _datePickerToData,
            DataIds.selectedFromYear to _selectedFromYear,
            DataIds.selectedFromMonth to _selectedFromMonth,
            DataIds.selectedFromDay to _selectedFromDay,

            DataIds.numberOfSelectedTimeframe to _numberOfSelectedTimeframe,
            DataIds.selectedDates to _selectedDates,

            DataIds.selectedToYear to _selectedToYear,
            DataIds.selectedToMonth to _selectedToMonth,
            DataIds.selectedToDay to _selectedToDay,
            DataIds.weeklyTab to selectedWeeklyTab,
            DataIds.monthlyTab to selectedMonthlyTab,
            DataIds.dateDisplayData to dateDisplayData,
            DataIds.summaryMode to summaryMode,
        )
    }

    private fun populateMembers() {
        viewModelScope.launch(Dispatchers.IO) {
            val members = summaryRepo.getMembers()
            val willGetTransactions = summaryRepo.getWillGetTransactions()
            val willPayTransactions = summaryRepo.getWillPayTransactions()
            withContext(Dispatchers.Main) {
                _members.addAll(
                    members
                )
                _willGetTransactions.addAll(
                    willGetTransactions
                )
                _willPayTransactions.addAll(
                    willPayTransactions
                )
            }
        }
    }

    private fun populateExpenses() {
        viewModelScope.launch(Dispatchers.IO) {
            val expenses = expenseRepository.getCategoryExpense()
            val barChartData = expenseRepository.getBarChartData()
            val pieChartData = expenseRepository.getPieChartData()
            val datePickerData = expenseRepository.getDatePickerData()
            withContext(Dispatchers.Main) {
                _expensesCategories.addAll(
                    expenses
                )
                _expenseChartList.addAll(
                    barChartData
                )
                _expensePieChartList.addAll(
                    pieChartData
                )
                _datePickerToData.value = datePickerData
            }
        }
    }

    private fun calculateExpensesTotal() {
        for (i in 0 until _expensesCategories.size) {
            _expensesOvertimeTotal.value += _expensesCategories[i].amount

            val portion = (_expensesCategories[i].amount / _expensesOvertimeTotal.value)
            _expensePieChartList[i] = _expensePieChartList[i].copy(portion = portion)
        }
    }

    private fun onSelectMemberClick(arg: Any?) {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                for (i in 0 until _members.size) {
                    _members[i] =
                        _members[i].copy(isSelected = false)
                }
                val index = _members.indexOf(arg)
                if (index > -1) {
                    _members[index] =
                        _members[index].copy(isSelected = !_members[index].isSelected)
                }
            }
        }
    }

    private fun onSelectTabClick(arg: Any?) {
        _selectedBalanceExpenseTab.value = arg as Int
    }

    private fun setUpStatusBarColor() {
        _statusBarColor.value = StatusBarColor(
            color = StatusBarGreen,
            darkIcons = true
        )
    }

    private fun populateTimeFilterItems() {
        viewModelScope.launch(Dispatchers.IO) {
            val items = timeFilterRepo.getItems()
            withContext(Dispatchers.Main) {
                _timeFilterItems.addAll(
                    items
                )
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    private fun selectItem(index: Any?) {
        val ind = index as? Int ?: return
        viewModelScope.launch(Dispatchers.IO) {
            for (i in 0 until _timeFilterItems.size) {
                _timeFilterItems[i] =
                    _timeFilterItems[i].copy(isSelected = false)
            }
            if (ind > -1) {
                _timeFilterItems[ind] =
                    _timeFilterItems[ind].copy(isSelected = true)
            }
        }
        if (ind == 3) {
            /*_sheets.value = Sheets.ExpenseDatePickerSheet
            sheetHandler.state {
                show()
            }*/
        }
    }

    init {
        setUpResolver()
        populateMembers()
        populateExpenses()
        populateTimeFilterItems()
        calculateExpensesTotal()
    }
}

fun <K,V>Map<K,V>.getOrMyDefault(key: K, defualt: V): V {
    return this[key] ?: defualt
}

val currentYear: Int
get(){
    return Calendar.getInstance().get(Calendar.YEAR)
}