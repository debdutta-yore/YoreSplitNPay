package co.yore.splitnpay.split_page_components // ktlint-disable package-name

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.constraintlayout.compose.layoutId
import co.yore.splitnpay.components.components.*
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.locals.RobotoText
import co.yore.splitnpay.models.*
import co.yore.splitnpay.ui.theme.robotoFonts

@Composable
fun Contents(
    selectedIndex: Int
) {
    Box(
        Modifier
            .layoutId("content")
            .background(Color.White),
        contentAlignment = Alignment.TopCenter
    ) {
        Crossfade(
            targetState = selectedIndex,
            animationSpec = tween(durationMillis = 700)
        ) {
            if (it == 0) {
                suffix("group") {
                    GroupsChildPage()
                }
            } else {
                suffix("people") {
                    PeoplesChildPage()
                }
            }
        }
    }
}

@Composable
fun GroupsChildPage(
    groups: List<GroupOrContact> = listState(DataIds.groupsAndPeoples),
    nogroup: Boolean = boolState(DataIds.noGroup).value
) {
    if (nogroup) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            NoGroupsContent()
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .padding(
                        start = 18.dep(),
                        bottom = 21.dep()
                    )
                    .height(40.dep()),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxHeight(),
                    horizontalArrangement = Arrangement.spacedBy(7.dep()),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    GiveTakeTypeTabs()
                }

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
                AddGroupButton_kbf1at()
                16.sx()
            }
            if (groups.isEmpty()){
                NothingFoundUI()
            } else {
                LazyColumn(
                    modifier = Modifier.fadingEdge(),
                    contentPadding = PaddingValues(
                        start = 17.dep(),
                        end = 17.dep(),
                        bottom = 110.dep()
                    )
                ) {
                    items(groups, key = { it.id() }) {
                        if (it is GroupData) {
                            GroupCard_0msq1z(
                                modifier = Modifier.padding(top = 13.dep()),
                                config = GroupCardConfiguration.unchecked,
                                contentDescription = "",
                                data = it
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PeoplesChildPage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .padding(
                    start = 18.dep(),
                    bottom = 21.dep()
                )
                .height(40.dep()),
            horizontalArrangement = Arrangement.spacedBy(7.dep()),
            verticalAlignment = Alignment.CenterVertically
        ) {
            GiveTakeTypeTabs()
        }
        FriendsContent()
    }
}

@Composable
fun GiveTakeTypeTabs(
    suffix: String = suffix(),
    selectedTab: SplitPageTabs = tState<SplitPageTabs>("${DataIds.subTab}$suffix").value,
    notifier: NotificationService = notifier()
) {
    val notificationId by remember(suffix) {
        derivedStateOf {
            "${DataIds.subTab}$suffix"
        }
    }
    SplitTabItem_89keto(
        text = "All",
        selected = selectedTab,
        currentTab = SplitPageTabs.All,
        contentDescription = "all"
    ) {
        notifier.notify(notificationId, SplitPageTabs.All)
    }
    SplitTabItem_89keto(
        text = "You owe",
        selected = selectedTab,
        currentTab = SplitPageTabs.YouOwe,
        contentDescription = "you_owe"
    ) {
        notifier.notify(notificationId, SplitPageTabs.YouOwe)
    }
    SplitTabItem_89keto(
        text = "You are owed",
        selected = selectedTab,
        currentTab = SplitPageTabs.YouAreOwed,
        contentDescription = "you_are_owed"
    ) {
        notifier.notify(notificationId, SplitPageTabs.YouAreOwed)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FriendsContent(
    peoples: List<GroupOrContact> = listState(DataIds.groupsAndPeoples)
) {
    val state = rememberLazyListState()
    LazyColumn(
        state = state,
        modifier = Modifier
            .fillMaxSize()
            .fadingEdge(),
        contentPadding = PaddingValues(
            start = 17.dep(),
            end = 17.dep(),
            bottom = 85.dep()
        )
    ) {
        items(
            peoples,
            key = {
                it.id()
            }
        ) {
            if (it is ContactData) {
                PeopleCard_eq3k8h(
                    modifier = Modifier
                        .animateItemPlacement()
                        .padding(top = 13.dep()),
                    data = it,
                    contentDescription = "",
                    checkBoxContentDescription = "",
                    profileImageContentDescription = "",
                    config = PeopleCardConfiguration.unCheckable
                )
            }
        }
    }
}

@Composable
fun NoGroupsContent() {
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(58.dep()))
        NoGroupHasBeenCreatedYet()
        13.sy()
        GroupCreationButton()
    }
}

@Composable
fun GroupCreationButton(
    config: GroupCreationButtonConfiguration = GroupCreationButtonConfiguration(),
    notifier: NotificationService = notifier()
) {
    Button(
        modifier = Modifier
            .width(config.width.dep())
            .height(config.height.dep()),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = config.backgroundColor
        ),
        onClick = {
            notifier.notify(DataIds.addGroup)
        }
    ) {
        Icon(
            painter = painterResource(id = config.iconId),
            contentDescription = "add group",
            tint = Color.Unspecified,
            modifier = Modifier.size(config.size.dep())
        )
    }
}

@Composable
fun NoGroupHasBeenCreatedYet(
    config: NoGroupHasBeenCreatedYetConfiguration = NoGroupHasBeenCreatedYetConfiguration()
) {
    RobotoText(
        text = stringResource(config.stringId),
        modifier = Modifier,
        textAlign = TextAlign.Center,
        color = config.color,
        fontWeight = FontWeight.Bold,
        fontSize = config.fontSize.sep()
    )
}

@Composable
fun Tabs(
    selectedIndex: Int,
    tabsList: List<String>,
    config: TabsConfiguration = TabsConfiguration(),
    onSelectionChanged: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .layoutId("tabs")
            .height(config.height.dep())
            .background(config.backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        TabRow(
            selectedTabIndex = selectedIndex,
            backgroundColor = config.backgroundColor,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = config.startPadding.dep()),
            indicator = {
                Box {}
            },
            divider = { TabRowDefaults.Divider(color = Color.Transparent) }
        ) {
            tabsList.forEachIndexed { index, text ->
                val computedColor by remember(selectedIndex) {
                    derivedStateOf {
                        if (selectedIndex == index) {
                            config.selectedColor
                        } else {
                            config.color
                        }
                    }
                }
                val tabColor by animateColorAsState(
                    targetValue = computedColor,
                    animationSpec = tween(500)
                )
                Text(
                    modifier = Modifier
                        .clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = null
                        ) {
                            onSelectionChanged(index)
                        },
                    text = text,
                    fontSize = config.fontSize.sep(),
                    color = tabColor,
                    fontWeight = FontWeight.Bold,
                    fontFamily = robotoFonts
                )
            }
        }
    }
}

@Composable
fun SplitBalanceText(
    splitTextId: Int,
    fontSize: Float,
    color: Color
) {
    RobotoText(
        stringResource(splitTextId),
        fontSize = fontSize.sep(),
        color = color
    )
}

@Composable
fun HeaderBackAndSplit(
    modifier: Modifier = Modifier,
    contentDescription: String,
    config: HeaderBackAndSplitConfiguration = HeaderBackAndSplitConfiguration(),
    notifier: NotificationService = notifier()
) {
    Row(
        modifier = modifier
            .semantics {
                this.contentDescription = contentDescription
            }
    ) {
        BackButton(
            contentDescription = "split_back_button"
        ) {
            notifier.notify(DataIds.back, null)
        }
        config.space.sx()
        RobotoText(
            stringResource(config.splitTextId),
            fontSize = config.splitFontSize.sep(),
            fontWeight = FontWeight.Bold,
            color = config.tint
        )
    }
}
