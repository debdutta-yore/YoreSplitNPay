package co.yore.splitnpay.pages

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import co.yore.splitnpay.R
import co.yore.splitnpay.addmembers.FontFamilyText
import co.yore.splitnpay.components.PhotoSelectionBottomSheet
import co.yore.splitnpay.components.components.*
import co.yore.splitnpay.components.configuration.TopbarWithIconConfiguration
import co.yore.splitnpay.demos.sx
import co.yore.splitnpay.demos.sy
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.locals.localCurrency
import co.yore.splitnpay.models.*
import co.yore.splitnpay.ui.theme.*
import co.yore.splitnpay.viewModels.MemberPayment
import co.yore.splitnpay.viewModels.MembersMock.transaction
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.rudra.yoresplitbill.common.dashedBorder
import kotlinx.coroutines.launch



@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SplitDetailsScreen(
    sheetHandler: SheetHandler = localSheetHandler(),
    sheets: Sheets = tState<Sheets>(key = DataIds.sheets).value,
    billTotalBottomSheetModel: BillTotalBottomSheetModel = t(DataIds.billTotalBottomSheetModel)
) {
    val state = sheetHandler.handle()
    ModalBottomSheetLayout(
        sheetState = state,
        sheetContent = {
            when(sheets){
                Sheets.ImagePicker-> PhotoSelectionBottomSheet()
                Sheets.BillTotalAndCategories-> billTotalBottomSheetModel.provide { BillTotalBottomSheet() }
                else->Text("")
            }
        },
        sheetShape = RoundedCornerShape(
            topStart = 25f.dep(),
            topEnd = 25f.dep()
        ),
        scrimColor = Color(0x8C243257)
    ) {
        SplitDetailsPage()
    }
}

