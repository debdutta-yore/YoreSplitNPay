package co.yore.splitnpay.pages.screens

import androidx.compose.animation.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.libs.jerokit.bottom_sheet.Sheeting
import co.yore.splitnpay.libs.jerokit.bottom_sheet.sheetContent
import co.yore.splitnpay.libs.jerokit.dep
import co.yore.splitnpay.pages.SplitDetailsPage
import co.yore.splitnpay.ui.theme.*
import co.yore.splitnpay.libs.jerokit.bottom_sheet.sheeting

@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Composable
fun SplitDetailsScreen(
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
        SplitDetailsPage()
    }
}