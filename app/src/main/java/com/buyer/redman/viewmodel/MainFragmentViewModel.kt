package com.buyer.redman.viewmodel

import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.fragment.app.Fragment
import com.zl.library.Base.BaseFragment
import com.zl.zlibrary.base.BaseViewModel
import com.zl.zlibrary.base.adapter.base.BindingViewPagerAdapter
import com.zl.zlibrary.base.adapter.base.ItemBinding
import com.zl.zlibrary.ext.loge


class MainFragmentViewModel : BaseViewModel() {

    override fun onSingleClick(id: Int) {
        super.onSingleClick(id)
        "onSingleClick".loge()
    }



}