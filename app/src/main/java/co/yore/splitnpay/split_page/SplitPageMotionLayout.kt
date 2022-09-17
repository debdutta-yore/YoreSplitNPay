package co.yore.splitnpay.split_page

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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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

@OptIn(ExperimentalMotionApi::class)
@Composable
fun SplitPageMotionLayout(
    progress: Float,
    swipe: ()->Unit
) {
    val d = 234.dep().value
    MotionLayout(
        start = JsonConstraintSetStart(),
        end = JsonConstraintSetEnd(d),
        progress = progress,
        modifier = Modifier
            .fillMaxSize()
    ) {
        var curveHeight by remember { mutableStateOf(0f) }

        val maxHeight = with(LocalDensity.current) {
            411.dep().value * this.density
        }
        val minHeight = with(LocalDensity.current) {
            234.dep().value * this.density
        }
        var f by remember { mutableStateOf(1f) }
        LaunchedEffect(key1 = f){
            if(f==1f){
                swipe()
            }
        }
        HeaderAndSearchBar(minHeight,maxHeight,curveHeight,f,){
            f = it
            curveHeight = 159 + 60 * f
        }
        val tabsList =
            listOf(ContactTabs.Groups.name, ContactTabs.Friends.name)
        var selectedIndex by remember { mutableStateOf(0) }
        TabsSection(
            selectedIndex,
            tabsList
        ){ selectedIndex = it }
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

@Composable
fun GroupCreationButton() {
    Button(modifier = Modifier
        .width(90f.dep())
        .height(35f.dep()),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = colorResource(
                id = R.color.splitGreyCard
            )
        ),
        onClick = {  }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_add_group),
            contentDescription = "add group",
            tint = Color.Unspecified,
            modifier = Modifier.size(18.dep())
        )
    }
}

@Composable
fun NoGroupHasBeenCreatedYet() {
    RobotoText(
        text = stringResource(R.string.no_group_has_been_created_yet),
        modifier = Modifier,
        textAlign = TextAlign.Center,
        color = colorResource(id = R.color.splitGreyCard),
        fontWeight = FontWeight.Bold,
        fontSize = 13.sep()
    )
}

