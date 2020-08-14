package com.buyer.zcommon_module.Http

import com.buyer.zcommon_module.Entity.BaseRequestParam
import com.buyer.zcommon_module.Entity.UserInfo
import com.buyer.zcommon_module.Http.Service.Register.RegisterService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class HttpRequest {

    val instance: HttpRequest by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        HttpRequest()
    }


    var userInfoResult: getUserInfo_Inf? = null


    fun getUserInfo(param: BaseRequestParam) {

        NetWorkManager.instance.getOkHttpRetrofit()!!.create(RegisterService::class.java).login(
            NetWorkManager.instance.getBaseRequestBody(param)!!
        )
            .map {
                return@map it.data as UserInfo
            }?.subscribeOn(Schedulers.io())!!.observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseObserver<UserInfo>() {
                override fun onNext(t: UserInfo) {
                    super.onNext(t)
                    if (userInfoResult != null) {
                        userInfoResult!!.getUserInfoSuccess(t)
                    }
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    if (userInfoResult != null) {
                        userInfoResult!!.getUserInfoError(e)
                    }
                }
            })
    }


}