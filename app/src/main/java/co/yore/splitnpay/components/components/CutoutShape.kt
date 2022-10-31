package co.yore.splitnpay.components.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import co.yore.splitnpay.libs.jerokit.dep

@Composable
fun CutoutShape(
    modifier: Modifier = Modifier,
    boxHeight: Float,
    cardColor: Color
) {
    Column(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(boxHeight.dp)
                .background(
                    color = cardColor,
                    shape = RoundedCornerShape(
                        0.dp,
                        0.dp,
                        bottomEnd = 0f.dep(),
                        bottomStart = 48f.dep()
                    )
                ),
            contentAlignment = Alignment.BottomCenter
        ) {

        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(42.dep())
                .background(cardColor)
                .background(
                    Color.White,
                    RoundedCornerShape(
                        topEnd = 48.dep()

                    )
                )
        ){

        }
    }
}
