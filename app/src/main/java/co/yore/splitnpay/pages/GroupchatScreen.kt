package co.yore.splitnpay.pages

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
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
import co.yore.splitnpay.split_page_components.BottomCut
import co.yore.splitnpay.split_page_components.UpperCut
import co.yore.splitnpay.ui.theme.Bluish
import co.yore.splitnpay.ui.theme.LightBlue4
import co.yore.splitnpay.ui.theme.LightGreen3
import co.yore.splitnpay.ui.theme.Whitish
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.launch

@SuppressLint("Range")
@OptIn(ExperimentalMaterialApi::class, ExperimentalMotionApi::class)
@Composable
fun GroupChatScreen(
    modifier: Modifier = Modifier,
    transactions: List<BillTransaction> = listState(key = DataIds.transactions),
    groupNameString: String = stringState(key = DataIds.groupName).value,
    groupAmountString: Float = floatState(key = DataIds.groupAmount).value,
    groupImageString: String = stringState(key = DataIds.groupImage).value,
    groupCreationDateString: String = stringState(key = DataIds.groupCreationDate).value,
    isSingleChat: Boolean = boolState(key = DataIds.isSingleChat).value,
    filteredMemberName: String = stringState(key = DataIds.filteredMemberName).value,
    filteredMemberImage: String = stringState(key = DataIds.filteredMemberImage).value,
    notifier: NotificationService = notifier()
) {
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
        confirmStateChange = { false })
    val scope = rememberCoroutineScope()
    val sheet = remember { mutableStateOf(true) }
    val context = LocalContext.current
    val motionSceneContent = remember {
        context.resources.openRawResource(R.raw.motion_scene).readBytes().decodeToString()
    }
    val lazyScrollState = rememberLazyListState()
    val dep = 1.dep()

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            if (sheet.value) {
                BillTotalBottomSheet(
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                FilterBottomSheet()
            }
        },
        scrimColor = SheetScrim,
        sheetBackgroundColor = White,
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
                                color = CircleImageShadow,
                                offsetX = 0.dep(),
                                offsetY = 2.25.dep(),
                                blurRadius = 4.5.dep(),
                                borderRadius = 100.dep(),
                                spread = 0f
                            )
                            .size(51.dep())
                            .border(
                                width = 2.25.dep(),
                                color = White,
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
                        color = White,
                        letterSpacing = 0.166667.sep()
                    )

                    FontFamilyText(
                        modifier = modifier.layoutId("amount"),
                        annotatedString = groupAmountString.amountAnnotatedString(
                            wholeNumberTextColor = White,
                            decNumberTextColor = White,
                            currencyTextColor = White,
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
//                                navController.navigate("split_card_details_screen")
                            notifier.notify(DataIds.settleClick)
                        },
                        backgroundColor = Color.Companion.blend(
                            color1 = Yellow,
                            color2 = White,
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
                                                color = White01,
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
                                                color = White
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
                            FontFamilyText(
                                modifier = Modifier.align(CenterHorizontally),
                                text = groupCreationDateString.toString(),
                                fontSize = 14.sep(),
                                color = Bluish
                            )
                            if (transactions.isEmpty()) {
                                8.sy()
                                FontFamilyText(
                                    modifier = Modifier.align(CenterHorizontally),
                                    annotatedString = buildAnnotatedString {
                                        withStyle(
                                            style = SpanStyle(
                                                fontSize = 13.sep(),
                                                color = LightBlue5
                                            ),
                                        ) {
                                            append(stringResource(R.string.you_created_group))
                                        }
                                        withStyle(
                                            style = SpanStyle(
                                                fontSize = 13.sep(),
                                                fontWeight = FontWeight.Bold,
                                                color = LightBlue5
                                            ),
                                        ) {
                                            append(" group name")
                                        }
                                    },
                                    color = LightBlue5,
                                    fontSize = 13.sep()
                                )
                            }
                            20.sy()
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 18.dep())
                            ) {
                                LazyColumn(
                                    state = lazyScrollState
                                ) {
                                    items(transactions) { it ->
                                        BillCard_s10zd7(
                                            transaction = it,
                                            onClick = {
                                                notifier.notify(DataIds.cardClick)
                                            }
                                        )
                                    }
                                }
                            }
                            42.sy()
                        }
                    }

                    SearchBar(
                        modifier = Modifier
                            .padding(horizontal = 18.dep())
                            .fillMaxWidth()
                            .layoutId("searchBar"),
                        onFilter = {
                            sheet.value = !sheet.value
                            scope.launch {
                                sheetState.show()
                            }
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
                                start = 19.dep(),
                                bottom = 14.dep()
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