package co.yore.splitnpay.components.components

import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Bottom
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.Top
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.yore.splitnpay.R
import co.yore.splitnpay.addmembers.FontFamilyText
import co.yore.splitnpay.demos.sx
import co.yore.splitnpay.demos.sy
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.models.DataIds
import co.yore.splitnpay.pages.CustomButton_3egxtx
import co.yore.splitnpay.pages.LightBlue1
import co.yore.splitnpay.ui.theme.Bluish
import co.yore.splitnpay.ui.theme.DarkBlue
import co.yore.splitnpay.ui.theme.LightGreen3
import co.yore.splitnpay.ui.theme.Pink
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettlePaymentMethodBottomSheetModel(val callback: Callback) : BottomSheetModel {
    interface Callback {
        fun scope(): CoroutineScope
        suspend fun getUpis(): List<Upi>
        suspend fun cashPaymentMobileNumber(): String
        fun close()
        fun onAddContinue(upiId: String)
        fun onContinue(upiId: String)
    }

    // ///////////
    private val _resolver = Resolver()
    private var selectedUpiIdIndex = -1
    private val _notifier = NotificationService { id, arg ->
        when (id) {
            DataIds.selectedSettleOption -> {
                selectedSettleOption.value = arg as? SettlePaymentOptions ?: return@NotificationService
                if (selectedSettleOption.value == SettlePaymentOptions.Cash) {
                    if (selectedUpiIdIndex != -1) {
                        upiIds[selectedUpiIdIndex] = upiIds[selectedUpiIdIndex].copy(isSelected = false)
                    }
                    selectedUpiIdIndex = -1
                    selectedUpiId.value = ""
                    adding.value = false
                }
            }
            DataIds.selectedUpiId -> {
                adding.value = false
                if (selectedUpiIdIndex > -1) {
                    upiIds[selectedUpiIdIndex] = upiIds[selectedUpiIdIndex].copy(isSelected = false)
                }
                selectedUpiIdIndex = arg as? Int ?: return@NotificationService
                upiIds[selectedUpiIdIndex] = upiIds[selectedUpiIdIndex].copy(isSelected = true)
                selectedUpiId.value = upiIds[selectedUpiIdIndex].upiId
            }
            DataIds.addUpiClick -> {
                selectedSettleOption.value = SettlePaymentOptions.Upi
                adding.value = !adding.value
            }
            DataIds.editUpiId -> {
                editUpiId.value = arg as? String ?: return@NotificationService
            }
            DataIds.settleCashUpiContinueClick -> {
                if (adding.value) {
                    callback.onAddContinue(editUpiId.value)
                } else {
                    callback.onContinue(selectedUpiId.value)
                }
            }
        }
    }

    // //////////////
    override val resolver = _resolver
    override val notifier = _notifier
    override val scope = callback.scope()

    // /////////////////////
    private val adding = mutableStateOf(false)
    private val editUpiId = mutableStateOf("")
    private val upiIds = mutableStateListOf<Upi>()
    private val cashPaymentMobileNumber = mutableStateOf("")
    private val selectedUpiId = mutableStateOf("")
    private val selectedSettleOption = mutableStateOf(SettlePaymentOptions.Cash)
    // /////////////////////

    @Composable
    override fun Content() {
        SettlePaymentMethodBottomSheet_v9w5fs()
    }

    override fun initialize() {
        clear()
        scope.launch(Dispatchers.IO) {
            val upis = callback.getUpis()
            val mobileNumber = callback.cashPaymentMobileNumber()
            withContext(Dispatchers.Main) {
                upiIds.addAll(upis)
                cashPaymentMobileNumber.value = mobileNumber
                selectedUpiId.value = upiIds.firstOrNull { it.isSelected }?.upiId ?: ""
                selectedUpiIdIndex = upiIds.indexOfFirst { it.isSelected }
            }
        }
    }

    override fun clear() {
        upiIds.clear()
        adding.value = false
        editUpiId.value = ""
    }

    override fun onBack() {
        if (adding.value) {
            adding.value = false
        } else {
            callback.close()
        }
    }

    // /////////////
    init {
        _resolver.addAll(
            DataIds.upiAdding to adding,
            DataIds.editUpiId to editUpiId,
            DataIds.upiIds to upiIds,
            DataIds.cashPaymentMobileNumber to cashPaymentMobileNumber,
            DataIds.selectedUpiId to selectedUpiId,
            DataIds.selectedSettleOption to selectedSettleOption
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SettlePaymentMethodBottomSheet_v9w5fs(
    contentDescription: String = "",
    adding: Boolean = boolState(key = DataIds.upiAdding).value,
    editUpiId: String = stringState(key = DataIds.editUpiId).value,
    upiIds: List<Upi> = listState(key = DataIds.upiIds),
    mobileNumber: String = stringState(key = DataIds.cashPaymentMobileNumber).value,
    selectedUpiId: String = stringState(key = DataIds.selectedUpiId).value,
    selectedSettleOption: SettlePaymentOptions = tState<SettlePaymentOptions>(key = DataIds.selectedSettleOption).value,
    notifier: NotificationService = notifier(),
    config: SettlePaymentMethodBottomSheetConfiguration = SettlePaymentMethodBottomSheetConfiguration()
) {
    // For some reason this does not work
    val isSettleViaCashPaymentSelected by remember(key1 = selectedSettleOption) {
        derivedStateOf {
            selectedSettleOption == SettlePaymentOptions.Cash
        }
    }

    val settleCashBackgroundColor = animateColorAsState(
        targetValue =
        if (isSettleViaCashPaymentSelected) {
            LightBlue1
        } else {
            Color.White
        }
    )

    val settleUpiBackgroundColor = animateColorAsState(
        targetValue =
        if (isSettleViaCashPaymentSelected) {
            Color.White
        } else {
            LightBlue1
        }
    )

    val upiButtonTopPadding = animateDpAsState(targetValue = if (selectedSettleOption == SettlePaymentOptions.Upi) 24.dep() else 0.dep())

    val upiButtonButtonPadding = animateDpAsState(targetValue = if (selectedUpiId.isNotEmpty() || adding) 0.dep() else 36.dep())

    Column(
        modifier = Modifier
            .semantics { this.contentDescription = contentDescription }
            .fillMaxWidth()
            .background(Color.White)
    ) {
        config.bottomSheetHolderTopPadding.sy()
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50.dep()))
                .height(2.dep())
                .width(19.dep())
                .background(Color(0xff5A87BB))
                .align(CenterHorizontally)
        )
        config.bottomPaddingOfHolder.sy()
        Row(
            modifier = Modifier
                .clickable {
                    notifier.notify(DataIds.selectedSettleOption, SettlePaymentOptions.Cash)
                }
                .fillMaxWidth()
                .height(config.firstRowHeight.dep())
                .background(
                    color = settleCashBackgroundColor.value
                ),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .height(config.firstRowHeight.dep())
                    .align(CenterVertically)
                    .padding(start = config.rowStartPadding.dep()),
                verticalAlignment = Top
            ) {
                Icon(
                    modifier = Modifier
                        .padding(top = 27.dep())
                        .size(24.dep()),
                    painter = painterResource(id = R.drawable.ic_phone),
                    tint = Color.Unspecified,
                    contentDescription = "phoneIcon"
                )
                17.sx()
                Column(
                    modifier = Modifier
                        .align(Bottom)
                        .padding(bottom = 16.dep())
                ) {
                    FontFamilyText(
                        text = stringResource(R.string.settle_as_cash),
                        color = DarkBlue,
                        fontSize = 14.sep(),
                        lineHeight = 16.sep(),
                        letterSpacing = 0.66.sep(),
                        fontWeight = FontWeight.Bold
                    )
                    6.sy()
                    FontFamilyText(
                        text = mobileNumber,
                        color = Color(0xff5A87BB),
                        fontSize = 14.sep(),
                        lineHeight = 16.sep()
                    )
                }
            }
            if (isSettleViaCashPaymentSelected) {
                Box(
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(
                                topStart = 5.dep(),
                                bottomStart = 5.dep()
                            )
                        )
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

        Row(
            modifier = Modifier
                .height(config.secondRowHeight.dep())
                .fillMaxWidth()
                .background(
                    color = settleUpiBackgroundColor.value
                ),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .align(CenterVertically)
                    .padding(
                        start = config.rowStartPadding.dep()
                    )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_upi),
                    tint = Color.Unspecified,
                    contentDescription = "phoneIcon"
                )
                14.sx()
                FontFamilyText(
                    text = stringResource(R.string.settle_via_upi),
                    color = DarkBlue,
                    fontSize = 16.sep(),
                    fontWeight = FontWeight.Bold
                )
            }
            if (!isSettleViaCashPaymentSelected) {
                Box(
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(
                                topStart = 5.dep(),
                                bottomStart = 5.dep()
                            )
                        )
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
        var arrowCx by remember {
            mutableStateOf(-100f)
        }
        val animatedArrowCxx by animateFloatAsState(targetValue = arrowCx)
        var currentSelectedIndex by remember {
            mutableStateOf(-1)
        }
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = upiButtonTopPadding.value,
                    bottom = upiButtonButtonPadding.value
                )
                .height(45.dep()),
            contentPadding = PaddingValues(horizontal = 65.dep()),
            horizontalArrangement = Arrangement.spacedBy(20.dep())
        ) {
            itemsIndexed(upiIds) { index, it ->
                UpiButton_fuhz6x(
                    upi = it,
                    contentDescription = "upiButton",
                    config = UpiButtonConfiguration(
                        backGroundColor = it.backGroundColor
                    ),
                    onHorizontalCenterChange = {
                        currentSelectedIndex = index
                        arrowCx = it
                    }
                ) {
                    notifier.notify(DataIds.selectedSettleOption, SettlePaymentOptions.Upi)
                    notifier.notify(DataIds.selectedUpiId, index)
                }
            }
            item {
                OutlineButton_hargeg(
                    selected = adding,
                    onClick = {
                        notifier.notify(DataIds.addUpiClick)
                    },
                    contentDescription = "otherButton"
                )
            }
        }

        val state by remember(adding, selectedUpiId) {
            derivedStateOf {
                if (adding) {
                    2
                } else if (selectedUpiId.isNotEmpty()) {
                    1
                } else {
                    0
                }
            }
        }
        AnimatedContent(
            state,
            transitionSpec = {
                fadeIn(animationSpec = tween(500, delayMillis = 90)) +
                    scaleIn(initialScale = 0.92f, animationSpec = tween(500, delayMillis = 90)) with
                    fadeOut(animationSpec = tween(500))
            }
        ) { it ->
            when (it) {
                0 -> {}
                1 -> {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val density = LocalDensity.current.density
                        Box(
                            modifier = Modifier
                                .padding(top = 16.dep())
                                .offset(x = (animatedArrowCxx / density).dp - 7.5.dp)
                                .size(15.dep())
                                .background(AlabasterSolid, TriangleShape)
                        ) {
                        }
                        Box(
                            modifier = Modifier
                                // .padding(top = 16.dep())
                                .height(75.dep())
                                .fillMaxWidth()
                                .background(color = AlabasterSolid),
                            contentAlignment = Center
                        ) {
                            FontFamilyText(
                                text = selectedUpiId,
                                color = LightGrey,
                                fontSize = 14.sep()
                            )
                        }
                    }
                }
                2 -> {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 31.dep())
                            .padding(top = 16.dep())
                            .clip(RoundedCornerShape(8.dep()))
                            .height(52.dep())
                            .fillMaxWidth()
                            .background(
                                color = LightGrey2,
                                shape = RoundedCornerShape(8.dep())
                            )
                    ) {
                        CustomTextField_wangst(
                            text = editUpiId,
                            change = {
                                notifier.notify(DataIds.editUpiId, it)
                            },
                            leadingIcon = R.drawable.ic_upi,
                            placeHolderText = stringResource(id = R.string.add_upi_id),
                            contentDescription = "",
                            iconTint = Color(0xff656565)
                        )
                    }
                }
            }
        }
        15.sy()
        val enabled by remember(
            selectedSettleOption,
            selectedUpiId,
            adding,
            editUpiId
        ) {
            derivedStateOf {
                selectedSettleOption == SettlePaymentOptions.Cash ||
                    (selectedUpiId.isNotEmpty() && !adding) ||
                    (adding && editUpiId.isNotEmpty())
            }
        }

        Box(
            modifier = Modifier
                .align(CenterHorizontally)
                .height(47.dep())
                .width(299.dep())
        ) {
            CustomButton_3egxtx(
                enabled = enabled,
                text = stringResource(id = R.string.continue1),
                onClick = {
                    notifier.notify(DataIds.settleCashUpiContinueClick)
                },
                contentDescription = "ContinueButton"
            )
        }
        config.bottomPaddingOfContinueButton.sy()
        Box(
            modifier = Modifier
                .navigationBarsPadding()
                .imePadding()
        ) {
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun UpiButton_fuhz6x(
    upi: Upi,
    contentDescription: String,
    config: UpiButtonConfiguration = UpiButtonConfiguration(),
    onHorizontalCenterChange: (Float) -> Unit,
    onClick: () -> Unit
) {
    val borderColor = animateColorAsState(targetValue = if (upi.isSelected) config.borderColor else config.backGroundColor)
    val borderWidth = animateDpAsState(targetValue = if (upi.isSelected) config.borderWidth.dep() else 0.dep())
    Box(
        modifier = Modifier
            .semantics { this.contentDescription = contentDescription }
            .size(config.size.dep())
            .then(
                if (upi.isSelected) {
                    Modifier.onGloballyPositioned {
                        val center = (it.positionInWindow().x) + it.size.width / 2f
                        onHorizontalCenterChange(center)
                    }
                } else {
                    Modifier
                }
            )

    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(config.shape)
                .border(
                    color = borderColor.value,
                    width = borderWidth.value,
                    shape = config.shape
                )
                .clickable { onClick() }
                .background(config.backGroundColor),
            contentAlignment = Center
        ) {
            FontFamilyText(
                text = upi.upiName,
                color = config.textColor,
                fontSize = config.textSize.sep()
            )
        }

        AnimatedVisibility(
            upi.isSelected,
            enter = fadeIn(tween(500)) + scaleIn(tween(500)),
            exit = fadeOut(tween(500)) + scaleOut(tween(500))
        ) {
            Box(
                Modifier
                    .align(Alignment.TopEnd)
                    .padding(
                        bottom = config.size.droot2.dep(),
                        start = config.size.droot2.dep()
                    )
            ) {
                SelectedIcon_f9tfi6(
                    config = SelectedIconConfiguration(backGroundColor = Bluish),
                    contentDescription = "selectedIcon"
                )
            }
        }
    }
}

