package co.yore.splitnpay.pages.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import co.yore.splitnpay.R
import co.yore.splitnpay.components.components.*
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.libs.jerokit.*
import co.yore.splitnpay.libs.jerokit.bottom_sheet.Sheeting
import co.yore.splitnpay.libs.jerokit.bottom_sheet.sheetContent
import co.yore.splitnpay.libs.jerokit.bottom_sheet.sheeting
import co.yore.splitnpay.models.*
import co.yore.splitnpay.pages.GroupSettingsCard
import co.yore.splitnpay.pages.Members
import co.yore.splitnpay.pages.Profile
import co.yore.splitnpay.ui.theme.*

@OptIn(
    ExperimentalMaterialApi::class,
    ExperimentalFoundationApi::class,
    ExperimentalAnimationApi::class
)
@Composable
fun GroupManageScreen(
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
        scrimColor = CloudBurst8C
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
                            Turquoise,
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
                        .background(Turquoise)
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
                    color = SteelBlue,
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
                                color = CloudBurst,
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
                                color = CloudBurst,
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
                                    backgroundColor = RadicalRed,
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