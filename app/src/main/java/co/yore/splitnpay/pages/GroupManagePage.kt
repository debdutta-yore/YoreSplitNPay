package co.yore.splitnpay.pages

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import co.yore.splitnpay.R
import co.yore.splitnpay.addmembers.FontFamilyText
import co.yore.splitnpay.components.components.LightBlue3
import co.yore.splitnpay.components.components.ProfileImageConfiguration
import co.yore.splitnpay.components.components.amountAnnotatedString
import co.yore.splitnpay.components.components.coloredShadow
import co.yore.splitnpay.demos.sx
import co.yore.splitnpay.demos.sy
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.models.DataIds
import co.yore.splitnpay.ui.theme.*
import coil.compose.AsyncImage
import coil.request.ImageRequest

data class Member(
    val id: Int = 0,
    val profilePic: String,
    val userName: String,
    val mobileNo: String,
    val isSelected: Boolean,
    val isGroupAdmin: Boolean = false
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Members(
    modifier: Modifier = Modifier,
    groupMembers: List<Member> = listState(key = DataIds.groupMembers),
    notifier: NotificationService = notifier()
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .coloredShadow(
                color = GreyShadow,
                borderRadius = 20.dep(),
                blurRadius = 16.dep(),
                offsetY = 7.dep(),
                offsetX = 7.dep(),
                spread = 0f
            )
            .clip(shape = RoundedCornerShape(20.dep()))
            .background(Color.White)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(
                    top = 22.dep(),
                    start = 27.dep(),
                    bottom = 22.dep(),
                    end = 20.dep()
                ),
            verticalArrangement = Arrangement.spacedBy(18.dep())
        ) {
            groupMembers.forEachIndexed { index, item ->
                val currentMember = groupMembers[index]
                SingleMember(
                    modifier = Modifier
                        .combinedClickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = {
                                if (item.isSelected) {
                                    notifier.notify(DataIds.selectMemberClick, item)
                                }
                            },
                            onLongClick = {
                                notifier.notify(DataIds.selectMemberClick, item)
                            },
                        ),
                    leadingIcon = currentMember.profilePic,
                    userName = currentMember.userName,
                    userPhNo = currentMember.mobileNo,
                    isSelected = currentMember.isSelected,
                    isGroupAdmin = currentMember.isGroupAdmin
                )
            }
        }
    }
}

@Composable
fun SelectedIcon(
    modifier: Modifier = Modifier,
    iconBackGroundColor: Color
) {
    Box(
        modifier = modifier
            .size(18.dep())
            .aspectRatio(1f, matchHeightConstraintsFirst = true)
            .border(width = 1.dp, color = Color.White, shape = CircleShape)
            .clip(CircleShape)
            .background(color = iconBackGroundColor)

    ) {
        Icon(
            modifier = Modifier
                .align(Alignment.Center)
                .width(7.dep())
                .height(5.dep()),
            tint = Color.Unspecified,
            painter = painterResource(id = R.drawable.ic_checked_right),
            contentDescription = "checked_right"
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SingleMember(
    modifier: Modifier = Modifier,
    leadingIcon: String,
    userName: String,
    userPhNo: String,
    isSelected: Boolean = false,
    isGroupAdmin: Boolean = false
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(){
                Box(
                    modifier = Modifier
                        .size(49.dep())
                        .clip(CircleShape)
                        .border(
                            width = 3.dep(),
                            color = Color(0xffEDF5FF),
                            CircleShape
                        )
                        .padding(3.dep())
                ) {
                    AsyncImage(
                        modifier = Modifier.fillMaxSize(),
                        model = leadingIcon,
                        placeholder = painterResource(id = R.drawable.user_dummy4),
                        contentDescription = "leadingIcon",
                        contentScale = ContentScale.Crop
                    )
                }
                androidx.compose.animation.AnimatedVisibility (
                    isSelected,
                    enter = fadeIn() + scaleIn(),
                    exit = fadeOut() + scaleOut(),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                ) {
                    SelectedIcon(
                        iconBackGroundColor = LightBlue
                    )
                }
            }

            22.sx()
            Column {
                FontFamilyText(
                    text = userName,
                    color = DarkBlue,
                    fontSize = 12.sep(),
                    fontWeight = FontWeight.Bold
                )
                5.sy()
                FontFamilyText(
                    text = userPhNo,
                    color = LightBlue5,
                    fontSize = 11.sep()
                )
            }
        }
        if (isGroupAdmin) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dep()))
                    .background(color = Whitish3)
            ) {
                FontFamilyText(
                    modifier = Modifier
                        .padding(10.dep()),
                    text = stringResource(R.string.group_admin),
                    color = LightGreen3,
                    fontSize = 12.sep(),
                    letterSpacing = (-0.333333).sep(),
                    lineHeight = 14.sep()
                )
            }
        }
    }
}

