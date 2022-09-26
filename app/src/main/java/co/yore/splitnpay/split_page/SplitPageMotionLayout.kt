package co.yore.splitnpay.split_page

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.*
import co.yore.splitnpay.R
import co.yore.splitnpay.addmembers.PeopleCard_eq3k8h
import co.yore.splitnpay.components.components.*
import co.yore.splitnpay.components.configuration.*
import co.yore.splitnpay.demos.sx
import co.yore.splitnpay.demos.sy
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.locals.RobotoText
import co.yore.splitnpay.locals.localCurrency
import co.yore.splitnpay.models.ContactData
import co.yore.splitnpay.models.DataIds
import co.yore.splitnpay.models.GroupData
import co.yore.splitnpay.models.GroupOrContact
import co.yore.splitnpay.pages.NothingFoundUI
import co.yore.splitnpay.split_page.you_will_get_pay_card.YouWillGetPayCard
import co.yore.splitnpay.ui.theme.robotoFonts
import co.yore.splitnpay.viewModels.SplitPageState

@Composable
fun JsonConstraintSetStart() = ConstraintSet(
    """ 
{
	poster: { 
		width: "spread",
		start: ['parent', 'start', 0],
		end: ['parent', 'end', 0],
		top: ['parent', 'top', 0],
	},
	tabs: { 
		width: "spread",
		start: ['parent', 'start', 0],
		end: ['parent', 'end', 0],
		top: ['poster', 'bottom', 0],
	},
	content: {
		width: "spread",
        height: "spread",
		start: ['parent', 'start', 0],
		end: ['parent', 'end', 0],
		top: ['tabs', 'bottom', ],
		bottom: ['parent', 'bottom', 0],
	}
} 
"""
)

@Composable
fun JsonConstraintSetEnd(
    dim: Float
) = ConstraintSet(
    """ {
	poster: { 
		width: "spread",
		height: $dim,
		start: ['parent', 'start', 0],
		end: ['parent', 'end', 0],
		top: ['parent', 'top', 0],
	},
    tabs: { 
		width: "spread",
		start: ['parent', 'start', 0],
		end: ['parent', 'end', 0],
		top: ['poster', 'bottom', 0],
	},
	content: {
		width: "spread",
        height: "spread",
		start: ['parent', 'start', 0],
		end: ['parent', 'end', 0],
		top: ['tabs', 'bottom', 0],
		bottom: ['parent', 'bottom', 0],
	}
                  
} """
)

val headerMinHeight = 234f

@OptIn(ExperimentalMotionApi::class)
@Composable
fun SplitPageMotionLayout(
    progress: Float,
    swipe: () -> Unit
) {
    val d = headerMinHeight.dep().value
    MotionLayout(
        start = JsonConstraintSetStart(),
        end = JsonConstraintSetEnd(d),
        progress = progress,
        modifier = Modifier
            .fillMaxSize()
    ) {

        var f by remember { mutableStateOf(1f) }
        LaunchedEffect(key1 = f) {
            if (f == 1f) {
                swipe()
            }
        }
        HeaderAndSearchBar(
            f,
        ) {
            f = it
        }
        val tabsList =
            listOf(ContactTabs.Groups.name, ContactTabs.People.name)
        var selectedIndex by remember { mutableStateOf(0) }

        Tabs(
            selectedIndex,
            tabsList,
        ) {
            selectedIndex = it
        }
        Contents(selectedIndex)
    }
}

@Composable
fun Contents(
    selectedIndex: Int
) {
    Box(
        Modifier
            .layoutId("content")
            .background(Color.White),
        contentAlignment = Alignment.TopCenter
    ) {
        Crossfade(
            targetState = selectedIndex,
            animationSpec = tween(durationMillis = 700)
        ) {
            if(it==0){
                suffix("group"){
                    GroupsChildPage()
                }
            }
            else{
                suffix("people"){
                    PeoplesChildPage()
                }
            }
        }
    }
}

