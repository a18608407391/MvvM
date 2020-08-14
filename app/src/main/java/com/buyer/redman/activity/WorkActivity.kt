package com.buyer.redman.activity

import com.buyer.redman.BR
import com.buyer.redman.R
import com.buyer.redman.databinding.ActivityWorkBinding
import com.buyer.redman.viewmodel.WorkViewModel
import com.buyer.zcommon_module.Entity.BaseRequestParam
import com.zl.zlibrary.base.activity.BaseActivity


class WorkActivity: BaseActivity<ActivityWorkBinding, WorkViewModel>(){


    override fun initContentView(): Int {
        return R.layout.activity_work
    }
    override fun initVariableId(): Int {
        
       var request =  BaseRequestParam.RequestBuilder().setId(0).build()
        return BR.work_viewmodel
    }
    override fun initData() {
        super.initData()
    }



}