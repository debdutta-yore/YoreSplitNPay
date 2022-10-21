package co.yore.splitnpay.activity

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.*
import androidx.constraintlayout.compose.*
import co.yore.splitnpay.R
import co.yore.splitnpay.app.YoreApp
import co.yore.splitnpay.components.components.*
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.localKeyboardHeight
import co.yore.splitnpay.locals.localDesignWidth
import co.yore.splitnpay.models.Bank
import co.yore.splitnpay.models.TransactionStatus
import co.yore.splitnpay.models.TransactionType
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
                        /*Box(
                            contentAlignment = Alignment.BottomCenter
                        ){
                            PaymentReviewBottomSheet_6vn06v(
                                contentDescription = "",
                                transaction = TransactionReview(
                                    transactionType = TransactionType.Paid,
                                    paymentMethod = "UPI",
                                    amount = 10000f,
                                    from = Friend(
                                        name = "Rudra Dev",
                                        mobileNumber = "7896230125",
                                        accountNumber = "AC-123",
                                        accountType = AccountType.Current,
                                        imageUrl = "https://i.pravatar.cc/300",
                                        bank = Bank(
                                            name = "SBI",
                                            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/c/cc/SBI-logo.svg/500px-SBI-logo.svg.png?20200329171950"
                                        ),
                                        isSelected = true,
                                        hasRead = false
                                    ),
                                    to = Friend(
                                        name = "Deb Pan",
                                        mobileNumber = "8954102365",
                                        accountNumber = "AC-124",
                                        accountType = AccountType.Current,
                                        imageUrl = "https://i.pravatar.cc/300",
                                        bank = Bank(
                                            name = "Axis",
                                            imageUrl = "https://is3-ssl.mzstatic.com/image/thumb/Purple112/v4/8f/77/e9/8f77e9ee-9cc9-a308-eeda-7b4cbbcdeda6/AppIcon-0-0-1x_U007emarketing-0-0-0-8-0-0-sRGB-0-0-0-GLES2_U002c0-512MB-85-220-0-0.png/146x0w.webp"
                                        ),
                                        isSelected = true,
                                        hasRead = false
                                    ),
                                    category = Category(
                                        name = "Category",
                                        color = 0xffff0000,
                                        icon = R.drawable.travel,
                                    )
                                ),
                                paymentReviewSubCategory = "Hati",
                                notifier = NotificationService{id,arg->

                                }
                            )
                        }*/
                    }
                }
            }
        }
    }
}






