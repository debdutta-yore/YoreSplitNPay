package co.yore.splitnpay.pages

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.yore.splitnpay.R
import co.yore.splitnpay.addmembers.FontFamilyText
import co.yore.splitnpay.components.components.*
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.locals.localCurrency
import co.yore.splitnpay.models.*
import co.yore.splitnpay.ui.theme.*
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun SplitCardDetailPage(
    splitNote: String = stringState(key = DataIds.splitNote).value,
    splitAmount: Float = floatState(key = DataIds.splitAmount).value,
    notifier: NotificationService = notifier()
) {

    Box(modifier = Modifier) {
        CutoutShape(
            boxHeight = 220.dep().value,
            cardColor = LightGreen3
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

@Composable
fun SplitCardInformations() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 137.dep())
    ){
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .fadingEdge(),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(top = 12.dep())
        ){
            item(){
                SplitDetailedCard()
            }
            item{
                37.sy()
            }
            item{
                SplitSelectableMembers()
            }
            item{
                29.sy()
            }
            item{
                YouWillGetDetails_o29zr4(
                    modifier = Modifier.padding(horizontal = 19.dep()),
                    contentDescription = "YouWillGetDetails"
                )
            }
            item{
                DashedHeight(31f)
            }
            item{
                YouWillPayDetails_8jaxv7(
                    modifier = Modifier.padding(horizontal = 19.dep()),
                    contentDescription = ""
                )
            }
            item{
                DashedHeight(44f)
            }
            item{
                PaidByCard_ut150e(
                    modifier = Modifier.padding(horizontal = 19.dep()),
                    contentDescription = "PaidByCard"
                )
            }

            item{
                DashedHeight(34f)
            }
            item{
                SplitAmongCard_pinvzl(contentDescription = "SplitAmongCard")
            }
            item{
                DashedHeight(34f)
            }
        }
    }
}

@Composable
fun DashedHeight(
    height: Float,
) {
    Box(
        modifier = Modifier
            .height(height.dep())
            .width(1.dep())
            .dashedBorder(
                width = 1.dep(),
                color = LightBlue4,
                shape = RoundedCornerShape(0.dep()),
                on = 5.dep(),
                off = 5.dep()
            )
    )
}

@Composable
fun SplitSelectableMembers(
    splitSelectableMembers: List<SplitSelectableMember> = listState(key = DataIds.splitSelectableMembers)
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(25.dep()),
        contentPadding = PaddingValues(horizontal = 26.dep())
    ) {
        itemsIndexed(splitSelectableMembers) { index, it ->
            SummarySinglePeople_q6c90m(
                splitSelectableMember = it,
                contentDescription = "SummaryEachUser"
            )
        }
    }
}

@Composable
fun SplitDetailedCard(
    splitCardDetailsData: SplitCardDetailsData = tState<SplitCardDetailsData>(key = DataIds.splitCard).value
) {
    PaidCardDetails_d6za8c(
        modifier = Modifier.padding(horizontal = 19.dep()),
        data = splitCardDetailsData,
        contentDescription = "PaidCardDetails"
    )
}

