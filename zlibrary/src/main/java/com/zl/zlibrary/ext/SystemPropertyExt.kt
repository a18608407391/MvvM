package com.zl.zlibrary.ext

import java.lang.Exception


val SYSTEM_PROPERTY_PATH = "android.os.SystemProperties"


fun getSystemProperty(key: String, def: String): String {
    var value = def
    try {
        var clazz = Class.forName(SYSTEM_PROPERTY_PATH)
        var get = clazz.getMethod("get", String::class.java, String::class.java)
        value = get.invoke(clazz, key, "") as String
    } catch (e: Exception) {
        "getSystemProperty".loge()
    }
    return value
}