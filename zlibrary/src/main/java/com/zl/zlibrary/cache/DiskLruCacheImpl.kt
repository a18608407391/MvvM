package com.zl.zlibrary.cache

import android.os.Environment
import android.graphics.Bitmap
import android.R.attr.key
import android.R.attr.key
import android.graphics.BitmapFactory
import java.io.*


class DiskLruCacheImpl {
    private val DISKLRU_CACHE_DIR = "disk_lru_cache_dir" // 磁盘缓存的的目录

    private val APP_VERSION = 1 // 我们的版本号，一旦修改这个版本号，之前的缓存失效
    private val VALUE_COUNT = 1 // 通常情况下都是1
    private val MAX_SIZE = (1024 * 1024 * 10).toLong() // 以后修改成 使用者可以设置的

    private var diskLruCache: DiskLruCache? = null

    constructor() {
        var file =
            File(Environment.getExternalStorageDirectory().path + File.separator + DISKLRU_CACHE_DIR)
        try {
            diskLruCache = DiskLruCache.open(file, APP_VERSION, VALUE_COUNT, MAX_SIZE);
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    fun put(key: String, value: Value) {
        if (key.isNullOrEmpty()) {
            return
        }

        var editor: DiskLruCache.Editor? = null
        var outputStream: OutputStream? = null
        try {
            editor = diskLruCache!!.edit(key)
            outputStream = editor!!.newOutputStream(0)// index 不能大于 VALUE_COUNT
            var out = ObjectOutputStream(outputStream)
            out.writeObject(value.obj)
            outputStream!!.flush()
        } catch (e: IOException) {
            e.printStackTrace()
            // 失败
            try {
                editor!!.abort()
            } catch (e1: IOException) {
                e1.printStackTrace()
            }

        } finally {
            try {
                editor!!.commit() // sp 记得一定要提交
                diskLruCache!!.flush()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            if (outputStream != null) {
                try {
                    outputStream!!.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }


    fun get(key: String):Value {
        if (key.isNullOrEmpty()) {
            return null!!
        }
        var inputStream: InputStream? = null
        try {
            val snapshot = diskLruCache!!.get(key)
            // 判断快照不为null的情况下，在去读取操作
            if (null != snapshot) {
                var value = Value()
                inputStream = snapshot.getInputStream(0)// index 不能大于 VALUE_COUNT
                var ob =  ObjectInputStream(inputStream)
                var bitmap =ob.readObject()
                value.obj  = bitmap!!
                // 保存key 唯一标识
                value.key = key
                return value!!
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (null != inputStream) {
                try {
                    inputStream!!.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
        return null!! // 为了后续好判断
    }


}