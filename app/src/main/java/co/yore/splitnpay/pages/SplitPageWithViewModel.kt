package co.yore.splitnpay.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.viewModels.SplitPageViewModel

@Composable
fun SplitPageWithViewModel() {
    val vm: SplitPageViewModel = viewModel()
    CompositionLocalProvider(
        LocalResolver provides vm.resolver,
        LocalNotificationService provides vm.notifier
    ) {
        StatusBarColorControl("split_page")
        SplitPage()
    }
}

interface WirelessViewModelInterface{
    val resolver: Resolver
    val notifier: NotificationService
}

@Composable
fun YorePage(
    suffix: String,
    wvm: WirelessViewModelInterface,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalResolver provides wvm.resolver,
        LocalNotificationService provides wvm.notifier
    ) {
        StatusBarColorControl(suffix)
        content()
        BackHandle(suffix)
    }
}