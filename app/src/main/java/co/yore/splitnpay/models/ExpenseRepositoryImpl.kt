package co.yore.splitnpay.models

import androidx.compose.ui.graphics.Color
import co.yore.splitnpay.R
import co.yore.splitnpay.components.components.Kal
import co.yore.splitnpay.components.components.YoreDatePickerData
import co.yore.splitnpay.pages.CategoryExpense
import co.yore.splitnpay.pages.PieData

class ExpenseRepositoryImpl : ExpenseRepository {

    private val categories = listOf(
        CategoryExpense(
            icon = R.drawable.ic_trip,
            category = "Trip",
            description = "Business Trip",
            count = 1,
            amount = 500.00f,
            tint = Color(0xffFF4077)
        ),
        CategoryExpense(
            icon = R.drawable.ic_food,
            category = "Food",
            description = "BFFs",
            count = 5,
            amount = 2500.00f,
            tint = Color(0xff1A79E5)
        )
    )

    private val barChartData = listOf(
        ExpenseChartData(
            xAxis = "Jan",
            yAxis = 12000f,
            year = 2022
        ),
        ExpenseChartData(
            xAxis = "Feb",
            yAxis = 25000f,
            year = 2023
        ),
        ExpenseChartData(
            xAxis = "Mar",
            yAxis = 4500f,
            year = 2024
        ),
        ExpenseChartData(
            xAxis = "Apr",
            yAxis = 0f,
            year = 2026
        ),
        ExpenseChartData(
            xAxis = "May",
            yAxis = 1000f,
            year = 2026
        ),
        ExpenseChartData(
            xAxis = "Jun",
            yAxis = 25000f,
            year = 2026
        ),
        ExpenseChartData(
            xAxis = "Jul",
            yAxis = 5000f,
            year = 2027
        ),
        ExpenseChartData(
            xAxis = "Aug",
            yAxis = 50000f,
            year = 2026
        ),
        ExpenseChartData(
            xAxis = "Sep",
            yAxis = 35000f,
            year = 2026
        )
        /*ExpenseChartData(
            xAxis = "Oct",
            yAxis = 0f,
            year = 2026
        ),
        ExpenseChartData(
            xAxis = "Nov",
            yAxis = 2000f,
            year = 2026
        ),
        ExpenseChartData(
            xAxis = "Dec",
            yAxis = 20000f,
            year = 2026
        )*/
    )

    private val pieChartData = listOf(
        PieData(
            Color(0xffFF4077),
            0.5f
        ),
        PieData(
            Color(0xff1A79E5),
            0.5f
        )
    )

    private val datePickerData = YoreDatePickerData(
        selectedDay = 1,
        selectedMonth = 1,
        selectedYear = 2022,
        minDate = Kal.Date.create(1, 1, 2022),
        maxDate = Kal.Date.create(23, 8, 2036),
        dateSelectable = true,
        yearSwitchable = true
    )

    override suspend fun getCategoryExpense(): List<CategoryExpense> {
        return categories
    }

    override suspend fun getBarChartData(): List<ExpenseChartData> {
        return barChartData
    }

    override suspend fun getPieChartData(): List<PieData> {
        return pieChartData
    }

    override suspend fun getDatePickerData(): YoreDatePickerData {
        return datePickerData
    }

}
