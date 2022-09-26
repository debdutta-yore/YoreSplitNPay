package co.yore.splitnpay

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import co.yore.splitnpay.friend_item.models.PeopleData

enum class SplitPageState{
    GET,
    PAY,
    NONE
}

class SplitPageViewModel: ViewModel() {
    private val _notificationService = NotificationService{id,arg->
        when(id){
            DataIds.textInput->{
                _input.value = (arg as? String)?:return@NotificationService
            }
        }
    }
    val notifier = _notificationService
    private val _resolver = Resolver()
    val resolver = _resolver
    private val _whole = mutableStateOf("00")
    private val _decimal = mutableStateOf("00")
    private val _wholeGet = mutableStateOf("00")
    private val _decimalGet = mutableStateOf("00")
    private val _wholePay = mutableStateOf("00")
    private val _decimalPay = mutableStateOf("00")
    private val _statusBarColor = mutableStateOf<StatusBarColor?>(null)
    private val _ultimateState = mutableStateOf(SplitPageState.NONE)
    private val _groups = mutableStateListOf<GroupCardData>()
    private val _peoples = mutableStateListOf<PeopleData>()
    private val _willGetActive = mutableStateOf(false)
    private val _willPayActive = mutableStateOf(false)
    private val _input = mutableStateOf("")
    init {
        resolver.addAll(
            DataIds.whole to _whole,
            DataIds.decimal to _decimal,
            DataIds.wholeGet to _wholeGet,
            DataIds.decimalGet to _decimalGet,
            DataIds.wholePay to _wholePay,
            DataIds.decimalPay to _decimalPay,
            DataIds.statusBarColor to _statusBarColor,
            DataIds.groups to _groups,
            DataIds.peoples to _peoples,
            DataIds.willGetActive to _willGetActive,
            DataIds.willPayActive to _willPayActive,
            DataIds.textInput to _input,
            DataIds.ultimateState to _ultimateState,
        )
        _statusBarColor.value = StatusBarColor(Color.Black,true)
    }
}