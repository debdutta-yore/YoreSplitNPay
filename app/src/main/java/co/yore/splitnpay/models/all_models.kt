package co.yore.splitnpay.models

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import co.yore.splitnpay.R
import co.yore.splitnpay.ui.theme.*
import java.math.BigDecimal

data class SplitCardDetailsPageData(
    val splitAmount: Float,
    val splitBalance: Float,
    val splitStatusMessage: String,
    val splitProgress: Float,
    val splitPaidMark: String,
    val splitTransacted: Float,
    val splitCardDetailsData: SplitCardDetailsData
)

data class SplitBrief(
    val id: Any,
    val amount: Float,
    val description: String,
    val date: SplitBrief.Date,
    val category: Category
){
    data class Date(val year: Int, val month: Int, val day: Int)
}

data class GroupChatPageData(
    val name: String,
    val amount: Float,
    val image: Any?
)

data class SplitPageData(
    val willGet: Float,
    val willPay: Float,
    val splitted: Boolean
)

data class Category(
    val id: Any,
    val name: String,
    val color: Long,
    val icon: Any?,
    val isSelected: Boolean = false,
    val isEditable: Boolean = false,
    val subCategory: String = "Business trip"
) {
    companion object {
        operator fun get(id: Any) = when (id) {
            "trip" -> trip
            "food" -> food
            "rent" -> rent
            "party" -> party
            "medical" -> medical
            "emi" -> emi
            "bills" -> bills
            else -> blank
        }
        val blank = Category(
            id = -1,
            name = "",
            color = 0,
            icon = null
        )
        val trip = Category(
            id = "trip",
            name = "Trip",
            color = RadicalRed.toArgb().toLong(),
            icon = R.drawable.ic_trip
        )
        val food = Category(
            id = "food",
            name = "Food",
            color = CuriousBlue.toArgb().toLong(),
            icon = R.drawable.ic_food
        )
        val rent = Category(
            id = "rent",
            name = "Rent",
            color = Supernova.toArgb().toLong(),
            icon = R.drawable.ic_rent
        )
        val party = Category(
            id = "party",
            name = "Party",
            color = Turquoise1.toArgb().toLong(),
            icon = R.drawable.ic_party
        )
        val medical = Category(
            id = "medical",
            name = "Medical",
            color = Turquoise.toArgb().toLong(),
            icon = R.drawable.ic_medical
        )
        val emi = Category(
            id = "emi",
            name = "EMI",
            color = BuddhaGold.toArgb().toLong(),
            icon = R.drawable.ic_emi
        )
        val bills = Category(
            id = "bills",
            name = "Bills",
            color = Amaranth.toArgb().toLong(),
            icon = R.drawable.ic_bills
        )
        val list = listOf(
            food,
            trip,
            rent,
            party,
            medical,
            emi,
            bills
        )
    }
}

// //////////////////////////////////////////////////////////////

interface GroupOrContact {
    fun id(): Any
    fun lastActivity(): Long
    fun searchables(): List<String>
    fun willGet(): Float
    fun willPay(): Float
}

data class Member(
    val id: Int = 0,
    val profilePic: String,
    val userName: String,
    val mobileNo: String,
    val isSelected: Boolean,
    val isGroupAdmin: Boolean = false
)

data class StatusBarColor(
    val color: Color,
    val darkIcons: Boolean = false
)

data class Bank(
    val name: String = "Axis Bank",
    val imageUrl: Any = R.drawable.ic_axis
)

data class BillTransaction(
    val transactionType: TransactionType,
    val transactionStatus: TransactionStatus,
    val isSingleChat: Boolean,
    val billTotal: Float, // total transaction amount to settle
    val allPaidReceivedTotal: Float, // total paid/received until now
    val amountLeft: Float, // amount left to complete the bill total
    val willPayReceive: Float, // you will received/pay in this transaction
    val paidReceived: Float, // paid received in this transaction
    val totalTransactions: Int,
    val completedTransactions: Int,
    val transactionTime: String,
    val transactionDate: String,
    val paymentMethod: String,
    val from: Friend,
    val to: Friend,
    val category: Category
)

