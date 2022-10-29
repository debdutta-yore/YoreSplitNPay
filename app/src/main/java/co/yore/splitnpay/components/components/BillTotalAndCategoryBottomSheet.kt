package co.yore.splitnpay.components.components

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.TextFieldDefaults.indicatorLine
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import co.yore.splitnpay.R
import co.yore.splitnpay.addmembers.FontFamilyText
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.models.*
import co.yore.splitnpay.models.Category
import co.yore.splitnpay.pages.CustomButton_3egxtx
import co.yore.splitnpay.ui.theme.DarkBlue
import co.yore.splitnpay.ui.theme.LightBlue4
import coil.compose.AsyncImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch



class BillTotalAndCategoryBottomSheetModel(
    val callback: BillTotalBottomSheetModelCallback
): BottomSheetModel{
    interface BillTotalBottomSheetModelCallback{
        suspend fun getCategories(): List<Category>
        suspend fun getBillTotalAmount(): String
        suspend fun getDescription(): String
        fun openAllCategories()
        fun close()
        fun onContinue(
            billTotal: String,
            billDescription: String,
            category: Category
        )

        fun onRenameContinue(
            category: Category,
            name: String
        )

        fun scope(): CoroutineScope
    }
    private val _resolver = Resolver()
    private val _notifier = NotificationService{id,arg->
        when(id){
            DataIds.billTotalAmount->{
                _billTotalAmount.value = arg as? String?:return@NotificationService
                updateCanProceed()
            }
            DataIds.billTotalDescription->{
                _billTotalDescription.value = arg as? String?:return@NotificationService
            }
            DataIds.categorySelectionClick->{
                for(i in 0 until _categories.size){
                    _categories[i] =
                        _categories[i].copy(isSelected = false)
                }
                val index = arg as? Int?:return@NotificationService
                _categories[index] =
                    _categories[index].copy(isSelected = true)
                updateCanProceed()
            }
            DataIds.openAllCategories->{
                callback.openAllCategories()
            }
            DataIds.renamePressed->{
                canProceedWithBillTotal.value = false
                _renamePressed.value = arg as? Int?:return@NotificationService
                val category = _categories[arg as? Int?:return@NotificationService]
                categoryName.value = category.name
                categoryPlaceholder.value = category.name
                categoryImage.value = category.icon
            }
            DataIds.categoryName->{
                categoryName.value = arg as? String?:return@NotificationService
                updateCanProceed()
            }
            DataIds.canProceedWithBillTotal->{
                if(_renamePressed.value==-1){
                    callback.onContinue(
                        _billTotalAmount.value,
                        _billTotalDescription.value,
                        _categories.first { it.isSelected }
                    )
                }
                else{
                    callback.onRenameContinue(_categories.first { it.isSelected }, categoryName.value)
                }
            }
        }
    }

    private fun updateCanProceed(){
        if(_renamePressed.value!=-1){
            canProceedWithBillTotal.value = _categories[_renamePressed.value].name!=categoryName.value
                    && categoryName.value.isNotEmpty()
        }
        else{
            canProceedWithBillTotal.value = _billTotalAmount.value.isNotEmpty()
                    && _categories.count { it.isSelected } == 1
        }
    }
    override val resolver get() = _resolver
    override val notifier get() = _notifier
    override val scope get() = callback.scope()
    @Composable
    override fun Content() {
        BillTotalAndCategoryBottomSheet()
    }

    ////////////////
    override fun initialize(){
        clear()
        scope.launch(Dispatchers.IO) {
            _categories.addAll(callback.getCategories())
            _billTotalAmount.value = callback.getBillTotalAmount()
            _billTotalDescription.value = callback.getDescription()
            updateCanProceed()
        }
    }

    override fun clear() {
        _categories.clear()
        _billTotalAmount.value = ""
        _billTotalDescription.value = ""
        _renamePressed.value = -1
        canProceedWithBillTotal.value = false
        categoryImage.value = null
        categoryName.value = ""
        categoryPlaceholder.value = ""
    }

    override fun onBack() {
        if(_renamePressed.value!=-1){
            _renamePressed.value = -1
            updateCanProceed()
        }
        else{
            clear()
            callback.close()
        }
    }

    ///////////////
    private val _categories = mutableStateListOf<Category>()
    private val _billTotalAmount = mutableStateOf("")
    private val _billTotalDescription = mutableStateOf("")
    private val _renamePressed = mutableStateOf(-1)
    private val canProceedWithBillTotal = mutableStateOf(false)
    private val categoryImage = mutableStateOf<Any?>(null)
    private val categoryName = mutableStateOf<String>("")
    private val categoryPlaceholder = mutableStateOf<String>("")

    init {
        _resolver.addAll(
            DataIds.categories to _categories,
            DataIds.billTotalAmount to _billTotalAmount,
            DataIds.billTotalDescription to _billTotalDescription,
            DataIds.renamePressed to _renamePressed,
            DataIds.canProceedWithBillTotal to canProceedWithBillTotal,
            DataIds.categoryName to categoryName,
            DataIds.categoryPlaceholder to categoryPlaceholder,
            DataIds.categoryImage to categoryImage,
        )
    }
}
fun Modifier.maxHeightFactor(
    factor: Float = 1f
) = composed(
    factory = {
        val screenHeight = LocalConfiguration.current.screenHeightDp
        Modifier
            .heightIn(max = (screenHeight*factor).dp)
    }
)
val LightGrey2 = Color(0xffF8F8F8)
val Grayish = Color(0xffF5F5F5)
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BillTotalAndCategoryBottomSheet(
    categories: List<Category> = listState(key = DataIds.categories),
    billTotalAmount: String = stringState(key = DataIds.billTotalAmount).value,
    billTotalDescription: String = stringState(key = DataIds.billTotalDescription).value,
    renamePressed: Int = intState(key = DataIds.renamePressed).value,
    canProceed: Boolean = boolState(key = DataIds.canProceedWithBillTotal).value,
    categoryName: String = stringState(key = DataIds.categoryName).value,
    categoryPlaceholder: String = stringState(key = DataIds.categoryPlaceholder).value,
    categoryImage: Any? = tState<Any?>(key = DataIds.categoryImage).value,
    notifier: NotificationService = notifier()
) {
    Column(
        modifier = Modifier
            .animateContentSize()
            .ModalBottomSheetAdjustWithIme()
            .fillMaxWidth()
            .padding(
                start = 32.dep(),
                end = 30.dep()
            )
    ) {
        Icon(
            modifier = Modifier
                .align(CenterHorizontally)
                .padding(top = 21.dep()),
            tint = Color.Unspecified,
            painter = painterResource(id = R.drawable.ic_sheet_holder),
            contentDescription = "sheet holder"
        )

        23.sy()

        FontFamilyText(
            text = stringResource(R.string.enter_bill_total),
            color = DarkBlue,
            fontSize = 16.sep(),
            fontWeight = FontWeight.Bold,
        )

        Box(
            modifier = Modifier
                .padding(top = 17.dep())
                .background(
                    color = LightGrey2,
                    shape = RoundedCornerShape(8.dep())
                )
                .clip(RoundedCornerShape(8.dep()))
                .height(52.dep())
                .fillMaxWidth(),
        ) {
            CustomTextField_wangst(
                text = billTotalAmount,
                change = {
                    notifier.notify(DataIds.billTotalAmount, it)
                },
                contentDescription = "",
                leadingIcon = R.drawable.ic_rupees,
                placeHolderText = stringResource(R.string.amount),
                singleLine = true,
                maxLines = 1,
                keyboardType = KeyboardType.Number,
                visualTransformation = ThousandsTransformer(),
                textStyle = TextStyle(
                    color = Color(0xff243257),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sep(),
                ),
                interceptor = {
                    it.replace("^0+".toRegex(),"").take(14)
                },
                iconTint = Color(0xff656565)
            )
        }

        Box(
            modifier = Modifier
                .padding(top = 17.dep())
                .background(
                    color = LightGrey2,
                    shape = RoundedCornerShape(8.dep())
                )
                .clip(RoundedCornerShape(8.dep()))
                .height(52.dep())
                .fillMaxWidth(),
        ) {
            CustomTextField_wangst(
                text = billTotalDescription.toString(),
                change = {
                    notifier.notify(DataIds.billTotalDescription, it)
                },
                contentDescription = "",
                leadingIcon = R.drawable.ic_description,
                placeHolderText = stringResource(R.string.add_description),
                singleLine = true,
                maxLines = 1,
                iconTint = Color(0xff656565)
            )
        }

        FontFamilyText(
            modifier = Modifier.padding(top = 23.dep()),
            text = stringResource(R.string.categories),
            color = DarkBlue,
            fontSize = 16.sep(),
            fontWeight = FontWeight.Bold,
        )

        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max=192.dp)
                .padding(
                    top = 20.dep()
                ),
            columns = GridCells.Fixed(5),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            itemsIndexed(categories.take(4)) { index, item ->
                CategoryItem(
                    category = item,
                    onClick = {
                        if (renamePressed != -1) {
                            notifier.notify(DataIds.renamePressed, index)
                        }
                        notifier.notify(DataIds.categorySelectionClick, index)
                    },
                    onLongTap = {
                        notifier.notify(DataIds.renamePressed, index)
                    },
                    contentDescription = "",
                    selectorItemContentDescription = ""
                )
            }
            item {

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .height(70.dep()),
                        contentAlignment = Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(45.dep())
                                .background(
                                    color = Grayish,
                                    shape = CircleShape
                                )
                                .clip(CircleShape)
                                .clickable {
                                    notifier.notify(DataIds.openAllCategories)
                                }
                        ) {
                            Icon(
                                modifier = Modifier
                                    .align(Center),
                                painter = painterResource(id = R.drawable.ic_next),
                                contentDescription = "Next",
                                tint = Color.Unspecified
                            )
                        }
                    }

                    FontFamilyText(
                        text = stringResource(id = R.string.others),
                        color = DarkBlue,
                        fontSize = 11.sep(),
                    )
                }
            }
        }

        ////////////////
        var visible by remember {
            mutableStateOf(false)
        }
        var height by remember {
            mutableStateOf(0)
        }
        val alpha by remember(renamePressed) {
            derivedStateOf {
                if(renamePressed > -1){
                    visible = true
                    height = 52
                    1f
                }
                else{
                    height = 0
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
                    //start = 31.dep(),
                    //end = 31.dep()
                )
                .background(
                    color = LightGrey2,
                    shape = RoundedCornerShape(8.dep())
                )
                .clip(RoundedCornerShape(8.dep()))
                .animateContentSize(
                    animationSpec = tween(700)
                )
                .height(height/*(animatedAlpha * 52)*/.dep())
                .alpha(animatedAlpha)
                .fillMaxWidth()
        ) {
            if(visible){
                CustomTextField_wangst(
                    text = categoryName,
                    change = {
                        notifier.notify(DataIds.categoryName, it)
                    },
                    contentDescription = "",
                    leadingIcon = categoryImage,
                    placeHolderText = categoryPlaceholder,
                    iconTint = Color(0xff656565)
                )
            }
        }
        ////////////////

        if (renamePressed != -1) {
            12.sy()
        } else {
            27.sy()
        }

        Box(
            modifier = Modifier
                .padding(bottom = 20.dep())
                .height(47.dep())
        ) {
            CustomButton_3egxtx(
                text = stringResource(id = R.string.continue1),
                onClick = {
                    notifier.notify(DataIds.canProceedWithBillTotal)
                },
                contentDescription = "Continue button",
                enabled = canProceed
            )
        }
        /*Box(
            modifier = Modifier
                .navigationBarsPadding()
                .imePadding()
        ){

        }*/
    }
}

