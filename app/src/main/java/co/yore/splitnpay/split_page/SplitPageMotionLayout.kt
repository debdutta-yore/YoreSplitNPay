package co.yore.splitnpay.split_page

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import co.yore.splitnpay.*
import co.yore.splitnpay.R
import co.yore.splitnpay.friend_item.FriendItem
import co.yore.splitnpay.friend_item.models.Friend
import co.yore.splitnpay.split_page.you_will_get_pay_card.YouWillGetPayCard
import co.yore.splitnpay.split_page.you_will_get_pay_card.YouWillGetPayCardConfig

@Composable
private fun JsonConstraintSetStart() = ConstraintSet(
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
private fun JsonConstraintSetEnd(
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
    wholeGet: String,
    decGet: String,
    wholePay: String,
    decPay: String,
    whole: String,
    decimal: String,
    swipe: ()->Unit
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
        LaunchedEffect(key1 = f){
            if(f==1f){
                swipe()
            }
        }
        HeaderAndSearchBar(
            f,
            wholeGet,
            decGet,
            wholePay,
            decPay,
            whole,
            decimal,
        ){
            f = it
        }
        val tabsList =
            listOf(ContactTabs.Groups.name, ContactTabs.Friends.name)
        var selectedIndex by remember { mutableStateOf(0) }

        Tabs(
            selectedIndex,
            tabsList,
        ){
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
        if (selectedIndex == 0) {
            NoGroupsContent()
        } else {
            FriendsContent()
        }
    }
}

@Composable
fun FriendsContent() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 17.dep()),
    ) {
        items(
            100,
            key = {
                it
            }
        ) {
            FriendItem(
                selected = false,
                friend = Friend(
                    name = "Debdutta Panda $it",
                    mobile = "8967114927",
                    imageUrl = ""
                ),
                contentDescription = "friend_item",
                onClicked = {},
                onPressed = {}
            )
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

data class GroupCreationButtonConfiguration(
    val width: Float = 90f,
    val height: Float = 35f,
    val backgroundColor: Color = Color(0xff839BB9),
    val iconId: Int = R.drawable.ic_add_group,
    val size: Float = 18f
)

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
        onClick = {  }) {
        Icon(
            painter = painterResource(id = config.iconId),
            contentDescription = "add group",
            tint = Color.Unspecified,
            modifier = Modifier.size(config.size.dep())
        )
    }
}

data class NoGroupHasBeenCreatedYetConfiguration(
    val stringId: Int = R.string.no_group_has_been_created_yet,
    val color: Color = Color(0xff839BB9),
    val fontSize: Float = 13f
)

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

data class TabsConfiguration(
    val backgroundColor: Color = Color.White,
    val startPadding: Float = 15f,
    val fontSize: Float = 21f,
    val color: Color = Color(0xffCFD8E4),
    val selectedColor: Color = Color(0xff243257),
    val height: Float = 73f,
    val dividerThickness: Float=1f,
    val dividerColor: Color=Color(0xfffafcff),
)

@Composable
fun Tabs(
    selectedIndex: Int,
    tabsList: List<String>,
    config: TabsConfiguration = TabsConfiguration(),
    onSelectionChanged: (Int)->Unit
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
        Divider(
            color = config.dividerColor,
            thickness = config.dividerThickness.dep(),
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

/**/

data class HeadersAndSearchBarConfiguration(
    val height: Float=411f,
    val maxHeight: Float=411f,
    val minHeight: Float=headerMinHeight,
    val color: Color=Color.White,
)

@Composable
fun HeaderAndSearchBar(
    progress: Float,
    wholeGet: String,
    decGet: String,
    wholePay: String,
    decPay: String,
    whole: String,
    decimal: String,
    config: HeadersAndSearchBarConfiguration = HeadersAndSearchBarConfiguration(),
    onFactorChanged: (Float)->Unit
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
            wholeGet,
            decGet,
            wholePay,
            decPay,
            whole,
            decimal,
        )
        ExpandedCards(progress)
        SearchBar()
    }
}

data class SearchBarConfiguration(
    val horizontalPadding: Float = 16f
)

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
        ){

        }
    }
}

@Composable
fun BoxScope.ExpandedCards(
    progress: Float
) {
    Row(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = 78.dep())
            .alpha(progress)
    ) {
        YouWillGetPayCard(
            config = YouWillGetPayCardConfig(type = YouWillGetPayCardConfig.Type.GET),
            whole = "00",
            decimal = "00",
            {}
        ){}
        22.sx()
        YouWillGetPayCard(
            config = YouWillGetPayCardConfig(type = YouWillGetPayCardConfig.Type.PAY),
            whole = "00",
            decimal = "00",
            {}
        ){}
    }
}

@Composable
fun HeaderCutout(
    progress: Float,
    wholeGet: String,
    decGet: String,
    wholePay: String,
    decPay: String,
    whole: String,
    decimal: String,
) {
    Column() {
        HeaderUpperCutout(
            progress,
            wholeGet,
            decGet,
            wholePay,
            decPay,
            whole,
            decimal,
        )
        HeaderBottomCutout()
    }
}

data class HeaderUpperCutoutConfiguration(
    val curveHeightConstant: Float = 159f,
    val curveHeightVariable: Float = 60f,
    val curveRadius: Number = 47,
)

