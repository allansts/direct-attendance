package com.direct.attendance.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.direct.attendance.constant.UserType
import com.direct.attendance.utils.ErrorUtils
import com.direct.attendance.database.State
import com.direct.attendance.database.dao.UserDao
import com.direct.attendance.model.User
import kotlinx.coroutines.CoroutineDispatcher

class UserRepositoryImpl(
    var userDao: UserDao
) : UserRepository {

    private val _updateState = MutableLiveData<State<Long>>()
    override val updateState: LiveData<State<Long>>
        get() = _updateState

    private val _deleteState = MutableLiveData<State<Int>>()
    override val deleteState: LiveData<State<Int>>
        get() = _deleteState

    override suspend fun insert(user: User): Long {
        user.setupAttendance()
        return userDao.insertOrUpdate(user)
    }

    override suspend fun update(user: User, dispatcher: CoroutineDispatcher) {
        _updateState.value = State.Loading()

        _updateState.postValue(
            ErrorUtils.safeSuspendCall(dispatcher) { userDao.insertOrUpdate(user) }
        )
    }

    override suspend fun delete(user: User, dispatcher: CoroutineDispatcher) {
        _deleteState.value = State.Loading()

        _deleteState.postValue(
            ErrorUtils.safeSuspendCall(dispatcher) { userDao.delete(user) }
        )
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

    override fun usersByClass(classId: Int): LiveData<List<User>> {
        return userDao.usersByClass(classId)
    }
}