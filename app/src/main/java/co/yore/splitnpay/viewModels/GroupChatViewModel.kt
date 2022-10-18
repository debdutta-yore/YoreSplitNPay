package co.yore.splitnpay.viewModels

import androidx.annotation.DrawableRes
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.yore.splitnpay.R
import co.yore.splitnpay.components.components.YoreDatePickerData
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.models.*
import co.yore.splitnpay.pages.GroupChatTab
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
    suspend fun getCategories(): List<Category>
    suspend fun getAllCategories(): List<Category>
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
    //////////////
    var categoryList  =
        listOf(
            Category(
                name = "Food",
                0xFF1A79E5,
                R.drawable.ic_food,
                isSelected = true
            ),
            Category(
                name = "Trip",
                0xFFFF4077,
                R.drawable.ic_trip
            ),
            Category(
                name = "Rent",
                0xFFF6CC00,
                R.drawable.ic_rent
            ),
            Category(
                name = "Party",
                0xFF37D8CF,
                R.drawable.ic_party
            ),
            Category(
                name = "Medical",
                0xFF37D8CF,
                R.drawable.ic_medical
            ),
            Category(
                name = "Emi",
                0xFFF6CC00,
                R.drawable.ic_emi
            ),
            Category(
                name = "Bills",
                0xFFFF4077,
                R.drawable.ic_bills
            ),
        )

    override suspend fun getCategories(): List<Category> {
        return categoryList.take(4)
    }

    override suspend fun getAllCategories(): List<Category> {
        return categoryList
    }
}

data class GroupCreationEvent(
    val creator: String,
    val groupName: String
)

data class ChatStatus(
    val memberImages: List<Any?>,
    val left: Boolean = true
)

data class Conversation(
    val type: Type,
    val data: Any
) {
    enum class Type {
        TRANSACTION,
        DATE,
        CREATION,
        STATUS,
        CHAT,
        MEMBER,
    }
}

data class ChatData(
    val content: Any,
    val profileImage: Any? = null
)

data class MemberData(
    val name: String,
    val profileImage: Any?
)

data class SingleItem(
    val id: Int,
    @DrawableRes
    val profilePic: Int,
    val userName: String,
    val mobileNo: String,
    val isSelected: Boolean
)

