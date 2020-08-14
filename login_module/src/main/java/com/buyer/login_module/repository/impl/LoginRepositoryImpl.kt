package com.buyer.login_module.repository.impl

import com.buyer.login_module.model.LoginModel
import com.buyer.login_module.repository.LoginRepository
import com.buyer.login_module.serivce.LoginService
import com.zl.library.Entity.PostEntity


class LoginRepositoryImpl(private val service: LoginService) : LoginRepository {

    override suspend fun login(query: PostEntity): LoginModel {
        return service.login(query)
    }

}