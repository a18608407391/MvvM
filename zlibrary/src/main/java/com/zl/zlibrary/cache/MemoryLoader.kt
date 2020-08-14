package com.zl.zlibrary.cache


/**
 *       当我们需要加载一部分数据，但是这个数据并不需要长时间持有，只有要用的时候，才拿来用，这时可以选择暂时让gc回收此数据，节约内存，便于当下使用量大的数据内存充盈
 *       例如主界面下导航栏的多个fragment是长时间存在的，他们所请求的网络数据，除开vo，po数据可回收，但到需要使用时，再取出，刷新时再刷新
 *       目前来说，单Activity模式下，众多fragment并存，对部分数据进行内存管理，很有必要
 *
 * */
class MemoryLoader : ValueCallBack, MemoryCacheCallBack {
    override fun entryRemovedMemoryCache(key: String?, oldValue: Value?) {
        //从内存中移除的监听
    }

    override fun valueNonUseListener(key: String, value: Value) {
        if (key != null && value != null) {
            memoryCache!!.put(key, value)
        }
    }

    private val MEMORY_MAX_SIZE = 1024 * 1024 * 60
    private var activeCache: ActiveCache? = null // 活动缓存
    private var memoryCache: MemoryCache? = null // 内存缓存
    private var diskLruCache: DiskLruCacheImpl? = null // 磁盘缓存




    constructor() {
        if (activeCache == null) {
            this.activeCache = ActiveCache(this)
            if (memoryCache == null)
                memoryCache = MemoryCache(MEMORY_MAX_SIZE)
            memoryCache!!.memoryCacheCallback = this
            diskLruCache = DiskLruCacheImpl()
        }
    }

    private var path: String? = null
    private var key: String? = null


    fun returnAction(key: String): Value {
        var keys = Key(path).key
        this.key = keys
        var activeValue = activeCache!!.get(keys)
        if (activeValue != null) {
            activeValue.UseAction()
            return activeValue!!
        }

        var memoryValue = memoryCache!!.get(keys)
        if (memoryValue != null) {
            activeCache!!.put(key,memoryValue)
            memoryValue.UseAction()
            return memoryValue
        }
        var diskValue = diskLruCache!!.get(keys)
        if (diskValue != null) {
            activeCache!!.put(key,memoryValue)
            memoryCache!!.put(key,memoryValue)
            diskValue.UseAction()
            return diskValue
        }
        return null!!
    }


}