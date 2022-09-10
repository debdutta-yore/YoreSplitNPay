package co.yore.splitnpay

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import co.yore.splitnpay.friend_item.FriendItem
import co.yore.splitnpay.friend_item.models.Friend
import co.yore.splitnpay.ui.theme.YoreSplitNPayTheme
import co.yore.splitnpay.you_will_get_card.YouWillGetCard
import co.yore.splitnpay.you_will_get_card.YouWillPayCard
import kotlin.concurrent.thread
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YoreSplitNPayTheme {
                // A surface container using the 'background' color from the theme
                CompositionLocalProvider(localFullWidth provides 360f) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) {
                        /*LazyColumn(
                            contentPadding = PaddingValues(horizontal = 17.dep())
                        ){
                            items(
                                10,
                                key = {
                                    it
                                }
                            ){
                                FriendItem(
                                    selected = it%2==0,
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
                        }*/

                        /*Box(
                            contentAlignment = Alignment.Center
                        ){
                            YouWillPayCard(whole = "100", decimal = "20")
                        }*/
                        //test()

                        SplitPage()
                    }
                }
            }
        }
    }

    class Acc{
        private var lastRecorded = 0L
        private var score = 0
        private var _val = 0f
        private var _speed = 0f
        fun process(value: Float, min: Float, max: Float): Float{
            val now = System.currentTimeMillis()
            if(now-lastRecorded>300){
                score = 0
                _val = 0f
                _speed = 0f
            }
            lastRecorded = now
            val prefer = if(score>0 && _speed>0) 1 else 0
            val speed = value - _val
            _val = value
            if(speed==_speed){
                ++score
            }
            else{
                score = 0
            }
            _speed = speed
            if(prefer>0 && speed<0){
                return max
            }
            if(prefer<0 && speed>0){
                return min
            }
            return value
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun test() {
        val swipeableState = rememberSwipeableState(initialValue = States.EXPANDED)
        val maxHeight = with(LocalDensity.current){
            219.dep().value*this.density
        }
        val minHeight = with(LocalDensity.current){
            159.dep().value*this.density
        }
        val maxHeight1 = with(LocalDensity.current){
            367.dep().value*this.density
        }
        val minHeight1 = with(LocalDensity.current){
            190.dep().value*this.density
        }
        val acc by remember { mutableStateOf(Acc()) }
        var height by remember { mutableStateOf(maxHeight) }
        var height1 by remember { mutableStateOf(maxHeight1) }
        var progress by remember { mutableStateOf(1f) }
        var lastScrolled by remember { mutableStateOf(0L) }
        var ddy by remember { mutableStateOf(0f) }
        val nestedScrollConnection = remember {
            object : NestedScrollConnection {
                override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                    /*val dy = available.y
                    Log.d("fljlflsjfd1","$dy")
                    val temp = height + dy
                    val p = height
                    height = temp.coerceIn(minHeight,maxHeight)
                    progress = (height - minHeight)/(maxHeight - minHeight)
                    //return Offset(0.0f,height - p)
                    return Offset.Zero*/
                    ddy = available.y
                    val delta = ddy
                    val newOffset = height + delta
                    val newOffset1 = height1 + delta
                    val p = height
                    val p1 = height1
                    height = newOffset.coerceIn(minHeight,maxHeight)
                    height1 = newOffset1.coerceIn(minHeight1,maxHeight1)
                    //val h = newOffset.coerceIn(minHeight,maxHeight)
                    //height = acc.process(h,minHeight,maxHeight)
                    progress = (height - minHeight)/(maxHeight - minHeight)
                    return available
                    //return Offset(0.0f,height - p) /*+ Offset(0.0f,height1 - p1)*/
                    return Offset.Zero
                }

                /*override fun onPostScroll(
                    consumed: Offset,
                    available: Offset,
                    source: NestedScrollSource
                ): Offset {
                    Log.d("fljlflsjfd2","${consumed.y}")
                    if(progress<0.5){
                        height = maxHeight
                    }
                    else{
                        height = minHeight
                    }
                    return super.onPostScroll(consumed, available, source)
                }*/

                /*override suspend fun onPreFling(
                    available: Velocity
                ): Velocity {
                    //Log.d("flsjfldf","${available.y},$ddy")
                    return if((height==minHeight&&ddy<0)||(height==maxHeight&&ddy>0)){
                        Velocity.Zero
                    } else{
                        available
                    }
                }*/

                /*override suspend fun onPostFling(
                    consumed: Velocity,
                    available: Velocity
                ): Velocity {
                    Log.d("fdlkfdlf","${consumed.y},${available.y}")
                    swipeableState.performDrag(available.y)
                    if(available.y>0){
                        height = minHeight
                        progress = (height - minHeight)/(maxHeight - minHeight)
                    }
                    else if(available.y<0){
                        height = maxHeight
                        progress = (height - minHeight)/(maxHeight - minHeight)
                    }
                    return super.onPostFling(consumed, available)
                }*/
            }
        }
        Box(
        ) {
            Column(
                modifier = Modifier
                    .nestedScroll(nestedScrollConnection)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        /*.swipeable(
                            swipeableState,
                            anchors = mapOf(
                                minHeight to States.COLLAPSED,
                                maxHeight to States.EXPANDED,

                            ),
                            thresholds =  { _,_->
                                FractionalThreshold(0.5f)
                            },
                            orientation = Orientation.Vertical
                        )*/
                        .background(Color.White)
                ){
                    Box {
                        Column(){
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(with(LocalDensity.current) { height.toDp() })
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
                                Column(
                                    modifier = Modifier
                                        .align(Alignment.TopCenter)
                                        .padding(top = 83.dep())
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
                        Box(
                            modifier = Modifier
                                .padding(
                                    top = //with(LocalDensity.current) { height1.toDp() }
                                    (367 - 177 * (1-progress)).dep()
                                )
                                .padding(
                                    horizontal = 16.dep()
                                )
                        ){
                            ContactSearchBar()
                        }
                    }
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 78.dep())
                            .alpha(progress)
                    ){
                        Row(){
                            YouWillGetCard(whole = "00", decimal = "00")
                            Spacer(modifier = Modifier.width(22.dep()))
                            YouWillPayCard(whole = "00", decimal = "00")
                        }
                    }
                }

                val tabsList = listOf(ContactTabs.Groups.name, ContactTabs.Friends.name)
                var selectedIndex by remember { mutableStateOf(0) }
                29.SpaceY()
                TabRow(
                    selectedTabIndex = selectedIndex,
                    backgroundColor = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dep())
                        .height(50.dep()),
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
                    thickness = 1.dp
                )
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

    enum class ContactTabs {
        Recent,
        Groups,
        Friends,
        Contact
    }

    @Composable
    fun Number.SpaceX(fullWidth: Float = localFullWidth.current){
        Spacer(modifier = Modifier.width(this.dep(fullWidth)))
    }

    @Composable
    fun Number.SpaceY(fullWidth: Float = localFullWidth.current){
        Spacer(modifier = Modifier.height(this.dep(fullWidth)))
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
}

@Composable
fun ContactSearchBar() {
    val (value, onValueChange) = remember { mutableStateOf("") }

    Box(contentAlignment = Alignment.CenterStart){
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(44f.dep())
                .background(Color(0xFFf9f9f9), RoundedCornerShape(8.dep())),
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(fontSize = 11f.sep()),
            shape = RoundedCornerShape(8f.dep()),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                backgroundColor = Color.Transparent,
                cursorColor = Color.DarkGray
            ),
            leadingIcon = {
                Row(verticalAlignment = Alignment.CenterVertically,) {
                    Spacer(modifier = Modifier.width(20.5f.dep()))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = "search",
                        tint = Color.Unspecified
                    )
                    Spacer(modifier = Modifier.width(12.5f.dep()))
                    Divider(
                        color = colorResource(id = R.color.lightgrey4),
                        modifier = Modifier
                            .height(13.dep())
                            .width(1.dep())
                    )
                    Spacer(modifier = Modifier.width(15f.dep()))
                }
            },
        )

        AnimatedVisibility(visible = value.isEmpty()) {
            Text(
                text = "Search groups or contacts",
                fontSize = 11f .sep(),
                color = colorResource(id = R.color.lightgrey4),
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(start = 72.dep() )
            )
        }
    }
}

