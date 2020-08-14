package com.zl.library.Utils.Glide

import android.content.Context
import android.view.View
import com.bumptech.glide.MemoryCategory
import com.zl.zlibrary.Utils.Glide.config.SingleConfig
import com.zl.zlibrary.Utils.Glide.utils.DownLoadImageService


interface ILoader {
    fun init(
        context: Context?,
        cacheSizeInM: Int,
        memoryCategory: MemoryCategory?,
        isInternalCD: Boolean
    )

    fun request(config: SingleConfig?)
    fun pause()
    fun resume()
    fun clearDiskCache()
    fun clearMomoryCache(view: View?)
    fun clearMomory()
    fun isCached(url: String?): Boolean
    fun trimMemory(level: Int)
    fun clearAllMemoryCaches()
    fun saveImageIntoGallery(downLoadImageService: DownLoadImageService?)
}