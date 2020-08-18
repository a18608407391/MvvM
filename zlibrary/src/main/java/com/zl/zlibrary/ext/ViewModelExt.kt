package com.zl.zlibrary.ext

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zl.library.Base.BaseFragment
import com.zl.zlibrary.Utils.NetException
import com.zl.zlibrary.base.BaseViewModel
import com.zl.zlibrary.manager.BaseResponse
import com.zl.zlibrary.tools.NetState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


fun <T> BaseViewModel.request(
    block: suspend () -> BaseResponse<T>,
    state: MutableLiveData<NetState<T>>,
    isShowDialog: Boolean = false,
    loadingMessage: String = "请求网络中..."
): Job {
    return viewModelScope.launch {
        runCatching {
            if (isShowDialog) state.value = NetState.onRequestLoading(loadingMessage)
            //请求体
            block()
        }.onSuccess {
            state.paresResult(it)
        }.onFailure {
            state.paresException(it)
        }
    }
}


fun <T> BaseFragment<*,*>.parseState(

    resultState: NetState<T>,

    onSuccess: (T) -> Unit,

    onError: ((NetException) -> Unit)? = null,

    onLoading: (() -> Unit)? = null

) {

    when (resultState) {

        is NetState.Loading -> {

            _mActivity!!.showProgressDialog(resultState.loadingMessage)

            onLoading?.invoke()

        }

        is NetState.Success -> {

            _mActivity!!.dismissProgressDialog()

            onSuccess(resultState.data)

        }

        is NetState.Error -> {

            _mActivity!!.dismissProgressDialog()

            onError?.run {
                this(resultState.error)
            }
        }

    }

}