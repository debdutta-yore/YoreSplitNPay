package co.yore.splitnpay

import android.annotation.SuppressLint
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    private fun SplitPageDemo() {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {

                    }, backgroundColor = colorResource(id = R.color.pink),
                    contentColor = Color.White,
                    modifier = Modifier
                        .coloredShadow(
                            color = Color(0x4fff4077),
                            borderRadius = 100.dep(),
                            blurRadius = 12.dep(),
                            spread = 0f,
                            offsetX = 0.dep(),
                            offsetY = 3.dep(),
                        ),
                    shape = CircleShape,
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp,
                        hoveredElevation = 0.dp,
                        focusedElevation = 0.dp,
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_split_white),
                        "", tint = Color.Unspecified
                    )
                }
            }
        ) {
            SplitPage()
        }
    }