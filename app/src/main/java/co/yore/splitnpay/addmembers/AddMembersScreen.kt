package com.rudra.yoresplitbill.ui.split.group.addmembers

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import co.yore.splitnpay.*
import co.yore.splitnpay.R
import co.yore.splitnpay.addmembers.AddMemberCard_eq3k8h
import co.yore.splitnpay.addmembers.Friend
import co.yore.splitnpay.split_page.ContactSearchBar
import co.yore.splitnpay.ui.theme.DarkBlue
import co.yore.splitnpay.ui.theme.Pink
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
private fun SingleItem(
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

private data class SplitMemberItem(val id: Int, val icon: Painter, val iconName: String)
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
data class TopbarWithIconConfiguration(
    val iconSize : Float = 12f,
    val iconTint : Color = DarkBlue,
    val icon: Int = R.drawable.ic_left_arrow,
    val textSize : Float = 14f,
    val fontWeight: FontWeight = FontWeight(700),
    val fontColor: Color = DarkBlue,
    val shadowColor: Color = Color(0xff075692).copy(alpha = 0.11f),
    val blurRadius: Float = 6f,
    val offsetY: Float = 3f,
    val height: Float = 54f,
    val backgroundColor: Color = Color.White,
    val startPadding: Float = 18f,
    val space: Float = 13f
)
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
            Spacer(modifier = Modifier.width(config.startPadding.dep()))
            Icon(
                modifier = Modifier
                    .size(config.iconSize.dep())
                    .clickable {
                        onClick()
                    },
                painter = painterResource(config.icon),
                contentDescription = "back arrow",
                tint = config.iconTint
            )
            Spacer(modifier = Modifier.width(config.space.dep()))
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
fun AddMembersScreen_g5024t(

) {
    /*val fabVisible = remember { mutableStateOf(false) }
    val viewModel = AddMembersViewModel()

    if (viewModel.newlist.size > 0) {
        fabVisible.value = true
    }*/

    val coroutineScope = rememberCoroutineScope()

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

data class SplitWithPageContentConfiguration(
    val splitTextId: Int = R.string.split,
    val splitWithTextId: Int = R.string.split_with,
)

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SplitWithPageContent(
    config: SplitWithPageContentConfiguration = SplitWithPageContentConfiguration()
){
    Scaffold(
        floatingActionButton = {
            Fab()
        }
    ) {
        Column {
            TopBarWithIcon_1t9xbo(
                onClick = {

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
                ) {

                }
            }


            Spacer(modifier = Modifier.height(38.dep()))
            var selectedIndex by remember { mutableStateOf(0) }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 16f.dep(),
                        end = 16f.dep(),
                    )
            ) {
                SplitWithPageTabsSection(selectedIndex){
                    selectedIndex = it
                }
            }

            Spacer(modifier = Modifier.height(18.dep()))

            val addedFriends = remember {
                mutableStateListOf<Friend>()
            }
            AddedMembersSection(addedFriends)

            SplitWithListSection()
        }
    }
}

