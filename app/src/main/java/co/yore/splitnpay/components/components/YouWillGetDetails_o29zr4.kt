package co.yore.splitnpay.components.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import co.yore.splitnpay.R
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.models.*
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
    config: YouWillGetDetailsConfiguration = YouWillGetDetailsConfiguration(),
    selectedGetOption: TransactionStatus1 = tState<TransactionStatus1>(key = DataIds.transactionStatus).value,
    notifier: NotificationService = notifier()
) {


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
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                /*FontFamilyText(
                    text = stringResource(config.memberWillGetText, memberName),
                    color = config.youWillGetTextFontColor,
                    fontSize = config.youWillGetTextFontSize.sep(),
                    fontWeight = FontWeight.Bold
                )*/

                TextAnimation(
                    text = stringResource(config.memberWillGetText, memberName),
                    color = config.youWillGetTextFontColor,
                    fontSize = config.youWillGetTextFontSize.sep(),
                    fontWeight = FontWeight.Bold,
                    animationSpec = tween(700)
                )

                5.5f.sx()

                YouWillGetDetailsTabRow(
                    status = selectedGetOption,
                    contentDescription = "YouWillGetDetailsTabRow",
                )
                {
                    notifier.notify(DataIds.transactionStatus,it)
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
    status: TransactionStatus1,
    onClick: (TransactionStatus1) -> Unit
) {
    val length = with(LocalDensity.current){5.5.dep().toPx()}
    LazyRow(
        modifier = Modifier
            .semantics { this.contentDescription = contentDescription }
            .fillMaxWidth()
            .fadingEdge(horizontal = true, length = length),
        horizontalArrangement = Arrangement.spacedBy(5.dep()),
        contentPadding = PaddingValues(horizontal = 5.5.dep())
    ) {
        item{
            YouWillGetDetailsSingleTabItem_7ozj5w(
                item = stringResource(config.YouWillGetDetailsTabRowPendingItem),
                contentDescription = "PendingChip",
                selected = status == TransactionStatus1.Pending,
                onClick = { onClick(TransactionStatus1.Pending) }
            )
        }
        item{
            YouWillGetDetailsSingleTabItem_7ozj5w(
                item = stringResource(config.YouWillGetDetailsTabRowPartiallyReceivedItem),
                contentDescription = "PartiallyReceivedChip",
                selected = status == TransactionStatus1.PartiallyReceived,
                onClick = { onClick(TransactionStatus1.PartiallyReceived) }
            )
        }
        item{
            YouWillGetDetailsSingleTabItem_7ozj5w(
                item = stringResource(config.YouWillGetDetailsTabRowSettledItem),
                contentDescription = "SettledChip",
                selected = status == TransactionStatus1.Settled,
                onClick = { onClick(TransactionStatus1.Settled) }
            )
        }
    }
}






