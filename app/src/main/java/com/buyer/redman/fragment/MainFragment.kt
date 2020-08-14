package com.buyer.redman.fragment

import com.buyer.redman.BR
import com.buyer.redman.R
import com.buyer.redman.databinding.FragmentMainBinding
import com.buyer.redman.viewmodel.MainFragmentViewModel
import com.zl.library.Base.BaseFragment


class MainFragment : BaseFragment<FragmentMainBinding, MainFragmentViewModel>(){
    override fun initVariableId(): Int {
        return BR.fragment_main_model
    }

    override fun setContent(): Int {
        return R.layout.fragment_main
    }


    override fun initData() {
        super.initData()
        showContent()
    }
}