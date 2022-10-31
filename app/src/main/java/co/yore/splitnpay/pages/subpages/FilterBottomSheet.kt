package co.yore.splitnpay.pages.subpages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.yore.splitnpay.components.components.FontFamilyText
import co.yore.splitnpay.components.components.ProfileImage_2hf7q0
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.libs.jerokit.*
import co.yore.splitnpay.libs.jerokit.bottom_sheet.BottomSheetModel
import co.yore.splitnpay.models.CheckboxConfiguration
import co.yore.splitnpay.models.DataIds
import co.yore.splitnpay.models.FilterSingleUserConfiguration
import co.yore.splitnpay.models.SingleItem
import co.yore.splitnpay.ui.theme.CuriousBlue
import co.yore.splitnpay.ui.theme.CloudBurst
import co.yore.splitnpay.ui.theme.SteelBlue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MemberFilterBottomSheetModel(val callback: Callback) : BottomSheetModel {
    interface Callback{
        fun scope(): CoroutineScope
        suspend fun getMembers(): List<SingleItem>
        fun onContinue(index: Int)
        fun selectedIndex(): Int
    }

    private val _resolver = Resolver()
    private var selectedIndex = -1
    private val _notifier = NotificationService{id, arg ->
        when (id){
            DataIds.filterMember -> {
                updateAsSelected(arg)
            }
            DataIds.filterDone -> {
                callback.onContinue(selectedIndex)
            }
        }
    }

    private fun updateAsSelected(arg: Any?) {
        if (selectedIndex > -1){
            members[selectedIndex] = members[selectedIndex].copy(isSelected = false)
        }
        if (arg is SingleItem){
            val index = members.indexOf(arg)
            if (index > -1){
                val current = members[index]
                val selected = !current.isSelected
                if (selected){
                    selectedIndex = index
                } else {
                    selectedIndex = -1
                }
                members[index] = current.copy(isSelected = selected)
            } else {
                selectedIndex = -1
            }
        } else {
            selectedIndex = -1
        }
    }

    // ////////////
    override val resolver = _resolver
    override val notifier = _notifier
    override val scope get() = callback.scope()

    @Composable
    override fun Content() {
        MemberFilterBottomSheet()
    }

    override fun initialize() {
        clear()
        scope.launch(Dispatchers.IO) {
            val m = callback.getMembers()
            withContext(Dispatchers.Main){
                members.addAll(m)
                selectedIndex = callback.selectedIndex()

                if (selectedIndex > -1){
                    val current = members[selectedIndex]
                    members[selectedIndex] = current.copy(isSelected = true)
                }
            }
        }
    }

    override fun clear() {
        members.clear()
        selectedIndex = -1
    }

    override fun onBack() {

    }

    // //////////////
    private val members = mutableStateListOf<SingleItem>()

    // //////////////
    init {
        _resolver.addAll(
            DataIds.membersForFiltering to members
        )
    }
}

@Composable
fun MemberFilterBottomSheet(
    list: List<SingleItem> = listState(key = DataIds.membersForFiltering),
    notifier: NotificationService = notifier()
) {
    Column(
        modifier = Modifier
            .padding(start = 35.dep(), end = 35.dep(), top = 21.dep())
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50.dep()))
                .height(2.dep())
                .width(19.dep())
                .background(SteelBlue)
                .align(Alignment.CenterHorizontally)
        )
        41.sy()
        FontFamilyText(
//            modifier = Modifier.padding(start = 33.dep()),
            text = "Filter",
            color = CloudBurst,
            fontSize = 16.sep(),
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.67.sep(),
            lineHeight = 19.sep()
        )
        25.sy()
        val screenHeight = LocalConfiguration.current.screenHeightDp
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = (screenHeight * 0.6).dp)
                .fadingEdge(),
            verticalArrangement = Arrangement.spacedBy(18.dep())
        ) {
            items(list) {item ->
                FilterSingleUser_ffkz81(
                    config = FilterSingleUserConfiguration(),
                    contentDescription = "FilterSingleUser",
                    item = item
                ) {
                    notifier.notify(DataIds.filterMember, item)
                }
            }
        }
        37.sy()
        Button(
            modifier = Modifier
                .padding(bottom = 17.dep())
                .fillMaxWidth()
                .height(47.dep()),
            onClick = {
                notifier.notify(DataIds.filterDone)
            },
            shape = RoundedCornerShape(23.5.dep()),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = CuriousBlue
            )
        ) {
            FontFamilyText(
                text = "Done",
                fontSize = 16.sep(),
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
private fun FilterSingleUser_ffkz81(
    modifier: Modifier = Modifier,
    config: FilterSingleUserConfiguration = FilterSingleUserConfiguration(),
    contentDescription: String,
    item: SingleItem,
    onSelected: (SingleItem) -> Unit
) {
    Row(
        modifier = modifier
            .semantics { this.contentDescription = contentDescription }
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProfileImage_2hf7q0(
            contentDescription = "userProfileImageFilter",
            image = item.profilePic
        )

        config.spaceBetweenProfileImageAndUserName.sx()

        Column {
            FontFamilyText(
                text = item.userName,
                color = CloudBurst,
                fontSize = 12.sep(),
                fontWeight = FontWeight.Bold,
                lineHeight = 14.sep(),
                letterSpacing = 0.2.sep()
            )

            config.spaceBetweenUserNameAndPhoneNo.sy()

            FontFamilyText(
                text = item.mobileNo,
                color = SteelBlue,
                fontSize = 11.sep(),
                lineHeight = 13.sep(),
                letterSpacing = 0.2.sep()
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        SelectorIcon_ulkel8(
            selected = item.isSelected,
            onClick = { onSelected(item) },
            contentDescription = "CheckBox"
        )
    }
}

@Composable
fun SelectorIcon_ulkel8(
    selected: Boolean,
    config: CheckboxConfiguration = CheckboxConfiguration(),
    onClick: () -> Unit,
    contentDescription: String
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dep()))
            .size(config.iconSize.dep())
            .background(if (selected) config.iconColor else Color.White)
            .border(
                if (selected) 0.dep() else 1.dep(),
                color = config.iconColor,
                shape = RoundedCornerShape(10.dep())
            )
            .clickable {
                onClick()
            }
            .semantics { this.contentDescription = contentDescription }
    ) {
        Icon(
            modifier = Modifier
                .size(10.67.dep())
                .align(Center),
            painter = painterResource(config.icon),
            contentDescription = "check icon",
            tint = Color.Unspecified
        )
    }
}


