package com.zl.zlibrary.ext

import android.util.Log



/**
 * 通用日志
* */

const val TAG = "result"

var openLog = true

private enum class LogLevel {
    v,
    d,
    i,
    w,
    e
}


fun String.logv(tag: String = TAG) =
    log(LogLevel.v, tag, this)

fun String.logd(tag: String = TAG) =
    log(LogLevel.d, tag, this)

fun String.logi(tag: String = TAG) =
    log(LogLevel.i, tag, this)

fun String.logw(tag: String = TAG) =
    log(LogLevel.w, tag, this)

fun String.loge(tag: String = TAG) =
    log(LogLevel.e, tag, this)

private fun log(level: LogLevel, tag: String, message: String) {
    if (!openLog) return
    when (level) {
        LogLevel.v -> Log.v(tag, message)
        LogLevel.d -> Log.d(tag, message)
        LogLevel.i -> Log.i(tag, message)
        LogLevel.w -> Log.w(tag, message)
        LogLevel.e -> Log.e(tag, message)
    }
}