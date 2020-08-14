package com.buyer.redman

import android.content.Context
import android.content.Intent
import com.buyer.login_module.inject.loginModule
import com.buyer.redman.Service.InitService
import com.zl.zlibrary.base.BaseApplication
import com.zl.zlibrary.base.baseModule
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule


class AppInstance : BaseApplication(), KodeinAware {


    override val kodein = Kodein.lazy {
        import(androidXModule(this@AppInstance))
        import(baseModule)
        import(loginModule)
//        importAll(FeatureManager.kodeinModules)
//        externalSources.add(FragmentArgsExternalSource())
    }

    val TAG = javaClass.simpleName

    val instance: AppInstance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        AppInstance()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }

    override fun doCreated() {
        super.doCreated()
        startService(Intent(this, InitService::class.java))
    }


}