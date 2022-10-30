package co.yore.splitnpay.split_page_components

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.*
import co.yore.splitnpay.R
import co.yore.splitnpay.components.components.*
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.models.ContactTabs
import co.yore.splitnpay.models.DataIds
import co.yore.splitnpay.models.SplitPageState
import co.yore.splitnpay.models.YoreAmountConfiguration
import co.yore.splitnpay.ui.theme.*

@OptIn(ExperimentalMotionApi::class, ExperimentalLayoutApi::class)
@Composable
fun SplitPageContent() {
    CollapsibleBox(
        threshold = 0.05f,
        keyboardAware = true,
        insetAware = false
    ){progress ->
        val dep = 1.dep()
        MotionLayout(
            progress = progress,
            start = SplitPageStartConstraint(dep),
            end = SplitPageEndConstraint(dep),
            modifier = Modifier
                .fillMaxSize()
        ) {
            CollapsibleContents(
                progress = progress
            )
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
    )
    HeaderContentSection()
    BottomCut()
    YouWillGetCard(progress)
    YouWillPayCard(progress)
    SearchBarSection()
    TabsSection(selectedIndex){
        notifier.notify(DataIds.selectedTabIndex, it)
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
            height = Dimension.value(159 * dep)
            width = Dimension.matchParent
            end.linkTo(get_card.end)
        }
        constrain(bottom_cut){
            top.linkTo(upper_cut.bottom)
        }
        constrain(search_bar){
            top.linkTo(upper_cut.bottom, 31 * dep)
        }
        constrain(header_content){
            centerVerticallyTo(upper_cut)
            start.linkTo(upper_cut.start, 38 * dep)
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
            // bottom.linkTo(upper_cut.bottom,_53_dep)
            top.linkTo(you_will_get_card1.bottom, 8 * dep)
        }
        constrain(you_will_get_card1){
            end.linkTo(upper_cut.end)
            top.linkTo(upper_cut.top, 53 * dep)
            // bottom.linkTo(you_will_get_card.top,_8_dep)
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
    progress: Float,
    haveSplit: Boolean = tState<Boolean>(DataIds.haveSplit).value,
    wholeGet: String = stringState(DataIds.wholeGet).value,
    decimalGet: String = stringState(DataIds.decimalGet).value,
    notifier: NotificationService = notifier()
) {
    val themeColor by remember {
        derivedStateOf {
            if (haveSplit) {
                LightGreen3
            } else {
                MyColor3
            }
        }
    }
    val dep = 1.dep()
    val textColor by remember(progress) {
        derivedStateOf {
            Color.blend(DarkBlue, LightBlue4, progress)
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
                    color = Grey_shadow.copy(alpha = 1f - progress),
                    borderRadius = 15.dep(),
                    blurRadius = 33.dep(),
                    spread = 0f,
                    offsetX = 7.dep(),
                    offsetY = 7.dep()
                )
                .clip(RoundedCornerShape(15.dep()))
                .background(
                    Color.White
                )
                .clickable(
                    rippleColor = themeColor
                ) {
                    notifier.notify(DataIds.getCard, null)
                }
        ){

        }
        AnimatedTextContent(
            modifier = Modifier
                .layoutId("text"),
            from = "You’ll get",
            to = "You will get",
            progress = progress,
            spanStyle = SpanStyle(
                fontWeight = FontWeight((700 - 300 * progress).toInt()),
                color = textColor,
                fontSize = (16 - 5 * progress).sep()
            )
        )
        Box(
            modifier = Modifier
                .layoutId("amount")
        ){
            YoreAmount(
                config = YoreAmountConfiguration(
                    color = themeColor,
                    fontFamily = robotoFonts,
                    fontSize = 20,
                    currencyFontSize = 12,
                    decimalFontSize = 12,
                    wholeFontWeight = FontWeight.Bold
                ),
                whole = wholeGet,
                decimal = decimalGet
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
                .clip(CircleShape)
                .clickable(
                    rippleColor = themeColor
                ) {
                    notifier.notify(DataIds.getCard, null)
                }
        )
    }
}

@SuppressLint("Range")
fun YouWillGetCardEndConstraint(dep: Dp): ConstraintSet {
    return ConstraintSet {
        val get_card = createRefFor("get_card")
        val text = createRefFor("text")
        val amount = createRefFor("amount")
        val get_icon = createRefFor("get_icon")
        val arrow = createRefFor("arrow")
        constrain(get_card){
            height = Dimension.value(32 * dep)
            width = Dimension.fillToConstraints
            start.linkTo(text.start, -12 * dep)
            end.linkTo(amount.end, -12 * dep)
        }
        constrain(text){
            centerVerticallyTo(get_card)
        }
        constrain(amount){
            start.linkTo(text.end, 12 * dep)
            centerVerticallyTo(get_card)
        }
        constrain(amount){
            start.linkTo(text.end, 12 * dep)
            end.linkTo(parent.end, 28 * dep)
            centerVerticallyTo(get_card)
        }
        constrain(get_icon){
            start.linkTo(get_card.start, 33 * dep)
            bottom.linkTo(get_card.bottom)
            pivotX = 0f
            pivotY = 1f
            alpha = 0.0f
            scaleX = 0f
            scaleY = 0f
        }
        constrain(arrow){
            bottom.linkTo(get_card.bottom, 8 * dep)
            end.linkTo(get_card.end, 10 * dep)
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

            width = Dimension.value(153 * dep)
            height = Dimension.value(149 * dep)
        }
        constrain(text){
            centerHorizontallyTo(get_card)
            top.linkTo(get_card.top, 22 * dep)
        }
        constrain(amount){
            centerHorizontallyTo(get_card)
            top.linkTo(text.bottom, 6 * dep)
        }
        constrain(get_icon){
            pivotX = 0f
            pivotY = 1f
            start.linkTo(get_card.start, 33 * dep)
            bottom.linkTo(get_card.bottom)
        }
        constrain(arrow){
            pivotX = 1f
            pivotY = 1f
            bottom.linkTo(get_card.bottom, 8 * dep)
            end.linkTo(get_card.end, 10 * dep)
        }
    }
}

@SuppressLint("Range")
@OptIn(ExperimentalMotionApi::class)
@Composable
fun YouWillPayCard(
    progress: Float,
    haveSplit: Boolean = tState<Boolean>(DataIds.haveSplit).value,
    wholePay: String = stringState(DataIds.wholePay).value,
    decimalPay: String = stringState(DataIds.decimalPay).value,
    notifier: NotificationService = notifier()
) {

    val themeColor by remember {
        derivedStateOf {
            if (haveSplit) {
                Pink
            } else {
                MyColor3
            }
        }
    }
    val dep = 1.dep()

    val textColor by remember(progress) {
        derivedStateOf {
            Color.blend(DarkBlue, Lightblue4, progress)
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

                width = Dimension.value(153 * dep)
                height = Dimension.value(149 * dep)
            }
            constrain(text){
                centerHorizontallyTo(get_card)
                top.linkTo(get_card.top, 22 * dep)
            }
            constrain(amount){
                centerHorizontallyTo(get_card)
                top.linkTo(text.bottom, 6 * dep)
            }
            constrain(pay_icon){
                pivotX = 0f
                pivotY = 1f
                start.linkTo(get_card.start, 27 * dep)
                bottom.linkTo(get_card.bottom)
            }
            constrain(arrow){
                pivotX = 1f
                pivotY = 1f
                bottom.linkTo(get_card.bottom, 8 * dep)
                end.linkTo(get_card.end, 10 * dep)
            }
        },
        end = ConstraintSet {
            val get_card = createRefFor("get_card")
            val text = createRefFor("text")
            val amount = createRefFor("amount")
            val pay_icon = createRefFor("pay_icon")
            val arrow = createRefFor("arrow")
            constrain(get_card){
                height = Dimension.value(32 * dep)
                width = Dimension.fillToConstraints
                start.linkTo(text.start, -12 * dep)
                end.linkTo(amount.end, -12 * dep)
            }
            constrain(text){
                centerVerticallyTo(get_card)
            }
            constrain(amount){
                start.linkTo(text.end, 12 * dep)
                centerVerticallyTo(get_card)
            }
            constrain(amount){
                start.linkTo(text.end, 12 * dep)
                end.linkTo(parent.end, 28 * dep)
                centerVerticallyTo(get_card)
            }
            constrain(pay_icon){
                start.linkTo(get_card.start, 33 * dep)
                bottom.linkTo(get_card.bottom)
                pivotX = 0f
                pivotY = 1f
                alpha = 0.0f
                scaleX = 0f
                scaleY = 0f
            }
            constrain(arrow){
                bottom.linkTo(get_card.bottom, 8 * dep)
                end.linkTo(get_card.end, 10 * dep)
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
        // .size(cardSize)
        // .background(Color.Red)
        // .clipToBounds()
    ) {
        Box(
            modifier = Modifier
                .layoutId("get_card")
                .coloredShadow(
                    color = Grey_shadow.copy(alpha = 1f - progress),
                    borderRadius = 15.dep(),
                    blurRadius = 33.dep(),
                    spread = 0f,
                    offsetX = 7.dep(),
                    offsetY = 7.dep()
                )
                .clip(
                    RoundedCornerShape((15 * (1f + progress)).dep())
                )
                .background(
                    Color.White
                )
                .clickable(
                    rippleColor = themeColor
                ){
                    notifier.notify(DataIds.payCard, null)
                }
        ){

        }
        AnimatedTextContent(
            modifier = Modifier
                .layoutId("text"),
            from = "You'll pay",
            to = "You will pay",
            progress = progress,
            spanStyle = SpanStyle(
                fontWeight = FontWeight((700 - 300 * progress).toInt()),
                color = textColor,
                fontSize = (16 - 5 * progress).sep()
            )
        )
        Box(
            modifier = Modifier
                .layoutId("amount")
        ){
            YoreAmount(
                config = YoreAmountConfiguration(
                    color = themeColor,
                    fontFamily = robotoFonts,
                    fontSize = 20,
                    currencyFontSize = 12,
                    decimalFontSize = 12,
                    wholeFontWeight = FontWeight.Bold
                ),
                whole = wholePay,
                decimal = decimalPay
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
                .clip(CircleShape)
                .clickable(
                    rippleColor = Pink
                ) {
                    notifier.notify(DataIds.payCard, null)
                }
        )
    }
}

fun Modifier.ripple() = composed(
    factory = {
        val interactionSource = remember { MutableInteractionSource() }
        val rippleColor = Color.Red
        Modifier
            .indication(
                interactionSource = interactionSource,
                indication = rememberRipple(
                    color = rippleColor,
                    radius = 8.dp
                )
            )
    }
)

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
    onIndexChanged: (Int) -> Unit
) {
    val tabsList =
        listOf(ContactTabs.Groups.name, ContactTabs.People.name)

    Box(
        modifier = Modifier
            .layoutId("tabs")
    ){
        Tabs(
            selectedIndex,
            tabsList
        ) {
            onIndexChanged(it)
        }
    }
}

@Composable
fun HeaderContentSection(
    whole: String = stringState(DataIds.whole).value,
    decimal: String = stringState(DataIds.decimal).value
) {
    Column(
        modifier = Modifier
            .layoutId("header_content")
            .wrapContentWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SplitBalanceText(
            R.string.split_balance,
            12f,
            Color.White
        )

        YoreAmount(
            config = YoreAmountConfiguration.splitPageHeadContent,
            whole = whole,
            decimal = decimal
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
fun BottomCut(
    pageState: SplitPageState = tState<SplitPageState>(DataIds.ultimateState).value
) {
    val backgroundColor by remember(pageState) {
        derivedStateOf {
            when (pageState){
                SplitPageState.GET -> LightGreen3
                SplitPageState.PAY -> Pink
                SplitPageState.NONE -> LightBlue4
            }
        }
    }
    Box(
        modifier = Modifier
            .layoutId("bottom_cut")
            .fillMaxWidth()
            .height(42.dep())
            .background(backgroundColor)
            .background(
                Color.White,
                RoundedCornerShape(
                    topEnd = 48.dep()

                )
            )
    ){

    }
}

@Composable
fun UpperCut(
    pageState: SplitPageState = tState<SplitPageState>(DataIds.ultimateState).value
) {
    val backgroundColor by remember(pageState) {
        derivedStateOf {
            when (pageState){
                SplitPageState.GET -> Lightgreen3
                SplitPageState.PAY -> Pink
                SplitPageState.NONE -> LightBlue4
            }
        }
    }
    Box(
        modifier = Modifier
            .layoutId("upper_cut")
            .fillMaxWidth()
            .height(219.dep())
            .background(
                backgroundColor,
                RoundedCornerShape(
                    bottomStart = 48.dep()

                )
            )
    ){

    }
}