enum class States {
    EXPANDED,
    COLLAPSED
}

@ExperimentalMaterialApi
@Composable
fun FullHeightBottomSheet(
    header: @Composable () -> Unit,
    body: @Composable () -> Unit
) {
    val swipeableState = rememberSwipeableState(initialValue = States.EXPANDED)
    val scrollState = rememberScrollState()

    BoxWithConstraints {
        val constraintsScope = this
        val maxHeight = with(LocalDensity.current) {
            constraintsScope.maxHeight.toPx()
        }

        val connection = remember {
            object : NestedScrollConnection {

                override fun onPreScroll(
                    available: Offset,
                    source: NestedScrollSource
                ): Offset {
                    val delta = available.y
                    return if (delta < 0) {
                        swipeableState.performDrag(delta).toOffset()
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
                    return swipeableState.performDrag(delta).toOffset()
                }

                override suspend fun onPreFling(available: Velocity): Velocity {
                    return if (available.y < 0 && scrollState.value == 0) {
                        swipeableState.performFling(available.y)
                        available
                    } else {
                        Velocity.Zero
                    }
                }

                override suspend fun onPostFling(
                    consumed: Velocity,
                    available: Velocity
                ): Velocity {
                    swipeableState.performFling(velocity = available.y)
                    return super.onPostFling(consumed, available)
                }

                private fun Float.toOffset() = Offset(0f, this)
            }
        }

        Box(
            Modifier
                .swipeable(
                    state = swipeableState,
                    orientation = Orientation.Vertical,
                    anchors = mapOf(
                        0f to States.EXPANDED,
                        maxHeight to States.COLLAPSED,
                    )
                )
                .nestedScroll(connection)
                .offset {
                    IntOffset(
                        0,
                        swipeableState.offset.value.roundToInt()
                    )
                }
        ) {
            Column(
                Modifier
                    .fillMaxHeight()
                    .background(Color.White)
            ) {
                header()
                Box(
                    Modifier
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                ) {
                    body()
                }
            }
        }
    }
}