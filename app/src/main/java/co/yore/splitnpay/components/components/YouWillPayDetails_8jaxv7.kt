package co.yore.splitnpay.components.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import co.yore.splitnpay.addmembers.FontFamilyText
import co.yore.splitnpay.demos.sx
import co.yore.splitnpay.demos.sy
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.models.DataIds

private val list = listOf(
    MemberWillGetOrPayDetailsSingleRowItem(
        imageUrl = "imageUrl",
        userName = "Sushil Roy",
        userPhNo = "9563376942",
        getAmount = 600f,
        paidAmount = 0f
    )
)

@Composable
fun YouWillPayDetails_8jaxv7(
    modifier: Modifier = Modifier,
    memberName: String = stringState(key = DataIds.memberName).value,
    payMembers: List<MemberWillGetOrPayDetailsSingleRowItem> = listState(key = DataIds.payMembers),
    totalPay: Float = floatState(key = DataIds.totalPay).value,
    contentDescription: String,
    config: YouWillGetDetailsConfiguration = YouWillGetDetailsConfiguration.variation1
) {
    var selected by remember { mutableStateOf(YouWillPayTransactionStatus.Pending) }

    Box(
        modifier = modifier
            .semantics { this.contentDescription = contentDescription }
            .coloredShadow(
                color = config.shadowColor,
                borderRadius = config.cornerRadius.dep(),
                blurRadius = config.shadowBlurRadius.dep(),
                offsetX = config.shadowOffsetX.dep(),
                offsetY = config.shadowOffsetY.dep(),
                spread = config.shadowSpread
            )
            .fillMaxWidth()
            .background(
                color = config.backgroundColor,
                shape = RoundedCornerShape(config.cornerRadius.dep())
            )
    ) {
        Column(
            modifier =
            Modifier.padding(
                start = config.startPadding.dep(),
                end = config.endPadding.dep(),
                top = config.topPadding.dep(),
                bottom = config.BottomPadding.dep()
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                FontFamilyText(
                    text = stringResource(config.youWillGetDetailsYouWillGetText,memberName),
                    color = config.youWillGetTextFontColor,
                    fontSize = config.youWillGetTextFontSize.sep(),
                    fontWeight = FontWeight.Bold
                )

                config.gapBetweenYouWillGetTextAndTabRow.sx()

                YouWillPayDetailsTabRow(
                    status = selected,
                    contentDescription = "YouWillPayDetailsTabRow"
                )
                {
                    selected = it
                }

            }
            Column(
                modifier = Modifier
                    .padding(
                        end = config.lazyColumnEndPadding.dep(),
                        top = config.lazyColumnTopPadding.dep()
                    )
                    .fillMaxWidth()
            ){
                payMembers.forEachIndexed { index, it ->
                    YouWillGetOrPayDetailsSingleRow_tby58h(
                        contentDescription = "YouWillPayDetailsSingleRow",
                        item = it,
                        config = YouWillGetDetailsSingleRowConfiguration.variationOne
                    )
                    if (index != list.lastIndex) {
                        config.gapBetweenTwoRowInLazyColumn.sy()
                    }
                }
            }
            // /////
            Box(
                modifier = Modifier
                    .padding(
                        start = config.totalCardStartPadding.dep(),
                        end = config.totalCardEndPadding.dep(),
                        top = config.totalCardTopPadding.dep(),
                        bottom = config.totalCardBottomPadding.dep()
                    )
            ) {
                TotalCard_6re10h(
                    contentDescription = "TotalCard",
                    text = stringResource(config.totalCardText),
                    amount = totalPay,
                    amountTrailingText = stringResource(config.totalCardTrailingText),
                    config = TotalCardConfiguration.variationOne
                )
            }

        }

    }
}

@Composable
fun YouWillPayDetailsTabRow(
    contentDescription: String,
    config: YouWillGetDetailsTabRowConfiguration = YouWillGetDetailsTabRowConfiguration.variationOne,
    status: YouWillPayTransactionStatus,
    onClick: (YouWillPayTransactionStatus) -> Unit
) {
    Row(
        modifier = Modifier
            .semantics { this.contentDescription = contentDescription }
            .fillMaxWidth(),
        // horizontalArrangement = Arrangement.SpaceBetween,
        horizontalArrangement = Arrangement.spacedBy(5.dep())
    ) {

        YouWillGetDetailsSingleTabItem_7ozj5w(
            item = stringResource(config.YouWillGetDetailsTabRowPendingItem),
            contentDescription = "PendingChip",
            selected = status == YouWillPayTransactionStatus.Pending,
            onClick = { onClick(YouWillPayTransactionStatus.Pending) },
            config = YouWillGetDetailsSingleTabItemConfiguration.variationOne
        )

        YouWillGetDetailsSingleTabItem_7ozj5w(
            item = stringResource(config.YouWillGetDetailsTabRowPartiallyReceivedItem),
            contentDescription = "PartiallyPaidChip",
            selected = status == YouWillPayTransactionStatus.PartiallyPaid,
            onClick = { onClick(YouWillPayTransactionStatus.PartiallyPaid) },
            config = YouWillGetDetailsSingleTabItemConfiguration.variationOne
        )

        YouWillGetDetailsSingleTabItem_7ozj5w(
            item = stringResource(config.YouWillGetDetailsTabRowSettledItem),
            contentDescription = "SettledChip",
            selected = status == YouWillPayTransactionStatus.Settled,
            onClick = { onClick(YouWillPayTransactionStatus.Settled) },
            config = YouWillGetDetailsSingleTabItemConfiguration.variationOne
        )
    }
}

data class YouWillPayDetailsSingleRowItem(
    val imageUrl: String,
    val userName: String,
    val userPhNo: String,
    val dueAmount: Float,
    val paidAmount: Float
)

enum class YouWillPayTransactionStatus {
    Pending,
    PartiallyPaid,
    Settled
}
