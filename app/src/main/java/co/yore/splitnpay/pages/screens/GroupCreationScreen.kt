package co.yore.splitnpay.pages.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.runtime.Composable
import co.yore.splitnpay.components.components.*
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.libs.jerokit.bottom_sheet.Sheeting
import co.yore.splitnpay.libs.jerokit.dep
import co.yore.splitnpay.pages.GroupCreationPageContent
import co.yore.splitnpay.pages.subpages.PhotoSelectionBottomSheet
import co.yore.splitnpay.ui.theme.*

import co.yore.splitnpay.libs.jerokit.bottom_sheet.sheeting
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GroupCreationScreen(
    sheeting: Sheeting = sheeting()
) {
    val state = sheeting.sheetHandler.handle()
    ModalBottomSheetLayout(
        sheetState = state,
        sheetContent = {
            PhotoSelectionBottomSheet()
        },
        sheetShape = RoundedCornerShape(
            topStart = 25f.dep(),
            topEnd = 25f.dep()
        ),
        scrimColor = CloudBurst8C
    ) {
        GroupCreationPageContent()
    }
}
