package co.yore.splitnpay.components.configuration

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import co.yore.splitnpay.R
import co.yore.splitnpay.ui.theme.*

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

data class AddGroupButtonConfiguration(
    val backgroundColor: Color = LightRedButton,
    val size: Float = 40f,
    val icon: Int = R.drawable.ic_group
)

data class SplitButtonConfiguration(
    val backgroundColor: Color = Color(0xffFF4077),
    val tint: Color = Color.White,
    val shadowColor: Color = Color(0x4fff4077),
    val shadowBorderRadius: Float = 100f,
    val shadowBlurRadius: Float = 12f,
    val shadowSpread: Float = 0f,
    val offsetX: Float = 0f,
    val offsetY: Float = 3f,
    val iconId: Int = R.drawable.ic_split_white
)

data class ArrowButtonConfiguration(
    val iconBackgroundColor: Color = Color(0xff839BB9),
    val iconPressedColor: Color = Color(0xff1A79E5),
    val iconTint: Color = Color.White,
    val iconButtonRadius: Float = 9f,
    val iconButtonSize: Float = 28f,
    val iconSize: Float = 11f,
    val iconResource: Int = R.drawable.ic_nextarrow,
)

data class ProfileImageConfiguration1(
    val imageBorderColor: Color = Color(0xffEDF5FF),
    val imageUrl: String = "https://xsgames.co/randomusers/avatar.php?g=male",
    val imageContainerSize: Float = 49f,
    val imageSize: Float = 47f,
    val strokeWidth: Float = 3f
)

data class SplitTabItemConfiguration(
    val minWidth: Float = 48f,
    val roundedCorner: Float = 13.5f,
    val selectedBackground: Color = Bluish,
    val unSelectedBackground: Color = Lightblue1,
    val paddingStart: Float = 9f,
    val paddingEnd: Float = 8f,
    val paddingTop: Float = 7f,
    val paddingBottom: Float = 7f,
    val textSize: Float = 11f,
    val selectedTextColor: Color = Color.White,
    val unSelectedTextColor: Color = Bluish,
    val fontWeight: FontWeight = FontWeight(400)
)

data class SplitBackButtonConfiguration(
    val boxSize: Number = 24,
    val iconSize: Number = 12,
    val iconId: Int = R.drawable.ic_left_chevron,
    val tint: Color = Color.White,
)

data class ProfileImageConfiguration2(
    val imageSize: Float = 49f,
    val borderStroke: Float = 3f,
    val borderColor: Color = Color(0xffEDF5FF),
    val shape: Shape = CircleShape,
    val placeholder: Int = R.drawable.user_dummy4,
    val contentScale: ContentScale = ContentScale.Crop
)

data class YouWillGetPayCardConfig(
    val height: Float = 153f,
    val width: Float = 149f,

    val shadowColor: Color = Color(0x80C6CFD8),
    val shadowBlurRadius: Float = 33f,
    val shadowOffset: Float = 7f,
    val borderRadius: Float = 15f,
    val shadowSpread: Float = 0f,

    val headingTopPadding: Float = 22f,
    val youWillGetStringId:  Int = R.string.you_will_get,
    val youWillPayStringId:  Int = R.string.you_will_pay,
    val headingFontSize: Int = 16,
    val decimalTextSize: Float = 12f,
    val amountTextSize: Float = 20f,
    val youWillGetIconLeftPadding: Float = 33f,
    val amountTopPadding: Float = 6f,
    val currencyTextSize: Float = 12f,
    val arrowButtonRightPadding: Float = 10f,
    val arrowButtonBottomPadding: Float = 8f,
    val type: Type,
    val amountColor: Color = Color(0xff839BB9),
    val backgroundColor: Color = Color.White,
    val activeGetColor: Color = Color(0xff37D8CF),
    val activePayColor: Color = Color(0xffFF4077),
){
    enum class Type{
        GET,
        PAY
    }
}

