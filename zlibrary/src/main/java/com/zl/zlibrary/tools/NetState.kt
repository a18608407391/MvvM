package com.zl.zlibrary.tools

import com.zl.zlibrary.Utils.NetException

sealed class NetState<out T> {
    companion object {
        fun <T> onRequestSuccess(data: T): NetState<T> = Success(data)
        fun <T> onRequestLoading(loadingMessage: String): NetState<T> = Loading(loadingMessage)
        fun <T> onRequestError(error: NetException): NetState<T> = Error(error)
    }

    data class Loading(val loadingMessage: String) : NetState<Nothing>()
    data class Success<out T>(val data: T) : NetState<T>()
    data class Error(val error: NetException) : NetState<Nothing>()

}