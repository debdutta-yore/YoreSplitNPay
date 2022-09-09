package co.yore.splitnpay.friend_item.configs

import androidx.compose.ui.graphics.Color

data class ProfileImageConfiguration(
    val imageBorderColor: Color = Color(0xffEDF5FF),
    val imageUrl: String = "https://xsgames.co/randomusers/avatar.php?g=male",
    val imageContainerSize: Float = 49f,
    val imageSize: Float = 47f,
    val strokeWidth: Float = 3f
)