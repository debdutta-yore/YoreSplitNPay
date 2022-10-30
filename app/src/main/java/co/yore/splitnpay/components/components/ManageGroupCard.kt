package co.yore.splitnpay.components.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import co.yore.splitnpay.R
import co.yore.splitnpay.addmembers.FontFamilyText
import co.yore.splitnpay.libs.dep
import co.yore.splitnpay.libs.sep
import co.yore.splitnpay.libs.sx
import co.yore.splitnpay.libs.sy
import co.yore.splitnpay.locals.localCurrency
import co.yore.splitnpay.models.*
import co.yore.splitnpay.ui.theme.*
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun ManageGroupCard(
    group: GroupDetailed,
    onClick: () -> Unit,
    config: GroupCardConfiguration = GroupCardConfiguration(),
    contentDescription: String
) {
    val isPayAvailable = remember {
        derivedStateOf {
            group.payWholeBalance.isNotEmpty()
        }
    }
    val isGetAvailable = remember {
        derivedStateOf {
            group.getWholeBalance.isNotEmpty()
        }
    }

    Box(
        modifier = Modifier
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
            .background(
                config.backGroundColor,
                shape = RoundedCornerShape(config.cornerRadius.dep())
            ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = config.startPadding.dep(),
                    end = config.endPadding.dep()
                ),
            verticalAlignment = CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .align(CenterVertically)
            ) {
                GroupProfileImage_l5sx9n(
                    imageUrl = group.imageUrl,
                    contentDescription = "GroupProfilePhoto"
                )
            }
            23.sx()
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .widthIn(
                        min = 56.dep(),
                        max = 70.dep()
                    ),
                verticalArrangement = Arrangement.Center
            ) {
                FontFamilyText(
                    text = group.name,
                    color = DarkBlue,
                    fontSize = 12.sep(),
                    fontWeight = Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                8.sy()
                /*GroupMemberProfilePics(
                    firstImage = group.members[0].profilePic,
                    secondImage = group.members[1].profilePic,
                    extraMembers = group.members.size - 2
                )*/
                Text("Group Card")
            }
        }

        Row(
            modifier = Modifier
                .align(CenterEnd),
            horizontalArrangement = Arrangement.End
        ) {
            Column(
                modifier = Modifier
                    .widthIn(
                        min = 58.dep(),
                        max = 124.dep()
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isGetAvailable.value) {
                    FontFamilyText(
                        annotatedString = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    letterSpacing = (-0.333333).sep(),
                                    fontFamily = robotoFonts,
                                    color = LightGreen3,
                                    fontSize = 12f.sep()
                                ),
                            ) {
                                append(localCurrency.current)
                                append(" ")
                            }
                            withStyle(
                                style = SpanStyle(
                                    letterSpacing = (-0.333333).sep(),
                                    fontFamily = robotoFonts,
                                    color = LightGreen3,
                                    fontSize = 16f.sep(),
                                    fontWeight = Bold
                                ),
                            ) {
                                append(group.getWholeBalance)
                            }
                            withStyle(
                                style = SpanStyle(
                                    letterSpacing = (-0.333333).sep(),
                                    fontFamily = robotoFonts,
                                    color = LightGreen3,
                                    fontSize = 10f.sep()
                                ),
                            ) {
                                if (
//                                    group.getWholeBalance.length <= 6 &&
                                    !group.getWholeBalance.contains('L')
                                    && !group.getWholeBalance.contains("Cr")
                                ) {
                                    append(".")
                                    append(group.getDecBalance)
                                }
                            }
                        },
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                if (isPayAvailable.value) {
                    4.sy()
                    FontFamilyText(
                        annotatedString = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    letterSpacing = (-0.333333).sep(),
                                    fontFamily = robotoFonts,
                                    color = Pink,
                                    fontSize = 12f.sep()
                                ),
                            ) {
                                append(localCurrency.current)
                                append(" ")
                            }
                            withStyle(
                                style = SpanStyle(
                                    letterSpacing = (-0.333333).sep(),
                                    fontFamily = robotoFonts,
                                    color = Pink,
                                    fontSize = 16f.sep(),
                                    fontWeight = Bold
                                ),
                            ) {
                                append(group.payWholeBalance)
                            }
                            withStyle(
                                style = SpanStyle(
                                    letterSpacing = (-0.333333).sep(),
                                    fontFamily = robotoFonts,
                                    color = Pink,
                                    fontSize = 10f.sep()
                                ),
                            ) {
                                if ( !group.getWholeBalance.contains('L')
                                    && !group.getWholeBalance.contains("Cr")
                                ) {
                                    append(".")
                                    append(group.payDecBalance)
                                }
                            }
                        },
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                8.sy()
                FontFamilyText(
                    text = stringResource(R.string.group_balance),
                    color = LightBlue4,
                    fontSize = 10.sep(),
                )
            }
            AddGroupIconButton(
                modifier = Modifier
                    .padding(
                        start = 32.dep(),
                        end = 17.dep()
                    )
                    .align(CenterVertically),
                contentDescription = "add group",
                onClicked = { onClick() },
                onPressed = { }
            )
        }
    }
}

