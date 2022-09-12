package co.yore.splitnpay

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
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
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import dev.chrisbanes.snapper.rememberSnapperFlingBehavior
import kotlinx.coroutines.launch
import kotlin.concurrent.thread
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.math.sqrt

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
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
                        //SplitPageDemo()
                        Box(
                            contentAlignment = Alignment.Center
                        ){
                            DatePickerUI()
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalSnapperApi::class)
    @Composable
    private fun DatePickerUI() {
        var scrolled by remember{ mutableStateOf(0f)}
        val connection = remember {
            object : NestedScrollConnection {

                override fun onPreScroll(
                    available: Offset,
                    source: NestedScrollSource
                ): Offset {
                    scrolled += available.x
                    Log.d("fdlkfldfd","${available.x.toString()},$scrolled")
                    return Offset.Zero
                }
            }
        }
        /*
        val lazyListState = rememberLazyListState()
        LazyRow(
            modifier = Modifier
                //.nestedScroll(connection)
                .fillMaxWidth()
                .height(100.dp),
            flingBehavior = rememberSnapperFlingBehavior(lazyListState),
        ){
            items(31){
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(60.dp)
                        .border(
                            BorderStroke(
                                2.dp,
                                Color.Red
                            )
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(it.toString())
                    Text(it.toString())
                }
            }
        }*/

        val lazyListState: LazyListState = rememberLazyListState()

        BoxWithConstraints {
            /*val halfRowWidth = constraints.maxWidth / 2
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Green)
                    .nestedScroll(connection),
                state = lazyListState,
                flingBehavior = rememberSnapperFlingBehavior(lazyListState),
                contentPadding = PaddingValues(horizontal = 180.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                items(31){i->
                    val opacity by remember {
                        derivedStateOf {
                            val currentItemInfo = lazyListState.layoutInfo.visibleItemsInfo
                                .firstOrNull {
                                    it.index == i
                                }
                                ?: return@derivedStateOf 0.5f
                            val itemHalfSize = currentItemInfo.size / 2
                            (1f - minOf(1f, abs(currentItemInfo.offset + itemHalfSize - halfRowWidth).toFloat() / halfRowWidth) * 0.5f)
                        }
                    }
                    Column(
                        modifier = Modifier
                            .height(60.dp)
                            .alpha(opacity)
                            .width((60*opacity).dp)
                            .border(
                                BorderStroke(
                                    2.dp,
                                    Color.Red
                                )
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Text(i.toString())
                        Text(i.toString())
                    }
                }
            }*/
            Row(){

            }
            MonthDayPicker()
        }
    }

    @OptIn(ExperimentalSnapperApi::class)
    @Composable
    fun MonthDayPicker() {
        val items = remember {
            (1..31).map { it.toString() }
        }
        val listState = rememberLazyListState()
        BoxWithConstraints {
            val full = localFullWidth.current
            val box = 64
            val pad = (full - box)/2f
            val halfRowWidth = constraints.maxWidth / 2 /*+ with(LocalDensity.current){pad.dep().toPx()}*/
            Box(
                modifier = Modifier
                    .width(box.dp)
                    .height(89.dp)
                    .clip(RoundedCornerShape(13.dep()))
                    .background(Color(0xff1A79E5))
                    .align(Alignment.Center)
            ){

            }
            LazyRow(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = pad.dep()),
                flingBehavior = rememberSnapperFlingBehavior(listState),
            ) {
                itemsIndexed(items) { i, item ->
                    val opacity by remember {
                        derivedStateOf {
                            val currentItemInfo = listState.layoutInfo.visibleItemsInfo
                                .firstOrNull { it.index == i }
                                ?: return@derivedStateOf 0.5f
                            val itemHalfSize = currentItemInfo.size / 2
                            (1f - minOf(1f, abs(currentItemInfo.offset).toFloat() / halfRowWidth) * 0.5f)
                        }
                    }
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            //.scale(opacity)
                            .alpha(opacity)
                            .width((opacity * box).dep())
                            .height(89.dep())
                        //.scale(opacity)
                        //.background(Color.Blue)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            RobotoText(
                                "Week",
                                fontSize = (opacity*11).sep(),
                                fontWeight = FontWeight.Bold,
                                color = if(opacity in 0.9f..1f) Color.White else   Color(0xff243257)
                            )
                            Spacer(modifier = Modifier.height(12.dep()))
                            RobotoText(
                                i.toString(),
                                fontSize = (opacity*11).sep(),
                                fontWeight = FontWeight.Bold,
                                color = if(opacity in 0.9f..1f) Color.White else   Color(0xff1A79E5)
                            )
                        }
                    }
                }
            }

        }
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    private fun SplitPageDemo() {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {

                    }, backgroundColor = colorResource(id = R.color.pink),
                    contentColor = Color.White,
                    modifier = Modifier
                        .coloredShadow(
                            color = Color(0x4fff4077),
                            borderRadius = 100.dep(),
                            blurRadius = 12.dep(),
                            spread = 0f,
                            offsetX = 0.dep(),
                            offsetY = 3.dep(),
                        ),
                    shape = CircleShape,
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp,
                        hoveredElevation = 0.dp,
                        focusedElevation = 0.dp,
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_split_white),
                        "", tint = Color.Unspecified
                    )
                }
            }
        ) {
            SplitPage()
        }
    }
}
