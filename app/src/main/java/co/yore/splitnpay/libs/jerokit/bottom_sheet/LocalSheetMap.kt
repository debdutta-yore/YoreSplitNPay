package co.yore.splitnpay.libs.jerokit.bottom_sheet

import androidx.compose.runtime.compositionLocalOf

val LocalSheetMap = compositionLocalOf { mapOf<Sheets, BottomSheetModel>() }
val LocalSheeting = compositionLocalOf { Sheeting() }