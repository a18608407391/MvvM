package com.example.camera.streaming.audio


class AudioQuality {


    var samplingRate = 0
    var bitRate = 0

    constructor (samplingRate: Int, bitRate: Int) {
        this.samplingRate = samplingRate
        this.bitRate = bitRate
    }

    fun clone(): AudioQuality {
        return AudioQuality(samplingRate, bitRate)
    }

    companion object {
        val DEFAULT_AUDIO_QUALITY = AudioQuality(8000, 32000)
        fun parseQuality(str: String): AudioQuality {
            val quality = DEFAULT_AUDIO_QUALITY.clone()
            if (str != null) {
                val config = str.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                try {
                    quality.bitRate = Integer.parseInt(config[0]) * 1000 // conversion to bit/s
                    quality.samplingRate = Integer.parseInt(config[1])
                } catch (ignore: IndexOutOfBoundsException) {
                }
            }
            return quality
        }
    }
}