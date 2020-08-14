package com.zl.zlibrary.base.activity

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.zl.zlibrary.base.BaseViewModel
import com.zl.zlibrary.R
import com.zl.zlibrary.Utils.ClassUtil
import com.zl.zlibrary.databinding.ActivityBaseBinding


abstract class BaseActivity<V : ViewDataBinding, VM : BaseViewModel> : BaseActivityImpl(){

    var mBaseBinding: ActivityBaseBinding? = null

    var mAnimationDrawable: AnimationDrawable? = null

    var bindingView: V? = null

    var loadingView: View? = null

    var errorView: View? = null

    var viewModel: VM? = null

    var viewModelId: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(initContentView())
    }

    abstract fun initContentView(): Int
    abstract fun initVariableId(): Int

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        mBaseBinding =
            DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_base, null, false)
        bindingView = DataBindingUtil.inflate(layoutInflater, layoutResID, null, false);
        bindingView!!.lifecycleOwner = this
        var params = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        viewModelId = initVariableId()
        bindingView?.root!!.layoutParams = params
        val mContainer = mBaseBinding?.root!!.findViewById(R.id.container) as RelativeLayout
        mContainer.addView(bindingView?.root)
        window.setContentView(mBaseBinding?.root)
        loadingView = (findViewById<ViewStub>(R.id.vs_loading)).inflate()
        var img = loadingView?.findViewById<ImageView>(R.id.img_progress)
        // 加载动画
        mAnimationDrawable = img!!.getDrawable() as AnimationDrawable?
        // 默认进入页面就开启动画
        if (!mAnimationDrawable!!.isRunning) {
            mAnimationDrawable!!.start()
        }

        bindingView!!.root.visibility = View.GONE

        initViewModel()
        showContentView()
        bindingView!!.setVariable(viewModelId, viewModel)
        initData()
    }


    open fun initData() {
        registerViewModelNotify()
    }

    private fun registerViewModelNotify() {
        viewModel!!.getNotifyLiveData()!!.getStatusTextColorController()!!.observe(this, Observer {
            setStatusBarTextColor(it)
        })
        viewModel!!.getNotifyLiveData()!!.getPageStatus()!!.observe(this, Observer {
                when(it){
                    BaseViewModel.NotifyLiveData.PageEnum.SHOW_CONTENT ->{
                        showContentView()
                    }
                    BaseViewModel.NotifyLiveData.PageEnum.SHOW_LOADING ->{
                        showLoading()
                    }
                    BaseViewModel.NotifyLiveData.PageEnum.SHOW_ERROR ->{
                        showError()
                    }
                    BaseViewModel.NotifyLiveData.PageEnum.SHOW_EMPTY ->{

                    }
                }
        })
    }

    private fun initViewModel() {
        val viewModelClass = ClassUtil.getViewModel<VM>(this)
        if (viewModelClass != null) {
            this.viewModel = ViewModelProvider.NewInstanceFactory().create(viewModelClass)
        }
    }

    open fun showLoading() {
        if (loadingView != null && loadingView?.visibility != View.VISIBLE) {
            loadingView?.visibility = View.VISIBLE
        }
        // 开始动画
        if (!mAnimationDrawable?.isRunning!!) {
            mAnimationDrawable?.start()
        }
        if (bindingView?.root!!.visibility !== View.GONE) {
            bindingView?.root?.visibility = View.GONE
        }
        if (errorView != null) {
            errorView?.visibility = View.GONE
        }
    }

    open fun showContentView() {
        if (loadingView != null && loadingView?.visibility != View.GONE) {
            loadingView?.visibility = View.GONE
        }
        // 停止动画
        if (mAnimationDrawable?.isRunning!!) {
            mAnimationDrawable?.stop()
        }
        if (errorView != null) {
            errorView?.visibility = View.GONE
        }
        if (bindingView?.root?.visibility !== View.VISIBLE) {
            bindingView?.root?.visibility = View.VISIBLE
        }
    }

    open fun showError() {
        if (loadingView != null && loadingView?.visibility != View.GONE) {
            loadingView?.visibility = View.GONE
        }
        // 停止动画
        if (mAnimationDrawable?.isRunning()!!) {
            mAnimationDrawable?.stop()
        }
//        showLayoutType.set(2)
        if (errorView == null) {
            val viewStub = findViewById<ViewStub>(R.id.vs_error_refresh)
            errorView = viewStub.inflate()
            // 点击加载失败布局
            errorView?.setOnClickListener {
                showLoading()
            }
        } else {
            errorView?.visibility = View.VISIBLE
        }
        if (bindingView?.root?.visibility !== View.GONE) {
            bindingView?.root?.visibility = View.GONE
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(viewModel!!)
        viewModel!!.removeRxBus()
        viewModel = null
        bindingView?.unbind()
    }

}