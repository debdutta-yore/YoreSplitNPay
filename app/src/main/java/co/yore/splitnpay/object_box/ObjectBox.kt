package co.yore.splitnpay.object_box

import android.content.Context
import android.util.Log
import co.yore.splitnpay.BuildConfig
import io.objectbox.BoxStore
import io.objectbox.android.Admin
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

object ObjectBox {
    lateinit var store: BoxStore
        private set

    fun init(context: Context) {
        store = MyObjectBox.builder()
            .androidContext(context.applicationContext)
            .build()

        if (BuildConfig.DEBUG) {
            val started = Admin(store).start(context)
            Log.i("ObjectBoxAdmin", "Started: $started")
        }
    }

    fun get(): BoxStore {
        return store
    }
}

val <T>Class<T>.box get() = ObjectBox.get().boxFor(this)