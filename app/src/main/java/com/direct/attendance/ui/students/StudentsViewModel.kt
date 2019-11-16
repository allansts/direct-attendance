package com.direct.attendance.ui.students

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.direct.attendance.database.AppDatabase
import com.direct.attendance.database.State
import com.direct.attendance.model.ClassRoom
import com.direct.attendance.model.User
import com.direct.attendance.repository.ClassRoomRepository
import com.direct.attendance.repository.ClassRoomRepositoryImpl
import com.direct.attendance.repository.UserRepository
import com.direct.attendance.repository.UserRepositoryImpl
import kotlinx.coroutines.launch

class StudentsViewModel(application: Application): ViewModel() {

    private val userRepository: UserRepository
    private val classRepository: ClassRoomRepository

    val updateState: LiveData<State<Long>>
        get() = userRepository.updateState

    val deleteState: LiveData<State<Int>>
        get() = userRepository.deleteState

    val allClasses: LiveData<List<ClassRoom>>
        get() = classRepository.getAllClassRoom()

    val allStudents: LiveData<List<User>>
        get() = userRepository.getAllStudents()

    init {
        val userDao = AppDatabase.database(application).userDao()
        userRepository = UserRepositoryImpl(userDao)
        val classDao = AppDatabase.database(application).classRoomDao()
        classRepository = ClassRoomRepositoryImpl(classDao)
    }

    fun update(user: User) = viewModelScope.launch {
        userRepository.update(user)
    }

    fun delete(user: User) = viewModelScope.launch {
        userRepository.delete(user)
    }

    fun usersByClass(classId: Int): LiveData<List<User>> {
        return userRepository.usersByClass(classId)
    }

}