package co.yore.splitnpay.repo

import co.yore.splitnpay.R
import co.yore.splitnpay.app.AppContext
import co.yore.splitnpay.components.components.Kal
import co.yore.splitnpay.components.components.YoreDatePickerData
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.libs.contact.core.util.phoneList
import co.yore.splitnpay.models.*
import co.yore.splitnpay.object_box.Contact
import co.yore.splitnpay.object_box.box
import co.yore.splitnpay.pages.childpages.CategoryExpense
import co.yore.splitnpay.pages.childpages.PieData
import co.yore.splitnpay.ui.theme.CuriousBlue
import co.yore.splitnpay.ui.theme.Denim
import co.yore.splitnpay.ui.theme.FunGreen
import co.yore.splitnpay.ui.theme.RadicalRed
import io.github.serpro69.kfaker.Faker
import kotlinx.coroutines.delay
import java.util.*
import kotlin.random.Random

interface MasterRepo{
    suspend fun groupAndContacts(): List<GroupOrContact>
    suspend fun groups(contacts: List<ContactData>): List<GroupData>
    suspend fun peoples(count: Int = 20): List<ContactData>
    suspend fun saveContacts(contacts: List<String>)
    suspend fun purgeContacts()
    suspend fun contacts(): List<String>
    fun deviceContacts(contacts: List<String>): List<ContactData>

    suspend fun getPaidByMembers(): List<MemberPayment>

    suspend fun getBillTransactions(): List<BillTransaction>
    suspend fun getCategories(): List<Category>
    suspend fun getAllCategories(): List<Category>
    suspend fun getBillTransactions(number: Int): List<BillTransaction>
    suspend fun members(): List<SingleItem>

    suspend fun getWillGet(): List<Transaction>
    suspend fun getWillPay(): List<Transaction>

    suspend fun getTransactions(): List<BillTransaction>
    suspend fun getTransactions(number: Int): List<BillTransaction>

    suspend fun getMembers(): List<SplitSelectableMember>
    suspend fun getWillPayTransactions(): List<MemberTransact>
    suspend fun getWillGetTransactions(): List<MemberTransact>

    suspend fun getMembers1(): List<Member>

    // //////
    suspend fun getCategoryExpense(): List<CategoryExpense>
    suspend fun getBarChartData(): List<ExpenseChartData>
    suspend fun getPieChartData(): List<PieData>
    suspend fun getDatePickerData(): YoreDatePickerData

    suspend fun getItems(): List<SheetItem>
    fun sampleSplitCardDetails(): SplitCardDetailsPageData
    fun splitSelectableMembers(): Collection<SplitSelectableMember>
    fun splitCardGetMembers(): Collection<MemberWillGetOrPayDetailsSingleRowItem>
    fun splitCardPayMembers(): Collection<MemberWillGetOrPayDetailsSingleRowItem>
    fun splitCardPaidList(): Collection<MemberTransact>
    fun splitCardSplitAmong(): Collection<MemberTransact>
    fun getUpis(): List<Upi>
    fun transactionData(): TransactionReview
    suspend fun conversations(): Collection<Conversation>
    fun groupDatas(): Collection<GroupData>
    fun willGetTransact(): MemberTransact
    fun willPayTransact(): MemberTransact
    fun singleSettledOrUnsettledMembers(): List<SingleSettledOrUnsettledMember>
    fun settledMembers(): List<SingleSettledOrUnsettledMember>
    fun unsettledMembers(): List<SingleSettledOrUnsettledMember>
    suspend fun splitPageData(): SplitPageData
    suspend fun groupChatPageData(): GroupChatPageData
    fun getSplits(): List<SplitBrief>
}

class MasterRepoImpl : MasterRepo {
    override suspend fun groupAndContacts(): List<GroupOrContact> {
        // delay(6000)
        val contacts = peoples()
        val groups = groups(contacts)
        val list = mutableListOf<GroupOrContact>()
        list.addAll(contacts)
        list.addAll(groups)
        list.shuffle()
        return list
    }
    override suspend fun groups(contacts: List<ContactData>): List<GroupData> {
        val f = Faker()
        val r = Random(System.nanoTime())
        var i = 0
        return MutableList(50){
            val members = contacts.takeSome(2, 5).toList()
            val willGet = Rand.nextFloat(0f, 10000f, reseed = true, biased = 0f)
            val willPay = Rand.nextFloat(0f, 10000f, reseed = true, biased = 0f)
            val name = f.animal.name()
            val now = System.currentTimeMillis()
            val dif: Long = 365 * 24 * 3600 * 1000L
            val then = now - dif
            GroupData(
                id = newId,
                name = name,
                image = "https://randomuser.me/api/portraits/lego/${(++i) % 10}.jpg",
                members = members,
                lastActivity = randomDate(then, now),
                willGet = willGet,
                willPay = willPay
            )
        }
    }
    override suspend fun peoples(count: Int): List<ContactData> {
        return co.yore.splitnpay.libs.contact.core.Contacts(AppContext.app).query().find().map{
            ContactData(
                id = it.id,
                name = it.displayNamePrimary ?: "No name", // f.name.name(),
                mobile = it.phoneList().firstOrNull()?.number ?: "No phone",
                image = it.photoUri,
                lastActivity = randomDate(1643049000000L, 1664099455386L),
                willGet = Rand.nextFloat(0f, 10000f, reseed = true, biased = 0f),
                willPay = Rand.nextFloat(0f, 1000f, reseed = true, biased = 0f)
            )
        }
        /*val f = Faker()
        val r = Random(System.nanoTime())
        var i = 0
        return MutableList(count){
            ContactData(
                id = newId,
                name = "fdfdfdfdfd fdfdf12345678901234", // f.name.name(),
                mobile = f.phoneNumber.cellPhone(),
                image = "https://randomuser.me/api/portraits/men/${(++i) - 1}.jpg",
                lastActivity = randomDate(1643049000000L, 1664099455386L),
                willGet = Rand.nextFloat(0f, 10000f, reseed = true, biased = 0f),
                willPay = Rand.nextFloat(0f, 1000f, reseed = true, biased = 0f)
            )
        }*/
    }
    override suspend fun saveContacts(contacts: List<String>) {
        Contact::class.java.box
            .put(
                contacts.map {
                    Contact(
                        mobile = it
                    )
                }
            )
    }
    override suspend fun purgeContacts() {
        Contact::class.java
            .box
            .removeAll()
    }
    override suspend fun contacts(): List<String> {
        return Contact::class.java
            .box
            .all
            .map {
                it.mobile
            }
    }
    override fun deviceContacts(contacts: List<String>): List<ContactData> {
        var i = 0
        return contacts.map {
            ContactData(
                id = ++i,
                mobile = it,
                image = "https://randomuser.me/api/portraits/men/$i.jpg",
                name = "Name$i"
            )
        }
    }
    private val newId get() = UUID.randomUUID().toString()

