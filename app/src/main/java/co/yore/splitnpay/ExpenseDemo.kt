package co.yore.splitnpay

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight

@Composable
fun ExpenseDemo() {
    Box(
        modifier = Modifier
            .padding(
                top = 19.dep(),
                start = 21.dep(),
                end = 21.dep()
            )
            .fillMaxSize()
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
        ){
            Row(
                modifier = Modifier
                    .padding(8.dep())
                    .fillMaxWidth()
            ){
                Row(
                    modifier = Modifier
                        .height(29.dep())
                        .clip(RoundedCornerShape(14.dep()))
                        .clickable {

                        }
                        .background(Color(0xffEDF5FF))
                        .padding(horizontal = 14.dep()),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    RobotoText(
                        "Content",
                        fontSize = 14.sep(),
                        fontWeight = FontWeight.Bold,
                        color = Color(0xff1A79E5)
                    )
                    Spacer(modifier = Modifier.width(7.dep()))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_down_chevron),
                        contentDescription = "",
                        modifier = Modifier
                            .size(6.86.dep()),
                        tint = Color(0xff1B79E6)
                    )
                }
            }
        }
    }
}