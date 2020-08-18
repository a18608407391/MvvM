package com.zl.zlibrary.ext

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.zl.zlibrary.Utils.ExceptionHandle
import com.zl.zlibrary.Utils.NetException
import com.zl.zlibrary.manager.BaseResponse
import com.zl.zlibrary.tools.NetState

fun <T> LifecycleOwner.observe(liveData: LiveData<T>, observer: Observer<T>) {
    liveData.observe(this, observer)
}

fun <T> MutableLiveData<NetState<T>>.paresResult(result: BaseResponse<T>) {
    value = if (result.isSuccess()) NetState.onRequestSuccess(result.data!!) else
        NetState.onRequestError(NetException(result.code, result.msg))
}




fun <T> MutableLiveData<NetState<T>>.paresException(e: Throwable) {
    ///
    this.value = NetState.onRequestError(ExceptionHandle.handleException(e))
}
