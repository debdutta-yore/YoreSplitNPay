package co.yore.splitnpay.pages

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.stringResource
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import co.yore.splitnpay.R
import co.yore.splitnpay.components.components.*
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.libs.jerokit.*
import co.yore.splitnpay.models.Conversation
import co.yore.splitnpay.models.DataIds
import co.yore.splitnpay.ui.theme.*
import co.yore.splitnpay.viewModels.*

@OptIn(ExperimentalMotionApi::class)
@Composable
fun GroupChatPage(
    modifier: Modifier = Modifier,
    conversations: List<Conversation> = listState(key = DataIds.conversations),
    groupNameString: String = stringState(key = DataIds.groupName).value,
    groupAmountString: Float = floatState(key = DataIds.groupAmount).value,
    groupImageString: String = stringState(key = DataIds.groupImage).value,
    searchText: String = stringState(key = DataIds.searchText).value,
    notifier: NotificationService = notifier(),
    searching: Boolean = boolState(key = DataIds.search).value
) {

    val scope = rememberCoroutineScope()
    val dep = 1.dep()
    CollapsibleBox(
        modifier = Modifier.fillMaxSize(),
        threshold = 0.05f
    ) { progress ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            val startGap by remember(searching) {
                derivedStateOf {
                    if (!searching){
                        337.02f
                    } else {
                        132.3f
                    }
                }
            }
            val endGap by remember(searching) {
                derivedStateOf {
                    if (!searching){
                        236.96f
                    } else {
                        132.3f
                    }
                }
            }
            val contentTopGap by remember(searching) {
                derivedStateOf {
                    if (!searching){
                        40f
                    } else {
                        20f
                    }
                }
            }
            val animatedStartGap by animateFloatAsState(targetValue = startGap)
            val animatedEndGap by animateFloatAsState(targetValue = endGap)
            val animatedContentGap by animateFloatAsState(targetValue = contentTopGap)
            MotionLayout(
                modifier = Modifier.fillMaxSize(),
                start = normalStartConstraint(dep, animatedStartGap),
                end = normalEndConstraint(dep, animatedEndGap, animatedContentGap),
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
                        if (searching){
                            0f
                        } else {
                            1f
                        }
                    }
                }
                val animatedAlpha by animateFloatAsState(targetValue = alpha)
                if (1f - animatedAlpha > 0f){
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

                if (alpha > 0f){
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
                    alpha,
                    text = stringResource(R.string.split_group)
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
                        .fillMaxWidth()
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
                            rippleColor = CuriousBlue
                        )
                )
            }
        }
    }
}
