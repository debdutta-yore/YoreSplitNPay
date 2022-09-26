package co.yore.splitnpay.activity

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
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import co.yore.splitnpay.libs.LocalNotificationService
import co.yore.splitnpay.libs.LocalResolver
import co.yore.splitnpay.libs.StatusBarColorControl
import co.yore.splitnpay.locals.localFullWidth
import co.yore.splitnpay.pages.SplitPageFull
import co.yore.splitnpay.ui.theme.YoreSplitNPayTheme
import co.yore.splitnpay.viewModels.SplitPageViewModel

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            YoreSplitNPayTheme {
                // A surface container using the 'background' color from the theme
                CompositionLocalProvider(localFullWidth provides 360f) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) {
                        val vm: SplitPageViewModel = viewModel()
                        CompositionLocalProvider(
                            LocalResolver provides vm.resolver,
                            LocalNotificationService provides vm.notifier
                        ) {
                            StatusBarColorControl()
                            SplitPageFull()
                        }
                    }
                }
            }
        }
    }
}







