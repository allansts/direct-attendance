package com.direct.attendance.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.direct.attendance.database.State
import com.direct.attendance.database.dao.ClassRoomDao
import com.direct.attendance.model.ClassRoom
import com.direct.attendance.utils.ErrorUtils
import kotlinx.coroutines.CoroutineDispatcher

class ClassRoomRepositoryImpl(
    var classRoomDao: ClassRoomDao
): ClassRoomRepository {

    private val _updateState = MutableLiveData<State<Long>>()
    override val updateState: LiveData<State<Long>>
        get() = _updateState

    override suspend fun insert(classRoom: ClassRoom) {
        classRoomDao.insertOrUpdate(classRoom)
    }

    override suspend fun update(classRoom: ClassRoom, dispatcher: CoroutineDispatcher) {
        _updateState.value = State.Loading()
        _updateState.postValue(
            ErrorUtils.safeSuspendCall(dispatcher) { classRoomDao.insertOrUpdate(classRoom) }
        )
    }

    override suspend fun delete(classRoom: ClassRoom) {
        classRoomDao.delete(classRoom)
    }

    override suspend fun deleteAll() {
        classRoomDao.deleteAll()
    }

    override fun retrieve(classRoomId: Int): LiveData<ClassRoom> {
        return classRoomDao.retrieve(classRoomId)
    }

    override fun getAllClassRoom(): LiveData<List<ClassRoom>> {
        return classRoomDao.allClassRoom()
    }
}