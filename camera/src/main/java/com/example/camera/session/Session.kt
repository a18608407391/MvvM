package com.example.camera.session

import android.os.Handler
import com.example.camera.streaming.Stream
import com.example.camera.streaming.audio.AudioStream
import com.example.camera.streaming.exception.CameraInUseException
import com.example.camera.streaming.exception.ConfNotSupportedException
import com.example.camera.streaming.exception.InvalidSurfaceException
import com.example.camera.streaming.exception.StorageUnavailableException
import com.example.camera.streaming.video.VideoStream
import java.io.IOException
import java.math.BigDecimal
import java.net.InetAddress
import java.net.UnknownHostException
import java.util.concurrent.CountDownLatch


class Session {
    val TAG = "Session"

    val STREAM_VIDEO = 0x01

    val STREAM_AUDIO = 0x00

    /** 某些应用已在使用摄像头(摄像头打开（）失败）*/
    val ERROR_CAMERA_ALREADY_IN_USE = 0x00

    /** 手机可能不支持您正在使用的某些流媒体参数（比特率、帧速率…s）。*/
    val ERROR_CONFIGURATION_NOT_SUPPORTED = 0x01

    /**
     * T手机的内部存储器没有准备好。
     * 尝试在SD卡上存储测试文件，但无法。
     * 请参阅H264Stream和AACStream，以了解libstreaming为什么要这样做。
     */
    val ERROR_STORAGE_NOT_READY = 0x02

    /** The phone has no flash.  */
    val ERROR_CAMERA_HAS_NO_FLASH = 0x03

    /** The supplied SurfaceView is not a valid surface, or has not been created yet.  */
    val ERROR_INVALID_SURFACE = 0x04

    /**
     *  没有网无法识别host
     *
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


    fun stop() {
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

    @Throws(
        CameraInUseException::class,
        StorageUnavailableException::class,
        ConfNotSupportedException::class,
        InvalidSurfaceException::class,
        RuntimeException::class,
        IOException::class
    )
    fun syncConfigure() {
        for (id in 0..1) {
            val stream = if (id == 0) mAudioStream else mVideoStream
            if (stream != null && !stream.isStreaming()) {
                try {
                    stream.configure()
                } catch (e: CameraInUseException) {
                    postError(ERROR_CAMERA_ALREADY_IN_USE, id, e)
                    throw e
                } catch (e: StorageUnavailableException) {
                    postError(ERROR_STORAGE_NOT_READY, id, e)
                    throw e
                } catch (e: ConfNotSupportedException) {
                    postError(ERROR_CONFIGURATION_NOT_SUPPORTED, id, e)
                    throw e
                } catch (e: InvalidSurfaceException) {
                    postError(ERROR_INVALID_SURFACE, id, e)
                    throw e
                } catch (e: IOException) {
                    postError(ERROR_OTHER, id, e)
                    throw e
                } catch (e: RuntimeException) {
                    postError(ERROR_OTHER, id, e)
                    throw e
                }

            }
        }
        postSessionConfigured()
    }

    private fun postError(reason: Int, streamType: Int, e: Exception) {
        mMainHandler!!.post {
            if (mCallback != null) {
                mCallback!!.onSessionError(reason, streamType, e)
            }
        }
    }

    private fun postSessionConfigured() {
        mMainHandler!!.post {
            if (mCallback != null) {
                mCallback!!.onSessionConfigured()
            }
        }
    }

    fun getSessionDescription(): String {
        val sessionDescription = StringBuilder()
        checkNotNull(mDestination) { "setDestination() has not been called !" }

        sessionDescription.append("v=0\r\n")
        // TODO: Add IPV6 support
        sessionDescription.append("o=- $mTimestamp $mTimestamp IN IP4 $mOrigin\r\n")
        sessionDescription.append("s=Unnamed\r\n")
        sessionDescription.append("i=N/A\r\n")
        sessionDescription.append("c=IN IP4 $mDestination\r\n")
        // t=0 0 means the session is permanent (we don't know when it will stop)
        sessionDescription.append("t=0 0\r\n")
        sessionDescription.append("a=recvonly\r\n")
        // Prevents two different sessions from using the same peripheral at the same time
        if (mAudioStream != null) {
            sessionDescription.append(mAudioStream!!.getSessionDescription())
            sessionDescription.append("a=control:trackID=" + 0 + "\r\n")
        }
        if (mVideoStream != null) {
            sessionDescription.append(mVideoStream!!.getSessionDescription())
            sessionDescription.append("a=control:trackID=" + 1 + "\r\n")
        }
        return sessionDescription.toString()
    }

    fun setDestination(hostAddress: String?) {
        this.mDestination = hostAddress
    }

    fun setOrigin(origin: String?) {
        this.mOrigin = origin
    }

    interface Callback {
        fun onSessionConfigured()
        fun onSessionError(reason: Int, streamType: Int, e: Exception)
        fun onSessionStarted()
        fun onBitrareUpdate(bitrate: Long)
    }


    /**
     * 根据id获取当前的流类型
     * */
    fun getTrack(id: Int): Stream? {
        return if (id == 0)
            mAudioStream
        else
            mVideoStream
    }

