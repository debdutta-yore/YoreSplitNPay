package co.yore.splitnpay.viewModels

import android.provider.ContactsContract.Data
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import co.yore.splitnpay.R
import co.yore.splitnpay.components.components.*
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.models.DataIds
import co.yore.splitnpay.pages.SplitSelectableMember
import co.yore.splitnpay.pages.Transaction



class SplitCardDetailsViewModel: ViewModel(), WirelessViewModelInterface {
    override val softInputMode = mutableStateOf(SoftInputMode.adjustNothing)
    override val resolver = Resolver()
    override val notifier = NotificationService{id,arg->
        when(id){
            DataIds.selectBalanceMember->{
                val member = arg as? SplitSelectableMember
                val index = splitSelectableMembers.indexOf(member)
                if(index==-1){
                    return@NotificationService
                }
                splitSelectableMembers.forEachUpdate {
                    it.copy(isSelected = false)
                }
                splitSelectableMembers[index] = splitSelectableMembers[index].copy(isSelected = true)
                val name = splitSelectableMembers[index].name
                selectedMemberName.value = if(name.lowercase()=="you") "$name'll" else "$name will"
            }
            DataIds.transactionStatus->{
                selectedGetOption.value = arg as? TransactionStatus?:return@NotificationService
            }
            DataIds.payTransactionStatus->{
                selectedPayOption.value = arg as? YouWillPayTransactionStatus?:return@NotificationService
            }
            DataIds.back -> {
                navigation.scope { navHostController, lifecycleOwner, toaster ->
                    navHostController.popBackStack()
                }
            }
            "${DataIds.back}split_card_details" -> {
                navigation.scope { navHostController, lifecycleOwner, toaster ->
                    navHostController.popBackStack()
                }
            }
        }
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

    private val selectedMemberName = mutableStateOf("You'll")
    private val getMembers = mutableStateListOf<MemberWillGetOrPayDetailsSingleRowItem>()
    private val totalGet = mutableStateOf(0f)

    private val payMembers = mutableStateListOf<MemberWillGetOrPayDetailsSingleRowItem>()
    private val totalPay = mutableStateOf(0f)

    private val paidList = mutableStateListOf<MemberTransact>()

    private val splitAmong = mutableStateListOf<MemberTransact>()

    private val selectedGetOption = mutableStateOf(TransactionStatus.Pending)
    private val selectedPayOption = mutableStateOf(YouWillPayTransactionStatus.Pending)
    private val statusBarColor = mutableStateOf(
        StatusBarColor(
            color = Color(0xff00CEC3),
            darkIcons = false
        )
    )
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
            DataIds.splitTransacted to splitTransacted,
            DataIds.transactionStatus to selectedGetOption,
            DataIds.payTransactionStatus to selectedPayOption,
            DataIds.statusBarColor to statusBarColor
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
        splitSelectableMembers.addAll(
            listOf(
                SplitSelectableMember(
                    name = "You",
                    image = "https://i.pravatar.cc/300",
                    isSelected = true,
                ),
                SplitSelectableMember(
                    name = "Sushil",
                    image = "https://i.pravatar.cc/300",
                    isSelected = false,
                ),
                SplitSelectableMember(
                    name = "Manisha",
                    image = "https://i.pravatar.cc/300",
                    isSelected = false,
                ),
                SplitSelectableMember(
                    name = "Tanisha",
                    image = "https://i.pravatar.cc/300",
                    isSelected = false,
                ),
                SplitSelectableMember(
                    name = "Sanjana",
                    image = "https://i.pravatar.cc/300",
                    isSelected = false,
                ),
                SplitSelectableMember(
                    name = "Arvind",
                    image = "https://i.pravatar.cc/300",
                    isSelected = false,
                ),
            )
        )
        getMembers.addAll(
            listOf(
                MemberWillGetOrPayDetailsSingleRowItem(
                    userName = "Sushil Roy",
                    imageUrl = "https://i.pravatar.cc/300",
                    userPhNo = "7610036985",
                    getAmount = 600f,
                    paidAmount = 0f
                ),
                MemberWillGetOrPayDetailsSingleRowItem(
                    userName = "Sanjana Roy",
                    imageUrl = "https://i.pravatar.cc/300",
                    userPhNo = "8953246985",
                    getAmount = 600f,
                    paidAmount = 0f
                ),
            )
        )
        payMembers.addAll(
            listOf(
                MemberWillGetOrPayDetailsSingleRowItem(
                    userName = "Sushil Roy",
                    imageUrl = "https://i.pravatar.cc/300",
                    userPhNo = "7610036985",
                    getAmount = 600f,
                    paidAmount = 0f
                ),
                MemberWillGetOrPayDetailsSingleRowItem(
                    userName = "Sanjana Roy",
                    imageUrl = "https://i.pravatar.cc/300",
                    userPhNo = "8953246985",
                    getAmount = 600f,
                    paidAmount = 0f
                ),
            )
        )
        paidList.addAll(
            listOf(
                MemberTransact(
                    name = "You",
                    mobile = "7923012593",
                    image = "https://i.pravatar.cc/300",
                    amount = 5000f
                ),
                MemberTransact(
                    name = "Manisha Roy",
                    mobile = "9610356210",
                    image = "https://i.pravatar.cc/300",
                    amount = 5000f
                ),
            )
        )
        splitAmong.addAll(
            listOf(
                MemberTransact(
                    name = "You",
                    mobile = "7923012593",
                    image = "https://i.pravatar.cc/300",
                    amount = 5000f
                ),
                MemberTransact(
                    name = "Sushil Roy",
                    mobile = "9610356210",
                    image = "https://i.pravatar.cc/300",
                    amount = 5000f
                ),
                MemberTransact(
                    name = "Sanjana Roy",
                    mobile = "9610356210",
                    image = "https://i.pravatar.cc/300",
                    amount = 5000f
                ),
                MemberTransact(
                    name = "Ankita Roy",
                    mobile = "9610356210",
                    image = "https://i.pravatar.cc/300",
                    amount = 5000f
                ),
            )
        )
    }
}