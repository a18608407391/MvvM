package com.example.camera.streaming

import android.media.MediaCodec
import android.media.MediaRecorder
import android.net.LocalServerSocket
import android.net.LocalSocket
import android.net.LocalSocketAddress
import android.util.Log
import com.example.camera.streaming.rtp.AbstractPacketizer
import java.io.IOException
import java.net.InetAddress
import kotlin.random.Random


abstract class MediaStream : Stream {


    /** 将读取摄像机输出并通过网络发送RTP包的打包程序。 */
    protected var mPacketizer: AbstractPacketizer? = null


    protected var mMode: Byte = 0

    protected var mRequestedMode: Byte = 0

    protected var mStreaming = false
    protected var mConfigured = false
    protected var mRtpPort = 0
    protected var mRtcpPort = 0
    protected var mDestination: InetAddress? = null
    protected var mReceiver: LocalSocket? = null
    protected var mSender: LocalSocket? = null
    private var mLss: LocalServerSocket? = null
    private var mSocketId: Int = 0
    private var mTTL = 64
    protected var mMediaRecorder: MediaRecorder? = null
    protected var mMediaCodec: MediaCodec? = null


    companion object {
        protected val TAG = "MediaStream"
        /** 原始音频/视频将使用MediaRecorder API进行编码。  */
        val MODE_MEDIARECORDER_API: Byte = 0x01

        /** 原始音频/视频将使用带缓冲区的MediaCodec API进行编码。*/
        val MODE_MEDIACODEC_API: Byte = 0x02

        /** 原始音频/视频将使用带有表面的mediacodeapi进行编码。 */
        val MODE_MEDIACODEC_API_2: Byte = 0x05

        /** 将用于libstreaming保存的所有共享首选项的前缀  */
        protected val PREF_PREFIX = "libstreaming-"

        protected var sSuggestedMode = MODE_MEDIARECORDER_API

        init {
            try {
                Class.forName("android.media.MediaCodec")
                sSuggestedMode = MODE_MEDIACODEC_API
                Log.i(TAG, "Phone supports the MediaCoded API")
            } catch (e: ClassNotFoundException) {
                sSuggestedMode = MODE_MEDIARECORDER_API
                Log.i(TAG, "Phone does not support the MediaCodec API")
            }
        }
    }


    @Throws(IOException::class)
    protected fun createSocket()  {
        var address = "com.zlrtsp.streaming-"

        mSocketId = Random.nextInt()

        mLss = LocalServerSocket(address + mSocketId)

        mReceiver = LocalSocket()
        mReceiver!!.connect(LocalSocketAddress(address + mSocketId))
        mReceiver!!.receiveBufferSize = 500000
        mReceiver!!.soTimeout = 3000
        mSender = mLss!!.accept()
        mSender!!.sendBufferSize = 500000


    }

    constructor() {
        mRequestedMode = sSuggestedMode
        mMode = sSuggestedMode
    }


    override fun configure() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @Synchronized
    @Throws(IllegalStateException::class, IOException::class)
    override fun start() {

        checkNotNull(mDestination) { "No destination ip address set for the stream !" }

        check(!(mRtpPort <= 0 || mRtcpPort <= 0)) { "No destination ports set for the stream !" }

        mPacketizer!!.setTimeToLive(mTTL)

        if (mMode != MODE_MEDIARECORDER_API) {
            //使用带缓冲区的api
            encodeWithMediaCodec()
        } else {

            encodeWithMediaRecorder()
        }

    }

    @Throws(IOException::class)
    protected abstract fun encodeWithMediaRecorder()

    @Throws(IOException::class)
    protected abstract fun encodeWithMediaCodec()

    override fun stop() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setTimeToLive(ttl: Int) {
        mTTL = ttl
    }

    override fun setDestinationAddress(dest: InetAddress) {
        mDestination = dest
    }

    override fun setDestinationPorts(dport: Int) {
        //如果是奇数  rtp端口为偶数+1 = rtcp
        if (dport % 2 == 1) {
            mRtpPort = dport - 1
            mRtcpPort = dport
        } else {
            mRtpPort = dport
            mRtcpPort = dport + 1
        }
    }

    fun setStreamingMethod(mode: Byte) {
        mRequestedMode = mode
    }

    override fun setDestinationPorts(rtpPort: Int, rtcpPort: Int) {
        mRtpPort = rtpPort
        mRtcpPort = rtcpPort
    }

    override fun getLocalPorts(): IntArray {
        return intArrayOf(
            this.mPacketizer!!.getRtpSocket().getLocalPort(),
            this.mPacketizer!!.getRtcpSocket().getLocalPort()
        )
    }

    override fun getDestinationPorts(): IntArray {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSSRC(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBitrate(): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSessionDescription(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isStreaming(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}