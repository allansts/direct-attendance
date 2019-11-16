package com.direct.attendance.di

import com.direct.attendance.ui.attendance.AttendanceFragment
import com.direct.attendance.ui.attendance.AttendanceViewModelFactory
import com.direct.attendance.ui.classes.ClassesFragment
import com.direct.attendance.ui.classes.ClassesViewModelFactory
import com.direct.attendance.ui.launch.LaunchActivity
import com.direct.attendance.ui.launch.LaunchViewModelFactory
import com.direct.attendance.ui.settings.SettingsFragment
import com.direct.attendance.ui.settings.SettingsViewModelFactory
import com.direct.attendance.ui.students.StudentsFragment
import com.direct.attendance.ui.students.StudentsViewModelFactory
import org.koin.core.qualifier.named
import org.koin.dsl.module

val mainModule = module {

    scope(named<LaunchActivity>()) {

        scoped {
            LaunchViewModelFactory(application = get())
        }
    }

    scope(named<StudentsFragment>()) {

        scoped {
            StudentsViewModelFactory(application = get())
        }

    }

    scope(named<SettingsFragment>()) {

        scoped {
            SettingsViewModelFactory(application = get())
        }
    }

    scope(named<AttendanceFragment>()) {

        scoped {
            AttendanceViewModelFactory(application = get())
        }
    }

    scope(named<ClassesFragment>()) {

        scoped {
            ClassesViewModelFactory(application = get())
        }
    }

}