package co.yore.splitnpay.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yore.splitnpay.R
import co.yore.splitnpay.libs.NotificationService
import co.yore.splitnpay.libs.dep
import co.yore.splitnpay.libs.notifier

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PhotoSelectionBottomSheet(
    notifier: NotificationService = notifier()
) {

    val itemList = listOf(
        Item(id = 0, painterResource(R.drawable.ic_camera_blue), "Camera"),
        Item(id = 1, painterResource(id = R.drawable.ic_gallery_blue), "Gallery")
    )

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
                        .selectable(
                            selected = itemList[index].id == selectedIndex,
                            onClick = {
                                notifier.notify("camera_or_gallery",itemList[index].name)
                            }
                        )
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onPress = {
                                    selectedIndex = if (selectedIndex != itemList[index].id)
                                        itemList[index].id else -1
                                },
                                onTap = {

                                },
                                onDoubleTap = { },
                                onLongPress = { }
                            )
                        },
                    icon = itemList[index].icon,
                    text = itemList[index].name,
                    isSelected = itemList[index].id == selectedIndex
                )
            }
        }
    }
}


@Composable
private fun SingleItem(
    modifier: Modifier = Modifier,
    icon: Painter, text: String,
    isSelected: Boolean = false
) {
    Row(
        modifier = modifier
            .background(
                if (isSelected)
                    Color(237, 245, 255) else Color.White
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
                tint = Color(26, 121, 229),
                painter = icon, contentDescription = "selected photo icon"
            )
            Spacer(modifier = Modifier.width(15f.dep()))
            Text(
                text = text,
                color = Color(36, 50, 87),
                fontSize = 14.sp,
                fontWeight = FontWeight(700)
            )
        }

        if (isSelected) {
            Icon(
                modifier = Modifier
                //  .width(18f.dep())
                ,
                tint = Color(26, 121, 229),
                painter = painterResource(id = R.drawable.ic_sheet_selector),
                contentDescription = "holder"
            )
        }


    }

}

data class Item(val id: Int, val icon: Painter, val name: String)