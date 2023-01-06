package co.yore.splitnpay

object DataBank {
    private val _once = mutableMapOf<Key,Any?>()
    private val _ever = mutableMapOf<Key,Any?>()
    enum class Key{
        Members,
        BillTotal,
        SplitDescription,
        SplitCategory,
        ExpenseData,
        SplitMembers,
        GroupName,
        GroupImage
    }
    object once{
        operator fun set(key: Key, value: Any?){
            _once[key] = value
        }
        operator fun get(key: Key): Any?{
            return _once.remove(key)
        }
    }
    object ever{
        operator fun set(key: Key, value: Any?){
            _ever[key] = value
        }
        operator fun get(key: Key): Any?{
            return _ever[key]
        }
        fun delete(key: Key){
            _ever.remove(key)
        }
    }
}