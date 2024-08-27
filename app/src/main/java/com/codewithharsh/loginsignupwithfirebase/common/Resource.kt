package com.codewithharsh.loginsignupwithfirebase.common

sealed class Resource<out T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error<T>(val message: String) : Resource<T>()
    data object Loading : Resource<Nothing>()
}