package com.buyer.redman.fragment

import com.buyer.redman.BR
import com.buyer.redman.R
import com.buyer.redman.databinding.FragmentMainBinding
import com.buyer.redman.viewmodel.MainFragmentViewModel
import com.zl.library.Base.BaseFragment
import com.zl.library.Config.uiContext
import com.zl.zlibrary.annotation.inject.AnnotationInject
import com.zl.zlibrary.base.BaseViewModel
import com.zl.zlibrary.ext.NOTIFY_VIEW_STATE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainFragment : BaseFragment<FragmentMainBinding, MainFragmentViewModel>() {
    override fun initVariableId(): Int {
        return BR.fragment_main_model
    }

    override fun setContent(): Int {
        return R.layout.fragment_main
    }


    override fun initData() {
        super.initData()
        AnnotationInject.inject(mViewModel)
        CoroutineScope(uiContext).launch {
            delay(1000)
            mViewModel!!.setPageState(BaseViewModel.NotifyLiveData.PageEnum.SHOW_CONTENT)
        }
    }
}
