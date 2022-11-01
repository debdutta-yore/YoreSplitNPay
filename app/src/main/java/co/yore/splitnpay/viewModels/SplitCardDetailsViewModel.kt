package co.yore.splitnpay.viewModels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.libs.jerokit.*
import co.yore.splitnpay.models.*
import co.yore.splitnpay.repo.MasterRepo
import co.yore.splitnpay.repo.MasterRepoImpl
import co.yore.splitnpay.ui.theme.RobinsEggBlue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplitCardDetailsViewModel(
    val repo: MasterRepo = MasterRepoImpl()
) : ViewModel(), WirelessViewModelInterface {
    override val softInputMode = mutableStateOf(SoftInputMode.adjustNothing)
    override val resolver = Resolver()
    override val notifier =
        NotificationService { id, arg ->
            when (id) {
                DataIds.selectBalanceMember -> {
                    val member = arg as? SplitSelectableMember
                    val index = splitSelectableMembers.indexOf(member)
                    if (index == -1) {
                        return@NotificationService
                    }
                    splitSelectableMembers.forEachUpdate {
                        it.copy(isSelected = false)
                    }
                    splitSelectableMembers[index] =
                        splitSelectableMembers[index].copy(isSelected = true)
                    val name = splitSelectableMembers[index].name
                    selectedMemberName.value =
                        if (name.lowercase() == "you") "$name'll" else "$name will"
                }
                DataIds.transactionStatus -> {
                    selectedGetOption.value =
                        arg as? TransactionStatus1 ?: return@NotificationService
                }
                DataIds.payTransactionStatus -> {
                    selectedPayOption.value =
                        arg as? YouWillPayTransactionStatus ?: return@NotificationService
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

    // /////////////
    private val splitNote = mutableStateOf("Business trip")
    private val splitAmount = mutableStateOf(10000f)

    private val splitBalance = mutableStateOf(0f)
    private val splitStatusMessage = mutableStateOf("")
    private val splitProgress = mutableStateOf(0f)
    private val splitPaidMark = mutableStateOf("")
    private val splitTransacted = mutableStateOf(0f)

    private val splitCardDetailsData = mutableStateOf(SplitCardDetailsData.blank)

    private val splitSelectableMembers = mutableStateListOf<SplitSelectableMember>()

    private val selectedMemberName = mutableStateOf("You'll")
    private val getMembers = mutableStateListOf<MemberWillGetOrPayDetailsSingleRowItem>()
    private val totalGet = mutableStateOf(0f)

    private val payMembers = mutableStateListOf<MemberWillGetOrPayDetailsSingleRowItem>()
    private val totalPay = mutableStateOf(0f)

    private val paidList = mutableStateListOf<MemberTransact>()

    private val splitAmong = mutableStateListOf<MemberTransact>()

    private val selectedGetOption = mutableStateOf(TransactionStatus1.Pending)
    private val selectedPayOption = mutableStateOf(YouWillPayTransactionStatus.Pending)
    private val statusBarColor = mutableStateOf(
        StatusBarColor(
            color = RobinsEggBlue,
            darkIcons = false
        )
    )

    // /////////////
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

        viewModelScope.launch(Dispatchers.IO) {
            val pageData = repo.sampleSplitCardDetails()
            withContext(Dispatchers.Main) {
                splitCardDetailsData.value = pageData.splitCardDetailsData
                splitBalance.value = pageData.splitBalance
                splitStatusMessage.value = pageData.splitStatusMessage
                splitProgress.value = pageData.splitProgress
                splitPaidMark.value = pageData.splitPaidMark
                splitTransacted.value = pageData.splitTransacted
                splitAmount.value = pageData.splitAmount
            }
            val _splitSelectableMembers = repo.splitSelectableMembers()
            withContext(Dispatchers.Main) {
                splitSelectableMembers.addAll(
                    _splitSelectableMembers
                )
            }
            val _getMembers = repo.splitCardGetMembers()
            withContext(Dispatchers.Main) {
                getMembers.addAll(_getMembers)
            }

            val _payMembers = repo.splitCardPayMembers()
            withContext(Dispatchers.Main) {
                payMembers.addAll(_payMembers)
            }
            val _splitCardPaidList = repo.splitCardPaidList()
            withContext(Dispatchers.Main) {
                paidList.addAll(_splitCardPaidList)
            }

            val _splitAmong = repo.splitCardSplitAmong()
            withContext(Dispatchers.Main) {
                splitAmong.addAll(_splitAmong)
            }
        }
    }
}