@Composable
fun Modifier.keyboardHeight(
    keyboardHeight: Int = co.yore.splitnpay.keyboardHeight()
): Modifier{
    return this
        .then(Modifier.height(with(LocalDensity.current){keyboardHeight.toDp()}))
}
////////////////////////////////
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CustomTextField_wangst(
    text: String,
    change: (String) -> Unit,
    leadingIcon: Any?,
    placeHolderText: String,
    contentDescription: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    maxLines: Int = Int.MAX_VALUE,
    singleLine: Boolean = false,
    textStyle: TextStyle = TextStyle.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interceptor: ((String)->String)? = null,
    config: CustomTextFieldConfiguration = CustomTextFieldConfiguration(),
    iconTint: Color = Color.Unspecified
) {
    val interactionSource = remember { MutableInteractionSource() }
    val colors = TextFieldDefaults.textFieldColors(
        disabledTextColor = config.disabledTextColor,
        backgroundColor = config.backgroundColor,
        focusedIndicatorColor = config.focusedIndicatorColor,
        unfocusedIndicatorColor = config.unfocusedIndicatorColor,
        disabledIndicatorColor = config.disabledIndicatorColor,
        cursorColor = config.cursorColor
    )

    BasicTextField(
        value = text,
        onValueChange = {
            if(interceptor==null){
                change(it)
            }
            else{
                change(interceptor(it))
            }
        },
        textStyle = textStyle,
        modifier = Modifier
            .semantics { this.contentDescription = contentDescription }
            .fillMaxSize()
            .indicatorLine(
                enabled = true,
                isError = false,
                interactionSource = interactionSource,
                colors = colors
            ),
        interactionSource = interactionSource,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
        ),
        maxLines = maxLines,
        singleLine = singleLine,
        visualTransformation = visualTransformation,
    ) { innerTextField ->
        TextFieldDefaults.TextFieldDecorationBox(
            value = text,
            innerTextField = innerTextField,
            interactionSource = interactionSource,
            contentPadding = PaddingValues(
                //start = config.contentStartPadding.dep(),
                //top = config.contentTopPadding.dep(),
                bottom = config.contentBottomPadding.dep()
            ),
            singleLine = true,
            visualTransformation = VisualTransformation.None,
            enabled = true,
            colors = colors,
            placeholder = {
                FontFamilyText(
                    text = placeHolderText,
                    color = config.textColor,
                    fontSize = config.textFontSize.sep(),
                    fontWeight = config.textFontWeight,
                    textAlign = TextAlign.Center
                )
            },
            leadingIcon = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = leadingIcon,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(start = 18.dep())
                            .size(18.33.dep()),
                        contentScale = ContentScale.Fit,
                        colorFilter = ColorFilter.tint(iconTint)
                    )
                    config.dividerStartPadding.sx()
                    Divider(
                        color = config.dividerColor,
                        modifier = Modifier
                            .width(config.dividerWidth.dep())
                            .height(config.dividerHeight.dep())
                    )
                    25.sx()
                }
            }
        )
    }
}
val LightGrey = Color(0xff656565)
val LightGrey4 = Color(0xffBCBCBC)

