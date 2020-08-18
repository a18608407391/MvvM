package com.example.camera.session

import android.content.Context
import com.example.camera.streaming.video.VideoQuality
import java.nio.charset.CharsetEncoder


class SessionBuilder {


    private var mOrigin: String? = null
    private var mDestination: String? = null
    private var mCallback: Session.Callback? = null
    private var mTimeToLive = 64
     var mVideoEncoder = VIDEO_H263
    lateinit var mVideoQuality:VideoQuality
     var mAudioEncoder = AUDIO_AMRNB
    private lateinit var mContext:Context

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

    fun setAudioEncoder(code:Int): SessionBuilder {
         this.mAudioEncoder = code
        return this
    }

    fun setVideoEncoder(code:Int): SessionBuilder {
        this.mVideoEncoder = code
        return this
    }

    fun setVideoQuality(quality: VideoQuality): SessionBuilder {
        this.mVideoQuality = quality
        return this
    }

    fun build() {
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
    }


}