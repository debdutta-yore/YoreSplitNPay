package co.yore.splitnpay.split_page

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.max
import androidx.constraintlayout.compose.*
import co.yore.splitnpay.R
import co.yore.splitnpay.components.components.*
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.models.DataIds
import co.yore.splitnpay.ui.theme.robotoFonts

class KeyboardStater{
    private var prevValue = -1
    private var lastState = false
    fun process(value: Int): Boolean {
        if(value==0){
            lastState = false
            return lastState
        }
        if(value<prevValue){
            prevValue = value
            lastState = false
            return lastState
        }
        if(value>prevValue){
            prevValue = value
            lastState = true
            return lastState
        }
        return if(value>0) lastState else !lastState
    }

}
@OptIn(ExperimentalMotionApi::class, ExperimentalLayoutApi::class)
@Composable
fun SplitPage() {
    CollapsibleBox(threshold = 0.05f){progress->
        val ks = remember {
            KeyboardStater()
        }
        val insetLength = WindowInsets.ime.getBottom(LocalDensity.current)
        val height = LocalConfiguration.current.screenHeightDp*LocalDensity.current.density
        val keyboardStat by remember(insetLength) {
            derivedStateOf {
                ks.process(insetLength)
            }
        }
        var persistentKeyboardState by remember {
            mutableStateOf(false)
        }
        LaunchedEffect(key1 = keyboardStat){
            persistentKeyboardState = keyboardStat
        }
        val cp by remember {
            derivedStateOf {
                if(!persistentKeyboardState&&insetLength>0){
                    insetLength/height
                }
            }
        }
        val dep = 1.dep()
        MotionLayout(
            progress = progress,
            start = SplitPageStartConstraint(dep),
            end = SplitPageEndConstraint(dep),
            modifier = Modifier.fillMaxSize()
        ) {
            CollapsibleContents(progress)
        }
    }
}

@Composable
fun CollapsibleContents(
    progress: Float,
    notifier: NotificationService = notifier(),
    selectedIndex: Int = intState(DataIds.selectedTabIndex).value
) {
    UpperCut()
    HeaderBackAndSplit(
        modifier = Modifier
            .padding(
                top = 17.dep(),
                start = 9.dep()
            )
            .layoutId("back_button_section"),
        contentDescription = "header_back_and_split"
    ) {

    }
    HeaderContentSection()
    BottomCut()
    YouWillGetCard(progress)
    YouWillPayCard(progress)
    SearchBarSection()
    TabsSection(selectedIndex){
        notifier.notify(DataIds.selectedTabIndex,it)
    }
    ContentSection(selectedIndex)
}

fun SplitPageEndConstraint(dep: Dp): ConstraintSet {
    return ConstraintSet {
        val upper_cut = createRefFor("upper_cut")
        val bottom_cut = createRefFor("bottom_cut")
        val search_bar = createRefFor("search_bar")
        val header_content = createRefFor("header_content")
        val collapsed_cards = createRefFor("collapsed_cards")
        val tabs = createRefFor("tabs")
        val content = createRefFor("content")
        val get_card = createRefFor("get_card")
        val you_will_get_card = createRefFor("you_will_get_card")
        val you_will_get_card1 = createRefFor("you_will_get_card1")
        constrain(upper_cut){
            height = Dimension.value(159*dep)
            width = Dimension.matchParent
            end.linkTo(get_card.end)
        }
        constrain(bottom_cut){
            top.linkTo(upper_cut.bottom)
        }
        constrain(search_bar){
            top.linkTo(upper_cut.bottom,31*dep)
        }
        constrain(header_content){
            centerVerticallyTo(upper_cut)
            start.linkTo(upper_cut.start,38*dep)
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
            bottom.linkTo(parent.bottom)
            width = Dimension.matchParent
            height = Dimension.fillToConstraints
        }
        constrain(you_will_get_card){
            end.linkTo(upper_cut.end)
            //bottom.linkTo(upper_cut.bottom,_53_dep)
            top.linkTo(you_will_get_card1.bottom,8*dep)
        }
        constrain(you_will_get_card1){
            end.linkTo(upper_cut.end)
            top.linkTo(upper_cut.top,53*dep)
            //bottom.linkTo(you_will_get_card.top,_8_dep)
        }
    }
}

