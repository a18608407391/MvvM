package com.example.camera.session

import android.content.Context
import android.hardware.Camera
import android.view.SurfaceView
import com.example.camera.streaming.audio.AudioQuality
import com.example.camera.streaming.video.VideoQuality
import java.nio.charset.CharsetEncoder


class SessionBuilder {


    private var mOrigin: String? = null
    private var mDestination: String? = null
    private var mCallback: Session.Callback? = null
    private var mTimeToLive = 64
    var mVideoEncoder = VIDEO_H263
    private var mVideoQuality = VideoQuality.DEFAULT_VIDEO_QUALITY
    private var mAudioQuality = AudioQuality.DEFAULT_AUDIO_QUALITY
    var mAudioEncoder = AUDIO_AMRNB
    private var mSurfaceView: SurfaceView? = null
    private lateinit var mContext: Context
    private var mOrientation = 0
    private var mFlash = false
    private var mCamera = Camera.CameraInfo.CAMERA_FACING_BACK

    companion object {
        val TAG = "SessionBuilder"

        /** Can be used with [.setVideoEncoder].  */
        val VIDEO_NONE = 0

        /** Can be used with [.setVideoEncoder].  */
        val VIDEO_H264 = 1

        /** Can be used with [.setVideoEncoder].  */
        val VIDEO_H263 = 2

        /** Can be used with [.setAudioEncoder].  */
        val AUDIO_NONE = 0

        /** Can be used with [.setAudioEncoder].  */
        val AUDIO_AMRNB = 3

        /** Can be used with [.setAudioEncoder].  */
        val AUDIO_AAC = 5
        val instance: SessionBuilder by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            SessionBuilder()
        }
    }


    fun setContext(context: Context): SessionBuilder {
        this.mContext = context
        return this
    }

    fun setAudioEncoder(code: Int): SessionBuilder {
        this.mAudioEncoder = code
        return this
    }

    fun setVideoEncoder(code: Int): SessionBuilder {
        this.mVideoEncoder = code
        return this
    }

    fun setVideoQuality(quality: VideoQuality): SessionBuilder {
        this.mVideoQuality = quality
        return this
    }

    fun build(): Session {
        var session = Session().apply {
            this.mOrigin = this@SessionBuilder.mOrigin
            this.mDestination = this@SessionBuilder.mDestination
            this.mCallback = this@SessionBuilder.mCallback
            this.mTimeToLive = this@SessionBuilder.mTimeToLive

            when (mAudioEncoder) {
                AUDIO_AAC -> {
                    //aac格式
                }
                AUDIO_AMRNB -> {

                }
            }
            when (mVideoEncoder) {
                VIDEO_H263 -> {
                }
                VIDEO_H264 -> {
                }
            }
        }

        if (session.getAudioTrack() != null) {
            var audio = session.getAudioTrack()


        }

        if (session.getVideoTrack() != null) {
            var video = session.getVideoTrack()
        }
        return session
    }

    fun clone(): SessionBuilder {
        return SessionBuilder()
            .setDestination(mDestination)
            .setOrigin(mOrigin)
            .setSurfaceView(mSurfaceView!!)
            .setPreviewOrientation(mOrientation)
            .setVideoQuality(mVideoQuality)
            .setVideoEncoder(mVideoEncoder)
            .setFlashEnabled(mFlash)
            .setCamera(mCamera)
            .setTimeToLive(mTimeToLive)
            .setAudioEncoder(mAudioEncoder)
            .setAudioQuality(mAudioQuality)
            .setContext(mContext)
            .setCallback(mCallback)
    }

    private fun setCallback(mCallback: Session.Callback?): SessionBuilder {
        this.mCallback = mCallback
        return this
    }

    fun setAudioQuality(mAudioQuality: AudioQuality): SessionBuilder {
        this.mAudioQuality = mAudioQuality
        return this
    }

    fun setTimeToLive(mTimeToLive: Int): SessionBuilder {
        this.mTimeToLive = mTimeToLive
        return this
    }

    fun setCamera(mCamera: Int): SessionBuilder {
        this.mCamera = mCamera
        return this
    }

    fun setFlashEnabled(mFlash: Boolean): SessionBuilder {
        this.mFlash = mFlash
        return this
    }

    private fun setPreviewOrientation(mOrientation: Int): SessionBuilder {
        this.mOrientation = mOrientation
        return this
    }

    private fun setSurfaceView(mSurfaceView: SurfaceView): SessionBuilder {
        this.mSurfaceView = mSurfaceView
        return this
    }

    private fun setOrigin(mOrigin: String?): SessionBuilder {
        this.mOrigin = mOrigin
        return this
    }

    fun setDestination(mDestination: String?): SessionBuilder {
        this.mDestination = mDestination
        return this
    }


}