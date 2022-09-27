package co.yore.splitnpay.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import co.yore.splitnpay.locals.localDesignWidth
import co.yore.splitnpay.pages.AddMembersScreen_g5024t
import co.yore.splitnpay.pages.SplitPageWithViewModel
import co.yore.splitnpay.ui.theme.YoreSplitNPayTheme
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
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
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) {
                        val navController = rememberAnimatedNavController()
                        AnimatedNavHost(navController, startDestination = "split_page") {
                            composable(
                                "split_page"
                            ){
                                SplitPageWithViewModel()
                            }
                            composable(
                                "add_member"
                            ){
                                AddMembersScreen_g5024t()
                            }
                        }
                    }
                }
            }
        }
    }
}







