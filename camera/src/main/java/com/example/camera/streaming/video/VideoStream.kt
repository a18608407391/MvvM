package com.example.camera.streaming.video

import android.hardware.Camera
import android.os.Looper
import android.util.Log
import com.example.camera.gl.SurfaceView
import com.example.camera.streaming.MediaStream
import com.example.camera.streaming.exception.CameraInUseException
import com.example.camera.streaming.exception.InvalidSurfaceException
import java.io.IOException
import java.lang.RuntimeException
import java.util.concurrent.Semaphore


class VideoStream : MediaStream {


    var TAG = this.javaClass.simpleName
    var mCamera: Camera? = null
    protected var mUnlocked = false
    protected var mCameraLooper: Looper? = null
    protected var mSurfaceView: SurfaceView? = null
    protected var mSurfaceReady = false
    protected var mFlashEnabled = false
    //手动打开摄像头
    var mCameraOpenedManually = true
    var mCameraId = 0

    var mCameraThread: Thread? = null
    /**
     *预启动
     * */
    protected var mPreviewStarted = false

    override fun encodeWithMediaCodec() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun encodeWithMediaRecorder() {


        //初始化socket
        createSocket()

        //释放摄像头
        destroyCamera()

        //创建摄像头
        createCamera()
    }

    @Synchronized
    @Throws(RuntimeException::class)
    fun createCamera() {
        if (mSurfaceView == null)
            throw InvalidSurfaceException("Invalid surface or surface==null!")
        if (mSurfaceView!!.holder == null || !mSurfaceReady)
            throw InvalidSurfaceException("Invalid surface or surface not ready !")

        if (mCamera == null) {
            openCamera()
            mUnlocked = false
            mCamera!!.setErrorCallback(object : Camera.ErrorCallback {
                override fun onError(error: Int, camera: Camera?) {
                    if (error == Camera.CAMERA_ERROR_SERVER_DIED) {
                        // In this case the application must release the camera and instantiate a new one
                        Log.e(TAG, "Media server died !")
                        // We don't know in what thread we are so stop needs to be synchronized
                        mCameraOpenedManually = false
                        stop()
                    } else {
                        Log.e(TAG, "Error unknown with the camera: $error")
                    }
                }
            })
        }

        try {
            // If the phone has a flash, we turn it on/off according to mFlashEnabled
            // setRecordingHint(true) is a very nice optimisation if you plane to only use the Camera for recording
            var parameters = mCamera!!.parameters
            if (parameters.flashMode != null) {
                parameters.flashMode =
                    if (mFlashEnabled) Camera.Parameters.FLASH_MODE_TORCH else Camera.Parameters.FLASH_MODE_OFF
            }

            parameters.setRecordingHint(true)
            mCamera!!.setParameters(parameters)
            mCamera!!.setDisplayOrientation(mOrientation)

            try {
                if (mMode === MODE_MEDIACODEC_API_2) {
                    mSurfaceView!!.startGLThread()
                    mCamera!!.setPreviewTexture(mSurfaceView!!.getSurfaceTexture())
                } else {
                    mCamera!!.setPreviewDisplay(mSurfaceView!!.getHolder())
                }
            } catch (e: IOException) {
                throw InvalidSurfaceException("Invalid surface !")
            }

        } catch (e: RuntimeException) {
            destroyCamera()
            throw e
        }

    }

    private fun openCamera() {

        /**
         * 计数信号量，必须由获取它的线程释放
         * 同步关键类,为0表示仅为单独运行
         */
        var lock = Semaphore(0)

        val exception = arrayOfNulls<RuntimeException>(1)
        mCameraThread = Thread {
            Looper.prepare()
            mCameraLooper = Looper.myLooper()
            try {
                mCamera = Camera.open(mCameraId)
            } catch (e: RuntimeException) {
                exception[0] = e
            } finally {
                lock.release()
                Looper.loop()
            }
        }
        mCameraThread!!.start()
        //使等待进入acquire（）方法的线程，不允许被中断
        lock.acquireUninterruptibly()
        if (exception[0] != null) throw CameraInUseException(exception[0]!!.message!!)

    }

    @Synchronized
    private fun destroyCamera() {
        if (mCamera != null) {
            if (mStreaming) {
                stop()
            }
            lockCamera()
            mCamera!!.stopPreview()
            try {
                mCamera!!.release()
            } catch (e: Exception) {
                Log.e(TAG, if (e.message != null) e.message else "unknown error")
            }
            mCamera = null
            mCameraLooper!!.quit()
            mUnlocked = false
            mPreviewStarted = false
        }

    }

    protected fun lockCamera() {
        if (mUnlocked) {
            try {
                //重新"锁住"相机
                mCamera!!.reconnect()
            } catch (e: Exception) {
                Log.e(TAG, e.message)
            }
            mUnlocked = false
        }
    }

    constructor()

    protected var mRequestedOrientation = 0
    protected var mOrientation = 0

    @Synchronized
    @Throws(IllegalStateException::class, IOException::class)
    override fun configure() {
        super.configure()
        mOrientation = mRequestedOrientation
    }
}