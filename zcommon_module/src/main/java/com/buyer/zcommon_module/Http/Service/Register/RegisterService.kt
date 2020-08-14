package com.buyer.zcommon_module.Http.Service.Register

import com.buyer.zcommon_module.Entity.UserInfo
import com.zl.zlibrary.manager.BaseResponse
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST





interface RegisterService{

    @Headers("Content-type:application/json;charset=utf-8")//需要添加touch
    @POST("AmoskiActivity/memberUser/login")
    fun login(@Body builder: RequestBody
    ): Observable<BaseResponse<UserInfo>>


}
