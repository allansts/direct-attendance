package com.direct.attendance.repository

import androidx.lifecycle.LiveData
import com.direct.attendance.model.ClassRoom

interface ClassRoomRepository {

    suspend fun insertOrUpdate(classRoom: ClassRoom)
    suspend fun delete(classRoom: ClassRoom)
    suspend fun deleteAll()

    fun retrieve(classRoomId: Int) : LiveData<ClassRoom>

    fun getAllClassRoom(): LiveData<List<ClassRoom>>
}