data class Transaction(
    val name: String,
    val imageUrl: String = "https://i.pravatar.cc/300",
    val mobileNumber: String,
    val amount: Float,
    val transactionType: TransactionType,
    val isSelected:Boolean = false
)
enum class SplitListOptions {
    Equal,
    Unequal,
    Ratio,
    Percentage
}
data class SplitTypeRowItem(
    val selectedOptionBackground: Color = Bluish,
    val unSelectedOptionBackground: Color = LightBlue1,
    val selectedOptionTextColor: Color = White,
    val unSelectedOptionTextColor: Color = Bluish,
    val fontSize: Float = 12f
)
val BluishGrey = Color(0xff8498AB)
@Composable
fun SplitTypeRowItem_yxqp10(
    selected: Boolean,
    onClick: () -> Unit,
    item: String,
    config: SplitTypeRowItem = SplitTypeRowItem(),
    contentDescription:String
) {

    val optionColor = animateColorAsState(
        targetValue =
        if (selected) config.selectedOptionBackground
        else config.unSelectedOptionBackground,
        animationSpec = tween(500)
    )
    val optionTextColor = animateColorAsState(
        targetValue =
        if (selected) config.selectedOptionTextColor
        else config.unSelectedOptionTextColor,
        animationSpec = tween(500)
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null
            ) {
                onClick()
            }
            .clip(CircleShape)
            .background(
                color = optionColor.value
            )
    ) {
        Text(
            modifier = Modifier
                .padding(
                    top = 6.dep(),
                    bottom = 7.dep(),
                    start = 8.dep(),
                    end = 8.dep()
                ),
            text = item,
            color = optionTextColor.value,
            fontSize = config.fontSize.sep()
        )
    }
}
@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Composable
fun SplitDetailsPage(
    paidList: List<MemberPayment> = listState(key = DataIds.paidList),
    adjustList: List<MemberPayment> = listState(key = DataIds.adjustedList),
    billTotal: Float = floatState(key = DataIds.billTotal).value,
    selectedTabIndex: Int = intState(key = DataIds.selectedTabIndex).value,
    selectedListOption: Int = intState(key = DataIds.selectedListOption).value,
    subCategoryText: String = stringState(key = DataIds.subCategoryText).value,
    numberOfGroupMembers: Int = intState(key = DataIds.numberOfGroupMembers).value,
    paidRemaining: Double = doubleState(key = DataIds.paidRemaining).value,
    adjustRemaining: Double = doubleState(key = DataIds.adjustRemaining).value,
    receipt: Any? = tState<Any?>(key = DataIds.receipt).value,
    notifier: NotificationService = notifier()
) {
    val numberOfFriends = "$numberOfGroupMembers friends"

    val remainingFriends = remember {
        mutableStateOf(0)
    }

    //val splitListOptionsList = listOf("Equal", "Unequal", "Ratio", "Percentage")
    val splitListOptionsList = remember{ SplitListOptions.values() }
    val isAmountEditable = remember { mutableStateOf(false) }
    val tabsList = listOf(stringResource(R.string.paid_by), stringResource(R.string.adjust_split))
    val listState = rememberLazyListState()

    Box {
        Column(
            modifier = Modifier
                .padding(top = 54.dep())
                .fillMaxSize()
                .scrollable(
                    rememberScrollState(),
                    Orientation.Vertical
                )
        ) {
            val normalScript = SpanStyle(
                baselineShift = BaselineShift.None,
                fontSize = 14.sep(),
                fontWeight = FontWeight.Normal,
                fontFamily = robotoFonts,
                color = BluishGrey
            )
            val boldScript = SpanStyle(
                baselineShift = BaselineShift.None,
                fontSize = 14.sep(),
                fontWeight = FontWeight.Bold,
                fontFamily = robotoFonts
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dep())
                    .background(LightBlue1),
                contentAlignment = Center
            ) {
                FontFamilyText(
                    annotatedString = buildAnnotatedString {
                        withStyle(normalScript) {
                            append(stringResource(R.string.splitting_among) + ": ")
                        }
                        withStyle(boldScript) {
                            append(numberOfFriends)
                        }
                    },
                    fontSize = 14.sep()
                )
            }
            22.sy()
            Box(
                modifier = Modifier.fillMaxWidth()
            ){
                FontFamilyText(
                    text = stringResource(R.string.total_bill_amount),
                    fontSize = 14.sep(),
                    color = BluishGrey,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(alignment = Center)
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(
                            end = 16.dep()
                        )
                        .clip(CircleShape)
                        .clickable {
                            notifier.notify(DataIds.editBillAmountClick)
                        }
                ) {
                    EditIcon_gx6f1w(
                        text = "Edit",
                        icon = R.drawable.edit,
                        iconTint = Bluish,
                        contentDescription = "edit icon"
                    )
                }
            }

            18.sy()
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                FontFamilyText(
                    modifier = Modifier
                        .fillMaxWidth(),
                    annotatedString = billTotal.amountAnnotatedString(
                        isSpaceBetween = true,
                        isCurrencySymbolEnabled = false,
                        wholeNumberTextColor = DarkBlue,
                        wholeNumberFontSize = 40f,
                        decNumberTextColor = DarkBlue,
                        decNumberFontSize = 14f,
                        isTrailingTextEnabled = true,
                        trailingText = " INR",
                        trailingTextFontSize = 21f,
                        trailingTextTextColor = DarkBlue,
                        trailingTextFontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center
                )
            }
            10.sy()
            FontFamilyText(
                modifier = Modifier
                    .align(CenterHorizontally),
                text = billTotal.toInt().numberToWords(),
                color = DarkBlue,
                textAlign = TextAlign.Center,
                fontSize = 12.sep()
            )
            18.sy()
            val interactionSource = remember { MutableInteractionSource() }
            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dep())
                    .padding(horizontal = 69.dep())
                    .border(
                        width = 1.dep(),
                        color = GreyBorder,
                        shape = RoundedCornerShape(8.dep())
                    ),
                value = subCategoryText,
                onValueChange = { notifier.notify(DataIds.subCategoryText, it) },
            ) { innerTextField ->
                TextFieldDefaults.TextFieldDecorationBox(
                    value = subCategoryText,
                    innerTextField = innerTextField,
                    contentPadding = PaddingValues(
                        bottom = 9.dep(),
                        start = 12.dep()
                    ),
                    singleLine = true,
                    visualTransformation = VisualTransformation.None,
                    enabled = true,
                    placeholder = {
                        FontFamilyText(
                            text = stringResource(id = R.string.description),
                            fontSize = 13.sep(),
                            color = GreyBorder
                        )
                    },
                    trailingIcon = {

                    },
                    interactionSource = interactionSource
                )
            }
            32.sy()
            Row(
                modifier = Modifier
                    .align(CenterHorizontally)
            ) {
                Column(
                    modifier = Modifier
                        .padding(top = 2.dep())
                ) {
                    FontFamilyText(
                        text = stringResource(R.string.category) + ":",
                        color = LightBlue4,
                        fontSize = 15.sep()
                    )
                    12.sy()
                    CategorySelectorCard_owv32g(
                        contentDescription = "Category Selector"
                    )
                }
                29.sx()
                Column(
                    modifier = Modifier
                        .padding(top = 2.dep())
                ) {
                    FontFamilyText(
                        text = stringResource(R.string.date) + ":",
                        color = LightBlue4,
                        fontSize = 15.sep(),
                    )
                    12.sy()
                    DateSelectorCard_5b6qhm(contentDescription = "Date Selector")
                }
                23.sx()
                DashedBorderIconButtonWithText_13ppr3(
                    text = stringResource(R.string.receipt),
                    image = receipt,
                    contentDescription = "Dashed Receipt button",
                    onDelete = {
                        notifier.notify(DataIds.deleteReceipt)
                    }
                ) {
                    notifier.notify(DataIds.receiptClick)
                }
            }
            43.sy()
            TabRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 31.dep(),
                        end = 106.dep()
                    ),
                selectedTabIndex = selectedTabIndex,
                backgroundColor = Color.White,
                indicator = { tabPositions: List<TabPosition> ->
                    Box {}
                },
                divider = { TabRowDefaults.Divider(color = Color.Transparent) },
            ) {
                tabsList.forEachIndexed { index, text ->
                    TabItemUI(selectedTabIndex,index,text){
                        notifier.notify(DataIds.selectedTabIndex,it)
                    }
                }
            }
            22.sy()
            AnimatedVisibility(
                visible = selectedTabIndex == 1,
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ){
                    LazyRow(
                        modifier = Modifier
                            .padding(horizontal = 33.dep()),
                        horizontalArrangement = Arrangement.spacedBy(10f.dep())
                    ) {
                        itemsIndexed(splitListOptionsList) { index, item ->
                            val selected = selectedListOption == index
                            SplitTypeRowItem_yxqp10(
                                item = item.name,
                                selected = selected,
                                onClick = {
                                    notifier.notify(DataIds.selectedListOption,index)
                                },
                                contentDescription = "Split Type item"
                            )
                        }
                    }
                    24.sy()
                }
            }
            isAmountEditable.value = selectedListOption == 1

            Box(
                modifier = Modifier
            ) {
                if (selectedTabIndex == 1) {
                    val editWidths = remember {
                        mutableStateMapOf<Int,Int>()
                    }
                    val maxEditWidth by remember {
                        derivedStateOf {
                            if(editWidths.isNotEmpty()){
                                editWidths.maxBy {
                                    it.value
                                }.value
                            }
                            else{
                                0
                            }
                        }
                    }
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fadingEdge(),
                        verticalArrangement = Arrangement.spacedBy(
                            18f.dep()
                        )
                    ) {
                        itemsIndexed(adjustList) { index, item ->
                            SplitMemberPaymentItem_z0nkzc(
                                item,
                                adjustable = true,
                                editable = selectedListOption == 1,
                                onEditWidth = {
                                    editWidths[index] = it
                                },
                                maxEditWidth = maxEditWidth,
                                notificationId = DataIds.adjustByAmount
                            )
                        }
                        item {
                            Column() {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Center
                                ){
                                    PaidMessage(adjustRemaining)
                                }
                                18.sy()
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(47.dep())
                                        .padding(
                                            start = 16.dep(),
                                            end = 17.dep()
                                        )
                                ) {
                                    CustomButton_3egxtx(
                                        text = stringResource(R.string.confirm_split),
                                        onClick = {
                                            notifier.notify(DataIds.confirmSplitClick)
                                        },
                                        contentDescription = "Confirm button",
                                        enabled = paidRemaining==0.0 && adjustRemaining==0.0
                                    )
                                }
                                18.sy()
                            }
                        }
                    }
                } else {
                    val editWidths = remember {
                        mutableStateMapOf<Int,Int>()
                    }
                    val maxEditWidth by remember {
                        derivedStateOf {
                            if(editWidths.isNotEmpty()){
                                editWidths.maxBy {
                                    it.value
                                }.value
                            }
                            else{
                                0
                            }
                        }
                    }
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fadingEdge(),
                        verticalArrangement = Arrangement.spacedBy(18f.dep()),
                        contentPadding = PaddingValues(top = 8.dep())
                    ) {
                        itemsIndexed(
                            paidList,
                            key = {index, item ->
                                item.id
                            }
                        ) { index, item ->
                            SplitMemberPaymentItem_z0nkzc(
                                item,
                                onEditWidth = {
                                    editWidths[index] = it
                                },
                                maxEditWidth = maxEditWidth,
                                notificationId = DataIds.paidByAmount
                            )
                        }
                        item{
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Center
                            ){
                                PaidMessage(paidRemaining)
                            }
                        }
                        item {
                            18.sy()
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(47.dep())
                                    .padding(
                                        start = 16.dep(),
                                        end = 17.dep()
                                    )
                            ) {
                                CustomButton_3egxtx(
                                    enabled = paidRemaining==0.0 && adjustRemaining==0.0,
                                    text = "Confirm and Split",
                                    onClick = {
                                        notifier.notify(DataIds.confirmSplitClick)
                                    },
                                    contentDescription = "Confirm button"
                                )
                            }
                            18.sy()
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dep()),
            contentAlignment = Center
        ) {
            TopBarWithIcon_1t9xbo(
                stringResource(R.string.split_details),
                onClick = {
                    notifier.notify(DataIds.back)
                },
                iconClick = {
                    notifier.notify(DataIds.scanClick)
                },
                isEndIconVisible = true
            )
        }


    }
}

