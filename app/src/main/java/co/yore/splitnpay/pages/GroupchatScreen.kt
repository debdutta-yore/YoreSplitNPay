package co.yore.splitnpay.pages

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.TextFieldDefaults.indicatorLine
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import co.yore.splitnpay.R
import co.yore.splitnpay.addmembers.FontFamilyText
import co.yore.splitnpay.components.components.CollapsibleBox
import co.yore.splitnpay.components.components.amountAnnotatedString
import co.yore.splitnpay.components.components.coloredShadow
import co.yore.splitnpay.demos.sx
import co.yore.splitnpay.demos.sy
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.models.BillTransaction
import co.yore.splitnpay.models.DataIds
import co.yore.splitnpay.ui.theme.*
import co.yore.splitnpay.viewModels.Conversation
import co.yore.splitnpay.viewModels.GroupCreationEvent
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
val LightGrey8 = Color(0xffD9D9D9)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    searchText: String = stringState(key = DataIds.searchText).value,
    notifier: NotificationService = notifier(),
    onFilter: () -> Unit
) {
    val colors = TextFieldDefaults.textFieldColors(
        disabledTextColor = Color.Black,
        backgroundColor = Color.White,
        focusedIndicatorColor = LightGrey8,
        unfocusedIndicatorColor = LightGrey8,
        disabledIndicatorColor = LightGrey8,
        cursorColor = LightGray
    )
    val interactionSource = remember { MutableInteractionSource() }
    BasicTextField(
        enabled = false,
        value = searchText,
        onValueChange = { notifier.notify(DataIds.searchText, it)},
        modifier = modifier
            .background(
                color = colors.backgroundColor(true).value,
                shape = TextFieldDefaults.TextFieldShape
            )
            .indicatorLine(
                enabled = true,
                isError = false,
                interactionSource = interactionSource,
                colors = colors
            ),
        interactionSource = interactionSource,
    ) { innerTextField ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ){
            TextFieldDefaults.TextFieldDecorationBox(
                value = searchText,
                innerTextField = innerTextField,
                interactionSource = interactionSource,
                contentPadding = PaddingValues(bottom = 9.dep()),
                singleLine = true,
                visualTransformation = VisualTransformation.None,
                enabled = true,
                placeholder = {
                    Text(
                        text = stringResource(R.string.split_payments),
                        fontWeight = FontWeight.Bold,
                        fontSize = 21.sep(),
                        color = Color.Black,
                        letterSpacing = 0.17.sep(),
                        lineHeight = 25.sep(),
                        textAlign = TextAlign.Center,
                    )
                },
                colors = colors,
                trailingIcon = {
                    Row {
                        Spacer(modifier = Modifier
                            .width(114.dep())
                        )
                        Icon(
                            modifier = Modifier
                                .size(33.dep())
                                .clip(CircleShape)
                                .clickable { }
                                .padding(7.dep()),
                            painter = painterResource(id = R.drawable.search),
                            contentDescription = "search",
                            tint = Color.Unspecified
                        )
                        7.sx()
                        Icon(
                            modifier = Modifier
                                .size(33.dep())
                                .clip(CircleShape)
                                .clickable {
                                    onFilter()
                                }
                                .padding(6.dep()),
                            painter = painterResource(id = R.drawable.filter_icon),
                            contentDescription = "filter",
                            tint = Color.Unspecified
                        )
                    }
                }
            )
        }
    }
}

