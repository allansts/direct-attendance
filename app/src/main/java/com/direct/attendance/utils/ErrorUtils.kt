package com.direct.attendance.utils

import com.direct.attendance.database.State
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ErrorUtils {

    companion object {
        suspend fun <T> safeSuspendCall(dispatcher: CoroutineDispatcher, apiCall: suspend () -> T): State<T> {
            return withContext(dispatcher) {
                try {
                    State.Success(apiCall.invoke())
                } catch (throwable: Throwable) {
                    State.Error<T>("This action could not be performed. Please try again.")
                }
            }
        }
    }

}