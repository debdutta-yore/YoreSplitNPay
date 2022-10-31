package co.yore.splitnpay.components.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import co.yore.splitnpay.R
import co.yore.splitnpay.libs.jerokit.dep
import co.yore.splitnpay.libs.radialBottomLeft
import co.yore.splitnpay.models.ContactData
import co.yore.splitnpay.models.DeleteIconConfiguration
import co.yore.splitnpay.models.PeopleRowItemConfiguration
import co.yore.splitnpay.pages.DeleteIcon_iw4d3p
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
                .data(friend.image?: R.drawable.user_dummy4)
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
