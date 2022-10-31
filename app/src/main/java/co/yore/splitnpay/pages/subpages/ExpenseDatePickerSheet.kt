package co.yore.splitnpay.pages.subpages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import co.yore.splitnpay.R
import co.yore.splitnpay.components.components.CustomButton_3egxtx
import co.yore.splitnpay.components.components.YoreDatePicker
import co.yore.splitnpay.components.components.YoreDatePickerData
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.libs.jerokit.*
import co.yore.splitnpay.libs.jerokit.bottom_sheet.BottomSheetModel
import co.yore.splitnpay.libs.locals.RobotoText
import co.yore.splitnpay.models.DataIds
import co.yore.splitnpay.ui.theme.*
import kotlinx.coroutines.*

class ExpenseDatePickerBottomSheetModel(val callback: Callback): BottomSheetModel {
    interface Callback{
        fun scope(): CoroutineScope
        fun getDatePickerData(): YoreDatePickerData
        fun onContinue(
            selectedDay: Int,
            selectedMonth: Int,
            selectedYear: Int
        )

        fun close()
    }
    private val _resolver = Resolver()
    private val _notifier = NotificationService{id,arg->
        when(id){
            DataIds.year->{
                yoreDatePickerData.value = yoreDatePickerData.value.copy(selectedYear = arg as? Int?:return@NotificationService)
                onDateChange()
            }
            DataIds.month->{
                yoreDatePickerData.value = yoreDatePickerData.value.copy(selectedMonth = arg as? Int?:return@NotificationService)
                onDateChange()
            }
            DataIds.day->{
                yoreDatePickerData.value = yoreDatePickerData.value.copy(selectedDay = arg as? Int?:return@NotificationService)
                onDateChange()
            }
            DataIds.expenseDateContinue->{
                callback.onContinue(
                    yoreDatePickerData.value.selectedDay?:0,
                    yoreDatePickerData.value.selectedMonth?:0,
                    yoreDatePickerData.value.selectedYear,
                )
            }
        }
    }
    override val resolver = _resolver
    override val notifier = _notifier
    override val scope get() = callback.scope()
    @Composable
    override fun Content() {
        ExpenseDatePickerSheet()
    }

    override fun initialize() {
        scope.launch(Dispatchers.IO) {
            delay(300)
            withContext(Dispatchers.Main){
                yoreDatePickerData.value = callback.getDatePickerData()
            }
        }
    }

    override fun clear() {

    }

    override fun onBack() {
        callback.close()
    }
    ///////////////
    private val displayDate = mutableStateOf("")
    private val canProceedWithDate = mutableStateOf(false)
    private val yoreDatePickerData = mutableStateOf(YoreDatePickerData())
    init {
        _resolver.addAll(
            DataIds.displayDate to displayDate,
            DataIds.canProceedWithDate to canProceedWithDate,
            DataIds.yoreDatePickerData to yoreDatePickerData,
        )
    }
    /////////////////////
    private fun onDateChange() {
        val y = yoreDatePickerData.value.selectedYear
        val m = yoreDatePickerData.value.selectedMonth
        val d = yoreDatePickerData.value.selectedDay
        yoreDatePickerData.value = yoreDatePickerData.value.copy(
            selectedYear = y,
            selectedMonth = m,
            selectedDay = d
        )
        if(d != null && m != null){
            displayDate.value = displayableDate(d,m,y)
            canProceedWithDate.value = true
        }
        else{
            displayDate.value = ""
            canProceedWithDate.value = false
        }
    }


}

@Composable
fun ExpenseDatePickerSheet(
    yoreDatePickerData: YoreDatePickerData = tState<YoreDatePickerData>(key = DataIds.yoreDatePickerData).value,
    displayDate: String = stringState(key = DataIds.displayDate).value,
    canProceed: Boolean = boolState(key = DataIds.canProceedWithDate).value,
    notifier: NotificationService = notifier()
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ){
        21.sy()
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50.dep()))
                .height(2.dep())
                .width(19.dep())
                .background(SteelBlue)
                .align(Alignment.CenterHorizontally)
        )
        13.sy()
        Column(
            modifier = Modifier
                .padding(horizontal = 43.dep())
                .fillMaxWidth()
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(
                    painter = painterResource(id = R.drawable.date_32),
                    contentDescription = "date_picker",
                    modifier = Modifier.size(16.dep()),
                    tint = Color.Unspecified
                )
                8.sx()
                RobotoText(
                    text = stringResource(id = R.string.select_expense_date),
                    fontSize = 18.sep(),
                    fontWeight = FontWeight.Bold,
                    color = CloudBurst
                )
            }
            7.sy()
            RobotoText(
                text = stringResource(id = R.string.expense_date_instruction),
                color = BaliHai,
                fontSize = 12.sep(),
                modifier = Modifier.padding(start = 24.dep())
            )
        }
        20.sy()
        Divider(
            modifier = Modifier
                .padding(7.dep())
                .fillMaxWidth()
        )
        YoreDatePicker(
            yoreDatePickerData = yoreDatePickerData,
            onYearClick = {
                notifier.notify(DataIds.year,it)
            },
            onMonthClick = {
                notifier.notify(DataIds.month,it)
            },
            onDaySelect = {
                notifier.notify(DataIds.day,it)
            }
        ) {
            24.sy()
        }
        25.sy()
        Row(
            modifier = Modifier
                .padding(horizontal = 31.dep())
                .fillMaxWidth()
                .height(46.dep())
                .clip(RoundedCornerShape(5.dep()))
                .background(BlackSqueeze1)
                .padding(horizontal = 19.dep()),
            verticalAlignment = Alignment.CenterVertically
        ){
            RobotoText(
                text = stringResource(id = R.string.dates_colon_space),
                color = Lynch,
                fontSize = 12.sep()
            )
            RobotoText(
                text = displayDate,
                color = Color.Black,
                fontSize = 12.sep()
            )
        }
        18.sy()
        Box(
            modifier = Modifier
                .padding(horizontal = 30.dep())
                .height(47.dep())
        ) {
            CustomButton_3egxtx(
                text = stringResource(id = R.string.continue1),
                onClick = {
                    notifier.notify(DataIds.expenseDateContinue)
                },
                contentDescription = "Continue button",
                enabled = canProceed
            )
        }
        20.sy()
    }

}