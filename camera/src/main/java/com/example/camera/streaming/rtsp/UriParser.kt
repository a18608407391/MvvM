package com.example.camera.streaming.rtsp

import android.hardware.Camera
import com.example.camera.session.Session
import com.example.camera.session.SessionBuilder
import com.example.camera.session.SessionBuilder.Companion.AUDIO_AAC
import com.example.camera.session.SessionBuilder.Companion.AUDIO_AMRNB
import com.example.camera.session.SessionBuilder.Companion.AUDIO_NONE
import com.example.camera.session.SessionBuilder.Companion.VIDEO_H263
import com.example.camera.session.SessionBuilder.Companion.VIDEO_H264
import com.example.camera.session.SessionBuilder.Companion.VIDEO_NONE
import com.example.camera.streaming.MediaStream
import com.example.camera.streaming.audio.AudioQuality
import com.example.camera.streaming.video.VideoQuality
import org.apache.http.client.utils.URLEncodedUtils
import java.io.IOException
import java.net.InetAddress
import java.net.URI
import java.net.UnknownHostException
class UriParser {


    companion object {

        @Throws(IllegalStateException::class, IOException::class)
        fun parse(uri: String): Session {
            var builder = SessionBuilder.instance.clone()
            var audioApi: Byte = 0
            var videoApi: Byte = 0
            var params = URLEncodedUtils.parse(URI.create(uri), "UTF-8")
            if (params.size > 0) {
                builder.setAudioEncoder(AUDIO_NONE).setVideoEncoder(VIDEO_NONE)
                // 必须首先解析这些参数，否则就不必考虑它们
                val it = params.iterator()
                while (it.hasNext()) {
                    val param = it.next()
                    // FLASH ON/OFF
                    if (param.name == "flash") {
                        if (param.value == "on")
                            builder.setFlashEnabled(true)
                        else
                            builder.setFlashEnabled(false)
                    } else if (param.name =="camera") {
                        if (param.value =="back")
                            builder.setCamera(Camera.CameraInfo.CAMERA_FACING_BACK)
                        else if (param.value =="front")
                            builder.setCamera(Camera.CameraInfo.CAMERA_FACING_FRONT)
                    } else if (param.name =="multicast") {
                        if (param.value != null) {
                            try {
                                val addr = InetAddress.getByName(param.value)
                                check(addr.isMulticastAddress) { "Invalid multicast address !" }
                                builder.setDestination(param.value)
                            } catch (e: UnknownHostException) {
                                throw IllegalStateException("Invalid multicast address !")
                            }

                        } else {
                            // Default multicast address
                            builder.setDestination("228.5.6.7")
                        }
                    } else if (param.name =="unicast") {
                        if (param.value != null) {
                            builder.setDestination(param.value)
                        }
                    } else if (param.name =="videoapi") {
                        if (param.value != null) {
                            if (param.value =="mr") {
                                videoApi = MediaStream.MODE_MEDIARECORDER_API
                            } else if (param.value =="mc") {
                                videoApi = MediaStream.MODE_MEDIACODEC_API
                            }
                        }
                    } else if (param.name =="audioapi") {
                        if (param.value != null) {
                            if (param.value =="mr") {
                                audioApi = MediaStream.MODE_MEDIARECORDER_API
                            } else if (param.value =="mc") {
                                audioApi = MediaStream.MODE_MEDIACODEC_API
                            }
                        }
                    } else if (param.name ==("ttl")) {
                        if (param.value != null) {
                            try {
                                val ttl = Integer.parseInt(param.value)
                                check(ttl >= 0)
                                builder.setTimeToLive(ttl)
                            } catch (e: Exception) {
                                throw IllegalStateException("The TTL must be a positive integer !")
                            }
                        }
                    } else if (param.name =="h264") {
                        var quality = VideoQuality.parseQuality(param.value)
                        builder.setVideoQuality(quality).setVideoEncoder(VIDEO_H264)
                    } else if (param.name =="h263") {
                        val quality = VideoQuality.parseQuality(param.value)
                        builder.setVideoQuality(quality).setVideoEncoder(VIDEO_H263)
                    } else if (param.name =="amrnb" || param.name== "amr"

                    ) {
                        val quality = AudioQuality.parseQuality(param.value)
                        builder.setAudioQuality(quality).setAudioEncoder(AUDIO_AMRNB)
                    } else if (param.name =="aac") {
                        val quality = AudioQuality.parseQuality(param.value)
                        builder.setAudioQuality(quality).setAudioEncoder(AUDIO_AAC)
                    }// AAC
                    // AMR
                    // H.263
                    // H.264
                    // TTL -> 客户端可以修改数据包的生存时间
                    // By default ttl=64
                    // AUDIOAPI -> 可用于指定将用于编码音频的api（MediaRecorder api或MediaCodec api）
                    // VIDEOAPI -> 可用于指定将用于编码视频的api（MediaRecorder api或MediaCodec api）
                    // UNICAST -> 客户机可以使用它来指定他希望流发送到哪里
                    // MULTICAST -> 流将被发送到多播组
                    // 默认的mutlicast地址是228.5.6.7，但是客户端可以指定另一个地址
                    // CAMERA -> 客户可以在前置摄像头和后置摄像头之间进行选择

                }

            }

            if (builder.mVideoEncoder == VIDEO_NONE && builder.mAudioEncoder == AUDIO_NONE) {
                val b = SessionBuilder.instance
                builder.setVideoEncoder(b.mVideoEncoder)
                builder.setAudioEncoder(b.mAudioEncoder)
            }

            var session = builder.build()

            if (videoApi > 0 && session.getVideoTrack() != null) {
                session.getVideoTrack().setStreamingMethod(videoApi)
            }
            if (audioApi > 0 && session.getAudioTrack() != null) {
                session.getAudioTrack().setStreamingMethod(audioApi)
            }
            return session
        }
    }
}