data class CheckboxConfiguration(
    val iconColor: Color = Bluish,
    val iconSize: Float = 28f,
    val icon: Int = R.drawable.ic_check,
    val cornerRadius: Float = 10f
)

data class PeopleCardConfiguration(
    val cardHeight: Float = 95f,
    val paddingStart: Float = 18f,
    val paddingEnd: Float = 18f,
    val borderStroke: Float = 1f,
    val borderColor: Color = Bluish,
    val cardBackgroundColor: Color = Color(0xffEDF5FF),
    val cardUnselectedColor: Color = Color.White,
    val cornerRadius: Float = 22f,
    val blurRadius: Float = 33f,
    val blurOffsetX: Float = 7f,
    val blurOffsetY: Float = 7f,
    val blurSpread: Float = 0f,
    val checkable: Boolean = true
){
    companion object{
        val checkable = PeopleCardConfiguration(checkable = true)
        val unCheckable = PeopleCardConfiguration(checkable = false)
    }
}

data class MonthUIConfiguration(
    val width: Float = 74f,
    val height: Float = 42f,
    val borderRadius: Float = 21f,
    val selectedBackgroundColor: Color = Color(0xffEDF5FF),
    val fontSize: Float = 14f,
    val disabledColor: Color = Color.Gray,
    val activeColor: Color = Color(0xff1B79E6),
    val color: Color = Color(0xff243257)
)

data class YearSwitcherConfiguration(
    val navYearSpace: Float = 40f,
    val fontSize: Float = 16f,
    val color: Color = Color(0xff243257),
    val horizontalPadding: Float = 4f
)

data class YearsListConfiguration(
    val columnCount: Int = 3,
    val horizontalPadding: Float = 58f,
    val height: Float = 251f,
    val fadeColor: Color = Color.White,
    val fadeY: Float = 60f,
    val verticalContentPadding: Float = 20f,
    val verticalItemSpacing: Float = 21f
)

data class YearItemConfiguration(
    val width: Float = 84f,
    val height: Float = 42f,
    val borderRadius: Float = 21f,
    val selectedBackgroundColor: Color = Color(0xffEDF5FF),
    val unselectedBackgroundColor: Color = Color.Transparent,
    val fontSize: Float = 14f,
    val selectedColor: Color = Color(0xff1B79E6),
    val unselectedColor: Color = Color(0xff243257)
)

data class NavIconConfiguration(
    val size: Float = 18f,
    val disabledBackgroundColor: Color = Color.Gray,
    val enabledBackgroundColor: Color = Color(0xff1A79E5),
    val drawableId: Int,
    val padding: Float = 4f,
    val iconTint: Color = Color.White
){
    companion object{
        val Prev = NavIconConfiguration(
            drawableId = R.drawable.ic_left_chevron
        )
        val Next = NavIconConfiguration(
            drawableId = R.drawable.ic_right_chevron
        )
    }
}

data class MonthDayPickerConfiguration(
    val verticalPadding: Float = 9f,
    val height: Float = 89f,
    val backgroundColor: Color = Color(0xffEDF5FF),
    val boxSize: Float = 54f,
    val highlightWidth: Float = 64f,
    val highlightBorderRadius: Float = 13f,
    val highlightBackgroundColor: Color = Color(0xff1A79E5),
    val apparentCurrentThreshold: Float = 0.1f
)

data class WeekDayUIConfiguration(
    val weekDayFontSize: Float = 11f,
    val activeThreshold: Float = 0.1f,
    val weekDayDisabledColor: Color = Color.LightGray,
    val weekDayActiveColor: Color = Color.White,
    val weekDayEnabledColor: Color = Color(0xff243257),
    val weekDayDateSpace: Float = 12f,
    val dateFontSize: Float = 11f,
    val dateDisabledColor: Color = Color.LightGray,
    val dateActiveColor: Color = Color.White,
    val dateEnabledColor: Color = Color(0xff1A79E5),
)

