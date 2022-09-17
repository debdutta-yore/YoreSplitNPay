package co.yore.splitnpay


data class FloatSplitted(
    val wholeString: String,
    val decString: String,
    val whole: Int,
)

fun Float.splitted(): FloatSplitted {
    val text = this.toString()
    val parts = text.split(".")
    var wholeText = parts[0]
    var decText = parts[1]
    val whole = wholeText.toInt()
    if(whole<10){
        wholeText = "0$whole"
    }
    if(decText.length<2){
        decText = "${decText}0"
    }
    if(decText.length>2){
        decText = decText.substring(0..1)
    }
    return FloatSplitted(
        wholeText,
        decText,
        whole,
    )
}