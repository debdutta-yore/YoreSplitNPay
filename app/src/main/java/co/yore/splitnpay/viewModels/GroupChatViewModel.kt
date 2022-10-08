package co.yore.splitnpay.viewModels

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.models.BillTransaction
import co.yore.splitnpay.models.DataIds
import co.yore.splitnpay.models.TransactionStatus
import co.yore.splitnpay.models.TransactionType
import co.yore.splitnpay.viewModels.MembersMock.transaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object MembersMock {
    val transaction = BillTransaction(
        transactionType = TransactionType.Received,
        transactionStatus = TransactionStatus.Started,
        billTotal = 10000f,
        allPaidReceivedTotal = 3500f,
        paidReceived = 1000f,
        willPayReceive = 3000f,
        amountLeft = 6500f,
        transactionTime = "12:56 pm",
        totalTransactions = 5,
        completedTransactions = 5,
        isSingleChat = false
    )
}

data class Group(
    val name: String,
    val imageUrl: String = "",
    val creationDate: String = "May 9th 2022",
    val amount: Float
)

interface GroupRepository {
    suspend fun getBillTransactions(): List<BillTransaction>

    suspend fun getBillTransactions(number: Int): List<BillTransaction>
}

class GroupsMock : GroupRepository {
    val groupsList = emptyList<Group>()
    val group = listOf<Group>(
        Group("Office Buddies", "https://i.pravatar.cc/300", amount = 3000f),
        Group("Office Buddies", "https://i.pravatar.cc/300", amount = 3000f),
    )

    override suspend fun getBillTransactions(): List<BillTransaction> {
        return listOf(transaction)
    }

    override suspend fun getBillTransactions(number: Int): List<BillTransaction> {
        return listOf(transaction)
    }
}

data class GroupCreationEvent(
    val creator: String,
    val groupName: String
)

data class Conversation(
    val type: Type,
    val data: Any
){
    enum class Type{
        TRANSACTION,
        DATE,
        CREATION,
        STATUS,
        CHAT
    }
}

class GroupChatViewModel(
    private val repo: GroupRepository = GroupsMock()
) : ViewModel(), WirelessViewModelInterface {
    override val resolver = Resolver()
    override val navigation = Navigation()
    override val permissionHandler = PermissionHandler()
    override val resultingActivityHandler = ResultingActivityHandler()
    @OptIn(ExperimentalMaterialApi::class)
    override val sheetHandler = SheetHandler(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )

    //////////////////////////////////////////
    private val _conversations = mutableStateListOf<Conversation>()
    private val _statusBarColor = mutableStateOf<StatusBarColor?>(null)
    private val _groupName = mutableStateOf("Office Buddies")
    private val _groupImage = mutableStateOf("https://i.pravatar.cc/300")
    private val _groupAmount = mutableStateOf(5000f)
    private val _groupCreationDate = mutableStateOf("May 9th 2022")
    private val _isSingleChat = mutableStateOf(false)
    private val _filteredMemberImage = mutableStateOf("https://i.pravatar.cc/300")
    private val _filteredMemberName = mutableStateOf("Manisha Roy")
    private val _chatMessage = mutableStateOf("")
    private val _searchText = mutableStateOf("")

    //////////////////////////////////////////
    @OptIn(ExperimentalMaterialApi::class)
    override val notifier = NotificationService { id, arg ->
        when (id) {
            WirelessViewModelInterface.startupNotification->{

            }
            DataIds.back -> {
                navigation.scope { navHostController, lifecycleOwner, toaster ->
                    navHostController.popBackStack()
                }
            }
            DataIds.groupAmount -> {
                _groupAmount.value = (arg as? Float) ?: 0f
            }
            DataIds.filter->{
                sheetHandler.state {
                    show()
                }
            }
            DataIds.settleClick -> {
                /*navigation.scope { navHostController, lifecycleOwner, toaster ->
                    navHostController.navigate("settle_sheet")
                }*/
            }
            DataIds.summaryClick -> {
                /*navigation.scope { navHostController, lifecycleOwner, toaster ->
                    navHostController.navigate("split_summary_balance")
                }*/
            }
            DataIds.manageClick -> {
                /*navigation.scope { navHostController, lifecycleOwner, toaster ->
                    navHostController.navigate("manage_page")
                    navHostController.popBackStack()
                }*/
            }
            DataIds.chatMessage -> {
                _chatMessage.value = (arg as? String) ?: ""
            }
            DataIds.searchText -> {
                _searchText.value = (arg as? String) ?: ""
            }
            DataIds.cardClick -> {
                /*navigation.scope { navHostController, lifecycleOwner, toaster ->
                    navHostController.navigate("split_card_details_screen")
                }*/
            }
        }
    }
    /////////////////////////////////////////

    init {
        resolver.addAll(
            DataIds.conversations to _conversations,
            DataIds.statusBarColor to _statusBarColor,
            DataIds.groupName to _groupName,
            DataIds.groupImage to _groupImage,
            DataIds.groupAmount to _groupAmount,
            DataIds.groupCreationDate to _groupCreationDate,
            DataIds.isSingleChat to _isSingleChat,
            DataIds.filteredMemberName to _filteredMemberName,
            DataIds.filteredMemberImage to _filteredMemberImage,
            DataIds.chatMessage to _chatMessage,
            DataIds.searchText to _searchText,
        )
        viewModelScope.launch(Dispatchers.IO) {
            val billTransactions = repo.getBillTransactions()
            withContext(Dispatchers.Main) {
                _conversations.add(
                    Conversation(
                        type = Conversation.Type.DATE,
                        data = "May 9th 2022"
                    )
                )
                _conversations.add(
                    Conversation(
                        type = Conversation.Type.CREATION,
                        data = GroupCreationEvent(
                            creator = "You",
                            groupName = "Office buddies"
                        )
                    )
                )
                _conversations.addAll(
                    billTransactions.map {
                        Conversation(
                            type = Conversation.Type.TRANSACTION,
                            data = it
                        )
                    }
                )
            }
        }
        _statusBarColor.value = StatusBarColor(
            color = StatusBarGreen,
            darkIcons = true
        )
    }
}

val StatusBarGreen = Color(0xff00CEC3)