package co.yore.splitnpay.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import co.yore.splitnpay.libs.YorePage
import co.yore.splitnpay.libs.yoreComposable
import co.yore.splitnpay.locals.localDesignWidth
import co.yore.splitnpay.pages.AddMembersScreen_g5024t
import co.yore.splitnpay.pages.SplitPage
import co.yore.splitnpay.ui.theme.YoreSplitNPayTheme
import co.yore.splitnpay.viewModels.SplitPageViewModel
import co.yore.splitnpay.viewModels.SplitWithPageViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            YoreSplitNPayTheme {
                CompositionLocalProvider(localDesignWidth provides 360f) {
                    Surface(
                        modifier = Modifier.safeDrawingPadding().fillMaxSize(),
                        color = MaterialTheme.colors.background,
                    ) {
                        val splitPageViewModel: SplitPageViewModel = viewModel()
                        val splitWithPageViewModel: SplitWithPageViewModel = viewModel()
                        val navController = rememberAnimatedNavController()
                        AnimatedNavHost(navController, startDestination = "split_page") {
                            yoreComposable(
                                "split_page"
                            ){
                                YorePage(
                                    navController,
                                    suffix = "split_page",
                                    wvm = splitPageViewModel
                                ) {
                                    SplitPage()
                                }
                            }
                            yoreComposable(
                                "split_with_page",
                            ){
                                YorePage(
                                    navController,
                                    suffix = "split_with_page",
                                    wvm = splitWithPageViewModel
                                ) {
                                    AddMembersScreen_g5024t()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}







