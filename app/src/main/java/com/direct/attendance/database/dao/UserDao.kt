package com.direct.attendance.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.direct.attendance.model.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(user: User) : Long

    @Delete
    suspend fun delete(user: User): Int

    @Query("DELETE FROM user WHERE type == :userType")
    suspend fun deleteAllStudents(userType: String)

    @Query("SELECT * FROM user WHERE id == :userId")
    fun retrieve(userId: Int) : LiveData<User>

    @Query("SELECT * FROM user WHERE type == :userType ORDER BY name ASC")
    fun allUser(userType: String): LiveData<List<User>>

    @Query("SELECT * FROM user WHERE classRoomId == :classId ORDER BY name ASC")
    fun usersByClass(classId: Int): LiveData<List<User>>
}