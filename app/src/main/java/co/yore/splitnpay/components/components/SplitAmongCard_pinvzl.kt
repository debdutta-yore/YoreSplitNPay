package co.yore.splitnpay.components.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import co.yore.splitnpay.R
import co.yore.splitnpay.libs.dep
import co.yore.splitnpay.libs.listState
import co.yore.splitnpay.libs.sep
import co.yore.splitnpay.libs.sy
import co.yore.splitnpay.models.DataIds
import co.yore.splitnpay.models.MemberTransact
import co.yore.splitnpay.models.PaidByCardConfiguration

@Composable
fun SplitAmongCard_pinvzl(
    contentDescription: String,
    splitAmongMembers: List<MemberTransact> = listState(key = DataIds.splitAmongMembers),
    config: PaidByCardConfiguration = PaidByCardConfiguration(
        PaidByCardPaidByText = R.string.SplitAmongCardPaidByText
    )
) {

    Box(
        modifier = Modifier
            .semantics { this.contentDescription = contentDescription }
            .padding(horizontal = 19.dep())
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
    )
    {

        Column(
            modifier = Modifier
                .padding(
                    start = config.startPadding.dep(),
                    end = config.endPadding.dep(),
                    top = config.topPadding.dep(),
                    bottom = config.bottomPadding.dep()
                )
                .fillMaxWidth()
        ) {

            FontFamilyText(
                text = stringResource(config.PaidByCardPaidByText),
                color = config.paidByTextColor,
                fontSize = config.paidByTextFontSize.sep(),
                fontWeight = FontWeight.Bold
            )

            config.paidByTextBottomSpace.sy()

            Column(modifier = Modifier.fillMaxWidth()) {

                splitAmongMembers.forEachIndexed { index, item ->
                    SingleItemYouWillGetCard_3btamv(
                        item,
                        contentDescription = "SingleSplitAmongListItem"
                    )
                    if (index != splitAmongMembers.lastIndex) {
                        config.spaceBetweenTwoRowItem.sy()
                    }
                }
            }
        }
    }
}
