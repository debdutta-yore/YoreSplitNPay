package co.yore.splitnpay

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.yore.splitnpay.friend_item.models.PeopleData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object SplitDataIds{
    const val whole = 0
    const val decimal = 1
    const val wholeGet = 2
    const val decimalGet = 3
    const val wholePay= 4
    const val decimalPay = 5
    const val ultimateState = 6
    const val groups = 7
    const val peoples = 8
    const val willGetActive = 9
    const val willPayActive = 10
}

enum class SplitPageState{
    GET,
    PAY,
    NONE
}

class SplitPageViewModel: ViewModel() {
    private val _notificationService = NotificationService{id,arg->
        _whole.value = (System.currentTimeMillis()%1000).toString()
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
    private val _ultimateState = mutableStateOf(SplitPageState.NONE)
    private val _groups = mutableStateListOf<GroupCardData>()
    private val _peoples = mutableStateListOf<PeopleData>()
    private val _willGetActive = mutableStateOf(false)
    private val _willPayActive = mutableStateOf(false)
    init {
        _resolver.set(SplitDataIds.whole,_whole)
        _resolver.set(SplitDataIds.decimal,_decimal)
        _resolver.set(SplitDataIds.wholeGet,_wholeGet)
        _resolver.set(SplitDataIds.decimalGet,_decimalGet)
        _resolver.set(SplitDataIds.wholePay,_wholePay)
        _resolver.set(SplitDataIds.decimalPay,_decimalPay)
        _resolver.set(SplitDataIds.ultimateState,_ultimateState)
        _resolver.set(SplitDataIds.groups,_groups)
        _resolver.set(SplitDataIds.peoples,_peoples)
        _resolver.set(SplitDataIds.willGetActive,_willGetActive)
        _resolver.set(SplitDataIds.willPayActive,_willPayActive)
        _ultimateState.value = SplitPageState.GET
        _wholeGet.value = "4,500"
        _willGetActive.value = true
        _willPayActive.value = true
        viewModelScope.launch {
            while (true){
                delay(2000)
                _groups.add(GroupCardData(
                    id = System.currentTimeMillis(),
                    profileImage = "https://randomuser.me/api/portraits/men/68.jpg",
                    memberImages = listOf(
                        "https://randomuser.me/api/portraits/men/68.jpg",
                        "https://randomuser.me/api/portraits/men/68.jpg",
                        "https://randomuser.me/api/portraits/men/68.jpg",
                        "https://randomuser.me/api/portraits/men/68.jpg",
                        "https://randomuser.me/api/portraits/men/68.jpg",
                    ),
                    name = "Office",
                    willGet = 4500f,
                    willPay = 500f
                ))
                _peoples.add(
                    PeopleData(
                        uid = System.currentTimeMillis(),
                        name = "Debdutta Panda",
                        mobile = "8967114927",
                        imageUrl = "",
                        willGet = 1000f,
                        willPay = 500f
                    )
                )
            }
        }
    }
}