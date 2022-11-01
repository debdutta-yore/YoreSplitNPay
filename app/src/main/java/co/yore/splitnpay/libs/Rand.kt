package co.yore.splitnpay.libs

import kotlin.random.Random

object Rand {
    private var r: Random? = null
    fun nextFloat(
        min: Number? = null,
        max: Number? = null,
        reseed: Boolean = false,
        biased: Float? = null
    ): Float{
        ensure(reseed)
        val rr = (r?.nextFloat()) ?: 0f
        if (biased != null){
            val bias = r?.nextBoolean() ?: false
            if (bias){
                return biased
            }
        }
        if (min == null && max == null){
            return rr
        }
        val _max = (max ?: 0f).toFloat()
        val _min = (min ?: 0f).toFloat()
        val d = _max - _min
        if (d == 0f){
            return rr
        }
        return rr * d + _min
    }

    private fun ensure(reseed: Boolean = false){
        if (reseed){
            r = Random(System.nanoTime())
            return
        }
        if (r == null){
            r = Random(0)
        }
    }
}
