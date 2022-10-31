package co.yore.splitnpay.pages.subpages

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
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
import androidx.compose.ui.unit.dp
import co.yore.splitnpay.R
import co.yore.splitnpay.components.components.CustomButton_3egxtx
import co.yore.splitnpay.components.components.CustomTextField_wangst
import co.yore.splitnpay.components.components.FontFamilyText
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.libs.jerokit.*
import co.yore.splitnpay.libs.jerokit.bottom_sheet.BottomSheetModel
import co.yore.splitnpay.models.Category
import co.yore.splitnpay.models.DataIds
import co.yore.splitnpay.ui.theme.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AllCategoriesBottomSheetModel(
    val callback: Callback
) : BottomSheetModel {
    interface Callback{
        suspend fun getCategories(): List<Category>
        fun onCategoryAddContinue(name: String)
        fun onCategorySelectedContinue(category: Category)
        fun scope(): CoroutineScope
    }
    private val _resolver = Resolver()
    private val _notifier = NotificationService { id, arg ->
        when (id){
            DataIds.categoryEditSelectionClick -> {
                for (i in 0 until _allCategories.size){
                    _allCategories[i] =
                        _allCategories[i].copy(isSelected = false)
                }
                val index = arg as? Int ?: return@NotificationService
                _allCategories[index] =
                    _allCategories[index].copy(isSelected = true)
                updateCanProceed()
            }
            DataIds.isAddCategoryEnabled -> {
                _addCategoryName.value = ""
                _isAddCategoryEnabled.value = !_isAddCategoryEnabled.value
                updateCanProceed()
            }
            DataIds.addCategoryName -> {
                _addCategoryName.value = arg as? String ?: return@NotificationService
                updateCanProceed()
            }
            DataIds.allCategoriesContinue -> {
                if (_isAddCategoryEnabled.value){
                    callback.onCategoryAddContinue(_addCategoryName.value)
                } else {
                    callback.onCategorySelectedContinue(_allCategories.first { it.isSelected })
                }
            }
        }
    }

    private fun updateCanProceed(){
        if (_isAddCategoryEnabled.value){
            canProceedWithCategory.value = _addCategoryName.value.isNotEmpty()
        } else {
            canProceedWithCategory.value = _allCategories.count { it.isSelected } == 1
        }
    }

    private val _allCategories = mutableStateListOf<Category>()
    private val _isAddCategoryEnabled = mutableStateOf(false)
    private val _addCategoryName = mutableStateOf("")
    private val canProceedWithCategory = mutableStateOf(false)

    override val resolver = _resolver
    override val notifier = _notifier
    override val scope get() = callback.scope()

    @Composable
    override fun Content() {
        AllCategoriesBottomSheet()
    }

    override fun initialize() {
        clear()
        scope.launch(Dispatchers.IO) {
            val categories = callback.getCategories()
            withContext(Dispatchers.Main){
                _allCategories.addAll(categories)
            }
            updateCanProceed()
        }
    }

    override fun clear() {
        _allCategories.clear()
        _isAddCategoryEnabled.value = false
        _addCategoryName.value = ""
        canProceedWithCategory.value = false
    }

    override fun onBack() {

    }

    init {
        _resolver.addAll(
            DataIds.allCategories to _allCategories,
            DataIds.isAddCategoryEnabled to _isAddCategoryEnabled,
            DataIds.addCategoryName to _addCategoryName,
            DataIds.canProceedWithCategory to canProceedWithCategory
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AllCategoriesBottomSheet(
    categories: List<Category> = listState(key = DataIds.allCategories),
    isAddCategoryEnabled: Boolean = boolState(key = DataIds.isAddCategoryEnabled).value,
    addCategoryName: String = stringState(key = DataIds.addCategoryName).value,
    notifier: NotificationService = notifier(),
    canProceed: Boolean = boolState(key = DataIds.canProceedWithCategory).value
) {
    Column(
        modifier = Modifier
            .animateContentSize()
            .ModalBottomSheetAdjustWithIme()
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
                .background(color = Zumthor)
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
                    color = CloudBurst,
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
                        color = CuriousBlue,
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
                .heightIn(max = 420.dp)
                .padding(
                    start = 65.dep(),
                    end = 58.dep(),
                    top = 20.dep()
                ),
            columns = GridCells.Fixed(4),
            verticalArrangement = Arrangement.spacedBy(15.dep())
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
                    val color by remember(isAddCategoryEnabled) {
                        derivedStateOf {
                            if (isAddCategoryEnabled){
                                CuriousBlue
                            } else {
                                Color.White
                            }
                        }
                    }
                    val animatedColor by animateColorAsState(targetValue = color)
                    Box(
                        modifier = Modifier
                            .size(45.dep())
                            .background(
                                color = animatedColor,
                                shape = CircleShape
                            )
                            .border(
                                width = 1.dep(),
                                color = CuriousBlue,
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
                            tint = if (isAddCategoryEnabled) Color.White else CuriousBlue
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
                if (isAddCategoryEnabled){
                    visible = true
                    1f
                } else {
                    0f
                }
            }
        }
        val animatedAlpha by animateFloatAsState(
            targetValue = alpha,
            animationSpec = tween(700),
            finishedListener = {
                if (it == 0f){
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
                    color = Alabaster,
                    shape = RoundedCornerShape(8.dep())
                )
                .clip(RoundedCornerShape(8.dep()))
                .height((animatedAlpha * 52).dep())
                .alpha(animatedAlpha)
                .fillMaxWidth()
        ) {
            if (visible){
                CustomTextField_wangst(
                    text = addCategoryName,
                    change = {
                        notifier.notify(DataIds.addCategoryName, it)
                    },
                    contentDescription = "",
                    leadingIcon = R.drawable.ic_description,
                    placeHolderText = "Custom category name",
                    iconTint = DoveGray
                )
            }
        }

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
                    notifier.notify(DataIds.allCategoriesContinue)
                },
                contentDescription = "Continue button",
                enabled = canProceed
            )
        }
    }
}