@Composable
fun GroupsChildPage(
    groups: List<GroupOrContact> = listState(DataIds.groupsAndPeoples),
    nogroup: Boolean = boolState(DataIds.noGroup).value
) {
    if(nogroup){
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ){
            NoGroupsContent()
        }
    }
    else{
        if(groups.isEmpty()){
            NothingFoundUI()
        }
        else{
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .padding(
                            start = 18.dep(),
                            bottom = 21.dep()
                        )
                        .height(40.dep()),
                    //horizontalArrangement = Arrangement.spacedBy(11.dep()),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxHeight(),
                        horizontalArrangement = Arrangement.spacedBy(7.dep()),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        GiveTakeTypeTabs()
                    }

                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f))
                    16.sx()
                    AddGroupButton_kbf1at()
                }
                LazyColumn(
                    modifier = Modifier.fadingEdge(),
                    contentPadding = PaddingValues(
                        start = 17.dep(),
                        end = 17.dep(),
                        bottom = 110.dep()
                    )
                ) {
                    items(groups, key = { it.id() }) {
                        if(it is GroupData){
                            GroupCard_0msq1z(
                                modifier = Modifier.padding(top = 13.dep()),
                                contentDescription = "",
                                data = it
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PeoplesChildPage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .padding(
                    start = 18.dep(),
                    bottom = 21.dep()
                )
                .height(40.dep()),
            horizontalArrangement = Arrangement.spacedBy(7.dep()),
            verticalAlignment = Alignment.CenterVertically
        ) {
            GiveTakeTypeTabs()
        }
        FriendsContent()
    }
}

@Composable
fun GiveTakeTypeTabs(
    suffix: String = suffix(),
    selectedTab: SplitPageTabs = tState<SplitPageTabs>("${DataIds.subTab}$suffix").value,
    notifier: NotificationService = notifier(),
) {
    val notificationId by remember(suffix) {
        derivedStateOf {
            "${DataIds.subTab}$suffix"
        }
    }
    SplitTabItem_89keto(
        text = "All",
        selected = selectedTab,
        currentTab = SplitPageTabs.All,
        contentDescription = "all"
    ) {
        notifier.notify(notificationId,SplitPageTabs.All)
    }
    SplitTabItem_89keto(
        text = "You owe",
        selected = selectedTab,
        currentTab = SplitPageTabs.YouOwe,
        contentDescription = "you_owe"
    ) {
        notifier.notify(notificationId,SplitPageTabs.YouOwe)
    }
    SplitTabItem_89keto(
        text = "You are owed",
        selected = selectedTab,
        currentTab = SplitPageTabs.YouAreOwed,
        contentDescription = "you_are_owed"
    ) {
        notifier.notify(notificationId,SplitPageTabs.YouAreOwed)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FriendsContent(
    peoples: List<GroupOrContact> = listState(DataIds.groupsAndPeoples)
) {
    val state = rememberLazyListState()
    LazyColumn(
        state = state,
        modifier = Modifier
            .fillMaxSize()
            .fadingEdge(),
        contentPadding = PaddingValues(
            start = 17.dep(),
            end = 17.dep(),
            bottom = 85.dep()
        ),
        //verticalArrangement = Arrangement.spacedBy(13.dep())
    ) {
        items(
            peoples,
            key = {
                it.id()
            }
        ) {
            if(it is ContactData){
                PeopleCard_eq3k8h(
                    modifier = Modifier
                        .animateItemPlacement()
                        .padding(top = 13.dep()),
                    data = it,
                    contentDescription = "",
                    checkBoxContentDescription = "",
                    profileImageContentDescription = "",
                    config = PeopleCardConfiguration.unCheckable
                )
            }
        }
    }
}

@Composable
fun NoGroupsContent() {
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(58.dep()))
        NoGroupHasBeenCreatedYet()
        13.sy()
        GroupCreationButton()
    }
}



@Composable
fun GroupCreationButton(
    config: GroupCreationButtonConfiguration = GroupCreationButtonConfiguration()
) {
    Button(modifier = Modifier
        .width(config.width.dep())
        .height(config.height.dep()),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = config.backgroundColor
        ),
        onClick = { }) {
        Icon(
            painter = painterResource(id = config.iconId),
            contentDescription = "add group",
            tint = Color.Unspecified,
            modifier = Modifier.size(config.size.dep())
        )
    }
}

@Composable
fun NoGroupHasBeenCreatedYet(
    config: NoGroupHasBeenCreatedYetConfiguration = NoGroupHasBeenCreatedYetConfiguration()
) {
    RobotoText(
        text = stringResource(config.stringId),
        modifier = Modifier,
        textAlign = TextAlign.Center,
        color = config.color,
        fontWeight = FontWeight.Bold,
        fontSize = config.fontSize.sep()
    )
}



