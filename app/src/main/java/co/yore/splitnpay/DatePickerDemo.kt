package co.yore.splitnpay

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun DatePickerDemo() {
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