@Composable
fun PaidMessage(remaining: Double) {
    val backgroundColor by remember(remaining) {
        derivedStateOf {
            if(remaining==0.0){
                Color(0xffF2FFFD)
            }
            else if(remaining>0f){
                Color(0xFFFFE2D3)
            }
            else if(remaining<0f){
                Color(0xffFFEFF4)
            }
            else{
                Color.Transparent
            }
        }
    }
    val animatedBackgroundColor by animateColorAsState(targetValue = backgroundColor)
    val message by remember(remaining) {
        derivedStateOf {
            if(remaining==0.0){
                "0 Remaining"
            }
            else if(remaining>0f){
                "₹ ${YoreAmountFormatter.decFormatter.format(remaining)} Remaining"
            }
            else if(remaining<0f){
                "₹ ${YoreAmountFormatter.decFormatter.format(-remaining)} Exceeded"
            }
            else{
                ""
            }
        }
    }
    val color by remember(remaining) {
        derivedStateOf {
            if(remaining==0.0){
                Color(0xff37D8CF)
            }
            else if(remaining>0f){
                Color(0xFFFF5700)
            }
            else if(remaining<0f){
                Color(0xffFF4077)
            }
            else{
                Color.Transparent
            }
        }
    }
    val animatedColor by animateColorAsState(targetValue = color)
    Box(
        modifier = Modifier
            .animateContentSize()
            .height(34.dep())
            .clip(CircleShape)
            .background(animatedBackgroundColor)
            .padding(horizontal = 10.dep()),
        contentAlignment = Center
    ){
        Text(
            text = message,

            color = animatedColor,
            fontSize = 12.sep(),
            fontFamily = robotoFonts
        )
    }

}

