package co.yore.splitnpay.activity

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.*
import androidx.constraintlayout.compose.*
import co.yore.splitnpay.app.YoreApp
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.localKeyboardHeight
import co.yore.splitnpay.locals.localDesignWidth
import co.yore.splitnpay.ui.theme.YoreSplitNPayTheme
import java.lang.Integer.min


class MainActivity : ComponentActivity() {
    private val bottomHeight = mutableStateOf(0)
    private var minBottomHeight = Int.MAX_VALUE

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val contentView = findViewById<ViewGroup>(android.R.id.content)
        contentView.viewTreeObserver.addOnGlobalLayoutListener {
            val r = Rect()
            contentView.getWindowVisibleDisplayFrame(r)
            val screenHeight = contentView.rootView.height
            val keypadHeight = screenHeight - r.bottom
            Log.d("ffdjflfd","$keypadHeight")
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
                            .fillMaxSize(),
                        color = MaterialTheme.colors.background,
                    ) {
                        YoreApp()
                    }
                }
            }
        }
    }
}






