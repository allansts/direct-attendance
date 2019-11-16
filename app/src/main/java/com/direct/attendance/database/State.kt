package com.direct.attendance.database

sealed class State<T> {
    class Loading<T>: State<T>()
    class Success<T>(var data: T): State<T>()
    class Error<T>(var message: String): State<T>()
}