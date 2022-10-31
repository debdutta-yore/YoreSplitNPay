package co.yore.splitnpay.libs.jerokit

import androidx.compose.runtime.Composable
import co.yore.splitnpay.libs.jerokit.bottom_sheet.BottomSheetModel
import co.yore.splitnpay.libs.jerokit.bottom_sheet.LocalSheetMap
import co.yore.splitnpay.libs.jerokit.bottom_sheet.Sheets

@Composable
fun sheetMap(): Map<Sheets, BottomSheetModel>{
    return LocalSheetMap.current
}