val SheetScrim = Color(0x8C243257)

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class,
    ExperimentalAnimationApi::class
)
@Composable
fun GroupManagePage(
    groupNameString: String = stringState(key = DataIds.groupName).value,
    numberOfGroupMembers: Int = intState(key = DataIds.numberOfGroupMembers).value,
    groupCreatedBy: String = stringState(key = DataIds.groupCreatedBy).value,
    groupCreationDate: String = stringState(key = DataIds.groupCreationDate).value,
    groupImage: Any? = tState<Any?>(key = DataIds.groupImage).value,
    groupMembers: List<Member> = listState(key = DataIds.groupMembers),
    notifier: NotificationService = notifier()
) {
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
        confirmStateChange = { false }
    )

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            SettledUnsettledMembersBottomSheet_mxjiuq("")
        },
        sheetShape = RoundedCornerShape(
            topStart = 25f.dep(),
            topEnd = 25f.dep()
        ),
        scrimColor = SheetScrim
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(165.dep())
                        .clip(
                            RoundedCornerShape(
                                bottomStart = 48.dep(),

                                )
                        )
                        .background(
                            Color(0xff34cfcf),
                            RoundedCornerShape(
                                bottomStart = 48.dep(),

                                )
                        )
                ) {
                    Image(
                        modifier = Modifier
                            .height(212.dep())
                            .fillMaxWidth(),
                        painter = painterResource(id = R.drawable.manage_cut_fill),
                        contentScale = ContentScale.FillBounds,
                        contentDescription = "topBarBackground"
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dep())
                        .background(Color(0xff34cfcf))
                        .background(
                            Color.White,
                            RoundedCornerShape(
                                topEnd = 48.dep(),

                                )
                        )
                ) {

                }
            }

            Profile(
                modifier = Modifier
                    .offset(
                        x = 238.dep(),
                        y = 123.dep()
                    )
                    .size(65.dep()),
                groupImage = groupImage
            )

            Column {
                Row(
                    modifier = Modifier
                        .padding(
                            top = 47.dep(),
                            start = 31.dep(),
                            end = 70.dep()
                        )
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    FontFamilyText(
                        text = groupNameString,
                        color = White,
                        fontSize = 19.sep(),
                        fontWeight = FontWeight.Bold
                    )
                    FontFamilyText(
                        text = stringResource(R.string.edit),
                        color = White,
                        fontSize = 14.sep()
                    )
                }

                FontFamilyText(
                    modifier = Modifier.padding(
                        top = 8.dep(),
                        start = 31.dep()
                    ),
                    text = "$numberOfGroupMembers Participants",
                    color = White,
                    fontSize = 11.sep()
                )

                FontFamilyText(
                    modifier = Modifier
                        .padding(
                            top = 146.dep(),
                            start = 16.dep()
                        ),
                    color = LightBlue5,
                    fontSize = 13.sep(),
                    annotatedString = buildAnnotatedString {
                        append("Created by $groupCreatedBy on ")
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold
                            ),
                        ) {
                            append(groupCreationDate)
                        }
                    }
                )
                LazyColumn(
                    modifier = Modifier
                        .padding(
                            top = 21.dep()
                        )
                        .fillMaxSize()
                ){
                    stickyHeader {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White)
                                .padding(vertical = 2.dep())
                        ){
                            FontFamilyText(
                                modifier = Modifier.padding(start = 18.dep()),
                                text = stringResource(R.string.settings),
                                color = DarkBlue,
                                fontSize = 21.sep(),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    item {
                        17.sy()
                    }

                    item {
                        GroupSettingsCard(
                            modifier = Modifier
                                .padding(
                                    start = 18.dep(),
                                    end = 18.dep()
                                ),
                        )
                    }

                    item {
                        50.sy()
                    }

                    stickyHeader {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White)
                                .padding(vertical = 2.dep())
                        ){
                            FontFamilyText(
                                modifier = Modifier.padding(start = 18.dep()),
                                text = stringResource(R.string.members),
                                color = DarkBlue,
                                fontSize = 21.sep(),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    item {
                        17.sy()
                    }

                    item {
                        Members(
                            modifier = Modifier
                                .padding(
                                    start = 18.dep(),
                                    end = 18.dep(),
                                    bottom = 57.dep()
                                ),
                            groupMembers = groupMembers
                        )
                    }
                }
            }

            AnimatedVisibility (
                groupMembers.any { it.isSelected },
                modifier = Modifier
                    .padding(
                        bottom = 63.dep(),
                        end = 20.dep()
                    )
                    .size(47.dep())
                    .align(Alignment.BottomEnd),
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                FloatingActionButton(
                    modifier = Modifier
                        .size(47.dep()),
                    backgroundColor = Pink,
                    onClick = {
                        notifier.notify(DataIds.deleteMembersClick)
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_delete_white),
                        contentDescription = "delete member icon",
                        tint = Color.Unspecified
                    )
                }
            }
        }
    }
}

