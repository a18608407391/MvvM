package com.buyer.login_module.inject

import com.buyer.login_module.repository.impl.LoginRepositoryImpl
import com.buyer.login_module.serivce.LoginService
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import retrofit2.Retrofit


val loginModule = Kodein.Module("login_module_kodein") {


    /***
     *
     */
    bind() from singleton { instance<Retrofit>().create(LoginService::class.java) }

    bind() from singleton { LoginRepositoryImpl(instance()) }


}