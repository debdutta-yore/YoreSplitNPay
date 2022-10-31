package co.yore.splitnpay.pages

import android.annotation.SuppressLint
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import co.yore.splitnpay.components.components.FloatingSplitButton
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.pages.split_page_components.SplitPageContent

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