@SuppressLint("Range")
@OptIn(ExperimentalMaterialApi::class, ExperimentalMotionApi::class)
@Composable
fun GroupChatScreen(
    modifier: Modifier = Modifier,
    conversations: List<Conversation> = listState(key = DataIds.conversations),
    groupNameString: String = stringState(key = DataIds.groupName).value,
    groupAmountString: Float = floatState(key = DataIds.groupAmount).value,
    groupImageString: String = stringState(key = DataIds.groupImage).value,
    groupCreationDateString: String = stringState(key = DataIds.groupCreationDate).value,
    isSingleChat: Boolean = boolState(key = DataIds.isSingleChat).value,
    filteredMemberName: String = stringState(key = DataIds.filteredMemberName).value,
    filteredMemberImage: String = stringState(key = DataIds.filteredMemberImage).value,
    notifier: NotificationService = notifier(),
    sheetHandler: SheetHandler = localSheetHandler()
) {
    val sheetState = sheetHandler.handle()
    val scope = rememberCoroutineScope()
    val lazyScrollState = rememberLazyListState()
    val dep = 1.dep()

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
                       Text("")
            /*if (sheet.value) {
                BillTotalBottomSheet(
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                FilterBottomSheet()
            }*/
        },
        scrimColor = Color(0x8C243257),
        sheetBackgroundColor = Color.White,
        sheetShape = RoundedCornerShape(
            topStart = 33.dep(),
            topEnd = 33.dep()
        )
    ) {
        CollapsibleBox(
            modifier = Modifier.fillMaxSize(),
            threshold = 0.05f
        ) { progress ->
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                MotionLayout(
                    modifier = Modifier.fillMaxSize(),
                    start = ConstraintSet {
                        val upperCut = createRefFor("upperCut")
                        val bottomCut = createRefFor("bottomCut")
                        val content = createRefFor("content")
                        val groupImage = createRefFor("groupImage")
                        val groupName = createRefFor("groupName")
                        val amount = createRefFor("amount")
                        val settleCircle = createRefFor("settleCircle")
                        val settleText = createRefFor("settleText")
                        val summaryCircle = createRefFor("summaryCircle")
                        val summaryText = createRefFor("summaryText")
                        val menuOverlay = createRefFor("menuOverlay")
                        val manageCircle = createRefFor("manageCircle")
                        val manageText = createRefFor("manageText")
                        val bottomBar = createRefFor("bottomBar")
                        val topBar = createRefFor("topBar")
                        val searchBar = createRefFor("searchBar")
                        val tabs = createRefFor("tabs")

                        constrain(topBar) {
                            top.linkTo(parent.top)
                        }
                        constrain(bottomBar) {
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            width = Dimension.fillToConstraints
                        }
                        constrain(searchBar) {
                            top.linkTo(upperCut.bottom, 64 * dep)
                            width = Dimension.fillToConstraints
                        }
                        constrain(tabs) {
                            top.linkTo(searchBar.bottom, 9 * dep)
                        }
                        constrain(menuOverlay) {
                            start.linkTo(upperCut.end)
                            top.linkTo(upperCut.top, 68 * dep)
                        }
                        constrain(summaryCircle) {
                            centerHorizontallyTo(upperCut)
                            top.linkTo(upperCut.bottom, -23.5 * dep)
                            width = Dimension.value(47 * dep)
                        }
                        constrain(summaryText) {
                            centerHorizontallyTo(summaryCircle)
                            top.linkTo(summaryCircle.bottom, 8 * dep)
                        }
                        constrain(manageCircle) {
                            end.linkTo(upperCut.end, 72 * dep)
                            top.linkTo(upperCut.bottom, -23.5 * dep)
                            width = Dimension.value(47 * dep)
                        }
                        constrain(manageText) {
                            centerHorizontallyTo(manageCircle)
                            top.linkTo(manageCircle.bottom, 8 * dep)
                        }
                        constrain(upperCut) {
                            height = Dimension.value(219 * dep)
                        }
                        constrain(bottomCut) {
                            top.linkTo(upperCut.bottom)
                        }
                        constrain(content) {
                            top.linkTo(tabs.bottom, 20 * dep)
                            bottom.linkTo(parent.bottom)
                            height = Dimension.fillToConstraints
                        }
                        constrain(groupImage) {
                            centerHorizontallyTo(upperCut)
                            top.linkTo(upperCut.top, 51 * dep)
                        }
                        constrain(groupName) {
                            centerHorizontallyTo(groupImage)
                            top.linkTo(groupImage.bottom, 14 * dep)
                        }
                        constrain(amount) {
                            centerHorizontallyTo(groupImage)
                            top.linkTo(groupName.bottom, 3 * dep)
                        }
                        constrain(settleCircle) {
                            start.linkTo(upperCut.start, 72 * dep)
                            top.linkTo(upperCut.bottom, -23.5 * dep)
                            width = Dimension.value(47 * dep)
                        }
                        constrain(settleText) {
                            centerHorizontallyTo(settleCircle)
                            top.linkTo(settleCircle.bottom, 8 * dep)
                        }
                    },
                    end = ConstraintSet {
                        val upperCut = createRefFor("upperCut")
                        val bottomCut = createRefFor("bottomCut")
                        val content = createRefFor("content")
                        val groupImage = createRefFor("groupImage")
                        val groupName = createRefFor("groupName")
                        val amount = createRefFor("amount")
                        val settleCircle = createRefFor("settleCircle")
                        val settleText = createRefFor("settleText")
                        val summaryCircle = createRefFor("summaryCircle")
                        val summaryText = createRefFor("summaryText")
                        val menuOverlay = createRefFor("menuOverlay")
                        val manageCircle = createRefFor("manageCircle")
                        val manageText = createRefFor("manageText")
                        val topBar = createRefFor("topBar")
                        val bottomBar = createRefFor("bottomBar")
                        val searchBar = createRefFor("searchBar")
                        val tabs = createRefFor("tabs")

                        constrain(topBar) {
                            top.linkTo(parent.top)
                        }

                        constrain(bottomBar) {
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            width = Dimension.fillToConstraints
                        }

                        constrain(searchBar) {
                            top.linkTo(upperCut.bottom, 25 * dep)
                            width = Dimension.fillToConstraints
                        }

                        constrain(tabs) {
                            top.linkTo(searchBar.bottom, 9 * dep)
                        }

                        constrain(menuOverlay) {
                            centerTo(summaryCircle)
                        }
                        constrain(summaryCircle) {
                            top.linkTo(settleCircle.bottom, 2 * dep)
                            end.linkTo(upperCut.end, 26 * dep)
                            width = Dimension.value(4 * dep)
                        }
                        constrain(summaryText) {
                            centerHorizontallyTo(summaryCircle)
                            top.linkTo(summaryCircle.bottom)
                            alpha = 0.0f
                            scaleX = 0.0f
                            scaleY = 0.0f
                            pivotX = 0.5f
                            pivotY = 0.05f
                        }
                        constrain(manageCircle) {
                            top.linkTo(summaryCircle.bottom, 2 * dep)
                            end.linkTo(upperCut.end, 26 * dep)
                            width = Dimension.value(4 * dep)
                        }
                        constrain(manageText) {
                            centerHorizontallyTo(manageCircle)
                            top.linkTo(manageCircle.bottom)
                            alpha = 0.0f
                            scaleX = 0.0f
                            scaleY = 0.0f
                            pivotX = 0.5f
                            pivotY = 0.05f
                        }
                        constrain(settleText) {
                            centerHorizontallyTo(settleCircle)
                            top.linkTo(settleCircle.bottom)
                            alpha = 0.0f
                            scaleX = 0.0f
                            scaleY = 0.0f
                            pivotX = 0.5f
                            pivotY = 0.05f
                        }
                        constrain(upperCut) {
                            height = Dimension.value(159 * dep)
                        }
                        constrain(bottomCut) {
                            top.linkTo(upperCut.bottom)
                        }
                        constrain(content) {
                            top.linkTo(tabs.bottom, 40 * dep)
                            bottom.linkTo(parent.bottom)
                            height = Dimension.fillToConstraints
                        }
                        constrain(groupImage) {
                            centerVerticallyTo(upperCut)
                            start.linkTo(upperCut.start, 37 * dep)
                        }
                        constrain(groupName) {
                            top.linkTo(groupImage.top, 3 * dep)
                            start.linkTo(groupImage.end, 12 * dep)
                        }
                        constrain(amount) {
                            top.linkTo(groupName.bottom, 3 * dep)
                            start.linkTo(groupName.start)
                        }
                        constrain(settleCircle) {
                            top.linkTo(upperCut.top, 72 * dep)
                            end.linkTo(upperCut.end, 26 * dep)
                            width = Dimension.value(4 * dep)
                        }
                    },
                    progress = progress
                ) {
                    UpperCut(
                        modifier = Modifier.layoutId("upperCut"),
                        color = LightGreen3
                    )

                    BottomCut(
                        modifier = Modifier.layoutId("bottomCut"),
                        color = LightGreen3
                    )

                    AsyncImage(
                        modifier = Modifier
                            .layoutId("groupImage")
                            .coloredShadow(
                                color = Color(0x4F075692),
                                offsetX = 0.dep(),
                                offsetY = 2.25.dep(),
                                blurRadius = 4.5.dep(),
                                borderRadius = 100.dep(),
                                spread = 0f
                            )
                            .size(51.dep())
                            .border(
                                width = 2.25.dep(),
                                color = Color.White,
                                shape = CircleShape
                            )
                            .padding(2.25.dep())
                            .clip(CircleShape),
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(groupImageString)
                            .crossfade(true).build(),
                        placeholder = painterResource(R.drawable.personactionbar),
                        contentScale = ContentScale.Crop,
                        contentDescription = null
                    )

                    FontFamilyText(
                        modifier = modifier.layoutId("groupName"),
                        text = groupNameString,
                        fontSize = minMaxRangeValue(
                            percentage = ((1 - progress) * 100),
                            min = 11f,
                            max = 16f
                        ).sep(),
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        letterSpacing = 0.166667.sep()
                    )

                    FontFamilyText(
                        modifier = modifier.layoutId("amount"),
                        annotatedString = groupAmountString.amountAnnotatedString(
                            wholeNumberTextColor = Color.White,
                            decNumberTextColor = Color.White,
                            currencyTextColor = Color.White,
                            currencyFontSize = 21f,
                            wholeNumberFontSize = minMaxRangeValue(
                                percentage = (1 - progress) * 100,
                                max = 30f,
                                min = 24f
                            ),
                            wholeNumberFontWeight = FontWeight.Bold,
                            decNumberFontSize = 14f,
                        )
                    )

                    SingleButton(
                        modifier = Modifier.layoutId("settleCircle"),
                        painter = painterResource(id = R.drawable.rupee_sign),
                        onClick = {
                            //navController.navigate("split_card_details_screen")
                            notifier.notify(DataIds.settleClick)
                        },
                        backgroundColor = Color.Companion.blend(
                            color1 = Color.Yellow,
                            color2 = Color.White,
                            progress = progress
                        ),
                        progress = progress
                    )

                    FontFamilyText(
                        modifier = Modifier
                            .layoutId("settleText")
                            .scale(1 - progress)
                            .alpha(1 - progress),
                        text = stringResource(R.string.settle),
                        fontSize = 11.sep(),
                        color = LightBlue4,
                        lineHeight = 12.89.sep(),
                        letterSpacing = 0.2.sep()
                    )

                    SingleButton(
                        modifier = Modifier.layoutId("summaryCircle"),
                        painter = painterResource(id = R.drawable.statement_icon),
                        onClick = {
//                                navController.navigate("split_summary_balance")
                            notifier.notify(DataIds.summaryClick)
                        },
                        progress = progress
                    )

                    FontFamilyText(
                        modifier = Modifier
                            .layoutId("summaryText")
                            .scale(1 - progress)
                            .alpha(1 - progress),
                        text = stringResource(R.string.summary),
                        fontSize = 11.sep(),
                        color = LightBlue4,
                        lineHeight = 12.89.sep(),
                        letterSpacing = 0.2.sep()
                    )

                    SingleButton(
                        modifier = Modifier
                            .layoutId("manageCircle"),
                        painter = painterResource(id = R.drawable.manage_icon),
                        onClick = {
                            notifier.notify(DataIds.manageClick)
                        },
                        progress = progress
                    )

                    FontFamilyText(
                        modifier = Modifier
                            .layoutId("manageText")
                            .scale(1 - progress)
                            .alpha(1 - progress),
                        text = stringResource(R.string.Manage),
                        fontSize = 11.sep(),
                        color = LightBlue4,
                        lineHeight = 12.89.sep(),
                        letterSpacing = 0.2.sep()
                    )

                    Box(
                        modifier = Modifier
                            .layoutId("content")
                            .clip(RoundedCornerShape(24.dep()))
                            .fillMaxWidth()
                            .background(
                                color = Whitish,
                                shape = RoundedCornerShape(24.dep())
                            ),
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            13.sy()
                            if (isSingleChat) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    AsyncImage(
                                        modifier = Modifier
                                            .coloredShadow(
                                                color = Color(0x1A000000),
                                                offsetX = 0.dep(),
                                                offsetY = 4.dep(),
                                                blurRadius = 4.dep(),
                                                spread = 0f
                                            )
                                            .clip(
                                                CircleShape
                                            )
                                            .size(33.dep())
                                            .border(
                                                width = 2.02.dep(),
                                                color = Color.White
                                            ),
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data(filteredMemberImage).crossfade(true)
                                            .build(),
                                        placeholder = painterResource(R.drawable.personactionbar),
                                        contentScale = ContentScale.Crop,
                                        contentDescription = null
                                    )
                                    8.sx()
                                    FontFamilyText(
                                        text = filteredMemberName,
                                        fontSize = 12.sep(),
                                        fontWeight = FontWeight.Bold,
                                        lineHeight = 14.06.sep(),
                                        letterSpacing = 0.2.sep()
                                    )
                                }
                                11.sy()
                            }
                            /*FontFamilyText(
                                modifier = Modifier.align(CenterHorizontally),
                                text = groupCreationDateString.toString(),
                                fontSize = 14.sep(),
                                color = Bluish
                            )*/
                            /*if (transactions.isEmpty()) {
                                8.sy()
                                FontFamilyText(
                                    modifier = Modifier.align(CenterHorizontally),
                                    annotatedString = buildAnnotatedString {
                                        withStyle(
                                            style = SpanStyle(
                                                fontSize = 13.sep(),
                                                color = Color(0xff5A87BB)
                                            ),
                                        ) {
                                            append(stringResource(R.string.you_created_group))
                                        }
                                        withStyle(
                                            style = SpanStyle(
                                                fontSize = 13.sep(),
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xff5A87BB)
                                            ),
                                        ) {
                                            append(" group name")
                                        }
                                    },
                                    color = Color(0xff5A87BB),
                                    fontSize = 13.sep()
                                )
                            }*/
                            20.sy()
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 18.dep())
                            ) {
                                LazyColumn(
                                    state = lazyScrollState
                                ) {
                                    items(conversations) { it ->
                                        when(it.type){
                                            Conversation.Type.TRANSACTION -> {
                                                BillCard_s10zd7(
                                                    transaction = it.data as BillTransaction,
                                                    onClick = {
                                                        notifier.notify(DataIds.cardClick)
                                                    }
                                                )
                                            }
                                            Conversation.Type.DATE -> {
                                                Box(
                                                    modifier = Modifier.fillMaxWidth()
                                                ){
                                                    FontFamilyText(
                                                        modifier = Modifier.align(Alignment.Center),
                                                        text = it.data as String,
                                                        fontSize = 14.sep(),
                                                        color = Bluish
                                                    )
                                                }

                                            }
                                            Conversation.Type.CREATION -> {
                                                Box(
                                                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dep())
                                                ) {
                                                    FontFamilyText(
                                                        modifier = Modifier.align(Alignment.Center),
                                                        annotatedString = buildAnnotatedString {
                                                            withStyle(
                                                                style = SpanStyle(
                                                                    fontSize = 13.sep(),
                                                                    color = Color(0xff5A87BB)
                                                                ),
                                                            ) {
                                                                append(stringResource(R.string.x_created_group,(it.data as GroupCreationEvent).creator))
                                                            }
                                                            append(" ")
                                                            withStyle(
                                                                style = SpanStyle(
                                                                    fontSize = 13.sep(),
                                                                    fontWeight = FontWeight.Bold,
                                                                    color = Color(0xff5A87BB)
                                                                ),
                                                            ) {
                                                                append((it.data as GroupCreationEvent).groupName)
                                                            }
                                                        },
                                                        color = Color(0xff5A87BB),
                                                        fontSize = 13.sep()
                                                    )
                                                }
                                            }
                                            Conversation.Type.STATUS -> TODO()
                                            Conversation.Type.CHAT -> TODO()
                                        }
                                    }
                                }
                            }
                            42.sy()
                        }
                    }

                    SearchBar(
                        modifier = Modifier
                            .padding(end = 11.dep(), start = 18.dep())
                            .fillMaxWidth()
                            .layoutId("searchBar"),
                        onFilter = {
                            notifier.notify(DataIds.filter)
                        }
                    )

                    Box(
                        modifier = Modifier
                            .padding(start = 18.dep())
                            .layoutId("tabs")
                    ) {
                        GroupChatTabs_m5q49j()
                    }

                    TopBar(
                        modifier = Modifier
                            .layoutId("topBar"),
                    )

                    Message(
                        modifier = Modifier
                            .layoutId("bottomBar")
                            .padding(
                                start = 19.dep()
                            )
                            .fillMaxWidth(),
                        scope = scope,
                        sheetState = sheetState
                    )

                    Box(
                        modifier = Modifier
                            .layoutId("menuOverlay")
                            .size(24.dep())
                            .clip(CircleShape)
                            .clickable(
                                rippleRadius = 20.dep(),
                                enabled = true,
                                onClick = {

                                },
                                rippleColor = Bluish
                            )
                    )
                }
            }
        }
    }
}

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    notifier: NotificationService = notifier()
) {
    Row(
        modifier = modifier
            .padding(top = 13.dep(), start = 9.dep(), end = 17.dep())
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .size(24.dep())
                .clip(CircleShape)
                .clickable {
                    notifier.notify(DataIds.back)
                }
                .padding(6.dep()),
            painter = painterResource(id = R.drawable.ic_left_arrow),
            tint = Color.White,
            contentDescription = "left arrow"
        )

        4.sx()

        FontFamilyText(
            text = stringResource(R.string.split_group),
            fontSize = 14.sep(),
            fontWeight = FontWeight.Bold,
            color = White,
            letterSpacing = (-0.333333).sep()
        )

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            modifier = Modifier.size(18.dep()),
            painter = painterResource(id = R.drawable.ic_heart),
            tint = Color.White,
            contentDescription = "heart icon"
        )

        13.sx()

        Icon(
            modifier = Modifier
                .size(18.dep()),
            painter = painterResource(id = R.drawable.ic_share),
            tint = Color.White,
            contentDescription = "share icon"
        )
    }
}

