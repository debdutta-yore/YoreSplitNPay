package co.yore.splitnpay.components.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.yore.splitnpay.R
import co.yore.splitnpay.libs.dep
import co.yore.splitnpay.libs.sep
import co.yore.splitnpay.ui.theme.robotoFonts

@Composable
fun BackArrowText(text: String, onBack: () -> Unit) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current.density
    val width = configuration.screenWidthDp.dp.value

    Row(
        modifier = Modifier.padding(
            top = 19f.dep(width),
            start = 18f.dep(width)
        ),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = CenterVertically
    ) {

        IconButton(modifier = Modifier
            .align(CenterVertically)
            .then(Modifier.size(24.dp)),
            onClick = { onBack() }) {
            Icon(
                painter =
                painterResource(id = R.drawable.ic_left_chevron),
                contentDescription = "backArrow",
                tint = Color.White
            )
        }

        Text(
            text = text,
            fontSize = 16f.sep(width),
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontFamily = robotoFonts
        )
    }
}


@Preview
@Composable
fun BackArrowTextPreview() {
    BackArrowText("split") {

    }
}