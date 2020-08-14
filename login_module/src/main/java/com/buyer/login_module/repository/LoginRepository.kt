package com.buyer.login_module.repository

import com.buyer.login_module.model.LoginModel
import com.zl.library.Entity.PostEntity


interface LoginRepository {


    suspend  fun  login(query:PostEntity):LoginModel

}