@Composable
fun SplitCardBalance(
    splitBalance: Float = floatState(key = DataIds.splitBalance).value,
    splitStatusMessage: String = stringState(key = DataIds.splitStatusMessage).value,
    splitProgress: Float = floatState(key = DataIds.splitProgress).value,
    splitPaidMark: String = stringState(key = DataIds.splitPaidMark).value,
    splitTransacted: Float = floatState(key = DataIds.splitTransacted).value
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 18.dep())
            .coloredShadow(
                color = GrayShadow1,
                blurRadius = 33.dep(),
                borderRadius = 15.dep(),
                offsetX = 7.dep(),
                offsetY = 7.dep(),
                spread = 0f
            )
            .clip(RoundedCornerShape(15.dep()))
            .fillMaxWidth()
            .height(120.dep())
            .background(color = White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dep())
            ) {
                FontFamilyText(
                    modifier = Modifier.padding(top = 17.dep(), start = 21.dep()),
                    text = stringResource(R.string.balance),
                    color = DarkBlue,
                    fontSize = 14.sep(),
                    lineHeight = 16.41.sep(),
                    letterSpacing = 0.17.sep()
                )
                FontFamilyText(
                    modifier = Modifier.padding(top = 16.dep()),
                    annotatedString = splitBalance.amountAnnotatedString(
                        isSpaceBetween = true,
                        currencyTextColor = Pink,
                        currencyFontSize = 11f,
                        currencyFontWeight = FontWeight.Bold,
                        wholeNumberTextColor = Pink,
                        wholeNumberFontSize = 15f,
                        wholeNumberFontWeight = FontWeight.Bold,
                        decNumberTextColor = Pink,
                        decNumberFontSize = 9f
                    )
                )
            }
            19.sy()
            Row(
                modifier = Modifier
                    .padding(start = 24.dep(), end = 22.dep())
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                FontFamilyText(
                    text = splitStatusMessage,
                    color = LightGreen3,
                    fontSize = 12.sep(),
                    lineHeight = 14.06.sep(),
                    letterSpacing = (-0.33).sep()
                )
                FontFamilyText(
                    text = splitPaidMark,
                    color = LightBlue4,
                    fontSize = 9.sep()

                )
            }
            10.sy()
            RoundedLinearProgressIndicator(
                modifier = Modifier
                    .padding(
                        start = 23.dep(),
                        end = 22.dep()
                    )
                    .fillMaxWidth(),
                progress = splitProgress,
                color = LightGreen3,
                backgroundColor = Pink
            )
            10.sy()
            FontFamilyText(
                modifier = Modifier.padding(start = 24.dep()),
                text = "${localCurrency.current} $splitTransacted Paid / Received",
                color = LightGreen3,
                fontSize = 9.sep()
            )
        }
    }
}

// //////////


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SummarySinglePeople_q6c90m(
    splitSelectableMember: SplitSelectableMember,
    config: SummarySinglePeopleConfig = SummarySinglePeopleConfig(),
    notifier: NotificationService = notifier(),
    contentDescription: String
) {
    val (selected, onSelected) = remember { mutableStateOf(false) }

    val borderColor = animateColorAsState(
        targetValue = if (splitSelectableMember.isSelected) config.selectedBorderColor
        else config.unselectedBorderColor,
        tween(700)
    )

    val borderStroke by animateFloatAsState(
        targetValue = if (splitSelectableMember.isSelected) 2f else 3f,
        tween(700)
    )

    Column(
        modifier = Modifier
            .semantics { this.contentDescription = contentDescription },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() } // This is mandatory
                ) {
                    notifier.notify(DataIds.selectBalanceMember, splitSelectableMember)
                },
            contentAlignment = Alignment.TopEnd
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .coloredShadow(
                        color = config.shadowColor,
                        borderRadius = config.shadowBorderRadius.dp,
                        blurRadius = config.shadowBlurRadius.dp,
                        offsetY = config.shadowOffsetY.dp,
                        offsetX = config.shadowOffsetX.dp,
                        spread = config.shadowSpread
                    )
                    .clip(config.profileImageShape)
            ) {
                AsyncImage(
                    modifier = Modifier
                        .size(42.dep())
                        .clip(config.profileImageShape)
                        .border(
                            width = borderStroke.dep(),
                            color = borderColor.value,
                            shape = config.profileImageShape
                        ),
                    model = ImageRequest.Builder(LocalContext.current)
                        // TODO - update using friend.imageUrl
                        .data(splitSelectableMember.image)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(config.profileImagePlaceHolder),
                    contentScale = ContentScale.Crop,
                    contentDescription = contentDescription
                )
            }

            Box(
                Modifier
                    .padding(bottom = 42.droot2.dep(), start = 42.droot2.dep())
                    // .align(Alignment.TopEnd)
                    .size(18.dep())
            ) {
                androidx.compose.animation.AnimatedVisibility(
                    splitSelectableMember.isSelected,
                    enter = fadeIn(tween(700))+ scaleIn(tween(700)),
                    exit = fadeOut(tween(700))+ scaleOut(tween(700))
                ) {
                    SelectedIcon_f9tfi6(contentDescription = "SelectedIcon")
                }
            }
        }

        config.gapBetweenProfileImageAndName.sy()

        FontFamilyText(
            text = splitSelectableMember.name,
            fontWeight =
            if (splitSelectableMember.isSelected) {
                FontWeight.Bold
            } else {
                FontWeight.Normal
            },
            fontSize = 12.sep(),
            color = DarkBlue
        )
    }
}



