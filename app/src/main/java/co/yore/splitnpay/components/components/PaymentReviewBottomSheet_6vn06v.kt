package co.yore.splitnpay.components.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import co.yore.splitnpay.R
import co.yore.splitnpay.addmembers.FontFamilyText
import co.yore.splitnpay.demos.sx
import co.yore.splitnpay.demos.sy
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.models.*
import co.yore.splitnpay.pages.CustomButtonConfiguration
import co.yore.splitnpay.pages.CustomButton_3egxtx
import co.yore.splitnpay.ui.theme.*
import coil.compose.AsyncImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PaymentReviewBottomSheetModel(
    val callback: Callback
) : BottomSheetModel{
    interface Callback{
        fun scope(): CoroutineScope
        fun transaction(): TransactionReview
        fun onChangeReceiver()
        fun onChangeAmount()
    }

    // ////////////////
    private val _resolver = Resolver()
    private val _notifier = NotificationService{id, arg ->
        when (id){
            DataIds.paymentReviewSubCategory -> {
                paymentReviewSubCategory.value = arg as? String ?: return@NotificationService
            }
            DataIds.paymentReviewChangeClick -> {
                callback.onChangeReceiver()
            }
            DataIds.paymentReviewEditAmountClick -> {
                callback.onChangeAmount()
            }
        }
    }

    // /////////////////
    override val resolver = _resolver
    override val notifier = _notifier
    override val scope get() = callback.scope()

    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    override fun Content() {
        /*val activity = LocalContext.current as Activity
        DisposableEffect(key1 = Unit) {
            activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
            onDispose {
                activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
            }
        }*/
        PaymentReviewBottomSheet_6vn06v(contentDescription = "")
    }

    override fun initialize() {
        scope.launch(Dispatchers.IO) {
            val t = callback.transaction()
            withContext(Dispatchers.Main){
                transaction.value = t
            }
        }
    }

    override fun clear() {

    }

    override fun onBack() {

    }

    // ///////////////
    private val transaction = mutableStateOf<TransactionReview?>(null)
    private val paymentReviewSubCategory = mutableStateOf("")

    // ////////////////
    init {
        _resolver.addAll(
            DataIds.paymentReviewTransaction to transaction,
            DataIds.paymentReviewSubCategory to paymentReviewSubCategory
        )
    }
}

@Composable
fun PaymentReviewBottomSheet_6vn06v(
    transaction: TransactionReview? = tState<TransactionReview?>(key = DataIds.paymentReviewTransaction).value,
    paymentReviewSubCategory: String = stringState(key = DataIds.paymentReviewSubCategory).value,
    notifier: NotificationService = notifier(),
    contentDescription: String,
    config: PaymentReviewBottomSheetConfiguration = PaymentReviewBottomSheetConfiguration()
) {
    val isTransactionAvailable = remember(key1 = transaction) {
        derivedStateOf {
            transaction != null
        }
    }
    val isPaid = remember {
        derivedStateOf { transaction?.transactionType == TransactionType.Paid }
    }
    val buttonText = remember {
        derivedStateOf { if (isPaid.value) "Confirm and Pay" else "Confirm and Request" }
    }
    Column(
        modifier = Modifier
            .semantics { this.contentDescription = contentDescription }
            .animateContentSize()
            .ModalBottomSheetAdjustWithIme()
            .fillMaxWidth()
    ) {
        config.holderIconTopPadding.sy()
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50.dep()))
                .height(2.dep())
                .width(19.dep())
                .background(Color(0xff5A87BB))
                .align(Alignment.CenterHorizontally)
        )
        config.holderBottomPadding.sy()
        if (isTransactionAvailable.value) {
            FromToPortion(
                contentDescription = "FromToColumn",
                transaction = transaction!!
            )
            config.fromToPortionBottomPadding.sy()
            AmountSection(
                contentDescription = "AmountPortion",
                transaction = transaction
            )
        }
        config.amountSectionBottomPadding.sy()
        Box(
            modifier = Modifier
                .padding(horizontal = 31.dep())
                .fillMaxWidth()
                .background(
                    color = LightGrey2,
                    shape = RoundedCornerShape(8.dep())
                )
                .clip(RoundedCornerShape(8.dep()))
                .height(52.dep())
        ) {
            CustomTextField_wangst(
                text = paymentReviewSubCategory,
                change = {
                    notifier.notify(DataIds.paymentReviewSubCategory, it)
                },
                config = CustomTextFieldConfiguration(
                    textColor = LightGrey
                ),
                leadingIcon = R.drawable.ic_description,
                placeHolderText = stringResource(id = R.string.note),
                contentDescription = "CustomTextField",
                iconTint = Color(0xff656565)
            )
        }
        config.customTextBoxBottomPadding.sy()
        Box(
            modifier = Modifier
                .padding(horizontal = 31.dep())
                .fillMaxWidth()
                .height(55.dep())
                .clip(RoundedCornerShape(11.dep()))
                .background(color = LightGrey2),
            contentAlignment = Alignment.Center
        ) {
            FontFamilyText(
                text = stringResource(id = R.string.settle_confirm_note),
                color = LightGrey3,
                fontSize = 11.sep(),
                letterSpacing = (-0.33).sep(),
                lineHeight = 12.89.sep()
            )
        }
        config.acceptTextBottomPadding.sy()
        Box(
            modifier = Modifier
                .padding(horizontal = 31.dep())
                .height(50.dep())
        ) {
            CustomButton_3egxtx(
                text = buttonText.value,
                onClick = {
                    notifier.notify(DataIds.paymentReviewContinueClick)
                },
                contentDescription = "confirmAndPayButton",
                config = CustomButtonConfiguration(
                    fontWeight = FontWeight.Bold
                )
            )
        }
        config.buttonBottomPadding.sy()
        /*Box(
            modifier = Modifier
                .navigationBarsPadding()
                .imePadding()
        ){

        }*/
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