@Composable
fun GroupSettingsCard(
    modifier: Modifier = Modifier,
    notifier: NotificationService = notifier()
) {
    Box(
        modifier = modifier
            .coloredShadow(
                color = GreyShadow,
                borderRadius = 20.dep(),
                blurRadius = 16.dep(),
                offsetY = 7.dep(),
                offsetX = 7.dep(),
                spread = 0f
            )
            .clip(shape = RoundedCornerShape(20.dep()))
            .background(White)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 41.dep(),
                    bottom = 41.dep()
                ),
            //verticalArrangement = Arrangement.spacedBy(40.dep())
        ) {
            SingleSetting(
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = painterResource(id = R.drawable.ic_person_add),
                text = "Add Member",
                trailingIcon = painterResource(id = R.drawable.menu_right_arrow),
                onClick = {
                    notifier.notify(DataIds.addMemberClick)
                }
            )

            SingleSetting(
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = painterResource(id = R.drawable.ic_notification),
                text = "Mute notifications",
                trailingIcon = painterResource(id = R.drawable.menu_right_arrow),
                switchable = true,
                onClick = {
                    notifier.notify(DataIds.groupNotificationSwitch)
                }
            )

            SingleSetting(
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = painterResource(id = R.drawable.ic_link),
                text = "Invite via link",
                trailingIcon = painterResource(id = R.drawable.menu_right_arrow),
                onClick = {
                    notifier.notify(DataIds.inviteViaLinkClick)
                }
            )

            SingleSetting(
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = painterResource(id = R.drawable.ic_logout),
                text = "Leave Group",
                trailingIcon = painterResource(id = R.drawable.menu_right_arrow),
                onClick = {
                    notifier.notify(DataIds.leaveGroupClick)
                }
            )

            SingleSetting(
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = painterResource(id = R.drawable.ic_delete),
                text = "Delete Group",
                trailingIcon = painterResource(id = R.drawable.menu_right_arrow),
                onClick = {
                    notifier.notify(DataIds.deleteGroupClick)
                }
            )
        }
    }
}

