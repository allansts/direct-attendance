package com.direct.attendance.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.direct.attendance.constant.DatePatterns
import com.direct.attendance.constant.Gender
import com.direct.attendance.constant.UserType
import com.direct.attendance.database.AttendanceTypeConverter
import com.direct.attendance.extension.toDate
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Entity
class User {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var name: String = ""
    var surname: String = ""
    var gender: String = ""
    var type: String = UserType.STUDENT.name
    var classRoom: Int? = null
    @TypeConverters(AttendanceTypeConverter::class)
    var attendances: MutableList<Attendance> = ArrayList()
    var startDate: String = ""
        set(value) {
            field = value
            setupAttendance()
        }

    private fun setupAttendance() {
        val dateStart = startDate.toDate(DatePatterns.ddMMyyyy, Locale.getDefault())
        val calendarStartDate = Calendar.getInstance()
        calendarStartDate.time = dateStart ?: Date()

        val calendarToday = Calendar.getInstance()
        calendarToday.time = Date()

        while (calendarStartDate.before(calendarToday)) {
            val attend = Attendance()
            attend.date = calendarStartDate.time
            attendances.add(attend)
            calendarStartDate.add(Calendar.DATE, 1)
        }
    }

    @Ignore
    val classRoomObj: ClassRoom? = null

    val fullname: String
        get() = "$name $surname"

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
            return String.format("%.2f", percentual)
        }
}