@Composable
fun TabItemUI(
    selectedTabIndex: Int,
    index: Int,
    text: String,
    onSelected: (Int)->Unit,
) {
    val color by remember(selectedTabIndex,index) {
        derivedStateOf {
            if (selectedTabIndex == index) DarkBlue else LightBlue3
        }
    }
    val animatedColor by animateColorAsState(
        targetValue = color,
        animationSpec = tween(500)
    )
    Text(
        modifier = Modifier
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null
            ) {
                onSelected(index)
            }
            .padding(0.dep()),
        text = text,
        fontSize = 21f.sep(),
        color = animatedColor,
        fontWeight = FontWeight.Bold,
    )
}

data class SplitAdjustItemConfiguration(
    val horizontalRowPadding: Float = 32f,
    val borderColor: Color = LightBlue1,
    val imageSize: Float = 49f,
    val borderStroke: Float = 3f,
    val placeholder: Int = R.drawable.personactionbar,
    val contentScale: ContentScale = ContentScale.Crop,
    val nameFontSize: Float = 12f,
    val nameTextColor: Color = DarkBlue,
    val phoneNumberFontSize: Float = 11f,
    val phoneNumberTextColor: Color = LightBlue5
)
@Composable
fun SplitAdjustItem_eugo18(
    memberPayment: MemberPayment,
    isEditable: Boolean,
    config: SplitAdjustItemConfiguration = SplitAdjustItemConfiguration(),
    contentDescription:String
) {
    Row(
        modifier = Modifier
            .padding(horizontal = config.horizontalRowPadding.dep())
            .semantics { this.contentDescription = contentDescription }
    ) {
        Box(
            modifier = Modifier
                .size(config.imageSize.dep()),
            contentAlignment = Alignment.TopEnd
        ) {
            AsyncImage(
                modifier = Modifier
                    .clip(CircleShape)
                    .fillMaxSize()
                    .border(
                        config.borderStroke.dep(),
                        color = config.borderColor,
                        shape = CircleShape
                    )
                    .padding(config.borderStroke.dep()),
                model = ImageRequest.Builder(LocalContext.current)
                    //TODO - update using friend.imageUrl
                    .data("https://i.pravatar.cc/300?" + memberPayment.name)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.personactionbar),
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
        }
        22.sx()
        Column(
            modifier = Modifier
                .padding(top = 7.dep())) {
            FontFamilyText(
                text = memberPayment.name,
                fontSize = config.nameFontSize.sep(),
                color = config.nameTextColor
            )
            FontFamilyText(
                text = memberPayment.mobile,
                fontSize = config.phoneNumberFontSize.sep(),
                color = config.phoneNumberTextColor
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        SplitAdjustAmount_ppv64u(memberPayment.toPay, isEditable)
    }
}
val WhitishGreen = Color(0xFFF2FFFD)
data class SplitMembersRemainingBoxConfiguration(
    val backgroundColor: Color = WhitishGreen,
    val textPaddingVertical: Float = 10f,
    val fontSize : Float = 12f,
    val textColor: Color = LightGreen3
)
@Composable
fun SplitMembersRemainingBox(
    remaining: Int,
    config: SplitMembersRemainingBoxConfiguration = SplitMembersRemainingBoxConfiguration(),
    contentDescription:String
){
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50.dep()))
                .background(config.backgroundColor)
        ) {
            FontFamilyText(
                modifier = Modifier.padding(
                    vertical = 10.dep(),
                    horizontal = 15.dep()
                ),
                text = remaining.toString() + " "+ stringResource(id = R.string.remaining),
                fontSize = config.fontSize.sep(),
                color = config.textColor
            )
        }
    }
}
data class SplitPaidByItemConfiguration(
    val horizontalRowPadding: Float = 32f,
    val selectedBorderColor: Color = Pink,
    val unSelectedBorderColor: Color = LightBlue1,
    val imageSize: Float = 49f,
    val selectedBorderStroke: Float = 2f,
    val unSelectedBorderStroke: Float = 3f,
    val placeholder: Int = R.drawable.personactionbar,
    val contentScale: ContentScale = ContentScale.Crop,
    val nameFontSize: Float = 12f,
    val nameTextColor: Color = DarkBlue,
    val phoneNumberFontSize: Float = 11f,
    val phoneNumberTextColor: Color = LightBlue5
)
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SplitMemberPaymentItem_z0nkzc(
    memberPayment: MemberPayment,
    adjustable: Boolean = false,
    editable: Boolean = false,
    config: SplitPaidByItemConfiguration = SplitPaidByItemConfiguration(),
    maxEditWidth: Int? = null,
    onEditWidth: (Int)->Unit = {},
    notificationId: Any,
    notifier: NotificationService = notifier()
) {
    val selected by remember(memberPayment.selected,adjustable) {
        derivedStateOf {
            memberPayment.selected && !adjustable
        }
    }

    Row(
        modifier = Modifier
            .padding(horizontal = config.horizontalRowPadding.dep())
    ) {
        Box(
            modifier = Modifier
                .size(config.imageSize.dep())
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() } // This is mandatory
                ) {
                    notifier.notify(DataIds.selectPaidByMemberClick, transaction)
                },
            contentAlignment = Alignment.TopEnd
        ) {
            val borderStroke by remember(selected) {
                derivedStateOf {
                    if (selected) config.selectedBorderStroke else config.unSelectedBorderStroke
                }
            }
            val animatedBorderStroke by animateFloatAsState(targetValue = borderStroke)
            val borderColor by remember(selected) {
                derivedStateOf {
                    if (selected) config.selectedBorderColor else config.unSelectedBorderColor
                }
            }
            val animatedBorderColor by animateColorAsState(targetValue = borderColor)
            AsyncImage(
                modifier = Modifier
                    .clip(CircleShape)
                    .fillMaxSize()
                    .border(
                        width = animatedBorderStroke.dep(),
                        color = animatedBorderColor,
                        shape = CircleShape
                    )
                    .clip(CircleShape)
                    .clickable {
                        if (!adjustable) {
                            notifier.notify(DataIds.memberPaymentCheck, memberPayment)
                        }
                    }
                    .padding(animatedBorderStroke.dep()),
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://i.pravatar.cc/300?")
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(config.placeholder),
                contentScale = ContentScale.Crop,
                contentDescription = "ProfileImage"
            )

            androidx.compose.animation.AnimatedVisibility (
                selected,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_tickmark),
                    contentDescription = "tick mark"
                )
            }
        }

        22.sx()

        Column(modifier = Modifier.padding(top = 7.dep())) {
            FontFamilyText(
                text = memberPayment.name,
                fontSize = config.nameFontSize.sep(),
                color = config.nameTextColor
            )
            FontFamilyText(
                text = memberPayment.mobile,
                fontSize = config.phoneNumberFontSize.sep(),
                color = config.phoneNumberTextColor
            )
        }



        //SplitAdjustAmount_ppv64u(memberPayment.toPay, isAmountEditable = false)
        var currentEditWidth by remember {
            mutableStateOf(0)
        }
        val density = LocalDensity.current.density
        var endPaddingPx by remember {
            mutableStateOf(0)
        }
        LaunchedEffect(key1 = maxEditWidth, key2 = currentEditWidth){
            maxEditWidth?.let {
                endPaddingPx = if(it>currentEditWidth){
                    it - currentEditWidth
                } else{
                    0
                }
            }
            if(maxEditWidth==null){
                endPaddingPx = 0
            }
        }
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(10.dep())
            .weight(1f)
        )
        AmountField(
            amount = if(adjustable) memberPayment.toPay else memberPayment.paid,
            enabled = selected||editable,
            modifier = Modifier
                .animateContentSize()
                .widthIn(min = 69.dep())
                .onGloballyPositioned {
                    currentEditWidth = it.size.width
                    onEditWidth(currentEditWidth)
                }
        ){
            notifier.notify(
                notificationId,
                Store()
                    .putAll(
                        "member" to memberPayment,
                        "amount" to it
                    ))
        }
        Box(
            modifier = Modifier
                .animateContentSize()
                .width((endPaddingPx / density).dp)
                .height(10.dep())
        ){

        }
    }
}
@Composable
fun TopBarWithIcon_1t9xbo(
    text: String,
    onClick: () -> Unit,
    endIcon: Int = R.drawable.camera,
    iconClick: () -> Unit = {},
    isEndIconVisible: Boolean = false,
    config: TopbarWithIconConfiguration = TopbarWithIconConfiguration()
) {
    Column(
        modifier = Modifier
            .coloredShadowDp(
                blurRadius = 6.dep(),
                offsetX = 0.dep(),
                offsetY = 3.dep(),
                color = Color(0xff075692).copy(alpha = 0.11f)
            )
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(
            modifier =
            Modifier
                .fillMaxWidth()
                .height(2.dep())
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(8.dep()))
            Icon(
                modifier = Modifier
                    .size((config.iconSize + 20).dep())
                    .clip(CircleShape)
                    .clickable {
                        onClick()
                    }
                    .padding(10.dep()),
                painter = painterResource(config.icon),
                contentDescription = "back arrow",
                tint = config.iconTint
            )
            Spacer(modifier = Modifier.width(3.dep()))
            Text(
                text = text,
                fontSize = config.textSize.sep(),
                fontWeight = config.fontWeight,
                color = config.fontColor
            )
            Spacer(modifier = Modifier.weight(1f))
            if (isEndIconVisible) {
                Icon(
                    painter = painterResource(endIcon),
                    contentDescription = "camera",
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .size(32.dep())
                        .clip(CircleShape)
                        .clickable {
                            iconClick()
                        }
                        .padding(7.dep()),
                    tint = Color.Unspecified
                )
            }
            Spacer(modifier = Modifier.width(14.dep()))
        }
        Spacer(
            modifier =
            Modifier
                .fillMaxWidth()
                .height(2.dep())
                /*.shadow(
                    3.dep(),
                    shape = RectangleShape,
                    clip = true,
                    ambientColor = White,
                    spotColor = DarkBlue
                )*/
            /*.coloredShadow(
                color = GreyShadow,
                borderRadius = 0.dep(),
                blurRadius = 6.dep(),
                spread = 0f,
                offsetX = 0.dep(),
                offsetY = 3f.dep(),
            )*/
        )
    }
}
data class EditIconConfiguration(
    val backgroundColor: Color = LightBlue1,
    val innerPaddingStart: Float = 7.92f,
    val innerPaddingEnd: Float = 4f,
    val innerPaddingTop: Float = 4f,
    val innerPaddingBottom: Float = 4f,
    val editIcon: Int = R.drawable.ic_pencil,
    val textColor: Color = Bluish,
    val fontSize: Float = 12f,
    val cornerRadius: Float = 11f
)
@Composable
fun EditIcon_gx6f1w(
    text: String,
    icon: Int,
    iconTint: Color,
    config: EditIconConfiguration = EditIconConfiguration(),
    contentDescription: String
) {
    Row(
        Modifier
            .semantics { this.contentDescription = contentDescription }
            .background(
                config.backgroundColor,
                shape = RoundedCornerShape(config.cornerRadius.dep())
            )
            .padding(
                start = config.innerPaddingStart.dep(),
                end = config.innerPaddingEnd.dep(),
                top = config.innerPaddingTop.dep(),
                bottom = config.innerPaddingBottom.dep()
            )
    ) {
        Icon(
            modifier = Modifier.align(Alignment.CenterVertically),
            painter = painterResource(id = icon),
            contentDescription = "edit",
            tint = iconTint,
        )
        Spacer(modifier = Modifier.width(5.dep()))
        FontFamilyText(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = text,
            fontSize = 12.sep(),
            color = Bluish
        )
        Spacer(modifier = Modifier.width(10.dep()))
    }

}
@Composable
fun Any.amountAnnotatedString(
    isLeadingTextEnabled: Boolean = false,
    leadingText: String = "",
    leadingTextFontSize: Float = 9f,
    leadingTextTextColor: Color = DarkBlue,
    leadingTextFontWeight: FontWeight = FontWeight(400),
    isTrailingTextEnabled: Boolean = false,
    trailingText: String = "",
    trailingTextFontSize: Float = 9f,
    trailingTextTextColor: Color = DarkBlue,
    trailingTextFontWeight: FontWeight = FontWeight(400),
    isSpaceBetween: Boolean = true,
    isCurrencySymbolEnabled: Boolean = true,
    currencyFontSize: Float = 12f,
    currencyTextColor: Color = DarkBlue,
    currencyFontWeight: FontWeight = FontWeight(400),
    wholeNumberFontSize: Float = 12f,
    wholeNumberTextColor: Color,
    wholeNumberFontWeight: FontWeight = FontWeight(400),
    decNumberFontSize: Float = 12f,
    decNumberTextColor: Color,
    decNumberFontWeight: FontWeight = FontWeight(400),
    isDecimalEnabled: Boolean = true
): AnnotatedString = buildAnnotatedString {

    if (isLeadingTextEnabled) {
        withStyle(
            style = SpanStyle(
                letterSpacing = (0.166667).sep(),
                fontSize = leadingTextFontSize.sep(),
                fontWeight = leadingTextFontWeight,
                color = leadingTextTextColor,
                fontFamily = robotoFonts
            ),
        ) {
            append(leadingText)
        }
    }

    if(isCurrencySymbolEnabled){
        withStyle(
            style = SpanStyle(
                letterSpacing = (-0.333333).sep(),
                fontSize = currencyFontSize.sep(),
                fontWeight = currencyFontWeight,
                color = currencyTextColor,
                fontFamily = robotoFonts
            ),
        ) {
            append(localCurrency.current)
            if(isSpaceBetween){
                append(" ")
            }
        }
    }

    withStyle(
        style = SpanStyle(
            letterSpacing = (-0.333333).sep(),
            fontSize = wholeNumberFontSize.sep(),
            fontWeight = wholeNumberFontWeight,
            color = wholeNumberTextColor,
            fontFamily = robotoFonts
        ),
    ) {
        append(this@amountAnnotatedString.toString().toDouble().splitted().whole.toDouble().formatComma())
    }
    if (isDecimalEnabled) {
        withStyle(
            style = SpanStyle(
                letterSpacing = (-0.333333).sep(),
                fontSize = decNumberFontSize.sep(),
                fontWeight = decNumberFontWeight,
                color = decNumberTextColor,
                fontFamily = robotoFonts
            ),
        ) {
            append('.')
            append(this@amountAnnotatedString.toString().toDouble().splitted().dec)
        }
    }

    if (isTrailingTextEnabled) {
        withStyle(
            style = SpanStyle(
                letterSpacing = (0.166667).sep(),
                fontSize = trailingTextFontSize.sep(),
                fontWeight = trailingTextFontWeight,
                color = trailingTextTextColor,
                fontFamily = robotoFonts
            ),
        )
        {
            append(trailingText)
        }
    }
}

