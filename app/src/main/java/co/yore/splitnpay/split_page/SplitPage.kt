package co.yore.splitnpay.split_page

import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
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
            val _332_dep = 332.dep()
            val _183_dep = 183.dep()
            val _8_dep = 8.dep()
            val _53_dep = 53.dep()
            val _191_dep = 191.dep()
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
                    val cardm = createRefFor("cardm")
                    val cardm1 = createRefFor("cardm1")
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
                    constrain(cardm){
                        start.linkTo(upper_cut.start,_16_dep)
                        top.linkTo(search_bar.top,-_183_dep)
                    }
                    constrain(cardm1){
                        //end.linkTo(upper_cut.end,_16_dep)
                        top.linkTo(search_bar.top,-_183_dep)
                        start.linkTo(upper_cut.start,_191_dep)
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
                    val cardm = createRefFor("cardm")
                    val cardm1 = createRefFor("cardm1")
                    constrain(upper_cut){
                        height = Dimension.value(_159_dep)
                        width = Dimension.matchParent
                        end.linkTo(get_card.end)
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
                    constrain(cardm){
                        end.linkTo(upper_cut.end)
                        //bottom.linkTo(upper_cut.bottom,_53_dep)
                        top.linkTo(cardm1.bottom,_8_dep)
                    }
                    constrain(cardm1){
                        end.linkTo(upper_cut.end)
                        top.linkTo(upper_cut.top,_53_dep)
                        //bottom.linkTo(cardm.top,_8_dep)
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
                MyCard1(computedProgress)
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
    val _332_dep = 332.dep()
    val _28_dep = 28.dep()
    val _33_dep = 33.dep()
    val _8_dep = 8.dep()
    val _10_dep = 10.dep()
    val textColor by remember(progress) {
        derivedStateOf {
            Color.blend(Color(0xff243257), Color(0xff839BB9),progress)
        }
    }
    MotionLayout(
        start = ConstraintSet {
            val get_card = createRefFor("get_card")
            val text = createRefFor("text")
            val amount = createRefFor("amount")
            val get_icon = createRefFor("get_icon")
            val arrow = createRefFor("arrow")
            constrain(get_card){

                width = Dimension.value(_153_dep)
                height = Dimension.value(_149_dep)
            }
            constrain(text){
                centerHorizontallyTo(get_card)
                top.linkTo(get_card.top,_22_dep)
            }
            constrain(amount){
                centerHorizontallyTo(get_card)
                top.linkTo(text.bottom,_6_dep)
            }
            constrain(get_icon){
                pivotX = 0f
                pivotY = 1f
                start.linkTo(get_card.start,_33_dep)
                bottom.linkTo(get_card.bottom)
            }
            constrain(arrow){
                pivotX = 1f
                pivotY = 1f
                bottom.linkTo(get_card.bottom,_8_dep)
                end.linkTo(get_card.end,_10_dep)
            }
        },
        end = ConstraintSet {
            val get_card = createRefFor("get_card")
            val text = createRefFor("text")
            val amount = createRefFor("amount")
            val get_icon = createRefFor("get_icon")
            val arrow = createRefFor("arrow")
            constrain(get_card){
                height = Dimension.value(_32_dep)
                width = Dimension.fillToConstraints
                start.linkTo(text.start,-_12_dep)
                end.linkTo(amount.end,-_12_dep)
            }
            constrain(text){
                centerVerticallyTo(get_card)
            }
            constrain(amount){
                start.linkTo(text.end,_12_dep)
                centerVerticallyTo(get_card)
            }
            constrain(amount){
                start.linkTo(text.end,_12_dep)
                end.linkTo(parent.end,_28_dep)
                centerVerticallyTo(get_card)
            }
            constrain(get_icon){
                start.linkTo(get_card.start,_33_dep)
                bottom.linkTo(get_card.bottom)
                pivotX = 0f
                pivotY = 1f
                alpha = 0.0f
                scaleX = 0f
                scaleY = 0f
            }
            constrain(arrow){
                bottom.linkTo(get_card.bottom,_8_dep)
                end.linkTo(get_card.end,_10_dep)
                pivotX = 1f
                pivotY = 1f
                alpha = 0.0f
                scaleX = 0f
                scaleY = 0f
            }
        },
        progress = progress,
        modifier = Modifier
            .layoutId("cardm")
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
                .background(
                    Color.White/*.copy(alpha = 0.5f)*/,
                    RoundedCornerShape(15.dep())
                )
        ){

        }
        AnimatedTextContent(
            modifier = Modifier
            .layoutId("text"),
            from = "You’ll get",
            to = "You will get",
            progress = progress,
            spanStyle = SpanStyle(
                fontWeight = FontWeight((700 - 300*progress).toInt()),
                color = textColor,
                fontSize = (16-5*progress).sep()
            )
        )
        Box(
            modifier = Modifier
                .layoutId("amount")
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
        Image(
            painter = painterResource(id = R.drawable.you_will_get_icon),
            contentDescription = "you_will_get_icon",
            modifier = Modifier
                .layoutId("get_icon")
                .size(42.dep(), 68.dep())
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_you_will_get_arrow),
            contentDescription = "",
            tint = Color.Unspecified,
            modifier = Modifier
                .layoutId("arrow")
                .size(26.dep())
        )
    }
}

@OptIn(ExperimentalMotionApi::class)
@Composable
fun MyCard1(
    progress: Float
) {
    var cardSize by remember {
        mutableStateOf(DpSize.Zero)
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
    val _332_dep = 332.dep()
    val _28_dep = 28.dep()
    val _8_dep = 8.dep()
    val _10_dep = 10.dep()
    val _33_dep = 33.dep()
    val _27_dep = 27.dep()
    val density = LocalDensity.current.density

    val textColor by remember(progress) {
        derivedStateOf {
            Color.blend(Color(0xff243257), Color(0xff839BB9),progress)
        }
    }

    MotionLayout(
        start = ConstraintSet {
            val get_card = createRefFor("get_card")
            val text = createRefFor("text")
            val amount = createRefFor("amount")
            val pay_icon = createRefFor("pay_icon")
            val arrow = createRefFor("arrow")
            constrain(get_card){

                width = Dimension.value(_153_dep)
                height = Dimension.value(_149_dep)
            }
            constrain(text){
                centerHorizontallyTo(get_card)
                top.linkTo(get_card.top,_22_dep)
            }
            constrain(amount){
                centerHorizontallyTo(get_card)
                top.linkTo(text.bottom,_6_dep)
            }
            constrain(pay_icon){
                pivotX = 0f
                pivotY = 1f
                start.linkTo(get_card.start,_27_dep)
                bottom.linkTo(get_card.bottom)
            }
            constrain(arrow){
                pivotX = 1f
                pivotY = 1f
                bottom.linkTo(get_card.bottom,_8_dep)
                end.linkTo(get_card.end,_10_dep)
            }
        },
        end = ConstraintSet {
            val get_card = createRefFor("get_card")
            val text = createRefFor("text")
            val amount = createRefFor("amount")
            val pay_icon = createRefFor("pay_icon")
            val arrow = createRefFor("arrow")
            constrain(get_card){
                height = Dimension.value(_32_dep)
                width = Dimension.fillToConstraints
                start.linkTo(text.start,-_12_dep)
                end.linkTo(amount.end,-_12_dep)
            }
            constrain(text){
                centerVerticallyTo(get_card)
            }
            constrain(amount){
                start.linkTo(text.end,_12_dep)
                centerVerticallyTo(get_card)
            }
            constrain(amount){
                start.linkTo(text.end,_12_dep)
                end.linkTo(parent.end,_28_dep)
                centerVerticallyTo(get_card)
            }
            constrain(pay_icon){
                start.linkTo(get_card.start,_33_dep)
                bottom.linkTo(get_card.bottom)
                pivotX = 0f
                pivotY = 1f
                alpha = 0.0f
                scaleX = 0f
                scaleY = 0f
            }
            constrain(arrow){
                bottom.linkTo(get_card.bottom,_8_dep)
                end.linkTo(get_card.end,_10_dep)
                pivotX = 1f
                pivotY = 1f
                alpha = 0.0f
                scaleX = 0f
                scaleY = 0f
            }
        },
        progress = progress,
        modifier = Modifier
            .layoutId("cardm1")
            //.size(cardSize)
            //.background(Color.Red)
        //.clipToBounds()
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
                /*.onGloballyPositioned {
                    cardSize = DpSize((it.size.width/density).dp,(it.size.height/density).dp)
                    it.positionInWindow()
                }*/
                .background(
                    Color.White/*.copy(alpha = 0.5f)*/,
                    RoundedCornerShape((15 * (1f + progress)).dep())
                )
        ){

        }
        /*RobotoText(
            "You'll pay",
            fontWeight = FontWeight((700 - 300*progress).toInt()),
            modifier = Modifier
                .layoutId("text"),
            color = textColor,
            fontSize = (16-5*progress).sep()
        )*/
        AnimatedTextContent(
            modifier = Modifier
                .layoutId("text"),
            items = listOf(
                NonAnimatableText(
                    "You",
                    style = SpanStyle(
                        fontWeight = FontWeight((700 - 300*progress).toInt()),
                        color = textColor,
                        fontSize = (16-5*progress).sep()
                    )
                ),
                AnimatableTextPair(
                    start = "'",
                    end = " wi",
                    style = SpanStyle(
                        fontWeight = FontWeight((700 - 300*progress).toInt()),
                        color = textColor,
                        fontSize = (16-5*progress).sep()
                    )
                ),
                NonAnimatableText(
                    "ll pay",
                    style = SpanStyle(
                        fontWeight = FontWeight((700 - 300*progress).toInt()),
                        color = textColor,
                        fontSize = (16-5*progress).sep()
                    )
                ),
            ),
            progress = progress
        )
        Box(
            modifier = Modifier
                .layoutId("amount")
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
        Image(
            painter = painterResource(id = R.drawable.you_will_pay_hand_icon),
            contentDescription = "you_will_pay_icon",
            modifier = Modifier
                .layoutId("pay_icon")
                .size(46.dep(), 68.dep())
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_you_will_pay_arrow),
            contentDescription = "",
            tint = Color.Unspecified,
            modifier = Modifier
                .layoutId("arrow")
                .size(26.dep())
        )
    }
}

fun Color.Companion.blend(color1: Color, color2: Color, progress: Float): Color{
    val r1 = color1.red
    val g1 = color1.green
    val b1 = color1.blue

    val r2 = color2.red
    val g2 = color2.green
    val b2 = color2.blue

    val p = progress
    val q = 1f - progress
    return Color(
        red = q*r1 + p*r2,
        green = q*g1 + p*g2,
        blue = q*b1 + p*b2,
    )
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
