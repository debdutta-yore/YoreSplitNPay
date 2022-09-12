package co.yore.splitnpay

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
                        ){
                            DatePickerUI()
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
