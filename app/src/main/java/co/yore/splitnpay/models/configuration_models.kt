package co.yore.splitnpay.models

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import co.yore.splitnpay.R
import co.yore.splitnpay.ui.theme.*

data class AddGroupButtonConfiguration(
    val backgroundColor: Color = LavenderBlush,
    val size: Float = 40f,
    val icon: Int = R.drawable.ic_group
)

data class AmountPortionConfiguration(
    val startPadding: Float = 30f,
    val endPadding: Float = 30f
)

data class ArrowButtonConfiguration(
    val iconBackgroundColor: Color = WildBlueYonder,
    val iconPressedColor: Color = CuriousBlue,
    val iconTint: Color = Color.White,
    val iconButtonRadius: Float = 9f,
    val iconButtonSize: Float = 28f,
    val iconSize: Float = 11f,
    val iconResource: Int = R.drawable.ic_nextarrow
) {
    companion object {
        val group
            get() = ArrowButtonConfiguration(
                iconBackgroundColor = CuriousBlue,
                iconResource = R.drawable.ic_add_group,
                iconSize = 14f
            )
    }
}

data class BankSymbolIconConfiguration(
    val size: Float = 62f,
    val shadowColor: Color = VeniceBlue4F,
    val borderRadius: Float = 50f,
    val blurRadius: Float = 6f,
    val shadowOffsetX: Float = 0f,
    val shadowOffsetY: Float = 3f,
    val shadowSpread: Float = 0f,
    val shape: Shape = CircleShape,
    val backgroundColor: Color = Color.White,
    val borderStroke: Float = 5f,
    val borderColor: Color = Gallery,
    val icon: Int = R.drawable.ic_upi
)

data class CategoryItemConfiguration(
    val size: Float = 45f,
    val borderStroke: Float = 1f,
    val borderColor: Color = Botticelli,
    val iconSize: Float = 20f,
    val spacerIconText: Float = 7f,
    val textSize: Float = 11f,
    val textColor: Color = Color.Black
)

data class CategoryRowConfiguration(
    val categoryIconSize: Float = 19f,
    val backgroundColor: Color = LavenderBlush,
    val textColor: Color = WildBlueYonder,
    val fontSize: Float = 9f,
    val lineHeight: Float = 11f,
    val iconTextSpacing: Float = 4f
)

data class CategorySelectorCardConfiguration(
    val borderStroke: Float = 1f,
    val borderColor: Color = LinkWater,
    val height: Float = 26f,
    val icon: Int = R.drawable.ic_trip,
    val fontSize: Float = 11f,
    val textColor: Color = CloudBurst,
    val fontWeight: FontWeight = FontWeight(400),
    val iconTopPadding: Float = 4.53f,
    val iconBottomPadding: Float = 6.19f,
    val iconStartPadding: Float = 9.53f,
    val iconTextSpacer: Float = 3.19f,
    val iconEndSpacer: Float = 21f
) {
    companion object {
        val variation1 = CategorySelectorCardConfiguration(
            textColor = RadicalRed,
            fontWeight = FontWeight.Bold,
            fontSize = 12f,
            iconTopPadding = 0f,
            iconBottomPadding = 0f,
            iconStartPadding = 0f,
            iconTextSpacer = 3.67f,
            iconEndSpacer = 0f
        )
    }
}

data class ChatMessageReadStatusConfiguration(
    val checkIconSize: Float = 16f,
    val checkIcon: Int = R.drawable.ic_seen,
    val profileIconSize: Float = 30f,
    val profileIconBorderWidth: Float = 3f,
    val borderStroke: Float = 3f,
    val borderColor: Color = Zumthor,
    val placeholder: Int = R.drawable.ic_profilepic1,
    val iconImageSpacing: Float = 3f
)

data class CheckboxConfiguration(
    val iconColor: Color = CuriousBlue,
    val iconSize: Float = 28f,
    val icon: Int = R.drawable.ic_check,
    val cornerRadius: Float = 10f
)

data class CircleBoxTextConfiguration(
    val backgroundColor: Color = Whitish6,
    val fontSize: Float = 12f,
    val textColor: Color = CloudBurst,
    val lineHeight: Float = 14f,
    val textInnerHorizontalPadding: Float = 8f,
    val textInnerVerticalPadding: Float = 4f,
    val boxStartPadding: Float = 2f,
    val boxHeight: Float = 21f,
    val letterSpacing: Float = -0.333333f
)

data class CircleIconWithBackgroundConfiguration(
    val iconBackgroundSize: Float = 19f,
    val categoryIconSize: Float = 10.23f,
    val backgroundColor: Color = LavenderBlush,
    val textColor: Color = WildBlueYonder,
    val fontSize: Float = 9f,
    val lineHeight: Float = 11f
)

data class ContactSearchBarConfiguration(
    val height: Float = 44f,
    val backgroundColor: Color = Alabaster1,
    val borderRadius: Float = 8f,
    val fontSize: Float = 11f,
    val cursorColor: Color = Color.DarkGray,
    val iconLeftSpace: Float = 20.5f,
    val searchIconId: Int = R.drawable.ic_search,
    val searchIconTint: Color = DustyGray,
    val iconRightSpace: Float = 12.5f,
    val dividerColor: Color = Silver,
    val dividerHeight: Float = 13f,
    val dividerWidth: Float = 1f,
    val dividerRightSpace: Float = 15f,
    val color: Color = Silver,
    val startPadding: Float = 72f
) {
    companion object {
        val upi = ContactSearchBarConfiguration(
            height = 52f,
            searchIconId = R.drawable.ic_upi
        )
    }
}

