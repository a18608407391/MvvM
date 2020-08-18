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

                // Those parameters must be parsed first or else they won't necessarily be taken into account
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
                    // TTL -> the client can modify the time to live of packets
                    // By default ttl=64
                    // AUDIOAPI -> can be used to specify what api will be used to encode audio (the MediaRecorder API or the MediaCodec API)
                    // VIDEOAPI -> can be used to specify what api will be used to encode video (the MediaRecorder API or the MediaCodec API)
                    // UNICAST -> the client can use this to specify where he wants the stream to be sent
                    // MULTICAST -> the stream will be sent to a multicast group
                    // The default mutlicast address is 228.5.6.7, but the client can specify another
                    // CAMERA -> the client can choose between the front facing camera and the back facing camera

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