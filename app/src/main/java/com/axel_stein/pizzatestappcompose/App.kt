package com.axel_stein.pizzatestappcompose

import android.app.Application
import com.axel_stein.pizzatestappcompose.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(appModule)
        }
    }
}