data class CustomButtonConfiguration(
    val buttonColor: androidx.compose.ui.graphics.Color = CuriousBlue,
    val fontSize: Float = 16f,
    val fontColor: androidx.compose.ui.graphics.Color = Color.White,
    val fontWeight: FontWeight = FontWeight.Bold,
    val cornerRadius: Float = 23.5f
)

data class CustomTextFieldConfiguration(
    val disabledTextColor: Color = Color.Transparent,
    val backgroundColor: Color = Alabaster,
    val iconColor: Color = DoveGray,
    val focusedIndicatorColor: Color = Color.Transparent,
    val unfocusedIndicatorColor: Color = Color.Transparent,
    val disabledIndicatorColor: Color = Color.Transparent,
    val cursorColor: Color = Color.Gray,
    val cornerShape: Float = 8f,
    val contentStartPadding: Float = 15f,
    val contentTopPadding: Float = 10f,
    val contentBottomPadding: Float = 10f,
    val textColor: Color = Silver,
    val textFontSize: Float = 14f,
    val textFontWeight: FontWeight = FontWeight(400),
    val leadingIconWidth: Float = 22f,
    val leadingIconHeight: Float = 14f,
    val dividerWidth: Float = 1f,
    val dividerHeight: Float = 19f,
    val dividerColor: Color = WildBlueYonder,
    val dividerStartPadding: Float = 9f
)

data class DashedBorderIconButtonConfiguration(
    val borderWidth: Float = 1f,
    val borderColor: Color = ChetwodeBlue,
    val borderCorner: Float = 5f,
    val iconWidth: Float = 20f,
    val iconHeight: Float = 18f,
    val fontSize: Float = 10f,
    val textColor: Color = WildBlueYonder
)

data class DateBoxConfiguration(
    val backgroundColor: Color = Color.LightGray.copy(0.25f),
    val textColor: Color = WildBlueYonder,
    val fontSize: Float = 8f,
    val lineHeight: Float = 9.38f,
    val textInnerHorizontalPadding: Float = 7f,
    val textInnerVerticalPadding: Float = 2f
)

data class DateSelectorCardConfiguration(
    val borderStroke: Float = 1f,
    val borderColor: Color = LinkWater,
    val height: Float = 26f,
    val icon: Int = R.drawable.ic_calender,
    val fontSize: Float = 13f,
    val textColor: Color = CloudBurst,
    val fontWeight: FontWeight = FontWeight.Bold
)

data class DeleteIconConfiguration(
    val selectorSize: Float = 20f,
    val backgroundColor: Color = RadicalRed,
    val selectorBorderStroke: Float = 1f,
    val selectorBorderColor: Color = Color.White,
    val selectorIconSize: Float = 12f,
    val selectorIcon: Int = R.drawable.ic_cross,
    val selectorIconTint: Color = Color.Unspecified
)

data class DotConfiguration(
    val color: Color = CuriousBlue1,
    val size: Float = 4f
)

data class EditIconConfiguration(
    val backgroundColor: Color = Zumthor,
    val innerPaddingStart: Float = 7.92f,
    val innerPaddingEnd: Float = 4f,
    val innerPaddingTop: Float = 4f,
    val innerPaddingBottom: Float = 4f,
    val editIcon: Int = R.drawable.ic_pencil,
    val textColor: Color = CuriousBlue,
    val fontSize: Float = 12f,
    val cornerRadius: Float = 11f
)

data class FilterSingleUserConfiguration(
    val padding: Float = 0f,
    val spaceBetweenProfileImageAndUserName: Float = 22f,
    val spaceBetweenUserNameAndPhoneNo: Float = 5f
)

data class FromToPortionConfiguration(
    val startPadding: Float = 31f,
    val endPadding: Float = 30f,
    val bankIconColumnTopPadding: Float = 6f,
    val gapBetweenBankIconAndDetailsPart: Float = 28f,
    val spaceBetweenToAndFrom: Float = 28f
)

data class GroupCardConfiguration(
    val shadowColor: Color = Ghost80,
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
    val cardBackgroundColor: Color = Zumthor,
    val cardUnselectedColor: Color = Color.White,
    val borderStroke: Float = 1f,
    val borderColor: Color = CuriousBlue,
    val type: Type = Type.CHECKABLE
) {
    enum class Type {
        CHECKABLE,
        ARROW,
        GROUP
    }

    companion object {
        val GROUP = GroupCardConfiguration(type = Type.GROUP)
        val checked = GroupCardConfiguration(type = Type.CHECKABLE)
        val unchecked = GroupCardConfiguration(type = Type.ARROW)
    }
}

data class GroupChatTabsConfiguration(
    val horizontalSpace: Float = 11f
)

data class GroupCreationButtonConfiguration(
    val width: Float = 90f,
    val height: Float = 35f,
    val backgroundColor: Color = WildBlueYonder,
    val iconId: Int = R.drawable.ic_add_group,
    val size: Float = 18f
)

data class GroupImagePickerConfiguration(
    val imageSize: Float = 68f,
    val borderStroke: Float = 3f,
    val borderColor: Color = White
)

data class GroupMemberProfilePicsConfiguration(
    val startPadding: Float = 14.5f,
    val smaller: Boolean = false
) {
    companion object {
        val smaller = GroupMemberProfilePicsConfiguration(
            smaller = true,
            startPadding = 8.5f
        )
    }
}

