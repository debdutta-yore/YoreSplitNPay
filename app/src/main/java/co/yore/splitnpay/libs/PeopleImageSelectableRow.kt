package co.yore.splitnpay.libs

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.yore.splitnpay.R
import co.yore.splitnpay.addmembers.FontFamilyText
import co.yore.splitnpay.components.components.coloredShadow
import co.yore.splitnpay.demos.sx
import co.yore.splitnpay.models.DataIds
import co.yore.splitnpay.pages.Transaction
import co.yore.splitnpay.ui.theme.DarkBlue
import co.yore.splitnpay.ui.theme.Greyish2
import co.yore.splitnpay.ui.theme.Pink
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun SummarySelectableRow(list: List<Transaction>) {
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

@Composable
fun SummarySinglePeople_q6c90m(
    transaction: Transaction,
    config: SummarySinglePeopleConfig = SummarySinglePeopleConfig(),
    notifier: NotificationService = notifier(),
    contentDescription: String
) {
    val (selected, onSelected) = remember { mutableStateOf(false) }

    val borderColor = animateColorAsState(
        targetValue = if (transaction.isSelected) config.selectedBorderColor
        else config.unselectedBorderColor
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
                        .data(transaction.imageUrl)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(config.profileImagePlaceHolder),
                    contentScale = ContentScale.Crop,
                    contentDescription = contentDescription
                )
            }

            Crossfade(targetState = transaction.isSelected) { isChecked ->
                if (isChecked) {
                    Box(
                        Modifier
                            .padding(bottom = 42.droot2.dep(), start = 42.droot2.dep())
                            .size(18.dep())
                    ) {
                        SelectedIcon_f9tfi61(contentDescription = "SelectedIcon")
                    }
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
            color = DarkBlue
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

data class SelectedIconConfiguration(
    val selectedIconSize: Float = 18f,
    val borderWidth: Float = 1f,
    val borderColor: Color = Color.White,
    val shape: Shape = CircleShape,
    val backGroundColor: Color = Pink,
    val icon: Int = R.drawable.ic_checked_right,
    val iconSize: Float = 7f
)

data class SummarySinglePeopleConfig(
    val profileImageSize: Float = 42f,
    val shadowColor: Color = Greyish2,
    val shadowBorderRadius: Float = 50f,
    val shadowBlurRadius: Float = 5.63f,
    val shadowOffsetX: Float = 5.63f,
    val shadowOffsetY: Float = 0f,
    val shadowSpread: Float = 0f,
    val profileImageShape: Shape = CircleShape,
    val selectedBorderColor: Color = Pink,
    val unselectedBorderColor: Color = Color.White,
    val profileImagePlaceHolder: Int = R.drawable.ic_people,
    val gapBetweenProfileImageAndName: Float = 17f
)