package co.yore.splitnpay.pages.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import co.yore.splitnpay.R
import co.yore.splitnpay.components.components.YouWillGetCard
import co.yore.splitnpay.components.components.YouWillPayCard
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.libs.jerokit.*
import co.yore.splitnpay.libs.jerokit.bottom_sheet.Sheeting
import co.yore.splitnpay.libs.jerokit.bottom_sheet.sheetContent
import co.yore.splitnpay.libs.jerokit.bottom_sheet.sheeting
import co.yore.splitnpay.models.DataIds
import co.yore.splitnpay.models.MemberTransact
import co.yore.splitnpay.models.SplitSelectableMember
import co.yore.splitnpay.pages.BalanceExpenseTabs
import co.yore.splitnpay.pages.TopBarWithIcon_1t9xbo
import co.yore.splitnpay.pages.childpages.ExpenseDemo
import co.yore.splitnpay.ui.theme.CloudBurst8C

@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Composable
fun GroupSplitSummaryScreen(
    members: List<SplitSelectableMember> = listState(key = DataIds.members),
    willGetTransactions: List<MemberTransact> = listState(key = DataIds.willGetTransactions),
    willPayTransactions: List<MemberTransact> = listState(key = DataIds.willPayTransactions),
    getTotal: Float = floatState(key = DataIds.getTotal).value,
    payTotal: Float = floatState(key = DataIds.payTotal).value,
    payeeName: String = stringState(key = DataIds.payeeName).value,
    payerName: String = stringState(key = DataIds.payerName).value,
    selectedBalanceExpenseTab: Int = intState(key = DataIds.selectedBalanceExpenseTab).value,
    notifier: NotificationService = notifier(),
    sheeting: Sheeting = sheeting()
) {
    val sheetState = sheeting.sheetHandler.handle()
    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            sheeting.sheetContent()
        },
        sheetShape = RoundedCornerShape(
            topStart = 25f.dep(),
            topEnd = 25f.dep()
        ),
        scrimColor = CloudBurst8C
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 54.dep())
                    .fillMaxSize()
            ) {
                BalanceExpenseTabs(
                    selectedBalanceExpenseTab,
                    isExpenseTabVisible = true
                )
            }

            Box(
                modifier = Modifier
                    .padding(top = 119.dep())
            ) {
                AnimatedContent(
                    targetState = selectedBalanceExpenseTab,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(500)) with
                            fadeOut(animationSpec = tween(500))
                    }
                ) {
                    if (it == 0) {
                        Column(
                            modifier = Modifier
                                .fadingEdge()
                                .verticalScroll(rememberScrollState())
                        ) {
                            12.sy()
                            // SummarySelectableRow(members)
                            LazyRow(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(25.dep()),
                                contentPadding = PaddingValues(
                                    start = 26.dep(),
                                    end = 26.dep()
                                )
                            ) {
                                itemsIndexed(members) { index, it ->
                                    co.yore.splitnpay.pages.SummarySinglePeople_q6c90m(
                                        splitSelectableMember = it,
                                        contentDescription = "SummaryEachUser"
                                    )
                                }
                            }
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
                    } else if (it == 1) {
                        ExpenseDemo()
                    }
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
}