    @Throws(
        CameraInUseException::class,
        StorageUnavailableException::class,
        ConfNotSupportedException::class,
        InvalidSurfaceException::class,
        UnknownHostException::class,
        IOException::class
    )
    fun syncStart() {
        syncStart(1)
        try {
            syncStart(0)
        } catch (e: RuntimeException) {
            syncStop(1)
            throw e
        } catch (e: IOException) {
            syncStop(1)
            throw e
        }
    }

    @Throws(
        CameraInUseException::class,
        StorageUnavailableException::class,
        ConfNotSupportedException::class,
        InvalidSurfaceException::class,
        UnknownHostException::class,
        IOException::class
    )
    fun syncStart(id: Int) {
        val stream = if (id == 0) mAudioStream else mVideoStream
        if (stream != null && !stream.isStreaming()) {
            try {
                val destination = InetAddress.getByName(mDestination)
                stream.setTimeToLive(mTimeToLive)
                stream.setDestinationAddress(destination)
                stream.start()

                if (getTrack(1 - id) == null || getTrack(1 - id)!!.isStreaming()) {
                    postSessionStarted()
                }
                if (getTrack(1 - id) == null || !getTrack(1 - id)!!.isStreaming()) {
                    sHandler!!.post(mUpdateBitrate)
                }
            } catch (e: UnknownHostException) {
                postError(ERROR_UNKNOWN_HOST, id, e)
                throw e
            } catch (e: CameraInUseException) {
                postError(ERROR_CAMERA_ALREADY_IN_USE, id, e)
                throw e
            } catch (e: StorageUnavailableException) {
                postError(ERROR_STORAGE_NOT_READY, id, e)
                throw e
            } catch (e: ConfNotSupportedException) {
                postError(ERROR_CONFIGURATION_NOT_SUPPORTED, id, e)
                throw e
            } catch (e: InvalidSurfaceException) {
                postError(ERROR_INVALID_SURFACE, id, e)
                throw e
            } catch (e: IOException) {
                postError(ERROR_OTHER, id, e)
                throw e
            } catch (e: RuntimeException) {
                postError(ERROR_OTHER, id, e)
                throw e
            }

        }

    }
    private val mUpdateBitrate = object : Runnable {
        override fun run() {
            if (isStreaming()) {
                postBitRate(getBitrate())
                sHandler!!.postDelayed(this, 500)
            } else {
                postBitRate(0)
            }
        }
    }

    /**
     * 返回会话消耗的带宽的近似值，单位为位/秒。
     * */
    fun getBitrate(): Long {
        var sum: Long = 0
        if (mAudioStream != null) sum += mAudioStream!!.getBitrate()
        if (mVideoStream != null) sum += mVideoStream!!.getBitrate()
        return sum
    }

    private fun postBitRate(bitrate: Long) {
        mMainHandler!!.post {
            if (mCallback != null) {
                mCallback!!.onBitrareUpdate(bitrate)
            }
        }
    }

    private fun postSessionStarted() {
        mMainHandler!!.post {
            if (mCallback != null) {
                mCallback!!.onSessionStarted()
            }
        }
    }

    fun trackExists(id: Int): Boolean {
        return if (id == 0)
            mAudioStream != null
        else
            mVideoStream != null
    }
}