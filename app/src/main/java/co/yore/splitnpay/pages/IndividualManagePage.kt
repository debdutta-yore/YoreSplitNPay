package co.yore.splitnpay.pages

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import co.yore.splitnpay.R
import co.yore.splitnpay.components.components.FontFamilyText
import co.yore.splitnpay.components.components.GroupCard_0msq1z
import co.yore.splitnpay.components.components.coloredShadow
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.models.DataIds
import co.yore.splitnpay.models.GroupCardConfiguration
import co.yore.splitnpay.models.GroupData
import co.yore.splitnpay.ui.theme.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IndividualManagePage(
    memberName: String = stringState(key = DataIds.memberName).value,
    memberPhoneNumber: String = stringState(key = DataIds.memberMobile).value,
    memberImage: Any? = stringState(key = DataIds.memberImage).value,
    memberJoiningDate: String = stringState(key = DataIds.memberJoiningDate).value,
    notifier: NotificationService = notifier()
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        /*Image(
            modifier = Modifier
                .height(212.dep())
                .fillMaxWidth(),
            painter = painterResource(id = R.drawable.manage_cut_fill),
            contentScale = ContentScale.FillBounds,
            contentDescription = "topBarBackground"
        )*/

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
                .offset(
                    x = 238.dep(),
                    y = 123.dep()
                )
                .size(65.dep()),
            groupImage = memberImage,
            onClick = {
                notifier.notify(DataIds.groupImageClick)
            }
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
                    text = memberName,
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
                text = memberPhoneNumber,
                color = White,
                fontSize = 14.sep()
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
                    append(stringResource(R.string.joined_on) + " ")
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append(memberJoiningDate)
                    }
                }
            )

            val state = rememberLazyListState()

            LazyColumn(
                modifier = Modifier
                    .padding(
                        top = 21.dep()
                    )
                    .fillMaxWidth()
                    .fadingEdge(),
                state = state,
                contentPadding = PaddingValues(top = 4.dep(), bottom = 24.dep())
            ) {
                stickyHeader {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color.White)
                    ) {
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
                    IndividualSettingsCard(
                        modifier = Modifier
                            .padding(
                                start = 18.dep(),
                                end = 18.dep(),
                                top = 17.dep(),
                                bottom = 18.dep()
                            )
                            .coloredShadow(
                                color = Ghost80,
                                borderRadius = 20.dep(),
                                blurRadius = 33.dep(),
                                offsetY = 7.dep(),
                                offsetX = 7.dep(),
                                spread = 0f
                            )
                            .background(
                                color = White,
                                shape = RoundedCornerShape(20.dep())
                            )
                            .fillMaxWidth()
                    )
                }
                stickyHeader {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color.White)
                    ) {
                        FontFamilyText(
                            modifier = Modifier.padding(start = 18.dep()),
                            text = stringResource(R.string.groups),
                            color = CloudBurst,
                            fontSize = 21.sep(),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                item {
                    Groups(
                        modifier = Modifier
                            .padding(
                                top = 0.dep(),
                                start = 18.dep(),
                                end = 18.dep()
                            )
                    )
                }
            }
        }
    }
}

@Composable
fun Groups(
    modifier: Modifier = Modifier,
    groups: List<GroupData> = listState(key = DataIds.groups),
    notifier: NotificationService = notifier()
) {
    Column(
        modifier = modifier
            .padding(
//                top = 22.dep(),
//                bottom = 22.dep(),
//                    start = 27.dep(),
//                    end = 20.dep()
            )
            .fillMaxSize()
    ) {
        groups.forEachIndexed { index, item ->
            /*ManageGroupCard(
                group = item,
                contentDescription = "",
                onClick = {

                }
            )*/
            GroupCard_0msq1z(
                modifier = Modifier.padding(top = 13.dep()),
                config = GroupCardConfiguration.GROUP,
                contentDescription = "",
                data = item
            )
            14.sy()
        }
    }
}
