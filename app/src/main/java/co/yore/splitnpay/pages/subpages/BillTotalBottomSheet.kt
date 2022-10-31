package co.yore.splitnpay.pages.subpages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import co.yore.splitnpay.R
import co.yore.splitnpay.components.components.CustomButton_3egxtx
import co.yore.splitnpay.components.components.CustomTextField_wangst
import co.yore.splitnpay.components.components.FontFamilyText
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.libs.jerokit.*
import co.yore.splitnpay.libs.jerokit.bottom_sheet.BottomSheetModel
import co.yore.splitnpay.models.DataIds
import co.yore.splitnpay.ui.theme.Alabaster
import co.yore.splitnpay.ui.theme.CloudBurst
import co.yore.splitnpay.ui.theme.DoveGray
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BillTotalBottomSheetModel(
    val callback: Callback
) : BottomSheetModel {
    interface Callback{
        suspend fun getBillTotalAmount(): String
        suspend fun getDescription(): String
        fun close()
        fun onContinue(
            billTotal: String,
            billDescription: String
        )

        fun scope(): CoroutineScope
    }
    private val _resolver = Resolver()
    private val _notifier = NotificationService{id, arg ->
        when (id){
            DataIds.billTotalAmount -> {
                _billTotalAmount.value = arg as? String ?: return@NotificationService
                updateCanProceed()
            }
            DataIds.billTotalDescription -> {
                _billTotalDescription.value = arg as? String ?: return@NotificationService
            }
            DataIds.canProceedWithBillTotal -> {
                callback.onContinue(
                    _billTotalAmount.value,
                    _billTotalDescription.value
                )
            }
        }
    }

    private fun updateCanProceed(){
        canProceedWithBillTotal.value = _billTotalAmount.value.isNotEmpty()
    }
    override val resolver get() = _resolver
    override val notifier get() = _notifier
    override val scope get() = callback.scope()

    @Composable
    override fun Content() {
        BillTotalBottomSheet()
    }

    // //////////////
    override fun initialize(){
        clear()
        scope.launch(Dispatchers.IO) {
            _billTotalAmount.value = callback.getBillTotalAmount()
            _billTotalDescription.value = callback.getDescription()
            updateCanProceed()
        }
    }

    override fun clear() {
        _billTotalAmount.value = ""
        _billTotalDescription.value = ""
        canProceedWithBillTotal.value = false
    }

    override fun onBack() {
        clear()
        callback.close()
    }

    // /////////////
    private val _billTotalAmount = mutableStateOf("")
    private val _billTotalDescription = mutableStateOf("")
    private val canProceedWithBillTotal = mutableStateOf(false)

    init {
        _resolver.addAll(
            DataIds.billTotalAmount to _billTotalAmount,
            DataIds.billTotalDescription to _billTotalDescription,
            DataIds.canProceedWithBillTotal to canProceedWithBillTotal
        )
    }
}

@Composable
fun BillTotalBottomSheet(
    billTotalAmount: String = stringState(key = DataIds.billTotalAmount).value,
    billTotalDescription: String = stringState(key = DataIds.billTotalDescription).value,
    canProceed: Boolean = boolState(key = DataIds.canProceedWithBillTotal).value,
    notifier: NotificationService = notifier()
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 32.dep(),
                end = 30.dep()
            )
    ) {
        Icon(
            modifier = Modifier
                .align(CenterHorizontally)
                .padding(top = 21.dep()),
            tint = Color.Unspecified,
            painter = painterResource(id = R.drawable.ic_sheet_holder),
            contentDescription = "sheet holder"
        )

        23.sy()

        FontFamilyText(
            text = stringResource(R.string.enter_bill_total),
            color = CloudBurst,
            fontSize = 16.sep(),
            fontWeight = FontWeight.Bold
        )

        Box(
            modifier = Modifier
                .padding(top = 17.dep())
                .background(
                    color = Alabaster,
                    shape = RoundedCornerShape(8.dep())
                )
                .clip(RoundedCornerShape(8.dep()))
                .height(52.dep())
                .fillMaxWidth()
        ) {
            CustomTextField_wangst(
                text = billTotalAmount,
                change = {
                    notifier.notify(DataIds.billTotalAmount, it)
                },
                contentDescription = "",
                leadingIcon = R.drawable.ic_rupees,
                placeHolderText = stringResource(R.string.amount),
                singleLine = true,
                maxLines = 1,
                keyboardType = KeyboardType.Number,
                visualTransformation = ThousandsTransformer(),
                textStyle = TextStyle(
                    color = CloudBurst,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sep()
                ),
                interceptor = {
                    it.replace("^0+".toRegex(), "").take(14)
                },
                iconTint = DoveGray
            )
        }

        Box(
            modifier = Modifier
                .padding(top = 17.dep())
                .background(
                    color = Alabaster,
                    shape = RoundedCornerShape(8.dep())
                )
                .clip(RoundedCornerShape(8.dep()))
                .height(52.dep())
                .fillMaxWidth()
        ) {
            CustomTextField_wangst(
                text = billTotalDescription.toString(),
                change = {
                    notifier.notify(DataIds.billTotalDescription, it)
                },
                contentDescription = "",
                leadingIcon = R.drawable.ic_description,
                placeHolderText = stringResource(R.string.add_description),
                singleLine = true,
                maxLines = 1,
                iconTint = DoveGray
            )
        }

        Box(
            modifier = Modifier
                .padding(
                    top = 27.dep(),
                    bottom = 20.dep()
                )
                .height(47.dep())
        ) {
            CustomButton_3egxtx(
                text = stringResource(id = R.string.continue1),
                onClick = {
                    notifier.notify(DataIds.canProceedWithBillTotal)
                },
                contentDescription = "Continue button",
                enabled = canProceed
            )
        }
        Box(
            modifier = Modifier
                .navigationBarsPadding()
                .imePadding()
        ){

        }
    }
}
