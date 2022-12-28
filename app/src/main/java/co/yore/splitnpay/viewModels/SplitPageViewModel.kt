package co.yore.splitnpay.viewModels

import android.Manifest
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.yore.splitnpay.GrpcServer
import co.yore.splitnpay.app.Routes
import co.yore.splitnpay.components.components.YoreDatePickerData
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.libs.jerokit.*
import co.yore.splitnpay.libs.jerokit.bottom_sheet.Sheeting
import co.yore.splitnpay.libs.jerokit.bottom_sheet.Sheets
import co.yore.splitnpay.models.*
import co.yore.splitnpay.pages.subpages.AllCategoriesBottomSheetModel
import co.yore.splitnpay.pages.subpages.BillTotalAndCategoryBottomSheetModel
import co.yore.splitnpay.pages.subpages.ExpenseDatePickerBottomSheetModel
import co.yore.splitnpay.pages.subpages.SuccessUndoSheetModel
import co.yore.splitnpay.repo.MasterRepo
import co.yore.splitnpay.repo.MasterRepoImpl
import co.yore.splitnpay.ui.theme.BermudaGray
import co.yore.splitnpay.ui.theme.CeriseRed
import co.yore.splitnpay.ui.theme.RobinsEggBlue
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.*
import splitpay.CategoryServiceGrpc
import splitpay.CategoryServiceGrpc.CategoryServiceBlockingStub
import splitpay.Splitpay
import splitpay.Splitpay.ListCategoryRequest
import splitpay.Splitpay.ListCategoryResponse
import kotlin.math.abs

