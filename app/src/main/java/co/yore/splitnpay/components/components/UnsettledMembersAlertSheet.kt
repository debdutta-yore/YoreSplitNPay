package co.yore.splitnpay.components.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import co.yore.splitnpay.R
import co.yore.splitnpay.addmembers.FontFamilyText
import co.yore.splitnpay.demos.sy
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.models.DataIds
import co.yore.splitnpay.pages.*
import co.yore.splitnpay.ui.theme.DarkBlue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UnsettledMembersAlertSheetModel(val callback: Callback) : BottomSheetModel{
    interface Callback{
        fun scope(): CoroutineScope
        suspend fun members(): List<SingleSettledOrUnsettledMember>
        fun onContinue()
    }

    // /////////////
    private val _resolver = Resolver()
    private val _notifier = NotificationService{id, arg ->
        when (id){
            DataIds.selectUnsettledMembers -> {
                val index = arg as? Int ?: return@NotificationService
                val m = _members[index]
                _members[index] = m.copy(isChecked = !m.isChecked)
            }
            DataIds.splitIndividuallyClick -> {
                val index = arg as? Int ?: return@NotificationService
                val m = _members[index]
                _members[index] = m.copy(selectedSettleOption = SettleOptions.SplitIndividual)
            }
            DataIds.deleteAnywayClick -> {
                val index = arg as? Int ?: return@NotificationService
                val m = _members[index]
                _members[index] = m.copy(selectedSettleOption = SettleOptions.DeleteAnyway)
            }
            DataIds.settledUnsettledContinueClick -> {
                callback.onContinue()
            }
        }
    }

    // /////////////
    override val resolver = _resolver
    override val notifier = _notifier
    override val scope get() = callback.scope()

    @Composable
    override fun Content() {
        UnsettledMembersAlertSheet()
    }

    override fun initialize() {
        clear()
        scope.launch(Dispatchers.IO) {
            val members = callback.members()
            withContext(Dispatchers.Main){
                _members.addAll(members)
            }
        }
    }

    override fun clear() {

    }

    override fun onBack() {

    }

    // /////////
    private val _members = mutableStateListOf<SingleSettledOrUnsettledMember>()

    // ////////
    init {
        _resolver.addAll(
            DataIds.unsettledMembers to _members
        )
    }
}

@Composable
fun UnsettledMembersAlertSheet(
    unsettledMembers: List<SingleSettledOrUnsettledMember> = listState(key = DataIds.unsettledMembers),
    contentDescription: String = "",
    notifier: NotificationService = notifier(),
    config: UnSettledMembersConfiguration = UnSettledMembersConfiguration()
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 23.dep())
    ) {
        20.sy()
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50.dep()))
                .height(2.dep())
                .width(19.dep())
                .background(Color(0xff5A87BB))
                .align(Alignment.CenterHorizontally)
        )
        33.sy()
        FontFamilyText(
            text = stringResource(R.string.unsettled_members),
            color = DarkBlue,
            fontSize = 16.sep(),
            fontWeight = FontWeight.Bold
        )
        Box(
            modifier = Modifier
                .padding(
                    top = 14.dep()
                )
        ) {
            Column(
                modifier = Modifier
                    .semantics { this.contentDescription = contentDescription }
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .maxHeightFactor(0.7f)
                        .fadingEdge(),
                    verticalArrangement = Arrangement.spacedBy(26.dep()),
                    contentPadding = PaddingValues(vertical = 12.dep())
                ){
                    itemsIndexed(unsettledMembers){ index, item ->
                        SettledOrUnsettledSingleRow_70d834(
                            contentDescription = " SettledOrUnsettledSingleRow",
                            member = item
                        ) {
                            notifier.notify(DataIds.selectUnsettledMembers, index)
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
                12.sy()
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 7.dep(),
                            end = 7.dep()
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
                config.bottomPaddingOfButton.sy()
            }
        }
    }
}
