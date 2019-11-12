package com.direct.attendance.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.direct.attendance.constant.UserType
import com.direct.attendance.database.dao.UserDao
import com.direct.attendance.model.User

class UserRepositoryImpl(
    var userDao: UserDao
) : UserRepository {


    override suspend fun insertOrUpdate(user: User): Long {
        return userDao.insertOrUpdate(user)
    }

    override suspend fun delete(user: User) {
        userDao.delete(user)
    }

    override suspend fun deleteAllStudents(userType: String) {
        userDao.deleteAllStudents(userType)
    }

    override fun retrieve(userId: Int): LiveData<User> {
        return userDao.retrieve(userId)
    }

    override fun getAllStudents(): LiveData<List<User>> {
        return userDao.allUser(UserType.STUDENT.name)
    }
}