@Composable
fun SingleSetting(
    modifier: Modifier = Modifier,
    leadingIcon: Painter, text: String,
    trailingIcon: Painter,
    switchable: Boolean = false,
    onClick: () -> Unit,
    notifier: NotificationService = notifier(),
    groupNotificationSwitch: Boolean = boolState(key = DataIds.groupNotificationSwitch).value,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dep())
            .clickable(
                rippleColor = Color(0xff1A79E5),
                onClick = {
                    onClick()
                }
            )
            .padding(horizontal = 23.dep()),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(24.dep()),
                painter = leadingIcon,
                tint = Color.Unspecified,
                contentDescription = "leadingIcon"
            )
            36.sx()
            FontFamilyText(
                text = text,
                color = LightBlue5,
                fontSize = 14.sep()
            )
        }
        if (switchable) {
            CustomSwitch(
                scale = 1f,
                width = 36.dep(),
                height = 20.dep(),
                strokeWidth = 1.dep(),
                checkedTrackColor = Color(0xff37D8CF),
                uncheckedTrackColor = LightBlue4,
                gapBetweenThumbAndTrackEdge = 3.dep(),
                onSwitch = {
                    notifier.notify(DataIds.groupNotificationSwitch)
                },
                switchON = groupNotificationSwitch
            )
        } else {
            Icon(
                modifier = Modifier
                    .size(32.dep())
                    .clip(CircleShape)
                    .clickable(
                        rippleColor = Color(0xff1A79E5),
                    ) {

                    }
                    .padding(12.5.dep()),
                painter = trailingIcon,
                tint = Bluish,
                contentDescription = "trailingIcon"
            )
        }
    }
}

@Composable
fun CustomSwitch(
    switchON: Boolean,
    scale: Float = 2f,
    width: Dp = 36.dep(),
    height: Dp = 20.dep(),
    strokeWidth: Dp = 2.dep(),
    checkedTrackColor: Color = LightBlue4,
    uncheckedTrackColor: Color = LightBlue4,
    checkedBackgroundColor: Color = Color(0xffE4FFFA),
    unCheckedBackgroundColor: Color = colorResource(R.color.lightblue1),
    gapBetweenThumbAndTrackEdge: Dp = 4.dep(),
    onSwitch: () -> Unit
) {


    val thumbRadius = (height / 2) - gapBetweenThumbAndTrackEdge

    // To move thumb, we need to calculate the position (along x axis)
    val animatePosition = animateFloatAsState(
        targetValue = if (switchON)
            with(LocalDensity.current) { (width - thumbRadius - gapBetweenThumbAndTrackEdge).toPx() }
        else
            with(LocalDensity.current) { (thumbRadius + gapBetweenThumbAndTrackEdge).toPx() }
    )


    Canvas(
        modifier = Modifier
            .size(width = width, height = height)
            .scale(scale = scale)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        onSwitch()
                        // This is called when the user taps on the canvas
//                        switchON = !switchON
                    }
                )
            }
    ) {


        val radius = 200f
        drawRoundRect(
            color = if (switchON) checkedBackgroundColor else unCheckedBackgroundColor,
            cornerRadius = CornerRadius(x = radius, y = radius),
            style = Fill
        )
        // Track
        drawRoundRect(
            color = if (switchON) checkedTrackColor else uncheckedTrackColor,
            cornerRadius = CornerRadius(x = radius, y = radius),
            style = Stroke(width = strokeWidth.toPx())
        )

        // Thumb
        drawCircle(
            color = if (switchON) checkedTrackColor else uncheckedTrackColor,
            radius = thumbRadius.toPx(),
            center = Offset(
                x = animatePosition.value,
                y = size.height / 2
            )
        )
    }
}

