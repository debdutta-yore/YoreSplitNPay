package co.yore.splitnpay.app

import android.app.Application
import co.yore.splitnpay.object_box.ObjectBox
import dagger.hilt.android.HiltAndroidApp

object AppContext {
    private lateinit var _app: App
    val app: App get() = _app
    fun init(app: App) {
        _app = app
    }
}

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        AppContext.init(this)
        ObjectBox.init(this)
    }
}
