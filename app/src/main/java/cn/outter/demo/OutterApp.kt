package cn.outter.demo

import android.app.Application

class OutterApp : Application() {

    companion object {
        @JvmStatic
        lateinit var application: Application
    }

    override fun onCreate() {
        super.onCreate()
        application = this
    }
}