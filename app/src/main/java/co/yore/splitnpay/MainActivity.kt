package co.yore.splitnpay

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                        /*val vm: SplitPageViewModel = viewModel()
                        CompositionLocalProvider(
                            LocalNotificationService provides vm.notifier,
                            LocalResolver provides vm.resolver
                        ) {
                            val systemUiController = rememberSystemUiController()
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
                            }
                            SplitPageFull()
                        }*/

                        Column(){
                            var progress by remember {
                                mutableStateOf(0f)
                            }
                            AnimatedTextContent(
                                from = "i am a boy",
                                to = "i am a girl",
                                progress = progress,
                                spanStyle = SpanStyle(
                                    fontSize = 48.sp
                                ),
                                color = Color.Red,
                                diffSettings = AnimatableTextPart.DiffSettings(
                                    scheme = AnimatableTextPart.Scheme.EFFICIENT,
                                    editCost = 40
                                )
                            )
                            Slider(
                                value = progress,
                                onValueChange = {
                                    progress = it
                                },
                                valueRange = 0f..1f,
                                modifier = Modifier.padding(24.dp)
                            )
                            Text("$progress")
                        }
                    }
                }
            }
        }
    }
}




