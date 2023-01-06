package co.yore.splitnpay.pages.subpages

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yore.splitnpay.R
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.libs.jerokit.*
import co.yore.splitnpay.libs.jerokit.bottom_sheet.BottomSheetModel
import co.yore.splitnpay.models.DataIds
import co.yore.splitnpay.models.Item
import co.yore.splitnpay.ui.theme.CloudBurst
import co.yore.splitnpay.ui.theme.CuriousBlue
import co.yore.splitnpay.ui.theme.Zumthor
import kotlinx.coroutines.CoroutineScope

class CalculationMethodSelectionBottomSheetModel(val callback: Callback) : BottomSheetModel {
    interface Callback{
        fun scope(): CoroutineScope
        fun onContinue(arg: Any?)
        fun close()
    }
    private val _resolver = Resolver()
    private val _notifier = NotificationService{id, arg ->
        when (id){
            DataIds.cameraOrGallery -> {
                callback.onContinue(arg)
            }
        }
    }

    // ////////////////
    override val resolver = _resolver
    override val notifier = _notifier
    override val scope get() = callback.scope()

    @Composable
    override fun Content() {
        CalculationMethodSelectionBottomSheet()
    }

    override fun initialize() {

    }

    override fun clear() {

    }

    override fun onBack() {
        callback.close()
    }
    // ///////////////////////
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CalculationMethodSelectionBottomSheet(
    notifier: NotificationService = notifier()
) {

    val itemList = remember {
        listOf(
            Item(id = 0, R.drawable.ic_proportionate, "Proportionate"),
            Item(id = 1, R.drawable.ic_gallery_blue, "Optimal")
        )
    }

    val listState = rememberLazyListState()
    var selectedIndex by remember { mutableStateOf(-1) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height((231f - 46f).dep()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            modifier = Modifier
                .padding(top = 20f.dep()),
            tint = Color.Unspecified,
            painter = painterResource(id = R.drawable.ic_sheet_holder),
            contentDescription = "sheet holder"
        )

        LazyColumn(
            state = listState,
            modifier = Modifier
                .padding(top = 24f.dep())
                .fillMaxWidth()
        )
        {
            itemsIndexed(items = itemList)
            { index, _ ->

                SingleItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onPress = {
                                    selectedIndex = itemList[index].id
                                },
                                onTap = {
                                    notifier.notify(DataIds.cameraOrGallery, itemList[index].name)
                                },
                                onDoubleTap = { },
                                onLongPress = { }
                            )
                        },
                    icon = painterResource(itemList[index].icon),
                    text = itemList[index].name,
                    isSelected = itemList[index].id == selectedIndex
                )
            }
        }
    }
}
