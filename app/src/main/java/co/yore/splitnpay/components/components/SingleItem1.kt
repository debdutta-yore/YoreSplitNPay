package co.yore.splitnpay.components.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yore.splitnpay.R
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.models.*
import co.yore.splitnpay.ui.theme.*

@Composable
fun SingleItem1(
    modifier: Modifier = Modifier,
    icon: Painter,
    text: String,
    isSelected: Boolean = false
) {
    Row(
        modifier = modifier
            .background(
                if (isSelected) {
                    Zumthor
                }else Color.White
            )
            .padding(start = 31f.dep())
            .fillMaxWidth()
            .height(49f.dep()),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                modifier = Modifier.size(18.dp),
                tint = CuriousBlue,
                painter = icon,
                contentDescription = "selected photo icon"
            )
            Spacer(modifier = Modifier.width(15f.dep()))
            Text(
                text = text,
                color = CloudBurst,
                fontSize = 14.sp,
                fontWeight = FontWeight(700)
            )
        }

        if (isSelected) {
            Icon(
                modifier = Modifier,
                //  .width(18f.dep())
                tint = CuriousBlue,
                painter = painterResource(id = R.drawable.ic_sheet_selector),
                contentDescription = "holder"
            )
        }

    }

}
