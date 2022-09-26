package co.yore.splitnpay.app

import android.app.Application
import co.yore.splitnpay.object_box.ObjectBox

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        ObjectBox.init(this)
    }
}