@Composable
fun UpperCut(
    modifier: Modifier = Modifier,
    color: Color
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = color,
                shape = RoundedCornerShape(
                    0.dp,
                    0.dp,
                    bottomEnd = 0f.dep(),
                    bottomStart = 48f.dep()
                )
            ),
        contentAlignment = Alignment.BottomCenter
    ) {

    }
}

@Composable
fun BottomCut(
    modifier: Modifier = Modifier,
    color: Color
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(42.dep())
            .background(color)
            .background(
                Color.White,
                RoundedCornerShape(
                    topEnd = 48.dep(),

                    )
            )
    ){

    }
}

fun minMaxRangeValue(percentage: Float, max: Float, min: Float) : Float {
    return (percentage * (max - min) / 100) + min
}
val CircleButtonShadow = Color(0x0756924f)
@Composable
fun SingleButton(
    modifier: Modifier = Modifier,
    painter: Painter,
    onClick: () -> Unit,
    backgroundColor: Color = White,
    progress: Float = 0f
) {
    Box(
        modifier = modifier
            .coloredShadow(
                color = CircleButtonShadow.copy(alpha = 0.31f),
                borderRadius = 50.dep(),
                blurRadius = 6.dep(),
                offsetX = 0.dep(),
                offsetY = 3.dep(),
            )
            .size(
                minMaxRangeValue(
                    percentage = (1 - progress) * 100,
                    max = 47f,
                    min = 4f
                ).dep()
            )
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier
                .width(16.dep())
                .scale(1 - progress)
                .alpha(1 - progress),
            painter = painter,
            tint = Color.Unspecified,
            contentDescription = "button section",
        )
    }
}

