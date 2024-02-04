package com.mbh.moviebrowser.util

sealed class Resource<out T>(val data: T? = null) {
    class Success<T>(data: T?) : Resource<T>(data)

    object Loading: Resource<Nothing>()
    sealed class Error : Resource<Nothing>(data = null) {
        data class NetworkError(val errorCode: Int) : Error()
        object UnknownHostError : Error()
        object UnknownError : Error()


    }
}
