package com.zl.zlibrary.manager

abstract open class BaseResponse<T> {
    var code = 0
    var msg: String? = null
    var data: T? = null

    abstract fun isSuccess():Boolean


}