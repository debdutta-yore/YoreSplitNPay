package co.yore.splitnpay.pages

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import co.yore.splitnpay.R
import co.yore.splitnpay.addmembers.PeopleCard_eq3k8h
import co.yore.splitnpay.components.components.ContactTabs
import co.yore.splitnpay.components.components.GroupCard_0msq1z
import co.yore.splitnpay.components.components.coloredShadow
import co.yore.splitnpay.components.configuration.*
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.locals.RobotoText
import co.yore.splitnpay.models.*
import co.yore.splitnpay.split_page_components.ContactSearchBar
import co.yore.splitnpay.ui.theme.robotoFonts
import co.yore.splitnpay.viewModels.TriState
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun SingleItem(
    modifier: Modifier = Modifier,
    icon: Painter, text: String,
    isSelected: Boolean = false
) {
    Row(
        modifier = modifier
            .background(
                if (isSelected)
                    Color(237, 245, 255) else Color.White
            )
            .padding(start = 31f.dep())
            .fillMaxWidth()
            .height(49f.dep())
        ,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                modifier = Modifier.size(18.dp),
                tint = Color(26, 121, 229),
                painter = icon, contentDescription = "selected photo icon"
            )
            Spacer(modifier = Modifier.width(15f.dep()))
            Text(
                text = text,
                color = Color(36, 50, 87),
                fontSize = 14.sp,
                fontWeight = FontWeight(700)
            )
        }

        if (isSelected) {
            Icon(
                modifier = Modifier
                //  .width(18f.dep())
                ,
                tint = Color(26, 121, 229),
                painter = painterResource(id = R.drawable.ic_sheet_selector),
                contentDescription = "holder"
            )
        }


    }

}


@Composable
fun MembersGroupSelectionBottomSheet(modifier: Modifier = Modifier, navController: NavController) {

    val itemList = listOf(
        SplitMemberItem(id = 0, painterResource(R.drawable.ic_splitasgroup), "Split as Group"),
        SplitMemberItem(
            id = 1,
            painterResource(id = R.drawable.ic_splitnongroup),
            "Split as Non-group"
        )
    )

    val listState = rememberLazyListState()
    var selectedIndex by remember { mutableStateOf(-1) }


    Column(
        modifier = modifier
            .fillMaxWidth()
            .height((231f - 46f).dep()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            modifier = Modifier
                .padding(top = 20.dep()),
            tint = Color(0xff5A87BB),
            painter = painterResource(id = R.drawable.ic_sheet_holder),
            contentDescription = "sheet holder"
        )

        LazyColumn(
            state = listState,
            modifier = Modifier
                .padding(top = 24f.dep())
                .fillMaxWidth()
        )
        {
            itemsIndexed(items = itemList)
            { index, _ ->

                SingleItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = itemList[index].id == selectedIndex,
                            onClick = {
                            }
                        )
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onPress = {
                                    selectedIndex = if (selectedIndex != itemList[index].id)
                                        itemList[index].id else -1

                                    if (selectedIndex == 0) {
                                        navController.navigate("group_creation_screen")
                                    }
                                    if (selectedIndex == 1) {
                                        navController.navigate("split_details_screen")
                                    }
                                },
                                onTap = { },
                                onDoubleTap = { },
                                onLongPress = { }
                            )
                        },
                    icon = itemList[index].icon,
                    text = itemList[index].iconName,
                    isSelected = itemList[index].id == selectedIndex
                )
            }
        }
    }
}

@Composable
fun TopBarWithIcon_1t9xbo(
    text: String,
    onClick: () -> Unit,
    config: TopbarWithIconConfiguration = TopbarWithIconConfiguration()
) {
    Column(
        modifier = Modifier
            .coloredShadow(
                color = config.shadowColor,
                blurRadius = config.blurRadius.dep(),
                offsetY = config.offsetY.dep()
            )
            .fillMaxWidth()
            .height(config.height.dep())
            .background(config.backgroundColor),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(modifier = Modifier.width((config.startPadding-12).dep()))
            Icon(
                modifier = Modifier
                    .size((config.iconSize + 12).dep())
                    .clip(CircleShape)
                    .clickable {
                        onClick()
                    }
                    .padding(6.dep()),
                painter = painterResource(config.icon),
                contentDescription = "back arrow",
                tint = config.iconTint
            )
            Spacer(modifier = Modifier.width((config.space-6).dep()))
            Text(
                text = text,
                fontSize = config.textSize.sep(),
                fontWeight = config.fontWeight,
                color = config.fontColor
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class)
@Composable
fun MemberSelectionPage_g5024t(

) {
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
        confirmStateChange = { it!=ModalBottomSheetValue.HalfExpanded }
    )
    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
                       Text("fdfd")
            //MembersGroupSelectionBottomSheet(navController = navController)

        },
        sheetShape = RoundedCornerShape(
            topStart = 25f.dep(),
            topEnd = 25f.dep()
        ),
        scrimColor = Color(0xff8C243257)
    ) {
        SplitWithPageContent()
    }
}


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
                enter = fadeIn(tween(300))+ scaleIn(tween(300)),
                exit = fadeOut(tween(300))+ scaleOut(tween(300))
            ){
                FloatingActionButton(
                    shape = CircleShape,
                    backgroundColor = colorResource(id = R.color.pink),
                    modifier = Modifier
                        .size(61.dep()),
                    onClick = {
                        notifier.notify(DataIds.proceedWithContacts,null)
                    }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_forward_black_24dp_1),
                        contentDescription = "next", tint = Color.Unspecified
                    )
                }
            }
        }
    ) {
        Column {
            TopBarWithIcon_1t9xbo(
                onClick = {
                    notifier.notify(DataIds.back,null)
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
                        end = 16f.dep(),
                    )
            ) {
                SplitWithPageTabsSection(selectedIndex){
                    notifier.notify(DataIds.selectedTabIndex,it)
                }
            }
            Spacer(modifier = Modifier.height(18.dep()))
            AddedMembersSection()
            GroupAndContactsUI()
        }
    }
}



