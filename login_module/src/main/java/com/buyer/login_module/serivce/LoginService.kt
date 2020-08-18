package com.buyer.login_module.serivce

import com.buyer.login_module.model.LoginModel
import com.zl.library.Entity.PostEntity
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


interface  LoginService{


    @Headers("Content-type:application/json;charset=utf-8")//需要添加touch
    @POST("AmoskiActivity/memberUser/login")
    suspend fun login(@Body builder: PostEntity
    ): LoginModel

}