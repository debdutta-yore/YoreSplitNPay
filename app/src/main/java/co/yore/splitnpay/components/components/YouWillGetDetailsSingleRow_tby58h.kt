package co.yore.splitnpay.components.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import co.yore.splitnpay.R
import co.yore.splitnpay.addmembers.FontFamilyText
import co.yore.splitnpay.demos.sx
import co.yore.splitnpay.demos.sy
import co.yore.splitnpay.libs.sep
import co.yore.splitnpay.pages.LightBlue5
import co.yore.splitnpay.ui.theme.DarkBlue
import co.yore.splitnpay.ui.theme.LightGreen3
import co.yore.splitnpay.ui.theme.Pink

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
            imageUrl = item.imageUrl,
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

data class YouWillGetDetailsSingleRowConfiguration(
    val gapBetweenProfileImageAndUserDetails: Float = 22f,
    val gapBetweenUserDetailsAndGetText: Float = 27f,
    val gapBetweenUserNameAndMobileNo: Float = 5f,
    val gapBetweenGetAndPaid: Float = 4f,

    val userNameTextSize: Float = 12f,
    val userPhNoTextSize: Float = 11f,
    val userNameColor: Color = DarkBlue,
    val userPhNoTextColor: Color = LightBlue5,

    val YouWillGetDetailsSingleRowGetText: Int = R.string.YouWillGetDetailsSingleRowGetText,
    val YouWillGetDetailsSingleRowPaidText: Int = R.string.YouWillGetDetailsSingleRowPaidText,
    val getTextColor: Color = LightGreen3,
    val paidTextColor: Color = LightBlue5,
    val getTextFontSize: Float = 11f,
    val paidTextFontSize: Float = 11f,

    val currencyFontSizeForGetAmount: Float = 12f,
    val currencyTextColorForGetAmount: Color = LightGreen3,
    val decNumberTextColorForGetAmount: Color = LightGreen3,
    val wholeNumberTextColorForGetAmount: Color = LightGreen3,
    val wholeNumberFontSizeForGetAmount: Float = 12f,
    val decNumberFontSizeForGetAmount: Float = 10f,

    val currencyFontSizeForPaidAmount: Float = 9f,
    val currencyTextColorForPaidAmount: Color = DarkBlue,
    val decNumberTextColorForPaidAmount: Color = DarkBlue,
    val wholeNumberTextColorForPaidAmount: Color = DarkBlue,
    val wholeNumberFontSizeForPaidAmount: Float = 10f,
    val decNumberFontSizeForPaidAmount: Float = 8f
)
{
    companion object{
        val variationOne = YouWillGetDetailsSingleRowConfiguration(
            YouWillGetDetailsSingleRowGetText = R.string.YouWillPayDetailsSingleRowDueText,
            YouWillGetDetailsSingleRowPaidText = R.string.YouWillPayDetailsSingleRowPaidText,
            getTextColor = Pink,
            paidTextColor = LightBlue5,
            currencyTextColorForGetAmount = Pink,
            decNumberTextColorForGetAmount = Pink,
            wholeNumberTextColorForGetAmount = Pink,
            currencyTextColorForPaidAmount = DarkBlue,
            decNumberTextColorForPaidAmount = DarkBlue,
            wholeNumberTextColorForPaidAmount = DarkBlue
        )
    }

}
