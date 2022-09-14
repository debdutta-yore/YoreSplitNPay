package co.yore.splitnpay

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import co.yore.splitnpay.ui.theme.YoreSplitNPayTheme
import java.util.*

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("fdlfjdfjd", Calendar.getInstance().get(Calendar.MONTH).toString())
        setContent {
            YoreSplitNPayTheme {
                // A surface container using the 'background' color from the theme
                CompositionLocalProvider(localFullWidth provides 360f) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) {
                        //SplitPageDemo()
                        Box(
                            contentAlignment = Alignment.Center
                        ){
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ){
                                Divider()
                                var selectedDay by remember {
                                    mutableStateOf<Int?>(17)
                                }
                                var selectedMonth by remember {
                                    mutableStateOf<Int?>(4)
                                }
                                var selectedYear by remember {
                                    mutableStateOf(2022)
                                }
                                val yoreDatePickerData by remember {
                                    derivedStateOf {
                                        return@derivedStateOf YoreDatePickerData(
                                            selectedDay,
                                            selectedMonth,
                                            selectedYear,
                                            Kal.Date.create(22,3,2022),
                                            Kal.Date.create(23,8,2026),
                                        )
                                    }
                                }
                                YoreDatePicker(
                                    yoreDatePickerData,
                                    {
                                        selectedYear = it
                                        selectedMonth = null
                                        selectedDay = null
                                    },
                                    {
                                        selectedMonth = it
                                        selectedDay = null
                                    },
                                    {
                                        selectedDay = it
                                    }
                                )
                                Divider()
                                val myDate by remember {
                                    derivedStateOf {
                                        "$selectedDay/${selectedMonth}/$selectedYear"
                                    }
                                }
                                Text(myDate)
                            }
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    private fun SplitPageDemo() {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {

                    }, backgroundColor = colorResource(id = R.color.pink),
                    contentColor = Color.White,
                    modifier = Modifier
                        .coloredShadow(
                            color = Color(0x4fff4077),
                            borderRadius = 100.dep(),
                            blurRadius = 12.dep(),
                            spread = 0f,
                            offsetX = 0.dep(),
                            offsetY = 3.dep(),
                        ),
                    shape = CircleShape,
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp,
                        hoveredElevation = 0.dp,
                        focusedElevation = 0.dp,
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_split_white),
                        "", tint = Color.Unspecified
                    )
                }
            }
        ) {
            SplitPage()
        }
    }
}
