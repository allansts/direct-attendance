package com.direct.attendance.repository

import androidx.lifecycle.LiveData
import com.direct.attendance.database.dao.ClassRoomDao
import com.direct.attendance.model.ClassRoom

class ClassRoomRepositoryImpl(
    var classRoomDao: ClassRoomDao
): ClassRoomRepository {

    override suspend fun insertOrUpdate(classRoom: ClassRoom) {
        classRoomDao.insertOrUpdate(classRoom)
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