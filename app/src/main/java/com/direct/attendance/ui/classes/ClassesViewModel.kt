package com.direct.attendance.ui.classes

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.direct.attendance.database.AppDatabase
import com.direct.attendance.database.State
import com.direct.attendance.model.ClassRoom
import com.direct.attendance.repository.ClassRoomRepository
import com.direct.attendance.repository.ClassRoomRepositoryImpl
import kotlinx.coroutines.launch

class ClassesViewModel(
    var application: Application
): ViewModel() {

    private val classRepository: ClassRoomRepository

    val allClasses: LiveData<List<ClassRoom>>
        get() = classRepository.getAllClassRoom()

    val updateState: LiveData<State<Long>>
        get() = classRepository.updateState

    init {
        val classDao = AppDatabase.database(application).classRoomDao()
        classRepository = ClassRoomRepositoryImpl(classDao)
    }

    fun update(classRoom: ClassRoom) = viewModelScope.launch {
        classRepository.update(classRoom)
    }

}