data class GroupProfileImageConfiguration(
    val imageSize: Float = 45f,
    val borderStroke: Float = 2.25f,
    val borderColor: Color = Color.White,
    val placeholder: Int = R.drawable.ic_people,
    val contentScale: ContentScale = ContentScale.Crop,
    val shadowColor: Color = VeniceBlue4F,
    val shadowBorderRadius: Float = 50f,
    val shadowBlurRadius: Float = 4.5f,
    val shadowOffsetX: Float = 0f,
    val shadowOffsetY: Float = 2.25f,
    val shadowSpread: Float = 0f
)

data class HeaderBackAndSplitConfiguration(
    val splitTextId: Int = R.string.split,
    val splitFontSize: Number = 14,
    val tint: Color = Color.White,

    val space: Number = 4
)

data class MonthDayPickerConfiguration(
    val verticalPadding: Float = 9f,
    val height: Float = 89f,
    val backgroundColor: Color = Zumthor,
    val boxSize: Float = 54f,
    val highlightWidth: Float = 64f,
    val highlightBorderRadius: Float = 13f,
    val highlightBackgroundColor: Color = CuriousBlue,
    val apparentCurrentThreshold: Float = 0.1f
)

data class MonthUIConfiguration(
    val width: Float = 74f,
    val height: Float = 42f,
    val borderRadius: Float = 21f,
    val selectedBackgroundColor: Color = Zumthor,
    val fontSize: Float = 14f,
    val disabledColor: Color = Color.Gray,
    val activeColor: Color = CuriousBlue1,
    val color: Color = CloudBurst
)

data class NavIconConfiguration(
    val size: Float = 18f,
    val disabledBackgroundColor: Color = Color.Gray,
    val enabledBackgroundColor: Color = CuriousBlue,
    val drawableId: Int,
    val padding: Float = 4f,
    val iconTint: Color = Color.White
) {
    companion object {
        val Next = NavIconConfiguration(
            drawableId = R.drawable.ic_right_chevron
        )
        val Prev = NavIconConfiguration(
            drawableId = R.drawable.ic_left_chevron
        )
    }
}

data class SelectorIconConfiguration(
    val selectorSize: Float = 20f,
    val backgroundColor: Color = Color.White,
    val selectorBorderStroke: Float = 1f,
    val selectorBorderColor: Color = Color.White,
    val selectorIconSize: Float = 6.67f,
    val selectorIcon: Int = R.drawable.ic_checked_right,
    val selectorIconContentDescription: String = "checked_right",
    val selectorIconTint: Color = Color.Unspecified
)

data class TotalCardConfiguration(
    val textFontSize: Float = 12f,
    val cornerRadius: Float = 8f,
    val borderRadius: Float = 1f,
    val borderColor: Color = Turquoise1,
    val backgroundColor: Color = Dew1,
    val paddingValue: Float = 16f,
    val currencyTextColor: Color = Turquoise1,
    val wholeNumberTextColor: Color = Turquoise1,
    val decNumberTextColor: Color = Turquoise1,
    val trailingTextTextColor: Color = Turquoise1,
    val currencyFontSize: Float = 11f,
    val wholeNumberFontSize: Float = 14f,
    val decNumberFontSize: Float = 10f,
    val trailingTextFontSize: Float = 11f
) {
    companion object {

        val variationOne = TotalCardConfiguration(
            borderColor = RadicalRed,
            backgroundColor = LavenderBlush,
            currencyTextColor = RadicalRed,
            wholeNumberTextColor = RadicalRed,
            decNumberTextColor = RadicalRed,
            trailingTextTextColor = RadicalRed
        )
    }
}

data class YoreAmountConfiguration(
    val fontSize: Number,
    val fontFamily: FontFamily = robotoFonts,
    val color: Color,
    val fontWeight: FontWeight = FontWeight.Normal,
    val decimalPointLocationInDecimalPart: Boolean = true,
    val negative: Boolean = false,

    val currencyFontSize: Number? = null,
    val currencyFontFamily: FontFamily? = null,
    val currencyColor: Color? = null,
    val currencyBaselineShift: Number? = null,
    val currencyFontWeight: FontWeight? = null,

    val wholeFontSize: Number? = null,
    val wholeFontFamily: FontFamily? = null,
    val wholeColor: Color? = null,
    val wholeFontWeight: FontWeight? = null,

    val decimalFontSize: Number? = null,
    val decimalFontFamily: FontFamily? = null,
    val decimalColor: Color? = null,
    val decimalFontWeight: FontWeight? = null
) {

    val _currencyBaselineShift: Float
        get() {
            return (currencyBaselineShift ?: 0).toFloat()
        }
    val _currencyColor: Color
        get() {
            return currencyColor ?: color
        }
    val _currencyFontFamily: FontFamily
        get() {
            return currencyFontFamily ?: fontFamily
        }
    val _currencyFontSize: Number
        get() {
            return currencyFontSize ?: fontSize
        }
    val _currencyFontWeight: FontWeight
        get() {
            return currencyFontWeight ?: fontWeight
        }
    val _decimalColor: Color
        get() {
            return decimalColor ?: color
        }
    val _decimalFontFamily: FontFamily
        get() {
            return decimalFontFamily ?: fontFamily
        }
    val _decimalFontSize: Number
        get() {
            return decimalFontSize ?: fontSize
        }
    val _decimalFontWeight: FontWeight
        get() {
            return decimalFontWeight ?: fontWeight
        }
    val _wholeColor: Color
        get() {
            return wholeColor ?: color
        }
    val _wholeFontFamily: FontFamily
        get() {
            return wholeFontFamily ?: fontFamily
        }
    val _wholeFontSize: Number
        get() {
            return wholeFontSize ?: fontSize
        }
    val _wholeFontWeight: FontWeight
        get() {
            return wholeFontWeight ?: fontWeight
        }

    companion object {

        val splitGroupCardGet = YoreAmountConfiguration(
            color = Turquoise1,
            fontSize = 14,
            currencyFontSize = 12,
            decimalFontSize = 10,
            wholeFontWeight = FontWeight.Bold
        )
        val splitGroupCardPay = YoreAmountConfiguration(
            color = RadicalRed,
            fontSize = 14,
            currencyFontSize = 12,
            decimalFontSize = 10,
            wholeFontWeight = FontWeight.Bold,
            negative = true
        )
        val splitPageHeadContent = YoreAmountConfiguration(
            fontSize = 30,
            color = Color.White,
            wholeFontWeight = FontWeight.Bold,
            currencyFontSize = 21,
            decimalFontSize = 14,
            currencyBaselineShift = -0.2f
        )
    }
}

