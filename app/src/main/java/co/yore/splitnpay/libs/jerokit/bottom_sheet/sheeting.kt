package co.yore.splitnpay.libs.jerokit.bottom_sheet

import androidx.compose.runtime.Composable

@Composable
fun sheeting(): Sheeting {
    return LocalSheeting.current
}
