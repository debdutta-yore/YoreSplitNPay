package co.yore.splitnpay.models

class Store {
    private val map = mutableMapOf<Any,Any>()

    operator fun get(key: Any) : Any?{
        return map[key]
    }

    operator fun set(key: Any, value: Any){
        map[key] = value
    }

    fun putAll(vararg pairs: Pair<Any,Any>):Store{
        map.putAll(pairs)
        return this
    }

    constructor(vararg pairs: Pair<Any,Any>){
        map.putAll(pairs)
    }

    fun getAll(): Map<Any,Any>{
        return map
    }
}
