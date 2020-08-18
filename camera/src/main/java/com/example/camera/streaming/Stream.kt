package com.example.camera.streaming

import java.io.IOException
import java.net.InetAddress


interface Stream {


    @Throws(IllegalStateException::class, IOException::class)
    fun configure()


    @Throws(IllegalStateException::class, IOException::class)
     fun start()

    /**
     * 停止流
     */
     fun stop()

    /**
     * 设置通过网络发送的数据包的生存时间。
     */
    @Throws(IOException::class)
     fun setTimeToLive(ttl: Int)

    /**
     * 设置流的目标ip地址
     *
     */
     fun setDestinationAddress(dest: InetAddress)

    /**
    设置流的目标端口。

    如果为目标端口提供奇数，则下一个

    低偶数将用于RTP，它将用于RTCP。

    如果提供一个偶数，它将用于RTP和下一个奇数

    编号将用于RTCP。
     * @param dport The destination port
     */
     fun setDestinationPorts(dport: Int)

    /**
     *
     * @param rtpPort rtp端口
     * @param rtcpPort rtcp端口
     */
     fun setDestinationPorts(rtpPort: Int, rtcpPort: Int)

    /**
     * 返回一对源端口，第一个用于RTP，第二个用于RTCP。
     */
     fun getLocalPorts(): IntArray

    /**
     * 返回一对源端口，第一个用于RTP，第二个用于RTCP。
     */
     fun getDestinationPorts(): IntArray


    /**
     * Returns the SSRC of the underlying [net.majorkernelpanic.streaming.rtp.RtpSocket].
     * @return the SSRC of the stream.
     */
     fun getSSRC(): Int

    /**
     * 返回流消耗的比特率的近似值，单位为每秒比特数。
     */
     fun getBitrate(): Long

    /**
     * Returns a description of the stream using SDP.
     * This method can only be called after [Stream.configure].
     * @throws IllegalStateException Thrown when [Stream.configure] wa not called.
     */
    @Throws(IllegalStateException::class)
     fun getSessionDescription(): String

     fun isStreaming(): Boolean


}