data class GroupChatTabsConfiguration(
    val horizontalSpace: Float = 11f
)

enum class GroupChatTab {
    All,
    Pending,
    Settled
}

@Composable
fun GroupChatTabs_m5q49j(config: GroupChatTabsConfiguration = GroupChatTabsConfiguration()) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(config.horizontalSpace.dep())
    ) {
        val selectedTab = remember {
            mutableStateOf(GroupChatTab.All)
        }

        GroupChatTabItem_yb6b5a(
            selected = selectedTab.value,
            text = "All",
            current = GroupChatTab.All,
            contentDescription = "tab",
            onClick = { selectedTab.value = GroupChatTab.All }
        )

        GroupChatTabItem_yb6b5a(
            selected = selectedTab.value,
            text = "Pending",
            current = GroupChatTab.Pending,
            contentDescription = "tab",
            onClick = { selectedTab.value = GroupChatTab.Pending }
        )

        GroupChatTabItem_yb6b5a(
            selected = selectedTab.value,
            text = "Settled",
            current = GroupChatTab.Settled,
            contentDescription = "tab",
            onClick = { selectedTab.value = GroupChatTab.Settled }
        )
    }
}

data class GroupChatTabItemConfiguration(
    val minWidth: Float = 48f,
    val roundedCorner: Float = 13.5f,
    val selectedBackground: Color = Bluish,
    val unSelectedBackground: Color = LightBlue1,
    val paddingHorizontal: Float = 8f,
    val paddingVertical: Float = 7f,
    val textSize: Float = 11f,
    val selectedTextColor: Color = Color.White,
    val unSelectedTextColor: Color = Bluish
)

