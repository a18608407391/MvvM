package com.zl.library.Config







var Base_URL :String= ""


//设置默认值
fun <T, R> T?.useOrDefault(default: R, usage: T.(R) -> R) = if (this == null) default else usage(default)



internal fun <T : Number> bound(min: T, value: T, max: T) = when {
    value.toDouble() > max.toDouble() -> max
    value.toDouble() < min.toDouble() -> min
    else -> value
}



