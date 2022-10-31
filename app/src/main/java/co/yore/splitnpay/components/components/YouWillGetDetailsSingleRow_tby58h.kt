package co.yore.splitnpay.components.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import co.yore.splitnpay.libs.amountAnnotatedString
import co.yore.splitnpay.libs.jerokit.sep
import co.yore.splitnpay.libs.sx
import co.yore.splitnpay.libs.sy
import co.yore.splitnpay.models.MemberWillGetOrPayDetailsSingleRowItem
import co.yore.splitnpay.models.YouWillGetDetailsSingleRowConfiguration

@Composable
fun YouWillGetOrPayDetailsSingleRow_tby58h(
    contentDescription: String,
    config: YouWillGetDetailsSingleRowConfiguration = YouWillGetDetailsSingleRowConfiguration(),
    item: MemberWillGetOrPayDetailsSingleRowItem
) {
    Row(
        modifier = Modifier.semantics { this.contentDescription = contentDescription },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ProfileImage_2hf7q0(
            image = item.imageUrl,
            contentDescription = "UserProfilePic"
        )

        config.gapBetweenProfileImageAndUserDetails.sx()

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {

            Column {
                FontFamilyText(
                    text = item.userName,
                    fontSize = config.userNameTextSize.sep(),
                    fontWeight = FontWeight.Bold,
                    color = config.userNameColor
                )

                config.gapBetweenUserNameAndMobileNo.sy()

                FontFamilyText(
                    text = item.userPhNo,
                    fontSize = config.userNameTextSize.sep(),
                    color = config.userPhNoTextColor
                )
            }

            config.gapBetweenUserDetailsAndGetText.sx()

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    FontFamilyText(
                        text = stringResource(config.YouWillGetDetailsSingleRowGetText),
                        fontSize = config.getTextFontSize.sep(),
                        color = config.getTextColor
                    )
                    FontFamilyText(
                        annotatedString = item.getAmount.amountAnnotatedString(
                            currencyFontSize = config.currencyFontSizeForGetAmount,
                            currencyTextColor = config.currencyTextColorForGetAmount,
                            decNumberTextColor = config.decNumberTextColorForGetAmount,
                            wholeNumberTextColor = config.wholeNumberTextColorForGetAmount,
                            wholeNumberFontSize = config.wholeNumberFontSizeForGetAmount,
                            isSpaceBetween = true,
                            wholeNumberFontWeight = FontWeight.Bold,
                            decNumberFontSize = config.decNumberFontSizeForGetAmount
                        )
                    )
                }

                config.gapBetweenGetAndPaid.sy()

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    FontFamilyText(
                        text = stringResource(config.YouWillGetDetailsSingleRowPaidText),
                        color = config.paidTextColor,
                        fontSize = config.paidTextFontSize.sep()
                    )

                    FontFamilyText(
                        annotatedString = item.paidAmount.amountAnnotatedString(
                            currencyFontSize = config.currencyFontSizeForPaidAmount,
                            currencyTextColor = config.currencyTextColorForPaidAmount,
                            decNumberTextColor = config.decNumberTextColorForPaidAmount,
                            wholeNumberTextColor = config.wholeNumberTextColorForPaidAmount,
                            isSpaceBetween = true,
                            wholeNumberFontWeight = FontWeight.Bold,
                            wholeNumberFontSize = config.wholeNumberFontSizeForPaidAmount,
                            decNumberFontSize = config.decNumberFontSizeForPaidAmount
                        )
                    )
                }
            }
        }
    }
}