@Composable
fun SelectedIcon_f9tfi6(
    config: SelectedIconConfiguration = SelectedIconConfiguration(),
    contentDescription: String
) {
    Box(
        modifier = Modifier
            .semantics { this.contentDescription = contentDescription }
            .size(config.selectedIconSize.dep())
            .aspectRatio(1f, matchHeightConstraintsFirst = true)
            .border(width = config.borderWidth.dep(), color = config.borderColor, shape = config.shape)
            .padding(config.borderWidth.dep())
            .clip(config.shape)
            .background(color = config.backGroundColor)
    ) {
        Icon(
            modifier = Modifier
                .align(Alignment.Center)
                .size(config.iconSize.dep()),
            tint = Color.Unspecified,
            painter = painterResource(id = config.icon),
            contentDescription = contentDescription
        )
    }
}

data class SelectedIconConfiguration(
    val selectedIconSize: Float = 18f,
    val borderWidth: Float = 1f,
    val borderColor: Color = Color.White,
    val shape: Shape = CircleShape,
    val backGroundColor: Color = Pink,
    val icon: Int = R.drawable.ic_checked_right,
    val iconSize: Float = 7f
)

@Composable
fun OutlineButton_hargeg(
    selected: Boolean,
    onClick: () -> Unit,
    contentDescription: String,
    config: OutlineButtonConfiguration = OutlineButtonConfiguration()
) {
    val color by remember(selected) {
        derivedStateOf {
            if (selected) config.borderColor else Color.White
        }
    }
    val tint by remember(selected) {
        derivedStateOf {
            if (!selected) config.borderColor else Color.White
        }
    }
    val animatedColor by animateColorAsState(targetValue = color, tween(500))
    val animatedTint by animateColorAsState(targetValue = tint, tween(500))
    Box(
        modifier = Modifier
            .semantics { this.contentDescription = contentDescription }
            .size(config.size.dep())
            .clip(config.shape)
            .background(animatedColor)
            .border(
                color = config.borderColor,
                width = config.borderWidth.dep(),
                shape = config.shape
            )
            .clickable(
                rippleColor = DarkBlue,
                onClick = {
                    onClick()
                }
            ),
        contentAlignment = Center
    ) {
        Icon(
            painter = painterResource(id = config.icon),
            contentDescription = "centerIcon",
            tint = animatedTint
        )
    }
}

