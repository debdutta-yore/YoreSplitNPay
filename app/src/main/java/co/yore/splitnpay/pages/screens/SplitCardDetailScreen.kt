package co.yore.splitnpay.pages.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import co.yore.splitnpay.R
import co.yore.splitnpay.components.components.*
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.libs.jerokit.*
import co.yore.splitnpay.models.*
import co.yore.splitnpay.pages.SplitCardBalance
import co.yore.splitnpay.pages.SplitCardInformations
import co.yore.splitnpay.ui.theme.*

@Composable
fun SplitCardDetailScreen(
    splitNote: String = stringState(key = DataIds.splitNote).value,
    splitAmount: Float = floatState(key = DataIds.splitAmount).value,
    notifier: NotificationService = notifier()
) {

    Box(modifier = Modifier) {
        CutoutShape(
            boxHeight = 220.dep().value,
            cardColor = Turquoise1
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BackArrowText(text = "Split Details") {
                    notifier.notify(DataIds.back)
                }
                Row(
                    modifier = Modifier
                        .padding(
                            top = 16.25.dep(),
                            end = 12.75.dep()
                        ),
                    horizontalArrangement = Arrangement.spacedBy(8.75.dep())
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_pencil),
                        "",
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .size(24.dep())
                            .clip(CircleShape)
                            .clickable {
                                notifier.notify(DataIds.edit)
                            }
                            .padding(6.dep())
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.ic_bin),
                        "",
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .size(24.dep())
                            .clip(CircleShape)
                            .clickable {
                                notifier.notify(DataIds.delete)
                            }
                            .padding(6.dep())
                    )
                }
            }
            50.sy()
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                FontFamilyText(
                    text = splitNote,
                    color = Color.White,
                    fontSize = 12.sep(),
                    lineHeight = 14.06.sep(),
                    letterSpacing = 0.17.sep()
                )
                FontFamilyText(
                    annotatedString = splitAmount.amountAnnotatedString(
                        currencyTextColor = White,
                        currencyFontSize = 21f,
                        wholeNumberTextColor = White,
                        wholeNumberFontSize = 30f,
                        wholeNumberFontWeight = FontWeight.Bold,
                        decNumberTextColor = White,
                        decNumberFontSize = 14f
                    )
                )
            }
            29.sy()
            Box(
                modifier = Modifier.fillMaxSize()
            ){
                SplitCardInformations()
                SplitCardBalance()
            }
        }
    }
}
