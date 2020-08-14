package com.zl.zlibrary.cache

import android.util.LruCache
import com.zl.zlibrary.Utils.memory.RamUsageEstimator
import com.zl.zlibrary.ext.loge


class MemoryCache : LruCache<String, Value> {


    constructor(size: Int) : super(size)


    public var memoryCacheCallback :MemoryCacheCallBack ? = null

    private var shutdown: Boolean = false

    fun shutdownMove(key:String): Value? {
        shutdown = true
       var value =  remove(key)
        shutdown = false
        return value
    }

    override fun sizeOf(key: String?, value: Value?): Int {
        //计算对象内存占用情况
       var size =  RamUsageEstimator.shallowSizeOf(value)
        "当前对象内存占用$size".loge(this.javaClass.simpleName)
        return size.toInt()
    }

    override fun entryRemoved(evicted: Boolean, key: String?, oldValue: Value?, newValue: Value?) {
        super.entryRemoved(evicted, key, oldValue, newValue);
        if (memoryCacheCallback != null && !shutdown) { // !shoudonRemove == 被动的
            memoryCacheCallback!!.entryRemovedMemoryCache(key, oldValue)
        }
    }



}