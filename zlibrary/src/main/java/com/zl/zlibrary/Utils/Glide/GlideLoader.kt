package com.zl.library.Utils.Glide

import android.content.Context
import android.graphics.Bitmap
import android.graphics.PointF
import android.graphics.drawable.Drawable
import android.net.Uri
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.*
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.zl.zlibrary.Utils.Glide.GlideModule
import com.zl.zlibrary.Utils.Glide.config.*
import com.zl.zlibrary.Utils.Glide.utils.DownLoadImageService
import com.zl.zlibrary.Utils.Glide.utils.ImageUtil
import jp.wasabeef.glide.transformations.*
import jp.wasabeef.glide.transformations.gpu.*


class GlideLoader : ILoader {
    /**
     * @param context        上下文
     * @param cacheSizeInM   Glide默认磁盘缓存最大容量250MB
     * @param memoryCategory 调整内存缓存的大小 LOW(0.5f) ／ NORMAL(1f) ／ HIGH(1.5f);
     * @param isInternalCD   true 磁盘缓存到应用的内部目录 / false 磁盘缓存到外部存
     */
    override fun init(
        context: Context?,
        cacheSizeInM: Int,
        memoryCategory: MemoryCategory?,
        isInternalCD: Boolean
    ) {

        Glide.get(context!!).setMemoryCategory(memoryCategory!!) //如果在应用当中想要调整内存缓存的大小，开发者可以通过如下方式：
        val builder = GlideBuilder()
        if (isInternalCD) {
            builder.setDiskCache(InternalCacheDiskCacheFactory(context, cacheSizeInM * 1024 * 1024*1L))
        } else {
            builder.setDiskCache(ExternalCacheDiskCacheFactory(context, cacheSizeInM * 1024 * 1024))
        }
    }