data class Upi(
    val upiName: String,
    val upiId: String,
    val bankName: String,
    val userName: String,
    val backGroundColor: Color,
    val isSelected: Boolean
)

data class UpiButtonConfiguration(
    val size: Float = 45f,
    val shape: Shape = CircleShape,
    val backGroundColor: Color = LightGreen3,
    val borderColor: Color = Bluish,
    val borderWidth: Float = 1f,
    val textSize: Float = 12f,
    val textColor: Color = Color.White,
    val textWeight: FontWeight = FontWeight(400)
)

data class OutlineButtonConfiguration(
    val size: Float = 45f,
    val shape: Shape = CircleShape,
    val borderColor: Color = Bluish,
    val borderWidth: Float = 1.1f,
    val icon: Int = R.drawable.ic_category_blue_plus,
    val iconTint: Color = Color.Unspecified
)

data class SettlePaymentMethodBottomSheetConfiguration(
    val bottomSheetHolderTopPadding: Float = 20f,
    val bottomSheetHolderTint: Color = Color.Unspecified,
    val bottomPaddingOfHolder: Float = 22f,
    val firstRowHeight: Float = 80f,
    val secondRowHeight: Float = 57f,
    val selectedBackGroundColor: Color = LightBlue1,
    val rowStartPadding: Float = 28f,
    val upiRowTopPadding: Float = 21f,
    val bottomPaddingOfContinueButton: Float = 12f
)
val AlabasterSolid = Color(0xffFAFAFA)
enum class SettlePaymentOptions {
    Cash,
    Upi
}
val TriangleShape = GenericShape { size, _ ->
    // 1)
    moveTo(size.width / 2f, 0f)

    // 2)
    lineTo(size.width, size.height)

    // 3)
    lineTo(0f, size.height)
}