@Composable
fun NothingFoundUI() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        RobotoText(
            "Nothing found",
            fontSize = 13.sep(),
            color = Color(0xff839BB9),
            fontWeight = FontWeight.Bold
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GroupAndContactsUI(
    items: List<GroupOrContact> = listState(DataIds.groupsAndPeoples),
    selecteds: List<Any> = listState(DataIds.selecteds),
    proceedWithContacts: Boolean = boolState(DataIds.proceedWithContacts).value,
    groupsChecked: Map<Any,TriState>? = safeMapState(DataIds.groupsChecked)
) {
    val bottomPadding by remember(proceedWithContacts) {
        derivedStateOf {
            if(proceedWithContacts)
                85
            else
                13
        }
    }
    if(items.isNotEmpty()){
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
                if(it is ContactData){
                    PeopleCard_eq3k8h(
                        modifier = Modifier.animateItemPlacement(),
                        data = it,
                        selected = selecteds.contains(it.id),
                        contentDescription = "",
                        checkBoxContentDescription = "",
                        profileImageContentDescription = "",
                    )
                }
                else if(it is GroupData){
                    GroupCard_0msq1z(
                        modifier = Modifier.animateItemPlacement(),
                        config = GroupCardConfiguration.checked,
                        data = it,
                        contentDescription = "",
                        selected = groupsChecked?.get(it.id)?:TriState.UNCHECKED
                    )
                }
            }
        }
    }
    else{
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
            exit = fadeOut(tween(700)),
        ) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                items(
                    addedFriends,
                    key = {
                        it.id
                    },
                ) { item ->
                    Box(
                        modifier = Modifier
                            .padding(start = 16.dep())
                            .animateItemPlacement()
                    ){
                        PeopleImageItem_r02b97(
                            onDelete = {
                                notifier.notify(DataIds.deleteAdded,item.id)
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
    onIndexChanged: (Int)->Unit
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
        divider = { TabRowDefaults.Divider(color = Color.Transparent) },
    ) {
        tabsList.forEachIndexed { index, text ->
            val computedColor by remember(selectedIndex) {
                derivedStateOf {
                    if (selectedIndex == index)
                        Color(0xff243257)
                    else
                        Color(0xffCFD8E4)
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
fun PeopleImageItem_r02b97(
    config: PeopleRowItemConfiguration = PeopleRowItemConfiguration(),
    onDelete: () -> Unit,
    friend: ContactData,
    contentDescription: String
) {
    Box(
        modifier = Modifier
            .semantics { this.contentDescription = contentDescription }
    ) {
        AsyncImage(
            modifier = Modifier
                .clip(CircleShape)
                .size(config.imageSize.dep()),
            model = ImageRequest.Builder(LocalContext.current)
                //TODO- update using friend.imageUrl
                .data(friend.image)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.personactionbar),
            contentScale = ContentScale.Crop,
            contentDescription = ""
        )

        if(friend.deletable){
            DeleteIcon_iw4d3p(
                modifier = Modifier
                    .radialBottomLeft(config.imageSize.dep()),
                config = DeleteIconConfiguration(),
                contentDescription = "",
                onClick = {
                    onDelete()
                }
            )
        }
    }
}

fun Modifier.radialBottomLeft(
    parentSize: Dp
) = layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)
    val pw = parentSize.roundToPx()
    val ph = pw
    val mw = placeable.width
    val mh = placeable.height
    val cx = (pw - mw)/2
    val cy = (ph - mh)/2
    val offset = (pw/2.8284f).toInt()
    layout(pw/2+mw/2+offset, ph/2+mh/2+offset) {
        placeable.placeRelative(cx+offset, cy+offset)
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