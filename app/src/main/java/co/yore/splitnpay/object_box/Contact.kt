package co.yore.splitnpay.object_box

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class Contact(
    @JvmField
    @Id
    var id: Long = 0,
    @JvmField
    var mobile: String
)