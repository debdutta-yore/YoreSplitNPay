package co.yore.splitnpay.viewModels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.models.DataIds
import co.yore.splitnpay.models.TransactionType
import co.yore.splitnpay.pages.Transaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface SplitReviewRepository {
    suspend fun getPaidByMembers(): List<Transaction>
    suspend fun getAdjustSplit(): List<Transaction>
}
class SplitReviewMock : SplitReviewRepository {
    private val adjustedList = listOf<Transaction>(
        Transaction(
            name = "You",
            imageUrl = "",
            mobileNumber = "9563376942",
            amount = 0f,
            TransactionType.Paid
        ),
        Transaction(
            name = "Manisha Roy",
            imageUrl = "",
            mobileNumber = "9563376942",
            amount = 3000.1f,
            TransactionType.Paid
        ),
        Transaction(
            name = "Sushil Roy",
            imageUrl = "",
            mobileNumber = "9563376942",
            amount = 3000f,
            TransactionType.Paid
        ),
        Transaction(
            name = "Sanjana Ray",
            imageUrl = "",
            mobileNumber = "9563376942",
            amount = 2500f,
            TransactionType.Paid
        ),
    )
    private val paidList = listOf<Transaction>(
        Transaction(
            name = "You",
            mobileNumber = "9563376942",
            amount = 5000f,
            transactionType = TransactionType.Paid
        ),
        Transaction(
            name = "Manisha Roy",
            mobileNumber = "9563376942",
            amount = 5000f,
            transactionType = TransactionType.Paid
        ),
        Transaction(
            name = "Sushil Roy",
            mobileNumber = "9563376942",
            amount = 0f,
            transactionType = TransactionType.Unspecified
        ),
        Transaction(
            name = "Sanjanaa Ray",
            mobileNumber = "9563376942",
            amount = 0f,
            transactionType = TransactionType.Unspecified
        ),
    )

    override suspend fun getPaidByMembers(): List<Transaction> {
        return paidList
    }

    override suspend fun getAdjustSplit(): List<Transaction> {
        return adjustedList
    }

}
class SplitReviewViewModel(
    private val repo: SplitReviewRepository = SplitReviewMock()
) : ViewModel(), WirelessViewModelInterface {
    override val resolver = Resolver()
    override val navigation = Navigation()
    override val permissionHandler = PermissionHandler()
    override val resultingActivityHandler = ResultingActivityHandler()

    //////////////////////////////////////////
    private val _adjustedList = mutableStateListOf<Transaction>()
    private val _paidList = mutableStateListOf<Transaction>()
    private val _statusBarColor = mutableStateOf<StatusBarColor?>(null)
    private val _groupName = mutableStateOf("Office buddies")
    private val _billTotal = mutableStateOf(10000f)
    private val _subCategoryText = mutableStateOf("")
    private val _categoryText = mutableStateOf("Trip")
    private val _dateText = mutableStateOf("7th June, 2022")
    private val _numberOfGroupMembers = mutableStateOf(5)

    //////////////////////////////////////////
    override val notifier = NotificationService { id, arg ->
        when (id) {
            DataIds.back -> {
                navigation.scope { navHostController, lifecycleOwner, toaster ->
                    navHostController.popBackStack()
                }
            }
            DataIds.selectPaidByMeberClick -> {
                viewModelScope.launch(Dispatchers.IO) {
                    withContext(Dispatchers.Main) {
                        val index = _paidList.indexOf(arg)
                        _paidList[index] =
                            _paidList[index].copy(isSelected = !_paidList[index].isSelected)
                    }
                }
            }
            DataIds.editBillAmountClick -> {

            }
            DataIds.categoryClick -> {

            }
            DataIds.dateClick -> {

            }
            DataIds.receiptClick -> {

            }
            DataIds.confirmSplitClick -> {
                navigation.scope { navHostController, lifecycleOwner, toaster ->
                    navHostController.navigate("group_chat_screen")
                }
            }
            DataIds.scanClick -> {

            }
            DataIds.subCategoryText -> {
                _subCategoryText.value = (arg as? String) ?: ""
            }
        }
    }

    /////////////////////////////////////////
    init {
        resolver.addAll(
            DataIds.statusBarColor to _statusBarColor,
            DataIds.groupName to _groupName,
            DataIds.paidList to _paidList,
            DataIds.adjustedList to _adjustedList,
            DataIds.billTotal to _billTotal,
            DataIds.subCategoryText to _subCategoryText,
            DataIds.categoryText to _categoryText,
            DataIds.dateText to _dateText,
            DataIds.numberOfGroupMembers to _numberOfGroupMembers,
        )

        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                _paidList.addAll(
                    repo.getPaidByMembers()
                )
                _adjustedList.addAll(
                    repo.getAdjustSplit()
                )
            }
        }

        _statusBarColor.value = StatusBarColor(
            color = StatusBarGreen,
            darkIcons = true
        )
    }
}