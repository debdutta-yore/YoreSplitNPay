package co.yore.splitnpay.friend_item.models

import androidx.compose.ui.graphics.Color
import co.yore.splitnpay.R

data class ArrowButtonConfiguration(
    val iconBackgroundColor: Color = Color(0xff839BB9),
    val iconPressedColor: Color = Color(0xff1A79E5),
    val iconTint: Color = Color.White,
    val iconButtonRadius: Float = 9f,
    val iconButtonSize: Float = 28f,
    val iconSize: Float = 11f,
    val iconResource: Int = R.drawable.ic_nextarrow,
)