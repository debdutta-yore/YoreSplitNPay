package co.yore.splitnpay.pages

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import co.yore.splitnpay.libs.Sheeting
import co.yore.splitnpay.libs.dep
import co.yore.splitnpay.libs.sheetContent
import co.yore.splitnpay.libs.sheeting

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
        scrimColor = Color(0x8C243257)
    ) {
        IndividualManagePage()
    }
}