    override fun request(config: SingleConfig?) {
        val requestManager = Glide.with(config!!.context)
        val request = getDrawableTypeRequest(config, requestManager)
        if (config.isAsBitmap) {
            var target = object : SimpleTarget<Bitmap?>(config.width, config.height) {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap?>?
                ) {
                    config.bitmapListener.onSuccess(resource)
                }

//                override fun onResourceReady(
//                    resource: Bitmap?,
//                    glideAnimation: GlideAnimation<in Bitmap?>?
//                ) {
//                    config.bitmapListener.onSuccess(resource)
//                }

            }
            setShapeModeAndBlur(config, request)
            if (config.diskCacheStrategy != null) {
                request!!.diskCacheStrategy(config.diskCacheStrategy)
            }
            requestManager.asBitmap().into(target)
        } else {
            if (request == null) {
                return
            }
            if (ImageUtil.shouldSetPlaceHolder(config)) {
                request.placeholder(config.placeHolderResId)
            }
            val scaleMode = config.scaleMode
            when (scaleMode) {
                ScaleMode.CENTER_CROP -> request.centerCrop()
                ScaleMode.FIT_CENTER -> request.fitCenter()
                else -> request.fitCenter()
            }
            setShapeModeAndBlur(config, request)
            //设置缩略图
            if (config.thumbnail != 0f) {
                request.thumbnail(config.thumbnail)
            }
            //设置图片加载的分辨 sp
            if (config.getoWidth() != 0 && config.getoHeight() != 0) {
                request.override(config.getoWidth(), config.getoHeight())
            }
            //是否跳过磁盘存储
            if (config.diskCacheStrategy != null) {
                request.diskCacheStrategy(config.diskCacheStrategy)
            }
            //设置图片加载动画
            setAnimator(config, request)
            //设置图片加载优先级
            setPriority(config, request)
            if (config.errorResId > 0) {
                request.error(config.errorResId)
            }
            if (config.isGif) {
                requestManager.asGif()
            }
            if (config.target is ImageView) {
                request.into(config.target as ImageView)
            }
        }
    }

    /**
     * 设置加载优先级
     *
     * @param config
     * @param request
     */
    private fun setPriority(config: SingleConfig?, request: RequestBuilder<Drawable>) {
        when (config!!.priority) {
            PriorityMode.PRIORITY_LOW -> request.priority(Priority.LOW)
            PriorityMode.PRIORITY_NORMAL -> request.priority(Priority.NORMAL)
            PriorityMode.PRIORITY_HIGH -> request.priority(Priority.HIGH)
            PriorityMode.PRIORITY_IMMEDIATE -> request.priority(Priority.IMMEDIATE)
            else -> request.priority(Priority.IMMEDIATE)
        }
    }

    /**
     * 设置加载进入动画
     *
     * @param config
     * @param request
     */
    private fun setAnimator(config: SingleConfig?, request: RequestBuilder<Drawable>) {
        if (config!!.animationType == AnimationMode.ANIMATIONID) {
request.transition(GenericTransitionOptions.with(config.animationId))
        } else if (config.animationType == AnimationMode.ANIMATOR) {
            request.transition(GenericTransitionOptions.with(config.animator))
        } else if (config.animationType == AnimationMode.ANIMATION) {
            request.transition(GenericTransitionOptions.with(config.animation))
        }
    }

    private fun getDrawableTypeRequest(
        config: SingleConfig?,
        requestManager: RequestManager
    ): RequestBuilder<Drawable>? {
        var request: RequestBuilder<Drawable>? = null
        if (!TextUtils.isEmpty(config!!.url)) {
            request =
                requestManager.load(ImageUtil.appendUrl(config.url))
        } else if (!TextUtils.isEmpty(config.filePath)) {
            request =
                requestManager.load(ImageUtil.appendUrl(config.filePath))
        } else if (!TextUtils.isEmpty(config.contentProvider)) {
            request =
                requestManager.load(Uri.parse(config.contentProvider))
        } else if (config.resId > 0) {
            request = requestManager.load(config.resId)
        } else if (config.file != null) {
            request = requestManager.load(config.file)
        } else if (!TextUtils.isEmpty(config.assertspath)) {
            request = requestManager.load(config.assertspath)
        } else if (!TextUtils.isEmpty(config.rawPath)) {
            request = requestManager.load(config.rawPath)
        }
        return request
    }

    /**
     * 设置图片滤镜和形状
     *
     * @param config
     * @param request
     */
    private fun setShapeModeAndBlur(
        config: SingleConfig?,
        request: RequestBuilder<Drawable>?
    ) {
        var count = 0
        var transformation: Array<Transformation<Bitmap>?> =
            arrayOfNulls(statisticsCount(config))
        if (config!!.isNeedBlur) {
            transformation[count] = BlurTransformation(config.context, config.blurRadius)
            count++
        }
        if (config.isNeedBrightness) {
            transformation[count] =
                BrightnessFilterTransformation(config.context, config.brightnessLeve) //亮度
            count++
        }
        if (config.isNeedGrayscale) {
            transformation[count] = GrayscaleTransformation(config.context) //黑白效果
            count++
        }
        if (config.isNeedFilteColor) {
            transformation[count] =
                ColorFilterTransformation(config.context, config.filteColor)
            count++
        }
        if (config.isNeedSwirl) {
            transformation[count] =
                SwirlFilterTransformation(config.context, 0.5f, 1.0f, PointF(0.5f, 0.5f)) //漩涡
            count++
        }
        if (config.isNeedToon) {
            transformation[count] = ToonFilterTransformation(config.context) //油画
            count++
        }
        if (config.isNeedSepia) {
            transformation[count] = SepiaFilterTransformation(config.context) //墨画
            count++
        }
        if (config.isNeedContrast) {
            transformation[count] =
                ContrastFilterTransformation(config.context, config.getContrastLevel()) //锐化
            count++
        }
        if (config.isNeedInvert) {
            transformation[count] = InvertFilterTransformation(config.context) //胶片
            count++
        }
        if (config.isNeedPixelation) {
            transformation[count] = PixelationFilterTransformation(
                config.context,
                config.pixelationLevel
            ) //马赛克
            count++
        }
        if (config.isNeedSketch) {
            transformation[count] = SketchFilterTransformation(config.context) //素描
            count++
        }
        if (config.isNeedVignette) {
            transformation[count] = VignetteFilterTransformation(
                config.context,
                PointF(0.5f, 0.5f),
                floatArrayOf(0.0f, 0.0f, 0.0f),
                0f,
                0.75f
            ) //晕映
            count++
        }
        when (config.shapeMode) {
            ShapeMode.RECT -> {
            }
            ShapeMode.RECT_ROUND -> {
                transformation[count] = RoundedCornersTransformation(
                    config.context,
                    config.rectRoundRadius,
                    0,
                    RoundedCornersTransformation.CornerType.ALL
                )
                count++
            }
            ShapeMode.OVAL -> {
                transformation[count] = CropCircleTransformation(config.context)
                count++
            }
            ShapeMode.SQUARE -> {
                transformation[count] = CropSquareTransformation(config.context)
                count++
            }
        }
        if (transformation.size != 0) {
            request!!.transform(*transformation)
        }
    }

    private fun statisticsCount(config: SingleConfig?): Int {
        var count = 0
        if (config!!.shapeMode == ShapeMode.OVAL || config.shapeMode == ShapeMode.RECT_ROUND || config.shapeMode == ShapeMode.SQUARE) {
            count++
        }
        if (config.isNeedBlur) {
            count++
        }
        if (config.isNeedFilteColor) {
            count++
        }
        if (config.isNeedBrightness) {
            count++
        }
        if (config.isNeedGrayscale) {
            count++
        }
        if (config.isNeedSwirl) {
            count++
        }
        if (config.isNeedToon) {
            count++
        }
        if (config.isNeedSepia) {
            count++
        }
        if (config.isNeedContrast) {
            count++
        }
        if (config.isNeedInvert) {
            count++
        }
        if (config.isNeedPixelation) {
            count++
        }
        if (config.isNeedSketch) {
            count++
        }
        if (config.isNeedVignette) {
            count++
        }
        return count
    }

    override fun pause() {
        Glide.with(GlobalConfig.context).pauseRequestsRecursive()
    }

    override fun resume() {
        Glide.with(GlobalConfig.context).resumeRequestsRecursive()
    }

    override fun clearDiskCache() {
        Glide.get(GlobalConfig.context).clearDiskCache()
    }

    override fun clearMomoryCache(view: View?) {
//        Glide.clear(view)
        Glide.with(view!!).clear(view)
    }

    override fun clearMomory() {
        Glide.get(GlobalConfig.context).clearMemory()
    }

    override fun isCached(url: String?): Boolean {
        return false
    }

    override fun trimMemory(level: Int) {
        Glide.with(GlobalConfig.context).onTrimMemory(level)
    }

    override fun clearAllMemoryCaches() {
        Glide.with(GlobalConfig.context).onLowMemory()
    }

    override fun saveImageIntoGallery(downLoadImageService: DownLoadImageService?) {
        Thread(downLoadImageService).start()
    }
}