@Composable
fun Tabs(
    selectedIndex: Int,
    tabsList: List<String>,
    config: TabsConfiguration = TabsConfiguration(),
    onSelectionChanged: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .layoutId("tabs")
            .height(config.height.dep())
            .background(config.backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        TabRow(
            selectedTabIndex = selectedIndex,
            backgroundColor = config.backgroundColor,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = config.startPadding.dep()),
            indicator = {
                Box {}
            },
            divider = { TabRowDefaults.Divider(color = Color.Transparent) },
        ) {
            tabsList.forEachIndexed { index, text ->
                val computedColor by remember(selectedIndex) {
                    derivedStateOf {
                        if (selectedIndex == index)
                            config.selectedColor
                        else
                            config.color
                    }
                }
                val tabColor by animateColorAsState(
                    targetValue = computedColor,
                    animationSpec = tween(500)
                )
                Text(
                    modifier = Modifier
                        .clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = null
                        ) {
                            onSelectionChanged(index)
                        },
                    text = text,
                    fontSize = config.fontSize.sep(),
                    color = tabColor,
                    fontWeight = FontWeight.Bold,
                    fontFamily = robotoFonts
                )
            }
        }
        /*Divider(
            color = config.dividerColor,
            thickness = config.dividerThickness.dep(),
            modifier = Modifier.align(Alignment.BottomCenter)
        )*/
    }
}

/**/



@Composable
fun HeaderAndSearchBar(
    progress: Float,
    config: HeadersAndSearchBarConfiguration = HeadersAndSearchBarConfiguration(),
    onFactorChanged: (Float) -> Unit
) {
    val _maxHeight = with(LocalDensity.current) {
        config.maxHeight.dep().value * this.density
    }
    val _minHeight = with(LocalDensity.current) {
        config.minHeight.dep().value * this.density
    }
    Box(
        modifier = Modifier
            .layoutId("poster")
            .height(config.height.dep())
            .background(config.color)
            .onGloballyPositioned {
                val dif = _maxHeight - _minHeight
                val diff = it.size.height - _minHeight
                val p = diff / dif
                onFactorChanged(p)
            }
    ) {
        HeaderCutout(
            progress,
        )
        ExpandedCards(progress)
        SearchBar()
    }
}



@Composable
fun BoxScope.SearchBar(
    config: SearchBarConfiguration = SearchBarConfiguration()
) {
    Box(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(
                horizontal = config.horizontalPadding.dep()
            )
    ) {
        ContactSearchBar(
            contentDescription = "split_search"
        )
    }
}



@Composable
fun BoxScope.ExpandedCards(
    progress: Float,
    config: ExpandedCardsConfiguration = ExpandedCardsConfiguration(),
) {
    if (progress > 0f) {
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = config.bottomPadding.dep())
                .alpha(progress)
        ) {
            YouWillGetPayCard(
                config = YouWillGetPayCardConfig(type = YouWillGetPayCardConfig.Type.GET),
                whole = stringState(DataIds.wholeGet).value,
                decimal = stringState(DataIds.decimalGet).value,
                boolState(DataIds.willGetActive).value
            )
            config.space.sx()
            YouWillGetPayCard(
                config = YouWillGetPayCardConfig(type = YouWillGetPayCardConfig.Type.PAY),
                whole = stringState(DataIds.wholePay).value,
                decimal = stringState(DataIds.decimalPay).value,
                boolState(DataIds.willPayActive).value
            )
        }
    }
}

@Composable
fun HeaderCutout(
    progress: Float,
    splitPageState: SplitPageState = tState<SplitPageState>(DataIds.ultimateState).value
) {
    Column() {
        HeaderUpperCutout(
            progress,
            splitPageState
        )
        HeaderBottomCutout(splitPageState)
    }
}

@Composable
fun <T> rememberArgument(arg: T): T {
    var rArg by remember {
        mutableStateOf(arg)
    }
    LaunchedEffect(key1 = arg) {
        rArg = arg
    }
    return rArg
}

