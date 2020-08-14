package com.zl.zlibrary.Utils.Glide

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator


@GlideModule
class GlideModule : AppGlideModule(){

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {

    }
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        super.applyOptions(context, builder)

        var calculator =MemorySizeCalculator.Builder(context).build()
        var defaultMemoryCacheSize = calculator.memoryCacheSize
        var defaultBitmapPoolSize = calculator.bitmapPoolSize
        var customMemoryCacheSize = (1.2 * defaultMemoryCacheSize).toInt()
        var customBitmapPoolSize = (1.2 * defaultBitmapPoolSize).toInt()
        builder.setMemoryCache(LruResourceCache(customMemoryCacheSize.toLong()))
        builder.setBitmapPool(LruBitmapPool(customBitmapPoolSize.toLong()))
        builder.setDiskCache(
            DiskLruCacheFactory(
                context.externalCacheDir!!.absolutePath,
                240 * 1024 * 1024
            )
        )
    }
    override fun isManifestParsingEnabled(): Boolean {
        return false
    }
}