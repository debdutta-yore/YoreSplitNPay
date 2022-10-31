package co.yore.splitnpay.pages.subpages

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import co.yore.splitnpay.R
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.libs.jerokit.*
import co.yore.splitnpay.libs.jerokit.bottom_sheet.BottomSheetModel
import co.yore.splitnpay.models.DataIds
import co.yore.splitnpay.models.SheetItem
import kotlinx.coroutines.CoroutineScope

class TimeFilterBottomSheetModel(val callback: Callback) : BottomSheetModel {
    interface Callback{
        fun scope(): CoroutineScope
        fun close()
        fun onOptionSelected(arg: Any?)
    }

    // ///////////////
    private val _resolver = Resolver()
    private val _notifier =
        NotificationService { id, arg ->
            when (id) {
                DataIds.timeFilter -> {
                    callback.onOptionSelected(arg)
                }
            }
        }

    // ///////////////
    override val resolver = _resolver
    override val notifier = _notifier
    override val scope get() = callback.scope()

    @Composable
    override fun Content() {
        TimeFilterSheet()
    }

    override fun initialize() {

    }

    override fun clear() {

    }

    override fun onBack() {

    }

    // //////////
    // data
    // //////////
    init {
        _resolver.addAll()
    }
}

@Composable
fun TimeFilterSheet(
    timeFilterItems: List<SheetItem> = listState(
        key = DataIds.timeFilterItems
    ),
    notifier: NotificationService = notifier()
) {
    val itemList = remember {
        listOf(
            SheetItem(id = 0, R.drawable.calendar_1, "All Time"),
            SheetItem(id = 1, R.drawable.last_month, "Last Month"),
            SheetItem(id = 2, R.drawable.this_month, "This Month"),
            SheetItem(id = 3, R.drawable.custom_month, "Custom Month")
        )
    }

    val listState = rememberLazyListState()
    var selectedIndex by remember { mutableStateOf(-1) }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        // .height((231f - 46f).dep())
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
                .maxHeightFactor(0.7f)
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
                                    notifier.notify(DataIds.timeFilter, index)
                                },
                                onDoubleTap = { },
                                onLongPress = { }
                            )
                        },
                    icon = painterResource(itemList[index].icon),
                    text = itemList[index].iconName,
                    isSelected = itemList[index].id == selectedIndex
                )
            }
        }
        32.sy()
    }
}
