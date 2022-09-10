package co.yore.splitnpay

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.MotionLayout
import co.yore.splitnpay.friend_item.FriendItem
import co.yore.splitnpay.friend_item.models.Friend
import co.yore.splitnpay.you_will_get_card.YouWillGetCard
import co.yore.splitnpay.you_will_get_card.YouWillPayCard

enum class SwipingStates {
    EXPANDED,
    COLLAPSED
}
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SplitPage(){
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
                        // Maps anchor points (in px) to states
                        0f to SwipingStates.COLLAPSED,
                        heightInPx to SwipingStates.EXPANDED,
                    )
                )
                .nestedScroll(connection)
        ) {
            Column() {
                MotionLayoutHeader(
                    progress =
                    if (swipingState.progress.to == SwipingStates.COLLAPSED)
                        swipingState.progress.fraction
                    else
                        1f - swipingState.progress.fraction
                )
            }
        }
    }
}

@Composable
fun MotionLayoutHeader(progress: Float) {
    val d = 234.dep().value
    MotionLayout(
        start = JsonConstraintSetStart(),
        end = JsonConstraintSetEnd(d),
        progress = progress,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        var curveHeight by remember { mutableStateOf(0f) }

        val maxHeight = with(LocalDensity.current){
            411.dep().value*this.density
        }
        val minHeight = with(LocalDensity.current){
            234.dep().value*this.density
        }
        var f by remember { mutableStateOf(1f) }
        Box(
            modifier = Modifier
                .layoutId("poster")
                .height(411.dep())
                .background(Color.White)
                .onGloballyPositioned {
                    val dif = maxHeight - minHeight
                    val diff = it.size.height - minHeight
                    f = diff / dif
                    curveHeight = 159 + 60 * f
                }
        ){
            Column(){
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(curveHeight.dep())
                        .clip(
                            RoundedCornerShape(
                                0.dp, 0.dp, 0.dp, 47.dep()
                            )
                        )
                        .background(Color(0xff839BB9))
                ){
                    Box(
                        modifier = Modifier
                            .padding(
                                top = 13.dep(),
                                start = 9.dep()
                            )
                    ){
                        Row(){
                            BackButton()
                            Spacer(modifier = Modifier.width(4.dep()))
                            RobotoText(
                                "Split",
                                fontSize = 14.sep(),
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                        .align(Alignment.TopCenter)
                        .padding(top = 83.dep())
                            .background(Color.Red),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Spacer(modifier = Modifier.width(38.dep()).background(Color.Green))
                        Column(
                            modifier = Modifier
                        ){
                            RobotoText(
                                "Split Balance",
                                fontSize = 12.sep(),
                                color = Color.White,

                                )
                            Row(
                                verticalAlignment = Alignment.Bottom
                            ) {
                                RobotoText(
                                    localCurrency.current,
                                    fontSize = 21.sep(),
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(4.dep()))
                                RobotoText(
                                    "00",
                                    fontSize = 30.sep(),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .alignByBaseline()
                                )
                                RobotoText(
                                    ".00",
                                    fontSize = 14.sep(),
                                    color = Color.White,
                                    modifier = Modifier
                                        .alignByBaseline()
                                )
                            }
                        }
                        Column(
                            modifier = Modifier.padding(end = 16.dep())
                        ){
                            Box(
                                modifier = Modifier
                                    .width(139.dep())
                                    .height(32.dep())
                                    .clip(RoundedCornerShape(50.dep()))
                                    .background(Color.White),
                                contentAlignment = Alignment.Center
                            ){
                                val wholeText = "00"
                                val decimalText = "00"

                                val currencyScript = SpanStyle(
                                    baselineShift = BaselineShift.None,
                                    fontSize = 12.sep()
                                )

                                val decimalScript = SpanStyle(
                                    baselineShift = BaselineShift.None,
                                    fontSize = 12.sep(),
                                    fontWeight = FontWeight.Normal
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ){
                                    Text(
                                        "You will get",
                                        fontSize = 11.sep(),
                                        color = Color(0xff839BB9)
                                    )
                                    Spacer(modifier = Modifier.width(12.dep()))
                                    Text(
                                        text = buildAnnotatedString {
                                            withStyle(currencyScript) {
                                                append(localCurrency.current)
                                                append(" ")
                                            }
                                            append(wholeText)
                                            withStyle(decimalScript) {
                                                append(".")
                                                append(decimalText)
                                            }
                                        },
                                        fontSize = 20.sep(),
                                        color = Color(0xff37D8CF),
                                        fontFamily = robotoFonts,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .width(139.dep())
                                    .height(32.dep())
                                    .clip(RoundedCornerShape(50.dep()))
                                    .background(Color.White),
                                contentAlignment = Alignment.Center
                            ){
                                val wholeText = "00"
                                val decimalText = "00"

                                val currencyScript = SpanStyle(
                                    baselineShift = BaselineShift.None,
                                    fontSize = 12.sep()
                                )

                                val decimalScript = SpanStyle(
                                    baselineShift = BaselineShift.None,
                                    fontSize = 12.sep(),
                                    fontWeight = FontWeight.Normal
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ){
                                    Text(
                                        "You will pay",
                                        fontSize = 11.sep(),
                                        color = Color(0xff839BB9)
                                    )
                                    Spacer(modifier = Modifier.width(12.dep()))
                                    Text(
                                        text = buildAnnotatedString {
                                            withStyle(currencyScript) {
                                                append(localCurrency.current)
                                                append(" ")
                                            }
                                            append(wholeText)
                                            withStyle(decimalScript) {
                                                append(".")
                                                append(decimalText)
                                            }
                                        },
                                        fontSize = 20.sep(),
                                        color = Color(0xffFF4077),
                                        fontFamily = robotoFonts,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .background(Color(0xff839BB9))
                ){
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
                    ){

                    }
                }
            }
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 78.dep())
                    .alpha(f)
            ){
                YouWillGetCard(whole = "00", decimal = "00")
                Spacer(modifier = Modifier.width(22.dep()))
                YouWillPayCard(whole = "00", decimal = "00")
            }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(
                        horizontal = 16.dep()
                    )
            ){
                ContactSearchBar()
            }
        }
        val tabsList = listOf(MainActivity.ContactTabs.Groups.name, MainActivity.ContactTabs.Friends.name)
        var selectedIndex by remember { mutableStateOf(0) }
        Box(
            modifier = Modifier
                .layoutId("tabs")
                .height(73.dep())
                .background(Color.White),
            contentAlignment = Alignment.Center
        ){

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
                                selectedIndex = index
                            }
                            .padding(0.dep()),
                        text = text,
                        fontSize = 21f.sep(),
                        color = if (selected) colorResource(id = R.color.lightblue2)
                        else colorResource(
                            id = R.color.lightgrey5
                        ),
                        fontWeight = /*if (selected) */FontWeight.Bold/* else FontWeight.Normal*/,
                        fontFamily = robotoFonts
                    )
                }
            }
            Divider(
                color = Color(0xfffafcff),
                thickness = 1.dp,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
        Box(
            Modifier
                .layoutId("content")
                .fillMaxHeight()
                .background(Color.White)
        ) {
            if(selectedIndex==0){

            }
            else{
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 17.dep()),
                ){
                    items(
                        100,
                        key = {
                            it
                        }
                    ){
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
        }
    }
}

@Composable
private fun BackButton() {
    Box(
        modifier = Modifier
            .size(24.dep())
            .clip(CircleShape)
            .clickable { }
        ,
        contentAlignment = Alignment.Center
    ){
        Icon(
            modifier = Modifier.size(12.dep()),
            painter = painterResource(id = R.drawable.ic_back_arrow),
            contentDescription = "",
            tint = Color.White
        )
    }
}

@Composable
private fun JsonConstraintSetStart() = ConstraintSet (
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
		start: ['parent', 'start', 0],
		end: ['parent', 'end', 0],
		top: ['tabs', 'bottom', ],
	}
} 
"""
)

@Composable
private fun JsonConstraintSetEnd(
    dim: Float
) = ConstraintSet (""" {
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
		start: ['parent', 'start', 0],
		end: ['parent', 'end', 0],
		top: ['tabs', 'bottom', 0],
	}
                  
} """)

@Composable
internal fun ScrollableContent() {
    val list = listOf(1..200).flatten()
    LazyColumn(
        Modifier.padding(
            bottom = 56.dp // We need to reduce content height by the height of collapsed content
        )
    ) {
        items(
            items = list,
            itemContent = { id ->
                ScrollableContentItem(id = id.toString())
            },
        )
    }
}

@Composable
private fun ScrollableContentItem(id: String) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .height(100.dp)
        .padding(16.dp),
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 4.dp
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = "Item $id",
                color = MaterialTheme.colors.onSurface,
                style = MaterialTheme.typography.h5
            )
        }

    }
}