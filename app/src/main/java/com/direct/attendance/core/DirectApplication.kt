package com.direct.attendance.core

import android.app.Application
import com.direct.attendance.di.appModule
import com.direct.attendance.di.mainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class DirectApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@DirectApplication)
            modules(listOf(appModule, mainModule))
        }
    }
}