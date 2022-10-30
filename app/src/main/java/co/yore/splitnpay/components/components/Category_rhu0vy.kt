package co.yore.splitnpay.components.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import co.yore.splitnpay.libs.dep
import co.yore.splitnpay.libs.sep
import co.yore.splitnpay.models.CategorySelectorCardConfiguration
import coil.compose.AsyncImage

@Composable
fun Category_rhu0vy(
    text: String,
    icon: Any?,
    config: CategorySelectorCardConfiguration = CategorySelectorCardConfiguration(),
    contentDescription: String
) {
    Row(
        modifier = Modifier
            .semantics { this.contentDescription = contentDescription }
            .height(config.height.dep()),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = icon,
            contentDescription = "",
            modifier = Modifier
                .padding(
                    top = config.iconTopPadding.dep(),
                    bottom = config.iconBottomPadding.dep(),
                    start = config.iconStartPadding.dep()
                )
                .size(16.67.dep())
        )
        // ////////////
        Spacer(modifier = Modifier.width(config.iconTextSpacer.dep()))

        FontFamilyText(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = text,
            fontSize = config.fontSize.sep(),
            color = config.textColor,
            fontWeight = config.fontWeight
        )

        Spacer(modifier = Modifier.width(config.iconEndSpacer.dep()))
    }
}
