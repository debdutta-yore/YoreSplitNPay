package co.yore.splitnpay.pages

import android.annotation.SuppressLint
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import co.yore.splitnpay.demos.SplitButton
import co.yore.splitnpay.split_page.SplitPage

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SplitPageFull() {
    Scaffold(
        floatingActionButton = {
            SplitButton()
        }
    ) {
        SplitPage()
    }
}
