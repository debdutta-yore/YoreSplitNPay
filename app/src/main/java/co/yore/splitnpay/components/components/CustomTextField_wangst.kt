package co.yore.splitnpay.components.components

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.TextFieldDefaults.indicatorLine
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.libs.jerokit.dep
import co.yore.splitnpay.libs.jerokit.sep
import co.yore.splitnpay.models.*
import co.yore.splitnpay.ui.theme.*
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CustomTextField_wangst(
    text: String,
    change: (String) -> Unit,
    leadingIcon: Any?,
    placeHolderText: String,
    contentDescription: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    maxLines: Int = Int.MAX_VALUE,
    singleLine: Boolean = false,
    textStyle: TextStyle = TextStyle.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interceptor: ((String) -> String)? = null,
    config: CustomTextFieldConfiguration = CustomTextFieldConfiguration(),
    iconTint: Color = Color.Unspecified
) {
    val interactionSource = remember { MutableInteractionSource() }
    val colors = TextFieldDefaults.textFieldColors(
        disabledTextColor = config.disabledTextColor,
        backgroundColor = config.backgroundColor,
        focusedIndicatorColor = config.focusedIndicatorColor,
        unfocusedIndicatorColor = config.unfocusedIndicatorColor,
        disabledIndicatorColor = config.disabledIndicatorColor,
        cursorColor = config.cursorColor
    )

    BasicTextField(
        value = text,
        onValueChange = {
            if (interceptor == null){
                change(it)
            } else {
                change(interceptor(it))
            }
        },
        textStyle = textStyle,
        modifier = Modifier
            .semantics { this.contentDescription = contentDescription }
            .fillMaxSize()
            .indicatorLine(
                enabled = true,
                isError = false,
                interactionSource = interactionSource,
                colors = colors
            ),
        interactionSource = interactionSource,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        maxLines = maxLines,
        singleLine = singleLine,
        visualTransformation = visualTransformation
    ) { innerTextField ->
        TextFieldDefaults.TextFieldDecorationBox(
            value = text,
            innerTextField = innerTextField,
            interactionSource = interactionSource,
            contentPadding = PaddingValues(
                // start = config.contentStartPadding.dep(),
                // top = config.contentTopPadding.dep(),
                bottom = config.contentBottomPadding.dep()
            ),
            singleLine = true,
            visualTransformation = VisualTransformation.None,
            enabled = true,
            colors = colors,
            placeholder = {
                FontFamilyText(
                    text = placeHolderText,
                    color = config.textColor,
                    fontSize = config.textFontSize.sep(),
                    fontWeight = config.textFontWeight,
                    textAlign = TextAlign.Center
                )
            },
            leadingIcon = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = leadingIcon,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(start = 18.dep())
                            .size(18.33.dep()),
                        contentScale = ContentScale.Fit,
                        colorFilter = ColorFilter.tint(iconTint)
                    )
                    config.dividerStartPadding.sx()
                    Divider(
                        color = config.dividerColor,
                        modifier = Modifier
                            .width(config.dividerWidth.dep())
                            .height(config.dividerHeight.dep())
                    )
                    25.sx()
                }
            }
        )
    }
}
