package co.yore.splitnpay.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import co.yore.splitnpay.libs.YorePage
import co.yore.splitnpay.locals.localDesignWidth
import co.yore.splitnpay.pages.AddMembersScreen_g5024t
import co.yore.splitnpay.pages.SplitPage
import co.yore.splitnpay.ui.theme.YoreSplitNPayTheme
import co.yore.splitnpay.viewModels.SplitPageViewModel
import co.yore.splitnpay.viewModels.SplitWithPageViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

class MainActivity : ComponentActivity() {
    private val destinaitonChangedListener:
            NavController.OnDestinationChangedListener =
        NavController.OnDestinationChangedListener { controller, destination, arguments ->
            Log.d("fldkjffd","$destination,$arguments")
        }
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
                        val navController = rememberAnimatedNavController()
                        navController.removeOnDestinationChangedListener(destinaitonChangedListener)
                        navController.addOnDestinationChangedListener(destinaitonChangedListener)
                        AnimatedNavHost(navController, startDestination = "split_page") {
                            composable(
                                "split_page",
                                enterTransition = {
                                    slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                                },
                                exitTransition = {
                                    slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                                },
                                popEnterTransition = {
                                    slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                                },
                                popExitTransition = {
                                    slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                                }
                            ){
                                YorePage(
                                    navController,
                                    suffix = "split_page",
                                    wvm = viewModel<SplitPageViewModel>()
                                ) {
                                    SplitPage()
                                }
                            }
                            composable(
                                "split_with_page",
                                enterTransition = {
                                    slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                                },
                                exitTransition = {
                                    slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                                },
                                popEnterTransition = {
                                    slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                                },
                                popExitTransition = {
                                    slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                                }
                            ){
                                YorePage(
                                    navController,
                                    suffix = "split_with_page",
                                    wvm = viewModel<SplitWithPageViewModel>()
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







