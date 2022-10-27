package co.yore.splitnpay.libs

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
import co.yore.splitnpay.addmembers.FontFamilyText
import co.yore.splitnpay.components.components.*
import co.yore.splitnpay.components.components.coloredShadow
import co.yore.splitnpay.demos.expenseDemo.sx
import co.yore.splitnpay.demos.expenseDemo.sy
import co.yore.splitnpay.locals.localCurrency
import co.yore.splitnpay.pages.WhitishGreen
import co.yore.splitnpay.ui.theme.DarkBlue
import co.yore.splitnpay.ui.theme.Greyish2
import co.yore.splitnpay.ui.theme.LightBlue4
import co.yore.splitnpay.ui.theme.LightGreen3
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
                    color = DarkBlue,
                    fontSize = 16.sep(),
                    fontWeight = FontWeight.Bold
                )
//        config.gapBetweenTextAndLazyColumn.sy()
                Column(
                    modifier = Modifier
                        .padding(
                            top = config.lazyColumnTopPadding.dep(),
                            start = config.lazyColumnStartPadding.dep(),
                            end = config.lazyColumnEndPadding.dep(),
                        )
                        .fillMaxWidth()
                ) {
                    list.forEachIndexed { index, transaction ->
                        SingleItemYouWillGetCard_3btamv(
                            transaction = transaction,
                            contentDescription = "SingleItem"
                        )
                        if (index != list.lastIndex)
                            18.sy()
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
                        text = stringResource(R.string.total) + " " +stringResource(R.string.will_get, payerName),
                        amount = total,
                        borderColor = LightGreen3,
                        backgroundColor = WhitishGreen
                    )
                }
                config.bottomPadding.sy()
            }
        }
        else {
            Column() {
                FontFamilyText(
                    modifier = Modifier.padding(
                        start = 33.dep(),
                        top = 26.dep()
                    ),
                    text = stringResource(R.string.will_get, payerName),
                    color = DarkBlue,
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
                        color = LightBlue4,
                        fontSize = 13.sep(),
                        fontWeight = FontWeight.Bold
                    )
                    5.sy()
                    FontFamilyText(
                        text = stringResource(R.string.nobody_indebted),
                        color = LightBlue4,
                        fontSize = 13.sep()
                    )
                    85.sy()
                    // config.gapBetweenIconAndTotalCard.sy()
                    TotalCard_6re10h(
                        contentDescription = "TotalYouWillPayCard",
                        text = stringResource(R.string.total_receivable),
                        amount = total,
                        borderColor = LightGreen3,
                        backgroundColor = WhitishGreen
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
fun SingleItemYouWillGetCard_3btamv(
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
                imageUrl = transaction.image,
                contentDescription = "",
                config = ProfileImageConfiguration()
            )
            config.gapBetweenProfileImageAndUserName.sx()
            Column(modifier = Modifier) {
                FontFamilyText(
                    text = transaction.name,
                    fontSize = 12.sep(),
                    fontWeight = FontWeight.Bold,
                    color = DarkBlue
                )
                config.gapBetweenPUserNameAndUserPhNo.sx()
                FontFamilyText(
                    text = transaction.mobile,
                    fontSize = 11.sep(),
                    color = Color(0xff5A87BB)
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
            //transaction.amount.toString().substringBefore(".").padStart(2, '0')
        val decimalNumber =
            transaction.amount.splitted().decString
            //transaction.amount.toString().substringAfter(".").padStart(2, '0')

        val formattedWholeNoDo = wholeNumber.toLong()
        val formatter = DecimalFormat("#,###")
        val formattedWholeNo = formattedWholeNoDo.formatDecimalSeparator()

        //TODO: Fix this
        Box(
            modifier = Modifier
//                .width(60.dep())
            ,
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
                color = DarkBlue,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


data class SingleItemYouWillGetCardConfiguration(
    val gapBetweenProfileImageAndUserName: Float = 22f,
    val gapBetweenPUserNameAndUserPhNo: Float = 5f,
    val gapBetweenUserNameAndAmount: Float = 40f
)


data class YouWillGetCardConfiguration(
    val shadowColor: Color = Greyish2,
    val shadowBorderRadius: Float = 15f,
    val shadowBlurRadius: Float = 33f,
    val shadowOffsetX: Float = 5.63f,
    val shadowOffsetY: Float = 7f,
    val shadowSpread: Float = 0f,
    val cornerRadius: Float = 15f,
    val backgroundColor: Color = Color.White,
    val gapBetweenTextAndLazyColumn: Float = 19f,
    val gapBetweenLazyColumnAndTotalCard: Float = 34f,
    val bottomPadding: Float = 27f,
    val lazyColumnStartPadding: Float = 32f,
    val lazyColumnTopPadding: Float = 30f,
    val lazyColumnEndPadding: Float = 30f
)