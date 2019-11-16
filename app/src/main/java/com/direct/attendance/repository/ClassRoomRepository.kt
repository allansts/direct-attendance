package com.direct.attendance.repository

import androidx.lifecycle.LiveData
import com.direct.attendance.database.State
import com.direct.attendance.model.ClassRoom
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface ClassRoomRepository {

    val updateState: LiveData<State<Long>>

    suspend fun insert(classRoom: ClassRoom)
    suspend fun update(classRoom: ClassRoom, dispatcher: CoroutineDispatcher = Dispatchers.IO)
    suspend fun delete(classRoom: ClassRoom)
    suspend fun deleteAll()

    fun retrieve(classRoomId: Int) : LiveData<ClassRoom>

    fun getAllClassRoom(): LiveData<List<ClassRoom>>
}