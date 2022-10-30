package co.yore.splitnpay.components.components

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import co.yore.splitnpay.R
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.models.DataIds
import co.yore.splitnpay.models.SettleOptions
import co.yore.splitnpay.models.SettledUnsettledMembersBottomSheet
import co.yore.splitnpay.models.SingleSettledOrUnsettledMember
import co.yore.splitnpay.pages.*
import co.yore.splitnpay.ui.theme.Botticelli
import co.yore.splitnpay.ui.theme.CloudBurst
import co.yore.splitnpay.ui.theme.SteelBlue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettledUnsettledMembersBottomSheetModel(val callback: Callback) : BottomSheetModel{
    interface Callback{
        fun scope(): CoroutineScope
        fun close()
        suspend fun settledMembers(): List<SingleSettledOrUnsettledMember>
        suspend fun unsettledMembers(): List<SingleSettledOrUnsettledMember>
        abstract fun onContinue(
            settledMembers: List<SingleSettledOrUnsettledMember>,
            unsettledMembers: List<SingleSettledOrUnsettledMember>
        )
    }

    // ///////////////
    private val _resolver = Resolver()
    private val _notifier = NotificationService{id, arg ->
        when (id){
            DataIds.splitIndividuallyClick -> {
                val index = arg as? Int ?: return@NotificationService
                val member = unsettledMembers[index]
                unsettledMembers[index] = member.copy(selectedSettleOption = SettleOptions.SplitIndividual)
            }
            DataIds.deleteAnywayClick -> {
                val index = arg as? Int ?: return@NotificationService
                val member = unsettledMembers[index]
                unsettledMembers[index] = member.copy(selectedSettleOption = SettleOptions.DeleteAnyway)
            }
            DataIds.settledUnsettledContinueClick -> {
                callback.onContinue(settledMembers, unsettledMembers)
            }
            DataIds.checkUnsettledMember -> {
                val index = arg as? Int ?: return@NotificationService
                val member = unsettledMembers[index]
                unsettledMembers[index] = member.copy(isChecked = !member.isChecked)
            }
            DataIds.checkSettledMember -> {
                val index = arg as? Int ?: return@NotificationService
                val member = settledMembers[index]
                settledMembers[index] = member.copy(isChecked = !member.isChecked)
            }
        }
    }

    // ///////////////
    override val resolver = _resolver
    override val notifier = _notifier
    override val scope get() = callback.scope()

    @Composable
    override fun Content() {
        SettledUnsettledMembersBottomSheet_mxjiuq()
    }

    override fun initialize() {
        clear()
        scope.launch(Dispatchers.IO) {
            val sm = callback.settledMembers()
            val um = callback.unsettledMembers()
            withContext(Dispatchers.Main){
                settledMembers.addAll(sm)
                unsettledMembers.addAll(um)
            }
        }
    }

    override fun clear() {
        settledMembers.clear()
        unsettledMembers.clear()
    }

    override fun onBack() {
        callback.close()
    }

    // //////////
    private val settledMembers = mutableStateListOf<SingleSettledOrUnsettledMember>()
    private val unsettledMembers = mutableStateListOf<SingleSettledOrUnsettledMember>()

    // //////////
    init {
        _resolver.addAll(
            DataIds.settledMembers to settledMembers,
            DataIds.unsettledMembers to unsettledMembers
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SettledUnsettledMembersBottomSheet_mxjiuq(
    settledMembers: List<SingleSettledOrUnsettledMember> = listState(key = DataIds.settledMembers),
    unsettledMembers: List<SingleSettledOrUnsettledMember> = listState(key = DataIds.unsettledMembers),
    contentDescription: String = "",
    config: SettledUnsettledMembersBottomSheet = SettledUnsettledMembersBottomSheet(),
    notifier: NotificationService = notifier()
) {
    var isSettledMemberSelected by remember {
        mutableStateOf(true)
    }
    val settledTabTextColor = animateColorAsState(
        targetValue = if (isSettledMemberSelected) CloudBurst else Botticelli,
        tween(700)
    )
    val unsettledTabTextColor = animateColorAsState(
        targetValue = if (isSettledMemberSelected) Botticelli else CloudBurst,
        tween(700)
    )

    Column(
        modifier = Modifier
            .semantics { this.contentDescription = contentDescription }
    ) {
        config.holderTopPadding.sy()
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50.dep()))
                .height(2.dep())
                .width(19.dep())
                .background(SteelBlue)
                .align(Alignment.CenterHorizontally)
        )
        config.holderBottomPadding.sy()

        Row(
            modifier = Modifier
                .padding(start = 42.dep())
        ) {
            FontFamilyText(
                modifier = Modifier.clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null
                ) { isSettledMemberSelected = true },
                text = stringResource(R.string.settled_members),
                color = settledTabTextColor.value,
                fontSize = 16.sep(),
                fontWeight = FontWeight.Bold
            )
            21.sx()

            FontFamilyText(
                modifier = Modifier.clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null
                ) { isSettledMemberSelected = false },
                text = stringResource(R.string.unsettled_memebers),
                color = unsettledTabTextColor.value,
                fontSize = 16.sep(),
                fontWeight = FontWeight.Bold
            )
        }

        Box(
            modifier = Modifier
                .wrapContentHeight()
                .padding(
                    top = 14.dep(),
                    start = 31.dep(),
                    end = 31.dep()
                )

        ) {
            AnimatedContent(
                targetState = isSettledMemberSelected,
                transitionSpec = {
                    fadeIn(animationSpec = tween(700, delayMillis = 90)) +
                        scaleIn(initialScale = 0.92f, animationSpec = tween(700, delayMillis = 90)) with
                        fadeOut(animationSpec = tween(90))
                }
            ) {
                if (it) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .maxHeightFactor(0.7f)
                    ){
                        itemsIndexed(settledMembers){index, item ->
                            Column(){
                                SettledOrUnsettledSingleRow_70d834(
                                    contentDescription = " SettledOrUnsettledSingleRow",
                                    member = item
                                ){
                                    notifier.notify(DataIds.checkSettledMember, index)
                                }
                            }
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .maxHeightFactor(0.7f)
                    ){
                        itemsIndexed(unsettledMembers){index, item ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ){
                                SettledOrUnsettledSingleRow_70d834(
                                    contentDescription = " SettledOrUnsettledSingleRow",
                                    member = item
                                ){
                                    notifier.notify(DataIds.checkUnsettledMember, index)
                                }
                                14.sy()
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    CustomRadioButton_2ofz97(
                                        contentDescription = "split individually",
                                        radioButtonName = stringResource(R.string.split_individually),
                                        isSelected = item.selectedSettleOption == SettleOptions.SplitIndividual
                                    ) {
                                        notifier.notify(DataIds.splitIndividuallyClick, index)
                                    }
                                    5.sy()
                                    CustomRadioButton_2ofz97(
                                        contentDescription = "delete any way",
                                        radioButtonName = stringResource(R.string.delete_anyway),
                                        isSelected = item.selectedSettleOption == SettleOptions.DeleteAnyway
                                    ) {
                                        notifier.notify(DataIds.deleteAnywayClick, index)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        12.sy()
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 31.dep()
                )
                .height(50.dep())
        ) {
            CustomButton_3egxtx(
                text = stringResource(id = R.string.continue1),
                onClick = {
                    notifier.notify(DataIds.settledUnsettledContinueClick)
                },
                contentDescription = "ContinueButton"
            )
        }
        25.sy()
    }
}
