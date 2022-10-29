package co.yore.splitnpay.components.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import co.yore.splitnpay.addmembers.FontFamilyText
import co.yore.splitnpay.libs.dep
import co.yore.splitnpay.libs.sep
import co.yore.splitnpay.libs.sy
import co.yore.splitnpay.models.CategorySelectorCardConfiguration
import co.yore.splitnpay.models.PaidCardConfiguration
import co.yore.splitnpay.models.SplitCardDetailsData

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


