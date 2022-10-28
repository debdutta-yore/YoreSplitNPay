package co.yore.splitnpay.libs

import co.yore.splitnpay.components.components.Kal

fun displayableDate(d: Int?, m: Int?, y: Int?): String {
    if(
        d == null || d == 0
        || m == null || m == 0
        || y == null || y == 0
    ) return ""
    val ordinal = ordinal(d)
    val month = shortMonth(m)
    return "$d$ordinal $month, $y"
}
fun ordinal(d: Int?): String {
    if(d==null || d==0) return ""
    return when(d%10){
        1-> "st"
        2-> "nd"
        3-> "rd"
        else-> "th"
    }
}
fun shortMonth(m: Int): String {
    return when(m){
        1-> "Jan"
        2-> "Feb"
        3-> "Mar"
        4-> "Apr"
        5-> "May"
        6-> "Jun"
        7-> "Jul"
        8-> "Aug"
        9-> "Sep"
        10-> "Oct"
        11-> "Nov"
        12-> "Dec"
        else-> ""
    }
}

data class Date(
    val year: Int,
    val month: Int,
    val day: Int,
)
fun dayDif(
    dt1: Date,
    dt2: Date
): Int{
    if(
        dt1.year==0
        ||dt1.month==0
        ||dt1.day==0

        ||dt2.month==0
        ||dt2.day==0
        ||dt2.year==0
    ) return 0
    val monthDays = intArrayOf(
        31, 28, 31, 30, 31, 30,
        31, 31, 30, 31, 30, 31
    )
    var n1: Int = dt1.year * 365 + dt1.day
    for (i in 0 until dt1.month - 1) {
        n1 += monthDays.get(i)
    }
    n1 += countLeapYears(dt1)
    var n2: Int = dt2.year * 365 + dt2.day
    for (i in 0 until dt2.month - 1) {
        n2 += monthDays.get(i)
    }
    n2 += countLeapYears(dt2)
    return n2 - n1
}

fun countLeapYears(d: Date): Int {
    var years: Int = d.year
    if (d.month <= 2) {
        years--
    }
    return years / 4 - years / 100 + years / 400
}