package co.yore.splitnpay.libs

fun pack(a: Int, b: Int, C: Int = 10000): Int {
    return a * C + b
}
fun unpack(r: Int, C: Int = 10000): Pair<Int, Int> {
    return Pair(r / C, r % C)
}
