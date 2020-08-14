package com.zl.zlibrary.ext

import android.content.ClipData
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.text.Html
import android.text.Spanned
import android.text.TextUtils
import android.view.View
import android.view.ViewConfiguration


val Context.screenWidth
    get() = resources.displayMetrics.widthPixels

/**
 * 获取屏幕高度
 */
val Context.screenHeight
    get() = resources.displayMetrics.heightPixels

//val Context.real_screenWidth
//get() = resources.displayMetrics.

val Context.getStatusBarHeight
get() =this.resources.getDimensionPixelSize(this.resources.getIdentifier("status_bar_height", "dimen", "android"))



/**
 * 底部navigation高度dp
 */
val Context.getNavigationBarHeight:Int
get() {
    var result = 0
    if (hasNavBar) {
        val resourceId = this.resources.getIdentifier("navigation_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = this.resources.getDimensionPixelSize(resourceId)
        }
    }
    return result
}


/**
 * 是否有Navigation
 */
val Context.hasNavBar:Boolean
get() {
    var resourceId = this.resources.getIdentifier("config_showNavigationBar", "bool", "android");
    if (resourceId != 0) {
        var hasNav = this.resources.getBoolean(resourceId);
        var sNavBarOverride:String = getNavBarOverride
        if ("1".equals(sNavBarOverride)) {
            hasNav = false
        } else if ("0" == sNavBarOverride) {
            hasNav = true
        }
        return hasNav;
    } else { // fallback
        return !ViewConfiguration.get(this).hasPermanentMenuKey();
    }
}

val getNavBarOverride :String
get() {
    var sNavBarOverride = ""
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        try {
            val c = Class.forName("android.os.SystemProperties")
            val m = c.getDeclaredMethod("get", String::class.java)
            m.isAccessible = true
            sNavBarOverride = m.invoke(null, "qemu.hw.mainkeys") as String
        } catch (e: Throwable) {

        }
    }
    return sNavBarOverride;
}

/**
 * 判断是否为空 并传入相关操作
 */
inline fun <reified T> T?.notNull(notNullAction: (T) -> Unit, nullAction: () -> Unit = {}) {
    if (this != null) {
        notNullAction.invoke(this)
    } else {
        nullAction.invoke()
    }
}

/**
 * dp值转换为px
 */
fun Context.dp2px(dp: Int): Int {
    val scale = resources.displayMetrics.density
    return (dp * scale + 0.5f).toInt()
}

/**
 * px值转换成dp
 */
fun Context.px2dp(px: Int): Int {
    val scale = resources.displayMetrics.density
    return (px / scale + 0.5f).toInt()
}

/**
 * dp值转换为px
 */
fun View.dp2px(dp: Int): Int {
    val scale = resources.displayMetrics.density
    return (dp * scale + 0.5f).toInt()
}

/**
 * px值转换成dp
 */
fun View.px2dp(px: Int): Int {
    val scale = resources.displayMetrics.density
    return (px / scale + 0.5f).toInt()
}


/**
 * 检查是否启用无障碍服务
 */
fun Context.checkAccessibilityServiceEnabled(serviceName: String): Boolean {
    val settingValue =
        Settings.Secure.getString(
            applicationContext.contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        )
    var result = false
    val splitter = TextUtils.SimpleStringSplitter(':')
    while (splitter.hasNext()) {
        if (splitter.next().equals(serviceName, true)) {
            result = true
            break
        }
    }
    return result
}

/**
 * 设置点击事件
 * @param views 需要设置点击事件的view
 * @param onClick 点击触发的方法
 */

fun String.toHtml(flag: Int = Html.FROM_HTML_MODE_LEGACY): Spanned {
    return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
        Html.fromHtml(this, flag)
    } else {
        Html.fromHtml(this)
    }
}


