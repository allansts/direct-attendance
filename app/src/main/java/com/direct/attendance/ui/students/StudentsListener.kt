package com.direct.attendance.ui.students

import com.direct.attendance.model.User

interface StudentsListener {

    fun onUpdateStudent(user: User)
}