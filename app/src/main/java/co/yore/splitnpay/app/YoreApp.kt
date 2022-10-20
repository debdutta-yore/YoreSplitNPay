package co.yore.splitnpay.app

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.pages.*
import co.yore.splitnpay.viewModels.*
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalAnimationApi::class)
    @Composable
    fun YoreApp() {
        val navController = rememberAnimatedNavController()
        AnimatedNavHost(navController, startDestination = "group_chat_page") {
            yoreComposable(
                "split_page"
            ){
                YorePage(
                    navController,
                    suffix = "split_page",
                    wvm = viewModel<SplitPageViewModel>()
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
                    wvm = viewModel<MemberSelectionPageViewModel>()
                ) {
                    MemberSelectionPage_g5024t()
                }
            }

            yoreComposable(
                "group_creation"
            ){
                YorePage(
                    navController,
                    suffix = "group_creation",
                    wvm = viewModel<GroupCreationPageViewModel>()
                ) {
                    GroupCreationScreen()
                }
            }

            yoreComposable("group_chat_page") {
                YorePage(
                    navController = navController,
                    suffix = "group_chat_page",
                    wvm = viewModel<GroupChatViewModel>()
                ) {
                    GroupChatScreen()
                }
            }

            yoreComposable("group_manage") {
                YorePage(
                    navController = navController,
                    suffix = "group_manage",
                    wvm = viewModel<ManageViewModel>()
                ) {
                    GroupManagePage()
                }
            }

            yoreComposable("split_review_page") {
                YorePage(
                    navController = navController,
                    suffix = "split_review_page",
                    wvm = viewModel<SplitReviewViewModel>()
                ) {
                    SplitDetailsScreen()
                }
            }
        }
    }