fun SplitPageStartConstraint(dep: Dp): ConstraintSet {
    return ConstraintSet {
        val upper_cut = createRefFor("upper_cut")
        val bottom_cut = createRefFor("bottom_cut")
        val search_bar = createRefFor("search_bar")
        val expanded_cards = createRefFor("expanded_cards")
        val header_content = createRefFor("header_content")
        val collapsed_cards = createRefFor("collapsed_cards")
        val content = createRefFor("content")
        val tabs = createRefFor("tabs")
        val you_will_get_card = createRefFor("you_will_get_card")
        val you_will_get_card1 = createRefFor("you_will_get_card1")
        val back_button_section = createRefFor("back_button_section")
        constrain(back_button_section){
            start.linkTo(upper_cut.start)
        }
        constrain(upper_cut) {
            height = Dimension.value(dep * 219)
        }
        constrain(bottom_cut) {
            top.linkTo(upper_cut.bottom)
        }
        constrain(search_bar) {
            top.linkTo(bottom_cut.bottom, 106 * dep)
        }
        constrain(expanded_cards) {
            bottom.linkTo(search_bar.top, 34 * dep)
        }
        constrain(header_content) {
            centerTo(upper_cut)
        }
        constrain(collapsed_cards) {
            centerVerticallyTo(header_content)
            end.linkTo(upper_cut.end)
        }
        constrain(tabs) {
            top.linkTo(search_bar.bottom)
        }
        constrain(content) {
            top.linkTo(tabs.bottom)
            bottom.linkTo(parent.bottom)
            width = Dimension.matchParent
            height = Dimension.fillToConstraints
        }
        constrain(you_will_get_card) {
            start.linkTo(upper_cut.start, 16 * dep)
            top.linkTo(search_bar.top, -183 * dep)
        }
        constrain(you_will_get_card1) {
            top.linkTo(search_bar.top, -183 * dep)
            start.linkTo(upper_cut.start, 191 * dep)
        }
    }
}

