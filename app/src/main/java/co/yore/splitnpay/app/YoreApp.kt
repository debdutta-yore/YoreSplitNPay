package co.yore.splitnpay.app

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.pages.*
import co.yore.splitnpay.viewModels.*
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalAnimationApi::class)
inline fun <reified T : ViewModel>NavGraphBuilder.YoreScreen(
    navController: NavHostController,
    route: String,
    crossinline content: @Composable () -> Unit
){
    yoreComposable(
        route
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

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun YoreApp() {
    val navController = rememberAnimatedNavController()
    AnimatedNavHost(navController, startDestination = "split_review_page") {
        YoreScreen<SplitPageViewModel>(
            navController = navController,
            route = "split_page"
        ) {
            SplitPage()
        }
        YoreScreen<MemberSelectionPageViewModel>(
            navController = navController,
            route = "split_with_page"
        ) {
            MemberSelectionPage_g5024t()
        }
        YoreScreen<GroupCreationPageViewModel>(
            navController = navController,
            route = "group_creation"
        ) {
            GroupCreationScreen()
        }
        YoreScreen<GroupChatViewModel>(
            navController = navController,
            route = "group_chat_page"
        ) {
            GroupChatScreen()
        }
        YoreScreen<ManageViewModel>(
            navController = navController,
            route = "group_manage"
        ) {
            GroupManagePage()
        }
        YoreScreen<SplitReviewViewModel>(
            navController = navController,
            route = "split_review_page"
        ) {
            SplitDetailsScreen()
        }
        YoreScreen<SplitCardDetailsViewModel>(
            navController = navController,
            route = "split_review_page"
        ) {
            SplitCardDetailPage()
        }
    }
}
