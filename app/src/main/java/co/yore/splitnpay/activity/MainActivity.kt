package co.yore.splitnpay.activity

import android.annotation.SuppressLint
import android.database.ContentObserver
import android.graphics.Rect
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import co.yore.splitnpay.app.YoreApp
import co.yore.splitnpay.libs.EventBus
import co.yore.splitnpay.libs.jerokit.localDesignWidth
import co.yore.splitnpay.ui.theme.YoreSplitNPayTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

        val contentObserver = MyContentObserver()
        applicationContext.contentResolver.registerContentObserver(
            ContactsContract.Contacts.CONTENT_URI,
            true,
            contentObserver
        )

        setContent {
            YoreSplitNPayTheme {
                CompositionLocalProvider(
                    localDesignWidth provides 360f,
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

    internal class MyContentObserver : ContentObserver(null) {
        override fun onChange(selfChange: Boolean) {
            super.onChange(selfChange)
            Log.d("jflflfdfd","content changed")
            CoroutineScope(Dispatchers.IO).launch {
                EventBus.publish("content" to null)
            }
        }
    }
}
