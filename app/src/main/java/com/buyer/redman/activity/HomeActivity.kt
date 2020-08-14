package com.buyer.redman.activity

import com.alibaba.android.arouter.facade.annotation.Route
import com.buyer.redman.BR
import com.buyer.redman.R
import com.buyer.redman.databinding.ActivityHomeBinding
import com.buyer.redman.viewmodel.HomeActivityViewModel
import com.buyer.zcommon_module.Utils.RouteUtils

import com.zl.zlibrary.base.activity.BaseActivity
import com.zl.zlibrary.Utils.StatusbarUtils


/**
 * 说明：
 * 作者：左麟
 * 包名：com.buyer.redman
 * 类名：
 * 添加时间：2020/6/4 0004 10:19
 * 功能归属：
 * 主要作用：
 */


@Route(path = RouteUtils.ActivityPath.HomePath)
class HomeActivity : BaseActivity<ActivityHomeBinding, HomeActivityViewModel>(){
    override fun initContentView(): Int {
        StatusbarUtils.setTranslucentStatus(this)
        StatusbarUtils.setStatusBarMode(this, false, 0x00000000)
        setStatusBarTextColor(true)
        return R.layout.activity_home
    }

    override fun initVariableId(): Int {
        return BR.home_activity_viewmodel
    }

    override fun initData() {
        super.initData()
    }
}