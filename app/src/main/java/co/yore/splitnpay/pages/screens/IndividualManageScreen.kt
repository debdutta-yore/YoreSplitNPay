package co.yore.splitnpay.pages.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.runtime.Composable
import co.yore.splitnpay.libs.jerokit.bottom_sheet.Sheeting
import co.yore.splitnpay.libs.jerokit.bottom_sheet.sheetContent
import co.yore.splitnpay.libs.jerokit.dep
import co.yore.splitnpay.libs.jerokit.bottom_sheet.sheeting
import co.yore.splitnpay.pages.IndividualManagePage
import co.yore.splitnpay.ui.theme.CloudBurst8C

@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Composable
fun IndividualManageScreen(
    sheeting: Sheeting = sheeting()
) {
    val state = sheeting.sheetHandler.handle()
    ModalBottomSheetLayout(
        sheetState = state,
        sheetContent = {
            sheeting.sheetContent()
        },
        sheetShape = RoundedCornerShape(
            topStart = 25f.dep(),
            topEnd = 25f.dep()
        ),
        scrimColor = CloudBurst8C
    ) {
        IndividualManagePage()
    }
}
