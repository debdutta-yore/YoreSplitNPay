package co.yore.splitnpay.friend_item

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import co.yore.splitnpay.R
import co.yore.splitnpay.dep
import co.yore.splitnpay.friend_item.configs.ProfileImageConfiguration
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun ProfileImage(
    contentDescription: String,
    config: ProfileImageConfiguration = ProfileImageConfiguration()
){

    Box(
        Modifier
            .semantics {
                this.contentDescription = contentDescription
            }
            .size(config.imageContainerSize.dep())
            .clip(
                CircleShape
            )
            .border(
                BorderStroke(config.strokeWidth.dep(), config.imageBorderColor),
                CircleShape
            ),
        contentAlignment = Alignment.Center
    ){
        AsyncImage(
            placeholder = painterResource(id = R.drawable.person),
            model = ImageRequest.Builder(LocalContext.current)
                .data(config.imageUrl)
                .placeholder(R.drawable.person)
                .crossfade(durationMillis = 300)
                .build(),
            contentDescription = null,
            modifier = Modifier
                .size(config.imageSize.dep())
                .clip(
                    CircleShape
                )
        )
    }
}