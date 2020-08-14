package com.zl.zlibrary.cache

interface MemoryCacheCallBack  {

    fun entryRemovedMemoryCache(key: String?, oldValue: Value?)
}