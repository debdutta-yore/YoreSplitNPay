package co.yore.splitnpay.pages

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.TextFieldDefaults.indicatorLine
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import co.yore.splitnpay.R
import co.yore.splitnpay.libs.dep
import co.yore.splitnpay.libs.sep
import co.yore.splitnpay.models.TextFieldConfiguration


@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun GroupNameTextField(
    value: String,
    config: TextFieldConfiguration = TextFieldConfiguration(),
    onValueChange: (String)->Unit,
) {
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
        value = value,
        onValueChange = onValueChange,
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
            value = value,
            innerTextField = innerTextField,
            interactionSource = interactionSource,
            contentPadding = PaddingValues(
                start = 22.dep(),
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
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier
                            .padding(start = 17.dep())
                            .width(22f.dep())
                            .height(14f.dep()),
                        painter = painterResource(id = R.drawable.ic_group_icon),
                        tint = Color(0xff656565),
                        contentDescription = "people icon"
                    )

                    Spacer(modifier = Modifier.width(9f.dep()))

                    Divider(
                        color = Color(0xffBCBCBC),
                        modifier = Modifier
                            .width(1f.dep())
                            .height(13f.dep())
                    )
                }
            },
        )
    }
}