@Composable
fun Profile(
    modifier: Modifier = Modifier,
    groupImage: Any?,
) {
    Box(
        modifier = modifier
    ) {
        AsyncImage(
            modifier = Modifier
                .coloredShadow(
                    color = DarkBlueShadow,
                    offsetY = 3.75.dep(),
                    offsetX = 0.dep(),
                    blurRadius = 7.dep(),
                    borderRadius = 100.dep()
                )
                .border(
                    color = White,
                    width = 2.dep(),
                    shape = CircleShape
                )
                .size(68.dep())
                .clip(CircleShape),
            model = groupImage,
            contentDescription = "profile pic background",
            contentScale = ContentScale.FillBounds
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    start = 27.droot2.dep(),
                    top = 27.droot2.dep()
                )
                .size(27.dep())
                .background(
                    White,
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier
                    .width(15.dep())
                    .height(13.5.dep()),
                painter = painterResource(id = R.drawable.ic_camera),
                tint = Color.Unspecified,
                contentDescription = "cross icon"
            )
        }
    }
}

/////////////////////////////
@Composable
fun SettledUnsettledMembersBottomSheet_mxjiuq(
    contentDescription: String,
    config: SettledUnsettledMembersBottomSheet = SettledUnsettledMembersBottomSheet()
) {

    var isSettledMemberSelected by remember {
        mutableStateOf(true)
    }
    val localDensity = LocalDensity.current
    var heightIs by remember {
        mutableStateOf(0.dp)
    }

    Column(
        modifier = Modifier
            .semantics { this.contentDescription = contentDescription }
    ) {
        Icon(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = config.holderTopPadding.dep()),
            tint = Color.Unspecified,
            painter = painterResource(id = R.drawable.ic_sheet_holder),
            contentDescription = "sheetHolder"
        )

        Spacer(modifier = Modifier.height(config.holderBottomPadding.dep()))


        Row(
            modifier = Modifier
                .padding(start = 42.dep())
        ) {
            FontFamilyText(
                modifier = Modifier.clickable { isSettledMemberSelected = true },
                text = "Settled Members",
                color = if (isSettledMemberSelected) DarkBlue else LightBlue3,
                fontSize = 16.sep(),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(21.dep()))

            FontFamilyText(
                modifier = Modifier.clickable { isSettledMemberSelected = false },
                text = "Unsettled Members",
                color = if (isSettledMemberSelected) LightBlue3 else DarkBlue,
                fontSize = 16.sep(),
                fontWeight = FontWeight.Bold
            )

        }

        if (isSettledMemberSelected) {
            Box(
                modifier = Modifier
                    .onGloballyPositioned { coordinates ->
                        heightIs = with(localDensity) { coordinates.size.height.toDp() }
                    }
                    .wrapContentHeight()
                    .padding(
                        top = 14.dep(),
                        start = 31.dep(),
                        end = 43.dep()
                    )

            ) {
                SettledMembers(contentDescription = "Settled Members")
            }
        } else {
            Box(
                modifier = Modifier
                    .height(heightIs)
                    .padding(
                        top = 14.dep(),
                        start = 23.dep(), end = 21.dep()
                    )

            ) {
                UnSettledMemberItem(
                    contentDescription = "UnSettled Members"
                )
            }
        }
    }
}

data class UnSettledMembersConfiguration(
    val gapBetweenTwoRow: Float = 33f,
    val topPaddingOfRadioButton: Float = 14f,
    val topPaddingOfButton: Float = 26f,
    val bottomPaddingOfButton: Float = 25f
)