data class GroupMemberProfilePicsConfiguration(
    val startPadding: Float = 14.5f,
    val smaller: Boolean = false
){
    companion object{
        val smaller = GroupMemberProfilePicsConfiguration(
            smaller = true,
            startPadding = 8.5f
        )
    }
}

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
){
    companion object{
        val upi = ContactSearchBarConfiguration(
            height = 52f,
            searchIconId = R.drawable.ic_upi
        )
    }
}

data class GroupProfileImageConfiguration(
    val imageSize: Float = 45f,
    val borderStroke: Float = 2.25f,
    val borderColor: Color = Color.White,
    val placeholder: Int = R.drawable.user_dummy4,
    val contentScale: ContentScale = ContentScale.Crop,
    val shadowColor: Color = DarkBlueShadow,
    val shadowBorderRadius: Float = 50f,
    val shadowBlurRadius: Float = 4.5f,
    val shadowOffsetX: Float = 0f,
    val shadowOffsetY: Float = 2.25f,
    val shadowSpread: Float = 0f
)


data class SingleGroupMemberProfilePicConfiguration(
    val imageSize: Float = 29f,
    val borderWidth: Float = 1f,
    val borderColor: Color = Color.White,
    val placeholder: Int = R.drawable.user_dummy4,
    val contentScale: ContentScale = ContentScale.Crop
){
    companion object{
        val smaller = SingleGroupMemberProfilePicConfiguration(
            imageSize = 17f
        )
    }
}
data class PeopleRowItemConfiguration(
    val imageSize: Float = 45f,
)
data class SplitWithPageContentConfiguration(
    val splitTextId: Int = R.string.split,
    val splitWithTextId: Int = R.string.split_with,
)

data class TopbarWithIconConfiguration(
    val iconSize : Float = 12f,
    val iconTint : Color = DarkBlue,
    val icon: Int = R.drawable.ic_left_arrow,
    val textSize : Float = 14f,
    val fontWeight: FontWeight = FontWeight(700),
    val fontColor: Color = DarkBlue,
    val shadowColor: Color = Color(0xff075692).copy(alpha = 0.11f),
    val blurRadius: Float = 6f,
    val offsetY: Float = 3f,
    val height: Float = 54f,
    val backgroundColor: Color = Color.White,
    val startPadding: Float = 18f,
    val space: Float = 13f
)

data class CollapsedCardsSectionConfiguration(
    val space: Float = 8f,
    val endPadding: Float = 16f,
)

data class ExpandedCardsConfiguration(
    val bottomPadding: Float = 78f,
    val space: Float = 22f,
)

data class GroupCreationButtonConfiguration(
    val width: Float = 90f,
    val height: Float = 35f,
    val backgroundColor: Color = Color(0xff839BB9),
    val iconId: Int = R.drawable.ic_add_group,
    val size: Float = 18f
)

data class HeaderBackAndSplitConfiguration(
    val splitTextId: Int = R.string.split,
    val splitFontSize: Number = 14,
    val tint: Color = Color.White,

    val space: Number = 4,
)

data class HeaderContentConfiguration(
    val topPadding: Number = 4,
    val splitBalanceTextId: Int = R.string.split_balance,
    val splitBalanceFontSize: Float = 12f,
    val splitBalanceColor: Color = Color.White
)


data class TabsConfiguration(
    val backgroundColor: Color = Color.White,
    val startPadding: Float = 15f,
    val fontSize: Float = 21f,
    val color: Color = Color(0xffCFD8E4),
    val selectedColor: Color = Color(0xff243257),
    val height: Float = 73f,
    val dividerThickness: Float = 1f,
    val dividerColor: Color = Color(0xfffafcff),
)

data class YouWillPayGetIconConfig(
    val youWillPayIconWidth: Float = 46f,
    val youWillPayIconHeight: Float = 62f,
)

