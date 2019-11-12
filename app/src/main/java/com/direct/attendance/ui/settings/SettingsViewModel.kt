package com.direct.attendance.ui.settings

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.direct.attendance.database.AppDatabase
import com.direct.attendance.model.ClassRoom
import com.direct.attendance.model.User
import com.direct.attendance.repository.ClassRoomRepository
import com.direct.attendance.repository.ClassRoomRepositoryImpl
import com.direct.attendance.repository.UserRepository
import com.direct.attendance.repository.UserRepositoryImpl
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : ViewModel() {

    private val userRepository: UserRepository
    private val classRoomRepository: ClassRoomRepository

    init {
        val userDao = AppDatabase.database(application).userDao()
        val classRoomDao = AppDatabase.database(application).classRoomDao()
        userRepository = UserRepositoryImpl(userDao)
        classRoomRepository = ClassRoomRepositoryImpl(classRoomDao)
    }

    val allClasses: LiveData<List<ClassRoom>>
        get() = classRoomRepository.getAllClassRoom()


    fun insertOrUpdateUser(user: User) = viewModelScope.launch {
        userRepository.insertOrUpdate(user)
    }

    fun deleteAllStudents() = viewModelScope.launch {
        userRepository.deleteAllStudents()
    }

    fun insertOrUpdateClass(classRoom: ClassRoom) = viewModelScope.launch {
        classRoomRepository.insertOrUpdate(classRoom)
    }

}