    override suspend fun getPaidByMembers(): List<MemberPayment> {
        return listOf(
            MemberPayment(
                0,
                "You",
                "8967114927",
                image = "https://i.pravatar.cc/100"
            ),
            MemberPayment(
                1,
                "Manisha Roy",
                "9456321025",
                image = "https://i.pravatar.cc/100"
            ),
            MemberPayment(
                2,
                "Sushil Roy",
                "9746310862",
                image = "https://i.pravatar.cc/100"
            ),
            MemberPayment(
                3,
                "Sanjana Roy",
                "8319764035",
                image = "https://i.pravatar.cc/100"
            ),
            MemberPayment(
                4,
                "Ankita Roy",
                "7614563289",
                image = "https://i.pravatar.cc/100"
            )
        )
    }

    val groupsList = emptyList<GroupShort>()
    val group = listOf<GroupShort>(
        GroupShort("Office Buddies", "https://i.pravatar.cc/300", amount = 3000f),
        GroupShort("Office Buddies", "https://i.pravatar.cc/300", amount = 3000f)
    )

    override suspend fun getBillTransactions(): List<BillTransaction> {
        return listOf(MembersMock.transaction)
    }

    override suspend fun getBillTransactions(number: Int): List<BillTransaction> {
        return listOf(MembersMock.transaction)
    }

    // ////////////
    var categoryList =
        listOf(
            Category(
                id = 0,
                name = "Food",
                0xFF1A79E5,
                R.drawable.ic_food
            ),
            Category(
                id = 1,
                name = "Trip",
                0xFFFF4077,
                R.drawable.ic_trip
            ),
            Category(
                id = 2,
                name = "Rent",
                0xFFF6CC00,
                R.drawable.ic_rent
            ),
            Category(
                id = 3,
                name = "Party",
                0xFF37D8CF,
                R.drawable.ic_party
            ),
            Category(
                id = 4,
                name = "Medical",
                0xFF37D8CF,
                R.drawable.ic_medical
            ),
            Category(
                id = 5,
                name = "Emi",
                0xFFF6CC00,
                R.drawable.ic_emi
            ),
            Category(
                id = 6,
                name = "Bills",
                0xFFFF4077,
                R.drawable.ic_bills
            )
        )

    override suspend fun getCategories(): List<Category> {
        return categoryList.take(4)
    }

    override suspend fun getAllCategories(): List<Category> {
        return categoryList
    }

    override suspend fun members(): List<SingleItem>{
        return listOf(
            SingleItem(
                id = 1,
                profilePic = "https://i.pravatar.cc/300",
                userName = "You",
                mobileNo = "9563376942",
                isSelected = false
            ),
            SingleItem(
                id = 2,
                profilePic = "https://i.pravatar.cc/300",
                userName = "Manisha Roy",
                mobileNo = "9563376942",
                isSelected = false
            ),
            SingleItem(
                id = 3,
                profilePic = "https://i.pravatar.cc/300",
                userName = "Sushil Roy",
                mobileNo = "9563376942",
                isSelected = false
            ),
            SingleItem(
                id = 4,
                profilePic = "https://i.pravatar.cc/300",
                userName = "Sanjana Ray",
                mobileNo = "9563376942",
                isSelected = false
            ),
            SingleItem(
                id = 5,
                profilePic = "https://i.pravatar.cc/300",
                userName = "Ankita Ray",
                mobileNo = "9563376942",
                isSelected = false
            )
        )
    }

    private val getList = listOf(
        Transaction(
            name = "You",
            imageUrl = "https://i.pravatar.cc/300",
            mobileNumber = "9563376942",
            amount = 3000f,
            transactionType = TransactionType.Paid
        ),
        Transaction(
            name = "Sushil Roy",
            imageUrl = "https://i.pravatar.cc/300",
            mobileNumber = "9563376942",
            amount = 1000f,
            transactionType = TransactionType.Paid
        ),
        Transaction(
            name = "Manisha Roy",
            imageUrl = "https://i.pravatar.cc/300",
            mobileNumber = "9563376942",
            amount = 500f,
            transactionType = TransactionType.Unspecified
        ),
        Transaction(
            name = "Sanjanaa Ray",
            imageUrl = "https://i.pravatar.cc/300",
            mobileNumber = "9563376942",
            amount = 0f,
            transactionType = TransactionType.Unspecified
        )
    )

    private val payList = listOf(
        Transaction(
            name = "You",
            imageUrl = "https://i.pravatar.cc/300",
            mobileNumber = "9563376942",
            amount = 3000f,
            transactionType = TransactionType.Paid
        ),
        Transaction(
            name = "Sushil Roy",
            imageUrl = "https://i.pravatar.cc/300",
            mobileNumber = "9563376942",
            amount = 100000f,
            transactionType = TransactionType.Paid
        )
    )

    override suspend fun getWillGet(): List<Transaction> {
        return getList
    }

