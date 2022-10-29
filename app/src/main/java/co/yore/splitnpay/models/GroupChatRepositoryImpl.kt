package co.yore.splitnpay.models

import co.yore.splitnpay.R
interface TransactionRepository {
    suspend fun getTransactions(): List<BillTransaction>

    suspend fun getTransactions(number: Int): List<BillTransaction>
}
class GroupChatRepositoryImpl : TransactionRepository {
    private val youGetPendingTransaction = BillTransaction(
        transactionType = TransactionType.Received,
        transactionStatus = TransactionStatus.Started,
        billTotal = 10000f,
        allPaidReceivedTotal = 3500f,
        paidReceived = 1000f,
        willPayReceive = 3000f,
        amountLeft = 6500f,
        transactionTime = "12:56 pm",
        totalTransactions = 5,
        completedTransactions = 5,
        isSingleChat = false,
        paymentMethod = "UPI",
        transactionDate = "9 May 2020",
        category =  Category(
            id = 0,
            name = "Trip",
            color = 0xFFFF4077,
            icon = R.drawable.ic_trip,
            isSelected = true,
            isEditable = false,
            subCategory = "Business Trip"
        ),
        from = Friend(
            name = "Sushil Roy",
            mobileNumber = "8989898989",
            imageUrl = "https://i.pravatar.cc/300",
            accountNumber = "12334456511",
            bank = Bank(
                name = "SBI",
                imageUrl = "https://i.pravatar.cc/300"
            ),
            accountType = AccountType.Savings,
            hasRead = false,
            isSelected = true
        ),
        to = Friend(
            name = "Manisha Roy",
            mobileNumber = "8989898989",
            imageUrl = "https://i.pravatar.cc/300",
            accountNumber = "34526378964",
            accountType = AccountType.Savings,
            bank = Bank(
                name = "Axis Bank",
                imageUrl = "https://i.pravatar.cc/300"
            ),
            hasRead = false,
            isSelected = true
        )
    )

    private val youPayPendingTransaction = BillTransaction(
        transactionType = TransactionType.Paid,
        transactionStatus = TransactionStatus.Started,
        billTotal = 10000f,
        allPaidReceivedTotal = 3500f,
        paidReceived = 1000f,
        willPayReceive = 3000f,
        amountLeft = 6500f,
        transactionTime = "12:56 pm",
        totalTransactions = 5,
        completedTransactions = 5,
        isSingleChat = false,
        paymentMethod = "UPI",
        transactionDate = "9 May 2020",
        category =  Category(
            id = 1,
            name = "Trip",
            color = 0xFFFF4077,
            icon = R.drawable.ic_trip,
            isSelected = true,
            isEditable = false,
            subCategory = "Business Trip"
        ),
        from = Friend(
            name = "Sushil Roy",
            mobileNumber = "8989898989",
            imageUrl = "https://i.pravatar.cc/300",
            accountNumber = "12334456511",
            bank = Bank(
                name = "SBI",
                imageUrl = "https://i.pravatar.cc/300"
            ),
            accountType = AccountType.Savings,
            hasRead = false,
            isSelected = true
        ),
        to = Friend(
            name = "Manisha Roy",
            mobileNumber = "8989898989",
            imageUrl = "https://i.pravatar.cc/300",
            accountNumber = "34526378964",
            accountType = AccountType.Savings,
            bank = Bank(
                name = "Axis Bank",
                imageUrl = "https://i.pravatar.cc/300"
            ),
            hasRead = false,
            isSelected = true
        )
    )

