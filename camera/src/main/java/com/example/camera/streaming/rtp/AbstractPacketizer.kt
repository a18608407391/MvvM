package com.example.camera.streaming.rtp

import com.example.camera.streaming.rtcp.SenderReport
import java.io.IOException




/**
 * 抽象打包器
 * */
class AbstractPacketizer{




    var socket:RtpSocket ? = null


    fun getRtpSocket(): RtpSocket {
        return socket!!
    }

    fun getRtcpSocket(): SenderReport {
        return socket!!.getRtcpSocket()
    }

    fun setTimeToLive(mTTL: Int) {
        socket!!.setTimeToLive(mTTL)
    }

}