package com.direct.attendance.ui.launch

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.direct.attendance.database.AppDatabase
import com.direct.attendance.model.User
import com.direct.attendance.repository.UserRepository
import com.direct.attendance.repository.UserRepositoryImpl
import kotlinx.coroutines.launch

class LaunchViewModel(
    application: Application
): ViewModel() {

    private val userRepository: UserRepository

    val allStudents: LiveData<List<User>>
        get() = userRepository.getAllStudents()

    init {
        val userDao = AppDatabase.database(application).userDao()
        userRepository = UserRepositoryImpl(userDao)
    }

    fun update(user: User) = viewModelScope.launch {
        userRepository.update(user)
    }
}