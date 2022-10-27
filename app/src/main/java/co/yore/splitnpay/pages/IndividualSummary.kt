package co.yore.splitnpay.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import co.yore.splitnpay.R
import co.yore.splitnpay.components.components.MemberTransact
import co.yore.splitnpay.components.components.YouWillPayCard
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.models.DataIds

@Composable
fun IndividualSummary(
    members: List<Transaction> = listState(key = DataIds.members),
    willGetTransactions: List<MemberTransact> = listState(key = DataIds.willGetTransactions),
    willPayTransactions: List<MemberTransact> = listState(key = DataIds.willPayTransactions),
    getTotal: Float = floatState(key = DataIds.getTotal).value,
    payTotal: Float = floatState(key = DataIds.payTotal).value,
    payeeName: String = stringState(key = DataIds.payeeName).value,
    payerName: String = stringState(key = DataIds.payerName).value,
    selectedBalanceExpenseTab: Int = intState(key = DataIds.selectedBalanceExpenseTab).value,
    notifier: NotificationService = notifier()
){
//    val selected = remember { mutableStateOf(0) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(top = 54.dep())
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            BalanceExpenseTabs(
                selectedBalanceExpenseTab,
                isExpenseTabVisible = false
            )

            if (selectedBalanceExpenseTab == 0) {
                Column {
                    31.sy()
                    SplitSelectableMembers()
                    48.sy()
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dep())
                    ) {
                        YouWillGetCard(
                            payerName = payerName,
                            total = getTotal,
                            list = willGetTransactions,
                            contentDescription = ""
                        )
                    }
                    33.sy()
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dep())
                    ) {
                        YouWillPayCard(
                            payeeName = payeeName,
                            total = payTotal,
                            list = willPayTransactions,
                            contentDescription = ""
                        )
                    }
                    56.sy()
                }
            }
            else if(selectedBalanceExpenseTab == 1) {

            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dep()),
            contentAlignment = Alignment.Center
        ) {
            TopBarWithIcon_1t9xbo(
                onClick = {
                    notifier.notify(DataIds.back)
                },
                text = stringResource(id = R.string.summary)
            )
        }
    }
}