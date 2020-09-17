package inf3995_01.bixi

import android.app.Application

class App: Application() {
    companion object {
        lateinit var instance: App
        val database:Database by lazy {
            Database (instance)
        }
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}