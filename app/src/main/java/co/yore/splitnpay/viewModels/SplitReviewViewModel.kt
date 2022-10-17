package co.yore.splitnpay.viewModels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.models.DataIds
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
                "You",
                "8967114927",
                image = "https://i.pravatar.cc/100"
            ),
            MemberPayment(
                "Manisha Roy",
                "9456321025",
                image = "https://i.pravatar.cc/100"
            ),
            MemberPayment(
                "Sushil Roy",
                "9746310862",
                image = "https://i.pravatar.cc/100"
            ),
            MemberPayment(
                "Sanjana Roy",
                "8319764035",
                image = "https://i.pravatar.cc/100"
            ),
            MemberPayment(
                "Ankita Roy",
                "7614563289",
                image = "https://i.pravatar.cc/100"
            ),
        )
    }

}

data class MemberPayment(
    val name: String,
    val mobile: String,
    val image: Any?,
    val paid: Float = 0f,
    val toPay: Float = 0f,
)

class SplitReviewViewModel(
    private val repo: SplitReviewRepository = SplitReviewMock()
) : ViewModel(), WirelessViewModelInterface {
    override val resolver = Resolver()
    override val navigation = Navigation()
    override val permissionHandler = PermissionHandler()
    override val resultingActivityHandler = ResultingActivityHandler()

    //////////////////////////////////////////
    private val _members = mutableStateListOf<MemberPayment>()
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

            }
            DataIds.confirmSplitClick -> {

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
            DataIds.paidList to _members,
            DataIds.adjustedList to _members,
            DataIds.billTotal to _billTotal,
            DataIds.subCategoryText to _subCategoryText,
            DataIds.categoryText to _categoryText,
            DataIds.dateText to _dateText,
            DataIds.numberOfGroupMembers to _numberOfGroupMembers,
        )

        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                _members.addAll(
                    repo.getPaidByMembers()
                )
            }
        }

        _statusBarColor.value = StatusBarColor(
            color = StatusBarGreen,
            darkIcons = true
        )
    }
}