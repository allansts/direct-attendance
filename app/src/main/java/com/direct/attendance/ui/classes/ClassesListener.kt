package com.direct.attendance.ui.classes

import com.direct.attendance.model.ClassRoom

interface ClassesListener {

    fun onUpdateClass(classRoom: ClassRoom)
}