package co.yore.splitnpay.viewModels

import android.Manifest
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.yore.splitnpay.DataBank
import co.yore.splitnpay.app.Routes
import co.yore.splitnpay.components.components.Kal
import co.yore.splitnpay.components.components.YoreDatePickerData
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.libs.jerokit.*
import co.yore.splitnpay.libs.jerokit.bottom_sheet.Sheeting
import co.yore.splitnpay.libs.jerokit.bottom_sheet.Sheets
import co.yore.splitnpay.models.*
import co.yore.splitnpay.pages.subpages.*
import co.yore.splitnpay.repo.MasterRepo
import co.yore.splitnpay.ui.theme.RobinsEggBlue
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SplitReviewViewModel @Inject constructor(
    private val repo: MasterRepo,
    private val media: Media
) : ViewModel(), WirelessViewModelInterface {
    override val softInputMode = mutableStateOf(SoftInputMode.adjustPan)

    @OptIn(ExperimentalMaterialApi::class)

    override val resolver = Resolver()
    override val navigation = Navigation()
    override val permissionHandler = PermissionHandler()
    override val resultingActivityHandler = ResultingActivityHandler()

    @OptIn(ExperimentalMaterialApi::class)
    override val sheeting = Sheeting(
        sheetMap = mapOf(
            Sheets.CalculationMethod to CalculationMethodSelectionBottomSheetModel(
                object : CalculationMethodSelectionBottomSheetModel.Callback{
                    override fun scope(): CoroutineScope {
                        return viewModelScope
                    }

                    override fun onContinue(arg: Any?) {
                        mySheeting.hide()
                        onCalculationMethodSelected(arg as? String?:return)
                    }

                    override fun close() {
                        mySheeting.hide()
                    }

                }
            ),
            Sheets.ImagePicker to PhotoSelectionBottomSheetModel(
                object : PhotoSelectionBottomSheetModel.Callback{
                    override fun scope(): CoroutineScope {
                        return viewModelScope
                    }

                    override fun onContinue(arg: Any?) {
                        mySheeting.hide()
                        handleCameraOrGallery(arg as? String ?: return)
                    }

                    override fun close() {
                        mySheeting.hide()
                    }
                }
            ),
            Sheets.BillTotalAndCategories to BillTotalAndCategoryBottomSheetModel(
                object : BillTotalAndCategoryBottomSheetModel.BillTotalBottomSheetModelCallback{
                    override suspend fun getCategories(): List<Category> {
                        val categories = repo.getAllCategories().toMutableList()
                        val index = categories.indexOfFirst { it.id == category.value.id }
                        if (index != -1){
                            categories[index] = categories[index].copy(isSelected = true)
                        }
                        return categories
                    }

                    override suspend fun getBillTotalAmount(): String {
                        return _billTotal.value.toInt().toString()
                    }

                    override suspend fun getDescription(): String {
                        return _description.value
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
                        mySheeting.hide()
                        _billTotal.value = billTotal.toDouble()
                        onBillTotalChanged()
                        this@SplitReviewViewModel.category.value = category
                        _description.value = billDescription
                    }

                    override fun onRenameContinue(
                        category: Category,
                        name: String
                    ) {

                    }

                    override fun scope(): CoroutineScope {
                        return viewModelScope
                    }

                    override fun initialized() {
                        TODO("Not yet implemented")
                    }
                }
            ),
            Sheets.CategoriesEdit to AllCategoriesBottomSheetModel(
                object : AllCategoriesBottomSheetModel.Callback{
                    override suspend fun getCategories(): List<Category> {
                        val categories = repo.getAllCategories().toMutableList()
                        val index = categories.indexOfFirst { it.id == category.value.id }
                        if (index != -1){
                            categories[index] = categories[index].copy(isSelected = true)
                        }
                        return categories
                    }

                    override fun onCategoryAddContinue(name: String) {
                        mySheeting.hide()
                    }

                    override fun onCategorySelectedContinue(category: Category) {
                        mySheeting.hide()
                        this@SplitReviewViewModel.category.value = category
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
                        return YoreDatePickerData(
                            selectedDay = date.value.day,
                            selectedMonth = date.value.month,
                            selectedYear = date.value.year
                        )
                    }

                    override fun onContinue(
                        selectedDay: Int,
                        selectedMonth: Int,
                        selectedYear: Int
                    ) {
                        mySheeting.hide()
                        date.value = Kal.Date(selectedDay, selectedMonth, selectedYear)
                        _dateText.value = displayableDate(
                            date.value.day,
                            date.value.month,
                            date.value.year
                        )
                    }

                    override fun close() {
                        mySheeting.hide()
                    }
                }
            )
        ),
        onVisibilityChanged = {
            if (!it){
                softInputMode.value = SoftInputMode.adjustPan
                mySheeting.sheets.value = Sheets.None
            } else {
                softInputMode.value = SoftInputMode.adjustNothing
            }
        },
        confirmStateChange = {
            true
        }
    )

    private fun onCalculationMethodSelected(s: String) {
        if(s == "Proportionate"){
            viewModelScope.launch {
                val response = repo.createExpense(
                    categoryId = category.value.uid,
                    shareType = when(adjustMethod.value){
                        0-> "equal"
                        else -> "unequal"
                    },
                    amount = _billTotal.value,
                    description = _description.value,
                    receiptUrl = getImage(receipt),
                    calculationMethod = "proportionate",
                    members = _members.toList(),
                    groupId = "",
                    groupName = DataBank.once[DataBank.Key.GroupName] as? String?:"",
                    groupImage = getImage(DataBank.once[DataBank.Key.GroupImage])
                )
                val eid = response.first
                val gid = response.second
                leaveScreen(gid)
            }
        }
    }

    private fun leaveScreen(gid: String) {
        navigation.scope { navHostController, lifecycleOwner, toaster ->
            if (asGroup) {
                if (navHostController.backQueue.firstOrNull{ it.destination.route == Routes.groupChatPage.full} != null) {
                    navHostController.popBackStack("${Routes.groupChatPage.name}/$gid", false)
                } else {
                    navHostController.popBackStack(Routes.splitPage.full, false)
                    navHostController.navigate("${Routes.groupChatPage.name}/$gid")
                }
            } else {
                navHostController.set(Routes.splitPage.full, "splitAdded", true)
                navHostController.popBackStack(Routes.splitPage.full, false)
            }
        }
    }

    private fun getImage(image: Any?): Bitmap? {
        if(image is Uri){
            return media.decodeBitmapFromUri(image)
        }
        if(image is Bitmap){
            return image
        }
        if(image is String && image.startsWith("content://")){
            return media.decodeBitmapFromUri(Uri.parse(image))
        }
        return null
    }

    private fun onBillTotalChanged() {
        updateAsSelectedListOption()
        updateAsMemberSelection()
    }

    // ////////////

    // ////////////////////////////////////////
    private val _members = mutableStateListOf<MemberPayment>()
    private val _statusBarColor = mutableStateOf<StatusBarColor?>(null)

    private val _billTotal = mutableStateOf(0.0)
    private val _description = mutableStateOf("")
    private val _dateText = mutableStateOf("")

    // ///////////
    private val paidRemaining = mutableStateOf(0.0)
    private val adjustRemaining = mutableStateOf(0.0)
    private val paidByOrAdjustSplitMode = mutableStateOf(0)
    private val adjustMethod = mutableStateOf(0)
    private val receipt = mutableStateOf<Any?>(null)
    private val category = mutableStateOf(Category.blank)
    private val date = mutableStateOf(Kal.Date(7, 6, 2022))
    private var asGroup = false
    // /////////////////
    // ///////////////////////////////////////

    // ////////////////////////////////////////
    @OptIn(ExperimentalMaterialApi::class)
    override val notifier =
        NotificationService { id, arg ->
            when (id) {
                WirelessViewModelInterface.startupNotification -> {
                    navigation.scope { navHostController, lifecycleOwner, toaster ->
                        asGroup = getBoolean("asGroup", false)
                    }
                }
                DataIds.deleteReceipt -> {
                    receipt.value = null
                }
                "${DataIds.back}split_review_page" -> {
                    when (mySheeting.sheets.value) {
                        Sheets.BillTotalAndCategories -> mySheeting.model(Sheets.BillTotalAndCategories)
                            ?.onBack()
                        Sheets.DatePicker -> mySheeting.model(Sheets.DatePicker)?.onBack()
                        else -> pageBack()
                    }
                }
                DataIds.categoryEditClick -> {
                    openBillTotalBottomSheet()
                }
                DataIds.selectedTabIndex -> {
                    paidByOrAdjustSplitMode.value = arg as? Int ?: return@NotificationService
                }
                DataIds.selectedListOption -> {
                    adjustMethod.value = arg as? Int ?: return@NotificationService
                    updateAsSelectedListOption()
                }
                DataIds.memberPaymentCheck -> {
                    val index = _members.indexOf(arg as? MemberPayment)
                    if (index < 0) {
                        return@NotificationService
                    }
                    val member = _members[index]
                    val selected = !member.selected
                    _members[index] = member.copy(selected = selected)
                    // /
                    updateAsMemberSelection()
                }
                DataIds.paidByAmount -> {
                    if (arg !is Store) {
                        return@NotificationService
                    }
                    val member = arg["member"] as? MemberPayment ?: return@NotificationService
                    val amount = arg["amount"] as? Double ?: return@NotificationService
                    val index = _members.indexOf(member)
                    if (index < 0) {
                        return@NotificationService
                    }
                    _members[index] = _members[index].copy(paid = amount)
                    val paid = _members.sumOf { it.paid }.toFloat()
                    paidRemaining.value = _billTotal.value - paid
                }
                DataIds.adjustByAmount -> {
                    if (arg !is Store) {
                        return@NotificationService
                    }
                    val member = arg["member"] as? MemberPayment ?: return@NotificationService
                    val amount = arg["amount"] as? Double ?: return@NotificationService
                    val index = _members.indexOf(member)
                    if (index < 0) {
                        return@NotificationService
                    }
                    _members[index] = _members[index].copy(toPay = amount)
                    val paid = _members.sumOf { it.toPay }.toFloat()
                    adjustRemaining.value = _billTotal.value - paid
                }
                DataIds.back -> {
                    navigation.scope { navHostController, lifecycleOwner, toaster ->
                        navHostController.popBackStack()
                    }
                }
                DataIds.selectPaidByMemberClick -> {
                    val selected = paidByOrAdjustSplitMode.value
                    if(selected == 0){
                        val item = arg as? MemberPayment?:return@NotificationService
                        val index = _members.indexOfFirst { it.id == item.id }
                        if(index > -1){
                            _members[index] = item.copy(selected = item.selected.not())
                        }
                    }
                }
                DataIds.editBillAmountClick -> {
                    openBillTotalBottomSheet()
                }
                DataIds.categoryClick -> {

                }
                DataIds.dateClick -> {
                    mySheeting.sheets.value = Sheets.DatePicker
                    mySheeting.show()
                }
                DataIds.receiptClick -> {
                    showCameraOrGallery()
                }
                DataIds.confirmSplitClick -> {
                    mySheeting.sheets.value = Sheets.CalculationMethod
                    mySheeting.show()
                }
                DataIds.scanClick -> {

                }
                DataIds.subCategoryText -> {
                    _description.value = (arg as? String) ?: ""
                }
            }
        }

    private fun updateAsMemberSelection() {
        val selectedCount = _members.count {
            it.selected
        }
        val contribute = (
            if (selectedCount == 0){
                0f
            } else {
                (_billTotal.value / selectedCount)
            }
            ).toDouble()
        val count = _members.size
        for (i in 0 until count){
            val _member = _members[i]
            val _selected = _member.selected
            if (_selected){
                _members[i] = _member.copy(paid = contribute)
            } else {
                _members[i] = _member.copy(paid = 0.0)
            }
        }
        paidRemaining.value = if (contribute == 0.0) _billTotal.value else 0.0
    }

    private fun updateAsSelectedListOption() {
        if (adjustMethod.value == 0){
            val memberCount = _members.size
            val contribute = _billTotal.value / memberCount
            for (i in 0 until memberCount){
                _members[i] = _members[i].copy(toPay = contribute)
            }
            adjustRemaining.value = 0.0
        }
    }

    private fun pageBack() {

    }

    @OptIn(ExperimentalMaterialApi::class)
    private fun openBillTotalBottomSheet() {
        mySheeting.sheets.value = Sheets.BillTotalAndCategories
        mySheeting.show()
    }

    @OptIn(ExperimentalMaterialApi::class)
    private fun showCameraOrGallery() {
        mySheeting.sheets.value = Sheets.ImagePicker
        mySheeting.show()
    }

    private fun handleCameraOrGallery(arg: String) {
        when (arg){
            "Camera" -> {
                capturePicture()
            }
            "Gallery" -> {
                takePicture()
            }
        }
    }

    @OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterialApi::class)
    private fun capturePicture() {
        mySheeting.hide()
        viewModelScope.launch {
            val p = Manifest.permission.CAMERA
            val state = permissionHandler.check(p)
            if (state?.allPermissionsGranted == true){
                receipt.value = resultingActivityHandler.takePicturePreview()
            } else {
                val result = permissionHandler.request(p)
                if (result?.get(p) == true){
                    receipt.value = resultingActivityHandler.takePicturePreview()
                }
            }
        }
    }
    private fun takePicture() {
        mySheeting.hide()
        viewModelScope.launch {
            val uri = resultingActivityHandler.getContent("image/*")
            receipt.value = uri
        }
    }

    // ///////////////////////////////////////
    init {
        resolver.addAll(
            DataIds.statusBarColor to _statusBarColor,
            DataIds.paidList to _members,
            DataIds.adjustedList to _members,
            DataIds.billTotal to _billTotal,
            DataIds.subCategoryText to _description,
            DataIds.dateText to _dateText,
            DataIds.paidRemaining to paidRemaining,
            DataIds.adjustRemaining to adjustRemaining,
            DataIds.selectedTabIndex to paidByOrAdjustSplitMode,
            DataIds.selectedListOption to adjustMethod,
            DataIds.receipt to receipt,
            DataIds.sheets to DataIds.sheets,
            DataIds.category to category,
            // ////////////////////////////
            DataIds.statusBarColor to _statusBarColor,
            DataIds.canProceedWithBillTotal to DataIds.canProceedWithBillTotal,
            DataIds.canProceedWithCategory to DataIds.canProceedWithCategory
        )

        paidRemaining.value = _billTotal.value
        viewModelScope.launch(Dispatchers.IO) {
            val members = DataBank.once[DataBank.Key.SplitMembers] as List<ContactData>//repo.getPaidByMembers()
            withContext(Dispatchers.Main) {
                _members.addAll(members.map {
                    MemberPayment(
                        id = it.id,
                        name = it.name,
                        mobile = it.mobile,
                        image = it.image,
                        paid = 0.0,
                        toPay = 0.0,
                        selected = false
                    )
                })
                // /////////////////////
                val memberCount = _members.size
                val contribute = _billTotal.value / memberCount
                for (i in 0 until memberCount){
                    _members[i] = _members[i].copy(toPay = contribute)
                }
            }
            //val pageData = repo.splitReviewPageData()
            withContext(Dispatchers.Main) {
                val billTotal = DataBank.once[DataBank.Key.BillTotal] as? String?:return@withContext
                _billTotal.value = billTotal.toDouble()//pageData.amount.toDouble()
                _description.value = DataBank.once[DataBank.Key.SplitDescription] as? String?:return@withContext//pageData.description
                category.value = DataBank.once[DataBank.Key.SplitCategory] as? Category?:return@withContext//pageData.category
                val date = DataBank.once[DataBank.Key.ExpenseData] as? Date?:return@withContext
                _dateText.value = displayableDate(
                    y = date.year,
                    m = date.month,
                    d = date.day
                )
            }
        }
        _statusBarColor.value = StatusBarColor(
            color = RobinsEggBlue,
            darkIcons = true
        )
    }
}
