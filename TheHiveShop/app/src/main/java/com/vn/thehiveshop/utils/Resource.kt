package com.vn.thehiveshop.utils

sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error<out T>(val data: T?, val message: String) : Resource<T>()
    data class Loading<out T>(val data: T?) : Resource<T>()
}