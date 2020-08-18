package com.example.camera.init

import android.app.Application
import android.content.SharedPreferences
import com.example.camera.config.NOTIFICATION_ENABLED
import com.zl.zlibrary.Utils.PreferenceUtils


class CameraApplicationInit : SharedPreferences.OnSharedPreferenceChangeListener {
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {

    }

    lateinit var app: Application
    var notificationEnabled = true


    fun init(app: Application) {
        this.app = app
        notificationEnabled =  PreferenceUtils.getBoolean(app, NOTIFICATION_ENABLED, true)

        var sp = PreferenceUtils.getSp(app)
        sp.registerOnSharedPreferenceChangeListener(this)
    }
}