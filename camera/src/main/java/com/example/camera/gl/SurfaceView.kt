package com.example.camera.gl

import android.content.Context
import android.graphics.SurfaceTexture
import android.util.AttributeSet
import android.view.SurfaceView


class SurfaceView @JvmOverloads constructor(context: Context, attrs: AttributeSet, def: Int) :
    SurfaceView(context, null!!, 0) {
    private var mTextureManager: TextureManager? = null

    fun startGLThread() {


    }

    fun getSurfaceTexture(): SurfaceTexture? {
        return mTextureManager!!.getSurfaceTexture()
    }

}