class GroupChatViewModel(
    private val repo: GroupRepository = GroupsMock()
) : ViewModel(), WirelessViewModelInterface {
    val list = listOf(
        SingleItem(
            id = 1,
            profilePic = R.drawable.ic_profilepic1,
            userName = "You",
            mobileNo = "9563376942",
            isSelected = false
        ),
        SingleItem(
            id = 2,
            profilePic = R.drawable.ic_profilepic1,
            userName = "Manisha Roy",
            mobileNo = "9563376942",
            isSelected = false
        ),
        SingleItem(
            id = 3,
            profilePic = R.drawable.ic_profilepic1,
            userName = "Sushil Roy",
            mobileNo = "9563376942",
            isSelected = false
        ),
        SingleItem(
            id = 4,
            profilePic = R.drawable.ic_profilepic1,
            userName = "Sanjana Ray",
            mobileNo = "9563376942",
            isSelected = false
        ),
        SingleItem(
            id = 5,
            profilePic = R.drawable.ic_profilepic1,
            userName = "Ankita Ray",
            mobileNo = "9563376942",
            isSelected = false
        ),
    )
    override val resolver = Resolver()
    override val navigation = Navigation()
    override val permissionHandler = PermissionHandler()
    override val resultingActivityHandler = ResultingActivityHandler()

    @OptIn(ExperimentalMaterialApi::class)
    override val sheetHandler = SheetHandler(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
        confirmStateChange = {
            confirmSheetStateChange()
        },
        onVisibilityChange = {
            onSheetVisibilityChange(it)
        }
    )

    fun onSheetVisibilityChange(visible: Boolean){
        if(!visible){
            _sheets.value = Sheets.None
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    private fun confirmSheetStateChange(): Boolean {
        if(_sheets.value==Sheets.MemberFilter){
            return false
        }
        return true
    }

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
    private val _list = mutableStateListOf<SingleItem>()
    private val _typingMembers = mutableStateListOf<Any?>()
    private val searchText = mutableStateOf("")
    private val search = mutableStateOf(false)
    private val groupChatTab = mutableStateOf(GroupChatTab.All)
    private val _sheets = mutableStateOf(Sheets.None)
    private val canProceedWithBillTotal = mutableStateOf(false)
    private val capProceedWithCategory = mutableStateOf(false)
    private val displayDate = mutableStateOf("")
    private val canProceedWithDate = mutableStateOf(false)
    private val yoreDatePickerData = mutableStateOf(YoreDatePickerData())

    /////////////////////////////////////////
    private val _categories = mutableStateListOf<Category>()
    //private val _statusBarColor = mutableStateOf<StatusBarColor?>(null)
    private val _billTotalAmount = mutableStateOf("")
    private val _billTotalDescription = mutableStateOf("")
    private val _renamePressed = mutableStateOf(-1)

    //////////////////////////////////////////
    private val _allCategories = mutableStateListOf<Category>()
    private val _isAddCategoryEnabled = mutableStateOf(false)
    private val _addCategoryName = mutableStateOf("")
    //////////////////////////////////////////
    private var prevSelectedIndex = -1
    @OptIn(ExperimentalMaterialApi::class)
    override val notifier = NotificationService { id, arg ->
        when (id) {
            DataIds.year->{
                yoreDatePickerData.value = yoreDatePickerData.value.copy(selectedYear = arg as? Int?:return@NotificationService)
                onDateChange()
            }
            DataIds.month->{
                yoreDatePickerData.value = yoreDatePickerData.value.copy(selectedMonth = arg as? Int?:return@NotificationService)
                onDateChange()
            }
            DataIds.day->{
                yoreDatePickerData.value = yoreDatePickerData.value.copy(selectedDay = arg as? Int?:return@NotificationService)
                onDateChange()
            }
            DataIds.settleSummaryManage->{
                _sheets.value = Sheets.SettleSummaryManage
                sheetHandler.state {
                    show()
                }
            }
            "${DataIds.back}group_chat_page"->{
                sheetHandler.state {
                    this.isVisible
                    hide()
                }
            }
            DataIds.searchTextInput->{
                searchText.value = arg as? String?:return@NotificationService
            }
            DataIds.groupChantTab->{
                groupChatTab.value = arg as GroupChatTab?:return@NotificationService
            }
            DataIds.search->{
                search.value = true
                _statusBarColor.value = StatusBarColor(Color(0xffEDF3F9),true)
            }
            DataIds.filterDone->{
                sheetHandler.state {
                    hide()
                }
            }
            DataIds.split->{
                _sheets.value = Sheets.BillTotalAndCategories
                sheetHandler.state {
                    show()
                }
            }
            DataIds.filterMember->{
                if(prevSelectedIndex>-1){
                    _list[prevSelectedIndex] = _list[prevSelectedIndex].copy(isSelected = false)
                }
                if(arg is SingleItem){
                    val index = _list.indexOf(arg)
                    if(index > -1){
                        val current = _list[index]
                        prevSelectedIndex = index
                        _list[index] = current.copy(isSelected = !current.isSelected)
                    }
                }
            }
            WirelessViewModelInterface.startupNotification -> {
                _statusBarColor.value = StatusBarColor(
                    color = StatusBarGreen,
                    darkIcons = true
                )
            }
            DataIds.back -> {
                if(search.value){
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
            DataIds.groupAmount -> {
                _groupAmount.value = (arg as? Float) ?: 0f
            }
            DataIds.filter -> {
                _sheets.value = Sheets.MemberFilter
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
                navigation.scope { navHostController, lifecycleOwner, toaster ->
                    navHostController.navigate("group_manage")
                }
                //_typingMembers.add("https://i.pravatar.cc/100")
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
            ////////
            DataIds.categorySelectionClick -> {
                viewModelScope.launch(Dispatchers.IO) {
                    withContext(Dispatchers.Main) {
                        for(i in 0 until _categories.size){
                            _categories[i] =
                                _categories[i].copy(isSelected = false)
                        }
                        val index = arg as Int
                        _categories[index] =
                            _categories[index].copy(isSelected = !_categories[index].isSelected)
                    }
                }
            }
            DataIds.categoryEditSelectionClick -> {
                viewModelScope.launch(Dispatchers.IO) {
                    withContext(Dispatchers.Main) {
                        for(i in 0 until _allCategories.size){
                            _allCategories[i] =
                                _allCategories[i].copy(isSelected = false)
                        }
                        val index = arg as Int
                        _allCategories[index] =
                            _allCategories[index].copy(isSelected = !_allCategories[index].isSelected)
                    }
                }
            }
            DataIds.billTotalAmount -> {
                _billTotalAmount.value = (arg as? String) ?: ""
                canProceedWithBillTotal.value = try {
                    _billTotalAmount.value.toInt()>0
                } catch (_: Exception) {
                    false
                }
            }
            DataIds.billTotalDescription -> {
                _billTotalDescription.value = (arg as? String) ?: ""
            }
            DataIds.renamePressed -> {
                _renamePressed.value = (arg as? Int) ?: -1
            }
            DataIds.openAllCategories -> {
                _sheets.value = Sheets.CategoriesEdit
            }
            DataIds.billTotalContinueClick -> {
                _sheets.value = Sheets.DatePicker
            }
            DataIds.isAddCategoryEnabled -> {
                _isAddCategoryEnabled.value = !_isAddCategoryEnabled.value
            }
            DataIds.addCategoryName -> {
                _addCategoryName.value = (arg as? String) ?: ""
            }
        }
    }

    private fun onDateChange() {
        val y = yoreDatePickerData.value.selectedYear
        val m = yoreDatePickerData.value.selectedMonth
        val d = yoreDatePickerData.value.selectedDay
        if(d != null && m != null){
            displayDate.value = displayableDate(d,m,y)
            canProceedWithDate.value = true
        }
        else{
            displayDate.value = ""
            canProceedWithDate.value = false
        }
    }

    private fun displayableDate(d: Int, m: Int, y: Int): String {
        val ordinal = when(d%10){
            1-> "st"
            2-> "nd"
            3-> "rd"
            else-> "th"
        }
        val month = when(m){
            1-> "Jan"
            2-> "Feb"
            3-> "Mar"
            4-> "Apr"
            5-> "May"
            6-> "Jun"
            7-> "Jul"
            8-> "Aug"
            9-> "Sep"
            10-> "Oct"
            11-> "Nov"
            12-> "Dec"
            else-> ""
        }
        return "$d$ordinal $month, $y"
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
            DataIds.membersForFiltering to _list,
            DataIds.typingMembers to _typingMembers,
            DataIds.search to search,
            DataIds.groupChantTab to groupChatTab,
            ////////
            DataIds.categories to _categories,
            DataIds.statusBarColor to _statusBarColor,
            DataIds.billTotalAmount to _billTotalAmount,
            DataIds.billTotalDescription to _billTotalDescription,
            DataIds.renamePressed to _renamePressed,
            ////////////
            DataIds.allCategories to _allCategories,
            DataIds.isAddCategoryEnabled to _isAddCategoryEnabled,
            DataIds.addCategoryName to _addCategoryName,
            ///////////////
            DataIds.sheets to _sheets,
            DataIds.canProceedWithBillTotal to canProceedWithBillTotal,
            DataIds.capProceedWithCategory to capProceedWithCategory,
            DataIds.displayDate to displayDate,
            DataIds.canProceedWithDate to canProceedWithDate,
            DataIds.yoreDatePickerData to yoreDatePickerData,
        )
        //////////////////////////////////////
        viewModelScope.launch(Dispatchers.IO) {
            val categoriesList = repo.getCategories()
            withContext(Dispatchers.Main) {
                _categories.addAll(
                    categoriesList
                )
            }
        }

        _statusBarColor.value = StatusBarColor(
            color = StatusBarGreen,
            darkIcons = true
        )
        ///////////////////////////////////////
        viewModelScope.launch(Dispatchers.IO) {
            val members = repo.getAllCategories()
            withContext(Dispatchers.Main) {
                _allCategories.addAll(
                    members
                )
            }
        }
        //////////////////////////////////////
        _list.addAll(list)
        viewModelScope.launch(Dispatchers.IO) {
            val billTransactions = repo.getBillTransactions()
            withContext(Dispatchers.Main) {
                _conversations.add(
                    Conversation(
                        type = Conversation.Type.MEMBER,
                        data = MemberData(
                            name = "Manisha Roy",
                            profileImage = "https://i.pravatar.cc/100",
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
                                "https://i.pravatar.cc/100",
                            ),
                            left = false
                        )
                    )
                )
                _conversations.add(
                    Conversation(
                        type = Conversation.Type.CHAT,
                        data = ChatData(
                            content = "Hello, guy",
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

val StatusBarGreen = Color(0xff00CEC3)

