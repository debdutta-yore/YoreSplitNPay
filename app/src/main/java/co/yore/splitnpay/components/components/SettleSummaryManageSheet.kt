package co.yore.splitnpay.components.components

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
import co.yore.splitnpay.libs.BottomSheetModel
import co.yore.splitnpay.libs.NotificationService
import co.yore.splitnpay.libs.Resolver
import co.yore.splitnpay.libs.dep
import co.yore.splitnpay.models.DataIds
import kotlinx.coroutines.CoroutineScope


class SettleSummaryManageBottomSheetModel(val callback: Callback): BottomSheetModel {
    interface Callback{
        fun scope(): CoroutineScope
        fun onContinue(arg: Any?)
    }
    private val _resolver = Resolver()
    private val _notifier = NotificationService{id,arg->
        when(id){
            DataIds.settleSummaryManage->{
                callback.onContinue(arg)
            }
        }
    }
    //////////////////
    override val resolver = _resolver
    override val notifier = _notifier
    override val scope get() = callback.scope()
    @Composable
    override fun Content() {
        SettleSummaryManageSheet()
    }

    override fun initialize() {

    }

    override fun clear() {

    }

    override fun onBack() {

    }
    /////////////////////////
}

@Composable
fun SettleSummaryManageSheet(
    notifier: NotificationService = co.yore.splitnpay.libs.notifier()
) {
    val itemList = remember {
        listOf(
            co.yore.splitnpay.models.Item(id = 0, R.drawable.rupee, "Settle"),
            co.yore.splitnpay.models.Item(id = 1, R.drawable.summary, "Summary"),
            co.yore.splitnpay.models.Item(id = 2, R.drawable.manage, "Manage")
        )
    }

    val listState = rememberLazyListState()
    var selectedIndex by remember { mutableStateOf(-1) }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            modifier = Modifier
                .padding(top = 21f.dep()),
            tint = Color.Unspecified,
            painter = painterResource(id = R.drawable.ic_sheet_holder),
            contentDescription = "sheet holder"
        )

        LazyColumn(
            state = listState,
            modifier = Modifier
                .padding(top = 24f.dep())
                .fillMaxWidth(),
            contentPadding = PaddingValues(bottom = 20.dep())
        )
        {
            itemsIndexed(items = itemList)
            { index, _ ->

                co.yore.splitnpay.components.SingleItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onPress = {
                                    selectedIndex = itemList[index].id
                                },
                                onTap = {
                                    notifier.notify(DataIds.settleSummaryManage, itemList[index].name)
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