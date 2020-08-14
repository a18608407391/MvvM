package com.zl.zlibrary.manager.Inf


interface RequestCallBack {

    fun <T> onSuccess(result: T)

    fun onError(it: Throwable)
}