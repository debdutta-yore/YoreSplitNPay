package co.yore.splitnpay.friend_item.models

data class PeopleData(
    val uid: Int = 0,
    val name: String,
    val mobile: String,
    val imageUrl: String,
    val willGet: Float = 0f,
    val willPay: Float = 0f,
)