package co.yore.splitnpay.libs

import java.util.*

fun <T>Collection<T>.takeSome(min: Int, max: Int): Collection<T>{
    val count = (Random(System.nanoTime()).nextFloat()*(max-min)).toInt()+min
    val indices = (0 until this.size).toList().shuffled()
    return indices.take(count).map {
        this.elementAt(it)
    }
}