@Composable
fun GroupProfileImage_l5sx9n(
    imageUrl: String = "https://i.pravatar.cc/300",
    config: GroupProfileImageConfiguration = GroupProfileImageConfiguration(),
    contentDescription: String
) {
    Box(
        modifier = Modifier
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
                .clip(CircleShape)
                .fillMaxSize()
                .border(
                    width = config.borderStroke.dep(),
                    color = config.borderColor,
                    shape = CircleShape
                ),
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            placeholder = painterResource(config.placeholder),
            contentScale = config.contentScale,
            contentDescription = contentDescription
        )
    }
}

@Composable
fun GroupMemberProfilePics(
    firstImage: String,
    secondImage: String,
    extraMembers: Int
) {
    val extraMembersVisible = remember {
        derivedStateOf { extraMembers > 0 }
    }
    Box() {
        SingleGroupMemberProfilePic(
            imageUrl = firstImage,
            contentDescription = "SingleGroupMemberProfileImage"
        )
        Box(
            modifier = Modifier
                .padding(start = 14.dep())
        ) {
            SingleGroupMemberProfilePic(
                imageUrl = secondImage,
                contentDescription = "SingleGroupMemberProfileImage"
            )
        }
        if (extraMembersVisible.value) {
            Box(
                modifier = Modifier
                    .padding(start = 28.dep())
            ) {
                TransparentProfilePic_k7ibvr(
                    extraCount = extraMembers,
                    contentDescription = "TransparentExtraMemberCount"
                )
            }
        }
    }
}

@Composable
fun SingleGroupMemberProfilePic(
    imageUrl: String = "https://i.pravatar.cc/300",
    contentDescription: String,
    config: SingleGroupMemberProfilePicConfiguration = SingleGroupMemberProfilePicConfiguration()
) {
    AsyncImage(
        model = imageUrl,
        contentDescription = contentDescription,
        modifier = Modifier
            .size(config.imageSize.dep())
            .clip(CircleShape)
            .border(
                BorderStroke(
                    width = config.borderWidth.dep(),
                    color = config.borderColor
                ),
                shape = CircleShape
            )
    )
}

@Composable
fun AddGroupIconButton(
    modifier: Modifier = Modifier,
    icon: Int = R.drawable.ic_add_group,
    contentDescription: String,
    config: ArrowButtonConfiguration = ArrowButtonConfiguration(),
    onClicked: () -> Unit,
    onPressed: (Boolean) -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    LaunchedEffect(key1 = isPressed) {
        onPressed(isPressed)
    }
    Box(
        modifier = modifier
            .semantics {
                this.contentDescription = contentDescription
            }
            .clip(RoundedCornerShape(config.iconButtonRadius.dep()))
            .background(
                color = Bluish,
                shape = RoundedCornerShape(config.iconButtonRadius.dep())
            )
            .size(config.iconButtonSize.dep())
            .clickable { onClicked() },
        contentAlignment = Center
    ) {
        Icon(
            modifier = Modifier
                .size(14.dep()),
            painter = painterResource(id = icon),
            contentDescription = "",
            tint = config.iconTint
        )
    }
}









