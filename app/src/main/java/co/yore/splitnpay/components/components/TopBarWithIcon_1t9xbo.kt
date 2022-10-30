package co.yore.splitnpay.components.components

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import co.yore.splitnpay.R
import co.yore.splitnpay.components.components.ContactSearchBar
import co.yore.splitnpay.components.components.GroupCard_0msq1z
import co.yore.splitnpay.components.components.PeopleCard_eq3k8h
import co.yore.splitnpay.components.components.coloredShadow
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.locals.RobotoText
import co.yore.splitnpay.models.*
import co.yore.splitnpay.ui.theme.*
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun TopBarWithIcon_1t9xbo(
    text: String,
    onClick: () -> Unit,
    config: TopbarWithIconConfiguration = TopbarWithIconConfiguration()
) {
    Column(
        modifier = Modifier
            .coloredShadow(
                color = config.shadowColor,
                blurRadius = config.blurRadius.dep(),
                offsetY = config.offsetY.dep()
            )
            .fillMaxWidth()
            .height(config.height.dep())
            .background(config.backgroundColor),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width((config.startPadding - 12).dep()))
            Icon(
                modifier = Modifier
                    .size((config.iconSize + 12).dep())
                    .clip(CircleShape)
                    .clickable {
                        onClick()
                    }
                    .padding(6.dep()),
                painter = painterResource(config.icon),
                contentDescription = "back arrow",
                tint = config.iconTint
            )
            Spacer(modifier = Modifier.width((config.space - 6).dep()))
            Text(
                text = text,
                fontSize = config.textSize.sep(),
                fontWeight = config.fontWeight,
                color = config.fontColor
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}