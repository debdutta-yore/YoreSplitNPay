package co.yore.splitnpay.pages.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.libs.jerokit.bottom_sheet.Sheeting
import co.yore.splitnpay.libs.jerokit.bottom_sheet.sheetContent
import co.yore.splitnpay.libs.jerokit.dep
import co.yore.splitnpay.pages.SplitPage
import co.yore.splitnpay.ui.theme.CloudBurst8C
import co.yore.splitnpay.libs.jerokit.bottom_sheet.sheeting
@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Composable
fun SplitScreen(
    sheeting: Sheeting = sheeting()
){
    val sheetState = sheeting.sheetHandler.handle()
    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            sheeting.sheetContent()
        },
        scrimColor = CloudBurst8C,
        sheetBackgroundColor = Color.White,
        sheetShape = RoundedCornerShape(
            topStart = 33.dep(),
            topEnd = 33.dep()
        )
    ) {
        SplitPage()
    }
}
