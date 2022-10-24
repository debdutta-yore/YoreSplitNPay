package co.yore.splitnpay.components.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import co.yore.splitnpay.R
import co.yore.splitnpay.addmembers.FontFamilyText
import co.yore.splitnpay.demos.sy
import co.yore.splitnpay.libs.dep
import co.yore.splitnpay.libs.sep
import co.yore.splitnpay.pages.SplitDetailedCard
import co.yore.splitnpay.ui.theme.DarkBlue
import co.yore.splitnpay.ui.theme.GreyShadow
import co.yore.splitnpay.ui.theme.LightBlue4
import co.yore.splitnpay.ui.theme.LightGreen3

@Composable
fun PaidCardDetails_d6za8c(
    modifier: Modifier = Modifier,
    contentDescription: String,
    config: PaidCardConfiguration = PaidCardConfiguration(),
    data: SplitCardDetailsData
) {
    Box(
        modifier = modifier
            .semantics { this.contentDescription = contentDescription }
            .coloredShadow(
                color = config.shadowColor,
                borderRadius = config.borderRadius.dep(),
                blurRadius = config.shadowBlurRadius.dep(),
                offsetX = config.shadowOffsetX.dep(),
                offsetY = config.shadowOffsetY.dep(),
                spread = config.shadowSpread
            )
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(config.borderRadius.dep())
            )
            .background(config.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = config.startPadding.dep(),
                    end = config.endPadding.dep(),
                    top = config.topPadding.dep(),
                    bottom = config.bottomPadding.dep()
                ),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                FontFamilyText(
                    text = stringResource(config.PaidCardDetailsStatus),
                    fontSize = config.statusFontSize.sep(),
                    color = config.statusColor
                )

                config.gapBetweenTwoStatus.sy()

                FontFamilyText(
                    text = data.status,
                    fontSize = config.partiallyPaidSize.sep(),
                    color = config.partiallyPaidColor,
                    fontWeight = FontWeight(500)
                )

                config.gapBetweenStatusAndSplitMethod.sy()

                FontFamilyText(
                    text = stringResource(config.PaidCardDetailsSplitMethod),
                    fontSize = config.splitMethodFontSize.sep(),
                    color = config.splitMethodColor
                )

                config.gapBetweenTwoSplitMethod.sy()

                FontFamilyText(
                    text = data.splitMethod,
                    fontSize = config.unequalFontSize.sep(),
                    color = config.unequalColor
                )

                config.gapBetweenSplitMethodAndCategories.sy()

                FontFamilyText(
                    text = stringResource(config.PaidCardDetailsCategories),
                    fontSize = config.categoriesFontSize.sep(),
                    color = config.categoriesColor
                )

                config.gapBetweenTwoCategories.sy()

                Category_rhu0vy(
                    text = data.categories,
                    icon = data.categoryIcon,
                    config = CategorySelectorCardConfiguration.variation1,
                    contentDescription = " CategorySelectorCard"
                )

            }

            Column() {

                FontFamilyText(
                    text = stringResource(config.PaidCardDetailsCreatedBy),
                    fontSize = config.createdByFontSize.sep(),
                    color = config.createdByColor
                )

                config.gapBetweenTwoCreatedBy.sy()

                FontFamilyText(
                    text = data.createdBy,
                    fontSize = config.youFontSize.sep(),
                    color = config.youColor
                )

                config.gapBetweenCreatedByAndCreatedOn.sy()

                FontFamilyText(
                    text = stringResource(config.PaidCardDetailsCreatedOn),
                    fontSize = config.createdOnFontSize.sep(),
                    color = config.createdOnColor
                )

                config.gapBetweenTwoCreatedOn.sy()

                FontFamilyText(
                    text = data.createdOn,
                    fontSize = config.dateFontSize.sep(),
                    color = config.dateColor
                )

                config.gapBetweenCreatedOnAndSplitAmong.sy()

                FontFamilyText(
                    text = stringResource(config.PaidCardDetailsSplitAmong),
                    fontSize = config.splitAmongFontSize.sep(),
                    color = config.splitAmongColor
                )

                config.gapBetweenTwoSplitAmong.sy()

                FontFamilyText(
                    text = "${data.noOfMembers} Members",
                    fontSize = config.membersFontSize.sep(),
                    color = config.membersColor
                )
            }

        }

    }

}

data class PaidCardConfiguration(

    val shadowColor: Color = GreyShadow,
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
    val statusColor: Color = LightBlue4,

    val partiallyPaidSize: Float = 14f,
    val partiallyPaidColor: Color = LightGreen3,

    val splitMethodFontSize: Float = 11f,
    val splitMethodColor: Color = LightBlue4,

    val unequalFontSize: Float = 14f,
    val unequalColor: Color = DarkBlue,

    val categoriesFontSize: Float = 11f,
    val categoriesColor: Color = LightBlue4,

    val createdByFontSize: Float = 11f,
    val createdByColor: Color = LightBlue4,

    val youFontSize: Float = 14f,
    val youColor: Color = DarkBlue,

    val createdOnFontSize: Float = 11f,
    val createdOnColor: Color = LightBlue4,

    val dateFontSize: Float = 14f,
    val dateColor: Color = DarkBlue,

    val splitAmongFontSize: Float = 11f,
    val splitAmongColor: Color = LightBlue4,

    val membersFontSize: Float = 14f,
    val membersColor: Color = DarkBlue
)

data class SplitCardDetailsData(
    val status: String,
    val splitMethod: String,
    val categories: String,
    val categoryIcon: Any?,
    val createdBy: String,
    val createdOn: String,
    val noOfMembers: Int
){
    companion object{
        val blank = SplitCardDetailsData(
            status = "",
            splitMethod = "",
            categories = "",
            createdBy = "",
            createdOn = "",
            noOfMembers = 0,
            categoryIcon = null,
        )
    }
}
