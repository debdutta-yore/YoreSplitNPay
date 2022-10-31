package co.yore.splitnpay.components.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import co.yore.splitnpay.R
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.libs.jerokit.dep
import co.yore.splitnpay.libs.jerokit.sep
import co.yore.splitnpay.libs.locals.localCurrency
import co.yore.splitnpay.models.MemberTransact
import co.yore.splitnpay.models.SingleItemYouWillGetCardConfiguration
import co.yore.splitnpay.models.YouWillGetCardConfiguration
import co.yore.splitnpay.ui.theme.*
import java.text.DecimalFormat

@Composable
fun YouWillGetCard(
    payerName: String,
    total: Float,
    list: List<MemberTransact>,
    contentDescription: String,
    config: YouWillGetCardConfiguration = YouWillGetCardConfiguration()
) {
    Box(
        modifier = Modifier
            .semantics { this.contentDescription = contentDescription }
            .coloredShadow(
                color = config.shadowColor,
                borderRadius = config.shadowBorderRadius.dep(),
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
        if (list.isNotEmpty()) {
            Column() {
                FontFamilyText(
                    modifier = Modifier.padding(
                        start = 33.dep(),
                        top = 26.dep()
                    ),
                    text = stringResource(R.string.will_get, payerName),
                    color = CloudBurst,
                    fontSize = 16.sep(),
                    fontWeight = FontWeight.Bold
                )
//        config.gapBetweenTextAndLazyColumn.sy()
                Column(
                    modifier = Modifier
                        .padding(
                            top = config.lazyColumnTopPadding.dep(),
                            start = config.lazyColumnStartPadding.dep(),
                            end = config.lazyColumnEndPadding.dep()
                        )
                        .fillMaxWidth()
                ) {
                    list.forEachIndexed { index, transaction ->
                        SingleItemYouWillGetCard_3btamv1(
                            transaction = transaction,
                            contentDescription = "SingleItem"
                        )
                        if (index != list.lastIndex) {
                            18.sy()
                        }
                    }
                }
                config.gapBetweenLazyColumnAndTotalCard.sy()
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 38.dep(),
                            end = 30.dep()
                        )
                ) {
                    TotalCard_6re10h(
                        contentDescription = "TotalYouWillGetCard",
                        text = stringResource(R.string.total) + " " + stringResource(R.string.will_get, payerName),
                        amount = total,
                        borderColor = Turquoise1,
                        backgroundColor = Dew1
                    )
                }
                config.bottomPadding.sy()
            }
        } else {
            Column() {
                FontFamilyText(
                    modifier = Modifier.padding(
                        start = 33.dep(),
                        top = 26.dep()
                    ),
                    text = stringResource(R.string.will_get, payerName),
                    color = CloudBurst,
                    fontSize = 16.sep(),
                    fontWeight = FontWeight.Bold
                )
                Column(
                    modifier = Modifier.padding(
                        top = config.lazyColumnTopPadding.dep(),
                        bottom = config.bottomPadding.dep(),
                        start = config.lazyColumnStartPadding.dep(),
                        end = config.lazyColumnEndPadding.dep()
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    FontFamilyText(
                        text = stringResource(R.string.will_receive_nothing, payerName),
                        color = WildBlueYonder,
                        fontSize = 13.sep(),
                        fontWeight = FontWeight.Bold
                    )
                    5.sy()
                    FontFamilyText(
                        text = stringResource(R.string.nobody_indebted),
                        color = WildBlueYonder,
                        fontSize = 13.sep()
                    )
                    85.sy()
                    // config.gapBetweenIconAndTotalCard.sy()
                    TotalCard_6re10h(
                        contentDescription = "TotalYouWillPayCard",
                        text = stringResource(R.string.total_receivable),
                        amount = total,
                        borderColor = Turquoise1,
                        backgroundColor = Dew1
                    )
                }
            }
            Icon(
                modifier = Modifier
                    .size(121.dep())
                    .padding(bottom = 0.dep())
                    .align(Alignment.Center),
                painter = painterResource(id = R.drawable.willgetimage),
                tint = Color.Unspecified,
                contentDescription = "handIcon"
            )
        }
    }
}

@Composable
fun SingleItemYouWillGetCard_3btamv1(
    transaction: MemberTransact,
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
                image = transaction.image,
                contentDescription = ""
            )
            config.gapBetweenProfileImageAndUserName.sx()
            Column(modifier = Modifier) {
                FontFamilyText(
                    text = transaction.name,
                    fontSize = 12.sep(),
                    fontWeight = FontWeight.Bold,
                    color = CloudBurst
                )
                config.gapBetweenPUserNameAndUserPhNo.sx()
                FontFamilyText(
                    text = transaction.mobile,
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
        val wholeNumber =
            transaction.amount.splitted().whole
        // transaction.amount.toString().substringBefore(".").padStart(2, '0')
        val decimalNumber =
            transaction.amount.splitted().decString
        // transaction.amount.toString().substringAfter(".").padStart(2, '0')

        val formattedWholeNoDo = wholeNumber.toLong()
        val formatter = DecimalFormat("#,###")
        val formattedWholeNo = formattedWholeNoDo.formatDecimalSeparator()

        // TODO: Fix this
        Box(
            modifier = Modifier,
//                .width(60.dep())
            contentAlignment = CenterStart
        ) {
            FontFamilyText(
                textAlign = TextAlign.Left,
                maxLines = 1,
                annotatedString = buildAnnotatedString {
                    withStyle(currencyStyle) {
                        append("${localCurrency.current} ")
                    }
                    withStyle(currencyScriptSmall) {
                        append(formattedWholeNo.toString())
                    }
                    withStyle(decimalScriptSmall) {
                        append(".")
                        append(decimalNumber)
                    }
                },
                fontSize = 14f.sep(),
                color = CloudBurst,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
