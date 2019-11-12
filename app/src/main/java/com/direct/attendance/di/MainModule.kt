package com.direct.attendance.di

import com.direct.attendance.ui.home.HomeFragment
import com.direct.attendance.ui.home.HomeViewModelFactory
import com.direct.attendance.ui.settings.SettingsFragment
import com.direct.attendance.ui.settings.SettingsViewModelFactory
import org.koin.core.qualifier.named
import org.koin.dsl.module

val mainModule = module {

    scope(named<HomeFragment>()) {

        scoped {
            HomeViewModelFactory(application = get())
        }

    }

    scope(named<SettingsFragment>()) {

        scoped {
            SettingsViewModelFactory(application = get())
        }
    }

}