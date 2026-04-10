package com.miltonvaz.voltio1
import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class VoltioApp : Application() {
    override fun onCreate() {
        super.onCreate()

    }
}