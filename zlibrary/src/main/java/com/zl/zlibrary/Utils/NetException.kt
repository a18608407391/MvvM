package com.zl.zlibrary.Utils



class NetException  :Exception{
    var msg: String //错误消息
    var code: Int = 0 //错误码
    var errorLog: String? //错误日志

    constructor(errCode: Int, error: String?, errorLog: String? = "") : super(error) {
        this.msg = error ?: "请求错误"
        this.code = errCode
        this.errorLog = errorLog?:this.msg
    }

}