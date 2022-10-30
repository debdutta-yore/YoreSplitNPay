package co.yore.splitnpay.components.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import co.yore.splitnpay.libs.sep
import co.yore.splitnpay.locals.RobotoText
import co.yore.splitnpay.ui.theme.WildBlueYonder

@Composable
fun NothingFoundUI() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        RobotoText(
            "Nothing found",
            fontSize = 13.sep(),
            color = WildBlueYonder,
            fontWeight = FontWeight.Bold
        )
    }
}