@OptIn(ExperimentalMotionApi::class)
@Composable
fun HeaderUpperCutout(
    progress: Float,
    state: SplitPageState,
    config: HeaderUpperCutoutConfiguration = HeaderUpperCutoutConfiguration()
) {
    val bm = 34.dep()
    MotionLayout(
        start = ConstraintSet {
            val row = createRefFor("row")
            val container = createRefFor("container")

            constrain(row) {
                this.width = Dimension.wrapContent
                this.centerHorizontallyTo(container)
            }
        },
        end = ConstraintSet {
            val row = createRefFor("row")
            val container = createRefFor("container")

            constrain(row) {
                this.width = Dimension.matchParent
                this.centerHorizontallyTo(container)
                this.bottom.linkTo(container.bottom, bm)
            }
        },
        progress = (1f - progress),
    ) {
        val computedCurveHeight by remember(key1 = progress) {
            derivedStateOf {
                config.curveHeightConstant + config.curveHeightVariable * progress
            }
        }
        HeaderUpperCutShape(
            state,
            computedCurveHeight,
            config.curveRadius
        ) {
            HeaderBackAndSplit(
                contentDescription = "header_back_and_split"
            ) {

            }
        }

        HeaderContentAndCards(
            progress,
        )
    }
}

@Composable
fun HeaderBottomCutout(
    state: SplitPageState,
    config: HeaderBottomCutoutConfiguration = HeaderBottomCutoutConfiguration()
) {
    val computedBackgroundColor by remember {
        derivedStateOf {
            when (state) {
                SplitPageState.GET -> config.getColor
                SplitPageState.PAY -> config.payColor
                SplitPageState.NONE -> config.noneColor
            }
        }
    }
    val animatedBackgroundColor by animateColorAsState(
        targetValue = computedBackgroundColor,
        animationSpec = tween(700)
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(animatedBackgroundColor)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clip(
                    RoundedCornerShape(
                        0.dp, 47.dep(), 0.dp, 0.dp
                    )
                )
                .background(Color.White)
        ) {}
    }
}

@Composable
fun HeaderContentAndCards(
    progress: Float,
    config: HeaderContentAndCardsConfiguration = HeaderContentAndCardsConfiguration()
) {
    Row(
        modifier = Modifier
            .layoutId("row")
            .padding(top = config.headerTopPadding.dep()),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        HeaderContentWithSpace(
            progress,
        )
        CollapsedCards(
            progress,
        )
    }
}



@Composable
fun CollapsedCards(
    progress: Float,
    config: CollapsedCardsSectionConfiguration = CollapsedCardsSectionConfiguration()
) {
    Column(
        modifier = Modifier
            .padding(end = (config.endPadding * (1f - progress)).dep())
            .alpha(1f - progress),
        horizontalAlignment = Alignment.End
    ) {
        YouWillGetPayCollapsedCard(
            progress,
            whole = stringState(DataIds.wholeGet).value,
            decimal = stringState(DataIds.decimalGet).value,
            config = YouWillGetPayCollapsedCardConfiguration.get
        )
        (config.space * (1f - progress)).sy()
        YouWillGetPayCollapsedCard(
            progress,
            whole = stringState(DataIds.wholePay).value,
            decimal = stringState(DataIds.decimalPay).value,
            config = YouWillGetPayCollapsedCardConfiguration.pay
        )
    }
}

data class YouWillGetPayCollapsedCardConfiguration(
    val textId: Int,
    val amountColor: Color = Color(0xffFF4077),
    val height: Float = 32f,
    val borderRadius: Float = 50f,
    val backgroundColor: Color = Color.White,
    val currencyFontSize: Float = 12f,
    val wholeFontSize: Float = 20f,
    val decimalFontSize: Float = 12f,
    val fontSize: Float = 11f,
    val horizontalPadding: Float = 12f,
    val textColor: Color = Color(0xff839BB9),
    val space: Float = 12f,
) {
    companion object {
        val pay
            get() = YouWillGetPayCollapsedCardConfiguration(
                textId = R.string.you_will_pay,
                amountColor = Color(0xffFF4077)
            )
        val get
            get() = YouWillGetPayCollapsedCardConfiguration(
                textId = R.string.you_will_get,
                amountColor = Color(0xff37D8CF)
            )
    }
}