@Composable
fun <T>rememberArgument(arg: T): T{
    var rArg by remember {
        mutableStateOf(arg)
    }
    LaunchedEffect(key1 = arg){
        rArg = arg
    }
    return rArg
}

@OptIn(ExperimentalMotionApi::class)
@Composable
fun HeaderUpperCutout(
    progress: Float,
    wholeGet: String,
    decGet: String,
    wholePay: String,
    decPay: String,
    whole: String,
    decimal: String,
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
                this.bottom.linkTo(container.bottom,bm)
            }
        },
        progress = (1f - progress),
    ) {
        val computedCurveHeight by remember(key1 = progress) {
            derivedStateOf {
                config.curveHeightConstant+config.curveHeightVariable*progress
            }
        }
        HeaderUpperCutShape(
            computedCurveHeight,
            config.curveRadius
        ){
            HeaderBackAndSplit(
                contentDescription = "header_back_and_split"
            ){

            }
        }

        HeaderContentAndCards(
            progress,
            wholeGet,
            decGet,
            wholePay,
            decPay,
            whole,
            decimal,
        )
    }
}

@Composable
fun HeaderBottomCutout() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(Color(0xff839BB9))
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

data class HeaderContentAndCardsConfiguration(
    val headerTopPadding: Float = 83f
)

@Composable
fun HeaderContentAndCards(
    progress: Float,
    wholeGet: String,
    decGet: String,
    wholePay: String,
    decPay: String,
    whole: String,
    decimal: String,
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
            whole,
            decimal,
        )
        CollapsedCards(
            progress,
            wholeGet,
            decGet,
            wholePay,
            decPay,
        )
    }
}

data class CollapsedCardsSectionConfiguration(
    val space: Float = 8f,
    val endPadding: Float = 16f,
)

@Composable
fun CollapsedCards(
    progress: Float,
    wholeGet: String,
    decGet: String,
    wholePay: String,
    decPay: String,
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
            whole=wholeGet,
            decimal=decGet,
            config = YouWillGetPayCollapsedCardConfiguration.get
        )
        (config.space * (1f - progress)).sy()
        YouWillGetPayCollapsedCard(
            progress,
            whole=wholePay,
            decimal=decPay,
            config = YouWillGetPayCollapsedCardConfiguration.pay
        )
    }
}

data class YouWillGetPayCollapsedCardConfiguration(
    val textId: Int,
    val amountColor: Color=Color(0xffFF4077),
    val height: Float=32f,
    val borderRadius: Float=50f,
    val backgroundColor: Color=Color.White,
    val currencyFontSize: Float=12f,
    val wholeFontSize: Float=20f,
    val decimalFontSize: Float=12f,
    val fontSize: Float=11f,
    val horizontalPadding: Float=12f,
    val textColor: Color=Color(0xff839BB9),
    val space: Float=12f,
){
    companion object{
        val pay
        get()=YouWillGetPayCollapsedCardConfiguration(
            textId = R.string.you_will_pay,
            amountColor = Color(0xffFF4077)
        )
        val get
        get()=YouWillGetPayCollapsedCardConfiguration(
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

data class HeaderContentRightSpace(
    val rightSpace: Float = 38f
)

@Composable
fun HeaderContentWithSpace(
    progress: Float,
    whole: String,
    decimal: String,
    config: HeaderContentRightSpace = HeaderContentRightSpace(),
) {
    Row {
        (config.rightSpace * (1f - progress)).sx()
        HeaderContent(
            whole,
            decimal,
        )
    }
}

data class HeaderContentConfiguration(
    val topPadding: Number = 4,
    val splitBalanceTextId: Int = R.string.split_balance,
    val splitBalanceFontSize: Float = 12f,
    val splitBalanceColor: Color = Color.White
)

@Composable
fun HeaderContent(
    whole: String,
    decimal: String,
    config: HeaderContentConfiguration = HeaderContentConfiguration()
) {
    Column(
        modifier = Modifier
            .padding(top = config.topPadding.dep())
    ) {
        SplitBalanceText(
            config.splitBalanceTextId,
            config.splitBalanceFontSize,
            config.splitBalanceColor
        )

        YoreAmount(
            config = YoreAmountConfiguration.splitPageHeadContent,
            whole = whole,
            decimal = decimal,
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
    curveHeight: Number,
    curveRadius: Number,
    content: @Composable BoxScope.() -> Unit
) {
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
            .background(Color(0xff839BB9))
    ) {
        content()
    }
}

data class HeaderBackAndSplitConfiguration(
    val splitTextId: Int = R.string.split,
    val splitFontSize: Number = 14,
    val tint: Color = Color.White,
    val topPadding: Number = 17,
    val startPadding: Number = 9,
    val space: Number = 4,
)

@Composable
fun HeaderBackAndSplit(
    contentDescription: String,
    config: HeaderBackAndSplitConfiguration = HeaderBackAndSplitConfiguration(),
    onBackClick: ()->Unit
) {
    Row(
        modifier = Modifier
            .semantics {
                this.contentDescription = contentDescription
            }
            .padding(
                top = config.topPadding.dep(),
                start = config.startPadding.dep()
            )
    ) {
        val ns = yoreNotificationService.current
        BackButton(
            contentDescription = "split_back_button"
        ){
            ns.notify(0,null)
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