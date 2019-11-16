package com.direct.attendance.ui.attendance

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.direct.attendance.database.AppDatabase
import com.direct.attendance.database.State
import com.direct.attendance.model.User
import com.direct.attendance.repository.UserRepository
import com.direct.attendance.repository.UserRepositoryImpl
import kotlinx.coroutines.launch

class AttendanceViewModel(
    var application: Application
): ViewModel() {

    private val userRepository: UserRepository

    val updateState: LiveData<State<Long>>
        get() = userRepository.updateState

    init {
        val userDao = AppDatabase.database(application).userDao()
        userRepository = UserRepositoryImpl(userDao)
    }

    fun updateUser(user: User) = viewModelScope.launch {
        userRepository.update(user)
    }


}