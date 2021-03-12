package com.twoplaytech.drbetting.data

sealed class Result<T : Any> {
    data class Success<T : Any>(val data: T) : Result<T>()
    data class Loading(val message: String) : Result<Nothing>()
    data class Error(val errorMessage: String) : Result<Nothing>()
}
