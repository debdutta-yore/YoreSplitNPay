package co.yore.splitnpay.viewModels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import co.yore.splitnpay.R
import co.yore.splitnpay.components.components.SplitCardDetailsData
import co.yore.splitnpay.components.components.MemberWillGetOrPayDetailsSingleRowItem
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.models.DataIds
import co.yore.splitnpay.pages.SplitSelectableMember
import co.yore.splitnpay.pages.Transaction

class SplitCardDetailsViewModel: ViewModel(), WirelessViewModelInterface {
    override val resolver = Resolver()
    override val notifier = NotificationService{id,arg->

    }
    override val navigation = Navigation()
    override val permissionHandler = PermissionHandler()
    override val resultingActivityHandler = ResultingActivityHandler()
    ///////////////
    private val splitNote = mutableStateOf("Business trip")
    private val splitAmount = mutableStateOf(10000f)

    private val splitBalance = mutableStateOf(5000f)
    private val splitStatusMessage = mutableStateOf("Partially Paid / Received")
    private val splitProgress = mutableStateOf(0.3f)
    private val splitPaidMark = mutableStateOf("5 of 5 Paid")
    private val splitTransacted = mutableStateOf(3000f)

    private val splitCardDetailsData = mutableStateOf(SplitCardDetailsData.blank)

    private val splitSelectableMembers = mutableStateListOf<SplitSelectableMember>()

    private val selectedMemberName = mutableStateOf("You")
    private val getMembers = mutableStateListOf<MemberWillGetOrPayDetailsSingleRowItem>()
    private val totalGet = mutableStateOf(0f)

    private val payMembers = mutableStateListOf<MemberWillGetOrPayDetailsSingleRowItem>()
    private val totalPay = mutableStateOf(0f)

    private val paidList = mutableStateListOf<Transaction>()

    private val splitAmong = mutableStateListOf<Transaction>()
    ///////////////
    init {
        resolver.addAll(
            DataIds.splitNote to splitNote,
            DataIds.splitAmount to splitAmount,
            DataIds.splitCard to splitCardDetailsData,
            DataIds.splitSelectableMembers to splitSelectableMembers,
            DataIds.memberName to selectedMemberName,
            DataIds.getMembers to getMembers,
            DataIds.payMembers to payMembers,
            DataIds.totalGet to totalGet,
            DataIds.totalPay to totalPay,
            DataIds.paidList to paidList,
            DataIds.splitAmongMembers to splitAmong,
            DataIds.splitBalance to splitBalance,
            DataIds.splitStatusMessage to splitStatusMessage,
            DataIds.splitProgress to splitProgress,
            DataIds.splitPaidMark to splitPaidMark,
            DataIds.splitTransacted to splitTransacted
        )

        splitCardDetailsData.value = SplitCardDetailsData(
            status = "Partially Paid",
            splitMethod = "Unequal",
            categories = "Trip",
            createdBy = "You",
            createdOn = "9th May, 2022",
            noOfMembers = 5,
            categoryIcon = R.drawable.ic_trip
        )
    }
}