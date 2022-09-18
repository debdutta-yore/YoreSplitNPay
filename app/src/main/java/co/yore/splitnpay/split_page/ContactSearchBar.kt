package co.yore.splitnpay.split_page

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import co.yore.splitnpay.R
import co.yore.splitnpay.dep
import co.yore.splitnpay.sep
import co.yore.splitnpay.sx

data class ContactSearchBarConfiguration(
    val height: Float = 44f,
    val backgroundColor: Color = Color(0xFFf9f9f9),
    val borderRadius: Float = 8f,
    val fontSize: Float = 11f,
    val cursorColor: Color = Color.DarkGray,
    val iconLeftSpace: Float = 20.5f,
    val searchIconId: Int = R.drawable.ic_search,
    val searchIconTint: Color = Color(0xff989898),
    val iconRightSpace: Float = 12.5f,
    val dividerColor: Color = Color(0xffBCBCBC),
    val dividerHeight: Float = 13f,
    val dividerWidth: Float = 1f,
    val dividerRightSpace: Float = 15f,
    val color: Color = Color(0xffBCBCBC),
    val startPadding: Float = 72f
)

@Composable
fun ContactSearchBar(
    config: ContactSearchBarConfiguration = ContactSearchBarConfiguration(),
    contentDescription: String,
    onChange: (String) -> Unit
) {
    val (value, onValueChange) = remember { mutableStateOf("") }
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .semantics {
                this.contentDescription = contentDescription
            }
    ) {
        TextField(
            modifier = Modifier
                .semantics {
                    this.contentDescription = "${contentDescription}_text_field"
                }
                .fillMaxWidth()
                .height(config.height.dep())
                .background(
                    config.backgroundColor,
                    RoundedCornerShape(config.borderRadius.dep())
                ),
            value = value,
            onValueChange = {
                onValueChange(it)
                onChange(it)
            },
            textStyle = TextStyle(fontSize = config.fontSize.sep()),
            shape = RoundedCornerShape(config.borderRadius.dep()),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                backgroundColor = Color.Transparent,
                cursorColor = config.cursorColor
            ),
            leadingIcon = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    config.iconLeftSpace.sx()
                    Icon(
                        painter = painterResource(id = config.searchIconId),
                        contentDescription = "search",
                        tint = config.searchIconTint
                    )
                    config.iconRightSpace.sx()
                    Divider(
                        color = config.dividerColor,
                        modifier = Modifier
                            .height(config.dividerHeight.dep())
                            .width(config.dividerWidth.dep())
                    )
                    config.dividerRightSpace.sx()
                }
            },
        )

        AnimatedVisibility(
            visible = value.isEmpty(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Text(
                text = "Search groups or contacts",
                fontSize = config.fontSize.sep(),
                color = config.color,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(start = config.startPadding.dep())
            )
        }
    }
}