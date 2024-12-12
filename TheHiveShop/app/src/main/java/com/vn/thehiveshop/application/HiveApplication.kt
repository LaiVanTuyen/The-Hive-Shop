package com.vn.thehiveshop.application

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class HiveApplication :Application() {
    companion object {
        lateinit var instance: HiveApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }
}