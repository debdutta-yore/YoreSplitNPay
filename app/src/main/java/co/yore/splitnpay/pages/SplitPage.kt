package co.yore.splitnpay.pages

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import co.yore.splitnpay.demos.FloatingSplitButton
import co.yore.splitnpay.libs.NotificationService
import co.yore.splitnpay.libs.notifier
import co.yore.splitnpay.models.DataIds
import co.yore.splitnpay.split_page_components.SplitPageContent

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SplitPage() {
    Scaffold(
        floatingActionButton = {
            FloatingSplitButton()
        },
        modifier = Modifier.safeDrawingPadding()
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
