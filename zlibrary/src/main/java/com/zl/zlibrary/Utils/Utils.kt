package com.zl.zlibrary.Utils

import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.WindowManager
import androidx.annotation.RequiresApi
import com.zl.zlibrary.base.BaseApplication
import kotlinx.coroutines.*



var context = BaseApplication.getInstance().applicationContext
var windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
var MainApplication = BaseApplication.getInstance()
var job = Job()
var uiContext: MainCoroutineDispatcher = Dispatchers.Main
var ioContext: CoroutineDispatcher = Dispatchers.IO

fun <T, R> T?.useOrDefault(default: R, usage: T.(R) -> R) = if (this == null) default else usage(default)


val Float.dp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this, context.resources.displayMetrics
    )

val Float.sp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP, this, context.resources.displayMetrics
    )

val Float.px
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_PX, this, context.resources.displayMetrics
    )

fun getString(id: Int): String {
    return context.resources.getString(id)
}

@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
fun getScreenHeightPx(): Int {
    val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val dm = DisplayMetrics()
    if (windowManager != null) {
        //            windowManager.getDefaultDisplay().getMetrics(dm);
        windowManager.defaultDisplay.getRealMetrics(dm)
        return dm.heightPixels
    }
    return 0
}

@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
fun getRealScreenRelatedInformation(context: Context) {
    val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    if (windowManager != null) {
        val outMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getRealMetrics(outMetrics)
        val widthPixels = outMetrics.widthPixels
        val heightPixels = outMetrics.heightPixels
        val densityDpi = outMetrics.densityDpi
        val density = outMetrics.density
        val scaledDensity = outMetrics.scaledDensity
        //可用显示大小的绝对宽度（以像素为单位）。
        //可用显示大小的绝对高度（以像素为单位）。
        //屏幕密度表示为每英寸点数。
        //显示器的逻辑密度。
        //显示屏上显示的字体缩放系数。
        Log.e(
            "display", "widthPixels = " + widthPixels + ",heightPixels = " + heightPixels + "\n" +
                    ",densityDpi = " + densityDpi + "\n" +
                    ",density = " + density + ",scaledDensity = " + scaledDensity
        )
    }
}

fun getScreenRelatedInformation() {
    val outMetrics = getDisplayMetrics()
    val widthPixels = outMetrics!!.widthPixels
    val heightPixels = outMetrics.heightPixels
    val densityDpi = outMetrics.densityDpi
    val density = outMetrics.density
    val scaledDensity = outMetrics.scaledDensity
    //可用显示大小的绝对宽度（以像素为单位）。
    //可用显示大小的绝对高度（以像素为单位）。
    //屏幕密度表示为每英寸点数。
    //显示器的逻辑密度。
    //显示屏上显示的字体缩放系数。
}

fun getStatusBarHeight(): Int {
    val resources = context.getResources()
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    return resources.getDimensionPixelSize(resourceId)
}

fun getInteger(id: Int): Int {
    return context.resources.getInteger(id)
}

fun getArray(id: Int): Array<String> {
    return context.resources.getStringArray(id)
}

fun getColor(id: Int): Int {
    return context.resources.getColor(id)
}

fun getIntArray(id: Int): IntArray {
    return context.resources.getIntArray(id)
}

fun getText(id: Int): CharSequence {
    return context.resources.getText(id)
}

fun getDisplayMetrics(): DisplayMetrics? {
    return context.resources.displayMetrics
}

fun getDimension(id: Int): Float {
    return context.resources.getDimension(id)
}

fun getWindowWidth(): Int? {
    return getDisplayMetrics()?.widthPixels
}

fun getWindowHight(): Int? {
    return getDisplayMetrics()?.heightPixels
}

fun getDpi(): Int? {
    return getDisplayMetrics()?.densityDpi
}

fun setPhone(phone: String): String {
    var t = phone.substring(0, 3)
    var k = phone.substring(3, 7)
    return t + " " + k + " " + phone.substring(7)
}