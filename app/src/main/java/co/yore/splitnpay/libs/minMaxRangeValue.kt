package co.yore.splitnpay.libs

fun minMaxRangeValue(percentage: Float, max: Float, min: Float): Float {
    return (percentage * (max - min) / 100) + min
}
