package com.buyer.zcommon_module.Http

import com.buyer.zcommon_module.Entity.UserInfo


interface getUserInfo_Inf{

    fun getUserInfoSuccess(info:UserInfo)

    fun getUserInfoError(it:Throwable)

}