data class YouWillGetChildPageConfiguration(
    val gapBetweenTwoRow: Float = 18f,
    val bottomHeight: Float = 50f,
    val gapBetweenTwoText: Float = 5f,
    val gapBetweenTextAndIcon: Float = 5f,
    val bottomPadding: Float = 105f
)

data class YouWillGetPayBottomSheetConfiguration(
    val bottomSheetHolderTopPadding: Float = 20f,
    val bottomSheetHolderBottomPadding: Float = 41f,
    val bottomSheetHolderColor: Color = Color.Unspecified,
    val youWillGetTextStartPadding: Float = 33f,
    val gapBetweenYouWillPayAndYouWillGet: Float = 47f,
    val youWillGetBoxTopPadding: Float = 30f,
    val youWillGetBoxStartPadding: Float = 33f,
    val youWillGetBoxEndPadding: Float = 31f,
    val youWillPayBoxTopPadding: Float = 72f
)

data class YouWillGetSingleItemConfiguration(
    val checkBoxEndPadding: Float = 16f,
    val profilePicEndPadding: Float = 22f,
    val gapBetweenNameANdPhNo: Float = 5f,
    val amountBoxWidth: Float = 80f
)

data class SplitButtonConfiguration(
    val backgroundColor: Color = RadicalRed,
    val tint: Color = Color.White,
    val shadowColor: Color = RadicalRed4F,
    val shadowBorderRadius: Float = 100f,
    val shadowBlurRadius: Float = 12f,
    val shadowSpread: Float = 0f,
    val offsetX: Float = 0f,
    val offsetY: Float = 3f,
    val iconId: Int = R.drawable.ic_split_white
)

data class ProfileImageConfiguration(
    val imageSize: Float = 49f,
    val borderStroke: Float = 3f,
    val borderColor: Color = Zumthor,
    val shape: Shape = CircleShape,
    val placeholder: Int = R.drawable.user_dummy4,
    val contentScale: ContentScale = ContentScale.Crop
)

data class SplitTabItemConfiguration(
    val minWidth: Float = 48f,
    val roundedCorner: Float = 13.5f,
    val selectedBackground: Color = CuriousBlue,
    val unSelectedBackground: Color = Zumthor,
    val paddingStart: Float = 9f,
    val paddingEnd: Float = 8f,
    val paddingTop: Float = 7f,
    val paddingBottom: Float = 7f,
    val textSize: Float = 11f,
    val selectedTextColor: Color = Color.White,
    val unSelectedTextColor: Color = CuriousBlue,
    val fontWeight: FontWeight = FontWeight(400)
)

data class SplitBackButtonConfiguration(
    val boxSize: Number = 24,
    val iconSize: Number = 12,
    val iconId: Int = R.drawable.ic_left_chevron,
    val tint: Color = Color.White
)

data class YouWillGetPayCardConfig(
    val height: Float = 153f,
    val width: Float = 149f,

    val shadowColor: Color = Ghost80,
    val shadowBlurRadius: Float = 33f,
    val shadowOffset: Float = 7f,
    val borderRadius: Float = 15f,
    val shadowSpread: Float = 0f,

    val headingTopPadding: Float = 22f,
    val youWillGetStringId: Int = R.string.you_will_get,
    val youWillPayStringId: Int = R.string.you_will_pay,
    val headingFontSize: Int = 16,
    val decimalTextSize: Float = 12f,
    val amountTextSize: Float = 20f,
    val youWillGetIconLeftPadding: Float = 33f,
    val amountTopPadding: Float = 6f,
    val currencyTextSize: Float = 12f,
    val arrowButtonRightPadding: Float = 10f,
    val arrowButtonBottomPadding: Float = 8f,
    val type: Type,
    val amountColor: Color = WildBlueYonder,
    val backgroundColor: Color = Color.White,
    val activeGetColor: Color = Turquoise1,
    val activePayColor: Color = RadicalRed
) {
    enum class Type {
        GET,
        PAY
    }
}

data class PeopleCardConfiguration(
    val cardHeight: Float = 95f,
    val paddingStart: Float = 18f,
    val paddingEnd: Float = 18f,
    val borderStroke: Float = 1f,
    val borderColor: Color = CuriousBlue,
    val cardBackgroundColor: Color = Zumthor,
    val cardUnselectedColor: Color = Color.White,
    val cornerRadius: Float = 22f,
    val blurRadius: Float = 33f,
    val blurOffsetX: Float = 7f,
    val blurOffsetY: Float = 7f,
    val blurSpread: Float = 0f,
    val checkable: Boolean = true
) {
    companion object {
        val checkable = PeopleCardConfiguration(checkable = true)
        val unCheckable = PeopleCardConfiguration(checkable = false)
    }
}

