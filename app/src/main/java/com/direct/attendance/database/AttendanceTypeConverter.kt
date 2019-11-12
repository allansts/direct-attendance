package com.direct.attendance.database

import androidx.room.TypeConverter
import com.direct.attendance.model.Attendance
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Collections.emptyList


class AttendanceTypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun stringToList(data: String?): List<Attendance> {
        if (data == null) {
            return emptyList()
        }

        val listType = object : TypeToken<List<Attendance>>() {

        }.type

        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun listToString(someObjects: List<Attendance>): String {
        return gson.toJson(someObjects)
    }
}