@OptIn(ExperimentalPermissionsApi::class)
class SplitPageViewModel(
    private val repo: MasterRepo = MasterRepoImpl()
) : ViewModel(), WirelessViewModelInterface {
    ///////////////////////////////
    override val softInputMode = mutableStateOf(SoftInputMode.adjustNothing)
    override val resultingActivityHandler = ResultingActivityHandler()
    override val permissionHandler = PermissionHandler()
    override val navigation = mutableStateOf<UIScope?>(null)

    private val _whole = mutableStateOf("0")
    private val _decimal = mutableStateOf("00")
    private val _wholeGet = mutableStateOf("0")
    private val _decimalGet = mutableStateOf("00")
    private val _wholePay = mutableStateOf("0")
    private val _decimalPay = mutableStateOf("00")

    private val _statusBarColor = mutableStateOf<StatusBarColor?>(null)
    private val _ultimateState = mutableStateOf(SplitPageState.PAY)

    private val _input = mutableStateOf("")
    private val _subTabGroup = mutableStateOf(SplitPageTabs.All)
    private val _subTabPeople = mutableStateOf(SplitPageTabs.All)
    private val _groupsAndPeoplesVault = mutableListOf<GroupOrContact>()
    private val _groupsAndPeoples = mutableStateListOf<GroupOrContact>()
    private val _selectedTabIndex = mutableStateOf(0)
    private val _noGroup = mutableStateOf(true)
    private val _haveSplit = mutableStateOf(true)

    private var splitPageData: SplitPageData = SplitPageData(
        willPay = 0f,
        willGet = 0f,
        splitted = false
    )

    // /////////////////////////////////////////////////////
    private val _notificationService =
        NotificationService { id, arg ->
            when (id) {
                WirelessViewModelInterface.startupNotification -> {
                    updateSplitPageColor(splitPageData)
                    navigation.scope { navHostController, lifecycleOwner, toaster ->
                        val added = navHostController.get<Boolean>("splitAdded")
                        if (added == true) {
                            mySheeting.change(Sheets.SuccessUndo)
                            mySheeting.show()
                            navHostController.currentBackStackEntry?.savedStateHandle?.remove<Boolean>("splitAdded")
                        }
                        if (navHostController.currentBackStackEntry?.arguments?.getBoolean("blank") != true){
                            setupPage()
                        }
                    }
                }
                DataIds.textInput -> {
                    _input.value = (arg as? String) ?: return@NotificationService
                    filter()
                }
                DataIds.groupCard -> {
                    // Todo
                }
                DataIds.peopleCard -> {
                    // Todo
                }
                DataIds.split -> {
                    mySheeting.change(Sheets.BillTotalAndCategories)
                    mySheeting.show()
                }
                DataIds.groupCardGo -> {
                    gotoGroupPage()
                }
                DataIds.addGroup -> {
                    gotoSplitWithPage()
                }
                DataIds.peopleCardGo -> {
                    gotoIndividualPage()
                }
                "${DataIds.back}${Routes.splitPage.full}" -> {
                    when (mySheeting.sheets.value) {
                        Sheets.None -> pageBack()
                        else -> mySheeting.onBack()
                    }
                }
                DataIds.back -> {
                    pageBack()
                }
                DataIds.getCard -> {
                    // Todo
                }
                DataIds.payCard -> {
                    // Todo
                }
                DataIds.selectedTabIndex -> {
                    val index = (arg as? Int) ?: return@NotificationService
                    if (_selectedTabIndex.value == index) {
                        return@NotificationService
                    }
                    _selectedTabIndex.value = index
                    filter()
                }
                "${DataIds.subTab}group" -> {
                    _subTabGroup.value = (arg as? SplitPageTabs) ?: return@NotificationService
                    filter()
                }
                "${DataIds.subTab}people" -> {
                    _subTabPeople.value = (arg as? SplitPageTabs) ?: return@NotificationService
                    filter()
                }
            }
        }

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

                    override fun close() {
                        mySheeting.hide()
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
                        selectMembersForSplit()
                    }

                    override fun close() {
                        mySheeting.hide()
                    }
                }
            ),
            Sheets.SuccessUndo to SuccessUndoSheetModel(
                object : SuccessUndoSheetModel.Callback{
                    override fun scope(): CoroutineScope {
                        return viewModelScope
                    }

                    override fun undo() {
                        mySheeting.hide()
                    }

                    override fun timerEnded() {
                        mySheeting.hide()
                    }

                    override fun close() {
                        mySheeting.hide()
                    }

                    override fun message(): String {
                        return "Split as non-group successfully"
                    }

                    override fun timeMillis(): Int {
                        return 60 * 1000
                    }
                }
            )
        ),
        onVisibilityChanged = {
            if (!it){
                mySheeting.change(Sheets.None)
            }
        }
    )

    private fun selectMembersForSplit() {
        navigation.scope { navHostController, lifecycleOwner, toaster ->
            navHostController.navigate("${Routes.splitWithPage.name}?split=true")
        }
    }

    private fun gotoIndividualPage() {
        navigation.scope { navHostController, lifecycleOwner, toaster ->
            navHostController.navigate(Routes.individualChat.name)
        }
    }

    private fun gotoGroupPage() {
        navigation.scope { navHostController, lifecycleOwner, toaster ->
            navHostController.navigate(Routes.groupChatPage.name)
        }
    }

    private fun pageBack() {
        navigation.scope { navHostController, lifecycleOwner, toaster ->
            navHostController.popBackStack()
        }
    }

    private fun gotoSplitWithPage() {
        navigation.scope{ navHostController, lifecycleOwner, toaster ->
            navHostController.navigate(Routes.splitWithPage.name)
        }
    }

    override val notifier = _notificationService
    private val _resolver = Resolver()
    override val resolver = _resolver
    init {
        setupResolver()
        // setupPage()
    }

    private fun setupPage() {
        tryFetchGroupAndContacts()
        tryUpdateSplitPage()
    }

    private fun setupResolver() {
        resolver.addAll(
            DataIds.whole to _whole,
            DataIds.decimal to _decimal,
            DataIds.wholeGet to _wholeGet,
            DataIds.decimalGet to _decimalGet,
            DataIds.wholePay to _wholePay,
            DataIds.decimalPay to _decimalPay,
            DataIds.statusBarColor to _statusBarColor,
            DataIds.textInput to _input,
            DataIds.ultimateState to _ultimateState,
            DataIds.groupsAndPeoples to _groupsAndPeoples,
            DataIds.selectedTabIndex to _selectedTabIndex,
            DataIds.noGroup to _noGroup,
            DataIds.haveSplit to _haveSplit,
            "${DataIds.subTab}group" to _subTabGroup,
            "${DataIds.subTab}people" to _subTabPeople
        )
    }

    private fun tryUpdateSplitPage() {
        updateAsSplitPageData(
            SplitPageData(
                willPay = 0f,
                willGet = 0f,
                splitted = false
            )
        )
        viewModelScope.launch(Dispatchers.IO) {
            splitPageData = repo.splitPageData()
            withContext(Dispatchers.Main) {
                updateAsSplitPageData(splitPageData)
            }
        }
    }

    private fun tryFetchGroupAndContacts() {
        viewModelScope.launch(Dispatchers.Main) {
            val p = Manifest.permission.READ_CONTACTS
            val checked = permissionHandler.check(p)
            if (checked?.allPermissionsGranted == true) {
                fetchGroupAndContacts()
            } else {
                val requested = permissionHandler.request(p)
                if (requested?.get(p) == true) {
                    fetchGroupAndContacts()
                }
            }
        }
    }

    private fun fetchGroupAndContacts() {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main){
                _noGroup.value = true
            }
            val items = repo.groupAndContacts()
            withContext(Dispatchers.Main){
                _groupsAndPeoplesVault.addAll(items)
                _noGroup.value = false
                filter()
            }
        }
    }

    private fun updateAsSplitPageData(data: SplitPageData) {
        val value = data.willGet - data.willPay
        val total = abs(value)
        val getSplitted = data.willGet.splitted()
        val paySplitted = data.willPay.splitted()
        val totalSplitted = total.splitted()
        _whole.value = totalSplitted.wholeString
        _decimal.value = totalSplitted.decString

        _wholeGet.value = getSplitted.wholeString
        _decimalGet.value = getSplitted.decString

        _wholePay.value = paySplitted.wholeString
        _decimalPay.value = paySplitted.decString

        updateSplitPageColor(data)
    }

    private fun updateSplitPageColor(data: SplitPageData) {
        val value = data.willGet - data.willPay
        if (data.splitted){
            _ultimateState.value = if (value > 0f) {
                _statusBarColor.value = StatusBarColor(
                    color = RobinsEggBlue,
                    darkIcons = false
                )
                SplitPageState.GET
            } else if (value < 0f) {
                _statusBarColor.value = StatusBarColor(
                    color = CeriseRed,
                    darkIcons = false
                )
                SplitPageState.PAY
            } else {
                _statusBarColor.value = StatusBarColor(
                    color = BermudaGray,
                    darkIcons = false
                )
                SplitPageState.NONE
            }
            _haveSplit.value = true
        } else {
            _statusBarColor.value = StatusBarColor(
                color = BermudaGray,
                darkIcons = false
            )
            _ultimateState.value = SplitPageState.NONE
            _haveSplit.value = false
        }
    }

    fun filter(){
        viewModelScope.launch(Dispatchers.IO) {
            _groupsAndPeoples.clear()
            val filtered = _groupsAndPeoplesVault.filter {
                itemFilter(it)
            }
            withContext(Dispatchers.Main){
                _groupsAndPeoples.addAll(
                    filtered
                )
            }
        }
    }

    private fun itemFilter(it: GroupOrContact): Boolean {
        val tab = _selectedTabIndex.value
        if ((tab == 0 && it !is GroupData) || (tab == 1 && it !is ContactData)){
            return false
        }
        val owe = if (tab == 0) _subTabGroup.value else _subTabPeople.value
        if (
            (owe == SplitPageTabs.YouOwe && (it.willGet() > 0f || it.willPay() == 0f)) ||
            (owe == SplitPageTabs.YouAreOwed && (it.willPay() > 0f || it.willGet() == 0f))
        ){
            return false
        }
        return search(_input.value, it.searchables())
    }
}
