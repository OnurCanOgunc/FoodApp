package com.decode.udemyfoodapp.util

sealed class NetworkResult<out T> {
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Error<T>(val message: String?) : NetworkResult<T>()
    data object Loading : NetworkResult<Nothing>()
}