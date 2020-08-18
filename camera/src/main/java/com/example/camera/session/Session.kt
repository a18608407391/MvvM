package com.example.camera.session

import android.os.Handler
import com.example.camera.streaming.audio.AudioStream
import com.example.camera.streaming.video.VideoStream
import java.util.concurrent.CountDownLatch


class Session {
    val TAG = "Session"

    val STREAM_VIDEO = 0x01

    val STREAM_AUDIO = 0x00

    /** Some app is already using a camera (Camera.open() has failed).  */
    val ERROR_CAMERA_ALREADY_IN_USE = 0x00

    /** The phone may not support some streaming parameters that you are using (bit rate, frame rate...s).  */
    val ERROR_CONFIGURATION_NOT_SUPPORTED = 0x01

    /**
     * The internal storage of the phone is not ready.
     * Libstreaming tried to store a test file on the sdcard but couldn't.
     * See H264Stream and AACStream to find out why libstreaming would want to something like that.
     */
    val ERROR_STORAGE_NOT_READY = 0x02

    /** The phone has no flash.  */
    val ERROR_CAMERA_HAS_NO_FLASH = 0x03

    /** The supplied SurfaceView is not a valid surface, or has not been created yet.  */
    val ERROR_INVALID_SURFACE = 0x04

    /**
     * The destination set with [Session.setDestination] could not be resolved.
     * May mean that the phone has no access to the internet, or that the DNS server could not
     * resolved the host name.
     */
    val ERROR_UNKNOWN_HOST = 0x05

    /**
     * Some other error occured !
     */
    val ERROR_OTHER = 0x06


    var mOrigin: String? = null        //起源

    var mDestination: String? = null   //目的地
    var mTimeToLive = 64
    private var mTimestamp: Long = 0   //时间戳

    private var mAudioStream: AudioStream? = null
    private var mVideoStream: VideoStream? = null

    var mCallback: Callback? = null
    private var mMainHandler: Handler? = null

    private var sSignal: CountDownLatch? = null
    private var sHandler: Handler? = null

    /** 获取当前的声轨  */
    fun getAudioTrack(): AudioStream {
        return mAudioStream!!
    }

    /** 获取当前的影轨  */
    fun getVideoTrack(): VideoStream {
        return mVideoStream!!
    }

    fun isStreaming(): Boolean {
        return mAudioStream != null && mAudioStream!!.isStreaming() || mVideoStream != null && mVideoStream!!.isStreaming()
    }


    fun stop(){
        sHandler!!.post { syncStop() }
    }
    private fun syncStop(id: Int) {
        val stream = if (id == 0) mAudioStream else mVideoStream
        stream?.stop()
    }

    fun syncStop() {
        syncStop(0)
        syncStop(1)
        postSessionStopped()
    }

    private fun postSessionStopped() {

    }

    fun syncConfigure() {
    }

    fun getSessionDescription(): String {

        return null!!
    }

    fun setDestination(hostAddress: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun setOrigin(hostAddress: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    interface Callback {

    }

}