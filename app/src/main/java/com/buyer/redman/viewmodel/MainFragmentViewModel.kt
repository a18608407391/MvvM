package com.buyer.redman.viewmodel

import android.util.Log
import com.zl.zlibrary.base.BaseViewModel
import com.zl.zlibrary.ext.loge


class MainFragmentViewModel : BaseViewModel() {


    override fun onSingleClick(id: Int) {
        super.onSingleClick(id)

        "onSingleClick".loge()

    }



}