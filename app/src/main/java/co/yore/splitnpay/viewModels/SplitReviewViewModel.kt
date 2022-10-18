package co.yore.splitnpay.viewModels

import android.Manifest
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.yore.splitnpay.components.components.BillTotalBottomSheetModel
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.models.Category
import co.yore.splitnpay.models.DataIds
import co.yore.splitnpay.models.Sheets
import co.yore.splitnpay.models.Store
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface SplitReviewRepository {
    suspend fun getPaidByMembers(): List<MemberPayment>
}
class SplitReviewMock : SplitReviewRepository {
    override suspend fun getPaidByMembers(): List<MemberPayment> {
        return listOf(
            MemberPayment(
                0,
                "You",
                "8967114927",
                image = "https://i.pravatar.cc/100"
            ),
            MemberPayment(
                1,
                "Manisha Roy",
                "9456321025",
                image = "https://i.pravatar.cc/100"
            ),
            MemberPayment(
                2,
                "Sushil Roy",
                "9746310862",
                image = "https://i.pravatar.cc/100"
            ),
            MemberPayment(
                3,
                "Sanjana Roy",
                "8319764035",
                image = "https://i.pravatar.cc/100"
            ),
            MemberPayment(
                4,
                "Ankita Roy",
                "7614563289",
                image = "https://i.pravatar.cc/100"
            ),
        )
    }

}

data class MemberPayment(
    val id: Any,
    val name: String,
    val mobile: String,
    val image: Any?,
    val paid: Double = 0.0,
    val toPay: Double = 0.0,
    val selected: Boolean = false
)

