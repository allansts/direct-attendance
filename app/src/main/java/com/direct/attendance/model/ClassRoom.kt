package com.direct.attendance.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.direct.attendance.database.IntTypeConverter

@Entity
class ClassRoom {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var name: String = ""
    @TypeConverters(IntTypeConverter::class)
    var students: MutableList<Int> = ArrayList()

    override fun toString(): String {
        return "Class: $name"
    }
}