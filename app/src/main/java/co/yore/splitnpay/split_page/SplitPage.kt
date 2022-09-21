package co.yore.splitnpay.split_page

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Velocity
import androidx.constraintlayout.compose.*
import co.yore.splitnpay.*
import co.yore.splitnpay.R
import co.yore.splitnpay.split_page.you_will_get_pay_card.YouWillGetPayCard
import co.yore.splitnpay.split_page.you_will_get_pay_card.YouWillGetPayCardConfig

enum class SwipingStates {
    EXPANDED,
    COLLAPSED
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SplitPage1() {
    val swipingState = rememberSwipeableState(initialValue = SwipingStates.EXPANDED)
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val heightInPx = with(LocalDensity.current) { maxHeight.toPx() } // Get height of screen
        val connection = remember {
            object : NestedScrollConnection {
                override fun onPreScroll(
                    available: Offset,
                    source: NestedScrollSource
                ): Offset {
                    val delta = available.y
                    return if (delta < 0) {
                        swipingState.performDrag(delta).toOffset()
                    } else {
                        Offset.Zero
                    }
                }

                override fun onPostScroll(
                    consumed: Offset,
                    available: Offset,
                    source: NestedScrollSource
                ): Offset {
                    val delta = available.y
                    return swipingState.performDrag(delta).toOffset()
                }

                override suspend fun onPostFling(
                    consumed: Velocity,
                    available: Velocity
                ): Velocity {
                    swipingState.performFling(velocity = available.y)
                    return super.onPostFling(consumed, available)
                }

                private fun Float.toOffset() = Offset(0f, this)
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .swipeable(
                    state = swipingState,
                    thresholds = { _, _ -> FractionalThreshold(0.05f) },
                    orientation = Orientation.Vertical,
                    anchors = mapOf(
                        0f to SwipingStates.COLLAPSED,
                        heightInPx to SwipingStates.EXPANDED,
                    )
                )
                .nestedScroll(connection)
        ) {
            val computedProgress by remember {
                derivedStateOf {
                    if (swipingState.progress.to == SwipingStates.COLLAPSED)
                        swipingState.progress.fraction
                    else
                        1f - swipingState.progress.fraction
                }
            }
                SplitPageMotionLayout(
                    progress = computedProgress,
                ) {
                    swipingState.performDrag(-0.01f)
                }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMotionApi::class)
@Composable
fun SplitPage() {
    val swipingState = rememberSwipeableState(initialValue = SwipingStates.EXPANDED)
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val heightInPx = with(LocalDensity.current) { maxHeight.toPx() }
        val connection = remember {
            object : NestedScrollConnection {
                override fun onPreScroll(
                    available: Offset,
                    source: NestedScrollSource
                ): Offset {
                    val delta = available.y
                    return if (delta < 0) {
                        swipingState.performDrag(delta).toOffset()
                    } else {
                        Offset.Zero
                    }
                }

                override fun onPostScroll(
                    consumed: Offset,
                    available: Offset,
                    source: NestedScrollSource
                ): Offset {
                    val delta = available.y
                    return swipingState.performDrag(delta).toOffset()
                }

                override suspend fun onPostFling(
                    consumed: Velocity,
                    available: Velocity
                ): Velocity {
                    swipingState.performFling(velocity = available.y)
                    return super.onPostFling(consumed, available)
                }

                private fun Float.toOffset() = Offset(0f, this)
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .swipeable(
                    state = swipingState,
                    thresholds = { _, _ -> FractionalThreshold(0.05f) },
                    orientation = Orientation.Vertical,
                    anchors = mapOf(
                        0f to SwipingStates.COLLAPSED,
                        heightInPx to SwipingStates.EXPANDED,
                    )
                )
                .nestedScroll(connection)
        ) {
            val computedProgress by remember {
                derivedStateOf {
                    if (swipingState.progress.to == SwipingStates.COLLAPSED)
                        swipingState.progress.fraction
                    else
                        1f - swipingState.progress.fraction
                }
            }
            val _219_dep = 219.dep()
            val _159_dep = 159.dep()
            val _106_dep = 106.dep()
            val _31_dep = 31.dep()
            val _32_dep = 32.dep()
            val _34_dep = 34.dep()
            val _38_dep = 38.dep()
            val _153_dep = 153.dep()
            val _149_dep = 149.dep()
            val _22_dep = 22.dep()
            val _12_dep = 12.dep()
            val _6_dep = 6.dep()
            val _16_dep = 16.dep()
            MotionLayout(
                progress = computedProgress,
                start = ConstraintSet {
                    val upper_cut = createRefFor("upper_cut")
                    val bottom_cut = createRefFor("bottom_cut")
                    val search_bar = createRefFor("search_bar")
                    val expanded_cards = createRefFor("expanded_cards")
                    val header_content = createRefFor("header_content")
                    val collapsed_cards = createRefFor("collapsed_cards")
                    val content = createRefFor("content")
                    val tabs = createRefFor("tabs")
                    val get_card = createRefFor("get_card")
                    val you_will_get_text = createRefFor("you_will_get_text")
                    val get_amount = createRefFor("get_amount")
                    constrain(upper_cut){
                        height = Dimension.value(_219_dep)
                    }
                    constrain(bottom_cut){
                        top.linkTo(upper_cut.bottom)
                    }
                    constrain(search_bar){
                        top.linkTo(bottom_cut.bottom,_106_dep)
                    }
                    constrain(expanded_cards){
                        bottom.linkTo(search_bar.top,_34_dep)
                    }
                    constrain(header_content){
                        centerTo(upper_cut)
                    }
                    constrain(collapsed_cards){
                        centerVerticallyTo(header_content)
                        end.linkTo(upper_cut.end)
                    }
                    constrain(tabs){
                        top.linkTo(search_bar.bottom)
                    }
                    constrain(content){
                        top.linkTo(tabs.bottom)
                    }
                    constrain(get_card){
                        width = Dimension.value(_153_dep)
                        height = Dimension.value(_149_dep)
                        bottom.linkTo(search_bar.top,_34_dep)
                        start.linkTo(upper_cut.start,_16_dep)
                    }
                    constrain(you_will_get_text){
                        centerHorizontallyTo(get_card)
                        top.linkTo(get_card.top,_22_dep)
                    }
                    constrain(get_amount){
                        end.linkTo(you_will_get_text.end)
                        top.linkTo(you_will_get_text.bottom,_6_dep)
                    }
                },
                end = ConstraintSet {
                    val upper_cut = createRefFor("upper_cut")
                    val bottom_cut = createRefFor("bottom_cut")
                    val search_bar = createRefFor("search_bar")
                    val header_content = createRefFor("header_content")
                    val collapsed_cards = createRefFor("collapsed_cards")
                    val tabs = createRefFor("tabs")
                    val content = createRefFor("content")
                    val get_card = createRefFor("get_card")
                    val you_will_get_text = createRefFor("you_will_get_text")
                    val get_amount = createRefFor("get_amount")
                    constrain(upper_cut){
                        height = Dimension.value(_159_dep)
                        width = Dimension.matchParent
                    }
                    constrain(bottom_cut){
                        top.linkTo(upper_cut.bottom)
                    }
                    constrain(search_bar){
                        top.linkTo(upper_cut.bottom,_31_dep)
                    }
                    constrain(header_content){
                        centerVerticallyTo(upper_cut)
                        start.linkTo(upper_cut.start,_38_dep)
                    }
                    constrain(collapsed_cards){
                        centerVerticallyTo(header_content)
                        end.linkTo(upper_cut.end)
                    }
                    constrain(tabs){
                        top.linkTo(search_bar.bottom)
                    }
                    constrain(content){
                        top.linkTo(tabs.bottom)
                    }
                    constrain(get_card){
                        height = Dimension.value(_32_dep)
                        start.linkTo(you_will_get_text.start,-_12_dep)
                        width = Dimension.fillToConstraints
                        end.linkTo(get_amount.end,-_12_dep)
                        //end.linkTo(upper_cut.end,_16_dep)
                    }
                    constrain(you_will_get_text){
                        centerVerticallyTo(get_card)
                        //start.linkTo(get_card.start,_12_dep)
                        end.linkTo(get_amount.start,_12_dep)
                    }
                    constrain(get_amount){
                        //end.linkTo(get_card.end,_12_dep)
                        //centerVerticallyTo(get_card)
                        start.linkTo(you_will_get_text.end,_12_dep)
                    }
                },
                modifier = Modifier.fillMaxSize()
            ) {
                UpperCut()
                HeaderContentSection()
                //CollapsedCardsSection(1f-computedProgress)
                BottomCut()
                //ExpandedCardsSection(1f-computedProgress)
                MyCard(computedProgress)
                SearchBarSection()
                var selectedIndex by remember { mutableStateOf(0) }
                TabsSection(selectedIndex){
                    selectedIndex = it
                }
                ContentSection(selectedIndex)
            }
            Text(computedProgress.toString())
        }
    }
}

/*val shadowColor: Color = Color(0x80C6CFD8),
val shadowBlurRadius: Float = 33f,
val shadowOffset: Float = 7f,
val borderRadius: Float = 15f,
val shadowSpread: Float = 0f,*/

@OptIn(ExperimentalMotionApi::class)
@Composable
fun MyCard(
    progress: Float
) {
    Box(
        modifier = Modifier
            .layoutId("get_card")
            .coloredShadow(
                color = Color(0x80C6CFD8),
                borderRadius = 15.dep(),
                blurRadius = 33.dep(),
                spread = 0f,
                offsetX = 7.dep(),
                offsetY = 7.dep(),
            )
            .background(Color.White, RoundedCornerShape(15.dep()))
    ){

    }
    RobotoText(
        "You'll get",
        fontWeight = FontWeight((700 - 300*progress).toInt()),
        modifier = Modifier
            .layoutId("you_will_get_text"),
        color = Color(0xff243257)
    )
    Box(
        modifier = Modifier
            .layoutId("get_amount")
    ){
        YoreAmount(
            config = YoreAmountConfiguration(
                color = Color(0xff859DBA),
                fontFamily = robotoFonts,
                fontSize = 20,
                currencyFontSize = 12,
                decimalFontSize = 12,
                wholeFontWeight = FontWeight.Bold
            ),
            whole = "00",
            decimal = "00"
        )
    }
}

@Composable
fun ContentSection(
    selectedIndex: Int
) {
    Box(
        modifier = Modifier
            .layoutId("content")
    ){
        Contents(selectedIndex)
    }
}

@Composable
fun TabsSection(
    selectedIndex: Int,
    onIndexChanged: (Int)->Unit
) {
    val tabsList =
        listOf(ContactTabs.Groups.name, ContactTabs.Friends.name)

    Box(
        modifier = Modifier
            .layoutId("tabs")
    ){
        Tabs(
            selectedIndex,
            tabsList,
        ) {
            onIndexChanged(it)
        }
    }
}

@Composable
fun CollapsedCardsSection(
    progress: Float
) {
    Column(
        modifier = Modifier
            .layoutId("collapsed_cards")
            .padding(end = 16.dep())
            .alpha(1f - progress),
        horizontalAlignment = Alignment.End
    ) {
        YouWillGetPayCollapsedCard(
            progress,
            whole = stringState(SplitDataIds.wholeGet).value,
            decimal = stringState(SplitDataIds.decimalGet).value,
            config = YouWillGetPayCollapsedCardConfiguration.get
        )
        (8f * (1f - progress)).sy()
        YouWillGetPayCollapsedCard(
            progress,
            whole = stringState(SplitDataIds.wholePay).value,
            decimal = stringState(SplitDataIds.decimalPay).value,
            config = YouWillGetPayCollapsedCardConfiguration.pay
        )
    }
}

@Composable
fun HeaderContentSection() {
    Column(
        modifier = Modifier
            .layoutId("header_content")
            .wrapContentWidth()
    ) {
        SplitBalanceText(
            R.string.split_balance,
            12f,
            Color.White
        )

        YoreAmount(
            config = YoreAmountConfiguration.splitPageHeadContent,
            whole = stringState(SplitDataIds.whole).value,
            decimal = stringState(SplitDataIds.decimal).value,
        )
    }
}

@Composable
fun ExpandedCardsSection(
    progress: Float
){
    Box(
        modifier = Modifier
            .layoutId("expanded_cards")
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ){
        if (progress > 0f) {
            Row(
                modifier = Modifier
                    .alpha(progress)
            ) {
                YouWillGetPayCard(
                    config = YouWillGetPayCardConfig(type = YouWillGetPayCardConfig.Type.GET),
                    whole = stringState(SplitDataIds.wholeGet).value,
                    decimal = stringState(SplitDataIds.decimalGet).value,
                    boolState(SplitDataIds.willGetActive).value
                )
                22.sx()
                YouWillGetPayCard(
                    config = YouWillGetPayCardConfig(type = YouWillGetPayCardConfig.Type.PAY),
                    whole = stringState(SplitDataIds.wholePay).value,
                    decimal = stringState(SplitDataIds.decimalPay).value,
                    boolState(SplitDataIds.willPayActive).value
                )
            }
        }
    }
}

@Composable
fun SearchBarSection() {
    Box(
        modifier = Modifier
            .layoutId("search_bar")
            .padding(horizontal = 16.dep())
    ) {
        ContactSearchBar(
            contentDescription = "split_search"
        ) {

        }
    }
}

@Composable
fun BottomCut() {
    Box(
        modifier = Modifier
            .layoutId("bottom_cut")
            .fillMaxWidth()
            .height(42.dep())
            .background(Color(0xff839BB9))
            .background(
                Color.White,
                RoundedCornerShape(
                    topEnd = 48.dep(),

                    )
            )
    ){

    }
}

@Composable
fun UpperCut() {
    Box(
        modifier = Modifier
            .layoutId("upper_cut")
            .fillMaxWidth()
            .height(219.dep())
            .background(
                Color(0xff839BB9),
                RoundedCornerShape(
                    bottomStart = 48.dep(),

                    )
            )
    ){

    }
}