data class YearSwitcherConfiguration(
    val navYearSpace: Float = 40f,
    val fontSize: Float = 16f,
    val color: Color = CloudBurst,
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
    val selectedBackgroundColor: Color = Zumthor,
    val unselectedBackgroundColor: Color = Color.Transparent,
    val fontSize: Float = 14f,
    val selectedColor: Color = CuriousBlue1,
    val unselectedColor: Color = CloudBurst
)

data class WeekDayUIConfiguration(
    val weekDayFontSize: Float = 11f,
    val activeThreshold: Float = 0.1f,
    val weekDayDisabledColor: Color = Color.LightGray,
    val weekDayActiveColor: Color = Color.White,
    val weekDayEnabledColor: Color = CloudBurst,
    val weekDayDateSpace: Float = 12f,
    val dateFontSize: Float = 11f,
    val dateDisabledColor: Color = Color.LightGray,
    val dateActiveColor: Color = Color.White,
    val dateEnabledColor: Color = CuriousBlue
)

data class SingleGroupMemberProfilePicConfiguration(
    val imageSize: Float = 29f,
    val borderWidth: Float = 1f,
    val borderColor: Color = Color.White,
    val placeholder: Int = R.drawable.user_dummy4,
    val contentScale: ContentScale = ContentScale.Crop
) {
    companion object {
        val smaller = SingleGroupMemberProfilePicConfiguration(
            imageSize = 17f
        )
    }
}

data class PeopleRowItemConfiguration(
    val imageSize: Float = 45f
)

data class SplitWithPageContentConfiguration(
    val splitTextId: Int = R.string.split,
    val splitWithTextId: Int = R.string.split_with
)

data class TopbarWithIconConfiguration(
    val iconSize: Float = 12f,
    val iconTint: Color = CloudBurst,
    val icon: Int = R.drawable.ic_left_arrow,
    val textSize: Float = 14f,
    val fontWeight: FontWeight = FontWeight(700),
    val fontColor: Color = CloudBurst,
    val shadowColor: Color = VeniceBlue.copy(alpha = 0.11f),
    val blurRadius: Float = 6f,
    val offsetY: Float = 3f,
    val height: Float = 54f,
    val backgroundColor: Color = Color.White,
    val startPadding: Float = 18f,
    val space: Float = 13f
)

data class TabsConfiguration(
    val backgroundColor: Color = Color.White,
    val startPadding: Float = 15f,
    val fontSize: Float = 21f,
    val color: Color = Botticelli,
    val selectedColor: Color = CloudBurst,
    val height: Float = 73f,
    val dividerThickness: Float = 1f,
    val dividerColor: Color = Zircon2
)

data class YouWillPayGetIconConfig(
    val youWillPayIconWidth: Float = 46f,
    val youWillPayIconHeight: Float = 62f
)

data class YouWillGetPayArrowButtonConfiguration(
    val arrowButtonSize: Float = 26f,
    val icon: Int
) {
    companion object {
        val Get: YouWillGetPayArrowButtonConfiguration
            get() = YouWillGetPayArrowButtonConfiguration(icon = R.drawable.ic_you_will_get_arrow)
        val Pay: YouWillGetPayArrowButtonConfiguration
            get() = YouWillGetPayArrowButtonConfiguration(icon = R.drawable.ic_you_will_pay_arrow)
    }
}

data class TransparentProfilePicConfiguration(
    val imageSize: Float = 29f,
    val backGroundColor: Color = CloudBurst.copy(alpha = 0.65f),
    val borderWidth: Float = 1f,
    val borderColor: Color = Color.White.copy(alpha = 0.5f),
    val fontSize: Float = 12f,
    val fontColor: Color = Color.White,
    val fontWeight: FontWeight = FontWeight.Bold
) {
    companion object {
        val smaller = TransparentProfilePicConfiguration(
            imageSize = 17f,
            fontSize = 8f
        )
    }
}

data class NoGroupHasBeenCreatedYetConfiguration(
    val stringId: Int = R.string.no_group_has_been_created_yet,
    val color: Color = WildBlueYonder,
    val fontSize: Float = 13f
)

data class TextFieldConfiguration(
    val cardHeight: Float = 95f,
    val textFieldBackgroundColor: Color = Alabaster,
    val cursorColor: Color = Color.Gray,
    val cornerRadius: Float = 8f,
    val hintTextSize: Float = 14f,
    val hintTextColor: Color = Silver,
    val textColor: Color = DoveGray,
    val textSize: Float = 14f
)

data class PaidByCardConfiguration(
    val shadowColor: Color = Ghost80,
    val shadowBorderRadius: Float = 15f,
    val shadowBlurRadius: Float = 33f,
    val shadowOffsetX: Float = 7f,
    val shadowOffsetY: Float = 7f,
    val shadowSpread: Float = 0f,
    val cornerRadius: Float = 15f,
    val backgroundColor: Color = Color.White,
    val startPadding: Float = 33f,
    val endPadding: Float = 30f,
    val topPadding: Float = 26f,
    val bottomPadding: Float = 29f,

    val PaidByCardPaidByText: Int = R.string.PaidByCardPaidByText,
    val paidByTextColor: Color = CloudBurst,
    val paidByTextFontSize: Float = 16f,
    val paidByTextBottomSpace: Float = 16f,

    val spaceBetweenTwoRowItem: Float = 18f
)

data class SingleItemYouWillGetCardConfiguration(
    val gapBetweenProfileImageAndUserName: Float = 22f,
    val gapBetweenPUserNameAndUserPhNo: Float = 5f,
    val gapBetweenUserNameAndAmount: Float = 40f
)

