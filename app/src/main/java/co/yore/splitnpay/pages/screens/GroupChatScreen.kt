package co.yore.splitnpay.pages.screens

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.constraintlayout.compose.ExperimentalMotionApi
import co.yore.splitnpay.components.components.*
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.libs.jerokit.bottom_sheet.Sheeting
import co.yore.splitnpay.libs.jerokit.bottom_sheet.sheeting
import co.yore.splitnpay.libs.jerokit.dep
import co.yore.splitnpay.pages.*
import co.yore.splitnpay.ui.theme.*
import co.yore.splitnpay.viewModels.*

@SuppressLint("Range")
@OptIn(
    ExperimentalMaterialApi::class,
    ExperimentalMotionApi::class,
    ExperimentalAnimationApi::class
)
@Composable
fun GroupChatScreen(
    sheeting: Sheeting = sheeting()
) {
    val sheetState = sheeting.sheetHandler.handle()

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            AnimatedContent(targetState = sheeting.sheets.value) {
                sheeting[it]
            }
        },
        scrimColor = CloudBurst8C,
        sheetBackgroundColor = Color.White,
        sheetShape = RoundedCornerShape(
            topStart = 33.dep(),
            topEnd = 33.dep()
        )
    ) {
        GroupChatPage()
    }
}