    private val youPaidSettledTransaction = BillTransaction(
        transactionType = TransactionType.Paid,
        transactionStatus = TransactionStatus.Settled,
        billTotal = 10000f,
        allPaidReceivedTotal = 3500f,
        paidReceived = 1000f,
        willPayReceive = 3000f,
        amountLeft = 6500f,
        transactionTime = "12:56 pm",
        totalTransactions = 5,
        completedTransactions = 5,
        isSingleChat = false,
        paymentMethod = "UPI",
        transactionDate = "9 May 2020",
        category =  Category(
            id = 2,
            name = "Trip",
            color = 0xFFFF4077,
            icon = R.drawable.ic_trip,
            isSelected = true,
            isEditable = false,
            subCategory = "Business Trip"
        ),
        from = Friend(
            name = "Sushil Roy",
            mobileNumber = "8989898989",
            imageUrl = "https://i.pravatar.cc/300",
            accountNumber = "12334456511",
            bank = Bank(
                name = "SBI",
                imageUrl = "https://i.pravatar.cc/300"
            ),
            accountType = AccountType.Savings,
            hasRead = false,
            isSelected = true
        ),
        to = Friend(
            name = "Manisha Roy",
            mobileNumber = "8989898989",
            imageUrl = "https://i.pravatar.cc/300",
            accountNumber = "34526378964",
            accountType = AccountType.Savings,
            bank = Bank(
                name = "Axis Bank",
                imageUrl = "https://i.pravatar.cc/300"
            ),
            hasRead = false,
            isSelected = true
        )
    )

    private val youReceivedSettledTransaction = BillTransaction(
        transactionType = TransactionType.Received,
        transactionStatus = TransactionStatus.Settled,
        billTotal = 10000f,
        allPaidReceivedTotal = 3500f,
        paidReceived = 1000f,
        willPayReceive = 3000f,
        amountLeft = 6500f,
        transactionTime = "12:56 pm",
        totalTransactions = 5,
        completedTransactions = 5,
        isSingleChat = false,
        paymentMethod = "UPI",
        transactionDate = "9 May 2020",
        category =  Category(
            id = 3,
            name = "Trip",
            color = 0xFFFF4077,
            icon = R.drawable.ic_trip,
            isSelected = true,
            isEditable = false,
            subCategory = "Business Trip"
        ),
        from = Friend(
            name = "Sushil Roy",
            mobileNumber = "8989898989",
            imageUrl = "https://i.pravatar.cc/300",
            accountNumber = "12334456511",
            bank = Bank(
                name = "SBI",
                imageUrl = "https://i.pravatar.cc/300"
            ),
            accountType = AccountType.Savings,
            hasRead = false,
            isSelected = true
        ),
        to = Friend(
            name = "Manisha Roy",
            mobileNumber = "8989898989",
            imageUrl = "https://i.pravatar.cc/300",
            accountNumber = "34526378964",
            accountType = AccountType.Savings,
            bank = Bank(
                name = "Axis Bank",
                imageUrl = "https://i.pravatar.cc/300"
            ),
            hasRead = false,
            isSelected = true
        )
    )

    private val allSettledTransaction = BillTransaction(
        transactionType = TransactionType.Unspecified, // does not matter in all settled state
        transactionStatus = TransactionStatus.AllSettled,
        billTotal = 10000f,
        allPaidReceivedTotal = 10000f,
        paidReceived = 1000f,
        willPayReceive = 3000f,
        amountLeft = 6500f,
        transactionTime = "12:56 pm",
        totalTransactions = 5,
        completedTransactions = 5,
        isSingleChat = false,
        paymentMethod = "UPI",
        transactionDate = "9 May 2020",
        category =  Category(
            id = 4,
            name = "Trip",
            color = 0xFFFF4077,
            icon = R.drawable.ic_trip,
            isSelected = true,
            isEditable = false,
            subCategory = "Business Trip"
        ),
        from = Friend(
            name = "Sushil Roy",
            mobileNumber = "8989898989",
            imageUrl = "https://i.pravatar.cc/300",
            accountNumber = "12334456511",
            bank = Bank(
                name = "SBI",
                imageUrl = "https://i.pravatar.cc/300"
            ),
            accountType = AccountType.Savings,
            hasRead = false,
            isSelected = true
        ),
        to = Friend(
            name = "Manisha Roy",
            mobileNumber = "8989898989",
            imageUrl = "https://i.pravatar.cc/300",
            accountNumber = "34526378964",
            accountType = AccountType.Savings,
            bank = Bank(
                name = "Axis Bank",
                imageUrl = "https://i.pravatar.cc/300"
            ),
            hasRead = false,
            isSelected = true
        )
    )

