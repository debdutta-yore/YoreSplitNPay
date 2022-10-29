package co.yore.splitnpay.models

interface GroupOrContact{
    fun id(): Any
    fun lastActivity():Long
    fun searchables(): List<String>
    fun willGet(): Float
    fun willPay(): Float
}

