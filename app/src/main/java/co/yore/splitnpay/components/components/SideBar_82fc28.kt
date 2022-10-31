package co.yore.splitnpay.components.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import co.yore.splitnpay.libs.jerokit.dep
import co.yore.splitnpay.libs.rotateVertically
import co.yore.splitnpay.libs.jerokit.sep
import co.yore.splitnpay.models.*

@Composable
fun SideBar_82fc28(
    time: String,
    transaction: BillTransaction,
    config: SideBarConfiguration = SideBarConfiguration()
) {
    val timeBarIcon = remember {
        derivedStateOf {
            if (transaction.transactionType == TransactionType.Received) {
                config.receivedIcon
            } else {
                config.paidIcon
            }
        }
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier
                .padding(
                    top = config.topPadding.dep(),
                    start = config.startPadding.dep(),
                    end = config.endPadding.dep()
                )
                .size(config.iconSize.dep()),
            painter = painterResource(id = timeBarIcon.value),
            tint = Color.Unspecified,
            contentDescription = "icon"
        )

        Spacer(modifier = Modifier.weight(1f))

        FontFamilyText(
            text = time,
            fontSize = config.timeFontSize.sep(),
            maxLines = 1,
            color = config.timeTextColor,
            modifier = Modifier
                .rotateVertically(VerticalRotation.COUNTER_CLOCKWISE)
                .padding(start = config.textStartPadding.dep()),
            fontWeight = config.timeFontWeight
        )
    }
}
