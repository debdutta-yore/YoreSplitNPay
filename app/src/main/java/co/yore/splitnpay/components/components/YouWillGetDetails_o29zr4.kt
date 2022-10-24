package co.yore.splitnpay.components.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import co.yore.splitnpay.R
import co.yore.splitnpay.addmembers.FontFamilyText
import co.yore.splitnpay.demos.sx
import co.yore.splitnpay.demos.sy
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.models.DataIds
import co.yore.splitnpay.ui.theme.DarkBlue
import co.yore.splitnpay.ui.theme.GreyShadow

private val list = listOf(
    MemberWillGetOrPayDetailsSingleRowItem(
        imageUrl = "imageUrl",
        userName = "Sushil Roy",
        userPhNo = "9563376942",
        getAmount = 600f,
        paidAmount = 0f
    ),
    MemberWillGetOrPayDetailsSingleRowItem(
        imageUrl = "imageUrl",
        userName = "Sanjana Ray",
        userPhNo = "9563376942",
        getAmount = 600f,
        paidAmount = 0f
    ),
    MemberWillGetOrPayDetailsSingleRowItem(
        imageUrl = "imageUrl",
        userName = "Ankita Ray",
        userPhNo = "9563376942",
        getAmount = 600f,
        paidAmount = 0f
    )
)

@Composable
fun YouWillGetDetails_o29zr4(
    modifier: Modifier = Modifier,
    memberName: String = stringState(key = DataIds.memberName).value,
    getMembers: List<MemberWillGetOrPayDetailsSingleRowItem> = listState(key = DataIds.getMembers),
    totalGet: Float = floatState(key = DataIds.totalGet).value,
    contentDescription: String,
    config: YouWillGetDetailsConfiguration = YouWillGetDetailsConfiguration()
) {
    var selected by remember { mutableStateOf(TransactionStatus.Pending) }

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
            .clip(
                RoundedCornerShape(config.cornerRadius.dep())
            )
            .background(config.backgroundColor)
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
                    text = stringResource(config.youWillGetDetailsYouWillGetText, memberName),
                    color = config.youWillGetTextFontColor,
                    fontSize = config.youWillGetTextFontSize.sep(),
                    fontWeight = FontWeight.Bold
                )

                config.gapBetweenYouWillGetTextAndTabRow.sx()

                YouWillGetDetailsTabRow(
                    status = selected,
                    contentDescription = "YouWillGetDetailsTabRow"
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
                getMembers.forEachIndexed { index, it ->
                    YouWillGetOrPayDetailsSingleRow_tby58h(
                        contentDescription = "YouWillGetDetailsSingleRow",
                        item = it
                    )
                    if (index != list.lastIndex) {
                        config.gapBetweenTwoRowInLazyColumn.sy()
                    }
                }
            }

            ////////////
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
                    amount = totalGet,
                    amountTrailingText = stringResource(config.totalCardTrailingText)
                )
            }
        }
    }
}

@Composable
fun YouWillGetDetailsTabRow(
    contentDescription: String,
    config: YouWillGetDetailsTabRowConfiguration = YouWillGetDetailsTabRowConfiguration(),
    status: TransactionStatus,
    onClick: (TransactionStatus) -> Unit
) {
    Row(
        modifier = Modifier
            .semantics { this.contentDescription = contentDescription }
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        YouWillGetDetailsSingleTabItem_7ozj5w(
            item = stringResource(config.YouWillGetDetailsTabRowPendingItem),
            contentDescription = "PendingChip",
            selected = status == TransactionStatus.Pending,
            onClick = { onClick(TransactionStatus.Pending) }
        )

        YouWillGetDetailsSingleTabItem_7ozj5w(
            item = stringResource(config.YouWillGetDetailsTabRowPartiallyReceivedItem),
            contentDescription = "PartiallyReceivedChip",
            selected = status == TransactionStatus.PartiallyReceived,
            onClick = { onClick(TransactionStatus.PartiallyReceived) }
        )

        YouWillGetDetailsSingleTabItem_7ozj5w(
            item = stringResource(config.YouWillGetDetailsTabRowSettledItem),
            contentDescription = "SettledChip",
            selected = status == TransactionStatus.Settled,
            onClick = { onClick(TransactionStatus.Settled) }
        )
    }
}

data class MemberWillGetOrPayDetailsSingleRowItem(
    val imageUrl: String,
    val userName: String,
    val userPhNo: String,
    val getAmount: Float,
    val paidAmount: Float
)

enum class TransactionStatus {
    Pending,
    PartiallyReceived,
    Settled,
    AllSettled
}

data class YouWillGetDetailsTabRowConfiguration(
    val YouWillGetDetailsTabRowPendingItem: Int = R.string.YouWillGetDetailsTabRowPendingItem,
    val YouWillGetDetailsTabRowPartiallyReceivedItem: Int = R.string.YouWillGetDetailsTabRowPartiallyReceivedItem,
    val YouWillGetDetailsTabRowSettledItem: Int = R.string.YouWillGetDetailsTabRowSettledItem
)
{
    companion object{
        val variationOne = YouWillGetDetailsTabRowConfiguration(
            YouWillGetDetailsTabRowPartiallyReceivedItem = R.string.YouWillPayDetailsTabRowPartiallyPaidItem
        )
    }

}

data class YouWillGetDetailsConfiguration(
    val shadowColor: Color = GreyShadow,
    val shadowBorderRadius: Float = 15f,
    val shadowBlurRadius: Float = 33f,
    val shadowOffsetX: Float = 7f,
    val shadowOffsetY: Float = 7f,
    val shadowSpread: Float = 0f,
    val cornerRadius: Float = 15f,
    val backgroundColor: Color = Color.White,
    val startPadding: Float = 33f,
    val endPadding: Float = 18f,
    val topPadding: Float = 26f,
    val BottomPadding: Float = 18f,

    val youWillGetDetailsYouWillGetText: Int = R.string.YouWillGetDetailsYouWillGetText,
    val youWillGetTextFontSize: Float = 16f,
    val youWillGetTextFontColor: Color = DarkBlue,

    val gapBetweenYouWillGetTextAndTabRow: Float = 11f,

    val lazyColumnEndPadding: Float = 26f,
    val lazyColumnTopPadding: Float = 19f,
    val lazyColumnHeight: Float = 185f,
    val gapBetweenTwoRowInLazyColumn: Float = 19f,

    val totalCardStartPadding: Float = 5f,
    val totalCardEndPadding: Float = 12f,
    val totalCardTopPadding: Float = 27f,
    val totalCardBottomPadding: Float = 27f,
    val totalCardText: Int = R.string.YouWillGetDetailsTotalYouWillGet,
    val totalCardTrailingText: Int = R.string.YouWillGetDetailsCr
)
{
    companion object {

        val variation1 = YouWillGetDetailsConfiguration(
            lazyColumnHeight = 50f,
            youWillGetDetailsYouWillGetText = R.string.YouWillPayDetailsYouWillPayText,
            totalCardText = R.string.YouWillPayDetailsTotalYouWillPay,
            totalCardTrailingText = R.string.YouWillPayDetailsDr
        )
    }
}
