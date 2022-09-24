package co.yore.splitnpay

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import co.yore.splitnpay.addmembers.SplitWithPageViewModel
import co.yore.splitnpay.ui.theme.YoreSplitNPayTheme
import co.yore.splitnpay.addmembers.AddMembersScreen_g5024t

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
                        val vm: SplitWithPageViewModel = viewModel()
                        CompositionLocalProvider(
                            LocalResolver provides vm.resolver,
                            LocalNotificationService provides vm.notifier
                        ) {
                            AddMembersScreen_g5024t()
                        }
                    }
                }
            }
        }
    }
}





