package co.yore.splitnpay.components.components

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import co.yore.splitnpay.R
import co.yore.splitnpay.addmembers.FontFamilyText
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.models.*
import co.yore.splitnpay.models.CheckboxConfiguration
import co.yore.splitnpay.pages.*
import co.yore.splitnpay.ui.theme.Bluish
import co.yore.splitnpay.ui.theme.DarkBlue
import co.yore.splitnpay.ui.theme.LightBlue4
import co.yore.splitnpay.ui.theme.LightGreen3
import coil.compose.AsyncImage
import coil.request.ImageRequest

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun YouWillGet(
    userName: String,
    total: Float,
    list: List<Transaction>,
    isWillGetTransactionSelected: Boolean,
    contentDescription: String,
    config: YouWillGetConfiguration = YouWillGetConfiguration(),
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
                text = "$userName " + stringResource(id = R.string.will_receive_nothing),
                color = LightBlue4,
                fontSize = 13.sep(),
                fontWeight = FontWeight.Bold
            )
            config.gapBetweenTwoText.sy()
            FontFamilyText(
                text = stringResource(id = R.string.nobody_indebted),
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
    } else {
        Column(
            modifier = Modifier
                .semantics { this.contentDescription = contentDescription }
                .fillMaxWidth()
        ) {
            val screenHeight = LocalConfiguration.current.screenHeightDp
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = (screenHeight * 0.6).dp)
                    .fadingEdge(),
                contentPadding = PaddingValues(vertical = 8.dep())
            ){
                itemsIndexed(list){index, item ->
                    YouWillGetSingleItem_080weu(
                        contentDescription = "YouWillGetSingleItem",
                        transaction = item,
                        onClick = { notifier.notify(DataIds.selectSettleGetMembers, index) }
                    )
                    if (index != list.lastIndex) {
                        config.gapBetweenTwoRow.sy()
                    }
                }
            }
            /*Column(modifier = Modifier.fillMaxWidth()) {
                list.forEachIndexed { index, item ->
                    YouWillGetSingleItem_080weu(
                        contentDescription = "YouWillGetSingleItem",
                        transaction = item,
                        onClick = { notifier.notify(DataIds.selectSettleGetMembers, index) }
                    )
                    if (index != list.lastIndex)
                        config.gapBetweenTwoRow.sy()
                }
            }*/

            AnimatedContent(
                targetState = isWillGetTransactionSelected,
                transitionSpec = {
                    fadeIn(animationSpec = tween(500, delayMillis = 90)) +
                        scaleIn(initialScale = 0.92f, animationSpec = tween(500, delayMillis = 90)) with
                        fadeOut(animationSpec = tween(500))
                }
            ) {
                if (it) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ){
                        29.sy()
                        FontFamilyText(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
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
                                    notifier.notify(DataIds.willGetSettleClick)
                                },
                                contentDescription = "SettleUpButton"
                            )
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ){
                        20.sy()
                        TotalCard_6re10h(
                            text = stringResource(R.string.total_you_will_get),
                            amount = total,
                            contentDescription = "YouWillGet",
                            borderColor = LightGreen3,
                            backgroundColor = WhitishGreen
                        )
                        24.sy()
                    }
                }
            }
        }
    }
}

@Composable
fun TotalCard_6re10h(
    contentDescription: String,
    config: TotalCardConfiguration = TotalCardConfiguration(),
    text: String,
    amount: Float,
    backgroundColor: Color,
    borderColor: Color
) {
    Row(
        modifier = Modifier
            .semantics { this.contentDescription = contentDescription }
            .height(46.dep())
            .fillMaxWidth()
            .clip(RoundedCornerShape(config.cornerRadius))
            .background(color = backgroundColor)
            .border(
                width = config.borderRadius.dep(),
                color = borderColor,
                shape = RoundedCornerShape(config.cornerRadius.dep())
            )
            .padding(horizontal = config.paddingValue.dep()),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        FontFamilyText(
            text = text,
            color = DarkBlue,
            fontSize = 12.sep()
        )
        FontFamilyText(
            annotatedString = amount.amountAnnotatedString(
                isSpaceBetween = true,
                currencyTextColor = DarkBlue,
                currencyFontSize = 11.29f,
                wholeNumberTextColor = DarkBlue,
                wholeNumberFontSize = 14f,
                wholeNumberFontWeight = FontWeight.W600,
                decNumberTextColor = DarkBlue,
                decNumberFontSize = 10f,
                trailingText = " Dr",
                trailingTextFontSize = 11.29f,
                trailingTextTextColor = borderColor,
                isTrailingTextEnabled = true
            )
        )
    }
}

@Composable
fun YouWillGetSingleItem_080weu(
    contentDescription: String,
    config: YouWillGetSingleItemConfiguration = YouWillGetSingleItemConfiguration(),
    transaction: Transaction,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .semantics { this.contentDescription = contentDescription }
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SelectorIcon_ulkel8(
            config = CheckboxConfiguration(iconColor = Bluish),
            contentDescription = "YouWillGetCheckBox",
            selected = transaction.isSelected,
            onClick = onClick
        )

        config.checkBoxEndPadding.sx()

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row() {
                ProfileImage_2hf7q0(
                    imageUrl = transaction.imageUrl,
                    contentDescription = "UserProfilePic"
                )
                config.profilePicEndPadding.sx()
                Column() {
                    FontFamilyText(
                        text = transaction.name,
                        fontSize = 12.sep(),
                        fontWeight = FontWeight.Bold,
                        color = DarkBlue
                    )
                    config.gapBetweenNameANdPhNo.sy()
                    FontFamilyText(
                        text = transaction.mobileNumber,
                        fontSize = 11.sep(),
                        color = Color(0xff5A87BB)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .width(config.amountBoxWidth.dep()),
                contentAlignment = Alignment.CenterStart
            ) {
                FontFamilyText(
                    textAlign = TextAlign.Left,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    annotatedString = transaction.amount.amountAnnotatedString(
                        isSpaceBetween = true,
                        currencyTextColor = DarkBlue,
                        currencyFontSize = 12f,
                        wholeNumberTextColor = DarkBlue,
                        wholeNumberFontWeight = FontWeight.Bold,
                        wholeNumberFontSize = 14f,
                        decNumberTextColor = DarkBlue,
                        decNumberFontSize = 10f
                    )
                )
            }
        }
    }

}

@Composable
fun ProfileImage_2hf7q0(
    imageUrl: Any?,
    config: ProfileImageConfiguration = ProfileImageConfiguration(),
    contentDescription: String
) {
    AsyncImage(
        modifier = Modifier
            .size(config.imageSize.dep())
            .border(
                width = config.borderStroke.dep(),
                color = config.borderColor,
                shape = config.shape
            )
            .padding(config.borderStroke.dep())
            .clip(CircleShape),
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .build(),
        placeholder = painterResource(config.placeholder),
        contentScale = config.contentScale,
        contentDescription = contentDescription
    )
}
