package co.yore.splitnpay.viewModels

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.yore.splitnpay.R
import co.yore.splitnpay.components.components.*
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.models.*
import co.yore.splitnpay.models.Category
import co.yore.splitnpay.pages.ExpenseDatePickerBottomSheetModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class IndividualChatViewModel(
    private val repo: GroupRepository = GroupsMock(),
    private val settleRepository: SettleRepository = SettleRepositoryImpl()
) : ViewModel(), WirelessViewModelInterface {
    override val softInputMode = mutableStateOf(SoftInputMode.adjustNothing)

    override val resolver = Resolver()
    override val navigation = Navigation()
    override val permissionHandler = PermissionHandler()
    override val resultingActivityHandler = ResultingActivityHandler()

    private val selectedMember = mutableStateOf(-1)

    override val sheeting = Sheeting(
        sheetMap = mapOf(
            Sheets.BillTotalAndCategories to BillTotalAndCategoryBottomSheetModel(
                object : BillTotalAndCategoryBottomSheetModel.BillTotalBottomSheetModelCallback{
                    override suspend fun getCategories(): List<Category> {
                        val categories = repo.getAllCategories().toMutableList()
                        categories[0] = categories[0].copy(isSelected = true)
                        return categories
                    }

                    override suspend fun getBillTotalAmount(): String {
                        return ""
                    }

                    override suspend fun getDescription(): String {
                        return ""
                    }

                    override fun openAllCategories() {
                        mySheeting.sheets.value = Sheets.CategoriesEdit
                    }

                    override fun close() {
                        mySheeting.hide()
                    }

                    override fun onContinue(
                        billTotal: String,
                        billDescription: String,
                        category: Category
                    ) {
                        mySheeting.sheets.value = Sheets.DatePicker
                    }

                    override fun onRenameContinue(
                        category: Category,
                        name: String
                    ) {

                    }

                    override fun scope(): CoroutineScope {
                        return viewModelScope
                    }
                }
            ),
            Sheets.SettleSummaryManage to SettleSummaryManageBottomSheetModel(
                object : SettleSummaryManageBottomSheetModel.Callback{
                    override fun scope(): CoroutineScope {
                        return viewModelScope
                    }

                    override fun onContinue(arg: Any?) {
                        mySheeting.hide()
                        when (arg){
                            "Manage" -> navigation.scope { navHostController, lifecycleOwner, toaster ->
                                navHostController.navigate("group_manage")
                            }
                        }
                    }
                }
            ),
            Sheets.MemberFilter to MemberFilterBottomSheetModel(
                object : MemberFilterBottomSheetModel.Callback{
                    override fun scope(): CoroutineScope {
                        return viewModelScope
                    }

                    override suspend fun getMembers(): List<SingleItem> {
                        return repo.members()
                    }

                    override fun onContinue(index: Int) {
                        selectedMember.value = index
                        mySheeting.hide()
                    }

                    override fun selectedIndex(): Int {
                        return selectedMember.value
                    }

                }
            ),
            Sheets.CategoriesEdit to AllCategoriesBottomSheetModel(
                object : AllCategoriesBottomSheetModel.Callback{
                    override suspend fun getCategories(): List<Category> {
                        val categories = repo.getAllCategories().toMutableList()
                        categories[0] = categories[0].copy(isSelected = true)
                        return categories
                    }

                    override fun onCategoryAddContinue(name: String) {
                        mySheeting.hide()
                    }

                    override fun onCategorySelectedContinue(category: Category) {
                        mySheeting.sheets.value = Sheets.DatePicker
                    }

                    override fun scope(): CoroutineScope {
                        return viewModelScope
                    }
                }
            ),
            Sheets.DatePicker to ExpenseDatePickerBottomSheetModel(
                object : ExpenseDatePickerBottomSheetModel.Callback{
                    override fun scope(): CoroutineScope {
                        return viewModelScope
                    }

                    override fun getDatePickerData(): YoreDatePickerData {
                        return YoreDatePickerData()
                    }

                    override fun onContinue(
                        selectedDay: Int,
                        selectedMonth: Int,
                        selectedYear: Int
                    ) {
                        mySheeting.hide()
                    }

                    override fun close() {
                        mySheeting.hide()
                    }
                }
            ),
            Sheets.Settle to SettleBottomSheetModel(
                object : SettleBottomSheetModel.Callback{
                    override fun scope(): CoroutineScope {
                        return viewModelScope
                    }

                    override suspend fun getGetStat(): List<Transaction> {
                        return settleRepository.getWillGet()
                    }

                    override suspend fun getPayStat(): List<Transaction> {
                        return settleRepository.getWillPay()
                    }

                    override fun onGetContinue(transaction: Transaction) {
                        mySheeting.sheets.value = Sheets.SettlePaymentMethod
                    }

                    override fun onPayContinue(transaction: Transaction) {
                        mySheeting.sheets.value = Sheets.SettlePaymentMethod
                    }
                }
            ),
            Sheets.SettlePaymentMethod to SettlePaymentMethodBottomSheetModel(
                object : SettlePaymentMethodBottomSheetModel.Callback{
                    override fun scope(): CoroutineScope {
                        return viewModelScope
                    }

                    override suspend fun getUpis(): List<Upi> {
                        return listOf(
                            Upi("UPI-1", "fdfldf@ljl", "SBI", "User1", Color(0xff008523), false),
                            Upi("UPI-2", "fdfldf@ljl1", "Axis", "User2", Color(0xff186ec4), false),
                            Upi("UPI-2", "fdfldf@ljl1", "Axis", "User2", Color(0xff186ec4), false),
                            Upi("UPI-2", "fdfldf@ljl1", "Axis", "User2", Color(0xff186ec4), false),
                            Upi("UPI-2", "fdfldf@ljl1", "Axis", "User2", Color(0xff186ec4), false),
                            Upi("UPI-2", "fdfldf@ljl1", "Axis", "User2", Color(0xff186ec4), false),
                            Upi("UPI-2", "fdfldf@ljl1", "Axis", "User2", Color(0xff186ec4), false),
                            Upi("UPI-2", "fdfldf@ljl1", "Axis", "User2", Color(0xff186ec4), false),
                            Upi("UPI-2", "fdfldf@ljl1", "Axis", "User2", Color(0xff186ec4), false)
                        )
                    }

                    override suspend fun cashPaymentMobileNumber(): String {
                        return "8967114927"
                    }

                    override fun close() {
                        mySheeting.hide()
                    }

                    override fun onAddContinue(upiId: String) {
                        mySheeting.hide()
                    }

                    override fun onContinue(upiId: String) {
                        mySheeting.sheets.value = Sheets.PaymentReview
                    }
                }
            ),
            Sheets.PaymentReview to PaymentReviewBottomSheetModel(
                object : PaymentReviewBottomSheetModel.Callback{
                    override fun onContinue() {
                        mySheeting.hide()
                        navigation.scope { navHostController, lifecycleOwner, toaster ->
                            navHostController.navigate("payment_success")
                        }
                    }
                    override fun scope(): CoroutineScope {
                        return viewModelScope
                    }

                    override fun transaction(): TransactionReview {
                        return TransactionReview(
                            transactionType = TransactionType.Paid,
                            paymentMethod = "UPI",
                            amount = 10000f,
                            from = Friend(
                                name = "Rudra Dev",
                                mobileNumber = "7896230125",
                                accountNumber = "AC-123",
                                accountType = AccountType.Current,
                                imageUrl = "https://i.pravatar.cc/300",
                                bank = Bank(
                                    name = "SBI",
                                    imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/c/cc/SBI-logo.svg/500px-SBI-logo.svg.png?20200329171950"
                                ),
                                isSelected = true,
                                hasRead = false
                            ),
                            to = Friend(
                                name = "Deb Pan",
                                mobileNumber = "8954102365",
                                accountNumber = "AC-124",
                                accountType = AccountType.Current,
                                imageUrl = "https://i.pravatar.cc/300",
                                bank = Bank(
                                    name = "Axis",
                                    imageUrl = "https://is3-ssl.mzstatic.com/image/thumb/Purple112/v4/8f/77/e9/8f77e9ee-9cc9-a308-eeda-7b4cbbcdeda6/AppIcon-0-0-1x_U007emarketing-0-0-0-8-0-0-sRGB-0-0-0-GLES2_U002c0-512MB-85-220-0-0.png/146x0w.webp"
                                ),
                                isSelected = true,
                                hasRead = false
                            ),
                            category = Category1(
                                name = "Category",
                                color = 0xffff0000,
                                icon = R.drawable.travel
                            )
                        )
                    }

                    override fun onChangeReceiver() {
                        mySheeting.sheets.value = Sheets.SettlePaymentMethod
                    }

                    override fun onChangeAmount() {
                        mySheeting.sheets.value = Sheets.BillTotal
                    }
                }
            ),
            Sheets.BillTotal to BillTotalBottomSheetModel(
                object : BillTotalBottomSheetModel.Callback{
                    override suspend fun getBillTotalAmount(): String {
                        return ""
                    }

                    override suspend fun getDescription(): String {
                        return ""
                    }

                    override fun close() {
                        mySheeting.hide()
                    }

                    override fun onContinue(billTotal: String, billDescription: String) {
                        mySheeting.sheets.value = Sheets.PaymentReview
                    }

                    override fun scope(): CoroutineScope {
                        return viewModelScope
                    }

                }
            )
        ),
        onVisibilityChanged = {
            onSheetVisibilityChange(it)
        },
        confirmStateChange = {
            confirmSheetStateChange()
        }
    )

    fun onSheetVisibilityChange(visible: Boolean){
        if (!visible){
            mySheeting.sheets.value = Sheets.None
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    private fun confirmSheetStateChange(): Boolean {
        if (mySheeting.sheets.value == Sheets.MemberFilter){
            return false
        }
        return true
    }

    // ////////////////////////////////////////
    private val _conversations = mutableStateListOf<Conversation>()
    private val _statusBarColor = mutableStateOf<StatusBarColor?>(null)
    private val _groupName = mutableStateOf("Sushil Roy")
    private val _groupImage = mutableStateOf("https://i.pravatar.cc/300")
    private val mobile = mutableStateOf("89641203625")
    private val _groupCreationDate = mutableStateOf("May 9th 2022")
    private val _isSingleChat = mutableStateOf(false)
    private val _filteredMemberImage = mutableStateOf("https://i.pravatar.cc/300")
    private val _filteredMemberName = mutableStateOf("Manisha Roy")
    private val _chatMessage = mutableStateOf("")
    private val _searchText = mutableStateOf("")
    private val _typingMembers = mutableStateListOf<Any?>()
    private val searchText = mutableStateOf("")
    private val search = mutableStateOf(false)
    private val groupChatTab = mutableStateOf(GroupChatTab.All)

    // ///////////////////////////////////////
    // ////////////////////////////////////////
    private var prevSelectedIndex = -1

    @OptIn(ExperimentalMaterialApi::class)
    override val notifier = NotificationService { id, arg ->
        when (id) {
            DataIds.settleSummaryManage -> {
                mySheeting.sheets.value = Sheets.SettleSummaryManage
                mySheeting.show()
            }
            "${DataIds.back}group_chat_page" -> {
                when (mySheeting.sheets.value){
                    Sheets.None -> {}
                    else -> mySheeting.map[mySheeting.sheets.value]?.onBack()
                }

            }
            DataIds.searchTextInput -> {
                searchText.value = arg as? String ?: return@NotificationService
            }
            DataIds.groupChantTab -> {
                groupChatTab.value = arg as GroupChatTab ?: return@NotificationService
            }
            DataIds.search -> {
                search.value = true
                _statusBarColor.value = StatusBarColor(Color(0xffEDF3F9), true)
            }
            DataIds.filterDone -> {
                mySheeting.hide()
            }
            DataIds.split -> {
                mySheeting.sheets.value = Sheets.BillTotalAndCategories
                mySheeting.show()
            }

            WirelessViewModelInterface.startupNotification -> {
                _statusBarColor.value = StatusBarColor(
                    color = StatusBarGreen,
                    darkIcons = true
                )
            }
            DataIds.back -> {
                if (search.value){
                    search.value = false
                    _statusBarColor.value = StatusBarColor(
                        color = StatusBarGreen,
                        darkIcons = true
                    )
                    searchText.value = ""
                    return@NotificationService
                }
                navigation.scope { navHostController, lifecycleOwner, toaster ->
                    navHostController.popBackStack()
                }
            }
            DataIds.filter -> {
                mySheeting.sheets.value = Sheets.MemberFilter
                mySheeting.show()
            }
            DataIds.settleClick -> {
                mySheeting.sheets.value = Sheets.Settle
                mySheeting.show()
            }
            DataIds.summaryClick -> {
                navigation.scope { navHostController, lifecycleOwner, toaster ->
                    navHostController.navigate("individual_summary")
                }
            }
            DataIds.manageClick -> {
                navigation.scope { navHostController, lifecycleOwner, toaster ->
                    navHostController.navigate("individual_manage_page")
                }
                // _typingMembers.add("https://i.pravatar.cc/100")
            }
            DataIds.chatMessage -> {
                _chatMessage.value = (arg as? String) ?: ""
            }
            DataIds.searchText -> {
                _searchText.value = (arg as? String) ?: ""
            }
            DataIds.cardClick -> {
                navigation.scope { navHostController, lifecycleOwner, toaster ->
                    navHostController.navigate("split_card_details")
                }
            }
            // //////

            DataIds.openAllCategories -> {
                mySheeting.sheets.value = Sheets.CategoriesEdit
            }
            DataIds.billTotalContinueClick -> {
                mySheeting.sheets.value = Sheets.DatePicker
            }
        }
    }

    // ///////////////////////////////////////

    init {
        resolver.addAll(
            DataIds.conversations to _conversations,
            DataIds.statusBarColor to _statusBarColor,
            DataIds.groupName to _groupName,
            DataIds.groupImage to _groupImage,
            DataIds.mobile to mobile,
            DataIds.groupCreationDate to _groupCreationDate,
            DataIds.isSingleChat to _isSingleChat,
            DataIds.filteredMemberName to _filteredMemberName,
            DataIds.filteredMemberImage to _filteredMemberImage,
            DataIds.chatMessage to _chatMessage,
            DataIds.searchText to _searchText,
            DataIds.typingMembers to _typingMembers,
            DataIds.search to search,
            DataIds.groupChantTab to groupChatTab,
            // //////
            DataIds.statusBarColor to _statusBarColor,
            // /////////////
            DataIds.canProceedWithBillTotal to DataIds.canProceedWithBillTotal,
            DataIds.displayDate to DataIds.displayDate,
            DataIds.canProceedWithDate to DataIds.canProceedWithDate,
            DataIds.yoreDatePickerData to DataIds.yoreDatePickerData
        )
        // ////////////////////////////////////

        _statusBarColor.value = StatusBarColor(
            color = StatusBarGreen,
            darkIcons = true
        )
        // ////////////////////////////////////
        viewModelScope.launch(Dispatchers.IO) {
            val billTransactions = repo.getBillTransactions()
            withContext(Dispatchers.Main) {
                _conversations.add(
                    Conversation(
                        type = Conversation.Type.MEMBER,
                        data = MemberData(
                            name = "Manisha Roy",
                            profileImage = "https://i.pravatar.cc/100"
                        )
                    )
                )
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
                _conversations.add(
                    Conversation(
                        type = Conversation.Type.STATUS,
                        data = ChatStatus(
                            listOf(
                                "https://i.pravatar.cc/100",
                                "https://i.pravatar.cc/100",
                                "https://i.pravatar.cc/100",
                                "https://i.pravatar.cc/100"
                            ),
                            left = false
                        )
                    )
                )
                _conversations.add(
                    Conversation(
                        type = Conversation.Type.CHAT,
                        data = ChatData(
                            content = "Hello, guy"
                        )
                    )
                )
                _conversations.add(
                    Conversation(
                        type = Conversation.Type.CHAT,
                        data = ChatData(
                            content = "Hello, guys",
                            profileImage = "https://i.pravatar.cc/100"
                        )
                    )
                )
            }
        }

    }
}
