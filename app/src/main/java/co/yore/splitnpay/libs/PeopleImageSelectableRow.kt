package co.yore.splitnpay.libs

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.yore.splitnpay.components.components.FontFamilyText
import co.yore.splitnpay.components.components.coloredShadow
import co.yore.splitnpay.models.DataIds
import co.yore.splitnpay.models.SelectedIconConfiguration
import co.yore.splitnpay.models.SplitSelectableMember
import co.yore.splitnpay.models.SummarySinglePeopleConfig
import co.yore.splitnpay.ui.theme.CloudBurst
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun SummarySelectableRow(list: List<SplitSelectableMember>) {
    LazyRow(
        modifier = Modifier.padding(
            start = 24.dep(),
            end = 24.dep()
        )
    ) {
        itemsIndexed(list) { index, it ->
            SummarySinglePeople_q6c90m(
                transaction = it,
                contentDescription = "SummaryEachUser"
            )
            if (index != list.lastIndex)
                37.sx()
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SummarySinglePeople_q6c90m(
    transaction: SplitSelectableMember,
    config: SummarySinglePeopleConfig = SummarySinglePeopleConfig(),
    notifier: NotificationService = notifier(),
    contentDescription: String
) {
    val (selected, onSelected) = remember { mutableStateOf(false) }

    val borderColor = animateColorAsState(
        targetValue = if (transaction.isSelected) config.selectedBorderColor
        else config.unselectedBorderColor,
        tween(500)
    )

    Column(
        modifier = Modifier
            .semantics { this.contentDescription = contentDescription },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(config.profileImageSize.dep())
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() } // This is mandatory
                ) {
//                    onSelected(selected.not())
                    notifier.notify(DataIds.selectBalanceMember, transaction)
                },
            contentAlignment = Alignment.TopEnd
        ) {
            Box(
                modifier = Modifier
                    .coloredShadow(
                        color = config.shadowColor,
                        borderRadius = config.shadowBorderRadius.dp,
                        blurRadius = config.shadowBlurRadius.dp,
                        offsetY = config.shadowOffsetY.dp,
                        offsetX = config.shadowOffsetX.dp,
                        spread = config.shadowSpread
                    )
                    .clip(config.profileImageShape)
            ) {
                AsyncImage(
                    modifier = Modifier
                        .clip(config.profileImageShape)
                        .fillMaxSize()
                        .border(
                            width = if (transaction.isSelected) 3.dep() else (2.57).dep(),
                            color = borderColor.value,
                            shape = config.profileImageShape
                        ),
                    model = ImageRequest.Builder(LocalContext.current)
                        //TODO - update using friend.imageUrl
                        .data(transaction.image)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(config.profileImagePlaceHolder),
                    contentScale = ContentScale.Crop,
                    contentDescription = contentDescription
                )
            }

            androidx.compose.animation.AnimatedVisibility(
                transaction.isSelected,
                enter = fadeIn(tween(500)) + scaleIn(tween(500)),
                exit = fadeOut(tween(500)) + scaleOut(tween(500))
            ) {
                Box(
                    Modifier
                        .padding(bottom = 42.droot2.dep(), start = 42.droot2.dep())
                        .size(18.dep())
                ) {
                    SelectedIcon_f9tfi61(contentDescription = "SelectedIcon")
                }
            }
        }

        config.gapBetweenProfileImageAndName.sx()

        FontFamilyText(
            text = transaction.name,
            fontWeight =
            if (transaction.isSelected)
                FontWeight.Bold
            else
                FontWeight.Normal,
            fontSize = 12.sep(),
            color = CloudBurst
        )
    }
}

@Composable
fun SelectedIcon_f9tfi61(
    config: SelectedIconConfiguration = SelectedIconConfiguration(),
    contentDescription: String
) {
    Box(
        modifier = Modifier
            .semantics { this.contentDescription = contentDescription }
            .size(config.selectedIconSize.dep())
            .aspectRatio(1f, matchHeightConstraintsFirst = true)
            .border(width = config.borderWidth.dp, color = config.borderColor, shape = config.shape)
            .clip(config.shape)
            .background(color = config.backGroundColor)
    ) {
        Icon(
            modifier = Modifier
                .align(Alignment.Center)
                .size(config.iconSize.dep()),
            tint = Color.Unspecified,
            painter = painterResource(id = config.icon),
            contentDescription = contentDescription
        )
    }
}

