package com.direct.attendance.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.direct.attendance.constant.DatePatterns
import com.direct.attendance.database.AttendanceTypeConverter
import com.direct.attendance.extension.isNull
import com.direct.attendance.extension.toDate
import com.direct.attendance.extension.withoutTime
import java.io.Serializable
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Entity
class User(
    var name: String,
    var surname: String,
    var startedDate: String,
    var gender: String,
    var classTime: String,
    var classRoomId: Int,
    var type: String
) : Serializable {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @TypeConverters(AttendanceTypeConverter::class)
    var attendances: MutableList<Attendance> = ArrayList()

    fun setupAttendance() {
        var dateStart = startedDate.toDate(DatePatterns.ddMMyyyy, Locale.getDefault())?.withoutTime()
        if (attendances.isNotEmpty()) {
            dateStart = attendances.last().date.withoutTime()
        }

        val calendarStartDate = Calendar.getInstance()
        calendarStartDate.time = dateStart ?: Date().withoutTime()

        val calendarToday = Calendar.getInstance()
        calendarToday.time = Date().withoutTime()

        while (calendarStartDate.before(calendarToday) || calendarStartDate == calendarToday) {
            var attend = attendances.firstOrNull {
                it.date == calendarStartDate.time
            }

            if (attend.isNull()) {
                attend = Attendance()
                attend.date = calendarStartDate.time
                attend.time = classTime
                attendances.add(attend)
                calendarStartDate.add(Calendar.DATE, 1)
            }
        }
    }

    val fullname: String
        get() = "$name $surname"

    @Ignore
    var classRoom: ClassRoom? = null

    val myAttendance: String
        get() {
            if (attendances.isNullOrEmpty()) return ""
            var maxAttend = 0f
            var currentAttend = 0f

            for (att in attendances) {
                if (!att.enable) continue

                maxAttend += att.presents.size
                att.presents.forEach {
                    currentAttend += it
                }
            }

            val percentual = (1 - (maxAttend - currentAttend) / maxAttend) * 100
            return if (percentual.isNaN()) String.format("%.2f", 100.0) else String.format("%.2f", percentual)
        }
}