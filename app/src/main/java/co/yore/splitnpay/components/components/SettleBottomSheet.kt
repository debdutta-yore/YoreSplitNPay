package co.yore.splitnpay.components.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import co.yore.splitnpay.R
import co.yore.splitnpay.addmembers.FontFamilyText
import co.yore.splitnpay.demos.sx
import co.yore.splitnpay.demos.sy
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.models.DataIds
import co.yore.splitnpay.pages.Transaction
import co.yore.splitnpay.ui.theme.DarkBlue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettleBottomSheetModel(val callback: Callback): BottomSheetModel{
    interface Callback{
        fun scope(): CoroutineScope
        suspend fun getGetStat(): List<Transaction>
        suspend fun getPayStat(): List<Transaction>
        fun onGetContinue(transaction: Transaction)
        fun onPayContinue(transaction: Transaction)
    }
    private val _resolver = Resolver()
    private var getSelectedIndex = -1
    private var paySelectedIndex = -1
    private val _notifier = NotificationService{id,arg->
        when(id){
            DataIds.willGetPayTab -> {
                _willGetPayTab.value = arg as SettleBottomSheetTabs
            }
            DataIds.selectSettleGetMembers -> {
                val index = arg as Int
                val item = _willGet[index]
                val selected = !item.isSelected
                if(getSelectedIndex>-1){
                    _willGet[getSelectedIndex] = _willGet[getSelectedIndex].copy(isSelected = false)
                }
                _willGet[index] = _willGet[index].copy(isSelected = selected)
                getSelectedIndex = if(selected){
                    index
                } else{
                    -1
                }
                _isWillGetTransactionSelected.value = _willGet.any { it.isSelected }
            }
            DataIds.selectSettlePayMembers -> {
                val index = arg as Int
                val item = _willPay[index]
                item.apply {  }
                val selected = !item.isSelected
                if(paySelectedIndex>-1){
                    _willPay[paySelectedIndex] = _willPay[paySelectedIndex].copy(isSelected = false)
                }
                _willPay[index] = _willPay[index].copy(isSelected = selected)
                paySelectedIndex = if(selected){
                    index
                } else{
                    -1
                }
                _isWillPayTransactionSelected.value = _willPay.any { it.isSelected }
            }
            DataIds.willGetSettleClick->{
                callback.onGetContinue(_willGet[getSelectedIndex])
            }
            DataIds.willPaySettleClick->{
                callback.onPayContinue(_willPay[paySelectedIndex])
            }
        }
    }
    ////////////
    override val resolver = _resolver
    override val notifier = _notifier
    override val scope get() = callback.scope()

    @Composable
    override fun Content() {
        YouWillGetPayBottomSheet_al32sa()
    }

    override fun initialize() {
        clear()
        scope.launch(Dispatchers.IO) {
            val willGet = callback.getGetStat()
            val willPay = callback.getPayStat()
            withContext(Dispatchers.Main){
                _willGet.addAll(willGet)
                _willPay.addAll(willPay)
                _settleGetTotal.value = _willGet.sumOf { it.amount.toDouble() }.toFloat()
                _settlePayTotal.value = _willPay.sumOf { it.amount.toDouble() }.toFloat()
            }
        }
    }

    override fun clear() {
        _willGet.clear()
        _willPay.clear()
        getSelectedIndex = -1
        paySelectedIndex = -1
        _isWillGetTransactionSelected.value = false
        _isWillPayTransactionSelected.value = false
        _settleGetTotal.value = 0f
        _settlePayTotal.value = 0f
        _willGetPayTab.value = SettleBottomSheetTabs.YouWillGet
    }

    override fun onBack() {

    }
    /////////////////
    private val _willGet = mutableStateListOf<Transaction>()
    private val _willPay = mutableStateListOf<Transaction>()
    private val _isWillGetTransactionSelected = mutableStateOf(false)
    private val _isWillPayTransactionSelected = mutableStateOf(false)
    private val _settleGetTotal = mutableStateOf(0f)
    private val _settlePayTotal = mutableStateOf(0f)
    private val _willGetPayTab = mutableStateOf(SettleBottomSheetTabs.YouWillGet)
    ////////////////
    init {
        _resolver.addAll(
            DataIds.selectSettleGetMembers to _willGet,
            DataIds.selectSettlePayMembers to _willPay,
            DataIds.isWillGetTransactionSelected to _isWillGetTransactionSelected,
            DataIds.isWillPayTransactionSelected to _isWillPayTransactionSelected,
            DataIds.settleGetTotal to _settleGetTotal,
            DataIds.settlePayTotal to _settlePayTotal,
            DataIds.willGetPayTab to _willGetPayTab,
        )
    }
}

