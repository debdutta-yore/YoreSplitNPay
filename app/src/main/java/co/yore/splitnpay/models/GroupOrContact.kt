package co.yore.splitnpay.models

interface GroupOrContact{
    fun id(): Any
    fun lastActivity():Long
    fun searchables(): List<String>
    fun willGet(): Float
    fun willPay(): Float
}

data class ContactData(
    val id: Any,
    val image: Any?,
    val name: String,
    val mobile: String,
    val willPay: Float = 0.0f,
    val willGet: Float = 0.0f,
    val selected: Boolean = false,
    val lastActivity: Long = 0,
    val deletable: Boolean = true
): GroupOrContact {
    override fun id(): Any {
        return this.id
    }

    override fun lastActivity(): Long {
        return this.lastActivity
    }

    override fun searchables(): List<String> {
        return listOf(
            name.lowercase(),
            mobile
        )
    }

    override fun willGet(): Float {
        return willGet
    }

    override fun willPay(): Float {
        return willPay
    }
}

data class GroupData(
    val id: Any,
    val image: Any?,
    val name: String,
    val members: List<ContactData>,
    val willGet: Float = 0.0f,
    val willPay: Float = 0.0f,
    val showGroupBalance: Boolean = willGet>0f||willPay>0f,
    val lastActivity: Long = 0
): GroupOrContact {
    override fun id(): Any {
        return this.id
    }

    override fun lastActivity(): Long {
        return this.lastActivity
    }

    override fun searchables(): List<String> {
        return listOf(
            name.lowercase(),
        )
    }

    override fun willGet(): Float {
        return willGet
    }

    override fun willPay(): Float {
        return willPay
    }
}