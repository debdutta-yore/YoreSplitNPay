package co.yore.splitnpay.libs

class DragRecord{
    private var prev = 0
    fun current(value: Int): Int{
        val r = prev - value
        var dir = 0
        if (r < 0){
            dir = -1
        }
        if (r > 0){
            dir = 1
        }
        prev = value
        return dir
    }
}
enum class SwipingStates {
    EXPANDED,
    COLLAPSED
}