@Composable
fun UnSettledMemberItem(
    contentDescription: String,
    config: UnSettledMembersConfiguration = UnSettledMembersConfiguration()
) {

    var selected by remember { mutableStateOf(SettleOptions.SplitIndividual) }
    Column(
        modifier = Modifier
            .semantics { this.contentDescription = contentDescription }
    ) {


        Column(
            modifier = Modifier
                .padding(
                    start = 8.dep(),
                    end = 22.dep()
                )
                .fillMaxWidth()
        ) {

            list.forEachIndexed { index, item ->

                SettledOrUnsettledSingleRow_70d834(
                    contentDescription = " SettledOrUnsettledSingleRow",
                    userName = item.userName,
                    userPhNo = item.userPhNo,
                    getAmount = item.getAmount,
                    paidAMount = item.paidAmount,
                    isChecked = item.isChecked,
                    isSettledMember = item.isSettledMember
                ) {
                    list = list.mapIndexed { j, item ->
                        if (index == j) {
                            item.copy(isChecked = !item.isChecked)
                        } else item
                    }

                }
                if (index != list.lastIndex)
                    Spacer(modifier = Modifier.height(config.gapBetweenTwoRow.dep()))

            }

        }

        Spacer(modifier = Modifier.height(config.topPaddingOfRadioButton.dep()))

        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            CustomRadioButton_2ofz97(
                contentDescription = "CustomRadioButton",
                radioButtonName = "Split Individually",
                isSelected = selected == SettleOptions.SplitIndividual
            )
            {
                selected = SettleOptions.SplitIndividual

            }
            Spacer(modifier = Modifier.height(5.dep()))
            CustomRadioButton_2ofz97(
                contentDescription = "CustomRadioButton",
                radioButtonName = "Delete anyway",
                isSelected = selected == SettleOptions.Delete
            )
            {
                selected = SettleOptions.Delete
            }
        }

        Spacer(modifier = Modifier.height(config.topPaddingOfButton.dep()))
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
                text = "Continue",
                onClick = { },
                contentDescription = "ContinueButton"
            )
        }

        Spacer(modifier = Modifier.height(config.bottomPaddingOfButton.dep()))

    }
}

val Whitish5 = Color(0xFFF8FBFF)

data class CustomRadioButton(
    val height: Float = 47f,
    val cornerRadius: Float = 12f,
    val selectedBackGroundColor: Color = Whitish5,
    val unSelectedBackGroundColor: Color = Color.White,
    val borderColor: Color = Bluish,
    val borderWidth: Float = 1f,
    val selectedTextColor: Color = Bluish,
    val unSelectedTextColor: Color = DarkBlue,
    val fontSize: Float = 14f
)

@Composable
fun CustomRadioButton_2ofz97(
    contentDescription: String,
    config: CustomRadioButton = CustomRadioButton(),
    radioButtonName: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .semantics { this.contentDescription = contentDescription }
            .fillMaxWidth()
            .height(config.height.dep())
            .clip(RoundedCornerShape(config.cornerRadius.dep()))
            .background(
                if (isSelected) config.selectedBackGroundColor
                else config.unSelectedBackGroundColor
            )
            .border(
                width = if (isSelected) config.borderWidth.dep()
                else (-config.borderWidth).dep(),
                color = config.borderColor,
                shape = RoundedCornerShape(config.cornerRadius.dep())
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FontFamilyText(
                text = radioButtonName,
                modifier = Modifier
                    .padding(start = 27.dep()),
                color = if (isSelected) config.selectedTextColor
                else config.unSelectedTextColor,
                fontSize = config.fontSize.sep()
            )

            Spacer(modifier = Modifier.weight(1f))

            RadioButton(
                selected = isSelected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(
                    selectedColor = Bluish,
                    unselectedColor = GreyBorder,
                    disabledColor = LightGrayShadow
                )
            )
        }
    }
}

val LightGrayShadow = Color(0xffE6E5E5)

enum class SettleOptions {
    SplitIndividual,
    Delete
}

data class SettledUnsettledMembersBottomSheet(
    val holderTopPadding: Float = 20f,
    val holderBottomPadding: Float = 33f
)

////////////////////////////////
data class SingleSettledOrUnsettledMember(
    val isChecked: Boolean = false,
    val isSettledMember: Boolean,
    val imageUrl: String,
    val userName: String,
    val userPhNo: String,
    val getAmount: Float,
    val paidAmount: Float
)

