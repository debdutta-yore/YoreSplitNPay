package co.yore.splitnpay.components.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import co.yore.splitnpay.libs.coloredShadow
import co.yore.splitnpay.libs.jerokit.dep
import co.yore.splitnpay.libs.jerokit.sep
import co.yore.splitnpay.models.*

@Composable
fun TopBarWithIcon_1t9xbo1(
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
