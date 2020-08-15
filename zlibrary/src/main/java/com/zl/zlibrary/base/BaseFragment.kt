package com.zl.library.Base

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.zl.zlibrary.BR
import com.zl.zlibrary.base.*
import com.zl.zlibrary.base.activity.IBaseActivity
import com.zl.zlibrary.tools.ViewState
import com.zl.zlibrary.tools.ViewState.Companion.SHOW_CONTENT
import com.zl.zlibrary.tools.ViewState.Companion.SHOW_EMPTY
import com.zl.zlibrary.tools.ViewState.Companion.SHOW_ERROR
import com.zl.zlibrary.tools.ViewState.Companion.SHOW_LOADING
import com.zl.zlibrary.R
import com.zl.zlibrary.Utils.ClassUtil


abstract class BaseFragment<V : ViewDataBinding, VM : BaseViewModel> : Fragment() {


    var state = ViewState()
    var bindingView: V? = null
    var viewModelId = 0
    var mViewModel: VM? = null
    var mIsVisible = false
    // 加载中
    var loadingView: View? = null
    // 加载失败
    var errorView: View? = null
    // 空布局
    var emptyView: View? = null
    var mAnimationDrawable: AnimationDrawable? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var base =
            DataBindingUtil.inflate<ViewDataBinding>(inflater, R.layout.fragment_base, null, false)
        var inflate = base.root
        bindingView = DataBindingUtil.inflate(inflater, setContent(), null, false)
        var params = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        bindingView?.root!!.layoutParams = params
        viewModelId = initVariableId()
        mViewModel = initViewModel()
        bindingView?.setVariable(viewModelId, mViewModel)
        bindingView?.root?.visibility = View.GONE
        loadingView = (inflate.findViewById<ViewStub>(R.id.vs_loading)).inflate()
        errorView = (inflate.findViewById<ViewStub>(R.id.vs_error_refresh)).inflate()
        errorView!!.findViewById<LinearLayout>(R.id.reload_layout).setOnClickListener {
            mViewModel!!.initData(mViewModel!!.requestParameter!!)
        }
        var img = loadingView?.findViewById<ImageView>(R.id.img_progress)
        // 加载动画
        mAnimationDrawable = img!!.getDrawable() as AnimationDrawable?
        // 默认进入页面就开启动画
        if (!mAnimationDrawable!!.isRunning) {
            mAnimationDrawable!!.start()
        }
        lifecycle.addObserver(mViewModel!!)
        base.setVariable(BR.show_type, state)
        var mContainer = inflate.findViewById<RelativeLayout>(R.id.container)
        mContainer.addView(bindingView?.root)

        return inflate
    }


    abstract fun initVariableId(): Int

    abstract fun setContent(): Int


    fun initViewModel(): VM {
        val viewModelClass: Class<VM> = ClassUtil.getViewModel(this)
        if (viewModelClass != null) {
            return ViewModelProvider.NewInstanceFactory().create(viewModelClass)
        }
        return null!!
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (getUserVisibleHint()) {
            mIsVisible = true
            onVisible()
        } else {
            mIsVisible = false
            onInvisible()
        }
    }

    private fun onVisible() {
        loadData()
    }

    fun loadData() {
        registerViewModelNotify()
    }

    fun onInvisible() {
    }

    private fun registerViewModelNotify() {
        mViewModel!!.getNotifyLiveData()!!.getStatusTextColorController()!!.observe(this, Observer {
            _mActivity!!.setStatusBarTextColor(it)
        })

        mViewModel!!.getNotifyLiveData()!!.getPageStatus()!!.observe(this, Observer {
            when (it) {
                BaseViewModel.NotifyLiveData.PageEnum.SHOW_CONTENT -> {
                    showContent()
                }
                BaseViewModel.NotifyLiveData.PageEnum.SHOW_LOADING -> {
                    showLoading()
                }
                BaseViewModel.NotifyLiveData.PageEnum.SHOW_ERROR -> {
                    showError()
                }
                BaseViewModel.NotifyLiveData.PageEnum.SHOW_EMPTY -> {
                    showEmptyView("未找到該頁面")
                }
            }
        })
    }

    fun showContent() {
        if (bindingView!!.root.visibility != View.VISIBLE) {
            bindingView!!.root.visibility = View.VISIBLE
        }
        // 停止动画
        if (mAnimationDrawable != null && mAnimationDrawable!!.isRunning) {
            mAnimationDrawable!!.stop()
        }

        state.showLayoutType.set(SHOW_CONTENT)
    }

    fun showError() {
        var viewStub = getView(R.id.vs_error_refresh) as ViewStub
        if (viewStub != null) {
            errorView = viewStub.inflate()
            // 点击加载失败布局
            errorView?.setOnClickListener {
                showLoading()
                onRefresh()
            }
        }
        state.showLayoutType.set(SHOW_ERROR)
        if (mAnimationDrawable != null && mAnimationDrawable!!.isRunning) {
            mAnimationDrawable!!.stop()
        }
        if (bindingView?.root?.visibility != View.GONE) {
            bindingView?.root?.visibility = View.GONE
        }
    }

    private fun onRefresh() {
    }

    private fun showLoading() {

        var viewStub = getView(R.id.vs_loading) as ViewStub
        if (viewStub != null) {
            loadingView = viewStub.inflate()
            var img = loadingView?.findViewById<ImageView>(R.id.img_progress);
            mAnimationDrawable = img?.drawable as AnimationDrawable
        }
        state.showLayoutType.set(SHOW_LOADING)
        // 开始动画
        if (mAnimationDrawable != null && !mAnimationDrawable!!.isRunning) {
            mAnimationDrawable!!.start()
        }
        if (bindingView?.root?.visibility != View.GONE) {
            bindingView?.root?.visibility = View.GONE;
        }
    }


    fun showEmptyView(text: String) {

        // 需要这样处理，否则二次显示会失败
        var viewStub = getView(R.id.vs_empty) as ViewStub
        if (viewStub != null) {
            emptyView = viewStub.inflate()
            emptyView!!.findViewById<TextView>(R.id.tv_tip_empty).text = text
        }
        state.showLayoutType.set(SHOW_EMPTY)
        // 停止动画
        if (mAnimationDrawable != null && mAnimationDrawable?.isRunning!!) {
            mAnimationDrawable?.stop()
        }

        if (bindingView != null && bindingView!!.root.visibility != View.GONE) {
            bindingView?.root?.visibility = View.GONE
        }
    }

    fun getView(id: Int): View? {
        return view?.findViewById(id)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }


    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initData()

    }

    open fun initData() {

    }

    @CallSuper
    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    @CallSuper
    override fun onStart() {
        super.onStart()
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
    }

    @CallSuper
    override fun onPause() {
        super.onPause()
    }


    @CallSuper
    override fun onStop() {
        super.onStop()
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
    }

    @CallSuper
    override fun onDetach() {
        super.onDetach()
    }

    protected var _mActivity: IBaseActivity? = null


}