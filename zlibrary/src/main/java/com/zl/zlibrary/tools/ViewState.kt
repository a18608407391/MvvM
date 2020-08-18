package com.zl.zlibrary.tools

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData


class ViewState {
    companion object {
        const val SHOW_CONTENT = 0
        const val SHOW_LOADING = 1
        const val SHOW_ERROR = 2
        const val SHOW_EMPTY = 3
    }
    var showLayoutType = MutableLiveData(SHOW_LOADING)
}