enum class AccountType {
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

data class ContactData(
    val id: Any,
    val image: Any?,
    val name: String,
    val mobile: String,
    val willPay: Float = 0.0f,
    val willGet: Float = 0.0f,
    val selected: Boolean = false,
    val lastActivity: Long = 0,
    val deletable: Boolean = true
) : GroupOrContact {
    override fun id(): Any {
        return this.id
    }

    override fun lastActivity(): Long {
        return this.lastActivity
    }

    override fun searchables(): List<String> {
        return listOf(
            name.lowercase(),
            mobile
        )
    }

    override fun willGet(): Float {
        return willGet
    }

    override fun willPay(): Float {
        return willPay
    }
}

data class GroupData(
    val id: Any,
    val image: Any?,
    val name: String,
    val members: List<ContactData>,
    val willGet: Float = 0.0f,
    val willPay: Float = 0.0f,
    val showGroupBalance: Boolean = willGet > 0f || willPay > 0f,
    val lastActivity: Long = 0
) : GroupOrContact {
    override fun id(): Any {
        return this.id
    }

    override fun lastActivity(): Long {
        return this.lastActivity
    }

    override fun searchables(): List<String> {
        return listOf(
            name.lowercase()
        )
    }

    override fun willGet(): Float {
        return willGet
    }

    override fun willPay(): Float {
        return willPay
    }
}

data class BillCardAllConfiguration(

    val cardBackground: Color = Color.White,
    val cardHeight: Float = 125f,
    val allSettledCardHeight: Float = 120f,

    val firstRowVerticalAllSettledSpacing: Float = 15f,
    val firstRowVerticalPaidSpacing: Float = 15f,
    val firstRowVerticalReceivedSpacing: Float = 18f,
    val firstRowAllSettledInternalSpacing: Float = 8f,
    val progressBarTopAllSettledSpacing: Float = 7f,
    val progressBarTopSpacing: Float = 4f,

    val billTotalLetterSpacing: Float = (-0.333333f),
    val completedTransactionLetterSpacing: Float = -0.333333f,
    val willGetPayLetterSpacing: Float = -0.235784f,
    val allPaidReceivedLetterSpacing: Float = -0.575758f,
    val amountLeftLetterSpacing: Float = -0.575758f,

    val dotStartHorizontalSpacer: Float = 4f,
    val dotEndHorizontalSpacer: Float = 5f,
    val dotLastHorizontalSpacer: Float = 4f,
    val chatMessageReadSpacer: Float = 9f,
    val cardBottomSpacer: Float = 17f,

    val cardCornerAll: Float = 22f,
    val cardCornerRounded: Float = 15f,
    val cardCornerNotRounded: Float = 0f,

    val billTotalRowStartPadding: Float = 24f + 25f,
    val billTotalRowStartAllSettledPadding: Float = 24f,
    val billTotalRowStartIsPaidPadding: Float = 21f,
    val containerEndPadding: Float = 34f,
    val arrowIconEndPadding: Float = 14f + 25f,
    val arrowIconTopPadding: Float = 2f,
    val arrowIconEndAllSettledPadding: Float = 11f,
    val containerAllSettledEndPadding: Float = 0f,
    val cardInnerRowTopPadding: Float = 15f,
    val allPaidReceivedTopAllSettledPadding: Float = 10f,
    val allPaidReceivedTopPadding: Float = 4f,
    val allPaidReceivedExtraHorizontalPadding: Float = 2f,

    val columnStartPadding: Float = 75f,
    val columnStartAllSettledPadding: Float = 20f,
    val columnStartPaidPadding: Float = (46f + 25f),
    val columnStartReceivedPadding: Float = 72f,
    val columnEndAllSettledPadding: Float = 20f,
    val columnEndPaidPadding: Float = 53f - 25f,
    val columnEndReceivedPadding: Float = 24f + 25f,
    val allPaidReceivedAllSettledStartPadding: Float = 24f - 2f,
    val allPaidReceivedPaidStartPadding: Float = 53f - 25f,
    val allPaidReceivedReceivedStartPadding: Float = 24f + 25f,
    val allPaidReceivedAllSettledEndPadding: Float = 20f,
    val allPaidReceivedPaidEndPadding: Float = 72f,
    val allPaidReceivedReceivedEndPadding: Float = 72f,
    val completedTransactionEndPadding: Float = 2f,
    val completedTransactionTopPadding: Float = 4f,

    val paidReceivedColor: Color = WildBlueYonder,
    val totalPaidReceivedTextColor: Color = WildBlueYonder,
    val cardShadowColor: Color = SteelBlue.copy(0.2f),
    val paidSelectedAmountTextColor: Color = RadicalRed,
    val receivedSelectedAmountTextColor: Color = Turquoise1,
    val amountNormalTextColor: Color = CloudBurst,
    val paidTimeBackgroundColor: Color = RadicalRed,
    val settledTimeBarBackgroundColor: Color = WildBlueYonder,
    val receivedTimeBackgroundColor: Color = Turquoise1,
    val paidProgressColor: Color = CloudBurst,
    val receiveProgressColor: Color = Turquoise1,
    val paidProgressBackgroundColor: Color = RadicalRed,
    val receiveProgressBackgroundColor: Color = CloudBurst,

    val willGetPayTextColor: Color = WildBlueYonder,
    val allPaidReceivedLeadingTextColor: Color = WildBlueYonder,
    val allPaidReceivedTrailingTextColor: Color = Turquoise1,
    val amountLeftTextColor: Color = WildBlueYonder,

    val billTotalCurrencyFontSize: Float = 8f,
    val billTotalLeadingFontSize: Float = 11f,
    val billTotalWholeFontSize: Float = 15f,
    val billTotalDecFontSize: Float = 9f,
    val paidReceivedFontSize: Float = 9f,
    val leftAmountCurrencyFontSize: Float = 11f,
    val leftAmountWholeFontSize: Float = 13f,
    val leftAmountDecFontSize: Float = 9f,
    val totalPaidReceivedFontSize: Float = 9f,
    val willGetPayFontSize: Float = 9f,
    val willGetPayCurrencyFontSize: Float = 11f,
    val willGetPayWholeFontSize: Float = 13f,
    val willGetPayDecFontSize: Float = 9f,
    val allPaidReceivedFontSize: Float = 9f,
    val allPaidReceivedTrailingFontSize: Float = 9f,
    val allPaidReceivedCurrencyFontSize: Float = 9f,
    val allPaidReceivedWholeFontSize: Float = 9f,
    val allPaidReceivedDecFontSize: Float = 9f,
    val amountLeftCurrencyFontSize: Float = 9f,
    val amountLeftWholeFontSize: Float = 9f,
    val amountLeftDecFontSize: Float = 9f,
    val amountLeftTrailingFontSize: Float = 9f,

    val arrowIconSize: Float = 14f,

    val progressBarHeight: Float = 5f,
    val totalPaidReceivedLineHeight: Float = 14f,
    val cardShadowBorderRadius: Float = 15f,
    val cardShadowBlurRadius: Float = 24f,
    val cardShadowOffsetX: Float = 3f,
    val cardShadowOffsetY: Float = 3f,
    val cardShadowSpread: Float = 0f,

    val categoryIconSize: Float = 19f,
    val profileImageSize: Float = 30f,
    val seenIconSize: Float = 16f,

    val rightArrow: Int = R.drawable.ic_right_arrow,

    val billTotalText: Int = R.string.bill_total,
    val receivedStatusText: Int = R.string.paid,
    val paidStatusText: Int = R.string.received,
    val youReceivedStatusText: Int = R.string.you_received,
    val youPaidStatusText: Int = R.string.you_paid,
    val payStatusText: Int = R.string.you_will_pay,
    val receiveStatusText: Int = R.string.you_will_get,
    val totalReceivedText: Int = R.string.all_received,
    val totalPaidText: Int = R.string.all_paid
)

data class Transaction(
    val name: String,
    val imageUrl: String = "https://i.pravatar.cc/300",
    val mobileNumber: String,
    val amount: Float,
    val transactionType: TransactionType,
    val isSelected: Boolean = false
)

enum class SplitListOptions {
    Equal,
    Unequal,
    Ratio,
    Percentage
}

data class SplitTypeRowItem(
    val selectedOptionBackground: Color = CuriousBlue,
    val unSelectedOptionBackground: Color = Zumthor,
    val selectedOptionTextColor: Color = White,
    val unSelectedOptionTextColor: Color = CuriousBlue,
    val fontSize: Float = 12f
)

data class GroupShort(
    val name: String,
    val imageUrl: String = "",
    val creationDate: String = "May 9th 2022",
    val amount: Float
)

data class GroupDetailed(
    val name: String,
    val imageUrl: String,
    val creationDate: String,
    val getWholeBalance: String,
    val payWholeBalance: String,
    val getDecBalance: String,
    val payDecBalance: String,
    val members: List<MemberEx>
)

data class GroupCreationEvent(
    val creator: String,
    val groupName: String
)

data class ChatStatus(
    val memberImages: List<Any?>,
    val left: Boolean = true
)

data class Conversation(
    val type: Type,
    val data: Any
) {
    enum class Type {
        TRANSACTION,
        DATE,
        CREATION,
        STATUS,
        CHAT,
        MEMBER,
    }
}

data class ChatData(
    val content: Any,
    val profileImage: Any? = null
)

data class MemberData(
    val name: String,
    val profileImage: Any?
)

data class SingleItem(
    val id: Int,
    val profilePic: Any?,
    val userName: String,
    val mobileNo: String,
    val isSelected: Boolean
)

data class MemberEx(
    val id: Int = 0,
    val profilePic: String,
    val userName: String,
    val mobileNo: String,
    val isSelected: Boolean,
    val isGroupAdmin: Boolean
)

data class Amount(
    val wholeText: String,
    val decText: String,
    val whole: BigDecimal,
    val dec: Float
) {
    override fun toString(): String {
        return "$wholeText.$decText"
    }
}

data class PaidBySingleItem(
    val name: String,
    val imageUrl: String,
    val mobileNumber: String,
    val amount: Float
)

data class MemberTransact(
    val name: String,
    val mobile: String,
    val image: Any?,
    val amount: Float
)

data class SplitCardDetailsData(
    val status: String,
    val splitMethod: String,
    val categories: String,
    val categoryIcon: Any?,
    val createdBy: String,
    val createdOn: String,
    val noOfMembers: Int
) {
    companion object {
        val blank = SplitCardDetailsData(
            status = "",
            splitMethod = "",
            categories = "",
            createdBy = "",
            createdOn = "",
            noOfMembers = 0,
            categoryIcon = null
        )
    }
}

data class TransactionReview(
    val transactionType: TransactionType,
    val paymentMethod: String,
    val amount: Float,
    val from: Friend,
    val to: Friend,
    val category: Category
)

data class Upi(
    val upiName: String,
    val upiId: String,
    val bankName: String,
    val userName: String,
    val backGroundColor: Color,
    val isSelected: Boolean
)

data class Date(
    val year: Int,
    val month: Int,
    val day: Int
)

data class Item(val id: Int, val icon: Int, val name: String)

data class CustomRadioButton(
    val height: Float = 47f,
    val cornerRadius: Float = 12f,
    val selectedBackGroundColor: Color = Zircon1,
    val unSelectedBackGroundColor: Color = Color.White,
    val borderColor: Color = CuriousBlue,
    val borderWidth: Float = 1f,
    val selectedTextColor: Color = CuriousBlue,
    val unSelectedTextColor: Color = CloudBurst,
    val fontSize: Float = 14f
)

data class SingleSettledOrUnsettledMember(
    val selectedSettleOption: SettleOptions = SettleOptions.SplitIndividual,
    val isChecked: Boolean = false,
    val isSettledMember: Boolean,
    val imageUrl: String,
    val userName: String,
    val userPhNo: String,
    val getAmount: Float,
    val paidAmount: Float
)

data class SplitSelectableMember(
    val name: String,
    val image: Any?,
    val isSelected: Boolean
)

data class FloatSplitted(
    val wholeString: String,
    val decString: String,
    val whole: Int
)

data class MemberPayment(
    val id: Any,
    val name: String,
    val mobile: String,
    val image: Any?,
    val paid: Double = 0.0,
    val toPay: Double = 0.0,
    val selected: Boolean = false
)

data class MemberWillGetOrPayDetailsSingleRowItem(
    val imageUrl: String,
    val userName: String,
    val userPhNo: String,
    val getAmount: Float,
    val paidAmount: Float
)

enum class TransactionStatus1 {
    Pending,
    PartiallyReceived,
    Settled,
    AllSettled
}

data class ExpenseChartData(
    val xAxis: String,
    val yAxis: Float,
    val year: Int,
    val description: String = "",
    val enabled: Boolean = true
)

interface DateDisplayData {
    val error: Boolean
}

object DateDisplayDataNone : DateDisplayData {
    override val error: Boolean
        get() = false
}

data class DateDisplayDataDate(
    val value: String,
    val duration: String,
    val range: Boolean,
    override val error: Boolean
) : DateDisplayData

data class DateDisplayDataMonths(
    val from: String,
    val to: String,
    val duration: String,
    override val error: Boolean
) : DateDisplayData

data class SheetItem(
    val id: Int,
    val icon: Int,
    val iconName: String,
    val isSelected: Boolean = false
)

data class LabelData(
    val index: Int,
    val touchArea: TouchArea?
)

data class BarGraphData(
    val xAxisGap: Float,
    val xValuesCount: Int,
    val xValueCallback: (Int) -> Float,
    val xAxisLabelCallback: (Int) -> String,
    val xLabelCallback: (Int) -> String,
    val barGraphColorCallback: (Int) -> Color,
    val maxYAxisValue: Float,
    val yAxisLabelCount: Int,
    val yAxisLabelCallback: (Float) -> String,
    val xAxisLabelColor: (Int) -> Color,
    val yAxisLabelColor: () -> Color,
    val axisColor: Color = Color.Gray,
    val strokeWidth: Float,
    val axisLabelFontSize: Float,
    val highlightedBarColor: Color,
    val highlightedXLabelColor: Color,
    val disabledTextColor: Color,
    val enabled: (Int) -> Boolean,
)

data class TouchArea(
    val offset: Offset,
    val size: Size
) {
    fun contains(pos: Offset): Boolean {
        return pos.x >= offset.x &&
            pos.x <= offset.x + size.width &&
            pos.y >= offset.y &&
            pos.y <= pos.y + size.height
    }
}

enum class SplitPageState {
    GET,
    PAY,
    NONE
}

enum class ContactTabs {
    Recent,
    Groups,
    People,
    Contact
}

enum class SettleBottomSheetTabs {
    YouWillGet,
    YouWillPay
}

enum class SettlePaymentOptions {
    Cash,
    Upi
}

enum class SplitPageTabs {
    All,
    YouOwe,
    YouAreOwed
}

enum class YouWillPayTransactionStatus {
    Pending,
    PartiallyPaid,
    Settled
}

enum class TransactionType {
    Received,
    Paid,
    Unspecified
}

enum class TransactionStatus {
    Started,
    Settled,
    AllSettled,
    Filtered,

}

enum class VerticalRotation(val value: Float) {
    CLOCKWISE(90f), COUNTER_CLOCKWISE(270f)
}

enum class GroupChatTab {
    All,
    Pending,
    Settled
}

enum class TimeTabs {
    SINGLE,
    RANGE,
}

enum class SummaryMode {

