package com.example.camera.streaming.rtp

import com.example.camera.streaming.rtcp.SenderReport
import java.io.IOException
import java.net.MulticastSocket


class RtpSocket{
    private val mReport: SenderReport ? = null
    private var mSocket: MulticastSocket? = null
    fun getRtcpSocket(): SenderReport {

        return mReport!!
    }

    fun getLocalPort(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun setTimeToLive(mTTL: Int) {
        mSocket!!.timeToLive = mTTL
    }

    /**
     * 这个RTP套接字实现了一个依赖于缓冲区FIFO和线程的缓冲机制。
     * @throws IOException
     */


}