package com.example.trackmytrack.utils

sealed class ActualResult<T>(val data: T? = null, val message: String? = null)
{
    class Success<T>(data: T?) : ActualResult<T>(data)

    class Error<T>(message: String, data: T? = null) : ActualResult<T>(data, message)

    class Loading<T> : ActualResult<T>()

}

