package com.zl.library.inteface.Builder

interface IBuilder<out T> {
    fun build(): T
}
