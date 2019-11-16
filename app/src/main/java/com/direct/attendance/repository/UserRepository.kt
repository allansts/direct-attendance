package com.direct.attendance.repository

import androidx.lifecycle.LiveData
import com.direct.attendance.constant.UserType
import com.direct.attendance.database.State
import com.direct.attendance.model.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface UserRepository {

    val updateState: LiveData<State<Long>>
    val deleteState: LiveData<State<Int>>

    suspend fun insert(user: User): Long
    suspend fun update(user: User, dispatcher: CoroutineDispatcher = Dispatchers.IO)
    suspend fun delete(user: User, dispatcher: CoroutineDispatcher = Dispatchers.IO)
    suspend fun deleteAllStudents(userType: String = UserType.STUDENT.name)

    fun retrieve(userId: Int) : LiveData<User>
    fun getAllStudents(): LiveData<List<User>>
    fun usersByClass(classId: Int): LiveData<List<User>>
}