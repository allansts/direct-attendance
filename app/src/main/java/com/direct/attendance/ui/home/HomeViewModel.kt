package com.direct.attendance.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.direct.attendance.database.AppDatabase
import com.direct.attendance.model.User
import com.direct.attendance.repository.UserRepository
import com.direct.attendance.repository.UserRepositoryImpl
import kotlinx.coroutines.launch

class HomeViewModel(application: Application): ViewModel() {

    private val repository: UserRepository

    val allStudents: LiveData<List<User>>
        get() = repository.getAllStudents()

    init {
        val userDao = AppDatabase.database(application).userDao()
        repository = UserRepositoryImpl(userDao)
    }

    fun insertOrUpdateUser(user: User) = viewModelScope.launch {
        Log.d("User", "saved ID: ${repository.insertOrUpdate(user)}")
    }

    fun delete(user: User) = viewModelScope.launch {
        repository.delete(user)
    }

}