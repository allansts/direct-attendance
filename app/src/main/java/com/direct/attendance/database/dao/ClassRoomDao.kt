package com.direct.attendance.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.direct.attendance.model.ClassRoom

@Dao
interface ClassRoomDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(classRoom: ClassRoom) : Long

    @Delete
    suspend fun delete(classRoom: ClassRoom)

    @Query("DELETE FROM classroom")
    suspend fun deleteAll()

    @Query("SELECT * FROM classroom WHERE id == :classRoomId")
    fun retrieve(classRoomId: Int) : LiveData<ClassRoom>

    @Query("SELECT * FROM classroom ORDER BY name ASC")
    fun allClassRoom(): LiveData<List<ClassRoom>>
}