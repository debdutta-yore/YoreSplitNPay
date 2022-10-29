package co.yore.splitnpay.app

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.*
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.pages.*
import co.yore.splitnpay.viewModels.*
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalAnimationApi::class)
inline fun <reified T : ViewModel>NavGraphBuilder.YoreScreen(
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

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun YoreApp() {
    val navController = rememberAnimatedNavController()
    AnimatedNavHost(navController, startDestination = "main") {
        YoreScreen<SplitPageViewModel>(
            navController = navController,
            route = "main"
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                Button(onClick = {
                    navController.navigate("split_page")
                }) {
                    Text("Split N Pay")
                }
            }
        }
        YoreScreen<SplitPageViewModel>(
            navController = navController,
            route = "split_page"
        ) {
            SplitScreen()
        }
        YoreScreen<MemberSelectionPageViewModel>(
            navController = navController,
            route = "split_with_page?split={split}",
            arguments = listOf(
                navArgument("split"){
                    type = NavType.BoolType
                    defaultValue = false
                }
            )
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
            route = "split_review_page?asGroup={asGroup}",
            arguments = listOf(
                navArgument("asGroup"){
                    type = NavType.BoolType
                    defaultValue = false
                }
            )
        ) {
            SplitDetailsScreen()
        }
        YoreScreen<SplitCardDetailsViewModel>(
            navController = navController,
            route = "split_card_details"
        ) {
            SplitCardDetailPage()
        }
        YoreScreen<IndividualManagePageViewModel>(
            navController = navController,
            route = "individual_manage_page"
        ) {
            IndividualManageScreen()
        }
        YoreScreen<IndividualSummaryViewModel>(
            navController = navController,
            route = "individual_summary"
        ) {
            IndividualSummary()
        }
        YoreScreen<PaymentSuccessViewModel>(
            navController = navController,
            route = "payment_success"
        ) {
            PaymentSuccessScreen()
        }
        YoreScreen<IndividualChatViewModel>(
            navController = navController,
            route = "individual_chat"
        ) {
            IndividualChatScreen()
        }
        YoreScreen<GroupSummaryViewModel>(
            navController = navController,
            route = "group_split_summary"
        ) {
            GroupSplitSummary()
        }
    }
}


