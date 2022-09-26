package co.yore.splitnpay.models

data class PeopleData(
    val uid: Long,
    val name: String,
    val mobile: String,
    val imageUrl: String,
    val willGet: Float = 0f,
    val willPay: Float = 0f,
)