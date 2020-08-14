package com.zl.zlibrary.cache


interface ValueCallBack {
    fun valueNonUseListener(key: String, value: Value)
}