package co.yore.splitnpay.libs.jerokit

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

@OptIn(ExperimentalAnimationApi::class)
inline fun <reified T : ViewModel> NavGraphBuilder.YoreScreen(
    navController: NavHostController,
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    crossinline content: @Composable () -> Unit
){
    yoreComposable(
        route,
        arguments = arguments,
        deepLinks = deepLinks
    ){
        YorePage(
            navController,
            suffix = route,
            wvm = viewModel<T>() as? WirelessViewModelInterface ?: return@yoreComposable
        ) {
            content()
        }
    }
}