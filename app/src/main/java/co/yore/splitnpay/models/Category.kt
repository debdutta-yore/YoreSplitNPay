package co.yore.splitnpay.models

data class Category(
    val id: Any,
    val name: String,
    val color: Long,
//    val icon: Painter,
    val icon: Any?,
    val isSelected: Boolean = false,
    val isEditable: Boolean = false,
    val subCategory: String = "Business trip"
){
    companion object{
        val blank = Category(
            id = -1,
            name = "",
            color = 0,
            icon = null,
        )
    }
}