@Composable
fun SplitWithListSection() {

    val listState = rememberLazyListState()
    val friends = remember {
        mutableStateListOf<Friend>(
            Friend(name = "Manisha Roy", mobileNumber = "95633376942", imageUrl = ""),
            Friend(name = "Sushil Roy", mobileNumber = "95633376942", imageUrl = ""),
            Friend(name = "Ankita Roy", mobileNumber = "95633376942", imageUrl = ""),
            Friend(name = "Banhi Sen", mobileNumber = "95633376942", imageUrl = ""),
            Friend(name = "Chitrak Dutta", mobileNumber = "95633376942", imageUrl = ""),
            Friend(name = "Sanchita Sen", mobileNumber = "95633376942", imageUrl = ""),
            Friend(name = "Chumki Sen", mobileNumber = "95633376942", imageUrl = ""),
        )
    }
    LazyColumn(
        state = listState,
        modifier = Modifier,
        contentPadding = PaddingValues(
            start = 16f.dep(),
            end = 16f.dep(),
            top = 26f.dep(),
            bottom = 26f.dep()
        ),
        verticalArrangement = Arrangement.spacedBy(13.dep())
    ) {
        itemsIndexed(friends) { index, item ->
            AddMemberCard_eq3k8h(
                member = item,
                selected = false,
                contentDescription = "",
                checkBoxContentDescription = "",
                profileImageContentDescription = "",
                onSelected = {

                }
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AddedMembersSection(addedFriends: SnapshotStateList<Friend>) {
    Box(modifier = Modifier.padding(start = 16.dep(), end = 16.dep())) {
        androidx.compose.animation.AnimatedVisibility(
            visible = true,
            enter = scaleIn(),
            exit = scaleOut(),
        ) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(15.dep())
            ) {
                items(addedFriends) { item ->
                    PeopleImageItem_r02b97(
                        onClick = {

                        },
                        onDelete = {
                            addedFriends.remove(item)
                        },
                        friend = item,
                        contentDescription = "people image"
                    )
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
    val tabsList = listOf(
        ContactTabs.Recent.name,
        ContactTabs.Groups.name,
        ContactTabs.Contact.name
    )
    TabRow(
        selectedTabIndex = selectedIndex,
        backgroundColor = Color.White,
        indicator = { tabPositions: List<TabPosition> ->
            Box {}
        },
        divider = { TabRowDefaults.Divider(color = Color.Transparent) },
    ) {
        tabsList.forEachIndexed { index, text ->
            val selected = selectedIndex == index

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
                color = if (selected) colorResource(id = R.color.lightblue2)
                else colorResource(
                    id = R.color.lightgrey5
                ),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Fab() {
    AnimatedVisibility(
        visible = true,
        enter = scaleIn(),
        exit = scaleOut(),
    ) {
        FloatingActionButton(
            shape = CircleShape,
            backgroundColor = colorResource(id = R.color.pink),
            modifier = Modifier
                .size(61.dep()),
            onClick = {

            }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_forward_black_24dp_1),
                contentDescription = "next", tint = Color.Unspecified
            )
        }
    }
}

data class PeopleRowItemConfiguration(
    val imageSize: Float = 45f,
)
@Composable
fun PeopleImageItem_r02b97(
    config: PeopleRowItemConfiguration = PeopleRowItemConfiguration(),
    onClick: () -> Unit,
    onDelete: () -> Unit,
    friend: Friend,
    contentDescription: String
) {
    Box(
        modifier = Modifier
            .clickable {
                onClick()
            }
            .semantics { this.contentDescription = contentDescription }
    ) {
        AsyncImage(
            modifier = Modifier
                .clip(CircleShape)
                .size(config.imageSize.dep()),
            model = ImageRequest.Builder(LocalContext.current)
                //TODO- update using friend.imageUrl
                .data("https://i.pravatar.cc/300?" + friend.name)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.personactionbar),
            contentScale = ContentScale.Crop,
            contentDescription = ""
        )

        DeleteIcon_iw4d3p(
            config = DeleteIconConfiguration(),
            contentDescription = "",
            onClick = {
                onDelete()
            }
        )
    }
}

@Composable
fun DeleteIcon_iw4d3p(
    config: DeleteIconConfiguration = DeleteIconConfiguration(),
    contentDescription: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(
                start = 45.droot2.dep(),
                top = 45.droot2.dep()
            )
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

data class DeleteIconConfiguration(
    val selectorSize: Float = 20f,
    val backgroundColor: Color = Pink,
    val selectorBorderStroke: Float = 1f,
    val selectorBorderColor: Color = Color.White,
    val selectorIconSize: Float = 12f,
    val selectorIcon: Int = R.drawable.ic_cross,
    val selectorIconTint: Color = Color.Unspecified
)