@Composable
fun GroupChatTabItem_yb6b5a(
    text: String,
    selected: GroupChatTab,
    current: GroupChatTab,
    contentDescription: String,
    config: GroupChatTabItemConfiguration = GroupChatTabItemConfiguration(),
    onClick: () -> Unit,
) {
    val selectedBackground =
        remember {
            derivedStateOf {
                if (selected == current)
                    config.selectedBackground
                else
                    config.unSelectedBackground
            }
        }

    val selectedTextColor =
        remember {
            derivedStateOf {
                if (selected == current)
                    config.selectedTextColor
                else
                    config.unSelectedTextColor
            }
        }


    Box(
        modifier = Modifier
            .semantics { this.contentDescription = contentDescription }
            .widthIn(config.minWidth.dep())
            .clip(RoundedCornerShape(config.roundedCorner.dep()))
            .background(selectedBackground.value)
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = rememberRipple(bounded = false),
                onClick = {
                    onClick()
                }
            )
            .padding(
                vertical = config.paddingVertical.dep(),
                horizontal = config.paddingHorizontal.dep(),
            ),
        contentAlignment = Alignment.Center
    ) {
        FontFamilyText(
            text = text,
            fontSize = config.textSize.sep(),
            color = selectedTextColor.value,
            lineHeight = 13.sep(),
            letterSpacing = 0.166667.sep()
        )
    }
}
////////
val PinkShadow = Color(0x4FFF4077)
val Blackish = Color(0x1A000000)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Message(
    modifier: Modifier = Modifier,
    scope: CoroutineScope,
    sheetState: ModalBottomSheetState,
    chatMessage: String = stringState(key = DataIds.chatMessage).value,
    notifier: NotificationService = notifier()
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Top,
    ) {
        FloatingActionButton(
            shape = CircleShape,
            backgroundColor = Pink,
            modifier = Modifier
                .size(47.dep())
                .coloredShadow(
                    color = PinkShadow,
                    offsetX = 0.dep(),
                    offsetY = 3.dep(),
                    blurRadius = 6.dep(),
                    borderRadius = 100.dep()
                ),
            onClick = {
                scope.launch {
                    if (!sheetState.isVisible)
                        sheetState.show()
                    else
                        sheetState.hide()
                }
            }) {
            Icon(
                tint = White,
                painter = painterResource(id = R.drawable.ic_fab_icon),
                contentDescription = "fab icon"
            )
        }

        9.sx()

        BasicTextField(
            textStyle = TextStyle(
                fontSize = 12.sep(),
                color = Color.Black
            ),
            singleLine = true,
            maxLines = 1,
            value = chatMessage,
            onValueChange = {
                notifier.notify(DataIds.chatMessage, it)
            },
            decorationBox = {innerTextField->
                Box(
                    Modifier
                        .padding(end = 18.dep(), bottom = 14.dep())
                        .fillMaxWidth()
                        .height(49.dep())
                        .coloredShadow(
                            color = Blackish,
                            offsetX = 0.dep(),
                            offsetY = 4.dep(),
                            blurRadius = 19.dep(),
                            borderRadius = 25.dep()
                        )
                        .background(Color.White, CircleShape)
                ){
                    Row(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxWidth()
                            .padding(start = 18.dep())
                            .wrapContentHeight(),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        ){
                            innerTextField()
                        }
                        6.sx()
                        Icon(
                            modifier = Modifier
                                .padding(end = 14.dep())
                                .size(24.dep())
                                .clip(CircleShape)
                                .clickable {

                                }
                                .padding(4.dep()),
                            painter = painterResource(id = R.drawable.ic_send_icon),
                            tint = Grey2,
                            contentDescription = "send"
                        )
                    }
                    if(chatMessage.isEmpty()){
                        Text(
                            "Message",
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(horizontal = 20.dep()),
                            fontSize = 12.sep(),
                            color = Color(0xff839BB9)
                        )
                    }

                }
            }
        )
    }
}