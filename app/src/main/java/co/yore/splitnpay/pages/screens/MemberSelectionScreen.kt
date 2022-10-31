package co.yore.splitnpay.pages.screens

import androidx.compose.animation.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.libs.jerokit.bottom_sheet.Sheeting
import co.yore.splitnpay.libs.jerokit.bottom_sheet.sheetContent
import co.yore.splitnpay.libs.jerokit.dep
import co.yore.splitnpay.libs.jerokit.bottom_sheet.sheeting
import co.yore.splitnpay.pages.SplitWithPageContent
import co.yore.splitnpay.ui.theme.*

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class)
@Composable
fun MemberSelectionScreen(
    sheeting: Sheeting = sheeting()
) {
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
        SplitWithPageContent()
    }
}
