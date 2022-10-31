package co.yore.splitnpay.pages.subpages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import co.yore.splitnpay.R
import co.yore.splitnpay.components.components.CustomButton_3egxtx
import co.yore.splitnpay.components.components.FontFamilyText
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.libs.jerokit.*
import co.yore.splitnpay.libs.jerokit.bottom_sheet.BottomSheetModel
import co.yore.splitnpay.models.DataIds
import co.yore.splitnpay.ui.theme.Alabaster
import co.yore.splitnpay.ui.theme.CloudBurst
import co.yore.splitnpay.ui.theme.CuriousBlue
import kotlinx.coroutines.CoroutineScope

class DeleteAlertSheetModel(val callback: Callback) : BottomSheetModel {
    interface Callback{
        fun scope(): CoroutineScope
        fun message(): String
        fun onDelete()
        fun onCancel()
        fun close()
    }

    // ///////////////
    private val _resolver = Resolver()
    private val _notifier = NotificationService{id, arg ->
        when (id){
            DataIds.delete -> {
                callback.onDelete()
            }
            DataIds.cancel -> {
                callback.onCancel()
            }
        }
    }

    // ///////////////
    override val resolver = _resolver
    override val notifier = _notifier
    override val scope get() = callback.scope()

    @Composable
    override fun Content() {
        DeleteAlertSheet()
    }

    override fun initialize() {
        _message.value = callback.message()
    }

    override fun clear() {

    }

    override fun onBack() {
        callback.close()
    }

    // //////////////
    private val _message = mutableStateOf("")

    // //////////////
    init {
        _resolver.addAll(
            DataIds.message to _message
        )
    }
}

@Composable
fun DeleteAlertSheet(
    message: String = stringState(key = DataIds.message).value,
    notifier: NotificationService = notifier()
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dep())
    ) {
        Icon(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 9.dep()),
            tint = Color.Unspecified,
            painter = painterResource(id = R.drawable.ic_sheet_holder),
            contentDescription = "sheet holder"
        )
        28.sy()
        Box(
            modifier = Modifier
                .align(CenterHorizontally)
                .clip(CircleShape)
                .size(80.dep())
                .background(Alabaster, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_delete_group),
                contentDescription = "delete_group",
                tint = Color.Unspecified
            )
        }
        11.sy()
        FontFamilyText(
            modifier = Modifier
                .fillMaxWidth()
                .align(CenterHorizontally),
            text = message,
            color = CloudBurst,
            fontSize = 16.sep(),
            textAlign = TextAlign.Center
        )
        24.sy()
        Box(
            modifier = Modifier
                .height(47.dep())
                .fillMaxWidth()
        ) {
            CustomButton_3egxtx(
                text = stringResource(id = R.string.delete),
                onClick = {
                    notifier.notify(DataIds.delete)
                },
                contentDescription = "Delete Button"
            )
        }
        25.sy()
        FontFamilyText(
            modifier = Modifier
                .align(CenterHorizontally)
                .clickable(
                    onClick = {
                        notifier.notify(DataIds.cancel)
                    }
                ),
            text = stringResource(id = R.string.cancel),
            color = CuriousBlue,
            fontSize = 16.sep()
        )
        20.sy()
    }
}