    override suspend fun getWillPay(): List<Transaction> {
        return payList
    }

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
        category = Category(
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
        category = Category(
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
        category = Category(
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
        category = Category(
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
        category = Category(
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
        category = Category(
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
        category = Category(
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

    val members = listOf(
        SplitSelectableMember(name = "You", "https://i.pravatar.cc/300?", true),
        SplitSelectableMember(name = "Sushil", "https://i.pravatar.cc/300?", false)
    )

    private val getList1 = listOf(
        MemberTransact(
            name = "You",
            image = "https://i.pravatar.cc/300?",
            mobile = "9563376942",
            amount = 30000f
        ),
        MemberTransact(
            name = "Sushil Roy",
            image = "https://i.pravatar.cc/300?",
            mobile = "9563376942",
            amount = 1000f
        ),
        MemberTransact(
            name = "Manisha Roy",
            image = "https://i.pravatar.cc/300?",
            mobile = "9563376942",
            amount = 500f
        ),
        MemberTransact(
            name = "Sanjanaa Ray",
            image = "https://i.pravatar.cc/300?",
            mobile = "9563376942",
            amount = 0f
        )
    )
    private val paidList = emptyList<MemberTransact>()

    override suspend fun getMembers(): List<SplitSelectableMember> {
        return members
    }

    override suspend fun getWillGetTransactions(): List<MemberTransact> {
        return getList1
    }

    override suspend fun getWillPayTransactions(): List<MemberTransact> {
        return paidList
    }

    val group1 = listOf(
        Member(
            id = 1,
            profilePic = "https://i.pravatar.cc/300",
            userName = "You",
            mobileNo = "9563376942",
            isSelected = false,
            isGroupAdmin = true
        ),
        Member(
            id = 2,
            profilePic = "https://i.pravatar.cc/300",
            userName = "Manisha Roy",
            mobileNo = "9563376942",
            isSelected = false
        ),
        Member(
            id = 3,
            profilePic = "https://i.pravatar.cc/300",
            userName = "Sushil Roy",
            mobileNo = "9563376942",
            isSelected = false
        ),
        Member(
            id = 4,
            profilePic = "https://i.pravatar.cc/300",
            userName = "Sanjana Ray",
            mobileNo = "9563376942",
            isSelected = false
        ),
        Member(
            id = 5,
            profilePic = "https://i.pravatar.cc/300",
            userName = "Ankita Ray",
            mobileNo = "9563376942",
            isSelected = false
        )
    )

    override suspend fun getMembers1(): List<Member> {
        return group1
    }

    // ////////////
    private val categories = listOf(
        CategoryExpense(
            icon = R.drawable.ic_trip,
            category = "Trip",
            description = "Business Trip",
            count = 1,
            amount = 500.00f,
            tint = RadicalRed
        ),
        CategoryExpense(
            icon = R.drawable.ic_food,
            category = "Food",
            description = "BFFs",
            count = 5,
            amount = 2500.00f,
            tint = CuriousBlue
        )
    )

    private val barChartData = listOf(
        ExpenseChartData(
            xAxis = "Jan",
            yAxis = 12000f,
            year = 2022
        ),
        ExpenseChartData(
            xAxis = "Feb",
            yAxis = 25000f,
            year = 2023
        ),
        ExpenseChartData(
            xAxis = "Mar",
            yAxis = 4500f,
            year = 2024
        ),
        ExpenseChartData(
            xAxis = "Apr",
            yAxis = 0f,
            year = 2026
        ),
        ExpenseChartData(
            xAxis = "May",
            yAxis = 1000f,
            year = 2026
        ),
        ExpenseChartData(
            xAxis = "Jun",
            yAxis = 25000f,
            year = 2026
        ),
        ExpenseChartData(
            xAxis = "Jul",
            yAxis = 5000f,
            year = 2027
        ),
        ExpenseChartData(
            xAxis = "Aug",
            yAxis = 50000f,
            year = 2026
        ),
        ExpenseChartData(
            xAxis = "Sep",
            yAxis = 35000f,
            year = 2026
        ),
        ExpenseChartData(
            xAxis = "Oct",
            yAxis = 0f,
            year = 2026
        ),
        ExpenseChartData(
            xAxis = "Nov",
            yAxis = 2000f,
            year = 2026
        ),
        ExpenseChartData(
            xAxis = "Dec",
            yAxis = 20000f,
            year = 2026
        )
    )

    private val pieChartData = listOf(
        PieData(
            RadicalRed,
            0.5f
        ),
        PieData(
            CuriousBlue,
            0.5f
        )
    )

    private val datePickerData = YoreDatePickerData(
        selectedDay = 1,
        selectedMonth = 1,
        selectedYear = 2022,
        minDate = Kal.Date.create(1, 1, 2022),
        maxDate = Kal.Date.create(23, 8, 2036),
        dateSelectable = true,
        yearSwitchable = true
    )

    override suspend fun getCategoryExpense(): List<CategoryExpense> {
        return categories
    }

    override suspend fun getBarChartData(): List<ExpenseChartData> {
        return barChartData
    }

    override suspend fun getPieChartData(): List<PieData> {
        return pieChartData
    }

    override suspend fun getDatePickerData(): YoreDatePickerData {
        return datePickerData
    }

    // /////

    private val itemList = listOf(
        SheetItem(id = 0, R.drawable.calendar_1, "All Time"),
        SheetItem(id = 1, R.drawable.last_month, "Last Month"),
        SheetItem(id = 2, R.drawable.this_month, "This Month"),
        SheetItem(id = 3, R.drawable.custom_month, "Custom Month")
    )

    override suspend fun getItems(): List<SheetItem> {
        return itemList
    }

    override fun sampleSplitCardDetails(): SplitCardDetailsPageData {
        return SplitCardDetailsPageData(
            splitAmount = 12000f,
            splitBalance = 5000f,
            splitStatusMessage = "Partially Paid / Received",
            splitProgress = 0.3f,
            splitPaidMark = "5 of 5 paid",
            splitTransacted = 3000f,
            splitCardDetailsData = SplitCardDetailsData(
                status = "Partially Paid",
                splitMethod = "Unequal",
                categories = "Trip",
                createdBy = "You",
                createdOn = "9th May, 2022",
                noOfMembers = 5,
                categoryIcon = R.drawable.ic_trip
            )
        )
    }

    override fun splitSelectableMembers(): Collection<SplitSelectableMember> {
        return listOf(
            SplitSelectableMember(
                name = "You",
                image = "https://i.pravatar.cc/300",
                isSelected = true
            ),
            SplitSelectableMember(
                name = "Sushil",
                image = "https://i.pravatar.cc/300",
                isSelected = false
            ),
            SplitSelectableMember(
                name = "Manisha",
                image = "https://i.pravatar.cc/300",
                isSelected = false
            ),
            SplitSelectableMember(
                name = "Tanisha",
                image = "https://i.pravatar.cc/300",
                isSelected = false
            ),
            SplitSelectableMember(
                name = "Sanjana",
                image = "https://i.pravatar.cc/300",
                isSelected = false
            ),
            SplitSelectableMember(
                name = "Arvind",
                image = "https://i.pravatar.cc/300",
                isSelected = false
            )
        )
    }

    override fun splitCardGetMembers(): Collection<MemberWillGetOrPayDetailsSingleRowItem> {
        return listOf(
            MemberWillGetOrPayDetailsSingleRowItem(
                userName = "Sushil Roy",
                imageUrl = "https://i.pravatar.cc/300",
                userPhNo = "7610036985",
                getAmount = 600f,
                paidAmount = 0f
            ),
            MemberWillGetOrPayDetailsSingleRowItem(
                userName = "Sanjana Roy",
                imageUrl = "https://i.pravatar.cc/300",
                userPhNo = "8953246985",
                getAmount = 600f,
                paidAmount = 0f
            )
        )
    }

    override fun splitCardPayMembers(): Collection<MemberWillGetOrPayDetailsSingleRowItem> {
        return listOf(
            MemberWillGetOrPayDetailsSingleRowItem(
                userName = "Sushil Roy",
                imageUrl = "https://i.pravatar.cc/300",
                userPhNo = "7610036985",
                getAmount = 600f,
                paidAmount = 0f
            ),
            MemberWillGetOrPayDetailsSingleRowItem(
                userName = "Sanjana Roy",
                imageUrl = "https://i.pravatar.cc/300",
                userPhNo = "8953246985",
                getAmount = 600f,
                paidAmount = 0f
            )
        )
    }

    override fun splitCardPaidList(): Collection<MemberTransact> {
        return listOf(
            MemberTransact(
                name = "You",
                mobile = "7923012593",
                image = "https://i.pravatar.cc/300",
                amount = 5000f
            ),
            MemberTransact(
                name = "Manisha Roy",
                mobile = "9610356210",
                image = "https://i.pravatar.cc/300",
                amount = 5000f
            )
        )
    }

    override fun splitCardSplitAmong(): Collection<MemberTransact> {
        return listOf(
            MemberTransact(
                name = "You",
                mobile = "7923012593",
                image = "https://i.pravatar.cc/300",
                amount = 5000f
            ),
            MemberTransact(
                name = "Sushil Roy",
                mobile = "9610356210",
                image = "https://i.pravatar.cc/300",
                amount = 5000f
            ),
            MemberTransact(
                name = "Sanjana Roy",
                mobile = "9610356210",
                image = "https://i.pravatar.cc/300",
                amount = 5000f
            ),
            MemberTransact(
                name = "Ankita Roy",
                mobile = "9610356210",
                image = "https://i.pravatar.cc/300",
                amount = 5000f
            )
        )
    }

    override fun getUpis(): List<Upi> {
        return listOf(
            Upi("UPI-1", "fdfldf@ljl", "SBI", "User1", FunGreen, false),
            Upi("UPI-2", "fdfldf@ljl1", "Axis", "User2", Denim, false),
            Upi("UPI-2", "fdfldf@ljl1", "Axis", "User2", Denim, false),
            Upi("UPI-2", "fdfldf@ljl1", "Axis", "User2", Denim, false),
            Upi("UPI-2", "fdfldf@ljl1", "Axis", "User2", Denim, false),
            Upi("UPI-2", "fdfldf@ljl1", "Axis", "User2", Denim, false),
            Upi("UPI-2", "fdfldf@ljl1", "Axis", "User2", Denim, false),
            Upi("UPI-2", "fdfldf@ljl1", "Axis", "User2", Denim, false),
            Upi("UPI-2", "fdfldf@ljl1", "Axis", "User2", Denim, false),
            Upi("UPI-2", "fdfldf@ljl1", "Axis", "User2", Denim, false)
        )
    }

    override fun transactionData(): TransactionReview {
        return TransactionReview(
            transactionType = TransactionType.Paid,
            paymentMethod = "UPI",
            amount = 10000f,
            from = Friend(
                name = "Rudra Dev",
                mobileNumber = "7896230125",
                accountNumber = "AC-123",
                accountType = AccountType.Current,
                imageUrl = "https://i.pravatar.cc/300",
                bank = Bank(
                    name = "SBI",
                    imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/c/cc/SBI-logo.svg/500px-SBI-logo.svg.png?20200329171950"
                ),
                isSelected = true,
                hasRead = false
            ),
            to = Friend(
                name = "Deb Pan",
                mobileNumber = "8954102365",
                accountNumber = "AC-124",
                accountType = AccountType.Current,
                imageUrl = "https://i.pravatar.cc/300",
                bank = Bank(
                    name = "Axis",
                    imageUrl = "https://is3-ssl.mzstatic.com/image/thumb/Purple112/v4/8f/77/e9/8f77e9ee-9cc9-a308-eeda-7b4cbbcdeda6/AppIcon-0-0-1x_U007emarketing-0-0-0-8-0-0-sRGB-0-0-0-GLES2_U002c0-512MB-85-220-0-0.png/146x0w.webp"
                ),
                isSelected = true,
                hasRead = false
            ),
            category = Category(
                id = 0,
                name = "Category",
                color = 0xffff0000,
                icon = R.drawable.travel
            )
        )
    }

    override suspend fun conversations(): Collection<Conversation> {
        val billTransactions = getTransactions()
        val list = mutableListOf<Conversation>(
            Conversation(
                type = Conversation.Type.MEMBER,
                data = MemberData(
                    name = "Manisha Roy",
                    profileImage = "https://i.pravatar.cc/100"
                )
            ),
            Conversation(
                type = Conversation.Type.DATE,
                data = "May 9th 2022"
            ),
            Conversation(
                type = Conversation.Type.CREATION,
                data = GroupCreationEvent(
                    creator = "You",
                    groupName = "Office buddies"
                )
            )
        )
        list.addAll(
            billTransactions.map {
                Conversation(
                    type = Conversation.Type.TRANSACTION,
                    data = it
                )
            }
        )
        list.add(
            Conversation(
                type = Conversation.Type.STATUS,
                data = ChatStatus(
                    listOf(
                        "https://i.pravatar.cc/100",
                        "https://i.pravatar.cc/100",
                        "https://i.pravatar.cc/100",
                        "https://i.pravatar.cc/100"
                    ),
                    left = false
                )
            )
        )
        list.add(
            Conversation(
                type = Conversation.Type.CHAT,
                data = ChatData(
                    content = "Hello, guy"
                )
            )
        )
        list.add(
            Conversation(
                type = Conversation.Type.CHAT,
                data = ChatData(
                    content = "Hello, guys",
                    profileImage = "https://i.pravatar.cc/100"
                )
            )
        )
        return list
    }

    override fun groupDatas(): Collection<GroupData> {
        return listOf(
            GroupData(
                id = 0,
                image = "https://i.pravatar.cc/300",
                name = "Tiger",
                members = listOf(
                    ContactData(
                        id = 0,
                        image = "https://i.pravatar.cc/300",
                        name = "Sanjana Roy",
                        mobile = "7964210356"
                    ),
                    ContactData(
                        id = 0,
                        image = "https://i.pravatar.cc/300",
                        name = "Sanjana Roy",
                        mobile = "7964210356"
                    ),
                    ContactData(
                        id = 0,
                        image = "https://i.pravatar.cc/300",
                        name = "Sanjana Roy",
                        mobile = "7964210356"
                    ),
                    ContactData(
                        id = 0,
                        image = "https://i.pravatar.cc/300",
                        name = "Sanjana Roy",
                        mobile = "7964210356"
                    )
                ),
                willGet = 1000f,
                willPay = 200f
            )
        )
    }

    override fun willGetTransact(): MemberTransact {
        return MemberTransact(
            name = "Sushil",
            image = "https://i.pravatar.cc/300",
            amount = 1000f,
            mobile = "7610236589"
        )
    }

    override fun willPayTransact(): MemberTransact {
        return MemberTransact(
            name = "Sushil",
            image = "https://i.pravatar.cc/300",
            amount = 1000f,
            mobile = "7610236589"
        )
    }

    override fun singleSettledOrUnsettledMembers(): List<SingleSettledOrUnsettledMember> {
        return listOf(
            SingleSettledOrUnsettledMember(
                selectedSettleOption = SettleOptions.SplitIndividual,
                isChecked = true,
                isSettledMember = false,
                imageUrl = "https://i.pravatar.cc/300",
                userName = "Sushil Roy",
                userPhNo = "8967114927",
                getAmount = 600f,
                paidAmount = 0f
            ),
            SingleSettledOrUnsettledMember(
                selectedSettleOption = SettleOptions.SplitIndividual,
                isChecked = true,
                isSettledMember = false,
                imageUrl = "https://i.pravatar.cc/300",
                userName = "Sushil Roy",
                userPhNo = "8967114927",
                getAmount = 600f,
                paidAmount = 0f
            ),
            SingleSettledOrUnsettledMember(
                selectedSettleOption = SettleOptions.SplitIndividual,
                isChecked = true,
                isSettledMember = false,
                imageUrl = "https://i.pravatar.cc/300",
                userName = "Sushil Roy",
                userPhNo = "8967114927",
                getAmount = 600f,
                paidAmount = 0f
            ),
            SingleSettledOrUnsettledMember(
                selectedSettleOption = SettleOptions.SplitIndividual,
                isChecked = true,
                isSettledMember = false,
                imageUrl = "https://i.pravatar.cc/300",
                userName = "Sushil Roy",
                userPhNo = "8967114927",
                getAmount = 600f,
                paidAmount = 0f
            ),
            SingleSettledOrUnsettledMember(
                selectedSettleOption = SettleOptions.SplitIndividual,
                isChecked = true,
                isSettledMember = false,
                imageUrl = "https://i.pravatar.cc/300",
                userName = "Sushil Roy",
                userPhNo = "8967114927",
                getAmount = 600f,
                paidAmount = 0f
            ),
            SingleSettledOrUnsettledMember(
                selectedSettleOption = SettleOptions.SplitIndividual,
                isChecked = true,
                isSettledMember = false,
                imageUrl = "https://i.pravatar.cc/300",
                userName = "Sushil Roy",
                userPhNo = "8967114927",
                getAmount = 600f,
                paidAmount = 0f
            ),
            SingleSettledOrUnsettledMember(
                selectedSettleOption = SettleOptions.SplitIndividual,
                isChecked = true,
                isSettledMember = false,
                imageUrl = "https://i.pravatar.cc/300",
                userName = "Sushil Roy",
                userPhNo = "8967114927",
                getAmount = 600f,
                paidAmount = 0f
            )
        )
    }

    override fun settledMembers(): List<SingleSettledOrUnsettledMember> {
        return listOf(
            SingleSettledOrUnsettledMember(
                selectedSettleOption = SettleOptions.SplitIndividual,
                isChecked = true,
                isSettledMember = true,
                imageUrl = "https://i.pravatar.cc/300",
                userName = "Sushil Roy",
                userPhNo = "8967114927",
                getAmount = 600f,
                paidAmount = 0f
            )
        )
    }

    override fun unsettledMembers(): List<SingleSettledOrUnsettledMember> {
        return listOf(
            SingleSettledOrUnsettledMember(
                selectedSettleOption = SettleOptions.SplitIndividual,
                isChecked = true,
                isSettledMember = false,
                imageUrl = "https://i.pravatar.cc/300",
                userName = "Sushil Roy",
                userPhNo = "8967114927",
                getAmount = 0f,
                paidAmount = 600f
            )
        )
    }

    override suspend fun splitPageData(): SplitPageData {
        delay(6000)
        return SplitPageData(
            willGet = 3000f,
            willPay = 2000f,
            splitted = true
        )
    }

    override suspend fun groupChatPageData(): GroupChatPageData {
        delay(2000)
        return GroupChatPageData(
            name = "Office Buddies",
            amount = 6000f,
            image = "https://i.pravatar.cc/300"
        )
    }

    override fun getSplits(): List<SplitBrief> {
        val list = mutableListOf<SplitBrief>()
        val count = Rand.nextFloat(2f, 20f).toInt()
        val currentYear = currentYear
        val currentMonth = currentMonth
        val currentDay = currentDay
        val categoryCount = Category.list.size
        for (i in 0 until count) {
            val category = Category.list[Rand.nextFloat(0, categoryCount - 1).toInt()]
            val year = Rand.nextFloat(currentYear - 2, currentYear).toInt()
            val month = Rand.nextFloat(1, 12).toInt()
                .coerceIn(1, if (year == currentYear) currentMonth else 12)
            val day = Rand.nextFloat(1, 28).toInt().coerceIn(1, if (year == currentYear && month == currentMonth) currentDay else 28)
            list.add(
                SplitBrief(
                    id = i,
                    amount = Rand.nextFloat(500f, 30000f),
                    date = SplitBrief.Date(
                        year,
                        month,
                        day
                    ),
                    category = category,
                    description = "My ${category.name}"
                )
            )
        }
        return list
    }
}
object MembersMock {
    val transaction = BillTransaction(
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
        category = Category(
            id = 6,
            name = "Trip",
            color = 0xFFFF4077,
            icon = R.drawable.ic_trip,
            isSelected = true,
            isEditable = false,
            subCategory = "Business Trip"
        ),
        from = co.yore.splitnpay.models.Friend(
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
        to = co.yore.splitnpay.models.Friend(
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
        ),
        paymentMethod = "UPI",
        transactionDate = "9 May 2020"
    )
}
// ////////////
/*class RepoImpl : Repo {
    override suspend fun groupAndContacts(): List<GroupOrContact> {
        val contacts = peoples()
        val groups = groups(contacts)
        val list = mutableListOf<GroupOrContact>()
        list.addAll(contacts)
        list.addAll(groups)
        list.shuffle()
        return list
    }
    override suspend fun groups(contacts: List<ContactData>): List<GroupData> {
        val f = Faker()
        val r = Random(System.nanoTime())
        var i = 0
        return MutableList(50){
            val members = contacts.takeSome(2, 5).toList()
            val willGet = Rand.nextFloat(0f, 10000f, reseed = true, biased = 0f)
            val willPay = Rand.nextFloat(0f, 10000f, reseed = true, biased = 0f)
            val name = f.animal.name()
            val now = System.currentTimeMillis()
            val dif: Long = 365 * 24 * 3600 * 1000L
            val then = now - dif
            GroupData(
                id = newId,
                name = name,
                image = "https://randomuser.me/api/portraits/lego/${(++i) % 10}.jpg",
                members = members,
                lastActivity = randomDate(then, now),
                willGet = willGet,
                willPay = willPay
            )
        }
    }
    override suspend fun peoples(count: Int): List<ContactData> {
        val f = Faker()
        val r = Random(System.nanoTime())
        var i = 0
        return MutableList(count){
            ContactData(
                id = newId,
                name = "fdfdfdfdfd fdfdf12345678901234", // f.name.name(),
                mobile = f.phoneNumber.cellPhone(),
                image = "https://randomuser.me/api/portraits/men/${(++i) - 1}.jpg",
                lastActivity = randomDate(1643049000000L, 1664099455386L),
                willGet = Rand.nextFloat(0f, 10000f, reseed = true, biased = 0f),
                willPay = Rand.nextFloat(0f, 1000f, reseed = true, biased = 0f)
            )
        }
    }
    override suspend fun saveContacts(contacts: List<String>) {
        Contact::class.java.box
            .put(
                contacts.map {
                    Contact(
                        mobile = it
                    )
                }
            )
    }
    override suspend fun purgeContacts() {
        Contact::class.java
            .box
            .removeAll()
    }
    override suspend fun contacts(): List<String> {
        return Contact::class.java
            .box
            .all
            .map {
                it.mobile
            }
    }
    override fun deviceContacts(contacts: List<String>): List<ContactData> {
        var i = 0
        return contacts.map {
            ContactData(
                id = ++i,
                mobile = it,
                image = "https://randomuser.me/api/portraits/men/$i.jpg",
                name = "Name$i"
            )
        }
    }
    private val newId get() = UUID.randomUUID().toString()
}
class SplitReviewMock : SplitReviewRepository {
    override suspend fun getPaidByMembers(): List<MemberPayment> {
        return listOf(
            MemberPayment(
                0,
                "You",
                "8967114927",
                image = "https://i.pravatar.cc/100"
            ),
            MemberPayment(
                1,
                "Manisha Roy",
                "9456321025",
                image = "https://i.pravatar.cc/100"
            ),
            MemberPayment(
                2,
                "Sushil Roy",
                "9746310862",
                image = "https://i.pravatar.cc/100"
            ),
            MemberPayment(
                3,
                "Sanjana Roy",
                "8319764035",
                image = "https://i.pravatar.cc/100"
            ),
            MemberPayment(
                4,
                "Ankita Roy",
                "7614563289",
                image = "https://i.pravatar.cc/100"
            )
        )
    }

}
class GroupsMock : GroupRepository {
    val groupsList = emptyList<GroupShort>()
    val group = listOf<GroupShort>(
        GroupShort("Office Buddies", "https://i.pravatar.cc/300", amount = 3000f),
        GroupShort("Office Buddies", "https://i.pravatar.cc/300", amount = 3000f)
    )

    override suspend fun getBillTransactions(): List<BillTransaction> {
        return listOf(MembersMock.transaction)
    }

    override suspend fun getBillTransactions(number: Int): List<BillTransaction> {
        return listOf(MembersMock.transaction)
    }

    // ////////////
    var categoryList =
        listOf(
            Category(
                id = 0,
                name = "Food",
                0xFF1A79E5,
                R.drawable.ic_food
            ),
            Category(
                id = 1,
                name = "Trip",
                0xFFFF4077,
                R.drawable.ic_trip
            ),
            Category(
                id = 2,
                name = "Rent",
                0xFFF6CC00,
                R.drawable.ic_rent
            ),
            Category(
                id = 3,
                name = "Party",
                0xFF37D8CF,
                R.drawable.ic_party
            ),
            Category(
                id = 4,
                name = "Medical",
                0xFF37D8CF,
                R.drawable.ic_medical
            ),
            Category(
                id = 5,
                name = "Emi",
                0xFFF6CC00,
                R.drawable.ic_emi
            ),
            Category(
                id = 6,
                name = "Bills",
                0xFFFF4077,
                R.drawable.ic_bills
            )
        )

    override suspend fun getCategories(): List<Category> {
        return categoryList.take(4)
    }

    override suspend fun getAllCategories(): List<Category> {
        return categoryList
    }

    override suspend fun members(): List<SingleItem>{
        return listOf(
            SingleItem(
                id = 1,
                profilePic = "https://i.pravatar.cc/300",
                userName = "You",
                mobileNo = "9563376942",
                isSelected = false
            ),
            SingleItem(
                id = 2,
                profilePic = "https://i.pravatar.cc/300",
                userName = "Manisha Roy",
                mobileNo = "9563376942",
                isSelected = false
            ),
            SingleItem(
                id = 3,
                profilePic = "https://i.pravatar.cc/300",
                userName = "Sushil Roy",
                mobileNo = "9563376942",
                isSelected = false
            ),
            SingleItem(
                id = 4,
                profilePic = "https://i.pravatar.cc/300",
                userName = "Sanjana Ray",
                mobileNo = "9563376942",
                isSelected = false
            ),
            SingleItem(
                id = 5,
                profilePic = "https://i.pravatar.cc/300",
                userName = "Ankita Ray",
                mobileNo = "9563376942",
                isSelected = false
            )
        )
    }
}
class SettleRepositoryImpl : SettleRepository {
    private val getList = listOf(
        Transaction(
            name = "You",
            imageUrl = "https://i.pravatar.cc/300",
            mobileNumber = "9563376942",
            amount = 3000f,
            transactionType = TransactionType.Paid
        ),
        Transaction(
            name = "Sushil Roy",
            imageUrl = "https://i.pravatar.cc/300",
            mobileNumber = "9563376942",
            amount = 1000f,
            transactionType = TransactionType.Paid
        ),
        Transaction(
            name = "Manisha Roy",
            imageUrl = "https://i.pravatar.cc/300",
            mobileNumber = "9563376942",
            amount = 500f,
            transactionType = TransactionType.Unspecified
        ),
        Transaction(
            name = "Sanjanaa Ray",
            imageUrl = "https://i.pravatar.cc/300",
            mobileNumber = "9563376942",
            amount = 0f,
            transactionType = TransactionType.Unspecified
        )
    )

    private val payList = listOf(
        Transaction(
            name = "You",
            imageUrl = "https://i.pravatar.cc/300",
            mobileNumber = "9563376942",
            amount = 3000f,
            transactionType = TransactionType.Paid
        ),
        Transaction(
            name = "Sushil Roy",
            imageUrl = "https://i.pravatar.cc/300",
            mobileNumber = "9563376942",
            amount = 100000f,
            transactionType = TransactionType.Paid
        )
    )

    override suspend fun getWillGet(): List<Transaction> {
        return getList
    }

    override suspend fun getWillPay(): List<Transaction> {
        return payList
    }
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
        category = Category(
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
        category = Category(
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
        category = Category(
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
        category = Category(
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
        category = Category(
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
        category = Category(
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
        category = Category(
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
class GroupSummaryRepositoryImpl : GroupSummaryRepository {

    val members = listOf(
        SplitSelectableMember(name = "You", "https://i.pravatar.cc/300?", true),
        SplitSelectableMember(name = "Sushil", "https://i.pravatar.cc/300?", false)
    )

    private val getList1 = listOf(
        MemberTransact(
            name = "You",
            image = "https://i.pravatar.cc/300?",
            mobile = "9563376942",
            amount = 30000f
        ),
        MemberTransact(
            name = "Sushil Roy",
            image = "https://i.pravatar.cc/300?",
            mobile = "9563376942",
            amount = 1000f
        ),
        MemberTransact(
            name = "Manisha Roy",
            image = "https://i.pravatar.cc/300?",
            mobile = "9563376942",
            amount = 500f
        ),
        MemberTransact(
            name = "Sanjanaa Ray",
            image = "https://i.pravatar.cc/300?",
            mobile = "9563376942",
            amount = 0f
        )
    )
    private val paidList = emptyList<MemberTransact>()

    override suspend fun getMembers(): List<SplitSelectableMember> {
        return members
    }

    override suspend fun getWillGetTransactions(): List<MemberTransact> {
        return getList1
    }

    override suspend fun getWillPayTransactions(): List<MemberTransact> {
        return paidList
    }

}
class ManageRepositoryMockImpl : ManageRepository {
    val group1 = listOf(
        Member(
            id = 1,
            profilePic = "https://i.pravatar.cc/300",
            userName = "You",
            mobileNo = "9563376942",
            isSelected = false,
            isGroupAdmin = true
        ),
        Member(
            id = 2,
            profilePic = "https://i.pravatar.cc/300",
            userName = "Manisha Roy",
            mobileNo = "9563376942",
            isSelected = false
        ),
        Member(
            id = 3,
            profilePic = "https://i.pravatar.cc/300",
            userName = "Sushil Roy",
            mobileNo = "9563376942",
            isSelected = false
        ),
        Member(
            id = 4,
            profilePic = "https://i.pravatar.cc/300",
            userName = "Sanjana Ray",
            mobileNo = "9563376942",
            isSelected = false
        ),
        Member(
            id = 5,
            profilePic = "https://i.pravatar.cc/300",
            userName = "Ankita Ray",
            mobileNo = "9563376942",
            isSelected = false
        )
    )

    override suspend fun getMembers1(): List<Member> {
        return group1
    }

}

interface Repo {
    suspend fun groupAndContacts(): List<GroupOrContact>
    suspend fun groups(contacts: List<ContactData>): List<GroupData>
    suspend fun peoples(count: Int = 20): List<ContactData>
    suspend fun saveContacts(contacts: List<String>)
    suspend fun purgeContacts()
    suspend fun contacts(): List<String>
    fun deviceContacts(contacts: List<String>): List<ContactData>
}
interface SplitReviewRepository {
    suspend fun getPaidByMembers(): List<MemberPayment>
}
interface GroupRepository {
    suspend fun getBillTransactions(): List<BillTransaction>
    suspend fun getCategories(): List<Category>
    suspend fun getAllCategories(): List<Category>
    suspend fun getBillTransactions(number: Int): List<BillTransaction>
    suspend fun members(): List<SingleItem>
}
interface SettleRepository {
    suspend fun getWillGet(): List<Transaction>
    suspend fun getWillPay(): List<Transaction>
}
interface TransactionRepository {
    suspend fun getTransactions(): List<BillTransaction>
    suspend fun getTransactions(number: Int): List<BillTransaction>
}
interface GroupSummaryRepository {
    suspend fun getMembers(): List<SplitSelectableMember>
    suspend fun getWillPayTransactions(): List<MemberTransact>
    suspend fun getWillGetTransactions(): List<MemberTransact>
}
interface ManageRepository {
    suspend fun getMembers1(): List<Member>
}*/
// ///////////////////

/*
interface ExpenseRepository {
    suspend fun getCategoryExpense(): List<CategoryExpense>
    suspend fun getBarChartData(): List<ExpenseChartData>
    suspend fun getPieChartData(): List<PieData>
    suspend fun getDatePickerData(): YoreDatePickerData
}

class ExpenseRepositoryImpl : ExpenseRepository {

    private val categories = listOf(
        CategoryExpense(
            icon = R.drawable.ic_trip,
            category = "Trip",
            description = "Business Trip",
            count = 1,
            amount = 500.00f,
            tint = RadicalRed
        ),
        CategoryExpense(
            icon = R.drawable.ic_food,
            category = "Food",
            description = "BFFs",
            count = 5,
            amount = 2500.00f,
            tint = CuriousBlue
        )
    )

    private val barChartData = listOf(
        ExpenseChartData(
            xAxis = "Jan",
            yAxis = 12000f,
            year = 2022
        ),
        ExpenseChartData(
            xAxis = "Feb",
            yAxis = 25000f,
            year = 2023
        ),
        ExpenseChartData(
            xAxis = "Mar",
            yAxis = 4500f,
            year = 2024
        ),
        ExpenseChartData(
            xAxis = "Apr",
            yAxis = 0f,
            year = 2026
        ),
        ExpenseChartData(
            xAxis = "May",
            yAxis = 1000f,
            year = 2026
        ),
        ExpenseChartData(
            xAxis = "Jun",
            yAxis = 25000f,
            year = 2026
        ),
        ExpenseChartData(
            xAxis = "Jul",
            yAxis = 5000f,
            year = 2027
        ),
        ExpenseChartData(
            xAxis = "Aug",
            yAxis = 50000f,
            year = 2026
        ),
        ExpenseChartData(
            xAxis = "Sep",
            yAxis = 35000f,
            year = 2026
        )
        */
/*ExpenseChartData(
            xAxis = "Oct",
            yAxis = 0f,
            year = 2026
        ),
        ExpenseChartData(
            xAxis = "Nov",
            yAxis = 2000f,
            year = 2026
        ),
        ExpenseChartData(
            xAxis = "Dec",
            yAxis = 20000f,
            year = 2026
        )*//*

    )

    private val pieChartData = listOf(
        PieData(
            RadicalRed,
            0.5f
        ),
        PieData(
            CuriousBlue,
            0.5f
        )
    )

    private val datePickerData = YoreDatePickerData(
        selectedDay = 1,
        selectedMonth = 1,
        selectedYear = 2022,
        minDate = Kal.Date.create(1, 1, 2022),
        maxDate = Kal.Date.create(23, 8, 2036),
        dateSelectable = true,
        yearSwitchable = true
    )

    override suspend fun getCategoryExpense(): List<CategoryExpense> {
        return categories
    }

    override suspend fun getBarChartData(): List<ExpenseChartData> {
        return barChartData
    }

    override suspend fun getPieChartData(): List<PieData> {
        return pieChartData
    }

    override suspend fun getDatePickerData(): YoreDatePickerData {
        return datePickerData
    }

}*/

/*interface TimeFilterRepository {
    suspend fun getItems(): List<SheetItem>
}

class TimeFilterRepositoryImpl : TimeFilterRepository {

    private val itemList = listOf(
        SheetItem(id = 0, R.drawable.calendar_1, "All Time"),
        SheetItem(id = 1, R.drawable.last_month, "Last Month"),
        SheetItem(id = 2, R.drawable.this_month, "This Month"),
        SheetItem(id = 3, R.drawable.custom_month, "Custom Month")
    )

    override suspend fun getItems(): List<SheetItem> {
        return itemList
    }
}*/
