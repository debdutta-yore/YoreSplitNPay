package co.yore.splitnpay.components.components

import androidx.compose.animation.*
import androidx.compose.animation.core.animateIntAsState
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
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TextFieldDefaults.indicatorLine
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
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
import co.yore.splitnpay.R
import co.yore.splitnpay.addmembers.FontFamilyText
import co.yore.splitnpay.demos.sx
import co.yore.splitnpay.demos.sy
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.models.Category
import co.yore.splitnpay.models.DataIds
import co.yore.splitnpay.pages.CustomButton_3egxtx
import co.yore.splitnpay.ui.theme.DarkBlue
import co.yore.splitnpay.ui.theme.LightBlue4

interface BottomSheetModel{
    val resolver : Resolver
    val notifier : NotificationService

    @Composable
    fun provide(
        content: @Composable () -> Unit
    ){
        CompositionLocalProvider(
            LocalResolver provides resolver,
            LocalNotificationService provides notifier
        ) {
            content()
        }
    }
}

class BillTotalBottomSheetModel: BottomSheetModel{
    private val _resolver = Resolver()
    private val _notifier = NotificationService{id,arg->
        when(id){

        }
    }
    override val resolver get() = _resolver
    override val notifier get() = _notifier
    ///////////////
    private val _categories = mutableStateListOf<Category>()
    private val _billTotalAmount = mutableStateOf("")
    private val _billTotalDescription = mutableStateOf("")
    private val _renamePressed = mutableStateOf(-1)
    private val canProceedWithBillTotal = mutableStateOf(false)

    init {
        _resolver.addAll(
            DataIds.categories to _categories,
            DataIds.billTotalAmount to _billTotalAmount,
            DataIds.billTotalDescription to _billTotalDescription,
            DataIds.renamePressed to _renamePressed,
            DataIds.canProceedWithBillTotal to canProceedWithBillTotal,
        )
    }
}

val LightGrey2 = Color(0xffF8F8F8)
val Grayish = Color(0xffF5F5F5)
@Composable
fun BillTotalBottomSheet(
    categories: List<Category> = listState(key = DataIds.categories),
    billTotalAmount: String = stringState(key = DataIds.billTotalAmount).value,
    billTotalDescription: String = stringState(key = DataIds.billTotalDescription).value,
    renamePressed: Int = intState(key = DataIds.renamePressed).value,
    canProceed: Boolean = boolState(key = DataIds.canProceedWithBillTotal).value,
    notifier: NotificationService = notifier()
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .safeDrawingPadding()
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
                leadingIcon = painterResource(id = R.drawable.ic_rupees),
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
                }
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
                leadingIcon = painterResource(id = R.drawable.ic_description),
                placeHolderText = stringResource(R.string.add_description),
                singleLine = true,
                maxLines = 1
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
                .padding(
                    top = 20.dep()
                ),
            columns = GridCells.Fixed(5),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            itemsIndexed(categories) { index, item ->
                CategoryItem(
                    category = item,
                    onClick = {
                        if (renamePressed != index && renamePressed != -1) {
                            notifier.notify(DataIds.renamePressed, index)
                        }
                        notifier.notify(DataIds.categorySelectionClick, index)
                    },
                    onLongTap = {
                        notifier.notify(DataIds.renamePressed, index)
                        notifier.notify(DataIds.categorySelectionClick, index)
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

        AnimatedVisibility(visible = renamePressed != -1) {
            Box(
                modifier = Modifier
                    .padding(top = 18.dep())
                    .background(
                        color = LightGrey2,
                        shape = RoundedCornerShape(8.dep())
                    )
                    .clip(RoundedCornerShape(8.dep()))
                    .height(52.dep())
                    .fillMaxWidth(),
            ) {
                CustomTextField_wangst(
                    text = categories[renamePressed].name,
                    change = {
                        notifier.notify(DataIds.billTotalAmount, it)
                    },
                    contentDescription = "",
                    leadingIcon = painterResource(id = categories[renamePressed].icon as Int),
                    placeHolderText = categories[renamePressed].name
                )
            }
        }

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
                    notifier.notify(DataIds.billTotalContinueClick)
                },
                contentDescription = "Continue button",
                enabled = canProceed
            )
        }
        Box(
            modifier = Modifier.keyboardHeight()
        ){

        }
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
    leadingIcon: Painter,
    placeHolderText: String,
    contentDescription: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    maxLines: Int = Int.MAX_VALUE,
    singleLine: Boolean = false,
    textStyle: TextStyle = TextStyle.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interceptor: ((String)->String)? = null,
    config: CustomTextFieldConfiguration = CustomTextFieldConfiguration()
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
                start = config.contentStartPadding.dep(),
                top = config.contentTopPadding.dep(),
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
                    Icon(
                        modifier = Modifier
                            .padding(start = 18.dep())
//                            .width(config.leadingIconWidth.dep())
//                            .height(config.leadingIconHeight.dep())
                        ,
                        painter = leadingIcon,
                        tint = config.iconColor,
                        contentDescription = "people icon",
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
data class CustomTextFieldConfiguration(
    val disabledTextColor: Color = Color.Transparent,
    val backgroundColor: Color = LightGrey2,
    val iconColor: Color = LightGrey,
    val focusedIndicatorColor: Color = Color.Transparent,
    val unfocusedIndicatorColor: Color = Color.Transparent,
    val disabledIndicatorColor: Color = Color.Transparent,
    val cursorColor: Color = Color.Gray,
    val cornerShape: Float = 8f,
    val contentStartPadding: Float = 15f,
    val contentTopPadding: Float = 10f,
    val contentBottomPadding: Float = 10f,
    val textColor: Color = LightGrey4,
    val textFontSize: Float = 14f,
    val textFontWeight: FontWeight = FontWeight(400),
    val leadingIconWidth: Float = 22f,
    val leadingIconHeight: Float = 14f,
    val dividerWidth: Float = 1f,
    val dividerHeight: Float = 19f,
    val dividerColor: Color = LightBlue4,
    val dividerStartPadding: Float = 9f
)
///////////////////////////////////////
val LightBlue3 = Color(0xffCFD8E4)
data class CategoryItemConfiguration(
    val size: Float = 45f,
    val borderStroke: Float = 1f,
    val borderColor: Color = LightBlue3,
    val iconSize: Float = 20f,
    val spacerIconText: Float = 7f,
    val textSize: Float = 11f,
    val textColor: Color = Color.Black,
)
data class SelectorIconConfiguration(
    val selectorSize: Float = 20f,
    val backgroundColor: Color = Color.White,
    val selectorBorderStroke: Float = 1f,
    val selectorBorderColor: Color = Color.White,
    val selectorIconSize: Float = 6.67f,
    val selectorIcon: Int = R.drawable.ic_checked_right,
    val selectorIconContentDescription: String = "checked_right",
    val selectorIconTint: Color = Color.Unspecified
)
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