data class PaidCardConfiguration(

    val shadowColor: Color = Ghost80,
    val borderRadius: Float = 15f,
    val shadowBlurRadius: Float = 33f,
    val shadowOffsetX: Float = 7f,
    val shadowOffsetY: Float = 7f,
    val shadowSpread: Float = 0f,
    val background: Color = Color.White,
    val startPadding: Float = 31f,
    val endPadding: Float = 49f,
    val topPadding: Float = 21f,
    val bottomPadding: Float = 26f,

    val gapBetweenTwoStatus: Float = 5f,
    val gapBetweenStatusAndSplitMethod: Float = 21f,
    val gapBetweenTwoSplitMethod: Float = 5f,
    val gapBetweenSplitMethodAndCategories: Float = 13f,
    val gapBetweenTwoCategories: Float = 10f,
    val gapBetweenTwoCreatedBy: Float = 5f,
    val gapBetweenCreatedByAndCreatedOn: Float = 21f,
    val gapBetweenTwoCreatedOn: Float = 5f,
    val gapBetweenCreatedOnAndSplitAmong: Float = 13f,
    val gapBetweenTwoSplitAmong: Float = 5f,

    val PaidCardDetailsStatus: Int = R.string.PaidCardDetailsStatus,
    val PaidCardDetailsSplitMethod: Int = R.string.PaidCardDetailsSplitMethod,
    val PaidCardDetailsCategories: Int = R.string.PaidCardDetailsCategories,
    val PaidCardDetailsCreatedBy: Int = R.string.PaidCardDetailsCreatedBy,
    val PaidCardDetailsCreatedOn: Int = R.string.PaidCardDetailsCreatedOn,
    val PaidCardDetailsSplitAmong: Int = R.string.PaidCardDetailsSplitAmong,

    val statusFontSize: Float = 11f,
    val statusColor: Color = WildBlueYonder,

    val partiallyPaidSize: Float = 14f,
    val partiallyPaidColor: Color = Turquoise1,

    val splitMethodFontSize: Float = 11f,
    val splitMethodColor: Color = WildBlueYonder,

    val unequalFontSize: Float = 14f,
    val unequalColor: Color = CloudBurst,

    val categoriesFontSize: Float = 11f,
    val categoriesColor: Color = WildBlueYonder,

    val createdByFontSize: Float = 11f,
    val createdByColor: Color = WildBlueYonder,

    val youFontSize: Float = 14f,
    val youColor: Color = CloudBurst,

    val createdOnFontSize: Float = 11f,
    val createdOnColor: Color = WildBlueYonder,

    val dateFontSize: Float = 14f,
    val dateColor: Color = CloudBurst,

    val splitAmongFontSize: Float = 11f,
    val splitAmongColor: Color = WildBlueYonder,

    val membersFontSize: Float = 14f,
    val membersColor: Color = CloudBurst
)

data class PaymentReviewBottomSheetConfiguration(
    val holderIconTopPadding: Float = 20f,
    val holderBottomPadding: Float = 33f,
    val fromToPortionBottomPadding: Float = 25f,
    val amountSectionBottomPadding: Float = 21f,
    val customTextBoxBottomPadding: Float = 18f,
    val acceptTextBottomPadding: Float = 18f,
    val buttonBottomPadding: Float = 12f
)

data class SelectedIconConfiguration(
    val selectedIconSize: Float = 18f,
    val borderWidth: Float = 1f,
    val borderColor: Color = Color.White,
    val shape: Shape = CircleShape,
    val backGroundColor: Color = RadicalRed,
    val icon: Int = R.drawable.ic_checked_right,
    val iconSize: Float = 7f
)

data class YouWillGetDetailsSingleRowConfiguration(
    val gapBetweenProfileImageAndUserDetails: Float = 22f,
    val gapBetweenUserDetailsAndGetText: Float = 27f,
    val gapBetweenUserNameAndMobileNo: Float = 5f,
    val gapBetweenGetAndPaid: Float = 4f,

    val userNameTextSize: Float = 12f,
    val userPhNoTextSize: Float = 11f,
    val userNameColor: Color = CloudBurst,
    val userPhNoTextColor: Color = SteelBlue,

    val YouWillGetDetailsSingleRowGetText: Int = R.string.YouWillGetDetailsSingleRowGetText,
    val YouWillGetDetailsSingleRowPaidText: Int = R.string.YouWillGetDetailsSingleRowPaidText,
    val getTextColor: Color = Turquoise1,
    val paidTextColor: Color = SteelBlue,
    val getTextFontSize: Float = 11f,
    val paidTextFontSize: Float = 11f,

    val currencyFontSizeForGetAmount: Float = 12f,
    val currencyTextColorForGetAmount: Color = Turquoise1,
    val decNumberTextColorForGetAmount: Color = Turquoise1,
    val wholeNumberTextColorForGetAmount: Color = Turquoise1,
    val wholeNumberFontSizeForGetAmount: Float = 12f,
    val decNumberFontSizeForGetAmount: Float = 10f,

    val currencyFontSizeForPaidAmount: Float = 9f,
    val currencyTextColorForPaidAmount: Color = CloudBurst,
    val decNumberTextColorForPaidAmount: Color = CloudBurst,
    val wholeNumberTextColorForPaidAmount: Color = CloudBurst,
    val wholeNumberFontSizeForPaidAmount: Float = 10f,
    val decNumberFontSizeForPaidAmount: Float = 8f
) {
    companion object {
        val variationOne = YouWillGetDetailsSingleRowConfiguration(
            YouWillGetDetailsSingleRowGetText = R.string.YouWillPayDetailsSingleRowDueText,
            YouWillGetDetailsSingleRowPaidText = R.string.YouWillPayDetailsSingleRowPaidText,
            getTextColor = RadicalRed,
            paidTextColor = SteelBlue,
            currencyTextColorForGetAmount = RadicalRed,
            decNumberTextColorForGetAmount = RadicalRed,
            wholeNumberTextColorForGetAmount = RadicalRed,
            currencyTextColorForPaidAmount = CloudBurst,
            decNumberTextColorForPaidAmount = CloudBurst,
            wholeNumberTextColorForPaidAmount = CloudBurst
        )
    }

}

