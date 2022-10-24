package co.yore.splitnpay.components.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import co.yore.splitnpay.libs.dep

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
        BottomCut(color = cardColor)
/*
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = cardColor
                )
        )
        {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(
                        colorResource(id = R.color.white),
                        shape = RoundedCornerShape(
                            0.dp,
                            48f.dep(),
                            0.dp,
                            0.dp
                        )
                    )
            ) {

            }
        }
*/
    }
}

@Composable
fun UpperCut(
    modifier: Modifier = Modifier,
    color: Color
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = color,
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
}

@Composable
fun BottomCut(
    modifier: Modifier = Modifier,
    color: Color
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(42.dep())
            .background(color)
            .background(
                Color.White,
                RoundedCornerShape(
                    topEnd = 48.dep()

                )
            )
    ){

    }
}
