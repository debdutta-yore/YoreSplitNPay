package co.yore.splitnpay.pages

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import co.yore.splitnpay.R
import co.yore.splitnpay.addmembers.FontFamilyText
import co.yore.splitnpay.components.components.TriangleShape
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.models.DataIds
import co.yore.splitnpay.ui.theme.DarkBlue
import co.yore.splitnpay.ui.theme.LightBlue3
import co.yore.splitnpay.ui.theme.White

@Composable
fun SplitSummary() {

    val selected = remember { mutableStateOf(0) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(top = 54.dep())
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            BalanceExpenseTabs(
                selected.value
            )

            if (selected.value == 0) {
                31.sy()

//                SummarySelectableRow()

                48.sy()

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dep())
                ) {
//                    YouWillGetCard("")
                }
                33.sy()

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dep())
                ) {
//                    YouWillPayCard("")
                }
                56.sy()
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dep()),
            contentAlignment = Alignment.Center
        ) {
            TopBarWithIcon_1t9xbo(
                onClick = {
                    // todo
                },
                text = "Summary"
            )
        }
    }
}

@Composable
fun BalanceExpenseTabs(
    selected: Int,
    isExpenseTabVisible: Boolean = true,
    notifier: NotificationService = notifier()
) {
    val offsetX = animateFloatAsState(
        targetValue =
        if (selected == 0) {
            51f
        } else {
            170f
        },
        tween(500)
    )

    val balanceTabColor = animateColorAsState(
        targetValue = if (selected == 0) DarkBlue else LightBlue3,
        tween(500)
    )
    val expenseTabColor = animateColorAsState(
        targetValue = if (selected == 1) DarkBlue else LightBlue3,
        tween(500)
    )

    Box(
        modifier = Modifier
            .height(56.dep())
            .background(LightBlue6)
    ) {
        // triangle arrow

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .absoluteOffset(y = 55.5.dep()),
            color = GreyShadow1
        )

        Row(
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            18.sx()
            FontFamilyText(
                modifier = Modifier
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null,
                        onClick = {
                            notifier.notify(
                                DataIds.selectedBalanceExpenseTab,
                                0
                            )
                        }
                    ),
                text = stringResource(R.string.balance),
                color = balanceTabColor.value,
                fontSize = 21.sep(),
                fontWeight = FontWeight.Bold
            )
            if (isExpenseTabVisible) {
                42.sx()
                FontFamilyText(
                    modifier = Modifier.clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null,
                        onClick = {
                            notifier.notify(
                                DataIds.selectedBalanceExpenseTab,
                                1
                            )
                        }
                    ),
                    text = stringResource(R.string.expense),
                    color = expenseTabColor.value,
                    fontSize = 21.sep(),
                    fontWeight = FontWeight.Bold
                )
            }
        }
        val depx = with(LocalDensity.current){1.dep().toPx()}
        Box(
            modifier = Modifier
                .absoluteOffset(
                    x = offsetX.value.dep(),
                    y = 50.dep()
                )
                .background(
                    color = White,
                    shape = TriangleShape
                )
                .drawBehind {
                    drawPath(
                        Path().apply {
                            moveTo(0f, size.height)
                            lineTo(size.width / 2f, 0f)
                            lineTo(size.width, size.height)
                        },
                        Color(0xffEAEEF3),
                        style = Stroke(
                            width = depx,
                            cap = StrokeCap.Butt
                        )
                    )
                }
                .width(10.dep())
                .height(6.5.dep())
        )
    }
}
