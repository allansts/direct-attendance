package com.direct.attendance.core

import android.app.Application
import com.direct.attendance.di.appModule
import com.direct.attendance.di.mainModule
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin


class AttendanceApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@AttendanceApplication)
            modules(listOf(appModule, mainModule))
        }

        AppCenter.start(
            this,
            "49720b8e-cabf-4dc5-b33a-cca7c501cfb7",
            Analytics::class.java,
            Crashes::class.java
        )
    }
}