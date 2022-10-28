package co.yore.splitnpay.pages

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.models.DataIds
import co.yore.splitnpay.split_page_components.SplitPageContent

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
        scrimColor = Color(0x8C243257),
        sheetBackgroundColor = Color.White,
        sheetShape = RoundedCornerShape(
            topStart = 33.dep(),
            topEnd = 33.dep()
        )
    ) {
        SplitPage()
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SplitPage() {
    Scaffold(
        floatingActionButton = {
            FloatingSplitButton()
        }
    ) {
        SplitPageContent()
    }
}

@Composable
fun BackHandle(
    suffix: String,
    notifier: NotificationService = notifier()
) {
    BackHandler(enabled = true, onBack = {
        notifier.notify("${DataIds.back}$suffix",null)
    })
}
