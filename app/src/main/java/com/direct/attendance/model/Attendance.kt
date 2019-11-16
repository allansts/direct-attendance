package com.direct.attendance.model

import com.direct.attendance.constant.ClassTime
import java.io.Serializable
import java.util.Calendar
import java.util.Date

class Attendance : Serializable {

    var date: Date = Date()
    var presents: MutableList<Int> = ArrayList()
    var time: String = ClassTime.MORNING.name

    val enable: Boolean
        get() = isDayOfWeek()

    init {
        presents = listOf(1, 1, 1).toMutableList()
    }

    private fun isDayOfWeek(): Boolean {
        val calendar = Calendar.getInstance()
        calendar.time = date
        val dayOfWeek = calendar[Calendar.DAY_OF_WEEK]
        return dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY
    }

}
