package co.yore.splitnpay.components.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import co.yore.splitnpay.R
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.libs.coloredShadow
import co.yore.splitnpay.libs.jerokit.dep
import co.yore.splitnpay.libs.jerokit.sep
import co.yore.splitnpay.models.*

@Composable
fun BillCard_s10zd7(
    transaction: BillTransaction,
    config: BillCardAllConfiguration = BillCardAllConfiguration(),
    onClick: () -> Unit
) {
    val cardHeight = remember {
        derivedStateOf {
            if (transaction.transactionStatus == TransactionStatus.AllSettled) {
                config.allSettledCardHeight
            } else {
                config.cardHeight
            }
        }
    }

    val isPaid = remember {
        derivedStateOf { transaction.transactionType == TransactionType.Paid }
    }

    val isSettled = remember {
        derivedStateOf { transaction.transactionStatus == TransactionStatus.Settled }
    }

    val isAllSettled = remember {
        derivedStateOf { transaction.transactionStatus == TransactionStatus.AllSettled }
    }

    val youPaidReceivedText = remember {
        derivedStateOf {
            if (isPaid.value) {
                config.youPaidStatusText
            } else {
                config.youReceivedStatusText
            }
        }
    }

    val paidReceivedText = remember {
        derivedStateOf {
            if (isPaid.value) {
                config.paidStatusText
            } else {
                config.receivedStatusText
            }
        }
    }

    val willGetPayText = remember {
        derivedStateOf {
            if (isPaid.value) {
                config.payStatusText
            } else {
                config.receiveStatusText
            }
        }
    }

    val allPaidReceivedText = remember {
        derivedStateOf {
            if (isPaid.value) {
                config.totalPaidText
            } else {
                config.totalReceivedText
            }
        }
    }

    val progress = remember {
        derivedStateOf {
            ((transaction.allPaidReceivedTotal) / transaction.billTotal)
        }
    }

    val progressColor = remember {
        derivedStateOf {
            if (isPaid.value) {
                config.paidProgressColor
            } else {
                config.receiveProgressColor
            }
        }
    }

    val progressBackgroundColor = remember {
        derivedStateOf {
            if (isPaid.value) {
                config.paidProgressBackgroundColor
            } else {
                config.receiveProgressBackgroundColor
            }
        }
    }

    val timeBarBackground = remember {
        derivedStateOf {
            if (transaction.transactionStatus == TransactionStatus.Settled) {
                config.settledTimeBarBackgroundColor
            } else if (transaction.transactionType == TransactionType.Paid) {
                config.paidTimeBackgroundColor
            } else {
                config.receivedTimeBackgroundColor
            }
        }
    }

    val timeBarAlignment = remember {
        derivedStateOf {
            if (isPaid.value) Alignment.CenterEnd else Alignment.CenterStart
        }
    }

    val leftTextColor = remember {
        derivedStateOf {
            if (isAllSettled.value) {
                config.receivedSelectedAmountTextColor
            } else if (transaction.isSingleChat) {
                config.amountNormalTextColor
            } else if (isPaid.value) {
                config.amountNormalTextColor
            } else {
                config.receivedSelectedAmountTextColor
            }
        }
    }

    val rightTextColor = remember {
        derivedStateOf {
            if (isAllSettled.value) {
                config.receivedSelectedAmountTextColor
            } else if (isPaid.value) {
                config.paidSelectedAmountTextColor
            } else {
                config.amountNormalTextColor
            }
        }
    }

    val billTotalRowStartPadding = remember {
        derivedStateOf {
            if (isAllSettled.value) {
                config.billTotalRowStartAllSettledPadding
            } else if (isPaid.value) {
                config.billTotalRowStartIsPaidPadding
            } else {
                config.billTotalRowStartPadding
            }
        }
    }
    val progressBarTopSpacer = remember {
        derivedStateOf {
            if (isAllSettled.value) {
                config.progressBarTopAllSettledSpacing
            } else {
                config.progressBarTopSpacing
            }
        }
    }

    val corerShape = config.cardCornerRounded.dep()
    val notRoundedShape = config.cardCornerNotRounded.dep()
    val allRoundedCardShape = config.cardCornerAll.dep()

    val cardCornerRadius = remember {
        derivedStateOf {
            if (isAllSettled.value) {
                RoundedCornerShape(allRoundedCardShape)
            } else if (isPaid.value) {
                RoundedCornerShape(
                    topStart = corerShape,
                    topEnd = notRoundedShape,
                    bottomStart = corerShape,
                    bottomEnd = corerShape
                )
            } else {
                RoundedCornerShape(
                    topStart = notRoundedShape,
                    topEnd = corerShape,
                    bottomStart = corerShape,
                    bottomEnd = corerShape
                )
            }
        }
    }

    val arrowIconEndPadding = remember {
        derivedStateOf {
            if (isAllSettled.value) {
                config.arrowIconEndAllSettledPadding
            } else {
                config.arrowIconEndPadding
            }
        }
    }

    val columnPaddingEnd = remember {
        derivedStateOf {
            if (isAllSettled.value) {
                config.columnStartAllSettledPadding
            } else if (isPaid.value) {
                config.columnStartPaidPadding
            } else {
                config.columnStartPadding
            }
        }
    }

    val columnPaddingStart = remember {
        derivedStateOf {
            if (isAllSettled.value) {
                config.columnEndAllSettledPadding
            } else if (isPaid.value) {
                config.columnEndPaidPadding
            } else {
                config.columnEndReceivedPadding
            }
        }
    }

    val allPaidReceivedTopPadding = remember {
        derivedStateOf {
            if (isAllSettled.value) {
                config.allPaidReceivedTopAllSettledPadding
            } else {
                config.allPaidReceivedTopPadding
            }
        }
    }

    val allPaidReceivedStartPadding = remember {
        derivedStateOf {
            if (isAllSettled.value) {
                config.allPaidReceivedAllSettledStartPadding
            } else if (isPaid.value) {
                config.allPaidReceivedPaidStartPadding
            } else {
                config.allPaidReceivedReceivedStartPadding
            }
        }
    }

    val allPaidReceivedEndPadding = remember {
        derivedStateOf {
            if (isAllSettled.value) {
                config.allPaidReceivedAllSettledEndPadding
            } else if (isPaid.value) {
                config.allPaidReceivedPaidEndPadding
            } else {
                config.allPaidReceivedReceivedEndPadding
            }
        }
    }

    val amountLeftTextColor = remember {
        derivedStateOf {
            if (!isAllSettled.value) rightTextColor.value else config.amountLeftTextColor
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
        // .height((cardHeight.value+config.cardShadowOffsetY*2+config.cardShadowBlurRadius*2).dep()),
    ){
        Column(
            modifier = Modifier
                .align(
                    if (transaction.transactionType == TransactionType.Received) {
                        Alignment.CenterStart
                    } else {
                        Alignment.CenterEnd
                    }
                )
                .width(
                    if (transaction.transactionStatus == TransactionStatus.AllSettled) 324.dep()
                    else if (transaction.transactionType == TransactionType.Received) (253 + 25).dep()
                    else (253).dep()
                )
        ) {
            Box(
                modifier = Modifier
                    .coloredShadow(
                        color = config.cardShadowColor,
                        borderRadius = config.cardShadowBorderRadius.dep(),
                        blurRadius = config.cardShadowBlurRadius.dep(),
                        offsetY = config.cardShadowOffsetY.dep(),
                        offsetX = config.cardShadowOffsetX.dep(),
                        spread = config.cardShadowSpread
                    )
                    .fillMaxWidth()
                    .height(cardHeight.value.dep())
                    .clip(cardCornerRadius.value)
                    .clickable { onClick() }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Box() { // container for card and paid/received bar
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .background(color = config.cardBackground)
                        ) {
                            config.cardInnerRowTopPadding.sy()

                            Row(
                                modifier = Modifier
                                    .padding(
                                        start = billTotalRowStartPadding.value.dep()
                                    )
                                    .fillMaxWidth()
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.Start,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (isAllSettled.value) config.firstRowAllSettledInternalSpacing.sx()

                                    FontFamilyText(
                                        letterSpacing = config.billTotalLetterSpacing.sep(),
                                        annotatedString = transaction.billTotal.amountAnnotatedString(
                                            isLeadingTextEnabled = true,
                                            leadingTextTextColor = config.amountNormalTextColor,
                                            leadingTextFontSize = config.billTotalLeadingFontSize,
                                            leadingText = stringResource(config.billTotalText) + "  ",
                                            currencyFontSize = config.billTotalCurrencyFontSize,
                                            currencyTextColor = config.amountNormalTextColor,
                                            currencyFontWeight = FontWeight.Bold,
                                            wholeNumberFontSize = config.billTotalWholeFontSize,
                                            wholeNumberTextColor = config.amountNormalTextColor,
                                            wholeNumberFontWeight = FontWeight.Bold,
                                            decNumberFontSize = config.billTotalDecFontSize,
                                            decNumberTextColor = config.amountNormalTextColor
                                        )
                                    )

                                    if (isAllSettled.value) {
                                        config.dotStartHorizontalSpacer.sx()
                                        Dot_t8dyts()
                                        config.dotEndHorizontalSpacer.sx()
                                        DateBox_rdu7a6(transaction.transactionDate)
                                        config.dotEndHorizontalSpacer.sx()
                                        Dot_t8dyts()
                                        config.dotLastHorizontalSpacer.sx()
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            CategoryRow_a96p42(category = transaction.category)
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.weight(1f))

                                Icon(
                                    modifier = Modifier
                                        .padding(
                                            top = config.arrowIconTopPadding.dep(),
                                            end = arrowIconEndPadding.value.dep()
                                        )
                                        .size(config.arrowIconSize.dep()),
                                    painter = painterResource(id = config.rightArrow),
                                    tint = Color.Unspecified,
                                    contentDescription = "right_arrow"
                                )
                            }

                            if (isAllSettled.value) {
                                config.firstRowVerticalAllSettledSpacing.sy()
                            } else if (isPaid.value) {
                                config.firstRowVerticalPaidSpacing.sy()
                            } else {
                                config.firstRowVerticalReceivedSpacing.sy()
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        start = columnPaddingStart.value.dep(),
                                        end = columnPaddingEnd.value.dep()
                                    ),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Left text
                                Column {
                                    if (isAllSettled.value) {
                                        CircleBoxText_yl5a4b(stringResource(id = R.string.all_settled))
                                    } else if (transaction.isSingleChat && isPaid.value) {
                                        CategoryRow_a96p42(category = transaction.category)
                                    } else if (transaction.isSingleChat && isSettled.value) {
                                        CircleBoxText_yl5a4b(stringResource(id = R.string.settled))
                                    } else {
                                        FontFamilyText(
                                            text = stringResource(id = youPaidReceivedText.value),
                                            fontSize = config.paidReceivedFontSize.sep(),
                                            color = config.paidReceivedColor,
                                            textAlign = TextAlign.End
                                        )

                                        FontFamilyText(
                                            annotatedString =
                                            transaction.paidReceived.amountAnnotatedString(
                                                currencyFontSize = config.leftAmountCurrencyFontSize,
                                                currencyTextColor = leftTextColor.value,
                                                wholeNumberFontSize = config.leftAmountWholeFontSize,
                                                wholeNumberTextColor = leftTextColor.value,
                                                wholeNumberFontWeight = FontWeight.Bold,
                                                decNumberFontSize = config.leftAmountDecFontSize,
                                                decNumberTextColor = leftTextColor.value
                                            ),
                                            letterSpacing = (-0.235784).sep()
                                        )
                                    }
                                }
                                // Right text
                                Column(
                                    horizontalAlignment = Alignment.End,
                                    modifier = Modifier.padding(end = 2.dep())
                                ) {
                                    if (isAllSettled.value) {
                                        FontFamilyText(
                                            text = "${transaction.completedTransactions} of ${transaction.totalTransactions} Paid",
                                            fontSize = config.totalPaidReceivedFontSize.sep(),
                                            color = config.totalPaidReceivedTextColor,
                                            lineHeight = config.totalPaidReceivedLineHeight.sep(),
                                            modifier = Modifier.padding(
                                                end = config.completedTransactionEndPadding.dep(),
                                                top = config.completedTransactionTopPadding.dep()
                                            ),
                                            letterSpacing = config.completedTransactionLetterSpacing.sep()
                                        )
                                    } else if (transaction.isSingleChat && !isPaid.value) {
                                        CategoryRow_a96p42(category = transaction.category)
                                    } else if (isSettled.value && !transaction.isSingleChat) {
                                        CircleBoxText_yl5a4b(text = stringResource(id = R.string.settled))
                                    } else {
                                        FontFamilyText(
                                            text = stringResource(id = willGetPayText.value),
                                            fontSize = config.willGetPayFontSize.sep(),
                                            color = config.willGetPayTextColor,
                                            textAlign = TextAlign.End
                                        )
                                        FontFamilyText(
                                            annotatedString =
                                            transaction.willPayReceive.amountAnnotatedString(
                                                currencyFontSize = config.willGetPayCurrencyFontSize,
                                                currencyTextColor = rightTextColor.value,
                                                isSpaceBetween = true,
                                                wholeNumberFontSize = config.willGetPayWholeFontSize,
                                                wholeNumberTextColor = rightTextColor.value,
                                                wholeNumberFontWeight = FontWeight.Bold,
                                                decNumberFontSize = config.willGetPayDecFontSize,
                                                decNumberTextColor = rightTextColor.value
                                            ),
                                            letterSpacing = config.willGetPayLetterSpacing.sep()
                                        )
                                    }
                                }
                            }

                            progressBarTopSpacer.value.sy()

                            RoundedLinearProgressIndicator(
                                modifier = Modifier
                                    .padding(
                                        start = columnPaddingStart.value.dep(),
                                        end = columnPaddingEnd.value.dep()
                                    )
                                    .fillMaxWidth()
                                    .height(config.progressBarHeight.dep()),
                                color = progressColor.value,
                                backgroundColor = progressBackgroundColor.value,
                                progress = progress.value
                            )

                            Row(
                                modifier = Modifier
                                    .padding(
                                        top = allPaidReceivedTopPadding.value.dep(),
                                        start = allPaidReceivedStartPadding.value.dep(),
                                        end = allPaidReceivedEndPadding.value.dep()
                                    )
                                    .fillMaxWidth()
                                    .padding(horizontal = config.allPaidReceivedExtraHorizontalPadding.dep()),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Bottom
                            ) {
                                if (transaction.isSingleChat) {
                                    if (!isPaid.value){
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                    FontFamilyText(
                                        annotatedString = transaction.paidReceived.amountAnnotatedString(
                                            currencyFontSize = config.amountLeftCurrencyFontSize,
                                            currencyTextColor = leftTextColor.value,
                                            isSpaceBetween = true,
                                            wholeNumberFontSize = config.amountLeftWholeFontSize,
                                            wholeNumberTextColor = leftTextColor.value,
                                            decNumberFontSize = config.amountLeftDecFontSize,
                                            decNumberTextColor = leftTextColor.value,
                                            isDecimalEnabled = false,
                                            isTrailingTextEnabled = true,
                                            trailingText = " " + stringResource(paidReceivedText.value),
                                            trailingTextFontSize = config.amountLeftTrailingFontSize,
                                            trailingTextTextColor = leftTextColor.value
                                        ),
                                        textAlign = TextAlign.End,
                                        letterSpacing = config.amountLeftLetterSpacing.sep()
                                    )
                                } else {
                                    FontFamilyText(
                                        fontSize = config.allPaidReceivedFontSize.sep(),
                                        annotatedString = transaction.allPaidReceivedTotal.amountAnnotatedString(
                                            isLeadingTextEnabled = true,
                                            leadingText =
                                            if (!isAllSettled.value) {
                                                stringResource(allPaidReceivedText.value) + " "
                                            } else "",
                                            leadingTextFontSize = config.allPaidReceivedFontSize,
                                            leadingTextTextColor = config.allPaidReceivedLeadingTextColor,
                                            isTrailingTextEnabled = isAllSettled.value,
                                            trailingTextTextColor = config.allPaidReceivedTrailingTextColor,
                                            isSpaceBetween = true,
                                            trailingText = " " + stringResource(paidReceivedText.value),
                                            trailingTextFontSize = config.allPaidReceivedTrailingFontSize,
                                            currencyFontSize = config.allPaidReceivedCurrencyFontSize,
                                            currencyTextColor = leftTextColor.value,
                                            wholeNumberFontSize = config.allPaidReceivedWholeFontSize,
                                            wholeNumberTextColor = leftTextColor.value,
                                            decNumberFontSize = config.allPaidReceivedDecFontSize,
                                            decNumberTextColor = leftTextColor.value,
                                            isDecimalEnabled = false
                                        ),
                                        letterSpacing = config.allPaidReceivedLetterSpacing.sep()
                                    )

                                    Spacer(modifier = Modifier.weight(1f))

                                    FontFamilyText(
                                        annotatedString = transaction.amountLeft.amountAnnotatedString(
                                            currencyFontSize = config.amountLeftCurrencyFontSize,
                                            currencyTextColor = amountLeftTextColor.value,
                                            isSpaceBetween = true,
                                            wholeNumberFontSize = config.amountLeftWholeFontSize,
                                            wholeNumberTextColor = amountLeftTextColor.value,
                                            decNumberFontSize = config.amountLeftDecFontSize,
                                            decNumberTextColor = amountLeftTextColor.value,
                                            isDecimalEnabled = false,
                                            isTrailingTextEnabled = true,
                                            trailingText = " " + stringResource(R.string.left),
                                            trailingTextFontSize = config.amountLeftTrailingFontSize,
                                            trailingTextTextColor = amountLeftTextColor.value
                                        ),
                                        textAlign = TextAlign.End,
                                        letterSpacing = config.amountLeftLetterSpacing.sep()
                                    )
                                }
                            }

                            config.cardBottomSpacer.sy()
                        }

                        if (!isAllSettled.value) {
                            Box(
                                modifier = Modifier
                                    .background(timeBarBackground.value)
                                    .align(timeBarAlignment.value)
                            ) {
                                SideBar_82fc28(
                                    time = transaction.transactionTime,
                                    transaction = transaction
                                )
                            }
                        }
                    }
                }
            }

            config.chatMessageReadSpacer.sy()

            /*if(!isPaid.value){
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement =
//                if (isPaid.value)
//                    Arrangement.End
//                else
                    Arrangement.Start
                ) {
                    ChatMessageReadStatus_m1cy0j(imageUrl = transaction.to.imageUrl)
                }
            }*/
        }
    }
}
