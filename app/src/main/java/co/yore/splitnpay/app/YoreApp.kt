package co.yore.splitnpay.app

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.*
import co.yore.splitnpay.R
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.libs.jerokit.*
import co.yore.splitnpay.libs.kontakts.Kontakts
import co.yore.splitnpay.libs.kontakts.MergedContact
import co.yore.splitnpay.pages.*
import co.yore.splitnpay.pages.screens.*
import co.yore.splitnpay.viewModels.*
import coil.compose.AsyncImage
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


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
                    Button(onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            /*val result = GrpcServer.ExpenseService.createExpense(
                                accountId = "8967114927",
                                categoryId = "d856f982-f4a3-4beb-9031-1a9750b01dc6",
                                shareType = GrpcServer.ShareType.Unequal,
                                amount = 10000.0,
                                description = "Weekend Food",
                                receiptUrl = "",
                                calculationMethod = GrpcServer.CalculationMethod.Proportionate,
                                expenseMembers = listOf(
                                    GrpcServer.ExpenseMember(
                                        phone = "8967114927",
                                        name = "Debdutta Panda",
                                        image = "",
                                        initialPaidAmount = 6000.0,
                                        shareAmount = 2000.0,
                                        toPay = listOf()
                                    ),
                                    GrpcServer.ExpenseMember(
                                        phone = "6290721134",
                                        name = "Suman",
                                        image = "",
                                        initialPaidAmount = 4000.0,
                                        shareAmount = 2000.0,
                                        toPay = listOf()
                                    ),
                                    GrpcServer.ExpenseMember(
                                        phone = "1234567890",
                                        name = "Raju",
                                        image = "",
                                        initialPaidAmount = 0.0,
                                        shareAmount = 2000.0,
                                        toPay = listOf()
                                    ),
                                    GrpcServer.ExpenseMember(
                                        phone = "1234567891",
                                        name = "Minu",
                                        image = "",
                                        initialPaidAmount = 0.0,
                                        shareAmount = 2000.0,
                                        toPay = listOf()
                                    ),
                                    GrpcServer.ExpenseMember(
                                        phone = "1234567892",
                                        name = "Hemu",
                                        image = "",
                                        initialPaidAmount = 0.0,
                                        shareAmount = 2000.0,
                                        toPay = listOf()
                                    )
                                ),
                                groupId = "",
                                groupName = "",
                                groupImageUrl = ""
                            )
                            Log.d("fldfdlfdfd","${result.eid},${result.gid}")*/
                            /*val response = GrpcServer.ExpenseService.getSettlementPreview(
                                GrpcServer.CalculationMethod.Proportionate,
                                listOf(
                                    GrpcServer.ExpenseMember(
                                        phone = "8967114927",
                                        initialPaidAmount = 6000.0,
                                        shareAmount = 2000.0
                                    ),
                                    GrpcServer.ExpenseMember(
                                        phone = "8967114928",
                                        initialPaidAmount = 0.0,
                                        shareAmount = 2000.0
                                    )
                                )
                            )*/
                            /*val response = GrpcServer.GroupService.updateGroup(
                                groupId = "d89260dc-82d5-4a20-aeee-6d910d9fa4a7",
                                accountId = "8967114927",
                                groupName = "MyNewGroupName",
                                groupImageUrl = ""
                            )*/
                            /*val response = GrpcServer
                                .CategoryService
                                .createCategory(
                                    accountId = "1234567890",
                                    name = "MyCategory",
                                    isEnabled = true
                                )
                            Log.d("fldfdfd","${response.id}")*/

                            /*val response = GrpcServer
                                .GroupService
                                .createGroup(
                                    accountId = "1234567890",
                                    groupName = "MyGroup",
                                    groupImageUrl = "image_url",
                                    members = listOf(
                                        GrpcServer.ExpenseMember(
                                            phone = "1234567890",
                                            name = "member_name1",
                                            image = "image_url1"
                                        ),
                                        GrpcServer.ExpenseMember(
                                            phone = "1234567891",
                                            name = "member_name2",
                                            image = "image_url2"
                                        ),
                                        GrpcServer.ExpenseMember(
                                            phone = "1234567892",
                                            name = "member_name3",
                                            image = "image_url3"
                                        )
                                    )
                                )
                            Log.d("fldfdfd","${response.id}")*/

                            /*val response = GrpcServer
                                .ExpenseService
                                .listExpenses(
                                    accountId = "8967114927",
                                    needSplitTotal = true
                                )
                            Log.d("fldfdfd","${response.resultsCount}")*/
                        }
                    }) {
                        Text("Grpc Test")
                    }
                    ContactUI()
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

@Composable
fun ContactUI(
    notifier: NotificationService = notifier(),
    list: List<MergedContact> = listState(key = "contacts")
){
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Button(onClick = {
            notifier.notify("contacts")
        }) {
            Text("Contacts")
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ){
            items(
                list,
                key = {
                    it.phone
                }
            ){
                Column {
                    Text(
                        it.name,
                        fontSize = if(it.name == "Asdd") 40.sep() else 14.sep()
                    )
                    Text(it.phone)
                    Text(it.email)
                    AsyncImage(
                        model = it.image?: R.drawable.user_dummy4,
                        contentDescription = "",
                        placeholder = painterResource(id = R.drawable.user_dummy4),
                        modifier = Modifier.size(75.dep())
                    )
                    Divider()
                }

            }
        }
    }
}
