package com.example.camera.streaming.rtsp

import android.app.Service
import android.content.Intent
import android.content.SharedPreferences
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.example.camera.config.KEY_ENABLED
import com.example.camera.config.KEY_PORT
import com.example.camera.session.Session
import com.example.camera.session.SessionBuilder
import com.zl.zlibrary.Utils.PreferenceUtils
import com.zl.zlibrary.Utils.context
import com.zl.zlibrary.ext.loge
import java.io.*
import java.net.*
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


//rtsp服务端
class RtspServer : Service(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {

    }

    val TAG = "RtspServer"

    /** 服务名 */
    var SERVER_NAME = "RTSP Server"

    /**RTSP默认端口  */
    val DEFAULT_RTSP_PORT = 8086

    /** 绑定失败，已有连接  */
    val ERROR_BIND_FAILED = 0x00

    /** 无法开启流.  */
    val ERROR_START_FAILED = 0x01

    /** 开始推流 */
    val MESSAGE_STREAMING_STARTED = 0X00

    /** 停止  */
    val MESSAGE_STREAMING_STOPPED = 0X01



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
    protected fun postMessage(id: Int) {
        synchronized(mListeners) {
            if (mListeners.size > 0) {
                for (cl in mListeners) {
                    cl.onMessage(this, id)
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

    /**
     * 默认情况下，RTSP使用[UriParser]解析客户端请求的URI
     * 也可以通过重写此方法来更改该行为。
     * @param uri  请求的Uri
     * @param client 与客户端关联的套接字
     * @return A proper session
     */
    @Throws(IllegalStateException::class, IOException::class)
    protected fun handleRequest(uri: String, client: Socket): Session {
        var session = UriParser.parse(uri)
        session.setOrigin(client.localAddress.hostAddress)
        if (session.mDestination == null) {
            session.setDestination(client.inetAddress.hostAddress)
        }
        return session
    }
    fun isStreaming(): Boolean {
        for (session in mSessions.keys) {
            if (session != null) {
                if (session.isStreaming()) return true
            }
        }
        return false
    }

    inner class WorkerThread : Thread, Runnable {
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
            var request: Request = null!!
            var response: Response = null!!
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
                /**
                 * 相应地执行一些操作，如启动流、发送会话描述
                 * */
                if (request != null) {
                    try {
                        response = processRequest(request)
                    } catch (e: Exception) {
                        /**
                         * 这会提醒主线程该线程中出现了错误
                         * */
                        postError(e, ERROR_START_FAILED)
                        Log.e(TAG, if (e.message != null) e.message else "An error occurred")
                        e.printStackTrace()
                        response = Response(request)
                    }
                }
            }

        }

        fun processRequest(request: Request): Response {
            var response = Response(request)
            /***
             * 处理响应体
             */
            /* ********************************************************************************** */
            /* ********************************* Method DESCRIBE ******************************** */
            /* ********************************************************************************** */
            if (request.method.equals("DESCRIBE", ignoreCase = true)) run {
                // Parse the requested URI and configure the session
                /**
                 * S->C
                 *  RTSP/1.0 200 OK
                CSeq: 2
                Content-Base: rtsp://example.com/media.mp4
                Content-Type: application/sdp
                Content-Length: 460
                m=video 0 RTP/AVP 96
                a=control:streamid=0
                a=range:npt=0-7.741000
                a=length:npt=7.741000
                a=rtpmap:96 MP4V-ES/5544
                a=mimetype:string;"video/MP4V-ES"
                a=AvgBitRate:integer;304018
                a=StreamName:string;"hinted video track"
                m=audio 0 RTP/AVP 97
                a=control:streamid=1
                a=range:npt=0-7.712000
                a=length:npt=7.712000
                a=rtpmap:97 mpeg4-generic/32000/2
                a=mimetype:string;"audio/mpeg4-generic"
                a=AvgBitRate:integer;65790
                a=StreamName:string;"hinted audio track"
                 * */
                mSession = handleRequest(request.uri!!, mClient)
                mSessions[mSession] = null
                mSession.syncConfigure()

                val requestContent = mSession.getSessionDescription()
                val requestAttributes =
                    "Content-Base: " + mClient.localAddress.hostAddress + ":" + mClient.localPort + "/\r\n" +
                            "Content-Type: application/sdp\r\n"

                response.attributes = requestAttributes
                response.content = requestContent

                // If no exception has been thrown, we reply with OK
                response.status = Response.STATUS_OK

            } else if (request.method.equals("OPTIONS", ignoreCase = true)) run {

                /***
                 *
                 * RTSP/1.0 200 OK
                   CSeq: 1
                   Public: DESCRIBE, SETUP, TEARDOWN, PLAY, PAUSE
                 */
                response.status = Response.STATUS_OK
                response.attributes = "Public: DESCRIBE,SETUP,TEARDOWN,PLAY,PAUSE\r\n"
                response.status = Response.STATUS_OK
            } else if (request.method.equals("SETUP", ignoreCase = true)) run {

                /**
                 *
                 *
                 * S->C: RTSP/1.0 200 OK
                 *       CSeq: 3
                 *       Transport: RTP/AVP;unicast;client_port=8000-8001;server_port=9000-9001;ssrc=1234ABCD
                 *       Session: 12345678
                 *       */

                var p: Pattern
                var m: Matcher
                var p2: Int
                var p1: Int
                var ssrc: Int
                var trackId: Int
                var src: IntArray
                var destination: String

                p = Pattern.compile("trackID=(\\w+)", Pattern.CASE_INSENSITIVE)
                m = p.matcher(request.uri)

                if (!m.find()) {
                    response.status = Response.STATUS_BAD_REQUEST
                    return response
                }

                trackId = Integer.parseInt(m.group(1)!!)

                if (!mSession.trackExists(trackId)) {
                    response.status = Response.STATUS_NOT_FOUND
                    return response
                }

                p = Pattern.compile("client_port=(\\d+)-(\\d+)", Pattern.CASE_INSENSITIVE)
                m = p.matcher(request.headers["transport"]!!)

                if (!m.find()) {
                    val ports = mSession.getTrack(trackId)!!.getDestinationPorts()
                    p1 = ports[0]
                    p2 = ports[1]
                } else {
                    p1 = Integer.parseInt(m.group(1)!!)
                    p2 = Integer.parseInt(m.group(2)!!)
                }


                //获取SSRC
                ssrc = mSession.getTrack(trackId)!!.getSSRC()


                //获取服务端口
                src = mSession.getTrack(trackId)!!.getLocalPorts()
                destination = mSession.mDestination!!


                //设置目标客户端端口
                mSession.getTrack(trackId)!!.setDestinationPorts(p1, p2)

                var streaming = isStreaming()


                mSession.syncStart(trackId)
                if (!streaming && isStreaming()) {
                    postMessage(MESSAGE_STREAMING_STARTED)
                }

                response.attributes =
                    "Transport: RTP/AVP/UDP;" + (if (InetAddress.getByName(destination).isMulticastAddress) "multicast" else "unicast") +
                            ";destination=" + mSession.mDestination +
                            ";client_port=" + p1 + "-" + p2 +
                            ";server_port=" + src[0] + "-" + src[1] +
                            ";ssrc=" + Integer.toHexString(ssrc) +
                            ";mode=play\r\n" +
                            "Session: " + "1185d20035702ca" + "\r\n" +
                            "Cache-Control: no-cache\r\n"

                response.status = Response.STATUS_OK

                // If no exception has been thrown, we reply with OK
                response.status = Response.STATUS_OK



            }





            return response
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
                    if (line == null) throw SocketException("Client disconnected")

                    (request.method + " " + request.uri).loge(this.javaClass.simpleName)
                    return request
                }
            }
        }

        class Response {
            // 定义状态值
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
                mRequest = null
            }
        }
    }
}