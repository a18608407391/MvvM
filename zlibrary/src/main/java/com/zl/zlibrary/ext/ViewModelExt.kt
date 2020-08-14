package com.zl.zlibrary.ext

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zl.zlibrary.base.BaseViewModel
import com.zl.zlibrary.manager.BaseResponse
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


fun <T> BaseViewModel.request(
    block: suspend () -> BaseResponse<T>,
    resultState: MutableLiveData<BaseViewModel.NotifyLiveData.PageEnum>,
    isShowDialog: Boolean = false,
    loadingMessage: String = "请求网络中..."
): Job {
    return viewModelScope.launch {
        runCatching {
//            if (isShowDialog) resultState.value = ResultState.onAppLoading(loadingMessage)
            //请求体
            block()
        }.onSuccess {
//            resultState.paresResult(it)
        }.onFailure {
            it.message?.loge("JetpackMvvm")
//            resultState.paresException(it)
        }
    }
}