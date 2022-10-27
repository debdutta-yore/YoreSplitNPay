package co.yore.splitnpay.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import co.yore.splitnpay.R
import co.yore.splitnpay.addmembers.FontFamilyText
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.models.DataIds
import co.yore.splitnpay.models.SheetItem
import co.yore.splitnpay.ui.theme.Bluish
import co.yore.splitnpay.ui.theme.DarkBlue
import kotlinx.coroutines.CoroutineScope

class ExpenseFilterBottomSheetModel(val callback: Callback): BottomSheetModel{
    interface Callback{
        fun scope(): CoroutineScope
        fun close()
        fun onOptionSelected(arg: Any?)
    }
    /////////////////
    private val _resolver = Resolver()
    private val _notifier = NotificationService{id,arg->
        when(id){
            DataIds.expenseFilterOption->{
                callback.onOptionSelected(arg)
            }
        }
    }
    /////////////////
    override val resolver = _resolver
    override val notifier = _notifier
    override val scope get() = callback.scope()

    @Composable
    override fun Content() {
        ExpenseFilterBottomSheet()
    }

    override fun initialize() {

    }

    override fun clear() {

    }

    override fun onBack() {

    }
    ////////////
    //data
    ////////////
    init {
        _resolver.addAll(

        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ExpenseFilterBottomSheet(
    notifier: NotificationService = notifier()
) {
    val itemList = listOf(
        SheetItem(id = 0, R.drawable.ic_category, "Expense categories"),
        SheetItem(id = 1, R.drawable.expense_overtime, "Expense Overtime")
    )
    val listState = rememberLazyListState()
    var selectedIndex by remember { mutableStateOf(-1) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height((231f - 46f).dep()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        20.sy()
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50.dep()))
                .height(2.dep())
                .width(19.dep())
                .background(LightBlue5)
                .align(Alignment.CenterHorizontally)
        )
        LazyColumn(
            state = listState,
            modifier = Modifier
                .padding(top = 24f.dep())
                .fillMaxWidth()
        ) {
            itemsIndexed(items = itemList) { index, _ ->
                /*SingleItem1(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = itemList[index].id == selectedIndex,
                            onClick = {

                            }
                        )
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onPress = {
                                    selectedIndex = if (selectedIndex != itemList[index].id)
                                        itemList[index].id else -1
                                },
                                onTap = {
                                    *//*coroutineScope.launch {
                                        sheetState.hide()
                                        onImageSelected()
                                    }*//*
                                },
                                onDoubleTap = { },
                                onLongPress = { }
                            )
                        },
                    icon = painterResource(id = itemList[index].icon),
                    text = itemList[index].iconName,
                    isSelected = itemList[index].id == selectedIndex,
                    onClick = {
                        notifier.notify(DataIds.expenseFilterOption,index)
                    }
                )*/

                co.yore.splitnpay.components.SingleItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onPress = {
                                    selectedIndex = itemList[index].id
                                },
                                onTap = {
                                    notifier.notify(DataIds.expenseFilterOption, index)
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
    }
}

@Composable
fun SingleItem1(
    modifier: Modifier = Modifier,
    icon: Painter, text: String,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .background(
                if (isSelected)
                    LightBlue1
                else
                    Color.White
            )
            .fillMaxWidth()
            .height(49f.dep())
            .clickable(
                rippleRadius = 360.dep(),
                rippleColor = LightBlue1,
                onClick = {
                    onClick()
                }
            )
        ,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .padding(start = 21f.dep()),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(18.dep()),
                tint = Bluish,
                painter = icon,
                contentDescription = "selected photo icon"
            )
            15.sx()
            FontFamilyText(
                text = text,
                color = DarkBlue,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

        if (isSelected) {
            /*Icon(
                tint = Bluish,
                painter = painterResource(id = R.drawable.ic_sheet_selector),
                contentDescription = "holder"
            )*/
            Box(
                modifier = Modifier
                    .width(8.dep())
                    .fillMaxHeight()
                    .background(
                        color = Bluish,
                        shape = RoundedCornerShape(
                            topStart = 5.dep(),
                            bottomStart = 5.dep()
                        )
                    )
            )
        }
    }
}