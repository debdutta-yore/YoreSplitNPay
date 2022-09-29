package co.yore.splitnpay.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import co.yore.splitnpay.components.components.coloredShadow
import co.yore.splitnpay.libs.dep
import co.yore.splitnpay.libs.droot2
import co.yore.splitnpay.ui.theme.DarkBlueShadow
import co.yore.splitnpay.ui.theme.White
import coil.compose.AsyncImage

data class GroupImagePickerConfiguration(
    val imageSize: Float = 68f,
    val borderStroke: Float = 3f,
    val borderColor: Color = White
)

@Composable
fun GroupImagePicker_ncbmdg(
    config: GroupImagePickerConfiguration = GroupImagePickerConfiguration(),
    contentDescription: String,
    onClick: () -> Unit,
    groupImage: Any
) {
    Box(
        modifier = Modifier
            .size(config.imageSize.dep())
            .semantics { this.contentDescription = contentDescription }
    ) {
        Box(
            modifier = Modifier
                .size(config.imageSize.dep())
                .coloredShadow(
                    color = DarkBlueShadow,
                    borderRadius = 50.dep(),
                    blurRadius = 6.dep(),
                    spread = 0f,
                    offsetX = 0.dep(),
                    offsetY = 3.dep(),
                )
                .clip(CircleShape)
                .background(Color(0xffCFD8E4))
                .border(
                    width = config.borderStroke.dep(),
                    color = config.borderColor,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                model = groupImage,
                contentScale = ContentScale.FillBounds,
                contentDescription = "people icon"
            )
        }

        Box(
            modifier = Modifier
                .padding(
                    start = config.imageSize.droot2.dep(),
                    top = config.imageSize.droot2.dep()
                )
                .size(27.dep())
                .coloredShadow(
                    color = DarkBlueShadow,
                    borderRadius = 50.dep(),
                    blurRadius = 6.dep(),
                    spread = 0f,
                    offsetX = 0.dep(),
                    offsetY = 3.dep(),
                )
                .clip(CircleShape)
                .background(color = White)
                .clickable {
                    onClick()
                }
        ) {
            Icon(
                modifier = Modifier
                    .width(15.dep())
                    .height(13.5.dep())
                    .align(Alignment.Center),
                painter = painterResource(id = co.yore.splitnpay.R.drawable.ic_camera),
                tint = Color.Unspecified,
                contentDescription = "camera icon"
            )
        }
    }
}