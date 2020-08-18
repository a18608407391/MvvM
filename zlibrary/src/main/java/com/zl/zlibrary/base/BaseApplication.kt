package com.zl.zlibrary.base

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.multidex.MultiDexApplication
import com.zk.library.Base.AppManager


open class BaseApplication : MultiDexApplication(), ViewModelStoreOwner {

    private lateinit var mAppViewModelStore: ViewModelStore
    override fun getViewModelStore(): ViewModelStore {
        return mAppViewModelStore
    }

    companion object {
        lateinit var instanc: BaseApplication
        @Synchronized
        fun setApplication(baseApplication: BaseApplication) {
            instanc = baseApplication
            baseApplication.registerActivityLifecycleCallbacks(object :
                Application.ActivityLifecycleCallbacks {
                override fun onActivityPaused(activity: Activity?) {
                }

                override fun onActivityResumed(activity: Activity?) {
                }

                override fun onActivityStarted(activity: Activity?) {
                }

                override fun onActivityDestroyed(activity: Activity?) {
                    AppManager.get()!!.removeActivity(activity)
                }

                override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
                }

                override fun onActivityStopped(activity: Activity?) {
                }

                override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
                    AppManager.get()!!.addActivity(activity!!)
                }
            })
        }

        fun getInstance(): BaseApplication {
            return instanc
        }
    }


    override fun onCreate() {
        super.onCreate()
        instanc = this
        doCreated()
    }

    open fun doCreated() {
    }

    private var mFactory: ViewModelProvider.Factory? = null

    fun getAppViewModelProvider(): ViewModelProvider {
        return ViewModelProvider(this, this.getAppFactory())
    }

    private fun getAppFactory(): ViewModelProvider.Factory {
        if (mFactory == null) {
            mFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(this)
        }
        return mFactory as ViewModelProvider.Factory
    }
}