package co.yore.splitnpay.components.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.libs.jerokit.dep
import co.yore.splitnpay.libs.jerokit.listState
import co.yore.splitnpay.libs.jerokit.sep
import co.yore.splitnpay.libs.locals.localCurrency
import co.yore.splitnpay.models.*
import co.yore.splitnpay.ui.theme.CloudBurst
import co.yore.splitnpay.ui.theme.SteelBlue

@Composable
fun PaidByCard_ut150e(
    modifier: Modifier = Modifier,
    paidList: List<MemberTransact> = listState(key = DataIds.paidList),
    contentDescription: String,
    config: PaidByCardConfiguration = PaidByCardConfiguration()
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

                paidList.forEachIndexed { index, item ->
                    SingleItemYouWillGetCard_3btamv(
                        item = item,
                        contentDescription = "SinglePaidByListItem"
                    )
                    if (index != paidList.lastIndex) {
                        config.spaceBetweenTwoRowItem.sy()
                    }
                }
            }
        }
    }
}

@Composable
fun SingleItemYouWillGetCard_3btamv(
    item: MemberTransact,
    contentDescription: String,
    config: SingleItemYouWillGetCardConfiguration = SingleItemYouWillGetCardConfiguration()
) {
    Row(
        modifier = Modifier
            .semantics { this.contentDescription = contentDescription }
            .fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.Top) {
            ProfileImage_2hf7q0(
                image = item.image,
                contentDescription = ""
            )
            config.gapBetweenProfileImageAndUserName.sx()
            Column(modifier = Modifier) {
                FontFamilyText(
                    text = item.name,
                    fontSize = 12.sep(),
                    fontWeight = FontWeight.Bold,
                    color = CloudBurst
                )
                config.gapBetweenPUserNameAndUserPhNo.sx()
                FontFamilyText(
                    text = item.mobile,
                    fontSize = 11.sep(),
                    color = SteelBlue
                )
            }
        }

        val currencyStyle = SpanStyle(
            baselineShift = BaselineShift.None,
            fontSize = 12.sep(),
            fontWeight = FontWeight.Normal
        )

        val currencyScriptSmall = SpanStyle(
            baselineShift = BaselineShift.None,
            fontSize = 14.sep(),
            fontWeight = FontWeight.Bold
        )

        val decimalScriptSmall = SpanStyle(
            baselineShift = BaselineShift.None,
            fontSize = 10.sep(),
            fontWeight = FontWeight.Normal
        )
        val wholeNumber = remember(item.amount){
            derivedStateOf {
                item.amount.splitted().wholeString
            }
        }
        val decimalNumber = remember(item.amount){
            derivedStateOf {
                item.amount.splitted().decString
            }
        }
        Box(
            modifier = Modifier,
            contentAlignment = Alignment.CenterStart
        ) {
            FontFamilyText(
                textAlign = TextAlign.Left,
                maxLines = 1,
                annotatedString = buildAnnotatedString {
                    withStyle(currencyStyle) {
                        append("${localCurrency.current} ")
                    }
                    withStyle(currencyScriptSmall) {
                        append(wholeNumber.value)
                    }
                    withStyle(decimalScriptSmall) {
                        append(".")
                        append(decimalNumber.value)
                    }
                },
                fontSize = 14f.sep(),
                color = CloudBurst,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


