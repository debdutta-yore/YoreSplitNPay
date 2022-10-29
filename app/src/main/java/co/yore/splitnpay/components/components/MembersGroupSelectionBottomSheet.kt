package co.yore.splitnpay.components.components

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import co.yore.splitnpay.R
import co.yore.splitnpay.components.SingleItem
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.models.DataIds
import co.yore.splitnpay.models.Item
import kotlinx.coroutines.CoroutineScope

class SplitAsChoiceBottomSheetModel(val callback: Callback) : BottomSheetModel {
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
        SplitAsChoiceBottomSheet()
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
fun SplitAsChoiceBottomSheet(
    notifier: NotificationService = notifier()
) {

    val itemList = remember {
        listOf(
            Item(id = 0, R.drawable.ic_splitasgroup, "Split as Group"),
            Item(id = 1, R.drawable.ic_splitnongroup, "Split as Non-Group")
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
                                    notifier.notify(DataIds.cameraOrGallery, index)
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