class SplitReviewViewModel(
    private val repo: SplitReviewRepository = SplitReviewMock(),
    private val groupRepo: GroupRepository = GroupsMock()
) : ViewModel(), WirelessViewModelInterface {
    override val resolver = Resolver()
    override val navigation = Navigation()
    override val permissionHandler = PermissionHandler()
    override val resultingActivityHandler = ResultingActivityHandler()
    /////////////
    private val billTotalBottomSheetModel = BillTotalBottomSheetModel()
    //////////////
    @OptIn(ExperimentalMaterialApi::class)
    override val sheetHandler = SheetHandler(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
        confirmStateChange = {
            true
        },
        onVisibilityChange = {
            if(!it){
                sheets.value = Sheets.None
            }
        }
    )

    //////////////////////////////////////////
    private val _members = mutableStateListOf<MemberPayment>()
    private val _statusBarColor = mutableStateOf<StatusBarColor?>(null)
    private val _groupName = mutableStateOf("Office buddies")
    private val _billTotal = mutableStateOf(10000.0)
    private val _subCategoryText = mutableStateOf("")
    private val _categoryText = mutableStateOf("Trip")
    private val _dateText = mutableStateOf("7th June, 2022")
    private val _numberOfGroupMembers = mutableStateOf(5)
    private val paidRemaining = mutableStateOf(0.0)
    private val adjustRemaining = mutableStateOf(0.0)
    private val selectedTabIndex = mutableStateOf(0)
    private val selectedListOption = mutableStateOf(0)
    private val receipt = mutableStateOf<Any?>(null)
    private val sheets = mutableStateOf(Sheets.None)
    ///////////////////
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
    /////////////////////////
    private val canProceedWithBillTotal = mutableStateOf(false)
    private val capProceedWithCategory = mutableStateOf(false)

    //////////////////////////////////////////
    override val notifier = NotificationService { id, arg ->
        when (id) {
            DataIds.categoryEditClick->{
                openBillTotalBottomSheet()
            }
            DataIds.cameraOrGallery->{
                handleCameraOrGallery(arg as? String?:return@NotificationService)
            }
            DataIds.selectedTabIndex->{
                selectedTabIndex.value = arg as? Int?:return@NotificationService
            }
            DataIds.selectedListOption->{
                selectedListOption.value = arg as? Int?:return@NotificationService
                if(selectedListOption.value==0){
                    val memberCount = _members.size
                    val contribute = _billTotal.value/memberCount
                    for(i in 0 until memberCount){
                        _members[i] = _members[i].copy(toPay = contribute)
                    }
                    adjustRemaining.value = 0.0
                }
            }
            DataIds.memberPaymentCheck->{
                val index = _members.indexOf(arg as? MemberPayment)
                if(index<0){
                    return@NotificationService
                }
                val member = _members[index]
                val selected = !member.selected
                _members[index] = member.copy(selected = selected)
                val selectedCount = _members.count {
                    it.selected
                }
                val contribute = (if(selectedCount==0){
                    0f
                }
                else{
                    (_billTotal.value/selectedCount)
                }).toDouble()
                val count = _members.size
                for(i in 0 until count){
                    val _member = _members[i]
                    val _selected = _member.selected
                    if(_selected){
                        _members[i] = _member.copy(paid = contribute)
                    }
                    else{
                        _members[i] = _member.copy(paid = 0.0)
                    }
                }
                paidRemaining.value = if(contribute==0.0) _billTotal.value else 0.0
            }
            DataIds.paidByAmount->{
                if(arg !is Store){
                    return@NotificationService
                }
                val member = arg["member"] as? MemberPayment?:return@NotificationService
                val amount = arg["amount"] as? Double?:return@NotificationService
                val index = _members.indexOf(member)
                if(index<0){
                    return@NotificationService
                }
                _members[index] = _members[index].copy(paid = amount)
                val paid = _members.sumOf { it.paid }.toFloat()
                paidRemaining.value = _billTotal.value - paid
            }
            DataIds.adjustByAmount->{
                if(arg !is Store){
                    return@NotificationService
                }
                val member = arg["member"] as? MemberPayment?:return@NotificationService
                val amount = arg["amount"] as? Double?:return@NotificationService
                val index = _members.indexOf(member)
                if(index<0){
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
                /*val index = _paidList.indexOf(arg as? MemberPayment?:return@NotificationService)
                if(index==-1){
                    return@NotificationService
                }*/
                /*_paidList[index] =
                    _paidList[index].copy(isSelected = !_paidList[index].isSelected)*/
            }
            DataIds.editBillAmountClick -> {

            }
            DataIds.categoryClick -> {

            }
            DataIds.dateClick -> {

            }
            DataIds.receiptClick -> {
                showCameraOrGallery()
            }
            DataIds.confirmSplitClick -> {

            }
            DataIds.scanClick -> {

            }
            DataIds.subCategoryText -> {
                _subCategoryText.value = (arg as? String) ?: ""
            }
            DataIds.billTotalAmount->{
                _billTotalAmount.value = arg as? String?:return@NotificationService
            }
            DataIds.billTotalDescription->{
                _billTotalDescription.value = arg as? String?:return@NotificationService
            }
            DataIds.categorySelectionClick->{
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

    @OptIn(ExperimentalMaterialApi::class)
    private fun openBillTotalBottomSheet() {
        _billTotalAmount.value = _billTotal.value.toInt().toString()
        sheets.value = Sheets.BillTotalAndCategories
        sheetHandler.state {
            show()
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    private fun showCameraOrGallery() {
        sheets.value = Sheets.ImagePicker
        sheetHandler.state {
            show()
        }
    }

    private fun handleCameraOrGallery(arg: String) {
        when(arg){
            "Camera"->{
                capturePicture()
            }
            "Gallery"->{
                takePicture()
            }
        }
    }

    @OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterialApi::class)
    private fun capturePicture() {
        sheetHandler.state {
            hide()
        }
        viewModelScope.launch {
            val p = Manifest.permission.CAMERA
            val state = permissionHandler.check(p)
            if(state?.allPermissionsGranted==true){
                receipt.value = resultingActivityHandler.takePicturePreview()
            }
            else{
                val result = permissionHandler.request(p)
                if(result?.get(p)==true){
                    receipt.value = resultingActivityHandler.takePicturePreview()
                }
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    private fun takePicture() {
        sheetHandler.state {
            hide()
        }
        viewModelScope.launch {
            val uri = resultingActivityHandler.getContent("image/*")
            receipt.value = uri
        }
    }

    /////////////////////////////////////////
    init {
        resolver.addAll(
            DataIds.statusBarColor to _statusBarColor,
            DataIds.groupName to _groupName,
            DataIds.paidList to _members,
            DataIds.adjustedList to _members,
            DataIds.billTotal to _billTotal,
            DataIds.subCategoryText to _subCategoryText,
            DataIds.categoryText to _categoryText,
            DataIds.dateText to _dateText,
            DataIds.numberOfGroupMembers to _numberOfGroupMembers,
            DataIds.paidRemaining to paidRemaining,
            DataIds.adjustRemaining to adjustRemaining,
            DataIds.selectedTabIndex to selectedTabIndex,
            DataIds.selectedListOption to selectedListOption,
            DataIds.receipt to receipt,
            DataIds.sheets to sheets,
            //////////////////////////////
            DataIds.categories to _categories,
            DataIds.statusBarColor to _statusBarColor,
            DataIds.billTotalAmount to _billTotalAmount,
            DataIds.billTotalDescription to _billTotalDescription,
            DataIds.renamePressed to _renamePressed,
            ////////////
            DataIds.allCategories to _allCategories,
            DataIds.isAddCategoryEnabled to _isAddCategoryEnabled,
            DataIds.addCategoryName to _addCategoryName,
            /////////////
            DataIds.canProceedWithBillTotal to canProceedWithBillTotal,
            DataIds.capProceedWithCategory to capProceedWithCategory,
            DataIds.billTotalBottomSheetModel to billTotalBottomSheetModel,
        )
        viewModelScope.launch(Dispatchers.IO) {
            val categoriesList = groupRepo.getCategories()
            withContext(Dispatchers.Main) {
                _categories.addAll(
                    categoriesList
                )
            }
        }
        paidRemaining.value = _billTotal.value
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                _members.addAll(
                    repo.getPaidByMembers()
                )
                ///////////////////////
                val memberCount = _members.size
                val contribute = _billTotal.value/memberCount
                for(i in 0 until memberCount){
                    _members[i] = _members[i].copy(toPay = contribute)
                }
            }
        }
        _statusBarColor.value = StatusBarColor(
            color = StatusBarGreen,
            darkIcons = true
        )
    }
}