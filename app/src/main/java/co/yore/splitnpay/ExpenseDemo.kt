package co.yore.splitnpay

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight

@Composable
fun ExpenseDemo() {
    val data = remember {
        mutableStateOf(listOf(
            PieData(
                Color(0xffFF4077),
                0.5f
            ),
            PieData(
                Color(0xff1A79E5),
                0.5f
            )
        ))
    }

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
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
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
                Row {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_calendar_icon),
                        contentDescription = "",
                        modifier = Modifier
                            .size(14.dep())
                    )
                    Spacer(modifier = Modifier.width(4.62.dep()))
                    RobotoText(
                        "Content",
                        fontSize = 12.sep(),
                        color = Color(0xff243257)
                    )
                }
            }
            Spacer(modifier = Modifier.height(42.dep()))
            Box(
                modifier = Modifier
                    .size(227.dep())
                    .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center
            ){
                val stroke = with(LocalDensity.current){
                    50.dep().toPx()
                }

                Canvas(
                    modifier = Modifier
                        .padding(25.dep())
                        .fillMaxSize()
                ){
                    var start = -90f
                    data.value.forEach {
                        val used = 360f*it.portion
                        drawArc(
                            color = it.color,
                            useCenter = false,
                            startAngle = start,
                            sweepAngle = used,
                            style = Stroke(
                                width = stroke
                            ),
                            size = this.size
                        )
                        start += used
                    }

                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    RobotoText(
                        "Total share",
                        color = Color(0xff677C91),
                        fontSize = 13.sep()
                    )
                    Spacer(modifier = Modifier.height(3.dep()))
                    Row(){
                        RobotoText(
                            "₹ ",
                            fontSize = 10.sep(),
                            color = Color(0xff243257),
                            modifier = Modifier
                                .alignByBaseline()
                        )
                        RobotoText(
                            "500.",
                            fontSize = 15.sep(),
                            color = Color(0xff243257),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .alignByBaseline()
                        )
                        RobotoText(
                            "00",
                            fontSize = 15.sep(),
                            color = Color(0xff243257),
                            modifier = Modifier
                                .alignByBaseline()
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(28.dep()))
            Row(
                modifier = Modifier
                    .padding(
                        start = 16.dep(),
                        end = 7.dep()
                    )
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                RobotoText(
                    "Expense Categories",
                    color = Color(0xff243257),
                    fontSize = 18.sep(),
                    fontWeight = FontWeight.Bold
                )
                Row {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = "",
                        modifier = Modifier
                            .size(19.dep()),
                        tint = Color(0xff1B79E6)
                    )
                    Spacer(modifier = Modifier.width(20.dep()))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_filter),
                        contentDescription = "",
                        modifier = Modifier
                            .size(21.dep()),
                        tint = Color(0xff1B79E6)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dep()))
        }
    }
}

data class PieData(
    val color: Color,
    val portion: Float
)

data class CategoryExpense(
    val icon: Any?,
    val category: String,
    val description: String,
    val count: Int,
    val amount: Float,
)