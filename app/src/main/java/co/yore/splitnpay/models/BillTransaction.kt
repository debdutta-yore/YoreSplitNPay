package co.yore.splitnpay.models

import co.yore.splitnpay.R

data class BillTransaction(
    val transactionType: TransactionType,
    val transactionStatus: TransactionStatus,
    val isSingleChat: Boolean,
    val billTotal: Float, // total transaction amount to settle
    val allPaidReceivedTotal: Float, // total paid/received until now
    val amountLeft: Float, // amount left to complete the bill total
    val willPayReceive: Float,  // you will received/pay in this transaction
    val paidReceived: Float, // paid received in this transaction
    val totalTransactions: Int,
    val completedTransactions: Int,
    val transactionTime: String,
    val transactionDate: String = "9th May'22",
    val bank: Bank = Bank(),
    val from: Friend = Friend(name = "Rudra", mobileNumber = "99999999999", imageUrl = "https://i.pravatar.cc/300"),
    val to: Friend = Friend(name = "Rudra", mobileNumber = "99999999999", imageUrl = "https://i.pravatar.cc/300"),
    val category: Category = Category(id = 0, name = "Travel", color = 0L, icon = R.drawable.travel)
)