    CATEGORY, TIME;

    val next: SummaryMode
        get() {
            val index = this.ordinal
            var nextIndex = index + 1
            val vals = enumValues<SummaryMode>()
            val count = vals.size
            nextIndex %= count
            return vals[nextIndex]
        }
}

enum class SettleOptions {
    SplitIndividual,
    DeleteAnyway
}

enum class DatePickerFromTo {

    From, To;

    val switch: DatePickerFromTo
        get() {
            val index = this.ordinal
            var nextIndex = index + 1
            val vals = enumValues<DatePickerFromTo>()
            val count = vals.size
            nextIndex %= count
            return vals[nextIndex]
        }
}

enum class DatePickerRange {

    Date, DateRange;

    val switch: DatePickerRange
        get() {
            val index = this.ordinal
            var nextIndex = index + 1
            val vals = enumValues<DatePickerRange>()
            val count = vals.size
            nextIndex %= count
            return vals[nextIndex]
        }
}

enum class DatePickerOption {

    Monthly, Weekly;

    val switch: DatePickerOption
        get() {
            val index = this.ordinal
            var nextIndex = index + 1
            val vals = enumValues<DatePickerOption>()
            val count = vals.size
            nextIndex %= count
            return vals[nextIndex]
        }
}

enum class TriState {
    CHECKED,
    UNCHECKED,
    INTERMEDIATE
}