enum class SettleBottomSheetTabs {
    YouWillGet,
    YouWillPay
}

@Composable
fun YouWillGetPayBottomSheet_al32sa(
    getTransactions: List<Transaction> = listState(key = DataIds.selectSettleGetMembers),
    payTransactions: List<Transaction> = listState(key = DataIds.selectSettlePayMembers),
    isWillGetTransactionSelected: Boolean = boolState(key = DataIds.isWillGetTransactionSelected).value,
    isWillPayTransactionSelected: Boolean = boolState(key = DataIds.isWillPayTransactionSelected).value,
    settleGetTotal: Float = floatState(key = DataIds.settleGetTotal).value,
    settlePayTotal: Float = floatState(key = DataIds.settlePayTotal).value,
    willGetPayTab: SettleBottomSheetTabs = tState<SettleBottomSheetTabs>(key = DataIds.willGetPayTab).value,
    contentDescription: String = "",
    notifier: NotificationService = notifier(),
    config: YouWillGetPayBottomSheetConfiguration = YouWillGetPayBottomSheetConfiguration()
) {
    val isGetTabSelected = remember(key1 = willGetPayTab) {
        derivedStateOf {
            willGetPayTab == SettleBottomSheetTabs.YouWillGet
        }
    }
    val getTabColor = animateColorAsState(
        if (willGetPayTab == SettleBottomSheetTabs.YouWillGet) {
            DarkBlue
        } else {
            LightBlue3
        }
    )
    
    val payTabColor = animateColorAsState(
        if (willGetPayTab == SettleBottomSheetTabs.YouWillPay) {
            DarkBlue
        } else {
            LightBlue3
        }
    )

    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier
            .semantics { this.contentDescription = contentDescription }
            .fillMaxWidth()
    ) {
        config.bottomSheetHolderTopPadding.sy()
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50.dep()))
                .height(2.dep())
                .width(19.dep())
                .background(Color(0xff5A87BB))
                .align(Alignment.CenterHorizontally)
        )
        config.bottomSheetHolderBottomPadding.sy()
        Row(
            modifier = Modifier
                .padding(start = config.youWillGetTextStartPadding.dep())
        ) {
            FontFamilyText(
                modifier = Modifier.clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    notifier.notify(DataIds.willGetPayTab, SettleBottomSheetTabs.YouWillGet)
                },
                text = stringResource(id = R.string.you_will_get),
                color = getTabColor.value,
                fontSize = 16.sep(),
                fontWeight = FontWeight.Bold,
                lineHeight = 18.75.sep(),
                letterSpacing = 0.67.sep()
            )
            config.gapBetweenYouWillPayAndYouWillGet.sx()
            FontFamilyText(
                modifier = Modifier.clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    notifier.notify(DataIds.willGetPayTab, SettleBottomSheetTabs.YouWillPay)
                },
                text = stringResource(id = R.string.you_will_pay),
                color = payTabColor.value,
                fontSize = 16.sep(),
                fontWeight = FontWeight.Bold,
                lineHeight = 18.75.sep(),
                letterSpacing = 0.67.sep()
            )
        }
        Box(
            modifier = Modifier
                .wrapContentHeight()
                .padding(
                    top = config.youWillGetBoxTopPadding.dep(),
                    start = config.youWillGetBoxStartPadding.dep(),
                    end = config.youWillGetBoxEndPadding.dep()
                )
        ) {
            if (isGetTabSelected.value) {
                YouWillGet(
                    userName = stringResource(R.string.you),
                    total = settleGetTotal,
                    list = getTransactions,
                    isWillGetTransactionSelected = isWillGetTransactionSelected,
                    contentDescription = "YouWillGetSection"
                )
            } else {
                YouWillPay_m6awbp(
                    userName = stringResource(R.string.you),
                    total = settlePayTotal,
                    list = payTransactions,
                    isWillPayTransactionSelected = isWillPayTransactionSelected,
                    contentDescription = "YouWillPaySection"
                )
            }
        }
    }
}

data class YouWillGetPayBottomSheetConfiguration(
    val bottomSheetHolderTopPadding: Float = 20f,
    val bottomSheetHolderBottomPadding: Float = 41f,
    val bottomSheetHolderColor: Color = Color.Unspecified,
    val youWillGetTextStartPadding: Float = 33f,
    val gapBetweenYouWillPayAndYouWillGet: Float = 47f,
    val youWillGetBoxTopPadding: Float = 30f,
    val youWillGetBoxStartPadding: Float = 33f,
    val youWillGetBoxEndPadding: Float = 31f,
    val youWillPayBoxTopPadding: Float = 72f
)