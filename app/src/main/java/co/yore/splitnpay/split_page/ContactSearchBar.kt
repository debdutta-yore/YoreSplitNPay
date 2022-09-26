package co.yore.splitnpay.split_page

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import co.yore.splitnpay.R
import co.yore.splitnpay.components.configuration.ContactSearchBarConfiguration
import co.yore.splitnpay.demos.sx
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.models.DataIds



@Composable
fun ContactSearchBar(
    config: ContactSearchBarConfiguration = ContactSearchBarConfiguration(),
    contentDescription: String,
    text: String = stringState(DataIds.textInput).value,
    notifier: NotificationService = notifier()
) {
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
            value = text,
            onValueChange = {
                notifier.notify(DataIds.textInput,it)
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
            visible = text.isEmpty(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Text(
                text = stringResource(R.string.search_groups_or_contacts),
                fontSize = config.fontSize.sep(),
                color = config.color,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(start = config.startPadding.dep())
            )
        }
    }
}