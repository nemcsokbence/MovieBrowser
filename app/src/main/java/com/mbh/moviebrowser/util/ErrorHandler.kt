package com.mbh.moviebrowser.util

import android.util.Log
import com.mbh.moviebrowser.R
import com.mbh.moviebrowser.repository.TAG
import java.net.UnknownHostException

const val ERROR_401 = 401
const val ERROR_500 = 500

object ErrorHandler {

    fun Throwable.getMovieBrowserError(): Resource.Error {
        return when (this) {
            is UnknownHostException -> {
                Log.e(TAG, this.toString())
                Resource.Error.UnknownHostError
            }

            else-> {
                Log.e(TAG, this.toString())
                Resource.Error.UnknownError
            }
        }
    }

    fun Resource.Error.getErrorTitleRes(): Int {
        return when (this) {
            is Resource.Error.NetworkError -> {
                R.string.network_error_title
            }

            is Resource.Error.UnknownError -> {
                R.string.unknown_error_title
            }

            is Resource.Error.UnknownHostError -> {
                R.string.network_error_title
            }
        }
    }

    fun Resource.Error.getErrorMessageResource(): Int {
        return when (this) {
            is Resource.Error.NetworkError -> {
                getNetworkErrorMessageRes(this.errorCode)
            }

            is Resource.Error.UnknownError -> {
                R.string.unknown_error_message
            }

            is Resource.Error.UnknownHostError -> {
                return R.string.unknown_host_error_message
            }
        }
    }

    private fun getNetworkErrorMessageRes(errorCode: Int?): Int {
        return when (errorCode) {
            ERROR_401 -> R.string.unauthenticated_error_message
            ERROR_500 -> R.string.internal_server_error_message
            else -> R.string.unknown_network_error_message
        }
    }
}