val membersImages = listOf(
    Transaction(
        name = "You",
        imageUrl = "https://i.pravatar.cc/300",
        mobileNumber = "9563376942",
        amount = 3000f,
        transactionType = TransactionType.Paid,
        isSelected = true
    ),
    Transaction(
        name = "Sushil",
        imageUrl = "https://i.pravatar.cc/300",
        mobileNumber = "9563376942",
        amount = 1000f,
        transactionType = TransactionType.Paid
    ),
    Transaction(
        name = "Manisha Roy",
        imageUrl = "https://i.pravatar.cc/300",
        mobileNumber = "9563376942",
        amount = 500f,
        transactionType = TransactionType.Unspecified
    ),
    Transaction(
        name = "Sanjanaa Ray",
        imageUrl = "https://i.pravatar.cc/300",
        mobileNumber = "9563376942",
        amount = 0f,
        transactionType = TransactionType.Unspecified
    ),
    Transaction(
        name = "Sanjanaa Ray",
        imageUrl = "https://i.pravatar.cc/300",
        mobileNumber = "9563376942",
        amount = 0f,
        transactionType = TransactionType.Unspecified
    ),
    Transaction(
        name = "Sanjanaa Ray",
        imageUrl = "https://i.pravatar.cc/300",
        mobileNumber = "9563376942",
        amount = 0f,
        transactionType = TransactionType.Unspecified
    ),
    Transaction(
        name = "Sanjanaa Ray",
        imageUrl = "https://i.pravatar.cc/300",
        mobileNumber = "9563376942",
        amount = 0f,
        transactionType = TransactionType.Unspecified
    ),
    Transaction(
        name = "Sanjanaa Ray",
        imageUrl = "https://i.pravatar.cc/300",
        mobileNumber = "9563376942",
        amount = 0f,
        transactionType = TransactionType.Unspecified
    ),
    Transaction(
        name = "Sanjanaa Ray",
        imageUrl = "https://i.pravatar.cc/300",
        mobileNumber = "9563376942",
        amount = 0f,
        transactionType = TransactionType.Unspecified
    ),
    Transaction(
        name = "Sanjanaa Ray",
        imageUrl = "https://i.pravatar.cc/300",
        mobileNumber = "9563376942",
        amount = 0f,
        transactionType = TransactionType.Unspecified
    ),
    Transaction(
        name = "Sanjanaa Ray",
        imageUrl = "https://i.pravatar.cc/300",
        mobileNumber = "9563376942",
        amount = 0f,
        transactionType = TransactionType.Unspecified
    ),
    Transaction(
        name = "Sanjanaa Ray",
        imageUrl = "https://i.pravatar.cc/300",
        mobileNumber = "9563376942",
        amount = 0f,
        transactionType = TransactionType.Unspecified
    ),
    Transaction(
        name = "Sanjanaa Ray",
        imageUrl = "https://i.pravatar.cc/300",
        mobileNumber = "9563376942",
        amount = 0f,
        transactionType = TransactionType.Unspecified
    ),
    Transaction(
        name = "Sanjanaa Ray",
        imageUrl = "https://i.pravatar.cc/300",
        mobileNumber = "9563376942",
        amount = 0f,
        transactionType = TransactionType.Unspecified
    )
)
