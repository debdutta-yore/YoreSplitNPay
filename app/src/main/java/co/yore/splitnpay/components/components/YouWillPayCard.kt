package co.yore.splitnpay.components.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import co.yore.splitnpay.R
import co.yore.splitnpay.addmembers.FontFamilyText
import co.yore.splitnpay.libs.dep
import co.yore.splitnpay.libs.sep
import co.yore.splitnpay.libs.sy
import co.yore.splitnpay.models.MemberTransact
import co.yore.splitnpay.models.YouWillPayConfiguration1
import co.yore.splitnpay.ui.theme.*

@Composable
fun YouWillPayCard(
    payeeName: String,
    total: Float,
    list: List<MemberTransact>,
    contentDescription: String,
    config: YouWillPayConfiguration1 = YouWillPayConfiguration1()
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
            .clip(RoundedCornerShape(config.cornerRadius.dep()))
            .background(config.backgroundColor)
    ) {
        if (list.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 32.dep())
                    .fillMaxWidth()
            ) {
                FontFamilyText(
                    modifier = Modifier.padding(
                        top = 26.dep()
                    ),
                    text = stringResource(R.string.willpay,payeeName),
                    color = DarkBlue,
                    fontSize = 16.sep(),
                    fontWeight = FontWeight.Bold
                )
                19.sy()
                list.forEachIndexed { index, transaction ->
                    SingleItemYouWillGetCard_3btamv(
                        item = transaction,
                        contentDescription = "SingleItem"
                    )
                    if (index != list.lastIndex)
                        18.sy()
                }
                18.sy()
                TotalCard_6re10h(
                    contentDescription = "TotalYouWillPayCard",
                    text = stringResource(R.string.total)  + " " + stringResource(R.string.willpay,payeeName),
                    amount = 0f,
                    borderColor = Pink,
                    backgroundColor = LightRedButton
                )
                28.sy()
            }
        }
        else {
            Column(){
                FontFamilyText(
                    modifier = Modifier.padding(
                        top = 26.dep(),
                        start = 32.dep()
                    ),
                    text = stringResource(R.string.willpay, payeeName),
                    color = DarkBlue,
                    fontSize = 16.sep(),
                    fontWeight = FontWeight.Bold
                )
                Column(
                    modifier = Modifier.padding(
                        top = config.cardTopPadding.dep(),
                        bottom = config.cardBottomPadding.dep(),
                        start = config.cardStartPadding.dep(),
                        end = config.cardEndPadding.dep()
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    FontFamilyText(
                        text = payeeName + if (payeeName == "You") stringResource(R.string.have_already_paid) else stringResource(
                            R.string.has_already_paid
                        ),
                        color = LightBlue4,
                        fontSize = 13.sep(),
                        fontWeight = FontWeight.Bold
                    )
                    config.gapBetweenText.sy()
                    FontFamilyText(
                        text = "$payeeName " + stringResource(R.string.dont_owe_anything),
                        color = LightBlue4,
                        fontSize = 13.sep()
                    )

                    85.sy()

                    // config.gapBetweenIconAndTotalCard.sy()

                    TotalCard_6re10h(
                        contentDescription = "TotalYouWillPayCard",
                        text = stringResource(R.string.total) + " " + stringResource(R.string.willpay,payeeName),
                        amount = total,
                        borderColor = Pink,
                        backgroundColor = LightRedButton
                    )
                }

            }

            Icon(
                modifier = Modifier
                    .size(123.dep())
                    .offset(
                        y = 0.dep()
                    )
                    .align(Alignment.Center),
                painter = painterResource(id = R.drawable.pay_hand_padded),
                tint = Color.Unspecified,
                contentDescription = "handIcon"
            )
        }
    }
}

