package com.example.camera.ui

import androidx.databinding.ViewDataBinding
import com.example.camera.BR
import com.example.camera.R
import com.zl.library.Base.BaseFragment
import com.zl.zlibrary.base.BaseViewModel


/**
 * 开始直播  推流
 * */
class LiveShowFragment : BaseFragment<ViewDataBinding, BaseViewModel>() {

    override fun initVariableId(): Int {
        return BR.live_model
    }

    override fun setContent(): Int {
        return R.layout.live_fragment
    }

}