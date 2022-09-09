package co.yore.splitnpay

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import co.yore.splitnpay.friend_item.Friend
import co.yore.splitnpay.friend_item.FriendItem
import co.yore.splitnpay.ui.theme.YoreSplitNPayTheme

class MainActivity : ComponentActivity() {
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
                        LazyColumn(
                            contentPadding = PaddingValues(horizontal = 17.dep())
                        ){
                            items(
                                10,
                                key = {
                                    it
                                }
                            ){
                                FriendItem(
                                    selected = it%2==0,
                                    friend = Friend(
                                        name = "Debdutta Panda $it",
                                        mobile = "8967114927",
                                        imageUrl = ""
                                    ),
                                    contentDescription = "friend_item",
                                    onClicked = {},
                                    onPressed = {}
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}