package co.yore.splitnpay.pages

import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import co.yore.splitnpay.R
import co.yore.splitnpay.addmembers.FontFamilyText
import co.yore.splitnpay.components.components.LightBlue3
import co.yore.splitnpay.components.components.ProfileImageConfiguration
import co.yore.splitnpay.components.components.amountAnnotatedString
import co.yore.splitnpay.components.components.coloredShadow
import co.yore.splitnpay.demos.expenseDemo.sx
import co.yore.splitnpay.demos.expenseDemo.sy
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.models.DataIds
import co.yore.splitnpay.ui.theme.*
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlin.math.roundToInt

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
                            }
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
                androidx.compose.animation.AnimatedVisibility(
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
                    color = Color(0xff5A87BB),
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

@OptIn(
    ExperimentalMaterialApi::class,
    ExperimentalFoundationApi::class,
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
    notifier: NotificationService = notifier(),
    sheeting: Sheeting = sheeting()
) {
    ModalBottomSheetLayout(
        sheetState = sheeting.sheetHandler.handle(),
        sheetContent = {
            sheeting.sheetContent()
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
                                bottomStart = 48.dep()

                            )
                        )
                        .background(
                            Color(0xff34cfcf),
                            RoundedCornerShape(
                                bottomStart = 48.dep()

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
                                topEnd = 48.dep()

                            )
                        )
                ) {

                }
            }

            Profile(
                modifier = Modifier
                    .padding(
                        start = 238.dep(),
                        top = 123.dep()
                    )
                    .size(65.dep()),
                groupImage = groupImage
            ){
                notifier.notify(DataIds.pickImage)
            }

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
                    color = Color(0xff5A87BB),
                    fontSize = 13.sep(),
                    annotatedString = buildAnnotatedString {
                        append("Created by $groupCreatedBy on ")
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold
                            )
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
                                )
                        )
                    }

                    stickyHeader {
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
                        Box(
                            modifier = Modifier.fillMaxWidth()
                        ){
                            Members(
                                modifier = Modifier
                                    .padding(
                                        start = 18.dep(),
                                        end = 18.dep(),
                                        bottom = 57.dep()
                                    ),
                                groupMembers = groupMembers
                            )
                            androidx.compose.animation.AnimatedVisibility(
                                groupMembers.any { it.isSelected },
                                modifier = Modifier
                                    .padding(
                                        bottom = 126.dep(),
                                        end = 40.dep()
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
                )
            // verticalArrangement = Arrangement.spacedBy(40.dep())
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
    leadingIcon: Painter,
    text: String,
    trailingIcon: Painter,
    switchable: Boolean = false,
    onClick: () -> Unit,
    notifier: NotificationService = notifier(),
    groupNotificationSwitch: Boolean = boolState(key = DataIds.groupNotificationSwitch).value
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
                color = Color(0xff5A87BB),
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
                        rippleColor = Color(0xff1A79E5)
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
        targetValue = if (switchON) {
            with(LocalDensity.current) { (width - thumbRadius - gapBetweenThumbAndTrackEdge).toPx() }
        } else {
            with(LocalDensity.current) { (thumbRadius + gapBetweenThumbAndTrackEdge).toPx() }
        }
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
    onClick: () -> Unit,
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
                .clip(CircleShape)
                .background(
                    White
                )
                .clickable {
                    onClick()
                },
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

// ///////////////////////////
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
                        start = 23.dep(),
                        end = 21.dep()
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
    unsettledMembers: List<SingleSettledOrUnsettledMember> = listState(key = DataIds.unsettledMembers),
    contentDescription: String,
    notifier: NotificationService = notifier(),
    config: UnSettledMembersConfiguration = UnSettledMembersConfiguration()
) {
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
            unsettledMembers.forEachIndexed { index, item ->
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
                26.sy()
            }
        }
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

// //////////////
private const val RadioAnimationDuration = 700
private val RadioButtonRippleRadius = 24.dp
private val RadioButtonPadding = 2.dp
private val RadioButtonSize = 20.dp
private val RadioRadius = RadioButtonSize / 2
private val RadioButtonDotSize = 12.dp
private val RadioStrokeWidth = 2.dp
// /////

private class MinimumTouchTargetModifier(val size: DpSize) : LayoutModifier {
    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints
    ): MeasureResult {

        val placeable = measurable.measure(constraints)

        // Be at least as big as the minimum dimension in both dimensions
        val width = maxOf(placeable.width, size.width.roundToPx())
        val height = maxOf(placeable.height, size.height.roundToPx())

        return layout(width, height) {
            val centerX = ((width - placeable.width) / 2f).roundToInt()
            val centerY = ((height - placeable.height) / 2f).roundToInt()
            placeable.place(centerX, centerY)
        }
    }

    override fun equals(other: Any?): Boolean {
        val otherModifier = other as? MinimumTouchTargetModifier ?: return false
        return size == otherModifier.size
    }

    override fun hashCode(): Int {
        return size.hashCode()
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Suppress("ModifierInspectorInfo")
fun Modifier.minimumTouchTargetSize(): Modifier = composed(
    inspectorInfo = debugInspectorInfo {
        name = "minimumTouchTargetSize"
        // TODO: b/214589635 - surface this information through the layout inspector in a better way
        //  - for now just add some information to help developers debug what this size represents.
        properties["README"] = "Adds outer padding to measure at least 48.dp (default) in " +
            "size to disambiguate touch interactions if the element would measure smaller"
    }
) {
    if (LocalMinimumTouchTargetEnforcement.current) {
        // TODO: consider using a hardcoded value of 48.dp instead to avoid inconsistent UI if the
        // LocalViewConfiguration changes across devices / during runtime.
        val size = LocalViewConfiguration.current.minimumTouchTargetSize
        MinimumTouchTargetModifier(size)
    } else {
        Modifier
    }
}

// /////
@Composable
fun MyRadioButton(
    selected: Boolean,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    colors: RadioButtonColors = RadioButtonDefaults.colors()
) {
    val dotRadius = animateDpAsState(
        targetValue = if (selected) RadioButtonDotSize / 2 else 0.dp,
        animationSpec = tween(durationMillis = RadioAnimationDuration)
    )
    val radioColor = colors.radioColor(enabled, selected)
    val selectableModifier =
        if (onClick != null) {
            Modifier.selectable(
                selected = selected,
                onClick = onClick,
                enabled = enabled,
                role = Role.RadioButton,
                interactionSource = interactionSource,
                indication = rememberRipple(
                    bounded = false,
                    radius = RadioButtonRippleRadius
                )
            )
        } else {
            Modifier
        }
    Canvas(
        modifier
            .then(
                if (onClick != null) {
                    Modifier.minimumTouchTargetSize()
                } else {
                    Modifier
                }
            )
            .then(selectableModifier)
            .wrapContentSize(Alignment.Center)
            .padding(RadioButtonPadding)
            .requiredSize(RadioButtonSize)
    ) {
        // Draw the radio button
        val strokeWidth = RadioStrokeWidth.toPx()
        drawCircle(
            radioColor.value,
            RadioRadius.toPx() - strokeWidth / 2,
            style = Stroke(strokeWidth)
        )
        if (dotRadius.value > 0.dp) {
            drawCircle(radioColor.value, dotRadius.value.toPx() - strokeWidth / 2, style = Fill)
        }
    }
}

// //////////////
@Composable
fun CustomRadioButton_2ofz97(
    contentDescription: String,
    config: CustomRadioButton = CustomRadioButton(),
    radioButtonName: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val animDuration = remember {
        700
    }
    val borderWidth by remember(isSelected) {
        derivedStateOf {
            if (isSelected) {
                config.borderWidth
            } else {
                0f
            }
        }
    }
    val animatedBorderWidth by animateFloatAsState(targetValue = borderWidth, tween(animDuration))
    val borderColor by remember(isSelected) {
        derivedStateOf{
            if (isSelected){
                config.borderColor
            } else {
                Color.Transparent
            }
        }
    }
    val animatedBorderColor by animateColorAsState(targetValue = borderColor, tween(animDuration))
    val backgroundColor by remember(isSelected) {
        derivedStateOf {
            if (isSelected) {
                config.selectedBackGroundColor
            } else {
                config.unSelectedBackGroundColor
            }
        }
    }
    val animatedBackgroundColor by animateColorAsState(targetValue = backgroundColor, tween(animDuration))
    val textColor by remember(isSelected) {
        derivedStateOf {
            if (isSelected) config.selectedTextColor
            else config.unSelectedTextColor
        }
    }
    val animatedTextColor by animateColorAsState(targetValue = textColor, tween(animDuration))
    Box(
        modifier = Modifier
            .semantics { this.contentDescription = contentDescription }
            .fillMaxWidth()
            .height(config.height.dep())
            .clip(RoundedCornerShape(config.cornerRadius.dep()))
            .background(animatedBackgroundColor)
            .border(
                width = animatedBorderWidth.dep(),
                color = animatedBorderColor,
                shape = RoundedCornerShape(config.cornerRadius.dep())
            )
            .clickable {
                onClick()
            }
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
                color = animatedTextColor,
                fontSize = config.fontSize.sep()
            )

            Spacer(modifier = Modifier.weight(1f))

            MyRadioButton(
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
    DeleteAnyway
}

data class SettledUnsettledMembersBottomSheet(
    val holderTopPadding: Float = 20f,
    val holderBottomPadding: Float = 33f
)

// //////////////////////////////
data class SingleSettledOrUnsettledMember(
    val selectedSettleOption: SettleOptions = SettleOptions.SplitIndividual,
    val isChecked: Boolean = false,
    val isSettledMember: Boolean,
    val imageUrl: String,
    val userName: String,
    val userPhNo: String,
    val getAmount: Float,
    val paidAmount: Float
)

@Composable
fun SettledMembers(
    settledMembers: List<SingleSettledOrUnsettledMember> = listState(key = DataIds.settledMembers),
    contentDescription: String = "",
    config: SettledMembersConfiguration = SettledMembersConfiguration()
) {

    Column(
        modifier = Modifier
            .semantics { this.contentDescription = contentDescription }
            .fillMaxWidth()
    ) {

        Column(modifier = Modifier.fillMaxWidth()) {

            settledMembers.forEachIndexed { index, item ->

                SettledOrUnsettledSingleRow_70d834(
                    contentDescription = " SettledOrUnsettledSingleRow",
                    member = item
                ){

                }
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

// //////////////////////////
@Composable
fun SettledOrUnsettledSingleRow_70d834(
    contentDescription: String,
    config: SettledOrUnsettledSingleRowConfiguration = SettledOrUnsettledSingleRowConfiguration(),
    member: SingleSettledOrUnsettledMember,
    onChecked: () -> Unit
) {

    val getTextColor = animateColorAsState(targetValue = if (member.getAmount > 0f) LightGreen3 else Color(0xff5A87BB))
    val paidTextColor = animateColorAsState(targetValue = if (member.paidAmount > 0f) Pink else Color(0xff5A87BB))
    val getCurrencyColor = animateColorAsState(targetValue = if (member.getAmount > 0f) LightGreen3 else DarkBlue)
    val payCurrencyColor = animateColorAsState(targetValue = if (member.paidAmount > 0f) Pink else DarkBlue)

    Row(
        modifier = Modifier
            .semantics { this.contentDescription = contentDescription }
            .padding(end = 12.dep())
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            SelectorIcon_ulkel8(
                config = CheckboxConfiguration(
                    iconColor = LightBlue4
                ),
                contentDescription = "YouWillGetCheckBox",
                selected = member.isChecked,
                onClick = onChecked
            )

            config.gapBetweenSelectorAndProfileImage.sx()

            ProfileImage_2hf7q0(
                contentDescription = "UserProfilePic"
            )
        }

        config.gapBetweenProfileImageAndUserName.sx()

        Row(modifier = Modifier.padding(top = 9.dep())) {
            Column {
                FontFamilyText(
                    text = member.userName,
                    fontSize = 12.sep(),
                    fontWeight = FontWeight.Bold,
                    color = DarkBlue
                )
                5.sy()

                FontFamilyText(
                    text = member.userPhNo,
                    fontSize = 11.sep(),
                    color = Color(0xff5A87BB)
                )
            }

            24.sx()

            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    FontFamilyText(
                        text = stringResource(R.string.get),
                        fontSize = 11.sep(),
                        color = getTextColor.value
                    )

                    FontFamilyText(
                        annotatedString = member.getAmount.amountAnnotatedString(
                            currencyFontSize = if (member.getAmount > 0f) 12f else 9f,
                            currencyTextColor = getCurrencyColor.value,
                            decNumberTextColor = getCurrencyColor.value,
                            wholeNumberTextColor = getCurrencyColor.value,
                            wholeNumberFontSize = if (member.getAmount > 0f) 12f else 9f,
                            isSpaceBetween = true,
                            wholeNumberFontWeight = FontWeight.Bold,
                            decNumberFontSize = 9f
                        ),
                        color = Color(0xff5A87BB)
                    )
                }

                5.sy()

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    FontFamilyText(
                        text = stringResource(id = R.string.paid),
                        color = paidTextColor.value,
                        fontSize = 11.sep()
                    )

                    FontFamilyText(
                        annotatedString = member.paidAmount.amountAnnotatedString(
                            currencyFontSize = if (member.paidAmount > 0f) 12f else 9f,
                            currencyTextColor = payCurrencyColor.value,
                            decNumberTextColor = payCurrencyColor.value,
                            wholeNumberTextColor = payCurrencyColor.value,
                            isSpaceBetween = true,
                            wholeNumberFontWeight = FontWeight.Bold,
                            wholeNumberFontSize = if (member.paidAmount > 0f) 12f else 10f,
                            decNumberFontSize = if (member.paidAmount > 0f) 10f else 9f
                        ),
                        color = Color(0xff5A87BB)
                    )
                }

                if (member.isSettledMember) {
                    8.sy()

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100.dep()))
                            .background(color = Whitish6)
                    ) {
                        FontFamilyText(
                            text = stringResource(id = R.string.settled),
                            fontSize = 12.sep(),
                            color = DarkBlue,
                            lineHeight = 14.sep(),
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

// ///////////////////////
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
