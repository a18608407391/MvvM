package com.example.camera.session


class SessionBuilder {
    val TAG = "SessionBuilder"

    /** Can be used with [.setVideoEncoder].  */
    val VIDEO_NONE = 0

    /** Can be used with [.setVideoEncoder].  */
    val VIDEO_H264 = 1

    /** Can be used with [.setVideoEncoder].  */
    val VIDEO_H263 = 2

    /** Can be used with [.setAudioEncoder].  */
    val AUDIO_NONE = 0

    /** Can be used with [.setAudioEncoder].  */
    val AUDIO_AMRNB = 3

    /** Can be used with [.setAudioEncoder].  */
    val AUDIO_AAC = 5

    private var mOrigin: String? = null
    private var mDestination: String? = null
    private var mCallback: Session.Callback? = null
    private var mTimeToLive = 64
    private var mVideoEncoder = VIDEO_H263
    private var mAudioEncoder = AUDIO_AMRNB

    companion object {

        val instance: SessionBuilder by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            SessionBuilder()
        }
    }


    fun build() {
        var session = Session().apply {
            this.mOrigin = this@SessionBuilder.mOrigin
            this.mDestination = this@SessionBuilder.mDestination
            this.mCallback = this@SessionBuilder.mCallback
            this.mTimeToLive = this@SessionBuilder.mTimeToLive

            when (mAudioEncoder) {
                AUDIO_AAC -> {
                    //aac格式
                }
                AUDIO_AMRNB -> {

                }
            }
            when (mVideoEncoder) {
                VIDEO_H263 -> {
                }
                VIDEO_H264 -> {
                }
            }
        }

        if (session.getAudioTrack() != null) {
            var audio = session.getAudioTrack()


        }

        if (session.getVideoTrack() != null) {
            var video = session.getVideoTrack()
        }


    }


}