data class YouWillGetDetailsSingleTabItemConfiguration(
    val cornerRadius: Float = 50f,
    val selectedOptionBackground: Color = Tranquil,
    val unSelectedOptionBackground: Color = Whitish6,
    val selectedOptionTextColor: Color = Turquoise1,
    val unSelectedOptionTextColor: Color = SteelBlue,
    val fontSize: Float = 9f,
    val textTopPadding: Float = 3f,
    val textBottomPadding: Float = 3f,
    val textStartPadding: Float = 9f,
    val textEndPadding: Float = 9f
) {
    companion object {
        val variationOne = YouWillGetDetailsSingleTabItemConfiguration(
            selectedOptionBackground = LavenderBlush,
            unSelectedOptionBackground = Whitish6,
            selectedOptionTextColor = RadicalRed,
            unSelectedOptionTextColor = SteelBlue

        )
    }

}

data class YouWillPayChildPageConfiguration(
    val gapBetweenTwoText: Float = 5f,
    val gapBetweenTextAndIcon: Float = 5f,
    val bottomPadding: Float = 105f,
    val gapBetweenTwoRow: Float = 18f,
    val bottomHeight: Float = 50f
)

data class YouWillPayConfiguration(
    val shadowColor: Color = Black1A,
    val shadowBorderRadius: Float = 15f,
    val shadowBlurRadius: Float = 33f,
    val shadowOffsetX: Float = 5.63f,
    val shadowOffsetY: Float = 7f,
    val shadowSpread: Float = 0f,
    val cornerRadius: Float = 15f,
    val backgroundColor: Color = Color.White,
    val cardStartPadding: Float = 38f,
    val cardEndPadding: Float = 30f,
    val cardTopPadding: Float = 30f,
    val cardBottomPadding: Float = 28f,
    val gapBetweenText: Float = 5f,
    val gapBetweenIconAndTotalCard: Float = 11.3f

)

data class SummarySinglePeopleConfig(
    val profileImageSize: Float = 42f,
    val shadowColor: Color = Black1A,
    val shadowBorderRadius: Float = 50f,
    val shadowBlurRadius: Float = 5.63f,
    val shadowOffsetX: Float = 5.63f,
    val shadowOffsetY: Float = 0f,
    val shadowSpread: Float = 0f,
    val profileImageShape: Shape = CircleShape,
    val selectedBorderColor: Color = RadicalRed,
    val unselectedBorderColor: Color = Color.White,
    val profileImagePlaceHolder: Int = R.drawable.ic_people,
    val gapBetweenProfileImageAndName: Float = 17f
)

data class YouWillGetCardConfiguration(
    val shadowColor: Color = Black1A,
    val shadowBorderRadius: Float = 15f,
    val shadowBlurRadius: Float = 33f,
    val shadowOffsetX: Float = 5.63f,
    val shadowOffsetY: Float = 7f,
    val shadowSpread: Float = 0f,
    val cornerRadius: Float = 15f,
    val backgroundColor: Color = Color.White,
    val gapBetweenTextAndLazyColumn: Float = 19f,
    val gapBetweenLazyColumnAndTotalCard: Float = 34f,
    val bottomPadding: Float = 27f,
    val lazyColumnStartPadding: Float = 32f,
    val lazyColumnTopPadding: Float = 30f,
    val lazyColumnEndPadding: Float = 30f
)

data class SideBarConfiguration(
    val iconSize: Float = 14f,
    val topPadding: Float = 12f,
    val startPadding: Float = 6.5f,
    val endPadding: Float = 6.5f,
    val timeFontSize: Float = 9f,
    val textStartPadding: Float = 21.5f,
    val timeFontWeight: FontWeight = FontWeight.Bold,
    val timeTextColor: Color = White,
    val receivedIcon: Int = R.drawable.ic_arrowbelow,
    val paidIcon: Int = R.drawable.ic_arrow_up_right
)

data class UnSettledMembersConfiguration(
    val gapBetweenTwoRow: Float = 33f,
    val topPaddingOfRadioButton: Float = 14f,
    val topPaddingOfButton: Float = 26f,
    val bottomPaddingOfButton: Float = 25f
)

data class SettledUnsettledMembersBottomSheet(
    val holderTopPadding: Float = 20f,
    val holderBottomPadding: Float = 33f
)

data class SettledMembersConfiguration(
    val gapBetweenTwoRow: Float = 33f,
    val topPaddingOfBottom: Float = 38f,
    val bottomPaddingOfBottom: Float = 25f
)

data class SettledOrUnsettledSingleRowConfiguration(
    val gapBetweenSelectorAndProfileImage: Float = 12f,
    val gapBetweenProfileImageAndUserName: Float = 22f
)

data class SplitAdjustItemConfiguration(
    val horizontalRowPadding: Float = 32f,
    val borderColor: Color = Zumthor,
    val imageSize: Float = 49f,
    val borderStroke: Float = 3f,
    val placeholder: Int = R.drawable.personactionbar,
    val contentScale: ContentScale = ContentScale.Crop,
    val nameFontSize: Float = 12f,
    val nameTextColor: Color = CloudBurst,
    val phoneNumberFontSize: Float = 11f,
    val phoneNumberTextColor: Color = SteelBlue
)

