package co.yore.splitnpay.components.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import co.yore.splitnpay.R
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.locals.RobotoText
import co.yore.splitnpay.models.*
import co.yore.splitnpay.ui.theme.CloudBurst
import co.yore.splitnpay.ui.theme.WildBlueYonder
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun GroupCard_0msq1z(
    modifier: Modifier = Modifier,
    data: GroupData,
    config: GroupCardConfiguration,
    selected: TriState = TriState.UNCHECKED,
    contentDescription: String,
    notifier: NotificationService = notifier()
) {
    var _selected by remember {
        mutableStateOf(selected)
    }
    LaunchedEffect(key1 = selected){
        _selected = selected
    }
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    var isCheckBoxPressed by remember(selected) {
        mutableStateOf(false)
    }

    val focused by remember {
        derivedStateOf {
            (_selected == TriState.CHECKED) || isPressed || isCheckBoxPressed
        }
    }

    val computedBackgroundColor by remember(config) {
        derivedStateOf{
            if (focused) {
                config.cardBackgroundColor
            } else {
                config.cardUnselectedColor
            }
        }
    }
    val animatedBackgroundColor by animateColorAsState(
        targetValue = computedBackgroundColor,
        animationSpec = tween(700)
    )
    val computedBorderStroke by remember(config) {
        derivedStateOf{
            if (focused) {
                config.borderStroke
            } else {
                0f
            }
        }
    }
    val animatedBorderStroke by animateFloatAsState(
        targetValue = computedBorderStroke,
        animationSpec = tween(700)
    )
    val computedStrokeColor by remember(config) {
        derivedStateOf {
            if (focused){
                config.borderColor
            } else {
                Color.Transparent
            }
        }
    }
    val animatedStrokeColor by animateColorAsState(
        targetValue = computedStrokeColor,
        tween(700)
    )
    Box(
        modifier = modifier
            .semantics { this.contentDescription = contentDescription }
            .coloredShadow(
                color = config.shadowColor,
                borderRadius = config.shadowBorderRadius.dep(),
                blurRadius = config.shadowBlurRadius.dep(),
                offsetX = config.shadowOffsetX.dep(),
                offsetY = config.shadowOffsetY.dep()
            )
            .fillMaxWidth()
            .height(config.height.dep())
            .clip(
                RoundedCornerShape(config.cornerRadius.dep())
            )
            .clickable(
                interactionSource = interactionSource,
                indication = LocalIndication.current
            ) {
                notifier.notify(DataIds.checkGroupItem, data.id)
            }
            .border(
                width = animatedBorderStroke.dep(),
                color = animatedStrokeColor,
                shape = RoundedCornerShape(config.cornerRadius.dep())
            )
            .background(
                animatedBackgroundColor,
                shape = RoundedCornerShape(config.cornerRadius.dep())
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = config.startPadding.dep(),
                    end = config.endPadding.dep()
                ),
            verticalAlignment = CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(modifier = Modifier.align(CenterVertically)) {
                GroupProfileImage_l5sx9n(
                    imageUrl = data.image,
                    contentDescription = "GroupProfilePhoto"
                )
            }

            config.profileImageRightPadding.sx()

            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                RobotoText(
                    text = data.name,
                    color = CloudBurst,
                    fontSize = config.groupNameFontSize.sep(),
                    fontWeight = FontWeight.Bold
                )

                config.groupNameProfileImagesGap.sy()

                GroupMemberProfilePics(
                    data.members.map { it.image }
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            if (data.showGroupBalance){
                Column(
                    modifier = Modifier
                        .padding(vertical = config.groupBalanceTopPadding.dep())
                        .fillMaxHeight()
                ) {
                    RobotoText(
                        text = stringResource(R.string.group_balance),
                        color = WildBlueYonder,
                        fontSize = config.groupBalanceFontSize.sep()
                    )

                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .wrapContentWidth()
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.End
                    ) {
                        if (data.willGet > 0) {
                            val willGetSplitted by remember(data.willGet) {
                                derivedStateOf {
                                    data.willGet.splitted()
                                }
                            }
                            YoreAmount(
                                config = YoreAmountConfiguration.splitGroupCardGet,
                                whole = willGetSplitted.wholeString,
                                decimal = willGetSplitted.decString
                            )
                        }
                        if (data.willPay > 0) {
                            val willPaySplitted by remember(data.willPay) {
                                derivedStateOf {
                                    data.willPay.splitted()
                                }
                            }
                            YoreAmount(
                                config = YoreAmountConfiguration.splitGroupCardPay,
                                whole = willPaySplitted.wholeString,
                                decimal = willPaySplitted.decString
                            )
                        }
                    }
                }
            }

            config.arrowButtonLeftPadding.sx()

            when (config.type){
                GroupCardConfiguration.Type.CHECKABLE -> CheckBoxIcon_b6qwbf(
                    selected = selected,
                    contentDescription = "",
                    onClick = {
                        notifier.notify(DataIds.checkGroupItem, data.id)
                    },
                    onPressed = {
                        isCheckBoxPressed = it
                    }
                )
                GroupCardConfiguration.Type.ARROW -> ArrowButton_ohezqf(
                    contentDescription = "arrow ",
                    pressed = false,
                    onClicked = {
                        notifier.notify(DataIds.groupCardGo, data)
                    },
                    onPressed = {
                        isCheckBoxPressed = it
                    }
                )
                GroupCardConfiguration.Type.GROUP -> ArrowButton_ohezqf(
                    contentDescription = "arrow ",
                    pressed = false,
                    onClicked = {
                        notifier.notify(DataIds.groupCardGo, data)
                    },
                    onPressed = {
                        isCheckBoxPressed = it
                    },
                    config = ArrowButtonConfiguration.group
                )
            }
        }
    }
}

