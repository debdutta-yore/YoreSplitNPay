package co.yore.splitnpay.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import co.yore.splitnpay.R
import co.yore.splitnpay.components.components.YoreDatePicker
import co.yore.splitnpay.components.components.YoreDatePickerData
import co.yore.splitnpay.demos.sx
import co.yore.splitnpay.demos.sy
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.locals.RobotoText
import co.yore.splitnpay.models.DataIds

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
                .background(LightBlue5)
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
                    color = Color(0xff243257)
                )
            }
            7.sy()
            RobotoText(
                text = stringResource(id = R.string.expense_date_instruction),
                color = Color(0xff8498AB),
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

        }
        25.sy()
        Row(
            modifier = Modifier
                .padding(horizontal = 31.dep())
                .fillMaxWidth()
                .height(46.dep())
                .clip(RoundedCornerShape(5.dep()))
                .background(Color(0xffF4F7FB))
                .padding(horizontal = 19.dep()),
            verticalAlignment = Alignment.CenterVertically
        ){
            RobotoText(
                text = stringResource(id = R.string.dates_colon_space),
                color = Color(0xff677C91),
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
                    notifier.notify(DataIds.billTotalContinueClick)
                },
                contentDescription = "Continue button",
                enabled = canProceed
            )
        }
        20.sy()
    }

}