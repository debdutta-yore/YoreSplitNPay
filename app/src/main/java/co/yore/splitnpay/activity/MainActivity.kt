package co.yore.splitnpay.activity

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import co.yore.splitnpay.app.YoreApp
import co.yore.splitnpay.components.components.Group
import co.yore.splitnpay.components.components.Member
import co.yore.splitnpay.components.components.MemberEx
import co.yore.splitnpay.libs.LocalResolver
import co.yore.splitnpay.libs.NotificationService
import co.yore.splitnpay.libs.Resolver
import co.yore.splitnpay.localKeyboardHeight
import co.yore.splitnpay.locals.localDesignWidth
import co.yore.splitnpay.models.ContactData
import co.yore.splitnpay.models.DataIds
import co.yore.splitnpay.models.GroupData
import co.yore.splitnpay.pages.IndividualManagePage
import co.yore.splitnpay.pages.Members
import co.yore.splitnpay.pages.SplitCardDetailPage
import co.yore.splitnpay.pages.membersImages
import co.yore.splitnpay.ui.theme.YoreSplitNPayTheme
import java.lang.Integer.min

class MainActivity : ComponentActivity() {
    private val bottomHeight = mutableStateOf(0)
    private var minBottomHeight = Int.MAX_VALUE

    @OptIn(ExperimentalLayoutApi::class)
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val contentView = findViewById<ViewGroup>(android.R.id.content)
        contentView.viewTreeObserver.addOnGlobalLayoutListener {
            val r = Rect()
            contentView.getWindowVisibleDisplayFrame(r)
            val screenHeight = contentView.rootView.height
            val keypadHeight = screenHeight - r.bottom
            minBottomHeight = min(minBottomHeight, keypadHeight)
            bottomHeight.value = keypadHeight - minBottomHeight
        }
        setContent {
            YoreSplitNPayTheme {
                CompositionLocalProvider(
                    localDesignWidth provides 360f,
                    localKeyboardHeight provides bottomHeight.value
                ) {
                    Surface(
                        modifier = Modifier
                            .systemBarsPadding()
                            .fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) {
                        YoreApp()
                    }
                }
            }
        }
    }
}
