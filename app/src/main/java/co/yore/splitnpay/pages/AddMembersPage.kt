package co.yore.splitnpay.pages

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import co.yore.splitnpay.R
import co.yore.splitnpay.components.components.*
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.libs.jerokit.*
import co.yore.splitnpay.models.*
import co.yore.splitnpay.ui.theme.*

@OptIn(ExperimentalAnimationApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SplitWithPageContent(
    config: SplitWithPageContentConfiguration = SplitWithPageContentConfiguration(),
    selectedIndex: Int = intState(DataIds.selectedTabIndex).value,
    proceedWithContacts: Boolean = boolState(DataIds.proceedWithContacts).value,
    notifier: NotificationService = notifier()
){
    Scaffold(
        floatingActionButton = {
            AnimatedVisibility(
                proceedWithContacts,
                enter = fadeIn(tween(300)) + scaleIn(tween(300)),
                exit = fadeOut(tween(300)) + scaleOut(tween(300))
            ){
                FloatingActionButton(
                    shape = CircleShape,
                    backgroundColor = colorResource(id = R.color.pink),
                    modifier = Modifier
                        .size(61.dep()),
                    onClick = {
                        notifier.notify(DataIds.proceedWithContacts, null)
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_forward_black_24dp_1),
                        contentDescription = "next",
                        tint = Color.Unspecified
                    )
                }
            }
        }
    ) {
        Column {
            TopBarWithIcon_1t9xbo1(
                onClick = {
                    notifier.notify(DataIds.back, null)
                },
                text = stringResource(config.splitTextId)
            )

            Text(
                text = stringResource(config.splitWithTextId),
                modifier = Modifier
                    .padding(
                        start = 16.dep(),
                        top = 32.dep()
                    ),
                fontSize = 16.sep(),
                fontWeight = FontWeight.Bold,
                fontFamily = robotoFonts,
                color = colorResource(R.color.darkblue)
            )

            Spacer(modifier = Modifier.height(19.dep()))

            Box(
                modifier = Modifier.padding(horizontal = 16.dep())
            ){
                ContactSearchBar(
                    contentDescription = "split_search"
                )
            }

            Spacer(modifier = Modifier.height(38.dep()))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 16f.dep(),
                        end = 16f.dep()
                    )
            ) {
                SplitWithPageTabsSection(selectedIndex){
                    notifier.notify(DataIds.selectedTabIndex, it)
                }
            }
            Spacer(modifier = Modifier.height(18.dep()))
            AddedMembersSection()
            GroupAndContactsUI()
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GroupAndContactsUI(
    items: List<GroupOrContact> = listState(DataIds.groupsAndPeoples),
    selecteds: List<Any> = listState(DataIds.selecteds),
    proceedWithContacts: Boolean = boolState(DataIds.proceedWithContacts).value,
    groupsChecked: Map<Any, TriState>? = safeMapState(DataIds.groupsChecked)
) {
    val bottomPadding by remember(proceedWithContacts) {
        derivedStateOf {
            if (proceedWithContacts) {
                85
            } else {
                13
            }
        }
    }
    if (items.isNotEmpty()){
        val listState = rememberLazyListState()
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fadingEdge(),
            contentPadding = PaddingValues(
                start = 16f.dep(),
                end = 16f.dep(),
                top = 26f.dep(),
                bottom = bottomPadding.dep()
            ),
            verticalArrangement = Arrangement.spacedBy(13.dep())
        ) {
            items(
                items,
                key = {
                    it.id()
                }
            ) {
                if (it is ContactData){
                    PeopleCard_eq3k8h(
                        modifier = Modifier.animateItemPlacement(),
                        data = it,
                        selected = selecteds.contains(it.id),
                        contentDescription = "",
                        checkBoxContentDescription = "",
                        profileImageContentDescription = ""
                    )
                } else if (it is GroupData){
                    GroupCard_0msq1z(
                        modifier = Modifier.animateItemPlacement(),
                        config = GroupCardConfiguration.checked,
                        data = it,
                        contentDescription = "",
                        selected = groupsChecked?.get(it.id) ?: TriState.UNCHECKED
                    )
                }
            }
        }
    } else {
        NothingFoundUI()
    }
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalFoundationApi::class)
@Composable
fun AddedMembersSection(
    addedFriends: List<ContactData> = t(DataIds.addedContacts),
    notifier: NotificationService = notifier()
) {
    Box(
        modifier = Modifier
            .padding(
                end = 16.dep(),
                bottom = 16.dep()
            )
            .animateContentSize(tween(700))
    ) {
        AnimatedVisibility(
            visible = addedFriends.isNotEmpty(),
            enter = fadeIn(tween(700)),
            exit = fadeOut(tween(700))
        ) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                items(
                    addedFriends,
                    key = {
                        it.id
                    }
                ) { item ->
                    Box(
                        modifier = Modifier
                            .padding(start = 16.dep())
                            .animateItemPlacement()
                    ){
                        PeopleImageItem_r02b97(
                            onDelete = {
                                notifier.notify(DataIds.deleteAdded, item.id)
                            },
                            friend = item,
                            contentDescription = "people image"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SplitWithPageTabsSection(
    selectedIndex: Int,
    onIndexChanged: (Int) -> Unit
) {
    val tabsList = remember {
        listOf(
            ContactTabs.Recent.name,
            ContactTabs.Groups.name,
            ContactTabs.Contact.name
        )
    }
    TabRow(
        selectedTabIndex = selectedIndex,
        backgroundColor = Color.White,
        indicator = { tabPositions: List<TabPosition> ->
            Box {}
        },
        divider = { TabRowDefaults.Divider(color = Color.Transparent) }
    ) {
        tabsList.forEachIndexed { index, text ->
            val computedColor by remember(selectedIndex) {
                derivedStateOf {
                    if (selectedIndex == index) {
                        CloudBurst
                    } else {
                        Botticelli
                    }
                }
            }

            val animatedColor by animateColorAsState(
                targetValue = computedColor,
                animationSpec = tween(700)
            )

            Text(
                modifier = Modifier
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null
                    ) {
                        onIndexChanged(index)
                    }
                    .padding(0.dep()),
                text = text,
                fontSize = 21f.dep().value.sp,
                color = animatedColor,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun DeleteIcon_iw4d3p(
    modifier: Modifier = Modifier,
    config: DeleteIconConfiguration = DeleteIconConfiguration(),
    contentDescription: String,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .size(config.selectorSize.dep())
            .aspectRatio(1f, matchHeightConstraintsFirst = true)
            .border(
                width = config.selectorBorderStroke.dep(),
                color = config.selectorBorderColor,
                shape = CircleShape
            )
            .clip(CircleShape)
            .background(color = config.backgroundColor)
            .clickable {
                onClick()
            }
            .semantics { this.contentDescription = contentDescription }
    ) {
        Icon(
            modifier = Modifier
                .align(Alignment.Center)
                .size(config.selectorIconSize.dep()),
            tint = config.selectorIconTint,
            painter = painterResource(R.drawable.ic_cross),
            contentDescription = "cross icon"
        )
    }
}
