package co.yore.splitnpay.pages

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.TextFieldDefaults.indicatorLine
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rudra.yoresplitbill.R
import com.rudra.yoresplitbill.common.dep
import com.rudra.yoresplitbill.common.ep
import com.rudra.yoresplitbill.common.sep
import com.rudra.yoresplitbill.data.config.TextFieldConfiguration
import com.rudra.yoresplitbill.ui.theme.Black
import com.rudra.yoresplitbill.ui.theme.LightGrey
import com.rudra.yoresplitbill.ui.theme.LightGrey4
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun GroupNameTextField(
    config: TextFieldConfiguration = TextFieldConfiguration()
) {

    var groupName by remember { mutableStateOf("") }
    val interactionSource = remember { MutableInteractionSource() }

    val colors = TextFieldDefaults.textFieldColors(
        disabledTextColor = Color.Transparent,
        backgroundColor = config.textFieldBackgroundColor,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        cursorColor = config.cursorColor,
        textColor = config.textColor
    )

    BasicTextField(
        value = groupName,
        onValueChange = { groupName = it },
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(8.dep()))
            .background(
                color = colors.backgroundColor(true).value,
                shape = TextFieldDefaults.TextFieldShape
            )
            .indicatorLine(
                enabled = true,
                isError = false,
                interactionSource = interactionSource,
                colors = colors
            ),
        textStyle = LocalTextStyle.current.copy(
            color = config.textColor,
            fontSize = config.textSize.sep(),
            fontWeight = FontWeight(400)
        ),
        interactionSource = interactionSource,
    ) { innerTextField ->
        TextFieldDefaults.TextFieldDecorationBox(
            value = groupName,
            innerTextField = innerTextField,
            interactionSource = interactionSource,
            contentPadding = PaddingValues(
                start = 15.dep(),
                top = 10f.dep(),
                bottom = 10f.dep()
            ),
            singleLine = true,
            visualTransformation = VisualTransformation.None,
            enabled = true,
            colors = colors,
            placeholder = {
                Text(
                    text = "Enter group name",
                    color = config.hintTextColor,
                    fontSize = config.hintTextSize.sep(),
                    fontWeight = FontWeight(400),
                    textAlign = TextAlign.Center,
                )
            },
            leadingIcon = {
                Row {
                    Icon(
                        modifier = Modifier
                            .width(22f.dep())
                            .height(14f.dep()),
                        painter = painterResource(id = R.drawable.ic_group_icon),
                        tint = LightGrey,
                        contentDescription = "people icon"
                    )

                    Spacer(modifier = Modifier.width(9f.dep()))

                    Divider(
                        color = LightGrey4,
                        modifier = Modifier
                            .width(1f.dep())
                            .height(13f.dep())
                    )
                }
            },
        )
    }
}