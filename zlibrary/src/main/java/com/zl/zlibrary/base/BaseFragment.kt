package com.zl.library.Base

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.zl.zlibrary.base.*
import com.zl.zlibrary.base.activity.IBaseActivity
import com.zl.zlibrary.R
import com.zl.zlibrary.Utils.ClassUtil
import com.zl.zlibrary.bus.Even.ViewModelEvent
import com.zl.zlibrary.ext.loge
import com.zl.zlibrary.ext.observe
import com.zl.zlibrary.ext.parseState


abstract class BaseFragment<V : ViewDataBinding, VM : BaseViewModel> : Fragment() {


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
        _mActivity = activity as IBaseActivity
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
        lifecycle.addObserver(mViewModel!!)
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

    }

    fun onInvisible() {
    }


    private fun registerViewModelNotify() {
        observe(mViewModel!!.getNotifyLiveData()!!, Observer {
            //处理新增的通知
            doNotifyEven(it)
        })

        observe(mViewModel!!.netState!!, Observer {
            parseState(it,{

            }, {
                //登录失败
//                showMessage(it.errorMsg)
            })
        })
        //下面处理默认通知

        observe(mViewModel!!.getNotifyLiveData()!!.getStatusTextColorController()!!, Observer {
            _mActivity!!.setStatusBarTextColor(it as Boolean)
        })
        observe(mViewModel!!.getNotifyLiveData()!!.getPageStatus()!!, Observer {
            when (it) {
                BaseViewModel.NotifyLiveData.PageEnum.SHOW_CONTENT -> {
                    "显示主布局".loge()
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



    /**
     * 后期业务会有更多的viewmodel与view的交互，此处为自定义消息类型
     * */
    open fun doNotifyEven(it: String) {
           //处理自定义通知


    }

    fun showContent() {

        if (bindingView!!.root.visibility != View.VISIBLE) {
            bindingView!!.root.visibility = View.VISIBLE
        }
        // 停止动画
        if (mAnimationDrawable != null && mAnimationDrawable!!.isRunning) {
            mAnimationDrawable!!.stop()
        }
        if (errorView != null) {
            errorView!!.visibility = View.GONE
        }
        if (loadingView != null) {
            loadingView!!.visibility = View.GONE
        }
        if (emptyView != null) {
            emptyView!!.visibility = View.GONE
        }

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
        if (loadingView != null) {
            loadingView!!.visibility = View.GONE
        }
        if (emptyView != null) {
            emptyView!!.visibility = View.GONE
        }
        if (bindingView != null && bindingView!!.root.visibility != View.GONE) {
            bindingView!!.root.visibility = View.GONE
        }
        if (mAnimationDrawable != null && mAnimationDrawable!!.isRunning) {
            mAnimationDrawable!!.stop()
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
        if (errorView != null && errorView!!.visibility == View.VISIBLE) {
            errorView!!.visibility = View.GONE
        }

        if (bindingView != null) {
            bindingView!!.root.visibility = View.GONE
        }
        if (emptyView != null) {
            emptyView!!.visibility = View.GONE
        }
        // 开始动画
        if (mAnimationDrawable != null && !mAnimationDrawable!!.isRunning) {
            mAnimationDrawable!!.start()
        }
    }


    fun showEmptyView(text: String) {

        // 需要这样处理，否则二次显示会失败
        var viewStub = getView(R.id.vs_empty) as ViewStub
        if (viewStub != null) {
            emptyView = viewStub.inflate()
            emptyView!!.findViewById<TextView>(R.id.tv_tip_empty).text = text
        }
        // 停止动画
        if (mAnimationDrawable != null && mAnimationDrawable?.isRunning!!) {
            mAnimationDrawable?.stop()
        }
        if (errorView != null) {
            errorView!!.visibility = View.GONE
        }
        if (loadingView != null) {
            loadingView!!.visibility = View.GONE
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

        showLoading()
        initData()

    }

    open fun initData() {
        registerViewModelNotify()
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

     var _mActivity: IBaseActivity? = null


}