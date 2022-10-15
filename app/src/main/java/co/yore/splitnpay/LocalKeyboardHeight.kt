package co.yore.splitnpay

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf

val localKeyboardHeight = compositionLocalOf { 0 }

@Composable
fun keyboardHeight(): Int{
    return localKeyboardHeight.current
}