private var list by mutableStateOf(
    listOf(
        SingleSettledOrUnsettledMember(
            isSettledMember = true,
            imageUrl = "imageUrl",
            userName = "Manisha Roy",
            userPhNo = "9563376942",
            getAmount = 0f,
            paidAmount = 600f
        ),

        SingleSettledOrUnsettledMember(
            isSettledMember = true,
            imageUrl = "imageUrl",
            userName = "Sanjana Ray",
            userPhNo = "9563376942",
            getAmount = 0f,
            paidAmount = 600f
        ),
    )
)

@Composable
fun SettledMembers(
    contentDescription: String,
    config: SettledMembersConfiguration = SettledMembersConfiguration()
) {

    Column(modifier = Modifier
        .semantics { this.contentDescription = contentDescription }
        .fillMaxWidth()) {

        Column(modifier = Modifier.fillMaxWidth()) {

            list.forEachIndexed { index, item ->

                SettledOrUnsettledSingleRow_70d834(
                    contentDescription = " SettledOrUnsettledSingleRow",
                    userName = item.userName,
                    userPhNo = item.userPhNo,
                    getAmount = item.getAmount,
                    paidAMount = item.paidAmount,
                    isChecked = item.isChecked,
                    isSettledMember = item.isSettledMember
                )
                {
                    list = list.mapIndexed { j, item ->
                        if (index == j) {
                            item.copy(isChecked = !item.isChecked)
                        } else item
                    }

                }

                if (index != list.lastIndex)
                    Spacer(modifier = Modifier.height(33.dep()))

            }

        }

        Spacer(modifier = Modifier.height(config.topPaddingOfBottom.dep()))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dep())
        ) {

            CustomButton_3egxtx(
                text = "Continue",
                onClick = { },
                contentDescription = "ContinueButton"
            )
        }

        Spacer(modifier = Modifier.height(config.bottomPaddingOfBottom.dep()))
    }
}

data class SettledMembersConfiguration(
    val gapBetweenTwoRow: Float = 33f,
    val topPaddingOfBottom: Float = 38f,
    val bottomPaddingOfBottom: Float = 25f
)

