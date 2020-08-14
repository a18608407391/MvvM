package com.buyer.zcommon_module.Http

import com.buyer.zcommon_module.Entity.BaseRequestParam
import com.google.gson.Gson
import com.zl.library.Config.Base_URL
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.io.InputStream
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

class NetWorkManager {

    var type = "application/json;charset=utf-8".toMediaType()

    companion object {
        val instance: NetWorkManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            NetWorkManager()
        }
    }

    fun getOkHttpClient(): OkHttpClient? {
        var loggingInterceptor =
            HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT)
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    fun getOkHttpClientWithToken(token: String): OkHttpClient? {
        var loggingInterceptor =
            HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT)
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }


    fun setCertificates(vararg certificates: InputStream?) {
        try {
            var certificateFactory =
                CertificateFactory.getInstance("X.509")
            var keyStore =
                KeyStore.getInstance(KeyStore.getDefaultType())
            keyStore.load(null)
            for ((index, certificate) in certificates.withIndex()) {
                var certificateAlias = Integer.toString(index)
                keyStore.setCertificateEntry(
                    certificateAlias,
                    certificateFactory.generateCertificate(certificate)
                )
                try {
                    certificate?.close()
                } catch (e: IOException) {
                }
            }
            val sslContext = SSLContext.getInstance("TLS")
            val trustManagerFactory =
                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            trustManagerFactory.init(keyStore)
            sslContext.init(
                null,
                trustManagerFactory.trustManagers,
                SecureRandom()
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    var manager = object : X509TrustManager {
        override fun getAcceptedIssuers(): Array<out java.security.cert.X509Certificate>? {
            return arrayOf()
        }

        @Throws(CertificateException::class)
        override fun checkClientTrusted(
            chain: Array<java.security.cert.X509Certificate>,
            authType: String
        ) {
        }

        @Throws(CertificateException::class)
        override fun checkServerTrusted(
            chain: Array<java.security.cert.X509Certificate>,
            authType: String
        ) {
        }
    }

    fun getOkHttpsClient(): OkHttpClient? {
        var loggingInterceptor =
            HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT)
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        var sslSocketFactory: SSLSocketFactory
        val trustAllCerts = arrayOf<TrustManager>(manager)
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, SecureRandom())
        sslSocketFactory = sslContext.socketFactory
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            .hostnameVerifier(HostnameVerifier { hostname, session -> true })
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    fun getOkHttpRetrofit(): Retrofit? {
        return Retrofit.Builder()
            .client(getOkHttpClient()).baseUrl(Base_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    }

    fun getOkHttpsRetrofit(): Retrofit? {
        return Retrofit.Builder()
            .client(getOkHttpsClient()).baseUrl(Base_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    }

    fun getBaseRequestBody(map: BaseRequestParam): RequestBody? {
        return RequestBody.create(
            type,
            Gson().toJson(map)
        )
    }

    fun getBaseRequestBodyAny(map: HashMap<String, Any>): RequestBody? {
        return RequestBody.create(
            type,
            Gson().toJson(map)
        )
    }

}