@Composable
fun TabsSection(
    selectedIndex: Int,
    tabsList: List<String>,
    onSelectionChanged: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .layoutId("tabs")
            .height(73.dep())
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {

        Tabs(
            selectedIndex,
            tabsList
        ){
            onSelectionChanged(it)
        }
        Divider(
            color = Color(0xfffafcff),
            thickness = 1.dp,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun Tabs(
    selectedIndex: Int,
    tabsList: List<String>,
    onSelectionChanged: (Int)->Unit
) {
    TabRow(
        selectedTabIndex = selectedIndex,
        backgroundColor = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dep()),
        indicator = { tabPositions: List<TabPosition> ->
            Box {}
        },
        divider = { TabRowDefaults.Divider(color = Color.Transparent) },
    ) {
        tabsList.forEachIndexed { index, text ->
            val selected = selectedIndex == index

            Text(
                modifier = Modifier
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null
                    ) {
                        onSelectionChanged(index)
                    }
                    .padding(0.dep()),
                text = text,
                fontSize = 21f.sep(),
                color = if (selected) colorResource(id = R.color.lightblue2)
                else colorResource(
                    id = R.color.lightgrey5
                ),
                fontWeight = FontWeight.Bold,
                fontFamily = robotoFonts
            )
        }
    }
}

@Composable
fun HeaderAndSearchBar(
    minHeight: Float,
    maxHeight: Float,
    curveHeight: Number,
    progress: Float,
    curveRadius: Number,
    wholeGet: String,
    decGet: String,
    wholePay: String,
    decPay: String,
    whole: String,
    decimal: String,
    headerSpace: Float,//4f
    headerRightSpace: Float,//38f
    headerTopPadding: Float,//83
    cardSpace: Float,//8f
    cardEndPadding: Float,//16f
    splitFontSize: Float,//14,
    tint: Float,//Color.White,
    topPadding: Float,//13,
    startPadding: Float,//9,
    space: Float,//4f
    height: Float,//411
    color: Color,//white
    onFactorChanged: (Float)->Unit
) {
    Box(
        modifier = Modifier
            .layoutId("poster")
            .height(height.dep())
            .background(color)
            .onGloballyPositioned {
                val dif = maxHeight - minHeight
                val diff = it.size.height - minHeight
                val p = diff / dif
                onFactorChanged(p)
            }
    ) {
        HeaderCutout(
            progress,
            curveHeight,
            curveRadius,
            wholeGet,
            decGet,
            wholePay,
            decPay,
            whole,
            decimal,
            headerSpace,
            headerRightSpace,
            headerTopPadding,
            cardSpace,
            cardEndPadding,
            splitFontSize,
            tint,
            topPadding,
            startPadding,
            space,
        )
        ExpandedCards(progress)
        SearchBar()
    }
}

@Composable
fun BoxScope.SearchBar() {
    Box(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(
                horizontal = 16.dep()
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
    curveHeight: Number,
    curveRadius: Number,
    wholeGet: String,
    decGet: String,
    wholePay: String,
    decPay: String,
    whole: String,
    decimal: String,
    headerSpace: Float,//4f
    headerRightSpace: Float,//38f
    headerTopPadding: Float,//83
    cardSpace: Float,//8f
    cardEndPadding: Float,//16f
    splitFontSize: Float,//14,
    tint: Float,//Color.White,
    topPadding: Float,//13,
    startPadding: Float,//9,
    space: Float,//4f
) {
    Column() {
        HeaderUpperCutout(
            progress,
            curveHeight,
            curveRadius,
            wholeGet,
            decGet,
            wholePay,
            decPay,
            whole,
            decimal,
            headerSpace,
            headerRightSpace,
            headerTopPadding,
            cardSpace,
            cardEndPadding,
            splitFontSize,
            tint,
            topPadding,
            startPadding,
            space,
        )
        HeaderBottomCutout()
    }
}

@OptIn(ExperimentalMotionApi::class)
@Composable
fun HeaderUpperCutout(
    progress: Float,
    curveHeight: Number,//219
    curveRadius: Number,//47

    wholeGet: String,
    decGet: String,
    wholePay: String,
    decPay: String,
    whole: String,
    decimal: String,
    headerSpace: Float,//4f
    headerRightSpace: Float,//38f
    headerTopPadding: Float,//83
    cardSpace: Float,//8f
    cardEndPadding: Float,//16f

    splitFontSize: Float,//14,
    tint: Float,//Color.White,
    topPadding: Float,//13,
    startPadding: Float,//9,
    space: Float,//4f
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

        HeaderUpperCutShape(
            curveHeight,
            curveRadius
        ){
            HeaderBackAndSplit(
                splitTextId = R.string.split,
                splitFontSize = 14,
                tint = Color.White,
                topPadding = 13,
                startPadding = 9,
                space = 4f
            ){}
        }

        HeaderContentAndCards(
            wholeGet,
            decGet,
            wholePay,
            decPay,
            progress,
            whole,
            decimal,
            headerSpace,
            headerRightSpace,
            headerTopPadding,
            cardSpace,
            cardEndPadding,
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

@Composable
fun HeaderContentAndCards(
    wholeGet: String,
    decGet: String,
    wholePay: String,
    decPay: String,
    progress: Float,
    whole: String,
    decimal: String,
    headerSpace: Number,//4f
    headerRightSpace: Float,//38f
    headerTopPadding: Number,//83
    cardSpace: Float,//8f
    cardEndPadding: Float,//16f
) {
    Row(
        modifier = Modifier
            .layoutId("row")
            .padding(top = headerTopPadding.dep()),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        HeaderContentWithSpace(
            progress,
            whole,
            decimal,
            headerSpace,
            headerRightSpace,
        )
        CollapsedCards(
            progress,
            wholeGet,
            decGet,
            wholePay,
            decPay,
            cardSpace,
            cardEndPadding
        )
    }
}

@Composable
fun CollapsedCards(
    progress: Float,
    wholeGet: String,
    decGet: String,
    wholePay: String,
    decPay: String,
    space: Float,//8f
    endPadding: Float,//16
) {
    Column(
        modifier = Modifier
            .padding(end = (endPadding * (1f - progress)).dep())
            .alpha(1f - progress),
        horizontalAlignment = Alignment.End
    ) {
        YouWillGetPayCollapsedCard(
            progress,
            whole=wholeGet,
            decimal=decGet,
            config = YouWillGetPayCollapsedCardConfiguration.get
        )
        (space * (1f - progress)).sy()
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


@Composable
fun HeaderContentWithSpace(
    progress: Float,
    whole: String,
    decimal: String,
    headerSpace: Number,
    headerRightSpace: Float,
) {
    Row {
        (headerRightSpace * (1 - progress)).sx()
        HeaderContent(
            whole,
            decimal,
            headerSpace
        )
    }
}

@Composable
fun HeaderContent(
    whole: String,
    decimal: String,
    space: Number
) {
    Column(
        modifier = Modifier
            .padding(top = space.dep())
    ) {
        SplitBalanceText()
        HeaderAmount(
            whole,
            decimal
        )
    }
}

data class HeaderAmountConfiguration(
    val currencySpace: Number=4,
    val wholeFontSize: Float=30f,
    val decimalFontSize: Float=14f,
    val wholeColor: Color=Color.White,
    val decimalColor: Color=Color.White
)

@Composable
fun HeaderAmount(
    whole: String,
    decimal: String,
    config: HeaderAmountConfiguration = HeaderAmountConfiguration()
) {
    Row(
        verticalAlignment = Alignment.Bottom
    ) {
        HeaderAmountCurrency()
        config.currencySpace.sx()
        HeaderAmountWholePart(
            whole,
            fontSize = config.wholeFontSize,
            color = config.wholeColor
        )
        HeaderAmountDecimalPart(
            decimal,
            fontSize = config.decimalFontSize,
            color = config.decimalColor
        )
    }
}

@Composable
fun RowScope.HeaderAmountDecimalPart(
    decimal: String,
    fontSize: Float,
    color: Color
) {
    RobotoText(
        ".$decimal",
        fontSize = fontSize.sep(),
        color = color,
        modifier = Modifier
            .alignByBaseline()
    )
}

@Composable
fun RowScope.HeaderAmountWholePart(
    whole: String,
    fontSize: Float,//30f
    color: Color,//Color.White
) {
    RobotoText(
        whole,
        fontSize = fontSize.sep(),
        color = color,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .alignByBaseline()
    )
}

@Composable
fun HeaderAmountCurrency() {
    RobotoText(
        localCurrency.current,
        fontSize = 21.sep(),
        color = Color.White
    )
}

@Composable
fun SplitBalanceText() {
    RobotoText(
        stringResource(R.string.split_balance),
        fontSize = 12.sep(),
        color = Color.White,
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

@Composable
fun HeaderBackAndSplit(
    splitTextId: Int,
    splitFontSize: Number,
    tint: Color,
    topPadding: Number,//13
    startPadding: Number,//9,
    space: Float,//4f
    onBackClick: ()->Unit
) {
    Row(
        modifier = Modifier
            .padding(
                top = topPadding.dep(),
                start = startPadding.dep()
            )
    ) {
        BackButton(
            contentDescription = "split_back_button"
        ){onBackClick()}
        space.sx()
        SplitText(splitTextId,splitFontSize, tint)
    }
}

@Composable
fun SplitText(
    texId: Int,
    fontSize: Number,
    color: Color,
) {
    RobotoText(
        stringResource(texId),
        fontSize = fontSize.sep(),
        fontWeight = FontWeight.Bold,
        color = color
    )
}
