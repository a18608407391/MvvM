package com.example.camera.init

import android.app.Application
import android.content.SharedPreferences
import android.os.Build
import com.example.camera.config.*
import com.example.camera.session.SessionBuilder
import com.example.camera.streaming.video.VideoQuality
import com.zl.zlibrary.Utils.PreferenceUtils


/***
 *
 *   实现简单实时流媒体传输  ---直播
 * */

class CameraApplicationInit : SharedPreferences.OnSharedPreferenceChangeListener {

    val TAG = "SpydroidApplication"

    /** 视频流的默认质量*/
    var videoQuality = VideoQuality(320, 240, 20, 500000)

    /** 默认音频编码格式  */
    var audioEncoder = SessionBuilder.AUDIO_AAC

    /** 默认视频编码格式  */
    var videoEncoder = SessionBuilder.VIDEO_H264

    /** 广告禁用 */
    val DONATE_VERSION = false

    /** If the notification is enabled in the status bar of the phone.  */

    /** The HttpServer will use those variables to send reports about the state of the app to the web interface.  */
    var applicationForeground = true
    var lastCaughtException: Exception? = null

    /** Contains an approximation of the battery level.  */
    var batteryLevel = 0

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {

    }

    lateinit var app: Application
    var notificationEnabled = true


    fun init(app: Application) {
        this.app = app
        notificationEnabled = PreferenceUtils.getBoolean(app, NOTIFICATION_ENABLED, true)

        audioEncoder =
            if (Build.VERSION.SDK_INT < 14) SessionBuilder.AUDIO_AMRNB else SessionBuilder.AUDIO_AAC
        audioEncoder = PreferenceUtils.getInt(app, AUDIO_ENCODER)
        videoEncoder = PreferenceUtils.getInt(app, VIDEO_ENCODER)
        videoQuality = VideoQuality(
            PreferenceUtils.getInt(app, VIDEO_RESX, videoQuality.resX),
            PreferenceUtils.getInt(app, VIDEO_RESY, videoQuality.resX),
            PreferenceUtils.getInt(app, VIDEO_FRAMERATE, videoQuality.resX),
            PreferenceUtils.getInt(app, VIDEO_BITRATE, videoQuality.bitrate)
        )

        SessionBuilder.instance.setAudioEncoder(audioEncoder).setContext(app)
            .setVideoEncoder(videoEncoder).setVideoQuality(videoQuality)


        var sp = PreferenceUtils.getSp(app)
        sp.registerOnSharedPreferenceChangeListener(this)
    }



}