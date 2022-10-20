package co.yore.splitnpay.libs

fun displayableDate(d: Int, m: Int, y: Int): String {
        val ordinal = when(d%10){
            1-> "st"
            2-> "nd"
            3-> "rd"
            else-> "th"
        }
        val month = when(m){
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
        return "$d$ordinal $month, $y"
    }