@Composable
fun GroupProfileImage_l5sx9n(
    imageUrl: Any?,
    config: GroupProfileImageConfiguration = GroupProfileImageConfiguration(),
    contentDescription: String
) {
    Box(
        modifier = Modifier
            .semantics { this.contentDescription = contentDescription }
            .size(config.imageSize.dep())
            .coloredShadow(
                color = config.shadowColor,
                borderRadius = config.shadowBorderRadius.dep(),
                blurRadius = config.shadowBlurRadius.dep(),
                offsetY = config.shadowOffsetY.dep(),
                offsetX = config.shadowOffsetX.dep(),
                spread = config.shadowSpread
            )
            .clip(CircleShape)
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .border(
                    width = config.borderStroke.dep(),
                    color = config.borderColor,
                    shape = CircleShape
                )
                .padding(config.borderStroke.dep())
                .clip(CircleShape),
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            placeholder = painterResource(config.placeholder),
            contentScale = ContentScale.Crop,
            contentDescription = "profile_image"
        )
    }
}

@Composable
fun GroupMemberProfilePics(
    images: List<Any?>,
    config: GroupMemberProfilePicsConfiguration = GroupMemberProfilePicsConfiguration()
) {
    val visibleImages by remember(images) {
        derivedStateOf {
            images.take(2)
        }
    }
    val invisibleImagesCount by remember(visibleImages.size) {
        derivedStateOf {
            images.size - visibleImages.size
        }
    }
    Box() {
        visibleImages.forEachIndexed { i, item ->
            Box(
                modifier = Modifier
                    .padding(start = (i * config.startPadding).dep())
            ) {
                SingleGroupMemberProfilePic(
                    contentDescription = "SingleGroupMemberProfileImage",
                    image = item,
                    config = if (config.smaller) {
                        SingleGroupMemberProfilePicConfiguration.smaller
                    } else {
                        SingleGroupMemberProfilePicConfiguration()
                    }
                )
            }
        }
        if (invisibleImagesCount > 0) {
            Box(
                modifier = Modifier
                    .padding(start = (visibleImages.size * config.startPadding).dep())
            ) {
                TransparentProfilePic_k7ibvr(
                    extraCount = invisibleImagesCount,
                    contentDescription = "TransparentExtraMemberCount",
                    config = if (config.smaller) {
                        TransparentProfilePicConfiguration.smaller
                    } else {
                        TransparentProfilePicConfiguration()
                    }
                )
            }
        }
    }
}

@Composable
fun SingleGroupMemberProfilePic(
    image: Any?,
    contentDescription: String,
    config: SingleGroupMemberProfilePicConfiguration = SingleGroupMemberProfilePicConfiguration()
) {
    AsyncImage(
        model = image,
        contentDescription = contentDescription,
        modifier = Modifier
            .size(config.imageSize.dep())
            .border(
                BorderStroke(
                    width = config.borderWidth.dep(),
                    color = config.borderColor
                ),
                shape = CircleShape
            )
            .padding(config.borderWidth.dep())
            .clip(CircleShape),
        placeholder = painterResource(id = config.placeholder)
    )
}

@Composable
fun TransparentProfilePic_k7ibvr(
    extraCount: Int,
    config: TransparentProfilePicConfiguration = TransparentProfilePicConfiguration(),
    contentDescription: String
) {
    val members = remember {
        derivedStateOf {
            if (extraCount > 9) {
                "9+"
            } else {
                "$extraCount+"
            }
        }
    }

    Box(
        modifier = Modifier
            .size(config.imageSize.dep())
            .clip(CircleShape)
            .background(
                config.backGroundColor,
                shape = CircleShape
            )
            .border(
                BorderStroke(
                    width = config.borderWidth.dep(),
                    color = config.borderColor
                ),
                shape = CircleShape
            )
            .semantics { this.contentDescription = contentDescription },
        contentAlignment = Center
    ) {
        FontFamilyText(
            text = members.value,
            color = config.fontColor,
            fontWeight = config.fontWeight,
            fontSize = config.fontSize.sep()
        )
    }
}