///////////////////////////////////////
val LightBlue3 = Color(0xffCFD8E4)

@Composable
fun SelectorIcon_ulkel8(
    config: SelectorIconConfiguration = SelectorIconConfiguration(),
    contentDescription: String
) {
    Box(
        modifier = Modifier
            .size(config.selectorSize.dep())
            .aspectRatio(1f, matchHeightConstraintsFirst = true)
            .border(
                width = config.selectorBorderStroke.dep(),
                color = config.selectorBorderColor,
                shape = CircleShape
            )
            .clip(CircleShape)
            .background(color = config.backgroundColor)
            .semantics { this.contentDescription = contentDescription }
    ) {
        Icon(
            modifier = Modifier
                .align(Alignment.Center)
                .size(config.selectorIconSize.dep()),
            tint = config.selectorIconTint,
            painter = painterResource(id = config.selectorIcon),
            contentDescription = config.selectorIconContentDescription
        )
    }
}
@OptIn(ExperimentalAnimationApi::class, ExperimentalFoundationApi::class)
@Composable
fun CategoryItem(
    category: Category,
    onClick: () -> Unit,
    onLongTap: () -> Unit,
    config: CategoryItemConfiguration = CategoryItemConfiguration(),
    contentDescription: String,
    selectorItemContentDescription: String
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(config.size.dep())
            .semantics { this.contentDescription = contentDescription }
    ) {
        Box(
            modifier = Modifier
                .height(70.dep())
//                .padding(bottom = config.spacerIconText.dep())
            ,
            contentAlignment = Alignment.Center
        ) {
            val interactionSource = remember { MutableInteractionSource() }
            val borderColor by remember(category.isSelected) {
                derivedStateOf {
                    if(category.isSelected){
                        Color(category.color)
                    }
                    else{
                        config.borderColor
                    }
                }
            }
            val animatedBorderColor by animateColorAsState(targetValue = borderColor)
            Box(
                modifier = Modifier
                    .size(config.size.dep())
                    .clip(CircleShape)
                    .border(
                        config.borderStroke.dep(),
                        color = animatedBorderColor,
                        shape = CircleShape
                    )
                    .combinedClickable(
                        interactionSource = interactionSource,
                        indication = rememberRipple(
                            color = Color(category.color),
                            radius = (config.size / 2f).dep(),
                        ),
                        onLongClick = {
                            onLongTap()
                        }
                    ) {
                        onClick()
                    }
            ) {
                Icon(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(config.iconSize.dep()),
                    painter = painterResource(id = category.icon as Int),
                    contentDescription = "Category item",
                    tint = Color(category.color)
                )
            }
            androidx.compose.animation.AnimatedVisibility (
                category.isSelected,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                Box(
                    modifier = Modifier
                        .padding(
                            start = config.size.droot2.dep(),
                            bottom = config.size.droot2.dep()
                        )
                ) {
                    SelectorIcon_ulkel8(
                        config = SelectorIconConfiguration(backgroundColor = Color(category.color)),
                        contentDescription = selectorItemContentDescription
                    )
                }
            }
        }
        val fontWeight by remember(category.isSelected) {
            derivedStateOf{
                if (category.isSelected) 700 else 400
            }
        }
        val animatedFontWeight by animateIntAsState(targetValue = fontWeight)
        FontFamilyText(
            text = category.name,
            color = config.textColor,
            fontSize = config.textSize.sep(),
            fontWeight = FontWeight(animatedFontWeight),
            letterSpacing = 0.17.sep(),
            lineHeight = 12.89.sep()
        )
    }
}