@Composable
fun YouWillGetPayCollapsedCard(
    progress: Float,
    whole: String,
    decimal: String,
    config: YouWillGetPayCollapsedCardConfiguration
) {
    Box(
        modifier = Modifier
            .height((config.height * (1f - progress)).dep())
            .clip(RoundedCornerShape((config.borderRadius * (1f - progress)).dep()))
            .background(config.backgroundColor),
        contentAlignment = Alignment.Center
    ) {

        val currencyScript = SpanStyle(
            baselineShift = BaselineShift.None,
            fontSize = (config.currencyFontSize * (1f - progress)).sep()
        )

        val decimalScript = SpanStyle(
            baselineShift = BaselineShift.None,
            fontSize = (config.decimalFontSize * (1f - progress)).sep(),
            fontWeight = FontWeight.Normal
        )
        Row(
            verticalAlignment = Alignment
                .CenterVertically,
            modifier = Modifier.padding(horizontal = (config.horizontalPadding * (1f - progress)).dep())
        ) {
            Text(
                stringResource(id = config.textId),
                fontSize = (config.fontSize * (1f - progress)).sep(),
                color = config.textColor
            )
            (config.space * (1f - progress)).sx()
            Text(
                text = buildAnnotatedString {
                    withStyle(currencyScript) {
                        append(localCurrency.current)
                        append(" ")
                    }
                    append(whole)
                    withStyle(decimalScript) {
                        append(".")
                        append(decimal)
                    }
                },
                fontSize = (config.wholeFontSize * (1f - progress)).sep(),
                color = config.amountColor,
                fontFamily = robotoFonts,
                fontWeight = FontWeight.Bold
            )
        }
    }
}



@Composable
fun HeaderContentWithSpace(
    progress: Float,
    config: HeaderContentRightSpaceConfiguration = HeaderContentRightSpaceConfiguration(),
) {
    Row {
        (config.rightSpace * (1f - progress)).sx()
        HeaderContent()
    }
}



@Composable
fun HeaderContent(
    config: HeaderContentConfiguration = HeaderContentConfiguration()
) {
    Column(
        modifier = Modifier
            .padding(top = config.topPadding.dep())
            .wrapContentWidth()
    ) {
        SplitBalanceText(
            config.splitBalanceTextId,
            config.splitBalanceFontSize,
            config.splitBalanceColor
        )

        YoreAmount(
            config = YoreAmountConfiguration.splitPageHeadContent,
            whole = stringState(DataIds.whole).value,
            decimal = stringState(DataIds.decimal).value,
        )
    }
}

@Composable
fun SplitBalanceText(
    splitTextId: Int,
    fontSize: Float,
    color: Color
) {
    RobotoText(
        stringResource(splitTextId),
        fontSize = fontSize.sep(),
        color = color,
    )
}

@Composable
fun HeaderUpperCutShape(
    state: SplitPageState,
    curveHeight: Number,
    curveRadius: Number,
    config: HeaderUpperCutShapeConfiguration = HeaderUpperCutShapeConfiguration(),
    content: @Composable BoxScope.() -> Unit
) {
    val computedBackgroundColor by remember {
        derivedStateOf {
            when (state) {
                SplitPageState.GET -> config.getColor
                SplitPageState.PAY -> config.payColor
                SplitPageState.NONE -> config.noneColor
            }
        }
    }
    val animatedBackgroundColor by animateColorAsState(
        targetValue = computedBackgroundColor,
        animationSpec = tween(700)
    )
    Box(
        modifier = Modifier
            .layoutId("container")
            .fillMaxWidth()
            .height(curveHeight.dep())
            .clip(
                RoundedCornerShape(
                    0.dp, 0.dp, 0.dp, curveRadius.dep()
                )
            )
            .background(animatedBackgroundColor)
    ) {
        content()
    }
}



@Composable
fun HeaderBackAndSplit(
    modifier: Modifier = Modifier,
    contentDescription: String,
    config: HeaderBackAndSplitConfiguration = HeaderBackAndSplitConfiguration(),
    onBackClick: () -> Unit
) {
    Row(
        modifier = modifier
            .semantics {
                this.contentDescription = contentDescription
            }
    ) {
        val ns = LocalNotificationService.current
        BackButton(
            contentDescription = "split_back_button"
        ) {
            ns.notify(0, null)
            onBackClick()
        }
        config.space.sx()
        RobotoText(
            stringResource(config.splitTextId),
            fontSize = config.splitFontSize.sep(),
            fontWeight = FontWeight.Bold,
            color = config.tint
        )
    }
}