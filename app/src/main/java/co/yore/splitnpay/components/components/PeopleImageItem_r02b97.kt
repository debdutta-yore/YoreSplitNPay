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
import co.yore.splitnpay.components.components.*
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.locals.RobotoText
import co.yore.splitnpay.models.*
import co.yore.splitnpay.pages.DeleteIcon_iw4d3p
import co.yore.splitnpay.pages.radialBottomLeft
import co.yore.splitnpay.ui.theme.*
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun PeopleImageItem_r02b97(
    config: PeopleRowItemConfiguration = PeopleRowItemConfiguration(),
    onDelete: () -> Unit,
    friend: ContactData,
    contentDescription: String
) {
    Box(
        modifier = Modifier
            .semantics { this.contentDescription = contentDescription }
    ) {
        AsyncImage(
            modifier = Modifier
                .clip(CircleShape)
                .size(config.imageSize.dep()),
            model = ImageRequest.Builder(LocalContext.current)
                // TODO- update using friend.imageUrl
                .data(friend.image)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.personactionbar),
            contentScale = ContentScale.Crop,
            contentDescription = ""
        )

        if (friend.deletable){
            DeleteIcon_iw4d3p(
                modifier = Modifier
                    .radialBottomLeft(config.imageSize.dep()),
                config = DeleteIconConfiguration(),
                contentDescription = "",
                onClick = {
                    onDelete()
                }
            )
        }
    }
}