package co.yore.splitnpay.models

data class GroupCardData(
    val id: Long,
    val profileImage: String,
    val name: String,
    val memberImages: List<String>,
    val willPay: Float,
    val willGet: Float
)