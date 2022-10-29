package co.yore.splitnpay.models

import co.yore.splitnpay.R


interface TimeFilterRepository {
    suspend fun getItems(): List<SheetItem>
}



class TimeFilterRepositoryImpl : TimeFilterRepository {

    private val itemList = listOf(
        SheetItem(id = 0, R.drawable.calendar_1, "All Time"),
        SheetItem(id = 1, R.drawable.last_month, "Last Month"),
        SheetItem(id = 2, R.drawable.this_month, "This Month"),
        SheetItem(id = 3, R.drawable.custom_month, "Custom Month")
    )

    override suspend fun getItems(): List<SheetItem> {
        return itemList
    }
}