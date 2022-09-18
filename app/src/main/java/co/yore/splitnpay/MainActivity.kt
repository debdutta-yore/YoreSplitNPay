package co.yore.splitnpay

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import co.yore.splitnpay.split_page.SplitPage
import co.yore.splitnpay.ui.theme.YoreSplitNPayTheme



class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YoreSplitNPayTheme {
                // A surface container using the 'background' color from the theme
                CompositionLocalProvider(localFullWidth provides 360f) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) {
                        CompositionLocalProvider(yoreNotificationService provides NotificationService{ id, arg->
                            Log.d("fldjfldkf","$id=$arg")
                        }) {
                            SplitPage(
                                wholeGet="00",
                                decGet="00",
                                wholePay="00",
                                decPay="00",
                                whole="00",
                                decimal="00",
                            )
                        }
                    }
                }
            }
        }
    }




}
