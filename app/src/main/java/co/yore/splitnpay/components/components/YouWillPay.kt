package com.rudra.yoresplitbill.ui.split.groupchat.settle

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.yore.splitnpay.R
import co.yore.splitnpay.addmembers.FontFamilyText
import co.yore.splitnpay.demos.sy
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.models.DataIds
import co.yore.splitnpay.pages.CustomButtonConfiguration
import co.yore.splitnpay.pages.CustomButton_3egxtx
import co.yore.splitnpay.pages.Transaction
import co.yore.splitnpay.ui.theme.Bluish
import co.yore.splitnpay.ui.theme.LightBlue4
import co.yore.splitnpay.ui.theme.LightRedButton
import co.yore.splitnpay.ui.theme.Pink
import com.rudra.yoresplitbill.ui.split.group.splitsummary.TotalCard_6re10h

@Composable
fun YouWillPay_m6awbp(
    list: List<Transaction>,
    userName: String,
    total: Float,
    isWillPayTransactionSelected : Boolean,
    contentDescription: String,
    config: YouWillPayConfiguration = YouWillPayConfiguration(),
    notifier: NotificationService = notifier()
) {
    if (list.isEmpty()) {
        Column(
            modifier = Modifier
                .semantics { this.contentDescription = contentDescription }
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            72.sy()
            FontFamilyText(
                text = "$userName " + stringResource(id = R.string.have_already_paid),
                color = LightBlue4,
                fontSize = 13.sep(),
                fontWeight = FontWeight.Bold
            )
            config.gapBetweenTwoText.sy()
            FontFamilyText(
                text = "$userName " + stringResource(id = R.string.dont_owe_anything),
                color = LightBlue4,
                fontSize = 13.sep()
            )
            config.gapBetweenTextAndIcon.sy()
            Icon(
                modifier = Modifier.size(121.dep()),
                painter = painterResource(id = R.drawable.thumbsupicon),
                contentDescription = "thumbsUpIcon",
                tint = Color.Unspecified
            )
            config.bottomPadding.sy()
        }
    }
    else {
        Column(modifier = Modifier
            .semantics { this.contentDescription = contentDescription }
            .fillMaxWidth()
        ) {
            val screenHeight = LocalConfiguration.current.screenHeightDp
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = (screenHeight*0.6f).dp)
                    .fadingEdge()
            ){
                itemsIndexed(list){index, item ->
                    YouWillGetSingleItem_080weu(
                        contentDescription = "YouWillGetSingleItem",
                        transaction = item,
                        onClick = {
                            notifier.notify(DataIds.selectSettlePayMembers, index)
                        }
                    )
                    if (index != list.lastIndex)
                        config.gapBetweenTwoRow.sy()
                }
            }
            /*Column(modifier = Modifier.fillMaxWidth()) {
                list.forEachIndexed { index, item ->
                    YouWillGetSingleItem_080weu(
                        contentDescription = "YouWillGetSingleItem",
                        transaction = item,
                        onClick = {
                            notifier.notify(DataIds.selectSettlePayMembers, index)
                        }
                    )
                    if (index != list.lastIndex)
                        config.gapBetweenTwoRow.sy()
                }
            }*/
            if (isWillPayTransactionSelected) {
                29.sy()
                FontFamilyText(
                    modifier = Modifier.align(CenterHorizontally),
                    color = Bluish,
                    text = stringResource(R.string.remind),
                    fontSize = 16.sep()
                )
                14.sy()
                Box(
                    modifier = Modifier
                        .padding(bottom = 11.dep())
                        .fillMaxWidth()
                        .height(config.bottomHeight.dep())
                ) {
                    CustomButton_3egxtx(
                        config = CustomButtonConfiguration(),
                        text = stringResource(R.string.settle_up),
                        onClick = {
                            notifier.notify(DataIds.willPaySettleClick)
                        },
                        contentDescription = "SettleUpButton"
                    )
                }
            }
            else {
                20.sy()
                TotalCard_6re10h(
                    text = stringResource(R.string.total_you_will_pay),
                    amount = total,
                    contentDescription = "YouWillGet",
                    borderColor = Pink,
                    backgroundColor = LightRedButton
                )
                24.sy()
            }
        }
    }
}

data class YouWillPayConfiguration(
    val gapBetweenTwoText: Float = 5f,
    val gapBetweenTextAndIcon: Float = 5f,
    val bottomPadding: Float = 105f,
    val gapBetweenTwoRow: Float = 18f,
    val bottomHeight: Float = 50f
)