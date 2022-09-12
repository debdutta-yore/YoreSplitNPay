package co.yore.splitnpay

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign

@Composable
fun ContactSearchBar() {
    val (value, onValueChange) = remember { mutableStateOf("") }

    Box(contentAlignment = Alignment.CenterStart){
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(44f.dep())
                .background(Color(0xFFf9f9f9), RoundedCornerShape(8.dep())),
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(fontSize = 11f.sep()),
            shape = RoundedCornerShape(8f.dep()),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                backgroundColor = Color.Transparent,
                cursorColor = Color.DarkGray
            ),
            leadingIcon = {
                Row(verticalAlignment = Alignment.CenterVertically,) {
                    Spacer(modifier = Modifier.width(20.5f.dep()))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = "search",
                        tint = Color.Unspecified
                    )
                    Spacer(modifier = Modifier.width(12.5f.dep()))
                    Divider(
                        color = colorResource(id = R.color.lightgrey4),
                        modifier = Modifier
                            .height(13.dep())
                            .width(1.dep())
                    )
                    Spacer(modifier = Modifier.width(15f.dep()))
                }
            },
        )

        AnimatedVisibility(visible = value.isEmpty()) {
            Text(
                text = "Search groups or contacts",
                fontSize = 11f .sep(),
                color = colorResource(id = R.color.lightgrey4),
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(start = 72.dep() )
            )
        }
    }
}