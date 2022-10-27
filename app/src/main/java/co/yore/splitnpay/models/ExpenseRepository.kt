package co.yore.splitnpay.models

import co.yore.splitnpay.components.components.YoreDatePickerData
import co.yore.splitnpay.pages.CategoryExpense
import co.yore.splitnpay.pages.ExpenseChartData
import co.yore.splitnpay.pages.PieData

interface ExpenseRepository {
    suspend fun getCategoryExpense(): List<CategoryExpense>
    suspend fun getBarChartData(): List<ExpenseChartData>
    suspend fun getPieChartData(): List<PieData>
    suspend fun getDatePickerData(): YoreDatePickerData
}