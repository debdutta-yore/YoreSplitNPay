package co.yore.splitnpay.components.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.locals.localCurrency
import co.yore.splitnpay.models.DataIds
import co.yore.splitnpay.models.TransactionType
import co.yore.splitnpay.pages.Transaction
import co.yore.splitnpay.pages.splitted
import co.yore.splitnpay.ui.theme.DarkBlue
import co.yore.splitnpay.ui.theme.GreyShadow
import java.text.DecimalFormat

private val paidByList = listOf(
    PaidBySingleItem(
        name = "You",
        imageUrl = "imageUrl",
        mobileNumber = "9563376942",
        amount = 5000f
    ),
    PaidBySingleItem(
        name = "Manisha Roy",
        imageUrl = "imageUrl",
        mobileNumber = "9563376942",
        amount = 5000f
    )
)

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
private val paidList = listOf<Transaction>(
    Transaction(
        name = "You",
        mobileNumber = "9563376942",
        amount = 5000f,
        transactionType = TransactionType.Paid,
        imageUrl = "https://i.pravatar.cc/300?"
    ),
    Transaction(
        name = "Manisha Roy",
        mobileNumber = "9563376942",
        amount = 5000f,
        transactionType = TransactionType.Paid,
        imageUrl = "https://i.pravatar.cc/300?"
    ),
    Transaction(
        name = "Sushil Roy",
        mobileNumber = "9563376942",
        amount = 0f,
        transactionType = TransactionType.Unspecified,
        imageUrl = "https://i.pravatar.cc/300?"
    ),
    Transaction(
        name = "Sanjanaa Ray",
        mobileNumber = "9563376942",
        amount = 0f,
        transactionType = TransactionType.Unspecified,
        imageUrl = "https://i.pravatar.cc/300?"
    )
)
data class PaidBySingleItem(
    val name: String,
    val imageUrl: String,
    val mobileNumber: String,
    val amount: Float
)

data class PaidByCardConfiguration(
    val shadowColor: Color = GreyShadow,
    val shadowBorderRadius: Float = 15f,
    val shadowBlurRadius: Float = 33f,
    val shadowOffsetX: Float = 7f,
    val shadowOffsetY: Float = 7f,
    val shadowSpread: Float = 0f,
    val cornerRadius: Float = 15f,
    val backgroundColor: Color = Color.White,
    val startPadding: Float = 33f,
    val endPadding: Float = 30f,
    val topPadding: Float = 26f,
    val bottomPadding: Float = 29f,

    val PaidByCardPaidByText: Int = R.string.PaidByCardPaidByText,
    val paidByTextColor: Color = DarkBlue,
    val paidByTextFontSize: Float = 16f,
    val paidByTextBottomSpace: Float = 16f,

    val spaceBetweenTwoRowItem: Float = 18f
)

// ///////////////////
data class MemberTransact(
    val name: String,
    val mobile: String,
    val image: Any?,
    val amount: Float,
)
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
                imageUrl = item.image,
                contentDescription = "",
                config = ProfileImageConfiguration()
            )
            config.gapBetweenProfileImageAndUserName.sx()
            Column(modifier = Modifier) {
                FontFamilyText(
                    text = item.name,
                    fontSize = 12.sep(),
                    fontWeight = FontWeight.Bold,
                    color = DarkBlue
                )
                config.gapBetweenPUserNameAndUserPhNo.sx()
                FontFamilyText(
                    text = item.mobile,
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
            item.amount.splitted().whole
        // transaction.amount.toString().substringBefore(".").padStart(2, '0')
        val decimalNumber =
            item.amount.splitted().dec
        // transaction.amount.toString().substringAfter(".").padStart(2, '0')

        val formattedWholeNoDo = wholeNumber.toLong()
        val formatter = DecimalFormat("#,###")
        val formattedWholeNo = formattedWholeNoDo.formatDecimalSeparator()

        // TODO: Fix this
        Box(
            modifier = Modifier,
//                .width(60.dep())
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

fun Number.formatDecimalSeparator(): String {
    return toString()
        .reversed()
        .chunked(3)
        .joinToString(",")
        .reversed()
}
data class SingleItemYouWillGetCardConfiguration(
    val gapBetweenProfileImageAndUserName: Float = 22f,
    val gapBetweenPUserNameAndUserPhNo: Float = 5f,
    val gapBetweenUserNameAndAmount: Float = 40f
)
