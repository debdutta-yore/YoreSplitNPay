package co.yore.splitnpay.activity

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
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
import co.yore.splitnpay.components.components.SettledUnsettledMembersBottomSheet_mxjiuq
import co.yore.splitnpay.localKeyboardHeight
import co.yore.splitnpay.locals.localDesignWidth
import co.yore.splitnpay.pages.SettleOptions
import co.yore.splitnpay.pages.SingleSettledOrUnsettledMember
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
                        /*Box() {
                            SettledUnsettledMembersBottomSheet_mxjiuq(
                                settledMembers = listOf(
                                    SingleSettledOrUnsettledMember(
                                        selectedSettleOption = SettleOptions.SplitIndividual,
                                        isChecked = true,
                                        isSettledMember = true,
                                        imageUrl = "https://i.pravatar.cc/300",
                                        userName = "Sushil Roy",
                                        userPhNo = "8967114927",
                                        getAmount = 600f,
                                        paidAmount = 0f
                                    )
                                ),
                                unsettledMembers = listOf(
                                    SingleSettledOrUnsettledMember(
                                        selectedSettleOption = SettleOptions.SplitIndividual,
                                        isChecked = true,
                                        isSettledMember = false,
                                        imageUrl = "https://i.pravatar.cc/300",
                                        userName = "Sushil Roy",
                                        userPhNo = "8967114927",
                                        getAmount = 0f,
                                        paidAmount = 600f
                                    )
                                )
                            )
                        }*/
                    }
                }
            }
        }
    }
}
