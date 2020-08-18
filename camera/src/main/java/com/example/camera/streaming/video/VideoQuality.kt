package com.example.camera.streaming.video


class VideoQuality {

    var framerate = 0
    var bitrate = 0
    var resX = 0
    var resY = 0


    constructor(resX: Int, resY: Int, framerate: Int, bitrate: Int) {
        this.resX = resX
        this.resY = resY
        this.framerate = framerate
        this.bitrate = bitrate
    }


    companion object {
        val DEFAULT_VIDEO_QUALITY = VideoQuality(176, 144, 20, 500000)
        fun parseQuality(str: String): VideoQuality {
            var quality = DEFAULT_VIDEO_QUALITY.clone()
            if (str != null) {
                val config = str.split("-".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                try {
                    quality.bitrate = Integer.parseInt(config[0]) * 1000 // conversion to bit/s
                    quality.framerate = Integer.parseInt(config[1])
                    quality.resX = Integer.parseInt(config[2])
                    quality.resY = Integer.parseInt(config[3])
                } catch (ignore: IndexOutOfBoundsException) {

                }

            }
            return quality
        }
    }

    fun clone(): VideoQuality {
        return VideoQuality(resX, resY, framerate, bitrate)
    }
}