data class YouWillGetPayArrowButtonConfiguration(
    val arrowButtonSize: Float = 26f,
    val icon: Int
){
    companion object{
        val Get: YouWillGetPayArrowButtonConfiguration
            get() = YouWillGetPayArrowButtonConfiguration(icon = R.drawable.ic_you_will_get_arrow)
        val Pay: YouWillGetPayArrowButtonConfiguration
            get() = YouWillGetPayArrowButtonConfiguration(icon = R.drawable.ic_you_will_pay_arrow)
    }
}

data class DeleteIconConfiguration(
    val selectorSize: Float = 20f,
    val backgroundColor: Color = Pink,
    val selectorBorderStroke: Float = 1f,
    val selectorBorderColor: Color = Color.White,
    val selectorIconSize: Float = 12f,
    val selectorIcon: Int = R.drawable.ic_cross,
    val selectorIconTint: Color = Color.Unspecified
)

data class HeaderUpperCutoutConfiguration(
    val curveHeightConstant: Float = 159f,
    val curveHeightVariable: Float = 60f,
    val curveRadius: Number = 47,
)

data class HeaderContentAndCardsConfiguration(
    val headerTopPadding: Float = 83f
)

data class TransparentProfilePicConfiguration(
    val imageSize: Float = 29f,
    val backGroundColor: Color = DarkBlue.copy(alpha = 0.65f),
    val borderWidth: Float = 1f,
    val borderColor: Color = Color.White.copy(alpha = 0.5f),
    val fontSize: Float = 12f,
    val fontColor: Color = Color.White,
    val fontWeight: FontWeight = FontWeight.Bold
){
    companion object{
        val smaller = TransparentProfilePicConfiguration(
            imageSize = 17f,
            fontSize = 8f
        )
    }
}

data class GroupCardConfiguration(
    val shadowColor: Color = GreyShadow,
    val shadowBorderRadius: Float = 22f,
    val shadowBlurRadius: Float = 33f,
    val shadowOffsetX: Float = 7f,
    val shadowOffsetY: Float = 7f,
    val shadowSpread: Float = 0f,
    val startPadding: Float = 18f,
    val endPadding: Float = 17f,
    val height: Float = 95f,
    val cornerRadius: Float = 22f,
    val backGroundColor: Color = Color.White,
    val horizontalPadding: Float = 18f,
    val profileImageRightPadding: Float = 24f,
    val groupNameProfileImagesGap: Float = 6f,
    val arrowButtonLeftPadding: Float = 25f,
    val groupNameFontSize: Float = 12f,
    val groupBalanceTopPadding: Float = 21f,
    val groupBalanceFontSize: Float = 10f,
    val cardPaddingTop: Float = 17f,
    val cardBackgroundColor: Color = Color(0xffEDF5FF),
    val cardUnselectedColor: Color = Color.White,
    val borderStroke: Float = 1f,
    val borderColor: Color = Bluish,
    val checkable: Boolean = false,
){
    companion object{
        val checked = GroupCardConfiguration(checkable = true)
        val unchecked = GroupCardConfiguration(checkable = false)
    }
}

data class NoGroupHasBeenCreatedYetConfiguration(
    val stringId: Int = R.string.no_group_has_been_created_yet,
    val color: Color = Color(0xff839BB9),
    val fontSize: Float = 13f
)

data class HeaderBottomCutoutConfiguration(
    val noneColor: Color = Color(0xff839BB9),
    val getColor: Color = Color(0xff37D8CF),
    val payColor: Color = Color(0xffFF4077)
)

data class HeaderUpperCutShapeConfiguration(
    val noneColor: Color = Color(0xff839BB9),
    val getColor: Color = Color(0xff37D8CF),
    val payColor: Color = Color(0xffFF4077)
)

data class HeaderContentRightSpaceConfiguration(
    val rightSpace: Float = 38f
)

data class SearchBarConfiguration(
    val horizontalPadding: Float = 16f
)