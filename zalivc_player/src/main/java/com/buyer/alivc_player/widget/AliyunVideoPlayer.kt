package com.buyer.alivc_player.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.aliyun.player.IPlayer
import com.buyer.alivc_player.theme.ITheme
import com.buyer.alivc_player.View.controller.ControlView


/**
* 说明：
* 作者：左麟
* 包名：com.buyer.alivc_player.View
* 类名：Player
* 添加时间：2020/6/5 0005 15:21
* 功能归属：阿里播放器
* 主要作用：直播、点播
*/

class AliyunVideoPlayer @JvmOverloads constructor(context:Context, atts: AttributeSet, def:Int)
    :RelativeLayout(context,atts,0), ITheme {
    //对外的各种事件监听
    private val mOutInfoListener: IPlayer.OnInfoListener? = null
    private val mOutErrorListener: IPlayer.OnErrorListener? = null
    private var mOutPreparedListener: IPlayer.OnPreparedListener? = null
    private var mOutCompletionListener: IPlayer.OnCompletionListener? = null
    private var mOuterSeekCompleteListener: IPlayer.OnSeekCompleteListener? = null
    private var mOutFirstFrameStartListener: IPlayer.OnRenderingStartListener? = null
    //    private IPlayer.OnRePlayListener mOutRePlayListener = null;
    //    private IPlayer.OnUrlTimeExpiredListener mOutUrlTimeExpiredListener = null;

    private var mControllerListener : PlayerController?  =  null


//    private var mOutAutoPlayListener: OnAutoPlayListener? = null
//    private var mOutChangeQualityListener: OnChangeQualityListener? = null
//    private var mOnScreenCostingSingleTagListener: OnScreenCostingSingleTagListener? = null
//    private var mOnScreenBrightnessListener: OnScreenBrightnessListener? = null
//    private var mOutTimeExpiredErrorListener: OnTimeExpiredErrorListener? = null
    //对外view点击事件监听
//    private var mOnPlayerViewClickListener: OnPlayerViewClickListener? = null
    // 连网断网监听
    private var mNetConnectedListener: NetConnectedListener? = null
    // 横屏状态点击更多
    private var mOutOnShowMoreClickListener: ControlView.OnShowMoreClickListener? = null
    //播放按钮点击监听
//    private var onPlayStateBtnClickListener: OnPlayStateBtnClickListener? = null
    //停止按钮监听
//    private var mOnStoppedListener: OnStoppedListener? = null
    /**
     * 对外SEI消息通知
     */

    private val mOutSeiDataListener: IPlayer.OnSeiDataListener? = null


    init {

        initView()
    }

    private fun initView() {
        //初始化播放用的surfaceView
        initSurfaceView()
        //初始化播放器
        initAliVcPlayer()
        //初始化封面
        initCoverView()
        //初始化手势view
        initGestureView()
        //初始化控制栏
        initControlView()
        //初始化清晰度view
        initQualityView()
        //初始化缩略图
        initThumbnailView()
        //初始化倍速view
        initSpeedView()
        //初始化指引view
        initGuideView()
        //初始化提示view
        initTipsView()
        //初始化网络监听器
        initNetWatchdog()
        //初始化屏幕方向监听
        initOrientationWatchdog()
        //初始化手势对话框控制
        initGestureDialogManager()
        //默认为蓝色主题
        setTheme(Theme.Blue)
        //先隐藏手势和控制栏，防止在没有prepare的时候做操作。
        hideGestureAndControlViews()
    }

    private fun hideGestureAndControlViews() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun initGestureDialogManager() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun initOrientationWatchdog() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun initNetWatchdog() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun initTipsView() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun initGuideView() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun initSpeedView() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun initThumbnailView() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun initQualityView() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun initControlView() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun initGestureView() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun initCoverView() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun initAliVcPlayer() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun initSurfaceView() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    interface PlayerController{

        fun onAutoPlayStarted()

        fun onChangeQualitySuccess(quality: String)

        fun onChangeQualityFail(code: Int, msg: String)

        fun onScreenCostingSingleTag()

        fun onScreenBrightness(brightness:Int)

        fun showMoreClick()

        fun  onTimeExpiredError()

        //播放按钮点击监听
        fun onPlayBtnClick(playerState: Int)

        //停止按钮监听
        fun onStoped()

        //对外view点击事件监听
        fun onPlayerViewClick(screenMode: AliyunScreenMode, viewType: PlayViewType)
    }

    interface NetConnectedListener {
        /**
         * 网络已连接
         */
        fun onReNetConnected(isReconnect: Boolean)
        /**
         * 网络未连接
         */
        fun onNetUnConnected()
    }


    //设置主题
    override fun setTheme(theme: Theme) {
        val childCounts = childCount
        for (i in 0 until childCounts) {
            val view = getChildAt(i)
            if (view is ITheme) {
                (view as ITheme).setTheme(theme)
            }
        }
    }
    enum class Theme{
        /**
         * 蓝色主题
         */
        Blue,
        /**
         * 绿色主题
         */
        Green,
        /**
         * 橙色主题
         */
        Orange,
        /**
         * 红色主题
         */
        Red
    }

}