package co.yore.splitnpay.models

data class Category(
    val name: String,
    val color: Long,
//    val icon: Painter,
    val icon: Any,
    val isSelected: Boolean = false,
    val isEditable: Boolean = false,
    val subCategory: String = "Business trip"
)