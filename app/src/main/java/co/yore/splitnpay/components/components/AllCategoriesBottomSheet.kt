package co.yore.splitnpay.components.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import co.yore.splitnpay.R
import co.yore.splitnpay.addmembers.FontFamilyText
import co.yore.splitnpay.demos.sx
import co.yore.splitnpay.demos.sy
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.models.Category
import co.yore.splitnpay.models.DataIds
import co.yore.splitnpay.pages.CustomButton_3egxtx
import co.yore.splitnpay.pages.LightBlue1
import co.yore.splitnpay.ui.theme.Bluish
import co.yore.splitnpay.ui.theme.DarkBlue

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AllCategoriesBottomSheet(
    categories: List<Category> = listState(key = DataIds.allCategories),
    isAddCategoryEnabled: Boolean = boolState(key = DataIds.isAddCategoryEnabled).value,
    addCategoryName: String = stringState(key = DataIds.addCategoryName).value,
//    sheetState: ModalBottomSheetState,
//    coroutineScope: CoroutineScope
    notifier: NotificationService = notifier()
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        20.sy()
        Icon(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            tint = Color.Unspecified,
            painter = painterResource(id = R.drawable.ic_sheet_holder),
            contentDescription = "sheet holder"
        )
        22.sy()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dep())
                .background(color = LightBlue1)
                .padding(start = 28.dep()),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_category),
                    tint = Color.Unspecified,
                    contentDescription = "category"
                )
                14.sx()
                FontFamilyText(
                    text = "Categories",
                    color = DarkBlue,
                    fontSize = 14.sep(),
                    fontWeight = FontWeight.Bold
                )
            }
            Box(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = 5.dep(),
                            bottomStart = 5.dep()
                        )
                    )
                    .width((21 / 2).dep())
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
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 65.dep(),
                    end = 58.dep(),
                    top = 20.dep()
                ),
            columns = GridCells.Fixed(4),
            verticalArrangement = Arrangement.spacedBy(15.dep()),
//            horizontalArrangement = Arrangement.spacedBy(19.dep())
        ) {
            itemsIndexed(categories) { index, item ->
                CategoryItem(
                    category = item,
                    onClick = {
                        notifier.notify(DataIds.categoryEditSelectionClick, index)
                    },
                    onLongTap = {
                        notifier.notify(DataIds.categoryEditSelectionLongClick, index)
                    },
                    contentDescription = "",
                    selectorItemContentDescription = ""
                )
            }
            item {
                Box(
                    modifier = Modifier
                        .height(70.dep()),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(45.dep())
                            .background(
                                color = if (isAddCategoryEnabled) Bluish else Color.White,
                                shape = CircleShape
                            )
                            .border(
                                width = 1.dep(),
                                color = Bluish,
                                shape = CircleShape
                            )
                            .clip(CircleShape)
                            .clickable {
                                notifier.notify(DataIds.isAddCategoryEnabled)
                            }
                    ) {
                        Icon(
                            modifier = Modifier
                                .align(Alignment.Center),
                            painter = painterResource(id = R.drawable.ic_plus),
                            contentDescription = "Plus icon",
                            tint = if (isAddCategoryEnabled) Color.White else Bluish
                        )
                    }
                }
            }
        }

        15.sy()
        var visible by remember {
            mutableStateOf(false)
        }
        val alpha by remember(isAddCategoryEnabled) {
            derivedStateOf {
                if(isAddCategoryEnabled){
                    visible = true
                    1f
                }
                else{
                    0f
                }
            }
        }
        val animatedAlpha by animateFloatAsState(
            targetValue = alpha,
            animationSpec = tween(700),
            finishedListener = {
                if(it==0f){
                    visible = false
                }
            }
        )
        Box(
            modifier = Modifier
                .padding(
                    top = 10.dep(),
                    start = 31.dep(),
                    end = 31.dep()
                )
                .background(
                    color = LightGrey2,
                    shape = RoundedCornerShape(8.dep())
                )
                .clip(RoundedCornerShape(8.dep()))
                .height((animatedAlpha * 52).dep())
                .alpha(animatedAlpha)
                .fillMaxWidth()
        ) {
            if(visible){
                CustomTextField_wangst(
                    text = addCategoryName,
                    change = {
                        notifier.notify(DataIds.addCategoryName, it)
                    },
                    contentDescription = "",
                    leadingIcon = painterResource(id = R.drawable.ic_description),
                    placeHolderText = "Custom category name"
                )
            }
        }
        /*AnimatedVisibility(
            visible = isAddCategoryEnabled,
            enter = fadeIn(tween(700)) + slideInVertically(tween(700)) {
                it
            },
            exit = fadeOut(tween(700)) + slideOutVertically(tween(700)) {
                it
            }
        ) {
            Box(
                modifier = Modifier
                    .padding(
                        top = 10.dep(),
                        start = 31.dep(),
                        end = 31.dep()
                    )
                    .background(
                        color = LightGrey2,
                        shape = RoundedCornerShape(8.dep())
                    )
                    .clip(RoundedCornerShape(8.dep()))
                    .height(52.dep())
                    .fillMaxWidth()
            ) {
                CustomTextField_wangst(
                    text = addCategoryName,
                    change = {
                        notifier.notify(DataIds.addCategoryName, it)
                    },
                    contentDescription = "",
                    leadingIcon = painterResource(id = R.drawable.ic_description),
                    placeHolderText = "Custom category name"
                )
            }
        }*/

        Box(
            modifier = Modifier
                .padding(
                    top = 14.dep(),
                    start = 30.dep(),
                    end = 30.dep(),
                    bottom = 12.dep()
                )
                .fillMaxWidth()
                .height(47.dep())
        ) {
            CustomButton_3egxtx(
                text = stringResource(id = R.string.continue1),
                onClick = {
//                    coroutineScope.launch {
//                        sheetState.show()
//                    }
                },
                contentDescription = "Continue button"
            )
        }
    }
}