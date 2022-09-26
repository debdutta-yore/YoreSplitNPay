package co.yore.splitnpay

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import co.yore.splitnpay.addmembers.GroupData
import co.yore.splitnpay.components.components.coloredShadow
import co.yore.splitnpay.friend_item.ArrowButton_ohezqf
import co.yore.splitnpay.ui.theme.*
import coil.compose.AsyncImage
import coil.request.ImageRequest

data class GroupCardData(
    val id: Long,
    val profileImage: String,
    val name: String,
    val memberImages: List<String>,
    val willPay: Float,
    val willGet: Float
)

@Composable
fun GroupCard_0msq1z(
    modifier: Modifier = Modifier,
    data: GroupData,
    config: GroupCardConfiguration = GroupCardConfiguration(),
    contentDescription: String
) {
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
                        color = DarkBlue,
                        fontSize = config.groupNameFontSize.sep(),
                        fontWeight = FontWeight.Bold,
                    )

                    config.groupNameProfileImagesGap.sy()

                    GroupMemberProfilePics(
                        data.memberImages
                    )
                }
                Spacer(modifier = Modifier.fillMaxWidth().weight(1f))

                if(data.showGroupBalance){
                    Column(
                        modifier = Modifier
                            .padding(vertical = config.groupBalanceTopPadding.dep())
                            .fillMaxHeight(),
                    ) {
                        RobotoText(
                            text = stringResource(R.string.group_balance),
                            color = LightBlue4,
                            fontSize = config.groupBalanceFontSize.sep(),
                        )

                        Column(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .wrapContentWidth()
                                .fillMaxHeight(),
                            verticalArrangement = Arrangement.Center
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

                ArrowButton_ohezqf(contentDescription = "arrow ",
                    pressed = false,
                    onClicked = { },
                    onPressed = {}
                )
            /*}*/
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

data class GroupMemberProfilePicsConfiguration(
    val startPadding: Float = 14f
)

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
                    image = item
                )
            }
        }
        if (invisibleImagesCount > 0) {
            Box(
                modifier = Modifier
                    .padding(start = (visibleImages.size * config.startPadding).dep())
            ) {
                TransparentProfilePic_k7ibvr(
                    member = Member(invisibleImagesCount),
                    contentDescription = "TransparentExtraMemberCount"
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
                    color = config.borderColor,
                ),
                shape = CircleShape
            )
            .padding(config.borderWidth.dep())
            .clip(CircleShape),
        placeholder = painterResource(id = config.placeholder)
    )
}

/*@Composable
fun ArrowButton_ohezqf(
    contentDescription: String,
    config: ArrowButtonConfiguration = ArrowButtonConfiguration(),
    pressed: Boolean,
    onClicked: () -> Unit,
    onPressed: (Boolean) -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val iconBackground = remember {
        derivedStateOf {
            if (!(isPressed || pressed))
                config.iconBackgroundColor
            else
                config.iconPressedColor
        }
    }
    LaunchedEffect(key1 = isPressed) {
        onPressed(isPressed)
    }

    Box(
        modifier = Modifier
            .semantics {
                this.contentDescription = contentDescription
            }
            .clip(RoundedCornerShape(config.iconButtonRadius.dep()))
            .background(
                iconBackground.value,
                shape = RoundedCornerShape(config.iconButtonRadius.dep())
            )
            .size(config.iconButtonSize.dep())
            .clickable { onClicked() },
        contentAlignment = Center
    ) {
        Icon(
            modifier = Modifier
                .size(config.iconSize.dep()),
            painter = painterResource(id = config.iconResource),
            contentDescription = "",
            tint = config.iconTint
        )
    }
}*/

data class Member(
    val extraMembers: Int = 3,
)

@Composable
fun TransparentProfilePic_k7ibvr(
    member: Member,
    config: TransparentProfilePicConfiguration = TransparentProfilePicConfiguration(),
    contentDescription: String
) {
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
        Text(
            "${member.extraMembers}+",
            color = config.fontColor,
            fontWeight = config.fontWeight,
            fontSize = config.fontSize.sep()
        )
    }
}

data class GroupProfileImageConfiguration(
    val imageSize: Float = 45f,
    val borderStroke: Float = 2.25f,
    val borderColor: Color = Color.White,
    val placeholder: Int = R.drawable.user_dummy4,
    val contentScale: ContentScale = ContentScale.Crop,
    val shadowColor: Color = DarkBlueShadow,
    val shadowBorderRadius: Float = 50f,
    val shadowBlurRadius: Float = 4.5f,
    val shadowOffsetX: Float = 0f,
    val shadowOffsetY: Float = 2.25f,
    val shadowSpread: Float = 0f
)

data class SingleGroupMemberProfilePicConfiguration(
    val imageSize: Float = 29f,
    val borderWidth: Float = 1f,
    val borderColor: Color = Color.White,
    val placeholder: Int = R.drawable.user_dummy4,
    val contentScale: ContentScale = ContentScale.Crop
)

data class TransparentProfilePicConfiguration(
    val imageSize: Float = 29f,
    val backGroundColor: Color = DarkBlue.copy(alpha = 0.65f),
    val borderWidth: Float = 1f,
    val borderColor: Color = Color.White.copy(alpha = 0.5f),
    val fontSize: Float = 12f,
    val fontColor: Color = Color.White,
    val fontWeight: FontWeight = FontWeight.Bold
)

data class ArrowButtonConfiguration(
    val iconBackgroundColor: Color = LightBlue4,
    val iconPressedColor: Color = Bluish,
    val iconTint: Color = Color.White,
    val iconButtonRadius: Float = 9f,
    val iconButtonSize: Float = 29f,
    val iconSize: Float = 11f,
    val iconResource: Int = R.drawable.ic_nextarrow,
)

data class GroupCardConfiguration(
    val shadowColor: Color = GreyShadow,
    val shadowBorderRadius: Float = 22f,
    val shadowBlurRadius: Float = 33f,
    val shadowOffsetX: Float = 7f,
    val shadowOffsetY: Float = 7f,
    val shadowSpread: Float = 0f,
    val startPadding: Float = 18f,
    val endPadding: Float = 17f,
    val height: Float = 95f,
    val cornerRadius: Float = 22f,
    val backGroundColor: Color = Color.White,
    val horizontalPadding: Float = 18f,
    val profileImageRightPadding: Float = 24f,
    val groupNameProfileImagesGap: Float = 6f,
    val arrowButtonLeftPadding: Float = 25f,
    val groupNameFontSize: Float = 12f,
    val groupBalanceTopPadding: Float = 21f,
    val groupBalanceFontSize: Float = 10f,
    val cardPaddingTop: Float = 17f,
)