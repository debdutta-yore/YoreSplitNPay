package co.yore.splitnpay.viewModels

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import co.yore.splitnpay.components.components.MemberTransact
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.models.DataIds
import co.yore.splitnpay.models.TransactionType
import co.yore.splitnpay.pages.SplitSelectableMember
import co.yore.splitnpay.pages.Transaction

class IndividualSummaryViewModel: ViewModel(), WirelessViewModelInterface {
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
                //selectedMemberName.value = if(name.lowercase()=="you") "$name'll" else "$name will"
            }
        }
    }
    override val navigation = Navigation()
    override val permissionHandler = PermissionHandler()
    override val resultingActivityHandler = ResultingActivityHandler()
    //////////////////
    private val willGetTransactions = mutableStateListOf<MemberTransact>()
    private val willPayTransactions = mutableStateListOf<MemberTransact>()
    private val getTotal = mutableStateOf(0f)
    private val payTotal = mutableStateOf(0f)
    private val payeeName = mutableStateOf("You")
    private val payerName = mutableStateOf("You")
    private val selectedBalanceExpenseTab = mutableStateOf(0)
    private val statusBarColor = mutableStateOf(
        StatusBarColor(
            Color(0xffEDF3F9),
            darkIcons = false
        )
    )
    private val splitSelectableMembers = mutableStateListOf<SplitSelectableMember>()
    //////////////////
    init {
        resolver.addAll(
            DataIds.willGetTransactions to willGetTransactions,
            DataIds.willPayTransactions to willPayTransactions,
            DataIds.getTotal to getTotal,
            DataIds.payTotal to payTotal,
            DataIds.payeeName to payeeName,
            DataIds.payerName to payerName,
            DataIds.selectedBalanceExpenseTab to selectedBalanceExpenseTab,
            DataIds.statusBarColor to statusBarColor,
            DataIds.splitSelectableMembers to splitSelectableMembers
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
            )
        )
        willGetTransactions.add(
            MemberTransact(
                name = "Sushil",
                image = "https://i.pravatar.cc/300",
                amount = 1000f,
                mobile = "7610236589"
            )
        )
        willPayTransactions.add(
            MemberTransact(
                name = "Sushil",
                image = "https://i.pravatar.cc/300",
                amount = 1000f,
                mobile = "7610236589"
            )
        )
    }
}