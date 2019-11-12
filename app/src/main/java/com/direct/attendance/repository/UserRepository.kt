package com.direct.attendance.repository

import androidx.lifecycle.LiveData
import com.direct.attendance.constant.UserType
import com.direct.attendance.model.User

interface UserRepository {

    suspend fun insertOrUpdate(user: User): Long
    suspend fun delete(user: User)
    suspend fun deleteAllStudents(userType: String = UserType.STUDENT.name)

    fun retrieve(userId: Int) : LiveData<User>

    fun getAllStudents(): LiveData<List<User>>
}