@Composable
fun AmountSection(
    transaction: TransactionReview,
    notifier: NotificationService = notifier(),
    contentDescription: String,
    config: AmountPortionConfiguration = AmountPortionConfiguration()
) {
    val isPaid = remember {
        derivedStateOf { transaction.transactionType == TransactionType.Paid }
    }
    val tintColor = remember {
        derivedStateOf { if (isPaid.value) Pink else Bluish }
    }
    val amountTrailingText = remember {
        derivedStateOf {
            if (isPaid.value) "- " else ""
        }
    }
    Column(
        modifier = Modifier
            .semantics { this.contentDescription = contentDescription }
            .fillMaxWidth()
            .padding(
                start = config.startPadding.dep(),
                end = config.endPadding.dep()
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            FontFamilyText(
                text = stringResource(id = R.string.amount) + ":",
                color = DarkBlue,
                fontSize = 14.sep(),
                letterSpacing = 0.666667.sep(),
                lineHeight = 16.sep()
            )
            EditIcon_gx6f1w(
                text = stringResource(id = R.string.edit),
                icon = R.drawable.edit_icon_2,
                contentDescription = "edit icon",
                onClick = {
                    notifier.notify(DataIds.paymentReviewEditAmountClick)
                }
            )
        }
        6.sy()
        FontFamilyText(
            annotatedString = transaction.amount
                .amountAnnotatedString(
                    leadingText = amountTrailingText.value,
                    leadingTextTextColor = tintColor.value,
                    leadingTextFontSize = 12f,
                    isLeadingTextEnabled = true,
                    isSpaceBetween = true,
                    currencyTextColor = tintColor.value,
                    currencyFontSize = 12f,
                    wholeNumberTextColor = tintColor.value,
                    wholeNumberFontSize = 16f,
                    wholeNumberFontWeight = FontWeight.Bold,
                    decNumberTextColor = tintColor.value,
                    decNumberFontSize = 16f,
                    decNumberFontWeight = FontWeight.Bold
                ),
            letterSpacing = (-0.333333).sep(),
            lineHeight = 19.sep()
        )
        3.sy()
        FontFamilyText(
            text = transaction.amount.numberToWords(),
            color = DarkBlue,
            fontSize = 12.sep()
        )
        20.sy()
        FontFamilyText(
            text = stringResource(R.string.payment_method) + ":",
            color = DarkBlue,
            fontSize = 14.sep()
        )
        9.sy()
        FontFamilyText(
            text = transaction.paymentMethod,
            color = DarkBlue,
            fontSize = 14.sep(),
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun FromToPortion(
    transaction: TransactionReview,
    notifier: NotificationService = notifier(),
    contentDescription: String,
    config: FromToPortionConfiguration = FromToPortionConfiguration()
) {
    val isPaid = remember {
        derivedStateOf { transaction.transactionType == TransactionType.Paid }
    }
    val tintColor = remember {
        derivedStateOf { if (isPaid.value) Pink else Bluish }
    }
    Row(
        modifier = Modifier
            .semantics { this.contentDescription = contentDescription }
            .padding(
                start = config.startPadding.dep(),
                end = config.endPadding.dep()
            )
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(top = config.bankIconColumnTopPadding.dep()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BankSymbolIcon_1a0qh2(
                imageUrl = transaction.from.bank.imageUrl,
                contentDescription = "upiBankIcon"
            )
            4.sy()
            /*Icon(
                painter = painterResource(id = R.drawable.ic_transactionarrow),
                tint = tintColor.value,
                contentDescription = "transactionArrow"
            )*/
            Box(
                Modifier
                    .height((30.6 + 6).dep()),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .height(30.6.dep())
                        .width(2.dep())
                        .dashedBorder(
                            width = 2.dep(),
                            color = tintColor.value,
                            shape = RectangleShape,
                            on = 2.dep(),
                            off = 4.dep()
                        )
                )
                Icon(
                    modifier = Modifier
                        .width(9.dep())
                        .height(6.dep())
                        .align(Alignment.BottomCenter),
                    painter = painterResource(id = R.drawable.down_arrow),
                    contentDescription = "",
                    tint = tintColor.value
                )
            }
            4.5.sy()
            BankSymbolIcon_1a0qh2(
                /*config = BankSymbolIconConfiguration(
                    icon = R.drawable.ic_axis,
                    backgroundColor = Redish
                ), */
                imageUrl = transaction.to.bank.imageUrl,
                contentDescription = "axisBankIcon"
            )
        }
        config.gapBetweenBankIconAndDetailsPart.sx()
        Column(modifier = Modifier.fillMaxWidth()) {
            Column() {
                FontFamilyText(
                    text = stringResource(R.string.from) + ":",
                    color = Bluish,
                    fontSize = 14.sep()
                )
                FontFamilyText(
                    text = transaction.from.name,
                    color = Bluish,
                    fontSize = 14.sep(),
                    fontWeight = FontWeight.Bold
                )
                FontFamilyText(
                    text = transaction.from.mobileNumber,
                    color = DarkBlue,
                    fontSize = 14.sep(),
                    fontWeight = FontWeight.Bold
                )
            }
            config.spaceBetweenToAndFrom.sy()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    FontFamilyText(
                        text = stringResource(R.string.to) + ":",
                        color = DarkBlue,
                        fontSize = 14.sep()
                    )
                    EditIcon_gx6f1w(
                        text = stringResource(R.string.change),
                        icon = R.drawable.ic_repeat,
                        contentDescription = "change icon",
                        onClick = {
                            notifier.notify(DataIds.paymentReviewChangeClick)
                        }
                    )
                }
                FontFamilyText(
                    text = transaction.to.name,
                    color = DarkBlue,
                    fontSize = 14.sep(),
                    fontWeight = FontWeight.Bold
                )
                FontFamilyText(
                    text = "${transaction.to.accountNumber} (${transaction.to.accountType})",
                    color = DarkBlue,
                    fontSize = 14.sep(),
                    fontWeight = FontWeight.Bold
                )
                FontFamilyText(
                    text = transaction.to.bank.name,
                    color = DarkBlue,
                    fontSize = 14.sep(),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun EditIcon_gx6f1w(
    text: String,
    icon: Int,
    onClick: () -> Unit,
    config: EditIconConfiguration = EditIconConfiguration(),
    contentDescription: String
) {
    Row(
        modifier = Modifier
            .semantics { this.contentDescription = contentDescription }
            .clip(
                shape = CircleShape
            )
            .background(
                config.backgroundColor
            )
            .clickable(
                rippleColor = Bluish,
                onClick = {
                    onClick()
                },
                rippleRadius = 100.dep()
            )
            .padding(
                start = config.innerPaddingStart.dep(),
                end = config.innerPaddingEnd.dep(),
                top = config.innerPaddingTop.dep(),
                bottom = config.innerPaddingBottom.dep()
            )
    ) {
        Icon(
            modifier = Modifier.align(Alignment.CenterVertically),
            painter = painterResource(id = icon),
            contentDescription = "edit",
            tint = Bluish
        )
        5.sx()
        FontFamilyText(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = text,
            fontSize = 12.sep(),
            color = Bluish
        )
        10.sx()
    }
}

@Composable
fun BankSymbolIcon_1a0qh2(
    imageUrl: Any,
    config: BankSymbolIconConfiguration = BankSymbolIconConfiguration(),
    contentDescription: String
) {
    Box(
        modifier = Modifier
            .semantics { this.contentDescription = contentDescription }
            .size(config.size.dep())
            .coloredShadow(
                color = config.shadowColor,
                borderRadius = config.borderRadius.dep(),
                blurRadius = config.blurRadius.dep(),
                offsetY = config.shadowOffsetY.dep(),
                offsetX = config.shadowOffsetX.dep(),
                spread = config.shadowSpread
            )
            .clip(config.shape)
//            .background(color = config.backgroundColor)
            .border(
                width = config.borderStroke.dep(),
                color = config.borderColor,
                shape = config.shape
            ),
        contentAlignment = Alignment.Center
    ) {
        /*Icon(
            modifier = Modifier.size(22.dep()),
            painter = painterResource(id = config.icon),
            tint = Color.Unspecified,
            contentDescription = "phoneIcon"
        )*/
        AsyncImage(
            modifier = Modifier
                .padding(config.borderStroke.dep())
                .clip(CircleShape)
                .fillMaxSize(),
            model = imageUrl,
            placeholder = painterResource(id = R.drawable.user_dummy4),
            contentDescription = "leadingIcon"
        )
    }
}

data class EditIconConfiguration(
    val backgroundColor: Color = LightBlue1,
    val innerPaddingStart: Float = 7.92f,
    val innerPaddingEnd: Float = 4f,
    val innerPaddingTop: Float = 4f,
    val innerPaddingBottom: Float = 4f,
    val editIcon: Int = R.drawable.ic_pencil,
    val textColor: Color = Bluish,
    val fontSize: Float = 12f,
    val cornerRadius: Float = 11f
)

data class BankSymbolIconConfiguration(
    val size: Float = 62f,
    val shadowColor: Color = DarkBlueShadow,
    val borderRadius: Float = 50f,
    val blurRadius: Float = 6f,
    val shadowOffsetX: Float = 0f,
    val shadowOffsetY: Float = 3f,
    val shadowSpread: Float = 0f,
    val shape: Shape = CircleShape,
    val backgroundColor: Color = Color.White,
    val borderStroke: Float = 5f,
    val borderColor: Color = Greyish3,
    val icon: Int = R.drawable.ic_upi
)

data class FromToPortionConfiguration(
    val startPadding: Float = 31f,
    val endPadding: Float = 30f,
    val bankIconColumnTopPadding: Float = 6f,
    val gapBetweenBankIconAndDetailsPart: Float = 28f,
    val spaceBetweenToAndFrom: Float = 28f
)

data class AmountPortionConfiguration(
    val startPadding: Float = 30f,
    val endPadding: Float = 30f
)

data class PaymentReviewBottomSheetConfiguration(
    val holderIconTopPadding: Float = 20f,
    val holderBottomPadding: Float = 33f,
    val fromToPortionBottomPadding: Float = 25f,
    val amountSectionBottomPadding: Float = 21f,
    val customTextBoxBottomPadding: Float = 18f,
    val acceptTextBottomPadding: Float = 18f,
    val buttonBottomPadding: Float = 12f
)

// ////////////////////
// ////////////////////
// ////////////////////
// ////////////////////
// ////////////////////
// ////////////////////
// ////////////////////
val LightGrey3 = Color(0xff989898)

data class Category(
    val name: String,
    val color: Long,
//    val icon: Painter,
    val icon: Any,
    val isSelected: Boolean = false,
    val isEditable: Boolean = false,
    val subCategory: String = "Business trip"
)
enum class AccountType{
    Savings,
    Current
}

data class Friend(
    val uid: Int = 0,
    val name: String,
    val mobileNumber: String = "9563376942",
    val accountNumber: String = "",
    val accountType: AccountType = AccountType.Current,
    val imageUrl: String,
    val bank: Bank = Bank(),
    val isSelected: Boolean = false,
    val hasRead: Boolean = false
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

@Composable
fun AmountSection(
    transaction: BillTransaction,
    notifier: NotificationService = notifier(),
    contentDescription: String,
    config: AmountPortionConfiguration = AmountPortionConfiguration()
) {
    val isPaid = remember {
        derivedStateOf { transaction.transactionType == TransactionType.Paid }
    }
    val tintColor = remember {
        derivedStateOf { if (isPaid.value) Pink else Bluish }
    }
    val amountTrailingText = remember {
        derivedStateOf {
            if (isPaid.value) "- " else ""
        }
    }
    Column(
        modifier = Modifier
            .semantics { this.contentDescription = contentDescription }
            .fillMaxWidth()
            .padding(
                start = config.startPadding.dep(),
                end = config.endPadding.dep()
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            FontFamilyText(
                text = stringResource(id = R.string.amount) + ":",
                color = DarkBlue,
                fontSize = 14.sep(),
                letterSpacing = 0.666667.sep(),
                lineHeight = 16.sep()
            )
            EditIcon_gx6f1w(
                text = stringResource(id = R.string.edit),
                icon = R.drawable.edit_icon_2,
                contentDescription = "edit icon",
                onClick = {
                    notifier.notify(DataIds.paymentReviewEditAmountClick)
                }
            )
        }
        6.sy()
        FontFamilyText(
            annotatedString = transaction.willPayReceive
                .amountAnnotatedString(
                    leadingText = amountTrailingText.value,
                    leadingTextTextColor = tintColor.value,
                    leadingTextFontSize = 12f,
                    isLeadingTextEnabled = true,
                    isSpaceBetween = true,
                    currencyTextColor = tintColor.value,
                    currencyFontSize = 12f,
                    wholeNumberTextColor = tintColor.value,
                    wholeNumberFontSize = 16f,
                    wholeNumberFontWeight = FontWeight.Bold,
                    decNumberTextColor = tintColor.value,
                    decNumberFontSize = 16f,
                    decNumberFontWeight = FontWeight.Bold
                ),
            letterSpacing = (-0.333333).sep(),
            lineHeight = 19.sep()
        )
        3.sy()
        FontFamilyText(
            text = transaction.willPayReceive.numberToWords(),
            color = DarkBlue,
            fontSize = 12.sep()
        )
        20.sy()
        FontFamilyText(
            text = stringResource(R.string.payment_method) + ":",
            color = DarkBlue,
            fontSize = 14.sep()
        )
        9.sy()
        FontFamilyText(
            text = transaction.paymentMethod,
            color = DarkBlue,
            fontSize = 14.sep(),
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun FromToPortion(
    transaction: BillTransaction,
    notifier: NotificationService = notifier(),
    contentDescription: String,
    config: FromToPortionConfiguration = FromToPortionConfiguration()
) {
    val isPaid = remember {
        derivedStateOf { transaction.transactionType == TransactionType.Paid }
    }
    val tintColor = remember {
        derivedStateOf { if (isPaid.value) Pink else Bluish }
    }
    Row(
        modifier = Modifier
            .semantics { this.contentDescription = contentDescription }
            .padding(
                start = config.startPadding.dep(),
                end = config.endPadding.dep()
            )
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(top = config.bankIconColumnTopPadding.dep()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BankSymbolIcon_1a0qh2(
                imageUrl = transaction.from.bank.imageUrl,
                contentDescription = "upiBankIcon"
            )
            4.sy()
            /*Icon(
                painter = painterResource(id = R.drawable.ic_transactionarrow),
                tint = tintColor.value,
                contentDescription = "transactionArrow"
            )*/
            Box(
                Modifier
                    .height((30.6 + 6).dep()),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .height(30.6.dep())
                        .width(2.dep())
                        .dashedBorder(
                            width = 2.dep(),
                            color = tintColor.value,
                            shape = RectangleShape,
                            on = 2.dep(),
                            off = 4.dep()
                        )
                )
                Icon(
                    modifier = Modifier
                        .width(9.dep())
                        .height(6.dep())
                        .align(Alignment.BottomCenter),
                    painter = painterResource(id = R.drawable.down_arrow),
                    contentDescription = "",
                    tint = tintColor.value
                )
            }
            4.5.sy()
            BankSymbolIcon_1a0qh2(
                /*config = BankSymbolIconConfiguration(
                    icon = R.drawable.ic_axis,
                    backgroundColor = Redish
                ), */
                imageUrl = transaction.to.bank.imageUrl,
                contentDescription = "axisBankIcon"
            )
        }
        config.gapBetweenBankIconAndDetailsPart.sx()
        Column(modifier = Modifier.fillMaxWidth()) {
            Column() {
                FontFamilyText(
                    text = stringResource(R.string.from) + ":",
                    color = Bluish,
                    fontSize = 14.sep()
                )
                FontFamilyText(
                    text = transaction.from.name,
                    color = Bluish,
                    fontSize = 14.sep(),
                    fontWeight = FontWeight.Bold
                )
                FontFamilyText(
                    text = transaction.from.mobileNumber,
                    color = DarkBlue,
                    fontSize = 14.sep(),
                    fontWeight = FontWeight.Bold
                )
            }
            config.spaceBetweenToAndFrom.sy()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    FontFamilyText(
                        text = stringResource(R.string.to) + ":",
                        color = DarkBlue,
                        fontSize = 14.sep()
                    )
                    EditIcon_gx6f1w(
                        text = stringResource(R.string.change),
                        icon = R.drawable.ic_repeat,
                        contentDescription = "change icon",
                        onClick = {
                            notifier.notify(DataIds.paymentReviewChangeClick)
                        }
                    )
                }
                FontFamilyText(
                    text = transaction.to.name,
                    color = DarkBlue,
                    fontSize = 14.sep(),
                    fontWeight = FontWeight.Bold
                )
                FontFamilyText(
                    text = "${transaction.to.accountNumber} (${transaction.to.accountType})",
                    color = DarkBlue,
                    fontSize = 14.sep(),
                    fontWeight = FontWeight.Bold
                )
                FontFamilyText(
                    text = transaction.to.bank.name,
                    color = DarkBlue,
                    fontSize = 14.sep(),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

val LightBlue1 = Color(0xffEDF5FF)

@Composable
fun Number.numberToWords(): String {
    val units = arrayOf(
        "", "One", "Two", "Three", "Four",
        "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve",
        "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen",
        "Eighteen", "Nineteen"
    )

    val tens = arrayOf(
        "", // 0
        "", // 1
        "Twenty", // 2
        "Thirty", // 3
        "Forty", // 4
        "Fifty", // 5
        "Sixty", // 6
        "Seventy", // 7
        "Eighty", // 8
        "Ninety" // 9
    )

    fun convert(n: Int): String {
        if (n < 0) {
            return "Minus " + convert(-n)
        }
        if (n < 20) {
            return units[n]
        }
        if (n < 100) {
            return tens[n / 10] + (if (n % 10 != 0) " " else "") + units[n % 10]
        }
        if (n < 1000) {
            return units[n / 100] + " Hundred" + (if (n % 100 != 0) " " else "") + convert(n % 100)
        }
        if (n < 100000) {
            return convert(n / 1000) + " Thousand" + (if (n % 10000 != 0) " " else "") + convert(
                n % 1000
            )
        }
        return if (n < 10000000) {
            convert(n / 100000) + " Lakh" + (if (n % 100000 != 0) " " else "") + convert(n % 100000)
        } else convert(n / 10000000) + " Crore" + (if (n % 10000000 != 0) " " else "") + convert(
            n % 10000000
        )
    }

    // TODO: search better approach for .replace("Indian ","")
    return convert(this@numberToWords.toInt()) + " " + "Rupees"
}
