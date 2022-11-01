package co.yore.splitnpay.app

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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

object Routes{
    data class Route(
        val name: String,
        val args: String = ""
    ){
        val full get() = "$name$args"
    }
    val main = Route("main")
    val splitPage = Route("split_page", "?splitAdded={splitAdded}&blank={blank}")
    val splitWithPage = Route("split_with_page", "?split={split}")
    val groupCreation = Route("group_creation","?split={split}")
    val groupChatPage = Route("group_chat_page")
    val groupManagePage = Route("group_manage")
    val splitReviewPage = Route("split_review_page", "?asGroup={asGroup}")
    val splitCardDetailsPage = Route("split_card_details")
    val individualManagePage = Route("individual_manage_page")
    val individualSummary = Route("individual_summary")
    val paymentSuccess = Route("payment_success")
    val individualChat = Route("individual_chat")
    val groupSplitSummary = Route("group_split_summary")
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun YoreApp() {
    val navController = rememberAnimatedNavController()
    AnimatedNavHost(
        navController,
        startDestination = Routes.main.full
    ) {
        YoreScreen<MainViewModel>(
            navController = navController,
            route = Routes.main.full
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Button(onClick = {
                        navController.navigate(Routes.splitPage.name)
                    }) {
                        Text("Split N Pay")
                    }
                    Button(onClick = {
                        navController.navigate("${Routes.splitPage.name}?blank=true")
                    }) {
                        Text("Split Blank")
                    }
                }
            }
        }
        YoreScreen<MemberSelectionPageViewModel>(
            navController = navController,
            route = Routes.splitWithPage.full,
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
            route = Routes.splitPage.full,
            arguments = listOf(
                navArgument("splitAdded"){
                    type = NavType.BoolType
                    defaultValue = false
                },
                navArgument("blank"){
                    type = NavType.BoolType
                    defaultValue = false
                }
            )
        ) {
            SplitScreen()
        }
        YoreScreen<GroupCreationPageViewModel>(
            navController = navController,
            route = Routes.groupCreation.full,
            arguments = listOf(
                navArgument("split"){
                    type = NavType.BoolType
                    defaultValue = false
                }
            )
        ) {
            GroupCreationScreen()
        }
        YoreScreen<GroupChatViewModel>(
            navController = navController,
            route = Routes.groupChatPage.full
        ) {
            GroupChatScreen()
        }
        YoreScreen<ManageViewModel>(
            navController = navController,
            route = Routes.groupManagePage.full
        ) {
            GroupManageScreen()
        }
        YoreScreen<SplitReviewViewModel>(
            navController = navController,
            route = Routes.splitReviewPage.full,
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
            route = Routes.splitCardDetailsPage.full
        ) {
            SplitCardDetailScreen()
        }
        YoreScreen<IndividualManagePageViewModel>(
            navController = navController,
            route = Routes.individualManagePage.full
        ) {
            IndividualManageScreen()
        }
        YoreScreen<IndividualSummaryViewModel>(
            navController = navController,
            route = Routes.individualSummary.full
        ) {
            IndividualSummaryScreen()
        }
        YoreScreen<PaymentSuccessViewModel>(
            navController = navController,
            route = Routes.paymentSuccess.full
        ) {
            PaymentSuccessScreen()
        }
        YoreScreen<IndividualChatViewModel>(
            navController = navController,
            route = Routes.individualChat.full
        ) {
            IndividualChatScreen()
        }
        YoreScreen<GroupSummaryViewModel>(
            navController = navController,
            route = Routes.groupSplitSummary.full
        ) {
            GroupSplitSummaryScreen()
        }
    }
}
