package com.zl.zlibrary.manager.Inf


interface IDownLoadProgress {

    fun onProgress(url: String, progress: Int, cur: Long, total: Long)


    fun onSuccess(result: String)


    fun onComplete()


    fun onError(it: Throwable)


}