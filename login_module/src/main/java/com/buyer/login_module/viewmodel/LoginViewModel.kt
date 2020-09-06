package com.buyer.login_module.viewmodel

import androidx.lifecycle.MutableLiveData
import com.buyer.login_module.model.LoginModel
import com.buyer.login_module.repository.impl.LoginRepositoryImpl
import com.zl.library.Entity.PostEntity
import com.zl.zlibrary.base.BaseViewModel
import com.zl.zlibrary.ext.request
import com.zl.zlibrary.tools.NetState
import org.kodein.di.generic.instance


class LoginViewModel : BaseViewModel() {


    private val loginRepositoryImpl: LoginRepositoryImpl by instance()


    override fun initData(postEntity: PostEntity?) {
        super.initData(postEntity)
        request(
            {
                loginRepositoryImpl.login(postEntity!!)
            }, netState, true, "正在加载中......"
        )
    }

}