fun Number.splitted(): FloatSplitted {
    val text = this.toString()
    val parts = text.split(".")
    var wholeText = parts[0]
    var decText = parts[1]
    val whole = wholeText.toInt()
    if (whole < 10) {
        wholeText = "0$whole"
    }
    if (decText.length < 2) {
        decText = "${decText}0"
    }
    if (decText.length > 2) {
        decText = decText.substring(0..1)
    }
    return FloatSplitted(
        wholeText,
        decText
    )
}

data class FloatSplitted(
    val whole: String,
    val dec: String
)
val RoundedBorderGrey = Color(0xffD1E5F4)
data class CategorySelectorCardConfiguration(
    val borderStroke: Float = 1f,
    val borderColor: Color = RoundedBorderGrey,
    val height: Float = 26f,
    val icon: Int = R.drawable.ic_trip,
    val fontSize: Float = 11f,
    val textColor: Color = DarkBlue,
    val fontWeight: FontWeight = FontWeight(400),
    val iconTopPadding: Float = 4.53f,
    val iconBottomPadding: Float = 6.19f,
    val iconStartPadding: Float = 9.53f,
    val iconTextSpacer: Float = 3.19f,
    val iconEndSpacer: Float = 21f
)
@Composable
fun CategorySelectorCard_owv32g(
    notifier: NotificationService = notifier(),
    config: CategorySelectorCardConfiguration = CategorySelectorCardConfiguration(),
    category: Category = tState<Category>(key = DataIds.category).value,
    contentDescription: String
) {
    Row(
        modifier = Modifier
            .semantics { this.contentDescription = contentDescription }
            .height(config.height.dep())
            .border(
                width = config.borderStroke.dep(),
                color = config.borderColor,
                shape = RoundedCornerShape(50.dep())
            )
            .clip(shape = RoundedCornerShape(50.dep()))
            .clickable {
                notifier.notify(DataIds.categoryEditClick)
            }
    ) {
        /*Icon(
            modifier = Modifier
                .padding(
                    top = config.iconTopPadding.dep(),
                    bottom = config.iconBottomPadding.dep(),
                    start = config.iconStartPadding.dep()
                ),
            painter = painterResource(id = config.icon),
            contentDescription = "trip icon",
            tint = Color.Unspecified
        )*/
        AsyncImage(
            model = category.icon,
            contentDescription = "trip icon",
            modifier = Modifier
                .padding(
                    top = config.iconTopPadding.dep(),
                    bottom = config.iconBottomPadding.dep(),
                    start = config.iconStartPadding.dep()
                ),
        )
        config.iconTextSpacer.sx()
        FontFamilyText(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = category.name,
            fontSize = config.fontSize.sep(),
            color = config.textColor,
            fontWeight = config.fontWeight
        )
        config.iconEndSpacer.sx()
    }
}
data class DateSelectorCardConfiguration(
    val borderStroke: Float = 1f,
    val borderColor: Color = RoundedBorderGrey,
    val height: Float = 26f,
    val icon: Int = R.drawable.ic_calender,
    val fontSize: Float = 13f,
    val textColor: Color = DarkBlue,
    val fontWeight: FontWeight = FontWeight.Bold
)
@Composable
fun DateSelectorCard_5b6qhm(
    contentDescription: String,
    dateText: String = stringState(key = DataIds.dateText).value,
    notifier: NotificationService = notifier(),
    config: DateSelectorCardConfiguration = DateSelectorCardConfiguration()
) {

    Row(
        modifier = Modifier
            .semantics { this.contentDescription = contentDescription }
            .height(config.height.dep())
            .border(
                width = config.borderStroke.dep(),
                color = config.borderColor,
                shape = RoundedCornerShape(50.dep())
            )
            .clip(shape = RoundedCornerShape(50.dep()))
            .clickable {
                notifier.notify(DataIds.dateClick)
            }
    ) {
        Icon(
            modifier = Modifier
                .padding(
                    top = 4.dep(),
                    bottom = 6.dep(),
                    start = 9.dep()
                ),
            painter = painterResource(id = config.icon),
            contentDescription = "calendar",
            tint = Color.Unspecified
        )

        Spacer(modifier = Modifier.width(7.dep()))

        FontFamilyText(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = dateText,
            fontSize = config.fontSize.sep(),
            fontWeight = config.fontWeight,
            color = config.textColor
        )
        Spacer(modifier = Modifier.width(21.dep()))
    }
}
data class DashedBorderIconButtonConfiguration(
    val borderWidth : Float = 1f,
    val borderColor: Color = DarkGrey,
    val borderCorner: Float = 5f,
    val iconWidth: Float = 20f,
    val iconHeight: Float= 18f,
    val fontSize: Float = 10f,
    val textColor: Color = LightBlue4
)
@Composable
fun DashedBorderIconButtonWithText_13ppr3(
    text: String,
    image: Any?,
    config: DashedBorderIconButtonConfiguration = DashedBorderIconButtonConfiguration(),
    contentDescription:String,
    onDelete: ()->Unit,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .semantics { this.contentDescription = contentDescription }
            .size(59.dep())
            .clip(RoundedCornerShape(config.borderCorner.dep()))
            .clickable {
                onClick()
            }
            .dashedBorder(
                width = config.borderWidth.dep(),
                color = config.borderColor,
                shape = RoundedCornerShape(config.borderCorner.dep()),
                on = config.borderCorner.dep(),
                off = config.borderCorner.dep()
            ),
    ) {
        if(image==null){
            14.sy()
            Icon(
                modifier = Modifier
                    .width(config.iconWidth.dep())
                    .height(config.iconHeight.dep())
                    .align(CenterHorizontally),
                painter = painterResource(R.drawable.ic_receipt),
                contentDescription = "receipt",
                tint = Color.Unspecified
            )
            10.sy()
            FontFamilyText(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                text = text,
                fontSize = config.fontSize.sep(),
                color = config.textColor
            )
            5.sy()
        }
        else{
            Box(
                modifier = Modifier.fillMaxSize()
            ){
                AsyncImage(
                    model = image,
                    contentDescription = "receipt_image",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(config.borderCorner.dep())),
                    contentScale = ContentScale.Crop
                )
                IconButton(
                    onClick = {
                        onDelete()
                    },
                    modifier = Modifier
                        .size(16.dep())
                        .clip(CircleShape)
                        .alpha(0.3f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "delete",
                        tint = Color.Red
                    )
                }
            }
        }
    }
}
data class SplitAdjustAmountConfiguration(
    val backgroundColor: Color = CardGrey,
    val width: Float = 83f,
    val height: Float = 33f,
    val currencyFontSize: Float = 12f,
    val currencyFontWeight: FontWeight = FontWeight.Normal,
    val currencyScriptSize: Float = 16f,
    val currencyScriptFontWeight: FontWeight = FontWeight.Bold,
    val decimalScriptSize: Float = 10f,
    val decimalScriptFontWeight: FontWeight = FontWeight.Normal,
    val fontWeight: FontWeight = FontWeight.Bold,
    val fontSize: Float = 20f,
    val textColor: Color = DarkBlue,
    val textStartPadding : Float = 8f,
    val textEndPadding : Float = 8f,
    val fontFamily: FontFamily = robotoFonts
)
val GreyShade = Color(0xff8C93A2)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SplitAdjustAmount_ppv64u(
    amount: Double,
    isAmountEditable: Boolean,
    config: SplitAdjustAmountConfiguration = SplitAdjustAmountConfiguration()
) {
    val textColor = remember {
        derivedStateOf {
            if(!isAmountEditable){
                if (amount != 0.0) config.textColor else GreyShade
            } else config.textColor
        }
    }
    Box(
        modifier = Modifier
            .width(config.width.dep())
            .height(config.height.dep())
            .background(
                color = config.backgroundColor,
                shape = RoundedCornerShape(8.dep())
            ),
        contentAlignment = Alignment.Center
    ) {
        val amountString = amount.amountAnnotatedString(
            isSpaceBetween = true,
            currencyFontWeight = config.fontWeight,
            currencyFontSize = config.currencyFontSize,
            currencyTextColor = textColor.value,
            wholeNumberFontSize = config.currencyScriptSize,
            wholeNumberFontWeight = config.currencyScriptFontWeight,
            wholeNumberTextColor = textColor.value,
            decNumberFontSize = config.decimalScriptSize,
            decNumberTextColor = textColor.value
        )
        val focusManager = LocalFocusManager.current

        val bringIntoViewRequester = remember { BringIntoViewRequester() }
        val coroutineScope = rememberCoroutineScope()
        var text by remember { mutableStateOf(TextFieldValue(amountString)) }

        if (isAmountEditable) {
            BasicTextField(
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Number),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }),
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                modifier = Modifier
                    .align(Alignment.Center)
                    .bringIntoViewRequester(bringIntoViewRequester)
                    .onFocusEvent { focusState ->
                        if (focusState.isFocused) {
                            coroutineScope.launch {
                                bringIntoViewRequester.bringIntoView()
                            }
                        }
                    },
                value = text,
                onValueChange = {
                    text = it
                },
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.Center),
                        contentAlignment = Alignment.Center,

                        ) {
                        innerTextField()
                    }
                }
            )
        } else {
            FontFamilyText(
                modifier = Modifier.align(Alignment.Center),
                annotatedString = amountString,
                fontSize = config.fontSize.sep(),
                color = textColor.value
            )
        }
    }
}