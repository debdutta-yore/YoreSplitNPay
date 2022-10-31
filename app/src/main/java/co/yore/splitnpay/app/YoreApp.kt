package co.yore.splitnpay.app

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.*
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.libs.jerokit.YoreScreen
import co.yore.splitnpay.pages.*
import co.yore.splitnpay.pages.screens.*
import co.yore.splitnpay.viewModels.*
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun YoreApp() {
    val navController = rememberAnimatedNavController()
    AnimatedNavHost(
        navController,
        startDestination = "main"
    ) {
        YoreScreen<MainViewModel>(
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
            MemberSelectionScreen()
        }
        YoreScreen<SplitPageViewModel>(
            navController = navController,
            route = "split_page"
        ) {
            SplitScreen()
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
            GroupManageScreen()
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
            SplitCardDetailScreen()
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
            IndividualSummaryScreen()
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
            GroupSplitSummaryScreen()
        }
    }
}