    private val filteredReceivedSettledTransaction = BillTransaction(
        transactionType = TransactionType.Received,
        transactionStatus = TransactionStatus.Filtered,
        billTotal = 10000f,
        allPaidReceivedTotal = 3500f,
        paidReceived = 1000f,
        willPayReceive = 3000f,
        amountLeft = 6500f,
        transactionTime = "12:56 pm",
        totalTransactions = 5,
        completedTransactions = 5,
        isSingleChat = true,
        paymentMethod = "UPI",
        transactionDate = "9 May 2020",
        category =  Category(
            id = 5,
            name = "Trip",
            color = 0xFFFF4077,
            icon = R.drawable.ic_trip,
            isSelected = true,
            isEditable = false,
            subCategory = "Business Trip"
        ),
        from = Friend(
            name = "Sushil Roy",
            mobileNumber = "8989898989",
            imageUrl = "https://i.pravatar.cc/300",
            accountNumber = "12334456511",
            bank = Bank(
                name = "SBI",
                imageUrl = "https://i.pravatar.cc/300"
            ),
            accountType = AccountType.Savings,
            hasRead = false,
            isSelected = true
        ),
        to = Friend(
            name = "Manisha Roy",
            mobileNumber = "8989898989",
            imageUrl = "https://i.pravatar.cc/300",
            accountNumber = "34526378964",
            accountType = AccountType.Savings,
            bank = Bank(
                name = "Axis Bank",
                imageUrl = "https://i.pravatar.cc/300"
            ),
            hasRead = false,
            isSelected = true
        )
    )

    private val filteredPaidSettledTransaction = BillTransaction(
        transactionType = TransactionType.Paid,
        transactionStatus = TransactionStatus.Filtered,
        billTotal = 10000f,
        allPaidReceivedTotal = 3500f,
        paidReceived = 1000f,
        willPayReceive = 3000f,
        amountLeft = 6500f,
        transactionTime = "12:56 pm",
        totalTransactions = 5,
        completedTransactions = 5,
        isSingleChat = true,
        paymentMethod = "UPI",
        transactionDate = "9 May 2020",
        category =  Category(
            id = 6,
            name = "Trip",
            color = 0xFFFF4077,
            icon = R.drawable.ic_trip,
            isSelected = true,
            isEditable = false,
            subCategory = "Business Trip"
        ),
        from = Friend(
            name = "Sushil Roy",
            mobileNumber = "8989898989",
            imageUrl = "https://i.pravatar.cc/300",
            accountNumber = "12334456511",
            bank = Bank(
                name = "SBI",
                imageUrl = "https://i.pravatar.cc/300"
            ),
            accountType = AccountType.Savings,
            hasRead = false,
            isSelected = true
        ),
        to = Friend(
            name = "Manisha Roy",
            mobileNumber = "8989898989",
            imageUrl = "https://i.pravatar.cc/300",
            accountNumber = "34526378964",
            accountType = AccountType.Savings,
            bank = Bank(
                name = "Axis Bank",
                imageUrl = "https://i.pravatar.cc/300"
            ),
            hasRead = false,
            isSelected = true
        )
    )

    private val transactions = listOf(
        youGetPendingTransaction,
        youPayPendingTransaction,
        youPaidSettledTransaction,
        youReceivedSettledTransaction,
        allSettledTransaction,
        filteredReceivedSettledTransaction,
        filteredPaidSettledTransaction
    )

    override suspend fun getTransactions(): List<BillTransaction> {
        return transactions
    }

    override suspend fun getTransactions(number: Int): List<BillTransaction> {
        return transactions.take(number)
    }
}