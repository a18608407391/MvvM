package com.zl.zlibrary.base

import com.zl.zlibrary.manager.NavManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 *
 * 初始化基础组件
 * */
internal const val MODULE_NAME = "Base"

const val BaseUrl = ""

const val HTTP_CLIENT_MODULE_TAG = "httpClientModule"

const val TIME_OUT_SECONDS = 30L

val baseModule = Kodein.Module("${MODULE_NAME}Module") {
    bind() from singleton { NavManager() }

    bind<HttpLoggingInterceptor>() with singleton {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }
    bind<OkHttpClient>() with singleton {
        instance<OkHttpClient.Builder>()  // 委托给bind<OkHttpClient.Builder>()函数
            .connectTimeout(TIME_OUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIME_OUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TIME_OUT_SECONDS, TimeUnit.SECONDS)
            .addInterceptor(instance<HttpLoggingInterceptor>())
            .build()
    }
    bind<Retrofit.Builder>() with singleton { Retrofit.Builder() }

    bind<OkHttpClient.Builder>() with singleton { OkHttpClient.Builder() }

    bind<Retrofit>() with singleton {
        instance<Retrofit.Builder>()
            .baseUrl(BaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(instance())
            .build()
    }
}