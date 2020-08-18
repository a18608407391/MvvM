package com.example.camera.streaming.rtsp

import android.app.Service
import android.content.Intent
import android.content.SharedPreferences
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.example.camera.session.Session
import com.example.camera.session.SessionBuilder
import com.zl.zlibrary.Utils.PreferenceUtils
import com.zl.zlibrary.Utils.context
import java.io.*
import java.net.BindException
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketException
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


//rtsp服务端
class RtspServer : Service(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {

    }

    val TAG = "RtspServer"

    /** The server name that will appear in responses.  */
    var SERVER_NAME = "MajorKernelPanic RTSP Server"

    /** Port used by default.  */
    val DEFAULT_RTSP_PORT = 8086

    /** Port already in use.  */
    val ERROR_BIND_FAILED = 0x00

    /** A stream could not be started.  */
    val ERROR_START_FAILED = 0x01

    /** Streaming started.  */
    val MESSAGE_STREAMING_STARTED = 0X00

    /** Streaming stopped.  */
    val MESSAGE_STREAMING_STOPPED = 0X01

    /** Key used in the SharedPreferences to store whether the RTSP server is enabled or not.  */
    val KEY_ENABLED = "rtsp_enabled"

    /** Key used in the SharedPreferences for the port used by the RTSP server.  */
    val KEY_PORT = "rtsp_port"

    protected var mSessionBuilder: SessionBuilder? = null
    protected var mEnabled = true
    var mPort = DEFAULT_RTSP_PORT
    protected var mSessions = WeakHashMap<Session, Any>(2)

    private var mListenerThread: RequestListener? = null
    private val mBinder = LocalBinder()
    private var mRestart = false
    private val mListeners = LinkedList<CallbackListener>()

    /** Be careful: those callbacks won't necessarily be called from the ui thread !  */
    interface CallbackListener {

        /** Called when an error occurs.  */
        fun onError(server: RtspServer, e: Exception, error: Int)

        /** Called when streaming starts/stops.  */
        fun onMessage(server: RtspServer, message: Int)

    }

    override fun onBind(intent: Intent?): IBinder? {
        return mBinder
    }

    inner class LocalBinder : Binder() {
        val service: RtspServer
            get() = this@RtspServer
    }

    protected fun postError(exception: Exception, id: Int) {
        synchronized(mListeners) {
            if (mListeners.size > 0) {
                for (cl in mListeners) {
                    cl.onError(this, exception, id)
                }
            }
        }
    }

    inner class RequestListener : Thread, Runnable {
        private var mServer: ServerSocket

        constructor() {
            try {
                mServer = ServerSocket(mPort)
                start()
            } catch (e: BindException) {
                Log.e(TAG, "Port already in use !")
                postError(e, ERROR_BIND_FAILED)
                throw e
            }
        }

        override fun run() {
            super.run()
            Log.i(TAG, "RTSP server listening on port " + mServer.localPort)
            while (!interrupted()) {
                try {
                    WorkerThread(mServer.accept()).start()
                } catch (e: SocketException) {
                    break
                } catch (e: IOException) {
                    Log.e(TAG, e.message)
                    continue
                }

            }
            Log.i(TAG, "RTSP server stopped !")

        }

        fun kill() {
            try {
                /**
                 *  关闭sorket
                 * */
                mServer.close()
            } catch (e: IOException) {

            }
            try {
                /**
                 * 当前线程立即执行 其他线程等待
                 * */
                this.join()
            } catch (ignore: InterruptedException) {

            }
        }

    }


    class WorkerThread : Thread, Runnable {
        private var mClient: Socket
        private var mOutput: OutputStream
        private var mInput: BufferedReader
        private var mSession: Session

        constructor(socket: Socket) {
            this.mClient = socket
            mInput = BufferedReader(InputStreamReader(socket.getInputStream()))
            mOutput = socket.getOutputStream()
            mSession = Session()
        }

        override fun run() {
            super.run()
            //解析流
            var request: Request
            var response: Response
            while (!interrupted()) {
                try {
                    request = Request.parseRequest(mInput)
                } catch (e: SocketException) {
                    // Client has left
                    break
                } catch (e: Exception) {
                    // We don't understand the request :/
                    response = Response()
                    response.status = Response.STATUS_BAD_REQUEST
                }

            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        mPort = PreferenceUtils.getInt(context, KEY_PORT, mPort)
        mEnabled = PreferenceUtils.getBoolean(context, KEY_ENABLED, mEnabled)
        PreferenceUtils.getSp(context).registerOnSharedPreferenceChangeListener(this)
        start()
    }


    /**
     * 开启监听
     * */
    fun start() {
        if (!mEnabled || mRestart) stop()
        if (mEnabled && mListenerThread == null) {
            try {
                mListenerThread = RequestListener()
            } catch (e: Exception) {
                mListenerThread = null
            }
        }
        mRestart = false
    }


    /**
     * 停止
     * */
    fun stop() {
        if (mListenerThread != null) {
            try {
                mListenerThread!!.kill()
                for (session in mSessions.keys) {
                    if (session != null) {
                        if (session.isStreaming()) session.stop()
                    }
                }
            } catch (e: Exception) {
            } finally {
                mListenerThread = null
            }
        }
    }


    companion object {
        class Request {
            var method: String? = null
            var uri: String? = null
            var headers = HashMap<String, String>()

            companion object {
                // Parse method & uri
                val regexMethod = Pattern.compile("(\\w+) (\\S+) RTSP", Pattern.CASE_INSENSITIVE)
                // Parse a request header
                val rexegHeader = Pattern.compile("(\\S+):(.+)", Pattern.CASE_INSENSITIVE)

                fun parseRequest(reader: BufferedReader): Request {
                    var request = Request()
                    var line: String?
                    var matcher: Matcher
                    if (reader.readLine() == null) {
                        throw SocketException("Client Disconnect")
                    }
                    line = reader.readLine()
                    //解析rtsp协议域名
                    matcher = regexMethod.matcher(line!!)
                    matcher.find()
                    request.method = matcher.group(1)
                    request.uri = matcher.group(2)
                    while (true) {
                        line = readLine() ?: break
                        if (line.length <= 3) {
                            matcher = rexegHeader.matcher(line)
                            matcher.find()
                            request.headers[matcher.group(1)!!.toLowerCase(Locale.US)] =
                                matcher.group(2)
                        }
                    }

                    // Parsing request method & uri
//                    if ((line = input.readLine()) == null
//                    ) throw SocketException("Client disconnected")

//                    matcher.find()
//                    request.method = matcher.group(1)
//                    request.uri = matcher.group(2)
//
//                    // Parsing headers of the request
//                    while ((line = input.readLine()) != null && line!!.length > 3) {
//                        matcher = rexegHeader.matcher(line)
//                        matcher.find()
//                        request.headers.put(
//                            matcher.group(1)!!.toLowerCase(Locale.US),
//                            matcher.group(2)
//                        )
//                    }
//                    if (line == null) throw SocketException("Client disconnected")
//
//                    // It's not an error, it's just easier to follow what's happening in logcat with the request in red
//                    Log.e(TAG, request.method + " " + request.uri)
                    return request
                }
            }
        }

        class Response {
            // Status code definitions
            companion object {
                val STATUS_OK = "200 OK"
                val STATUS_BAD_REQUEST = "400 Bad Request"
                val STATUS_NOT_FOUND = "404 Not Found"
                val STATUS_INTERNAL_SERVER_ERROR = "500 Internal Server Error"
            }

            var status = STATUS_INTERNAL_SERVER_ERROR
            var content = ""
            var attributes = ""

            private var mRequest: Request? = null

            constructor(request: Request) {
                this.mRequest = request
            }

            constructor() {
                // Be carefull if you modify the send() method because request might be null !
                mRequest = null
            }
        }
    }
}