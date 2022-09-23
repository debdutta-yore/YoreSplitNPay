package co.yore.splitnpay

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import co.yore.splitnpay.ui.theme.YoreSplitNPayTheme

object SplitPageColor{
    val noneColor = Color(0xff7589A4)
    val getColor = Color(0xff00CEC3)
    val payColor = Color(0xffDB3665)
}


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
                        /*val systemUiController = rememberSystemUiController()
                        val state = tState<SplitPageState>(SplitDataIds.ultimateState)
                        LaunchedEffect(key1 = state.value) {
                            val color = when(state.value){
                                SplitPageState.GET -> SplitPageColor.getColor
                                SplitPageState.PAY -> SplitPageColor.payColor
                                SplitPageState.NONE -> SplitPageColor.noneColor
                            }
                            systemUiController.setStatusBarColor(
                                color = color,
                                darkIcons = false
                            )
                        }*/
                        /*val vm: SplitWithPageViewModel = viewModel()
                        CompositionLocalProvider(
                            LocalResolver provides vm.resolver,
                            LocalNotificationService provides vm.notifier
                        ) {
                            AddMembersScreen_g5024t()
                        }*/

                        AnimatedLazyColumnExample()
                    }
                }
            }
        }
    }
}

class ExampleViewModel: ViewModel() {
    val adapter: LazyAnimatedColumnAdapter<String> = LazyAnimatedColumnAdapter(emptyList(), isReversed = false)

    var counter: Int = 0

    fun addItem() {
        adapter.addItem("Item :$counter")
        counter ++
    }

    fun removeItem(index: Int) {
        adapter.removeItem(index)
    }
}

@Composable
fun AnimatedLazyColumnExample() {
    val viewModel: ExampleViewModel = viewModel()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.weight(1f)) {
            val listState = rememberLazyListState()

            AnimatedLazyColumn(
                adapter = viewModel.adapter,
                state = listState,
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) { data,t ->
                Column(
                    modifier = Modifier
                        .padding(vertical = 6.dp, horizontal = 12.dp)
                        .fillMaxWidth()
                        .background(Color.Red)
                        .padding(vertical = 20.dp)
                    ,
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(text = data.toString(), fontSize = 16.sp)
                }
            }

        }
        Button(onClick = { viewModel.addItem()}) {
            Text(text = "Add item")
        }
    }
}




