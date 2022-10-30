package co.yore.splitnpay.components

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
import co.yore.splitnpay.models.DataIds
import co.yore.splitnpay.models.Item
import co.yore.splitnpay.ui.theme.Bluish
import co.yore.splitnpay.ui.theme.DarkBlue
import co.yore.splitnpay.ui.theme.Lightblue1
import kotlinx.coroutines.CoroutineScope

class PhotoSelectionBottomSheetModel(val callback: Callback): BottomSheetModel{
    interface Callback{
        fun scope(): CoroutineScope
        fun onContinue(arg: Any?)
        fun close()
    }
    private val _resolver = Resolver()
    private val _notifier = NotificationService{id,arg->
        when(id){
            DataIds.cameraOrGallery->{
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
        PhotoSelectionBottomSheet()
    }

    override fun initialize() {

    }

    override fun clear() {

    }

    override fun onBack() {
        callback.close()
    }
    /////////////////////////
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PhotoSelectionBottomSheet(
    notifier: NotificationService = notifier()
) {

    val itemList = remember {
        listOf(
            Item(id = 0, R.drawable.ic_camera_blue, "Camera"),
            Item(id = 1, R.drawable.ic_gallery_blue, "Gallery")
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


@Composable
fun SingleItem(
    modifier: Modifier = Modifier,
    icon: Painter, text: String,
    isSelected: Boolean = false
) {
    Row(
        modifier = modifier
            .background(
                if (isSelected)
                    Lightblue1 else Color.White
            )
            .padding(start = 31f.dep())
            .fillMaxWidth()
            .height(49f.dep())
            ,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                modifier = Modifier.size(18.dp),
                tint = Bluish,
                painter = icon, contentDescription = "selected photo icon"
            )
            Spacer(modifier = Modifier.width(15f.dep()))
            Text(
                text = text,
                color = DarkBlue,
                fontSize = 14.sp,
                fontWeight = FontWeight(700)
            )
        }

        if (isSelected) {
            Box(
                modifier = Modifier
                    .height(49.dep())
                    .width(16.dep())
                    .offset(x = 8.dep())
                    .clip(
                        RoundedCornerShape(
                            topStart = 6.dep(),
                            bottomStart = 6.dep()
                        )
                    )
                    .background(Bluish)
            ){

            }
        }
    }
}