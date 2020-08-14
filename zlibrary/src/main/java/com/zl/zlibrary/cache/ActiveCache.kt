package com.zl.zlibrary.cache

import android.graphics.Bitmap
import com.bumptech.glide.Glide
import java.lang.ref.ReferenceQueue
import java.lang.ref.WeakReference



/**
 *
 * 活动内存，用于存储可供全局统一管理的数据
 *
 * */

class ActiveCache {

    //容器
    var container = HashMap<String, WeakReference<Value>>()

    var callBack: ValueCallBack? = null

    var thread: Thread? = null

    var isClose = false

    var isShutdown = false

    private var queue: ReferenceQueue<Value>? = null // 目的：为了监听这个弱引用 是否被回收了

    constructor(callBack: ValueCallBack) {
        this.callBack = callBack
    }


    fun put(key: String, value: Value) {
        if (!key.isNullOrEmpty()) {
            value.callBack = callBack
            container.put(key, CustomWeakReference(value, getQueue(), key))
        }
    }


    fun get(key:String): Value? {
       var value =  container[key]
        if(value!=null){
            return value.get()
        }
        return null
    }


    fun remove(key:String): Value? {
          isShutdown = true
       var reference =    container.remove(key)
        isShutdown = false
        if(reference!=null){
            return reference.get()
        }
        return null
    }


    class CustomWeakReference : WeakReference<Value> {
        var key: String? = null
        constructor(reference: Value, queue: ReferenceQueue<Value>, key: String) : super(
            reference,
            queue
        ) {
            this.key = key
        }
    }

    fun closeThread(){
        //关闭线程，主动启动gc清理
        isClose = true
        container.clear()
        System.gc()
    }

    fun getQueue(): ReferenceQueue<Value> {
        if (queue == null) {
            queue = ReferenceQueue()
        }
        thread = Thread {
            while (!isClose) {
                try {
                    if (!isShutdown) {
                        var remove = queue!!.remove() as CustomWeakReference
                        if (!container.isNullOrEmpty()) {
                            container.remove(remove.key)
                        }
                    }

                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
        thread!!.start()
        return queue!!
    }
}