////////////////////////////
@Composable
fun SettledOrUnsettledSingleRow_70d834(
    contentDescription: String,
    config: SettledOrUnsettledSingleRowConfiguration = SettledOrUnsettledSingleRowConfiguration(),
    userName: String,
    userPhNo: String,
    getAmount: Float,
    paidAMount: Float,
    isSettledMember: Boolean,
    isChecked: Boolean,
    onChecked: () -> Unit
) {
    Row(
        modifier = Modifier.semantics { this.contentDescription = contentDescription }
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {

            SelectorIcon_ulkel8(
                config = CheckboxConfiguration(
                    iconColor = LightBlue4
                ),
                contentDescription = "YouWillGetCheckBox",
                selected = isChecked,
                onClick = onChecked
            )

            Spacer(modifier = Modifier.width(config.gapBetweenSelectorAndProfileImage.dep()))

            ProfileImage_2hf7q0(
                contentDescription = "UserProfilePic"
            )
        }

        Spacer(modifier = Modifier.width(config.gapBetweenProfileImageAndUserName.dep()))

        Row(modifier = Modifier.padding(top = 9.dep())) {
            Column(modifier = Modifier) {

                FontFamilyText(
                    text = userName,
                    fontSize = 12.sep(),
                    fontWeight = FontWeight.Bold,
                    color = DarkBlue
                )
                Spacer(modifier = Modifier.height(5.dep()))

                FontFamilyText(
                    text = userPhNo,
                    fontSize = 11.sep(),
                    style = MaterialTheme.typography.h1,
                    color = LightBlue5
                )
            }

            Spacer(modifier = Modifier.width(24.dep()))

            Column(modifier = Modifier.fillMaxWidth()) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    FontFamilyText(
                        text = "Get",
                        fontSize = 11.sep(),
                        fontWeight = FontWeight(400),
                        color = if (isSettledMember) LightBlue5 else LightGreen3
                    )

                    Text(
                        getAmount.amountAnnotatedString(
                            currencyFontSize = if (isSettledMember) 9f else 12f,
                            currencyTextColor = if (isSettledMember) DarkBlue else LightGreen3,
                            decNumberTextColor = if (isSettledMember) DarkBlue else LightGreen3,
                            wholeNumberTextColor = if (isSettledMember) DarkBlue else LightGreen3,
                            wholeNumberFontSize = if (isSettledMember) 10f else 12f,
                            isSpaceBetween = true,
                            wholeNumberFontWeight = FontWeight.Bold,
                            decNumberFontSize = 10f
                        ),
                        color = LightBlue5
                    )
                }

                Spacer(modifier = Modifier.height(5.dep()))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    FontFamilyText(
                        text = "Paid",
                        color = if (isSettledMember) Pink else LightBlue5,
                        fontSize = 11.sep(),
                    )

                    Text(
                        paidAMount.amountAnnotatedString(
                            currencyFontSize = if (isSettledMember) 12f else 9f,
                            currencyTextColor = if (isSettledMember) Pink else DarkBlue,
                            decNumberTextColor = if (isSettledMember) Pink else DarkBlue,
                            wholeNumberTextColor = if (isSettledMember) Pink else DarkBlue,
                            isSpaceBetween = true,
                            wholeNumberFontWeight = FontWeight.Bold,
                            wholeNumberFontSize = if (isSettledMember) 12f else 10f,
                            decNumberFontSize = if (isSettledMember) 10f else 8f,
                        ),
                        color = LightBlue5
                    )
                }

                if (isSettledMember) {
                    Spacer(modifier = Modifier.height(8.dep()))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100.dep()))
                            .background(color = Whitish6)
                    ) {
                        FontFamilyText(
                            text = "Settled",
                            fontSize = 12.sep(),
                            color = DarkBlue,
                            lineHeight = 14.sep(),
                            fontWeight = FontWeight(400),
                            modifier = Modifier
                                .padding(
                                    horizontal = 8.dep(),
                                    vertical = 4.dep()
                                )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SelectorIcon_ulkel8(
    selected: Boolean,
    config: CheckboxConfiguration = CheckboxConfiguration(),
    onClick: () -> Unit,
    contentDescription: String
) {
    val color by remember(selected) {
        derivedStateOf{
            if (selected) config.iconColor else Color.White
        }
    }
    val animatedColor by animateColorAsState(targetValue = color, animationSpec = tween(500))
    val stroke by remember(selected) {
        derivedStateOf{
            if (selected) 0f else 1f
        }
    }
    val animatedStroke by animateFloatAsState(targetValue = stroke, animationSpec = tween(500))
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dep()))
            .size(config.iconSize.dep())
            .background(animatedColor)
            .border(
                animatedStroke.dep(),
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
                .align(Alignment.Center),
            painter = painterResource(config.icon),
            contentDescription = "check icon",
            tint = Color.Unspecified
        )
    }
}

val Whitish6 = Color(0xFFE7EEF6)

data class SettledOrUnsettledSingleRowConfiguration(
    val gapBetweenSelectorAndProfileImage: Float = 12f,
    val gapBetweenProfileImageAndUserName: Float = 22f
)

data class CheckboxConfiguration(
    val iconColor: Color = Bluish,
    val iconSize: Float = 28f,
    val icon: Int = R.drawable.ic_checked_right
)

/////////////////////////
@Composable
fun ProfileImage_2hf7q0(
    config: ProfileImageConfiguration = ProfileImageConfiguration(),
    contentDescription: String
) {
    AsyncImage(
        modifier = Modifier
            .size(config.imageSize.dep())
            .border(
                width = config.borderStroke.dep(),
                color = config.borderColor,
                shape = config.shape
            )
            .padding(config.borderStroke.dep())
            .clip(CircleShape),
        model = ImageRequest.Builder(LocalContext.current)
            .data(config.imageUrl)
            .crossfade(true)
            .build(),
        placeholder = painterResource(config.placeholder),
        contentScale = config.contentScale,
        contentDescription = contentDescription
    )
}