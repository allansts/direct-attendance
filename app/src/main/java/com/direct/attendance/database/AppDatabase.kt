package com.direct.attendance.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.direct.attendance.database.dao.ClassRoomDao
import com.direct.attendance.database.dao.UserDao
import com.direct.attendance.model.ClassRoom
import com.direct.attendance.model.User

@Database(
    entities = [User::class, ClassRoom::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    AttendanceTypeConverter::class,
    IntTypeConverter::class
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun classRoomDao(): ClassRoomDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        @Synchronized
        fun database(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "AppDatabase")
                    .build()
//                    .fallbackToDestructiveMigration()
            }
            return INSTANCE!!
        }
    }
}