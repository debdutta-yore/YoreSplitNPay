package co.yore.splitnpay.models

/*
data class Friend(
    val uid: Int = 0,
    val name: String,
    val mobileNumber: String,
    val imageUrl: String = "https://i.pravatar.cc/300",
    val isSelected: Boolean = false,
    val hasRead: Boolean = false
)*/
enum class AccountType{
    Savings,
    Current
}
data class Friend(
    val uid: Int = 0,
    val name: String,
    val mobileNumber: String,
    val accountNumber: String,
    val accountType: AccountType,
    val imageUrl: String,
    val bank: Bank,
    val isSelected: Boolean,
    val hasRead: Boolean
)
