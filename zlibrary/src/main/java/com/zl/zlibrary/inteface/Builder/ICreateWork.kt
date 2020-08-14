package com.zl.library.inteface.Builder

interface ICreateWork<out T> {
    fun create(): T
}