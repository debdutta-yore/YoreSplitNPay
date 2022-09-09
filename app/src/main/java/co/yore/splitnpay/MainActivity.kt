package co.yore.splitnpay

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.yore.splitnpay.ui.theme.YoreSplitNPayTheme
import co.yore.splitnpay.you_will_get_card.YouWillGetCard
import co.yore.splitnpay.you_will_get_card.YouWillPayCard

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
                        /*LazyColumn(
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
                        }*/

                        /*Box(
                            contentAlignment = Alignment.Center
                        ){
                            YouWillPayCard(whole = "100", decimal = "20")
                        }*/

                        Box {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(333.dep())
                                    .background(Color.White)
                            ){
                                Box {
                                    Column(){
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(219.dep())
                                                .clip(
                                                    RoundedCornerShape(
                                                        0.dp, 0.dp, 0.dp, 47.dep()
                                                    )
                                                )
                                                .background(Color(0xff839BB9))
                                        ){
                                            Box(
                                                modifier = Modifier
                                                    .padding(
                                                        top = 13.dep(),
                                                        start = 9.dep()
                                                    )
                                            ){
                                                Row(){
                                                    BackButton()
                                                    Spacer(modifier = Modifier.width(4.dep()))
                                                    RobotoText(
                                                        "Split",
                                                        fontSize = 14.sep(),
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color.White
                                                    )
                                                }
                                            }
                                            Column(
                                                modifier = Modifier
                                                    .align(Alignment.TopCenter)
                                                    .padding(top = 83.dep())
                                            ){
                                                RobotoText(
                                                    "Split Balance",
                                                    fontSize = 12.sep(),
                                                    color = Color.White,

                                                    )
                                                Row(
                                                    verticalAlignment = Alignment.Bottom
                                                ) {
                                                    RobotoText(
                                                        localCurrency.current,
                                                        fontSize = 21.sep(),
                                                        color = Color.White
                                                    )
                                                    Spacer(modifier = Modifier.width(4.dep()))
                                                    RobotoText(
                                                        "00",
                                                        fontSize = 30.sep(),
                                                        color = Color.White,
                                                        fontWeight = FontWeight.Bold,
                                                        modifier = Modifier
                                                            .alignByBaseline()
                                                    )
                                                    RobotoText(
                                                        ".00",
                                                        fontSize = 14.sep(),
                                                        color = Color.White,
                                                        modifier = Modifier
                                                            .alignByBaseline()
                                                    )
                                                }
                                            }
                                        }
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(60.dp)
                                                .background(Color(0xff839BB9))
                                        ){
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(60.dp)
                                                    .clip(
                                                        RoundedCornerShape(
                                                            0.dp, 47.dep(), 0.dp, 0.dp
                                                        )
                                                    )
                                                    .background(Color.White)
                                            ){

                                            }
                                        }
                                    }
                                }
                                Box(
                                    modifier = Modifier.align(Alignment.BottomCenter)
                                ){
                                    Row(){
                                        YouWillGetCard(whole = "00", decimal = "00")
                                        Spacer(modifier = Modifier.width(22.dep()))
                                        YouWillPayCard(whole = "00", decimal = "00")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun BackButton() {
        Box(
            modifier = Modifier
                .size(24.dep())
                .clip(CircleShape)
                .clickable { }
            ,
            contentAlignment = Alignment.Center
        ){
            Icon(
                modifier = Modifier.size(12.dep()),
                painter = painterResource(id = R.drawable.ic_back_arrow),
                contentDescription = "",
                tint = Color.White
            )
        }
    }
}