@OptIn(ExperimentalMotionApi::class)
@Composable
fun YouWillGetCard(
    progress: Float
) {
    val dep = 1.dep()
    val textColor by remember(progress) {
        derivedStateOf {
            Color.blend(Color(0xff243257), Color(0xff839BB9),progress)
        }
    }
    MotionLayout(
        start = YouWillGetCardStartConstraint(dep),
        end = YouWillGetCardEndConstraint(dep),
        progress = progress,
        modifier = Modifier
            .layoutId("you_will_get_card")
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

fun YouWillGetCardEndConstraint(dep: Dp): ConstraintSet {
    return ConstraintSet {
        val get_card = createRefFor("get_card")
        val text = createRefFor("text")
        val amount = createRefFor("amount")
        val get_icon = createRefFor("get_icon")
        val arrow = createRefFor("arrow")
        constrain(get_card){
            height = Dimension.value(32*dep)
            width = Dimension.fillToConstraints
            start.linkTo(text.start,-12*dep)
            end.linkTo(amount.end,-12*dep)
        }
        constrain(text){
            centerVerticallyTo(get_card)
        }
        constrain(amount){
            start.linkTo(text.end,12*dep)
            centerVerticallyTo(get_card)
        }
        constrain(amount){
            start.linkTo(text.end,12*dep)
            end.linkTo(parent.end,28*dep)
            centerVerticallyTo(get_card)
        }
        constrain(get_icon){
            start.linkTo(get_card.start,33*dep)
            bottom.linkTo(get_card.bottom)
            pivotX = 0f
            pivotY = 1f
            alpha = 0.0f
            scaleX = 0f
            scaleY = 0f
        }
        constrain(arrow){
            bottom.linkTo(get_card.bottom,8*dep)
            end.linkTo(get_card.end,10*dep)
            pivotX = 1f
            pivotY = 1f
            alpha = 0.0f
            scaleX = 0f
            scaleY = 0f
        }
    }
}

fun YouWillGetCardStartConstraint(dep: Dp): ConstraintSet {
    return ConstraintSet {
        val get_card = createRefFor("get_card")
        val text = createRefFor("text")
        val amount = createRefFor("amount")
        val get_icon = createRefFor("get_icon")
        val arrow = createRefFor("arrow")
        constrain(get_card){

            width = Dimension.value(153*dep)
            height = Dimension.value(149*dep)
        }
        constrain(text){
            centerHorizontallyTo(get_card)
            top.linkTo(get_card.top,22*dep)
        }
        constrain(amount){
            centerHorizontallyTo(get_card)
            top.linkTo(text.bottom,6*dep)
        }
        constrain(get_icon){
            pivotX = 0f
            pivotY = 1f
            start.linkTo(get_card.start,33*dep)
            bottom.linkTo(get_card.bottom)
        }
        constrain(arrow){
            pivotX = 1f
            pivotY = 1f
            bottom.linkTo(get_card.bottom,8*dep)
            end.linkTo(get_card.end,10*dep)
        }
    }
}

@OptIn(ExperimentalMotionApi::class)
@Composable
fun YouWillPayCard(
    progress: Float
) {
    val dep = 1.dep()

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

                width = Dimension.value(153*dep)
                height = Dimension.value(149*dep)
            }
            constrain(text){
                centerHorizontallyTo(get_card)
                top.linkTo(get_card.top,22*dep)
            }
            constrain(amount){
                centerHorizontallyTo(get_card)
                top.linkTo(text.bottom,6*dep)
            }
            constrain(pay_icon){
                pivotX = 0f
                pivotY = 1f
                start.linkTo(get_card.start,27*dep)
                bottom.linkTo(get_card.bottom)
            }
            constrain(arrow){
                pivotX = 1f
                pivotY = 1f
                bottom.linkTo(get_card.bottom,8*dep)
                end.linkTo(get_card.end,10*dep)
            }
        },
        end = ConstraintSet {
            val get_card = createRefFor("get_card")
            val text = createRefFor("text")
            val amount = createRefFor("amount")
            val pay_icon = createRefFor("pay_icon")
            val arrow = createRefFor("arrow")
            constrain(get_card){
                height = Dimension.value(32*dep)
                width = Dimension.fillToConstraints
                start.linkTo(text.start,-12*dep)
                end.linkTo(amount.end,-12*dep)
            }
            constrain(text){
                centerVerticallyTo(get_card)
            }
            constrain(amount){
                start.linkTo(text.end,12*dep)
                centerVerticallyTo(get_card)
            }
            constrain(amount){
                start.linkTo(text.end,12*dep)
                end.linkTo(parent.end,28*dep)
                centerVerticallyTo(get_card)
            }
            constrain(pay_icon){
                start.linkTo(get_card.start,33*dep)
                bottom.linkTo(get_card.bottom)
                pivotX = 0f
                pivotY = 1f
                alpha = 0.0f
                scaleX = 0f
                scaleY = 0f
            }
            constrain(arrow){
                bottom.linkTo(get_card.bottom,8*dep)
                end.linkTo(get_card.end,10*dep)
                pivotX = 1f
                pivotY = 1f
                alpha = 0.0f
                scaleX = 0f
                scaleY = 0f
            }
        },
        progress = progress,
        modifier = Modifier
            .layoutId("you_will_get_card1")
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
                .background(
                    Color.White,
                    RoundedCornerShape((15 * (1f + progress)).dep())
                )
        ){

        }
        AnimatedTextContent(
            modifier = Modifier
            .layoutId("text"),
            from = "You'll pay",
            to = "You will pay",
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
        listOf(ContactTabs.Groups.name, ContactTabs.People.name)

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
            whole = stringState(DataIds.whole).value,
            decimal = stringState(DataIds.decimal).value,
        )
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
        )
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