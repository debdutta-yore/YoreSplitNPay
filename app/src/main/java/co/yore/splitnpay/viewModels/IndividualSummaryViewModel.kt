package co.yore.splitnpay.viewModels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.models.DataIds
import co.yore.splitnpay.models.MemberTransact
import co.yore.splitnpay.models.SplitSelectableMember
import co.yore.splitnpay.models.StatusBarColor
import co.yore.splitnpay.repo.MasterRepo
import co.yore.splitnpay.repo.MasterRepoImpl
import co.yore.splitnpay.ui.theme.BlackSqueeze

class IndividualSummaryViewModel(
    val repo: MasterRepo = MasterRepoImpl()
) : ViewModel(), WirelessViewModelInterface {
    override val softInputMode = mutableStateOf(SoftInputMode.adjustNothing)
    override val resolver = Resolver()
    override val notifier = NotificationService{id, arg ->
        when (id){
            DataIds.selectBalanceMember -> {
                val member = arg as? SplitSelectableMember
                val index = splitSelectableMembers.indexOf(member)
                if (index == -1){
                    return@NotificationService
                }
                splitSelectableMembers.forEachUpdate {
                    it.copy(isSelected = false)
                }
                splitSelectableMembers[index] = splitSelectableMembers[index].copy(isSelected = true)
                val name = splitSelectableMembers[index].name
                // selectedMemberName.value = if(name.lowercase()=="you") "$name'll" else "$name will"
            }
            DataIds.back -> {
                navigation.scope { navHostController, lifecycleOwner, toaster ->
                    navHostController.popBackStack()
                }
            }
            "${DataIds.back}individual_summary" -> {
                navigation.scope { navHostController, lifecycleOwner, toaster ->
                    navHostController.popBackStack()
                }
            }
        }
    }
    override val navigation = Navigation()
    override val permissionHandler = PermissionHandler()
    override val resultingActivityHandler = ResultingActivityHandler()

    // ////////////////
    private val willGetTransactions = mutableStateListOf<MemberTransact>()
    private val willPayTransactions = mutableStateListOf<MemberTransact>()
    private val getTotal = mutableStateOf(0f)
    private val payTotal = mutableStateOf(0f)
    private val payeeName = mutableStateOf("You")
    private val payerName = mutableStateOf("You")
    private val selectedBalanceExpenseTab = mutableStateOf(0)
    private val statusBarColor = mutableStateOf(
        StatusBarColor(
            BlackSqueeze,
            darkIcons = false
        )
    )
    private val splitSelectableMembers = mutableStateListOf<SplitSelectableMember>()

    // ////////////////
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
            repo.splitSelectableMembers()
        )
        willGetTransactions.add(
            repo.willGetTransact()
        )
        willPayTransactions.add(
            repo.willPayTransact()
        )
    }
}
