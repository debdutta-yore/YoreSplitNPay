package co.yore.splitnpay.components.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import co.yore.splitnpay.R
import co.yore.splitnpay.libs.colorsFromHumanName
import co.yore.splitnpay.libs.jerokit.dep
import co.yore.splitnpay.libs.jerokit.sep
import co.yore.splitnpay.libs.locals.RobotoText
import co.yore.splitnpay.models.ProfileImageConfiguration
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun ProfileImage_2hf7q0(
    image: Any?,
    config: ProfileImageConfiguration = ProfileImageConfiguration(),
    contentDescription: String
) {
    if(image is String && image.startsWith("name://")){
        val name = image.replace("name://","")
        val colors = colorsFromHumanName(name)
        Box(
            modifier = Modifier
                .size(config.imageSize.dep())
                .clip(CircleShape)
                .background(colors.first)
                .border(
                    width = config.borderStroke.dep(),
                    color = config.borderColor,
                    shape = config.shape
                )
                .padding(config.borderStroke.dep()),
            contentAlignment = Alignment.Center
        ){
            RobotoText(
                text = String(
                name
                    .split("/|\\s+".toRegex())
                    .map { it.firstOrNull() }
                    .filterNotNull()
                    .filter { it.isDigit() || it.isLetter() || it == '+' }
                    .take(2)
                    .toCharArray()
            ).uppercase(),
                color = colors.second,
                fontSize = 20.sep(),
                fontWeight = FontWeight.Bold
            )
        }
    }
    else{
        AsyncImage(
            modifier = Modifier
                .size(config.imageSize.dep())
                .border(
                    width = config.borderStroke.dep(),
                    color = config.borderColor,
                    shape = config.shape
                )
                .padding(config.borderStroke.dep())
                .clip(CircleShape),
            model = ImageRequest.Builder(LocalContext.current)
                .data(image?: R.drawable.user_dummy4)
                .crossfade(true)
                .build(),
            placeholder = painterResource(config.placeholder),
            contentScale = config.contentScale,
            contentDescription = contentDescription
        )
    }
}
