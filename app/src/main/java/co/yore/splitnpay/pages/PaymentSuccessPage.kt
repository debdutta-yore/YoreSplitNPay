package co.yore.splitnpay.pages

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import co.yore.splitnpay.R
import co.yore.splitnpay.addmembers.FontFamilyText
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.models.DataIds

@Composable
fun PaymentSuccessScreen(
    paymentSuccessMessage: String = stringState(key = DataIds.paymentSuccessMessage).value,
    paymentSuccessButtonText: String = stringState(key = DataIds.paymentSuccessButtonText).value,
    paymentSuccessBackText: String = stringState(key = DataIds.paymentSuccessBackText).value,
    notifier: NotificationService = notifier()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = co.yore.splitnpay.ui.theme.Greenishblue)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            19.sx()
            Icon(
                modifier = Modifier
                    .padding(top = 14.dep())
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null
                    ) {
                        notifier.notify(DataIds.back)
                    },
                painter = painterResource(id = R.drawable.ic_left_chevron),
                tint = Color.Unspecified,
                contentDescription = " ",
            )
            13.sx()
            FontFamilyText(
                modifier = Modifier
                    .padding(top = 12.dep())
                    .align(Alignment.CenterVertically),
                text = paymentSuccessBackText,
                color = Color.White,
                fontSize = 14.sep(),
                fontWeight = FontWeight.Bold
            )
        }
        85.sy()
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(234.dep()),
            contentAlignment = Center
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(id = R.drawable.payment_success_image),
                contentDescription = " "
            )
        }
        11.sy()
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_success),
                "",
                tint = Color.Unspecified
            )
            12.sx()
            FontFamilyText(
                text = paymentSuccessMessage,
                color = Color.Cyan,
                fontSize = 18.sep(),
                letterSpacing = 0.67.sep(),
                lineHeight = 21.09.sep()
            )
        }
        123.sy()
        OutlinedButton(
            onClick = {
                notifier.notify(DataIds.paymentSuccessButtonClick)
            },
            modifier = Modifier
                .width(127.dep())
                .height(36.dep())
                .align(CenterHorizontally),
            shape = RoundedCornerShape(18.dep()),
            border = BorderStroke(
                1.dep(),
                color = Color.Cyan
            ),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)

        ) {
            FontFamilyText(
                text = paymentSuccessButtonText,
                fontSize = 12.sep(),
                color = Color.Cyan
            )
        }
    }
}