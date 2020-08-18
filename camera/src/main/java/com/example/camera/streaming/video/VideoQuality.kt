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

}