package co.yore.splitnpay.viewModels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.models.DataIds
import co.yore.splitnpay.models.Store
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
            }
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