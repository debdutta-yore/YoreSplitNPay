package co.yore.splitnpay.components.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.yore.splitnpay.R
import co.yore.splitnpay.addmembers.FontFamilyText
import co.yore.splitnpay.demos.sx
import co.yore.splitnpay.demos.sy
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.models.DataIds
import co.yore.splitnpay.pages.*
import co.yore.splitnpay.ui.theme.DarkBlue

@Composable
fun SettledUnsettledMembersBottomSheet_mxjiuq(
    settledMembers: List<SingleSettledOrUnsettledMember> = listState(key = DataIds.settledMembers),
    unsettledMembers: List<SingleSettledOrUnsettledMember> = listState(key = DataIds.unsettledMembers),
    contentDescription: String = "",
    config: SettledUnsettledMembersBottomSheet = SettledUnsettledMembersBottomSheet(),
    notifier: NotificationService = notifier()
) {
    var isSettledMemberSelected by remember {
        mutableStateOf(true)
    }
    val settledTabTextColor = animateColorAsState(targetValue = if (isSettledMemberSelected) DarkBlue  else LightBlue3)
    val unsettledTabTextColor = animateColorAsState(targetValue = if (isSettledMemberSelected) LightBlue3 else DarkBlue)

    Column(
        modifier = Modifier
            .semantics { this.contentDescription = contentDescription }
    ) {
        config.holderTopPadding.sy()
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50.dep()))
                .height(2.dep())
                .width(19.dep())
                .background(LightBlue5)
                .align(Alignment.CenterHorizontally)
        )
        config.holderBottomPadding.sy()

        Row(
            modifier = Modifier
                .padding(start = 42.dep())
        ) {
            FontFamilyText(
                modifier = Modifier.clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null
                )  { isSettledMemberSelected = true },
                text = stringResource(R.string.settled_members),
                color = settledTabTextColor.value,
                fontSize = 16.sep(),
                fontWeight = FontWeight.Bold
            )
            21.sx()

            FontFamilyText(
                modifier = Modifier.clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null
                )  { isSettledMemberSelected = false },
                text = stringResource(R.string.unsettled_memebers),
                color = unsettledTabTextColor.value,
                fontSize = 16.sep(),
                fontWeight = FontWeight.Bold
            )
        }

        if (isSettledMemberSelected) {
            Box(
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(
                        top = 14.dep(),
                        start = 31.dep(),
                        end = 43.dep()
                    )

            ) {
                LazyColumn(){
                    items(settledMembers){
                        SettledOrUnsettledSingleRow_70d834(
                            contentDescription = " SettledOrUnsettledSingleRow",
                            member = it
                        ){

                        }
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .padding(
                        top = 14.dep(),
                        start = 23.dep(), end = 21.dep()
                    )
            ) {
                LazyColumn(){
                    items(unsettledMembers){
                        SettledOrUnsettledSingleRow_70d834(
                            contentDescription = " SettledOrUnsettledSingleRow",
                            member = it
                        ){

                        }
                    }
                }
            }
        }
        12.sy()
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
        25.sy()
    }
}

data class SettledUnsettledMembersBottomSheet(
    val holderTopPadding: Float = 20f,
    val holderBottomPadding: Float = 33f
)