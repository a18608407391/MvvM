package com.buyer.redman.Service

import android.app.IntentService
import android.content.Intent
import android.util.Log
import androidx.multidex.MultiDex
import com.alibaba.android.arouter.launcher.ARouter
import com.buyer.redman.BuildConfig
import com.buyer.redman.R
import com.didichuxing.doraemonkit.DoraemonKit
import me.jessyan.autosize.AutoSize
import uk.co.chrisjenx.calligraphy.CalligraphyConfig


class InitService : IntentService {

    constructor() : super("InitService")

    override fun onHandleIntent(intent: Intent?) {
        if (BuildConfig.DEBUG) {
//            // 打印日志
            ARouter.openLog()
            //开启调试模式
            ARouter.openDebug()
        }
        MultiDex.install(application)
        AutoSize.initCompatMultiProcess(this)
        ARouter.init(application)
        CalligraphyConfig.initDefault(
            CalligraphyConfig.Builder()
            .setFontAttrId(R.attr.fontPath)
            .build()
        )
        DoraemonKit.install(application)

    }
}