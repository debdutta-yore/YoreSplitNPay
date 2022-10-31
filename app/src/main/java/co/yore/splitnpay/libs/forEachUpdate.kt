package co.yore.splitnpay.libs

fun <T>MutableList<T>.forEachUpdate(block: (T) -> T){
    val count = this.size
    for (i in 0 until count){
        this[i] = block(this[i])
    }
}
