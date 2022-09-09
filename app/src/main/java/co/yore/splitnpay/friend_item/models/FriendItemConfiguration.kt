package co.yore.splitnpay.friend_item.models

import androidx.compose.ui.graphics.Color

data class FriendItemConfiguration(
    val shadowColor: Color = Color(0x80c6cfd8),
    val backgroundPressedColor: Color = Color(0xffEDF5FF),
    val backgroundColor: Color = Color.White,
    val strokeColor: Color = Color(0xff1A79E5),
    val noStrokeColor: Color = Color(0x00000000),
    val borderRadius: Float = 22f,
    val blurRadius: Float = 33f,
    val shadowOffset: Float = 7f,
    val cardPaddingX : Float= 17f,
    val cardPaddingY : Float= 17f,
    val rowPadding: Float = 23f,
    val imageLeftPadding: Float = 18f,
    val imageRightPadding: Float = 22f,
    val iconButtonRightPadding: Float = 17f,
    val imageBorderStroke: Float = 1f
)