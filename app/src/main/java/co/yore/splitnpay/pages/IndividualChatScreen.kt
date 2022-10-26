package co.yore.splitnpay.pages

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.TextFieldDefaults.indicatorLine
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import co.yore.splitnpay.R
import co.yore.splitnpay.addmembers.FontFamilyText
import co.yore.splitnpay.components.components.*
import co.yore.splitnpay.components.configuration.ContactSearchBarConfiguration
import co.yore.splitnpay.components.configuration.GroupMemberProfilePicsConfiguration
import co.yore.splitnpay.components.configuration.SplitTabItemConfiguration
import co.yore.splitnpay.demos.sx
import co.yore.splitnpay.demos.sy
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.locals.RobotoText
import co.yore.splitnpay.models.BillTransaction
import co.yore.splitnpay.models.DataIds
import co.yore.splitnpay.models.Sheets
import co.yore.splitnpay.ui.theme.*
import co.yore.splitnpay.viewModels.*
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class,
    ExperimentalMotionApi::class
)
@Composable
fun IndividualChatScreen(
    modifier: Modifier = Modifier,
    conversations: List<Conversation> = listState(key = DataIds.conversations),
    groupNameString: String = stringState(key = DataIds.groupName).value,
    groupAmountString: String = stringState(key = DataIds.mobile).value,
    groupImageString: String = stringState(key = DataIds.groupImage).value,
    searchText: String = stringState(key = DataIds.searchText).value,
    notifier: NotificationService = notifier(),
    searching: Boolean = boolState(key = DataIds.search).value,
    sheeting: Sheeting = co.yore.splitnpay.libs.sheeting()
) {
    val sheetState = sheeting.sheetHandler.handle()
    val scope = rememberCoroutineScope()
    val dep = 1.dep()

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            AnimatedContent(targetState = sheeting.sheets.value) {
                sheeting[it]
            }
        },
        scrimColor = Color(0x8C243257),
        sheetBackgroundColor = Color.White,
        sheetShape = RoundedCornerShape(
            topStart = 33.dep(),
            topEnd = 33.dep()
        )
    ) {
        CollapsibleBox(
            modifier = Modifier.fillMaxSize(),
            threshold = 0.05f
        ) { progress ->
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                val startGap by remember(searching) {
                    derivedStateOf {
                        if(!searching){
                            337.02f
                        }
                        else{
                            132.3f
                        }
                    }
                }
                val endGap by remember(searching) {
                    derivedStateOf {
                        if(!searching){
                            236.96f
                        }
                        else{
                            132.3f
                        }
                    }
                }
                val contentTopGap by remember(searching) {
                    derivedStateOf {
                        if(!searching){
                            40f
                        }
                        else{
                            20f
                        }
                    }
                }
                val animatedStartGap by animateFloatAsState(targetValue = startGap)
                val animatedEndGap by animateFloatAsState(targetValue = endGap)
                val animatedContentGap by animateFloatAsState(targetValue = contentTopGap)
                MotionLayout(
                    modifier = Modifier.fillMaxSize(),
                    start = normalStartConstraint(dep,animatedStartGap),
                    end = normalEndConstraint(dep,animatedEndGap,animatedContentGap),
                    progress = progress
                ) {
                    HeaderUI(
                        groupImageString,
                        modifier,
                        groupNameString,
                        progress,
                        groupAmountString,
                        notifier,
                        searching
                    )
                    val alpha by remember(searching) {
                        derivedStateOf {
                            if(searching){
                                0f
                            }
                            else{
                                1f
                            }
                        }
                    }
                    val animatedAlpha by animateFloatAsState(targetValue = alpha)
                    if(1f-animatedAlpha>0f){
                        GroupSearchBar(
                            modifier = Modifier
                                .layoutId("searchField")
                                .alpha(1f - animatedAlpha),
                            text = searchText
                        )
                    }

                    ContentUI(
                        conversations,
                        notifier
                    )

                    if(alpha>0f){
                        SearchBar(
                            modifier = Modifier
                                .padding(end = 11.dep(), start = 18.dep())
                                .fillMaxWidth()
                                .layoutId("searchBar")
                                .alpha(animatedAlpha),
                            onFilter = {
                                notifier.notify(DataIds.filter)
                            }
                        )
                    }


                    Box(
                        modifier = Modifier
                            .padding(start = 18.dep())
                            .layoutId("tabs")
                    ) {
                        GroupChatTabs_m5q49j()
                    }

                    TopBar(
                        modifier = Modifier
                            .layoutId("topBar"),
                        alpha
                    )

                    TypingIndicator(
                        modifier = Modifier
                            .layoutId("typingIndicator")
                    )

                    Message(
                        modifier = Modifier
                            .layoutId("bottomBar")
                            .padding(
                                start = 19.dep()
                            )
                            .fillMaxWidth(),
                    )

                    Box(
                        modifier = Modifier
                            .layoutId("menuOverlay")
                            .size(24.dep())
                            .clip(CircleShape)
                            .clickable(
                                rippleRadius = 20.dep(),
                                enabled = true,
                                onClick = {
                                    notifier.notify(DataIds.settleSummaryManage)
                                },
                                rippleColor = Bluish
                            )
                    )
                }
            }
        }
    }
}