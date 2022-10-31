package co.yore.splitnpay.components.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import co.yore.splitnpay.libs.jerokit.sep
import co.yore.splitnpay.libs.sx
import co.yore.splitnpay.models.*

@Composable
fun CategoryRow_a96p42(
    config: CategoryRowConfiguration = CategoryRowConfiguration(),
    category: Category
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        CircleIconWithBackground_xyo2d4(icon = category.icon as Int)
        config.iconTextSpacing.sx()
        FontFamilyText(
            text = category.name,
            color = config.textColor,
            fontSize = config.fontSize.sep(),
            lineHeight = config.lineHeight.sep()
        )
    }

}