data class SplitMembersRemainingBoxConfiguration(
    val backgroundColor: Color = Dew1,
    val textPaddingVertical: Float = 10f,
    val fontSize: Float = 12f,
    val textColor: Color = Turquoise1
)

data class SplitPaidByItemConfiguration(
    val horizontalRowPadding: Float = 32f,
    val selectedBorderColor: Color = RadicalRed,
    val unSelectedBorderColor: Color = Zumthor,
    val imageSize: Float = 49f,
    val selectedBorderStroke: Float = 2f,
    val unSelectedBorderStroke: Float = 3f,
    val placeholder: Int = R.drawable.personactionbar,
    val contentScale: ContentScale = ContentScale.Crop,
    val nameFontSize: Float = 12f,
    val nameTextColor: Color = CloudBurst,
    val phoneNumberFontSize: Float = 11f,
    val phoneNumberTextColor: Color = SteelBlue
)

data class SplitAdjustAmountConfiguration(
    val backgroundColor: Color = Alabaster1,
    val width: Float = 83f,
    val height: Float = 33f,
    val currencyFontSize: Float = 12f,
    val currencyFontWeight: FontWeight = FontWeight.Normal,
    val currencyScriptSize: Float = 16f,
    val currencyScriptFontWeight: FontWeight = FontWeight.Bold,
    val decimalScriptSize: Float = 10f,
    val decimalScriptFontWeight: FontWeight = FontWeight.Normal,
    val fontWeight: FontWeight = FontWeight.Bold,
    val fontSize: Float = 20f,
    val textColor: Color = CloudBurst,
    val textStartPadding: Float = 8f,
    val textEndPadding: Float = 8f,
    val fontFamily: FontFamily = robotoFonts
)

data class UpiButtonConfiguration(
    val size: Float = 45f,
    val shape: Shape = CircleShape,
    val backGroundColor: Color = Turquoise1,
    val borderColor: Color = CuriousBlue,
    val borderWidth: Float = 1f,
    val textSize: Float = 12f,
    val textColor: Color = Color.White,
    val textWeight: FontWeight = FontWeight(400)
)

data class OutlineButtonConfiguration(
    val size: Float = 45f,
    val shape: Shape = CircleShape,
    val borderColor: Color = CuriousBlue,
    val borderWidth: Float = 1.1f,
    val icon: Int = R.drawable.ic_category_blue_plus,
    val iconTint: Color = Color.Unspecified
)

data class SettlePaymentMethodBottomSheetConfiguration(
    val bottomSheetHolderTopPadding: Float = 20f,
    val bottomSheetHolderTint: Color = Color.Unspecified,
    val bottomPaddingOfHolder: Float = 22f,
    val firstRowHeight: Float = 80f,
    val secondRowHeight: Float = 57f,
    val selectedBackGroundColor: Color = Zumthor,
    val rowStartPadding: Float = 28f,
    val upiRowTopPadding: Float = 21f,
    val bottomPaddingOfContinueButton: Float = 12f
)

data class YouWillGetDetailsTabRowConfiguration(
    val YouWillGetDetailsTabRowPendingItem: Int = R.string.YouWillGetDetailsTabRowPendingItem,
    val YouWillGetDetailsTabRowPartiallyReceivedItem: Int = R.string.YouWillGetDetailsTabRowPartiallyReceivedItem,
    val YouWillGetDetailsTabRowSettledItem: Int = R.string.YouWillGetDetailsTabRowSettledItem
) {
    companion object {
        val variationOne = YouWillGetDetailsTabRowConfiguration(
            YouWillGetDetailsTabRowPartiallyReceivedItem = R.string.YouWillPayDetailsTabRowPartiallyPaidItem
        )
    }

}

data class YouWillGetDetailsConfiguration(
    val shadowColor: Color = Ghost80,
    val shadowBorderRadius: Float = 15f,
    val shadowBlurRadius: Float = 33f,
    val shadowOffsetX: Float = 7f,
    val shadowOffsetY: Float = 7f,
    val shadowSpread: Float = 0f,
    val cornerRadius: Float = 15f,
    val backgroundColor: Color = Color.White,
    val startPadding: Float = 33f,
    val endPadding: Float = 18f,
    val topPadding: Float = 26f,
    val BottomPadding: Float = 18f,

    val memberWillGetText: Int = R.string.member_will_get_text,
    val memberWillPayText: Int = R.string.member_will_pay_text,
    val youWillGetTextFontSize: Float = 16f,
    val youWillGetTextFontColor: Color = CloudBurst,

    val gapBetweenYouWillGetTextAndTabRow: Float = 11f,

    val lazyColumnEndPadding: Float = 26f,
    val lazyColumnTopPadding: Float = 19f,
    val lazyColumnHeight: Float = 185f,
    val gapBetweenTwoRowInLazyColumn: Float = 19f,

    val totalCardStartPadding: Float = 5f,
    val totalCardEndPadding: Float = 12f,
    val totalCardTopPadding: Float = 27f,
    val totalCardBottomPadding: Float = 27f,
    val totalCardText: Int = R.string.YouWillGetDetailsTotalYouWillGet,
    val totalCardTrailingText: Int = R.string.YouWillGetDetailsCr
) {
    companion object {

        val variation1 = YouWillGetDetailsConfiguration(
            lazyColumnHeight = 50f,
            memberWillGetText = R.string.YouWillPayDetailsYouWillPayText,
            totalCardText = R.string.YouWillPayDetailsTotalYouWillPay,
            totalCardTrailingText = R.string.YouWillPayDetailsDr
        )
    }
}
