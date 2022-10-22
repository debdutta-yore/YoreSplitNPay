package co.yore.splitnpay.libs

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
fun Modifier.ModalBottomSheetAdjustWithIme()
= composed(
    factory = {
        val density = LocalDensity.current.density
        val kh = WindowInsets.ime.getBottom(LocalDensity.current)-
                (WindowInsets.systemBars.asPaddingValues().calculateBottomPadding().value*density)
        val keyboardOpen = WindowInsets.isImeVisible
        val scrollState = rememberScrollState()
        val scope = rememberCoroutineScope()
        val screenHeight = LocalConfiguration.current.screenHeightDp
        LaunchedEffect(key1 = keyboardOpen){
            if(keyboardOpen){
                scope.launch {
                    scrollState.animateScrollTo(Int.MAX_VALUE)
                }
            }
        }
        if (keyboardOpen) {
            Modifier
                .imePadding()
                .heightIn(min = 0.dp, max = ((screenHeight - kh / density)).dp)
                .verticalScroll(scrollState)
        } else {
            Modifier
        }
    }
)