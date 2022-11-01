package co.yore.splitnpay.libs

import java.util.*

fun <K, V>Map<K, V>.getOrMyDefault(key: K, defualt: V): V {
    return this[key] ?: defualt
}

val currentYear: Int
    get(){
        return Calendar.getInstance().get(Calendar.YEAR)
    }

val currentMonth: Int
    get(){
        return Calendar.getInstance().get(Calendar.MONTH)+1
    }

val currentDay: Int
    get(){
        return Calendar.getInstance().get(Calendar.DATE)
    }